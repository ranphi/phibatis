/**
 *    Copyright 2019 ranphi@foxmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ranphi.phibatis.core.sql.wrapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import com.ranphi.phibatis.core.JpaCriteria;
import com.ranphi.phibatis.core.Page;
import com.ranphi.phibatis.core.exception.ParseSqlException;
import com.ranphi.phibatis.core.sql.SelectStatement;
import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public class SelectWrapper extends SqlWrapper {

	private static final SelectWrapper INSTANCE = new SelectWrapper();

	public static SelectWrapper builder() {
		return INSTANCE;
	}

	public String execute(StatementParameter statementParameter) {
		org.apache.ibatis.annotations.Select selectAnno = statementParameter.getMethod()
				.getAnnotation(org.apache.ibatis.annotations.Select.class);
		String annoVal = selectAnno.value()[0];
		if (!MapperPattern.MAPPER_SET.contains(annoVal)) {
			return null;
		}
		Class<?> entityClass = handleEntityClass(statementParameter);
		SelectStatement select = null;
		String sql = null;
		if (MapperPattern.ID.equals(annoVal)) {
			select = new SelectStatement(entityClass);
			select.setIdQuery(true);
			sql = select.toStatementString();
		} else if (MapperPattern.DEFAULT.equals(annoVal)) {
			Object paramObj = statementParameter.getParamObj();
			if (paramObj instanceof JpaCriteria) {
				JpaCriteria criteria = (JpaCriteria) paramObj;
				criteria.setEntityClass(entityClass);
				select = criteria.transToSelect();
			}
			sql = select.toStatementString();
			if (paramObj instanceof Page) {
				Page page = (Page) paramObj;
				if (page.isCount()) {
					try {
						long total = getCount(statementParameter, sql);
						page.setTotal(total);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				sql = select.getDialect().page(sql, page.getStartRow(), page.getPageSize());
			}
		}
		return sql;
	}

	private long getCount(StatementParameter boundParameter, String sql) throws SQLException {
		if (StrUtils.isBlank(sql)) {
			throw new ParseSqlException("sql can't be empty :" + sql);
		}
		Object paramObj = boundParameter.getParamObj();
		Configuration configuration = boundParameter.getConfiguration();
		MappedStatement mappedStatement = boundParameter.getMappedStatement();
		SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
		SqlSource sqlSource = builder.parse(sql, paramObj.getClass(), null);
		List<ParameterMapping> parameterMappings = sqlSource.getBoundSql(paramObj).getParameterMappings();
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, parameterMappings, paramObj);
		boundParameter.setBoundSql(countBoundSql);
		return count(boundParameter, sqlSource.getBoundSql(paramObj).getSql());
	}

}

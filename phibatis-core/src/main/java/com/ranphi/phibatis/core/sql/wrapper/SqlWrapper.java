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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import com.ranphi.phibatis.core.JpaCriteria;
import com.ranphi.phibatis.core.Page;
import com.ranphi.phibatis.core.PhibatisMapper;
import com.ranphi.phibatis.core.sql.SqlDialect;
import com.ranphi.phibatis.core.sql.SqlParser;
import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public class SqlWrapper {

	protected static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	protected static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	protected static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

	protected static final SqlParser SQL_PARSER = SqlParser.getInstance();
	protected static final Map<String, Method[]> MAPPER_METHOD_MAP = new ConcurrentHashMap<String, Method[]>();
	protected static final Map<String, Class<?>> MAPPER_ENTITY_MAP = new ConcurrentHashMap<String, Class<?>>();

	private static final SqlWrapper INSTANCE = new SqlWrapper();

	public static SqlWrapper builder() {
		return INSTANCE;
	}
	
	public MetaObject getMetaObject(Object object) {
		MetaObject metaObject = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
				DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		return metaObject;
	}
	
	protected String extractClassName(String sqlId) {
		return StrUtils.isBlank(sqlId) ? null : sqlId.substring(0, sqlId.lastIndexOf("."));
	}

	protected String extractMethedName(String sqlId) {
		return StrUtils.isBlank(sqlId) ? null : sqlId.substring(sqlId.lastIndexOf(".") + 1, sqlId.length());
	}

	protected Class<?> handleEntityClass(StatementParameter statementParameter) {
		Object paramObj = statementParameter.getParamObj();
		if (paramObj instanceof JpaCriteria) {
			JpaCriteria jpaCriteria = (JpaCriteria) paramObj;
			if (jpaCriteria.getEntityClass() != null) {
				statementParameter.setEntityClass(jpaCriteria.getEntityClass());
			}
		}
		return statementParameter.getEntityClass();
	}

	protected Class<?> getEntityClass(String mapperClassName, Class<?> mapperClass) {
		if (!MAPPER_ENTITY_MAP.containsKey(mapperClass)) {
			Type genericType = null;
			Type[] interfacesTypes = mapperClass.getGenericInterfaces();
			if (interfacesTypes != null && interfacesTypes.length > 0) {
				Type[] genericTypes = ((ParameterizedType) interfacesTypes[0]).getActualTypeArguments();
				if (genericTypes != null && genericTypes.length > 0) {
					genericType = genericTypes[0];
				}
			}
			if (genericType == null && mapperClass.isAnnotationPresent(PhibatisMapper.class)) {
				PhibatisMapper jpaMapper = (PhibatisMapper) mapperClass.getAnnotation(PhibatisMapper.class);
				genericType = jpaMapper.value();
			}
			if (genericType != null) {
				MAPPER_ENTITY_MAP.put(mapperClassName, (Class<?>) genericType);
			}
		}
		return MAPPER_ENTITY_MAP.get(mapperClassName);
	}

	protected long count(StatementParameter statementParameter, String sql) throws SQLException {
		String tsql = sql.toLowerCase().trim().replaceAll("\n", " ").replaceAll("\t", " ");
		if (tsql.endsWith(" desc") || tsql.endsWith(" asc")) {
			sql = sql.substring(0, tsql.lastIndexOf(" order ") + 1);
		}
		String countSql = "select count(*) count " + sql.substring(tsql.indexOf(" from ") + 1, sql.length());
		MappedStatement mappedStatement = statementParameter.getMappedStatement();
		Object paramObj = statementParameter.getParamObj();
		BoundSql countBoundSql = statementParameter.getBoundSql();
		Connection connection = statementParameter.getConnection();
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, paramObj, countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(countSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return 0;
	}

	public String execute(StatementParameter statementParameter) {
		BoundSql boundSql = statementParameter.getBoundSql();
		String sql = null;
		if (statementParameter.getParamObj() instanceof Page) {
			try {
				Page page = (Page) statementParameter.getParamObj();
				if (page.isCount()) {
					long count = count(statementParameter, boundSql.getSql());
					page.setTotal(count);
				}
				SqlDialect dialect = SQL_PARSER.getSqlProcessor().getDialect();
				sql = dialect.page(boundSql.getSql(), (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sql;
	}

	
}

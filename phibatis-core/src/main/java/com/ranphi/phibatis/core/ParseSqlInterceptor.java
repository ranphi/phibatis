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
package com.ranphi.phibatis.core;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import com.ranphi.phibatis.core.sql.wrapper.DeleteWrapper;
import com.ranphi.phibatis.core.sql.wrapper.InsertWrapper;
import com.ranphi.phibatis.core.sql.wrapper.SelectWrapper;
import com.ranphi.phibatis.core.sql.wrapper.SqlWrapper;
import com.ranphi.phibatis.core.sql.wrapper.StatementParameter;
import com.ranphi.phibatis.core.sql.wrapper.UpdateWrapper;

/**
 * 
 * @author Ranphi
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }))
public class ParseSqlInterceptor extends SqlWrapper implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof StatementHandler) {
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			MetaObject metaStatementHandler = getMetaObject(statementHandler);
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
			Object paramObj = boundSql.getParameterObject();
			Connection connection = (Connection) invocation.getArgs()[0];
			String sqlId = mappedStatement.getId();
			String mapperClassName = extractClassName(sqlId);
			if (!MAPPER_METHOD_MAP.containsKey(mapperClassName)) {
				Class<?> mapperClass = Class.forName(mapperClassName);
				MAPPER_METHOD_MAP.put(mapperClassName, mapperClass.getMethods());
				MAPPER_ENTITY_MAP.put(mapperClassName, getEntityClass(mapperClassName, mapperClass));
			}
			String sql = null;
			boolean updatedParam = true;
			Method[] methods = MAPPER_METHOD_MAP.get(mapperClassName);
			for (Method method : methods) {
				if (method.getName().equals(extractMethedName(sqlId))) {
					StatementParameter statementParameter = new StatementParameter();
					statementParameter.setMappedStatement(mappedStatement);
					statementParameter.setConfiguration(configuration);
					statementParameter.setConnection(connection);
					statementParameter.setParamObj(paramObj);
					statementParameter.setMethod(method);
					statementParameter.setBoundSql(boundSql);
					statementParameter.setEntityClass(MAPPER_ENTITY_MAP.get(mapperClassName));
					if (method.isAnnotationPresent(Insert.class)) {
						sql = InsertWrapper.builder().execute(statementParameter);
					} else if (method.isAnnotationPresent(Update.class)) {
						sql = UpdateWrapper.builder().execute(statementParameter);
					} else if (method.isAnnotationPresent(Delete.class)) {
						sql = DeleteWrapper.builder().execute(statementParameter);
					} else if (method.isAnnotationPresent(Select.class)) {
						sql = SelectWrapper.builder().execute(statementParameter);
					} else {
						sql = SqlWrapper.builder().execute(statementParameter);
						updatedParam = false;
					}
					break;
				}
			}
			
			if(sql != null){
				SqlSourceBuilder builder = new SqlSourceBuilder(configuration);
				SqlSource sqlSource = builder.parse(sql, paramObj.getClass(), null);
				metaStatementHandler.setValue("delegate.boundSql.sql", sqlSource.getBoundSql(paramObj).getSql());
				if(updatedParam){
					List<ParameterMapping> parameterMappings = sqlSource.getBoundSql(paramObj).getParameterMappings();
					metaStatementHandler.setValue("delegate.boundSql.parameterMappings", parameterMappings);
				}
			}
			
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}
	

}

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
import java.sql.Connection;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

/**
 * 
 * @author Ranphi
 */
public class StatementParameter {

	private MappedStatement mappedStatement;
	private Configuration configuration;
	private Connection connection;
	private BoundSql boundSql;
	private Object paramObj;
	private Method method;
	private Class<?> entityClass;

	public StatementParameter() {
	}

	public Object getParamObj() {
		return paramObj;
	}

	public void setParamObj(Object paramObj) {
		this.paramObj = paramObj;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public MappedStatement getMappedStatement() {
		return mappedStatement;
	}

	public void setMappedStatement(MappedStatement mappedStatement) {
		this.mappedStatement = mappedStatement;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public BoundSql getBoundSql() {
		return boundSql;
	}

	public void setBoundSql(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

}

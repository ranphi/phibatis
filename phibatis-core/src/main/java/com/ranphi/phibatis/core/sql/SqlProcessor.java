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
package com.ranphi.phibatis.core.sql;

/**
 * 
 * @author Ranphi
 */
public class SqlProcessor extends AbstractSqlProcessor {

	private SqlDialect dialect;
	
	public SqlProcessor(){
	}
	
	public SqlProcessor(SqlDialect dialect) {
		this.dialect = dialect;
	}
	
	@Override
	public String getPlaceholder(Class<?> type, String field){
		String jdbcTypeName = type.getName();
		String jdbcType = JdbcType.getJdbcType(jdbcTypeName);
		return "#{" + field+",jdbcType="+jdbcType+"}";
	}
	
	public SqlDialect getDialect() {
		return dialect;
	}

	public void setDialect(SqlDialect dialect) {
		this.dialect = dialect;
	}

}

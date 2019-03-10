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

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Ranphi
 */
public enum JdbcType {
	
	INT("int", "INTEGER"),
	LONG("long", "BIGINT"),
	FLOAT("float", "FLOAT"),
	DOUBLE("double", "DOUBLE"),
	BOOLEAN("boolean", "BOOLEAN"),
	SHORT("short", "SMALLINT"),
	BYTE("byte", "TINYINT"),
	CHAR("char", "CHAR"),
	BLOB("[B", "BLOB"),
	
	REF_INTEGER("java.lang.Integer", "INTEGER"),
	REF_LONG("java.lang.Long", "BIGINT"),
	REF_FLOAT("java.lang.Float", "INTEGER"),
	REF_DOUBLE("java.lang.Double", "DOUBLE"),
	REF_BOOLEAN("java.lang.Boolean", "BOOLEAN"),
	REF_SHORT("java.lang.Short", "SMALLINT"),
	REF_BYTE("java.lang.Byte", "TINYINT"),
	REF_STRING("java.lang.String", "VARCHAR"),
	REF_DATE("java.util.Date", "TIMESTAMP"),
	REF_BIGDECIMAL("java.math.BigDecimal", "NUMERIC");
	
	public static Map<String, String> JDBC_MAP = null;
	
	public static String getJdbcType(String prop){
		if(JDBC_MAP == null){
			JDBC_MAP = new HashMap<String, String>();
			for(JdbcType o : JdbcType.values()){
				JDBC_MAP.put(o.prop, o.jdbc);
			}
		}
		return JDBC_MAP.get(prop);
	}
	
	public String prop;
	
	public String jdbc;
	
	private JdbcType(){
	}
	
	private JdbcType(String prop, String jdbc){
		this.prop = prop;
		this.jdbc = jdbc;
	}

}

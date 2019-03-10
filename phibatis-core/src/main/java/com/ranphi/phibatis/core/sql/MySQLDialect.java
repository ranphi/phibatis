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
public class MySQLDialect implements SqlDialect {

	private static final MySQLDialect instance = new MySQLDialect();
	private static final String QUOT = "`";
	private boolean snakeCase = true;

	public static MySQLDialect of() {
		return instance;
	}

	public String escape(String key) {
		return QUOT + key + QUOT;
	}

	public boolean isSnakeCase() {
		return snakeCase;
	}

	public void setSnakeCase(boolean snakeCase) {
		this.snakeCase = snakeCase;
	}

	public String page(String sql, int start, int size) {
		StringBuilder t = new StringBuilder(sql);
		t.append(" limit ").append(start).append(",").append(size);
		return t.toString();
	}

	public String like(Object value, String placeholder) {
		String likePlaceholder = "";
		String val = String.valueOf(value);
		if (val.startsWith("%") && val.endsWith("%")) {
			likePlaceholder = "'%'," + placeholder + ",'%'";
		} else if (val.startsWith("%")) {
			likePlaceholder = "'%'," + placeholder;
		} else if (val.endsWith("%")) {
			likePlaceholder = placeholder + ",'%'";
		} else {
			likePlaceholder = placeholder;
		}
		return " CONCAT(" + likePlaceholder + ") ";
	}

}

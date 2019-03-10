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
public class SqlField {

	private String column;
	private String field;
	private Class<?> jdbcType;
	private boolean isId = false;
	private boolean isTransient = false;
	private boolean isLockVersion = false;
	private GenerationType strategy;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	public Class<?> getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(Class<?> jdbcType) {
		this.jdbcType = jdbcType;
	}

	public boolean isLockVersion() {
		return isLockVersion;
	}

	public void setLockVersion(boolean isLockVersion) {
		this.isLockVersion = isLockVersion;
	}

	public GenerationType getStrategy() {
		return strategy;
	}

	public void setStrategy(GenerationType strategy) {
		this.strategy = strategy;
	}
	
}

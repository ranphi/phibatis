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

import java.util.Map;
/**
 * 
 * @author Ranphi
 */
public class SqlEntity {

	private String table;
	private Class<?> clazz;
	private SqlField idSqlField;
	private String version;
	private Map<String, SqlField> sqlFieldMap;

	private String insertSql;
	private String insertBatchSql;
	private String updateSql;
	private String updateLockVersionSql;
	private String deleteSql;
	private String selectSql;
	
	private boolean hasColumnAlias = false;
	private boolean hasLockVersion = false;
	

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public SqlField getIdSqlField() {
		return idSqlField;
	}

	public void setIdSqlField(SqlField idSqlField) {
		this.idSqlField = idSqlField;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public Map<String, SqlField> getSqlFieldMap() {
		return sqlFieldMap;
	}

	public void setSqlFieldMap(Map<String, SqlField> sqlFieldMap) {
		this.sqlFieldMap = sqlFieldMap;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	
	public String getInsertBatchSql() {
		return insertBatchSql;
	}

	public void setInsertBatchSql(String insertBatchSql) {
		this.insertBatchSql = insertBatchSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getDeleteSql() {
		return deleteSql;
	}
	
	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public boolean isHasColumnAlias() {
		return hasColumnAlias;
	}

	public void setHasColumnAlias(boolean hasColumnAlias) {
		this.hasColumnAlias = hasColumnAlias;
	}

	public String getUpdateLockVersionSql() {
		return updateLockVersionSql;
	}

	public void setUpdateLockVersionSql(String updateLockVersionSql) {
		this.updateLockVersionSql = updateLockVersionSql;
	}

	public boolean isHasLockVersion() {
		return hasLockVersion;
	}

	public void setHasLockVersion(boolean hasLockVersion) {
		this.hasLockVersion = hasLockVersion;
	}

}

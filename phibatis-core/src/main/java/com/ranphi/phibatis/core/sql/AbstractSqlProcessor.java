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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ranphi.phibatis.core.util.FieldNameUtils;
import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public abstract class AbstractSqlProcessor {

	private final static String INSERT = "insert into %s (%s) values (%s) ";
	private final static String INSERT_BATCH = "insert into %s (%s) values (%s) ";
	private final static String UPDATE = "update %s set %s where %s ";
	private final static String UPDATE_LOCK_VERSION = "update %s set %s where %s ";
	private final static String DELETE = "delete from %s where %s ";
	private final static String SELECT = "select %s from %s where %s ";

	public abstract String getPlaceholder(Class<?> type, String field);

	public abstract SqlDialect getDialect();

	public String getPlaceholder(SqlField sqlField) {
		if (sqlField == null) {
			return "";
		}
		return getPlaceholder(sqlField.getJdbcType(), sqlField.getField());
	}

	public void parseSql(SqlEntity sqlEntity) {
		Map<String, SqlField> sqlFieldMap = sqlEntity.getSqlFieldMap();
		List<String> columnList = new LinkedList<String>();
		List<String> propertyJdbcTypeLists = new LinkedList<String>();
		List<String> columnPropertyList = new LinkedList<String>();
		List<String> lockVersionColumnPropertyList = new LinkedList<String>();
		List<SqlField> lockVersionList = new LinkedList<SqlField>();

		if (sqlFieldMap != null && sqlFieldMap.size() > 0) {
			for (String field : sqlFieldMap.keySet()) {
				SqlField sqlField = sqlFieldMap.get(field);
				if (!sqlField.isTransient()) {
					String column = getDialect().escape(sqlField.getColumn());
					String placeholder = getPlaceholder(sqlField);
					columnList.add(column);
					propertyJdbcTypeLists.add(placeholder);
					if (!sqlField.isId()) {
						columnPropertyList.add(column + " = " + placeholder);
						if (sqlField.isLockVersion()) {
							lockVersionColumnPropertyList.add(column + " = 1 + " + column);
							lockVersionList.add(sqlField);
						} else {
							lockVersionColumnPropertyList.add(column + " = " + placeholder);
						}
					}
				}
			}

			if (columnList.size() > 0) {
				initInsertSql(sqlEntity, columnList, propertyJdbcTypeLists);
				initInsertBatchSql(sqlEntity, columnList, propertyJdbcTypeLists);
				initUpdateSql(sqlEntity, columnPropertyList);
				initUpdateLockVersionSql(sqlEntity, lockVersionColumnPropertyList, lockVersionList);
				initDeleteSql(sqlEntity);
				initSelectSql(sqlEntity);
			}
		}

	}

	public void initInsertSql(SqlEntity entity, List<String> columnList, List<String> propertyList) {
		entity.setInsertSql(String.format(INSERT, getTable(entity), StrUtils.join(columnList, " , "),
				StrUtils.join(propertyList, " , ")));
	}

	public void initInsertBatchSql(SqlEntity entity, List<String> columnList, List<String> propertyJdbcTypeLists) {
		List<String> lists = new LinkedList<String>();
		for (String propertyJdbcType : propertyJdbcTypeLists) {
			int right = propertyJdbcType.indexOf(",jdbcType=");
			String str = "#{" + InsertStatement.BATCH_SIGN + "." + propertyJdbcType.substring(2, right)
					+ propertyJdbcType.substring(right, propertyJdbcType.length());
			lists.add(str);
		}
		entity.setInsertBatchSql(String.format(INSERT_BATCH, getTable(entity), StrUtils.join(columnList, " , "),
				StrUtils.join(lists, " , ")));
	}

	public void initUpdateSql(SqlEntity entity, List<String> columnPropertyList) {
		String where = getWhere(entity);
		if (StrUtils.isNotBlank(where)) {
			entity.setUpdateSql(
					String.format(UPDATE, getTable(entity), StrUtils.join(columnPropertyList, " , "), where));
		}
	}

	public void initUpdateLockVersionSql(SqlEntity entity, List<String> lockVersionColumnPropertyList,
			List<SqlField> lockVersionList) {
		String where = getWhere(entity);
		if (StrUtils.isNotBlank(where)) {
			for (SqlField sqlField : lockVersionList) {
				where += " and " + getDialect().escape(sqlField.getColumn()) + " = " + getPlaceholder(sqlField);
			}
			entity.setUpdateLockVersionSql(String.format(UPDATE_LOCK_VERSION, getTable(entity),
					StrUtils.join(lockVersionColumnPropertyList, " , "), where));
		}
	}

	public void initDeleteSql(SqlEntity entity) {
		String where = getWhere(entity);
		if (StrUtils.isNotBlank(where)) {
			entity.setDeleteSql(String.format(DELETE, getTable(entity), where));
		}
	}

	public void initSelectSql(SqlEntity entity) {
		String where = getWhere(entity);
		if (StrUtils.isNotBlank(where)) {
			entity.setSelectSql(String.format(SELECT, generateSelectClause(entity), getTable(entity), where));
		}
	}

	private String getWhere(SqlEntity entity) {
		SqlField sqlField = entity.getIdSqlField();
		if (sqlField != null) {
			return getDialect().escape(sqlField.getColumn()) + " = " + getPlaceholder(sqlField);
		}
		return "";
	}

	private String getTable(SqlEntity entity) {
		return getDialect().escape(entity.getTable());
	}

	private String generateSelectClause(SqlEntity entity) {
		List<String> selectClauseList = new LinkedList<String>();
		if (entity.isHasColumnAlias()) {
			Map<String, SqlField> sqlFieldMap = entity.getSqlFieldMap();
			for (String field : sqlFieldMap.keySet()) {
				SqlField sqlField = sqlFieldMap.get(field);
				if (!sqlField.isTransient()) {
					selectClauseList.add(sqlField.getColumn() + " as " + FieldNameUtils.snakeCase(sqlField.getField()));
				}
			}
		} else {
			selectClauseList.add("*");
		}
		return StrUtils.join(selectClauseList, " , ");
	}

}

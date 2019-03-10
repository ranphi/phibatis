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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ranphi.phibatis.core.annotation.Column;
import com.ranphi.phibatis.core.annotation.Id;
import com.ranphi.phibatis.core.annotation.LockVersion;
import com.ranphi.phibatis.core.annotation.Table;
import com.ranphi.phibatis.core.annotation.Transient;
import com.ranphi.phibatis.core.util.FieldNameUtils;
import com.ranphi.phibatis.core.util.ReflectionUtil;
import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public class SqlParser {

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(SqlParser.class);

	private AbstractSqlProcessor sqlProcessor;
	private static Map<Class<?>, SqlEntity> sqlEntityMap = new HashMap<Class<?>, SqlEntity>();
	private static SqlParser instance = new SqlParser();

	private SqlParser() {
	}

	public static SqlParser getInstance() {
		return instance;
	}

	public static String getTable(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			return table.name();
		}
		return null;
	}

	public SqlEntity parse(Class<?> clazz) {
		if (!sqlEntityMap.containsKey(clazz)) {
			sqlEntityMap.put(clazz, paserEntity(clazz));
		}
		SqlEntity sqlEntity = sqlEntityMap.get(clazz);
		sqlProcessor.parseSql(sqlEntity);
		return sqlEntityMap.get(clazz);
	}

	private SqlEntity paserEntity(Class<?> clazz) {
		SqlEntity sqlEntity = new SqlEntity();
		sqlEntity.setClazz(clazz);
		sqlEntity.setTable(getTable(clazz));
		boolean hasColumnAlias = false;
		boolean hasLockVersion = false;
		Map<String, SqlField> sqlFieldMap = new LinkedHashMap<String, SqlField>();
		Map<String, Field> fieldMap = ReflectionUtil.getDeclaredFields(clazz);
		SqlDialect dialect = sqlProcessor.getDialect();
		for (String name : fieldMap.keySet()) {
			Field field = fieldMap.get(name);
			SqlField sqlField = new SqlField();
			sqlField.setField(name);
			sqlField.setJdbcType(field.getType());
			String column = null;
			if (sqlEntity.getIdSqlField() == null && field.isAnnotationPresent(Id.class)) {
				sqlField.setId(true);
				Id IdAnno = field.getAnnotation(Id.class);
				if (IdAnno.strategy() != null) {
					sqlField.setStrategy(IdAnno.strategy());
				}
				if (StrUtils.isNotBlank(IdAnno.value())) {
					column = IdAnno.value();
				}
				sqlEntity.setIdSqlField(sqlField);
			}

			if (field.isAnnotationPresent(Column.class)) {
				String value = field.getAnnotation(Column.class).value();
				if (StrUtils.isNotBlank(value)) {
					column = value;
					hasColumnAlias = true;
				}
			}

			if (field.isAnnotationPresent(Transient.class)) {
				sqlField.setTransient(true);
			}

			if (field.isAnnotationPresent(LockVersion.class)) {
				sqlField.setLockVersion(true);
				hasLockVersion = true;
			}
			
			if (column == null) {
				column = dialect.isSnakeCase() ? FieldNameUtils.snakeCase(name) : name;
			}
			
			sqlField.setColumn(column);
			sqlFieldMap.put(name, sqlField);
		}
		sqlEntity.setHasColumnAlias(hasColumnAlias);
		sqlEntity.setHasLockVersion(hasLockVersion);
		sqlEntity.setSqlFieldMap(sqlFieldMap);
		return sqlEntity;
	}

	public void setSqlProcessor(AbstractSqlProcessor sqlProcessor) {
		this.sqlProcessor = sqlProcessor;
	}

	public AbstractSqlProcessor getSqlProcessor() {
		return sqlProcessor;
	}

	public Map<Class<?>, SqlEntity> getSqlEntityMap() {
		return sqlEntityMap;
	}
	
}

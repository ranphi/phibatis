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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ranphi.phibatis.core.exception.ParseSqlException;
import com.ranphi.phibatis.core.util.ReflectionUtil;
import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public class UpdateStatement extends FromJoinStatement {
	public final static String INCR_SIGN = "___INCR";
	private final static String[] SET_SIGNS = new String[]{INCR_SIGN, ""};
	
	private boolean isUpdateSelective = false;
	private boolean isLockVersion = false;
	private Object param;

	public UpdateStatement(Class<?> entityClass) {
		super(entityClass);
	}

	@SuppressWarnings("unchecked")
	public String toStatementString() {
		if (!isUpdateSelective) {
			if (isLockVersion) {
				return getSqlEntity().getUpdateLockVersionSql();
			}
			return getSqlEntity().getUpdateSql();
		} else {
			Map<String, Object> paramMap;
			if (param instanceof Map) {
				paramMap = (Map<String, Object>) param;
			} else {
				paramMap = (Map<String, Object>) ReflectionUtil.toMap(param);
			}
			List<SqlField> lockVersionList = new LinkedList<SqlField>();
			SqlBuilder builder = SqlBuilder.builder();
			builder.append("update").append(getFromJoin()).append("set");
			Map<String, Object> setJoinMap = new HashMap<String, Object>();
			boolean isUpdated = false;
			for(String property : paramMap.keySet()){
				property = property.replace(INCR_SIGN, "");
				Class<?> clazz = getClassByProperty(property);
				if(clazz != null){
					String prop = property;
					if(property.indexOf(".") > -1){
						prop = property.substring(property.indexOf(".") + 1, property.length());
					}
					SqlField sqlField = getSqlEntity(clazz).getSqlFieldMap().get(prop);
					if(whereClauses.size() == 0 && sqlField.isId()){
						continue;
					}
					if (!sqlField.isTransient()){
						String column = getColumn(property);
						String fieldPlaceholder = getFieldPlaceholder(paramMap, setJoinMap, sqlField, property, column);
						if (fieldPlaceholder != null) {
							if (isLockVersion && sqlField.isLockVersion()) {
								lockVersionList.add(sqlField);
								builder.append(column).append("= 1 +").append(column);
							} else {
								builder.append(column).append("=").append(fieldPlaceholder);
							}
							builder.append(" ,");
							isUpdated = true;
						}
					}
				}
			}
			if(!isUpdated){
				throw new ParseSqlException("No updatable fields");
			}
			if(setJoinMap.size() > 0){
				for(String property : setJoinMap.keySet()){
					paramMap.put(property, setJoinMap.get(property));
				}
			}
			
			builder.deleteCharAt(builder.length() - 2);
			if (param instanceof Map) {
				String where = generateWhere();
				if (StrUtils.isNotBlank(where)) {
					builder.append("where").append(where);
				}
			} else {
				SqlField idSqlField = getSqlEntity().getIdSqlField();
				builder.append("where")
					   .append(getDialect().escape(idSqlField.getColumn()))
					   .append("=")
					   .append(getProcessor().getPlaceholder(idSqlField));
			}

			if (isLockVersion) {
				for (SqlField sqlField : lockVersionList) {
					builder.append("and")
						   .append(getDialect().escape(sqlField.getColumn()))
						   .append("=")
						   .append(getProcessor().getPlaceholder(sqlField));
				}
			}
			return builder.toString();
		}
	}
	
	private String getFieldPlaceholder(Map<String, Object> paramMap, Map<String, Object> setJoinMap, SqlField sqlField, String property, String column){
		for(String sign : SET_SIGNS){
			String signKey = property + sign;
			Object value = paramMap.get(signKey);
			boolean essential = false;
			if(isUpdateSelective){
				essential = value != null;
			}else if(paramMap.containsKey(signKey)){
				essential = true;
			}
			if(essential){
				if(signKey.indexOf(".") > -1){
					signKey = property.replace(".", Statement.SIGN) + sign;
					setJoinMap.put(signKey, value);
				}
				String placeholder = "";
				if(INCR_SIGN.equals(sign)){
					placeholder = column + " + ";
				} 
				return placeholder + getProcessor().getPlaceholder(sqlField.getJdbcType(), signKey);
			}
		}
		return null;
	}
	

	public boolean isUpdateSelective() {
		return isUpdateSelective;
	}

	public void setUpdateSelective(boolean isUpdateSelective) {
		this.isUpdateSelective = isUpdateSelective;
	}

	public boolean isLockVersion() {
		return isLockVersion;
	}

	public void setLockVersion(boolean isLockVersion) {
		this.isLockVersion = isLockVersion;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

}

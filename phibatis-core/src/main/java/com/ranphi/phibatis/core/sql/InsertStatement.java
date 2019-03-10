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
import java.util.List;

import com.ranphi.phibatis.core.util.ReflectionUtil;
import com.ranphi.phibatis.core.util.StrUtils;
import com.ranphi.phibatis.core.util.UuidGenerator;

/**
 * 
 * @author Ranphi
 */
public class InsertStatement extends Statement {
	
	public final static String BATCH_SIGN = "%PLACE_HOLDER%";
	private boolean isBatch = false;
	private String listKey;
	private int listSize;
	private Object param;

	public InsertStatement(Class<?> entityClass) {
		super(entityClass);
	}
	
	@SuppressWarnings("rawtypes")
	public String toStatementString() {
		SqlEntity sqlEntity = getSqlEntity(entityClass);
		SqlField sqlField = sqlEntity.getIdSqlField();
		if(sqlField != null){
			GenerationType strategy = sqlField.getStrategy();
			if(strategy != null && strategy == GenerationType.UUID){
				Field field = ReflectionUtil.getDeclaredFields(entityClass).get(sqlField.getField());
				if(isBatch){
					List list = (List)param;
					for(Object o : list){
						ReflectionUtil.setValue(field, o, UuidGenerator.get());
					}
				}else{
					ReflectionUtil.setValue(field, param, UuidGenerator.get());
				}
			}
		}
		if(!isBatch) {
			return getSqlEntity().getInsertSql();
		}else{
			if(StrUtils.isBlank(listKey) && listSize == 0){
				throw new IllegalArgumentException("Illegal collection parameters");
			}
			String sql = getSqlEntity().getInsertBatchSql();
			int index = sql.indexOf(" values ");
			String intoPart = sql.substring(0, index);
			String valuesPart = sql.substring(index + 8);
			StringBuilder builder = new StringBuilder();
			builder.append(intoPart).append(" values ");
			for (int i = 0; i < listSize; i++) {
				builder.append(valuesPart.replaceAll(BATCH_SIGN, listKey + "[" + i + "]")).append(",");
			}
			return builder.deleteCharAt(builder.length() - 1).toString();
		}
	}

	public boolean isBatch() {
		return isBatch;
	}

	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

}

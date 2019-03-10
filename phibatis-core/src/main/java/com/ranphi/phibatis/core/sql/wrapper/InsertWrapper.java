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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ranphi.phibatis.core.exception.ParseSqlException;
import com.ranphi.phibatis.core.sql.InsertStatement;

/**
 * 
 * @author Ranphi
 */
public class InsertWrapper extends SqlWrapper {

	private static final InsertWrapper INSTANCE = new InsertWrapper();
	private final static String LIST_KEY = "list";
	
	public static InsertWrapper builder() {
		return INSTANCE;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String execute(StatementParameter statementParameter) {
		org.apache.ibatis.annotations.Insert selectAnno = statementParameter.getMethod().getAnnotation(org.apache.ibatis.annotations.Insert.class);
		String annoVal = selectAnno.value()[0];
		if(!MapperPattern.MAPPER_SET.contains(annoVal)){
			return null;
		}
		Object paramObj = statementParameter.getParamObj();
		InsertStatement insert = null;
		if (MapperPattern.DEFAULT.equals(annoVal)) {
			statementParameter.setEntityClass(paramObj.getClass());
			insert = new InsertStatement(statementParameter.getEntityClass());
			insert.setBatch(false);
			insert.setParam(paramObj);
		} else if (MapperPattern.BATCH.equals(annoVal)) {
			if (paramObj instanceof HashMap<?, ?>) {
				Map<String, Object> param = (Map<String, Object>) paramObj;
				if(param.size() != 2){
					throw new ParseSqlException("Parameter error of batch insertion, only one list set is allowed");
				}
				List list = (List)param.get(LIST_KEY);
				if(list == null || list.size() == 0){
					throw new ParseSqlException("Collection cannot be empty and it's size must be greater than 0");
				}
				statementParameter.setEntityClass(list.get(0).getClass());
				insert = new InsertStatement(statementParameter.getEntityClass());
				insert.setBatch(true);
				insert.setListKey(LIST_KEY);
				insert.setListSize(list.size());
				insert.setParam(list);
			}
		}
		return insert.toStatementString();
	}

}

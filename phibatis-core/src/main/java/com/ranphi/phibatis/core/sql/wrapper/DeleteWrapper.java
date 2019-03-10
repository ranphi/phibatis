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

import com.ranphi.phibatis.core.JpaCriteria;
import com.ranphi.phibatis.core.sql.DeleteStatement;
/**
 * 
 * @author Ranphi
 */
public class DeleteWrapper extends SqlWrapper {

	private static final DeleteWrapper INSTANCE = new DeleteWrapper();

	public static DeleteWrapper builder(){
		return INSTANCE;
	}
	
	public String execute(StatementParameter statementParameter){
		org.apache.ibatis.annotations.Delete selectAnno = statementParameter.getMethod().getAnnotation(org.apache.ibatis.annotations.Delete.class);
		String annoVal = selectAnno.value()[0];
		if(!MapperPattern.MAPPER_SET.contains(annoVal)){
			return null;
		}
		Class<?> entityClass = handleEntityClass(statementParameter);
		DeleteStatement delete = new DeleteStatement(entityClass);
		if(MapperPattern.ID.equals(annoVal)){
			delete.setIdDeleted(true);
		}else if(MapperPattern.DEFAULT.equals(annoVal)){
			delete.setIdDeleted(false);
			Object paramObj = statementParameter.getParamObj();
			if(paramObj instanceof JpaCriteria){
				JpaCriteria criteria = (JpaCriteria)paramObj; 
				delete.setWhereClauses(criteria.getWhereClauses());
				delete.setJoinClauses(criteria.getJoinClauses());
				delete.setDeleteClause(criteria.getDeleteClause());
				delete.setAlias(criteria.getAlias());
			}
		}
		delete.initAliasEntityClassMap();
		return delete.toStatementString();
	}
	
	
}

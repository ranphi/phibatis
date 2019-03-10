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

import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public class DeleteStatement extends FromJoinStatement {

	private boolean isIdDeleted = true;
	private String deleteClause;
	
	public DeleteStatement(Class<?> entityClass) {
		super(entityClass);
	}
	
	public String toStatementString() {
		if(isIdDeleted){
			return getSqlEntity().getDeleteSql();
		}
		
		SqlBuilder builder = SqlBuilder.builder().append("delete");
		if(joinClauses.size() == 0){
			builder.append("from").append(getFromClause());
		}else{
			if(StrUtils.isNotBlank(deleteClause)){
				builder.append(deleteClause);
			}else{
				builder.append(alias);
			}
			builder.append("from").append(getFromJoin());
		}
		String queryClause = generateWhere();
		if (StrUtils.isNotBlank(queryClause)) {
			builder.append("where").append(queryClause);
		}
		return builder.toString();
	}

	public boolean isIdDeleted() {
		return isIdDeleted;
	}

	public void setIdDeleted(boolean isIdDeleted) {
		this.isIdDeleted = isIdDeleted;
	}

	public String getDeleteClause() {
		return deleteClause;
	}

	public void setDeleteClause(String deleteClause) {
		this.deleteClause = deleteClause;
	}
	
}

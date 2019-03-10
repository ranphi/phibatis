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
public class FromJoinStatement extends Statement {

	public FromJoinStatement(Class<?> entityClass) {
		super(entityClass);
	}

	public String getFromJoin() {
		if (joinClauses.size() == 0) {
			return getFromClause();
		}

		SqlBuilder builder = SqlBuilder.builder().append(getDialect().escape(getTable()))
				.append(alias);

		for (JoinClause joinClause : joinClauses) {
			Class<?> joinClass = joinClause.getJoinEntityClass();
			SqlEntity sqlEntity = getSqlEntity(joinClass);
			builder.append(joinClause.getJoin()).append(getDialect().escape(sqlEntity.getTable()))
				   .append(joinClause.getAlias());
			if(StrUtils.isNotBlank(joinClause.getOnClause()) || joinClause.getOnClauseList() != null){
				String onclause = "";
				if(StrUtils.isNotBlank(joinClause.getOnClause())){
					onclause = transformToSql(joinClause.getOnClause());
				}
				if(joinClause.getOnClauseList() != null){
					for(WhereClause whereClause : joinClause.getOnClauseList()){
						onclause += super.parseWhereClause(whereClause);
					}
				}	    
				if(onclause.trim().startsWith("and ")){
					onclause = onclause.substring(4, onclause.length());
				}
				builder.append("on (").append(onclause).append(")");
			}
		}
		return builder.toString();
	}

	@Override
	public String toStatementString() {
		return null;
	}
	
}

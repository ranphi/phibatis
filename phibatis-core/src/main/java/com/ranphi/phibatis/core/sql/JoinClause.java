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

import java.util.List;

/**
 * @author Ranphi
 */
public class JoinClause {
	
	private String join;
	
	private Class<?> joinEntityClass;
	
	private String alias;
	
	private String onClause;
	
	private List<WhereClause> onClauseList;
	
	public JoinClause(){
		
	}
	
	public JoinClause(String join, Class<?> joinEntityClass, String alias){
		this.join = join;
		this.joinEntityClass = joinEntityClass;
		this.alias = alias;
	}


	public Class<?> getJoinEntityClass() {
		return joinEntityClass;
	}

	public void setJoinEntityClass(Class<?> joinEntityClass) {
		this.joinEntityClass = joinEntityClass;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getOnClause() {
		return onClause;
	}

	public void setOnClause(String onClause) {
		this.onClause = onClause;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public List<WhereClause> getOnClauseList() {
		return onClauseList;
	}

	public void setOnClauseList(List<WhereClause> onClauseList) {
		this.onClauseList = onClauseList;
	}
	
}

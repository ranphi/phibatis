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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import com.ranphi.phibatis.core.util.StrUtils;

/**
 * 
 * @author Ranphi
 */
public class SelectStatement extends FromJoinStatement {
	public Stack<Integer> bracketStack = new Stack<Integer>();
	public Map<String, String> specialMap = new LinkedHashMap<String, String>();
	
	private Boolean distinct = false;
	private String selectClause;
	private String orderByClause;
	private String groupByClause;
	private String havingClause;
	private String limitClause;
	private boolean isIdQuery = false;
	private String jql;

	public SelectStatement(Class<?> entityClass) {
		super(entityClass);
	}

	public String toStatementString() {
		if (isIdQuery) {
			return getSqlEntity().getSelectSql();
		}

		SqlBuilder builder = SqlBuilder.builder().append("select");
		if(distinct != null && distinct){
			builder.append("distinct");
		}
		
		if (StrUtils.isBlank(selectClause)) {
			builder.append("*");
		} else {
			builder.append(transformToSql(selectClause));
		}

		builder.append("from").append(getFromJoin());
		String where = generateWhere();
		if (StrUtils.isNotBlank(where)) {
			builder.append("where").append(where);
		}

		if (StrUtils.isNotBlank(groupByClause)) {
			builder.append("group by").append(transformToSql(groupByClause));
		}
		
		if (StrUtils.isNotBlank(havingClause)) {
			builder.append(transformToSql(havingClause));
		}

		if (StrUtils.isNotBlank(orderByClause)) {
			builder.append("order by").append(transformToSql(orderByClause));
		}
		
		if (StrUtils.isNotBlank(limitClause)) {
			builder.append(limitClause);
		}
		
		return builder.toString();
	}

	public String getSelectClause() {
		return selectClause;
	}

	public void setSelectClause(String selectClause) {
		this.selectClause = selectClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getGroupByClause() {
		return groupByClause;
	}

	public void setGroupByClause(String groupByClause) {
		this.groupByClause = groupByClause;
	}
	
	public String getHavingClause() {
		return havingClause;
	}

	public void setHavingClause(String havingClause) {
		this.havingClause = havingClause;
	}

	public String getLimitClause() {
		return limitClause;
	}

	public void setLimitClause(String limitClause) {
		this.limitClause = limitClause;
	}

	public boolean isIdQuery() {
		return isIdQuery;
	}

	public void setIdQuery(boolean isIdQuery) {
		this.isIdQuery = isIdQuery;
	}

	public Boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(Boolean distinct) {
		this.distinct = distinct;
	}

	public String getJql() {
		return jql;
	}

	public void setJql(String jql) {
		this.jql = jql;
	}
	
}

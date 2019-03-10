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
package com.ranphi.phibatis.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ranphi.phibatis.core.sql.ExistsClause;
import com.ranphi.phibatis.core.sql.JoinClause;
import com.ranphi.phibatis.core.sql.SelectStatement;
import com.ranphi.phibatis.core.sql.Statement;
import com.ranphi.phibatis.core.sql.UpdateStatement;
import com.ranphi.phibatis.core.sql.WhereClause;
import com.ranphi.phibatis.core.util.ReflectionUtil;


/**
 * 
 * @author Ranphi
 */
public class JpaCriteria extends Bootmap{
	private static final long serialVersionUID = -415143959347521711L;
	private static final String START = "start";
	private static final String END = "end";
	
	private Class<?> entityClass;
	private String alias;
	private List<JoinClause> joinClauses = new LinkedList<JoinClause>();
	private String where;
	private List<WhereClause> whereClauses = new LinkedList<WhereClause>();
	private List<ExistsClause> exists;
	
	private Boolean distinct = false;
	private String selectClause;
	private String orderByClause;
	private String groupByClause;
	private String havingClause;
	private String limitClause;
	private Class<?> resultType;
	private String deleteClause;
	private String jql;
	
	@SuppressWarnings("unchecked")
	public static JpaCriteria builder(Object bean){
		JpaCriteria criteria = builder();
		Map<String, Object> beanMap;
		if(bean instanceof Map){
			beanMap = (Map<String, Object>)bean;
		}else{
			criteria.setEntityClass(bean.getClass());
			beanMap = ReflectionUtil.toMap(bean);
		}
		for (Object key : beanMap.keySet()) {  
			criteria.put(String.valueOf(key), beanMap.get(key));
		}
		return criteria;
	}
	
	public static JpaCriteria builder(){
		return new JpaCriteria();
	}
	
	public static JpaCriteria builder(Class<?> entityClass){
		JpaCriteria jpaCriteria = new JpaCriteria();
		jpaCriteria.setEntityClass(entityClass);
		return jpaCriteria;
	}
	
	public JpaCriteria select(String selectClause) {
		this.selectClause = selectClause;
		return this;
	}
	
	public JpaCriteria delete(String deleteClause) {
		this.deleteClause = deleteClause;
		return this;
	}
	
	public JpaCriteria delete(Class<?> entityClass, String alias) {
		this.entityClass = entityClass;
		this.alias = alias;
		return this;
	}
	
	public JpaCriteria set(String field, Object value){
		this.put(field, value);
		return this;
	}
	
	public JpaCriteria incr(String field, Object value){
		this.put(field + UpdateStatement.INCR_SIGN, value);
		return this;
	}
	
	/**
	 * ne : not equal 
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria ne(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.ne(property, value));
	}
	
	public JpaCriteria ne(String property){
		Object value = this.get(property);
		if(value != null){
			ne(property, value);
		}
		return this;
	}
	
	public JpaCriteria neProp(String property1, String property2){
		return bindCriteriaByWhereClause(Sifter.neProp(property1, property2));
	}
	
	/**
	 * eq : equal 
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria eq(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.eq(property, value));
	}
	
	public JpaCriteria eq(String property){
		Object value = this.get(property);
		if(value != null){
			eq(property, value);
		}
		return this;
	}
	
	public JpaCriteria eqProp(String property1, String property2){
		return bindCriteriaByWhereClause(Sifter.eqProp(property1, property2));
	}
	
	/**
	 * like clause ： like  %abc%
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria like(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.like(property, value));
	}
	
	public JpaCriteria like(String property){
		Object value = this.get(property);
		if(value != null){
			like(property, value);
		}
		return this;
	}
	
	/**
	 * like clause ：like abc% 
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria leftLike(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.leftLike(property, value));
	}
	
	public JpaCriteria leftLike(String property){
		Object value = this.get(property);
		if(value != null){
			leftLike(property, value);
		}
		return this;
	}
	
	/**
	 * like clause ：like %abc 
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria rightLike(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.rightLike(property, value));
	}
	
	public JpaCriteria rightLike(String property){
		Object value = this.get(property);
		if(value != null){
			rightLike(property, value);
		}
		return this;
	}
	
	/**
	 * gt : great than
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria gt(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.gt(property, value));
	}
	
	public JpaCriteria gt(String property){
		Object value = this.get(property);
		if(value != null){
			gt(property, value);
		}
		return this;
	}
	
	public JpaCriteria gtProp(String property1, String property2){
		return bindCriteriaByWhereClause(Sifter.gtProp(property1, property2));
	}
	
	/**
	 * ge : great equal
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria ge(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.ge(property, value));
	}
	
	public JpaCriteria ge(String property){
		Object value = this.get(property);
		if(value != null){
			ge(property, value);
		}
		return this;
	}
	
	public JpaCriteria geProp(String property1, String property2){
		return bindCriteriaByWhereClause(Sifter.geProp(property1, property2));
	}
	
	/**
	 * lt : less than 
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria lt(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.lt(property, value));
	}
	
	public JpaCriteria lt(String property){
		Object value = this.get(property);
		if(value != null){
			lt(property, value);
		}
		return this;
	}
	
	public JpaCriteria ltProp(String property1, String property2){
		return bindCriteriaByWhereClause(Sifter.ltProp(property1, property2));
	}
	
	/**
	 * le : less equal 
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria le(String property, Object value){
		return bindCriteriaByWhereClause(Sifter.le(property, value));
	}
	
	public JpaCriteria le(String property){
		Object value = this.get(property);
		if(value != null){
			le(property, value);
		}
		return this;
	}
	
	public JpaCriteria leProp(String property1, String property2){
		return bindCriteriaByWhereClause(Sifter.leProp(property1, property2));
	}
	
	/**
	 * is null
	 * @param property
	 * @return
	 */
	public JpaCriteria isNull(String property){
		return bindCriteriaByWhereClause(Sifter.isNull(property));
	}
	
	/**
	 * is not null
	 * @param property
	 * @return
	 */
	public JpaCriteria isNotNull(String property){
		return bindCriteriaByWhereClause(Sifter.isNotNull(property));
	}
	
	/**
	 * in clause
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria in(String property, Object[] value){
		return bindCriteriaByWhereClause(Sifter.in(property, value));
	}
	
	/**
	 * not in clause
	 * @param property
	 * @param value
	 * @return
	 */
	public JpaCriteria notIn(String property, Object[] value){
		return bindCriteriaByWhereClause(Sifter.notIn(property, value));
	}
	
	/**
	 * between clause
	 * @param property
	 * @param min 
	 * @param max
	 * @return
	 */
	public JpaCriteria between(String property, Object min, Object max){
		return bindCriteriaByWhereClause(Sifter.between(property, min, max));
	}
	
	public JpaCriteria between(String property){
		String propertyUpperCase = convertFirstPlaceToUpperCase(property);
		Object min = this.get(START + propertyUpperCase);
		Object max = this.get(END + propertyUpperCase);
		if(min != null && max != null){
			between(property, min, max);
		}else if(min != null){
			ge(property, min);
		}else if(max != null){
			le(property, max);
		}
		return this;
	}
	
	private String convertFirstPlaceToUpperCase(String property){
		String propertyUpperCase = property.substring(0, 1).toUpperCase() + property.substring(1);
		return propertyUpperCase;
	}
	
	public JpaCriteria or(WhereClause... clauses){
		WhereClause whereClause = new WhereClause();
		whereClause.setOperate("or");
		whereClause.setSubWhereClauses(new LinkedList<WhereClause>());
		for(WhereClause subWhereClause : clauses){
			bindCriteriaByWhereClause(whereClause.getSubWhereClauses(), subWhereClause);
		}
		whereClauses.add(whereClause);
		return this;
	}
	
	private JpaCriteria bindCriteriaByWhereClause(WhereClause whereClause){
		return bindCriteriaByWhereClause(whereClauses, whereClause);
	}
	
	private JpaCriteria bindCriteriaByWhereClause(List<WhereClause> whereClauseList, WhereClause whereClause){
		whereClauseList.add(whereClause);
		String property = whereClause.getProperty();
		if("between".equals(whereClause.getCompare())){
			String minPlaceholder = getPlaceholder(Statement.MIN + property);
			String maxPlaceholder = getPlaceholder(Statement.MAX + property);
			whereClause.setMinPlaceholder(minPlaceholder);
			whereClause.setMaxPlaceholder(maxPlaceholder);
			this.put(Statement.sign(minPlaceholder), whereClause.getMin());
			this.put(Statement.sign(maxPlaceholder), whereClause.getMax());
			return this;
		}else{
			String placeholder = getPlaceholder(property);
			whereClause.setPlaceholder(placeholder);
			this.put(Statement.sign(placeholder), whereClause.getValue());
			return this;
		}
	}
	
	public JpaCriteria from(Class<?> entityClass, String alias){
		this.entityClass = entityClass;
		this.alias = alias;
		return this;
	}
	
	public JpaCriteria leftJoin(Class<?> joinClazz, String alias){
		return this.join("left join", joinClazz, alias);
	}
	
	public JpaCriteria rightJoin(Class<?> joinClazz, String alias){
		return this.join("right join", joinClazz, alias);
	}
	
	public JpaCriteria innerJoin(Class<?> joinClazz, String alias){
		return this.join("inner join", joinClazz, alias);
	}
	
	public JpaCriteria join(String join, Class<?> joinEntityClass, String alias){
		joinClauses.add(new JoinClause(join, joinEntityClass, alias));
		return this;
	}
	
	public JpaCriteria on(String onClause){
		JoinClause joinClause = joinClauses.get(joinClauses.size() - 1);
		joinClause.setOnClause(onClause);
		return this;
	}
	
	public JpaCriteria on(WhereClause... whereClauses){
		JoinClause joinClause = joinClauses.get(joinClauses.size() - 1);
		List<WhereClause> onClauseList = joinClause.getOnClauseList();
		if(onClauseList == null){
			onClauseList = new LinkedList<WhereClause>();
		}
		for(WhereClause whereClause : whereClauses){
			if(!whereClause.isValIsProp()){
				String property = whereClause.getProperty();
				String placeholder = getPlaceholder(property);
				whereClause.setPlaceholder(placeholder);
				this.put(Statement.sign(placeholder), whereClause.getValue());
			}
			onClauseList.add(whereClause);
		}
		joinClause.setOnClauseList(onClauseList);
		return this;
	}
	
	public JpaCriteria exists(JpaCriteria... criterias){
		return buildExists(false, criterias);
	}
	
	public JpaCriteria notExists(JpaCriteria... criterias){
		 return buildExists(false, criterias);
	}
	
	private JpaCriteria buildExists(boolean isNot, JpaCriteria... criterias){
		if(exists == null){
			exists = new LinkedList<ExistsClause>();
		}
		for(JpaCriteria criteria : criterias){
			criteria.select("1");
			ExistsClause existsClause = new ExistsClause();
			existsClause.setNot(isNot);
			existsClause.setStatement(criteria.transToSelect());
			exists.add(existsClause);
		}
		return this;
	}
	
	public SelectStatement transToSelect(){
		SelectStatement select = new SelectStatement(this.getEntityClass());
		select.setSelectClause(this.getSelectClause());
		select.setDistinct(this.isDistinct());
		select.setWhereClauses(this.getWhereClauses());
		select.setAlias(this.getAlias());
		select.setJoinClauses(this.getJoinClauses());
		select.setWhere(this.getWhere());
		select.setExists(this.getExists());
		select.setOrderByClause(this.getOrderByClause());
		select.setGroupByClause(this.getGroupByClause());
		select.setHavingClause(this.getHavingClause());
		select.setLimitClause(this.getLimitClause());
		select.setJql(this.getJql());
		select.initAliasEntityClassMap();
		if(select.getExists() != null && select.getExists().size() > 0){
			for(ExistsClause existsClause : select.getExists()){
				Statement statement = existsClause.getStatement();
				statement.initAliasEntityClassMap();
				Map<String, Class<?>> aliasMap = select.getAliasEntityClassMap();
				for(String alias : aliasMap.keySet()){
					statement.getAliasEntityClassMap().put(alias, aliasMap.get(alias));
				}
			}
		}
		return select;
	}
	
	public UpdateStatement transToUpdate(){
		UpdateStatement update = new UpdateStatement(this.getEntityClass());
		update.setWhereClauses(this.getWhereClauses());
		update.setAlias(this.getAlias());
		update.setJoinClauses(this.getJoinClauses());
		update.setWhere(this.getWhere());
		update.setExists(this.getExists());
		update.initAliasEntityClassMap();
		if(update.getExists() != null && update.getExists().size() > 0){
			for(ExistsClause existsClause : update.getExists()){
				Statement statement = existsClause.getStatement();
				statement.initAliasEntityClassMap();
				Map<String, Class<?>> aliasMap = update.getAliasEntityClassMap();
				for(String alias : aliasMap.keySet()){
					statement.getAliasEntityClassMap().put(alias, aliasMap.get(alias));
				}
			}
		}
		return update;
	}
	
	private String getPlaceholder(String property){
		if(property.indexOf(".") > -1){
			property = property.replace(".", "_");
		}
		if(this.containsKey(Statement.sign(property))){
			return getPlaceholder(1, property);
		}else{
			return property;
		}
	}
	
	private String getPlaceholder(int index,String property){
		String key = property + index;
		if(this.containsKey(Statement.sign(key))){
			getPlaceholder(++index, property);
		}
		return key;
	}
	
	public List<ExistsClause> getExists() {
		return exists;
	}

	public List<WhereClause> getWhereClauses() {
		return whereClauses;
	}

	public void setWhereClauses(List<WhereClause> whereClauses) {
		this.whereClauses = whereClauses;
	}

	public Boolean isDistinct() {
		return distinct;
	}

	public JpaCriteria distinct(Boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public JpaCriteria orderBy(String orderByClause) {
		this.orderByClause = orderByClause;
		return this;
	}
	
	public String getGroupByClause() {
		return groupByClause;
	}

	public JpaCriteria groupBy(String groupByClause) {
		this.groupByClause = groupByClause;
		return this;
	}
	
	public String getSelectClause() {
		return selectClause;
	}
	
	
	public String getJql() {
		return jql;
	}


	public String getDeleteClause() {
		return deleteClause;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<?> getResultType() {
		return resultType;
	}

	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getWhere() {
		return where;
	}

	public JpaCriteria where(String where) {
		this.where = where;
		return this;
	}
	
	public String getHavingClause() {
		return havingClause;
	}

	public JpaCriteria having(String havingClause) {
		this.havingClause = "having " +havingClause;
		return this;
	}
	
	public JpaCriteria limit(int size) {
		this.limitClause = "limit " + size;
		return this;
	}

	public JpaCriteria limit(int start, int size) {
		this.limitClause = "limit " + start + "," + size;
		return this;
	}
	
	public String getLimitClause() {
		return limitClause;
	}

	public void setLimitClause(String limitClause) {
		this.limitClause = limitClause;
	}

	public List<JoinClause> getJoinClauses() {
		return joinClauses;
	}

	public void setJoinClauses(List<JoinClause> joinClauses) {
		this.joinClauses = joinClauses;
	}
	
}

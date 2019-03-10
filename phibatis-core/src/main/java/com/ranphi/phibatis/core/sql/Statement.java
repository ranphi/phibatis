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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ranphi.phibatis.core.exception.ParseSqlException;
import com.ranphi.phibatis.core.util.StrUtils;

/**
 * @author Ranphi
 */
public abstract class Statement {
	
	public final static String SIGN = "___";
	public final static String MIN = "min";
	public final static String MAX = "max";
	public final static String BLANK = " ";
	final static List<String> KEYS = new LinkedList<String>();
	
	protected Class<?> entityClass;
	protected String alias = "";
	protected List<JoinClause> joinClauses = new LinkedList<JoinClause>();
	protected List<WhereClause> whereClauses = new LinkedList<WhereClause>();
	private List<ExistsClause> exists;
	protected Map<String, Class<?>> aliasEntityClassMap = new LinkedHashMap<String, Class<?>>();
	private String where;
	
	public abstract String toStatementString();
	
	public Statement(Class<?> entityClass){
		this.entityClass = entityClass;
	}
	
	public SqlParser getParser(){
		return SqlParser.getInstance();
	}
	
	public AbstractSqlProcessor getProcessor(){
		return getParser().getSqlProcessor();
	}
	
	public SqlDialect getDialect(){
		return getParser().getSqlProcessor().getDialect();
	}
	
	public String getTable(){
		return getSqlEntity().getTable();
	}
	
	public SqlEntity getSqlEntity(Class<?> clazz){
		SqlEntity sqlEntity = getParser().getSqlEntityMap().get(clazz);
		if(sqlEntity == null){
			throw new ParseSqlException("Entity has not been scanned or Mapper corresponding Entity has not been found");
		}
		return sqlEntity;
	}
	
	public SqlEntity getSqlEntity(){
		return getSqlEntity(entityClass);
	}
	
	public String getColumn(Class<?> clazz, String property){
		if("*".trim().equals(property)){
			return property;
		}
		SqlField sqlField = getSqlEntity(clazz).getSqlFieldMap().get(property);
		if(sqlField == null || sqlField.isTransient()){
			return property;
		}
		return getDialect().escape(sqlField.getColumn());
	}
	
	public String getColumn(String property){
		String prop = property;
		String alias = "";
		String column = "";
		if(StrUtils.isNotBlank(property)){
			Class<?> clazz = getClassByProperty(property);
			if(clazz != null){
				if(property.indexOf(".") > -1){
					alias = property.substring(0, property.indexOf("."));
					prop = property.substring(property.indexOf(".") + 1, property.length());
				}
				column = getColumn(clazz, prop);
			}
		}
		if(StrUtils.isBlank(column)){
			column = property;
		}
		return StrUtils.isNotBlank(alias) ? alias + "." + column : column;
	}
	
	public Class<?> getClassByProperty(String property){
		Class<?> propClazz = null;
		if(StrUtils.isNotBlank(property)){
			if(property.indexOf(".") > -1){
				String al = property.substring(0, property.indexOf("."));
				propClazz = aliasEntityClassMap.get(al.trim());
			}else{
				boolean exists = false;
				for(String al : aliasEntityClassMap.keySet()){
					Class<?> clazz = aliasEntityClassMap.get(al);
					SqlField sqlField = getSqlEntity(clazz).getSqlFieldMap().get(property);
					if(sqlField != null){
						if(exists){
							throw new ParseSqlException("Property '" + property + "' in field list is ambiguous");
						}
						exists = true;
						propClazz = clazz;
					}
				}
			}
		}
		return propClazz;
	}
	
	public String getFromClause(){
		return getDialect().escape(getTable()) + (StrUtils.isNotBlank(alias) ? BLANK + alias : "");
	}
	
	public String generateWhere() {
		SqlBuilder builder = SqlBuilder.builder();
		if(StrUtils.isNotBlank(where)){
			builder.append("and").append(transformToSql(where));
		}
		if(whereClauses != null && whereClauses.size() > 0){
			for (int i = 0; i< whereClauses.size(); i++) {
				WhereClause queryClause = whereClauses.get(i);
				if("and".equals(queryClause.getOperate().toLowerCase())){
					String sql = parseWhereClause(queryClause);
					builder.append(sql);
				}else if("or".equals(queryClause.getOperate().toLowerCase())){
					List<WhereClause> subQueryClauseList = queryClause.getSubWhereClauses();
					if(subQueryClauseList.size() > 0){
						builder.append("and (");
						for(int j = 0; j < subQueryClauseList.size(); j++){
							WhereClause subQueryClause = subQueryClauseList.get(j);
							subQueryClause.setOperate(j == 0 ? "" : "or");
							String sql = parseWhereClause(subQueryClause);
							builder.append(sql);
						}
						builder.append(")");
					}
				}
			}
		}
		
		List<ExistsClause> existsList = getExists();
		if(existsList != null && existsList.size() > 0){
			for(ExistsClause existsClause : existsList){
				Statement statement = existsClause.getStatement();
				builder.append("and")
				.append(existsClause.isNot() ? "not" : "")
				.append("exists (")
				.append(statement.toStatementString())
				.append(")");
			}
		}
		
		if(builder.length() > 0){
			return builder.substring(4, builder.length());
		}
		return null;
	}
	
	public String parseWhereClause(WhereClause whereClause){
		String operate = whereClause.getOperate();
		String property = StrUtils.trim(whereClause.getProperty());
		String placeholder = StrUtils.trim(whereClause.getPlaceholder());
		String compare = whereClause.getCompare().toLowerCase();
		Object value = whereClause.getValue();
		boolean isValIsProp = whereClause.isValIsProp();
		SqlEntity sqlEntity = getSqlEntity();
		SqlField sqlField = sqlEntity.getSqlFieldMap().get(property.substring(property.indexOf(".") + 1));
		String column = getColumn(property);
		SqlBuilder builder = SqlBuilder.builder().append(operate).append(column);
		if(isValIsProp){
			String property2 = getColumn(String.valueOf(value));
			builder.append(compare).append(property2);
		}else{
			if(placeholder.indexOf(".") > -1){
				placeholder = placeholder.replace(".", "_");
			}
			placeholder = getProcessor().getPlaceholder(sqlField.getJdbcType(), sign(placeholder));
			if ("like".equals(compare)) {
				builder.append("like").append(getDialect().like(value, placeholder));
			} else if ("between".equals(compare)) {
				String minPlaceholder = getProcessor().getPlaceholder(sqlField.getJdbcType(), sign(whereClause.getMinPlaceholder()));
				String maxPlaceholder = getProcessor().getPlaceholder(sqlField.getJdbcType(), sign(whereClause.getMaxPlaceholder()));
				builder.append("between").append(minPlaceholder).append("and").append(maxPlaceholder);
			} else if ("is".equals(compare) || "in".equals(compare) || "not in".equals(compare)) {
				builder.append(compare).append(value);
			} else{
				builder.append(compare).append(placeholder);
			}
		}
		return builder.toString();
	}

	public String transformToSql(String sqlClause){
		sqlClause = BLANK + sqlClause + BLANK;
		StringBuffer buffer = new StringBuffer();
		int cursor = 0;
		int strQuantity = 0;
		boolean isPropAliasOpening = false;
		char[] chars = sqlClause.toCharArray();
		for (int index = 0; index < chars.length; index++) {
			char ch = chars[index];
			if(ch == '\''){
				strQuantity++;
			}
			if(!isPropertyChar(ch)){
				if(index - cursor > 1){
					String value = sqlClause.substring(cursor + 1, index);
					if(ch == '\'' || ch == '('){
						buffer.append(value);
					}else{
						if(strQuantity % 2 == 1){
							buffer.append(value);
						}else{
							if(isPropAliasOpening){
								buffer.append(value);
							}else{
								if(KEYS.contains(value.toLowerCase())){
									buffer.append(value);
								}else{
									buffer.append(getColumn(value));
								}
							}
						}
					}
				}
				buffer.append(ch);
				cursor = index;
				if(ch != ' '){
					isPropAliasOpening = false;
				}
			}
			if(ch == ' ' && index > 0){
				char lastChar = chars[index - 1];
				if(lastChar == ')' || isPropertyChar(lastChar) || lastChar == '\'' && strQuantity % 2 == 0){
					isPropAliasOpening = true;
				}
			}
		}
		return buffer.toString();
	}
	
	public static boolean isPropertyChar(char ch) {
		return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9' 
				|| ch == '$' || ch == '_' || ch == '.' || ch == '*';
	}
	
	public void initAliasEntityClassMap(){
		alias = alias == null ? "" : alias.trim();
		aliasEntityClassMap.put(alias, entityClass);
		if(joinClauses.size() > 0){
			aliasEntityClassMap.remove("");
			for(JoinClause joinClause : joinClauses){
				String alias = joinClause.getAlias();
				if(alias == null){
					throw new ParseSqlException("alias of entityClass[" + joinClause.getJoinEntityClass() + "] is null");
				}
				alias = alias.trim();
				if(aliasEntityClassMap.containsKey(alias)){
					throw new ParseSqlException("duplicate alias of entityClass name '"+ alias +"'");
				}
				aliasEntityClassMap.put(alias, joinClause.getJoinEntityClass());
			}
		}
	}
	
	public List<WhereClause> getWhereClauses() {
		return whereClauses;
	}

	public void setWhereClauses(List<WhereClause> whereClauses) {
		this.whereClauses = whereClauses;
	}
	
	public static String sign(String property){
		property = property.startsWith(".") ? "_" + property.substring(1) : property;
		return SIGN + property;
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

	public void setWhere(String where) {
		this.where = where;
	}
	
	public List<JoinClause> getJoinClauses() {
		return joinClauses;
	}

	public void setJoinClauses(List<JoinClause> joinClauses) {
		this.joinClauses = joinClauses;
	}
	
	public Map<String, Class<?>> getAliasEntityClassMap() {
		return aliasEntityClassMap;
	}

	public void setAliasEntityClassMap(Map<String, Class<?>> aliasEntityClassMap) {
		this.aliasEntityClassMap = aliasEntityClassMap;
	}
	
	public List<ExistsClause> getExists() {
		return exists;
	}

	public void setExists(List<ExistsClause> exists) {
		this.exists = exists;
	}
	
	static {
		KEYS.add("select");
		KEYS.add("distinct");
		KEYS.add("as");
		KEYS.add("from");
		KEYS.add("where");
		KEYS.add("and");
		KEYS.add("or");
		KEYS.add("exists");
		KEYS.add("order");
		KEYS.add("asc");
		KEYS.add("desc");
		KEYS.add("group");
		KEYS.add("limit");
	}
	
}

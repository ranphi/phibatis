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

import com.ranphi.phibatis.core.sql.WhereClause;

/**
 * 
 * @author Ranphi
 */
public class Sifter {
	
	
	private static WhereClause build(String property, String compare, Object value){
		property = property.trim();
		WhereClause whereClause = new WhereClause("and", property, compare, value);
		return whereClause;
	}
	
	/**
	 * ne : not equal to
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause ne(String property, Object value){
		return build(property, "!=", value);
	}
	
	public static WhereClause neProp(String property1, String property2){
		WhereClause clause = ne(property1, property2);
		clause.setValIsProp(true);
		return clause;
	}
	
	/**
	 * eq : equal to
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause eq(String property, Object value){
		return build(property, "=", value);
	}
	
	
	public static WhereClause eqProp(String property1, String property2){
		WhereClause clause = eq(property1, property2);
		clause.setValIsProp(true);
		return clause;
	}
	
	/**
	 * like clause ： like  %abc%
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause like(String property, Object value){
		return build(property, "like", "%" + value + "%");
	}
	
	/**
	 * like clause ：like abc% 
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause leftLike(String property, Object value){
		return build(property, "like", value + "%");
	}
	
	/**
	 * like clause ：like %abc 
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause rightLike(String property, Object value){
		return build(property, "like", "%" + value);
	}
	

	/**
	 * gt : great than
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause gt(String property, Object value){
		return build(property, ">", value);
	}
	
	
	public static WhereClause gtProp(String property1, String property2){
		WhereClause clause = gt(property1, property2);
		clause.setValIsProp(true);
		return clause;
	}
	
	/**
	 * ge : great than and equal to
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause ge(String property, Object value){
		return build(property, ">=", value);
	}
	 
	
	public static WhereClause geProp(String property1, String property2){
		WhereClause clause = ge(property1, property2);
		clause.setValIsProp(true);
		return clause;
	}
	
	/**
	 * lt : less than 
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause lt(String property, Object value){
		return build(property, "<", value);
	}
	
	public static WhereClause ltProp(String property1, String property2){
		WhereClause clause = lt(property1, property2);
		clause.setValIsProp(true);
		return clause;
	}
	
	/**
	 * le : less than and equal to
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause le(String property, Object value){
		return build(property, "<=", value);
	}
	
	
	public static WhereClause leProp(String property1, String property2){
		WhereClause clause = le(property1, property2);
		clause.setValIsProp(true);
		return clause;
	}
	
	/**
	 * is null
	 * @param property
	 * @return
	 */
	public static WhereClause isNull(String property){
		return build(property, "is", "null");
	}
	
	/**
	 * is not null
	 * @param property
	 * @return
	 */
	public static WhereClause isNotNull(String property){
		return build(property, "is", "not null");
	}
	
	
	/**
	 * in clause
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause in(String property, Object[] value){
		return inClause(property, "in", value);
	}
	
	
	/**
	 * not in clause
	 * @param property
	 * @param value
	 * @return
	 */
	public static WhereClause notIn(String property, Object[] value){
		return inClause(property, "not in", value);
	}
	
	
	public static WhereClause inClause(String property, String compare, Object[] value){
		String express = "";
		if(value != null && value.length > 0){
			StringBuffer strBuilder = new StringBuffer();
			for(Object val : value){
				String str = String.valueOf(val);
				if(val instanceof String){
					str = str.replaceAll(".*([';]+|(--)+).*","");
					strBuilder.append("'").append(str).append("',");
				}else{
					strBuilder.append(str).append(",");
				}
			}
			if(strBuilder.length() > 0){
				express = strBuilder.deleteCharAt(strBuilder.length() - 1).toString();
			}
		}
		return build(property, compare, "(" + express  + ")");
	}
	
	
	/**
	 * between clause
	 * @param property
	 * @param min 
	 * @param max
	 * @return
	 */
	public static WhereClause between(String property, Object min, Object max){
		return new WhereClause("and", property, "between", min, max);
	}
	
	
}

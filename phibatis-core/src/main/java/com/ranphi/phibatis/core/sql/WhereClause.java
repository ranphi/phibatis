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
 * 
 * @author Ranphi
 */
public class WhereClause {
	
	public WhereClause(){
	}
	
	private List<WhereClause> subWhereClauses;
	
	public WhereClause(String operate, String property, String compare,
			Object value) {
		this.operate = operate;
		this.property = property;
		this.compare = compare;
		this.value = value;
	}
	
	public WhereClause(String operate, String property, String compare,
			Object min, Object max) {
		this.operate = operate;
		this.property = property;
		this.compare = compare;
		this.min = min;
		this.max = max;
	}
	
	protected boolean valIsProp = false;
	/**
	 * field attribute
	 */
	protected String property;
	/**
	 * comparing operational logic (> , == , < ... )
	 */
	protected String compare;
	/**
	 * operational operational logic （and , or , in ...）
	 */
	protected String operate;
	/**
	 * field value
	 */
	protected Object value;
	/**
	 * minimum value of between
	 */
	protected Object min;
	/**
	 * maximum value of between
	 */
	protected Object max;
	
	protected String placeholder;
	
	protected String minPlaceholder;
	
	protected String maxPlaceholder;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getMin() {
		return min;
	}

	public void setMin(Object min) {
		this.min = min;
	}

	public Object getMax() {
		return max;
	}

	public void setMax(Object max) {
		this.max = max;
	}

	public String getCompare() {
		return compare;
	}

	public void setCompare(String compare) {
		this.compare = compare;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<WhereClause> getSubWhereClauses() {
		return subWhereClauses;
	}

	public void setSubWhereClauses(List<WhereClause> subWhereClauses) {
		this.subWhereClauses = subWhereClauses;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getMinPlaceholder() {
		return minPlaceholder;
	}

	public void setMinPlaceholder(String minPlaceholder) {
		this.minPlaceholder = minPlaceholder;
	}

	public String getMaxPlaceholder() {
		return maxPlaceholder;
	}

	public void setMaxPlaceholder(String maxPlaceholder) {
		this.maxPlaceholder = maxPlaceholder;
	}

	public boolean isValIsProp() {
		return valIsProp;
	}

	public void setValIsProp(boolean valIsProp) {
		this.valIsProp = valIsProp;
	}
	
}

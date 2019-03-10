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
/**
 * 
 * @author Ranphi
 */
public class SqlBuilder {

	private StringBuilder stringBuilder;

	public static SqlBuilder builder() {
		SqlBuilder builder = new SqlBuilder();
		builder.setStringBuilder(new StringBuilder());
		return builder;
	}

	public SqlBuilder append(String str) {
		stringBuilder.append(str).append(" ");
		return this;
	}

	public SqlBuilder append(Object obj) {
		return append(String.valueOf(obj));
	}

	public SqlBuilder deleteCharAt(int index) {
		stringBuilder.deleteCharAt(index);
		return this;
	}

	public int length() {
		return stringBuilder.length();
	}

	public String substring(int start, int end) {
		return stringBuilder.substring(start, end);
	}

	public String toString() {
		return stringBuilder.toString();
	}

	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}

	public void setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}

}

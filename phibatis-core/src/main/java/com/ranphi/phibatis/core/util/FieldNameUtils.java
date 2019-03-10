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
package com.ranphi.phibatis.core.util;

/**
 * 
 * @author Ranphi
 */
public class FieldNameUtils {

	public static String snakeCase(String property) {
		if (null == property) {
			return "";
		}
		StringBuilder sbl = new StringBuilder(property);
		sbl.setCharAt(0, Character.toLowerCase(sbl.charAt(0)));
		property = sbl.toString();
		char[] chars = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : chars) {
			if (isAsciiAlphaUpper(c)) {
				sb.append("_" + charToString(c).toLowerCase());
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String camelCase(String field) {
		if (null == field) {
			return "";
		}
		field = field.toLowerCase();
		char[] chars = field.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(charToString(chars[j]).toUpperCase());
					i++;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static boolean isAsciiAlphaUpper(char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	private static String charToString(char ch) {
		return new String(new char[] { ch });
	}

}

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
public class PathClassResolver {

	private PathClassResolver() {

	}

	public static String convertClassToPath(String name) {
		return name.replaceAll("\\.", "/");
	}

	public static String convertPathToClass(String name) {
		name = name.replaceAll("/", ".");
		int pos = name.lastIndexOf('.');
		if (pos == -1)
			return name;
		return name.substring(0, pos);
	}

}

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
package com.ranphi.phibatis.core.sql.wrapper;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Ranphi
 */
public class MapperPattern {
	
	public final static String DEFAULT = "DEFAULT";
	public final static String LOCK_VERSION = "LOCK_VERSION";
	public final static String SELECTIVE = "SELECTIVE";
	public final static String SELECTIVE_BY_LOCK_VERSION = "SELECTIVE_BY_LOCK_VERSION";
	public final static String ID = "ID";
	public final static String BATCH = "BATCH";
	public final static String COUNT = "COUNT";
	public final static Set<String> MAPPER_SET = new HashSet<String>();
	
	static {
		MAPPER_SET.add(DEFAULT);
		MAPPER_SET.add(LOCK_VERSION);
		MAPPER_SET.add(SELECTIVE);
		MAPPER_SET.add(SELECTIVE_BY_LOCK_VERSION);
		MAPPER_SET.add(ID);
		MAPPER_SET.add(BATCH);
		MAPPER_SET.add(COUNT);
	}
	
}

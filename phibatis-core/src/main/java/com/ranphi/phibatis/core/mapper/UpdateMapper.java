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
package com.ranphi.phibatis.core.mapper;

import org.apache.ibatis.annotations.Update;

import com.ranphi.phibatis.core.sql.wrapper.MapperPattern;

/**
 * 
 * @author Ranphi
 */
public interface UpdateMapper<T> {

	@Update(MapperPattern.DEFAULT)
	int update(Object o);
	
	@Update(MapperPattern.LOCK_VERSION)
	int updateByLockVersion(Object o);
	
	@Update(MapperPattern.SELECTIVE)
	int updateSelective(Object o);
	
	@Update(MapperPattern.SELECTIVE_BY_LOCK_VERSION)
	int updateSelectiveByLockVersion(Object o);
	
}

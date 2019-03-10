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

import java.util.List;

import com.ranphi.phibatis.core.mapper.SimpleMapper;
/**
 * 
 * @author Ranphi
 */
public abstract class ServiceSupport {
	
	public abstract SimpleMapper<?> getMapper();
	
	@SuppressWarnings("unchecked")
	public <R> R select(JpaCriteria jpaCriteria, Class<R> clazz) {
		jpaCriteria.setResultType(clazz);
		Object object = getMapper().select(jpaCriteria);
		return (R) object; 
    }
	
	@SuppressWarnings("unchecked")
	public <R> List<R> selectList(JpaCriteria jpaCriteria, Class<R> clazz) {
		jpaCriteria.setResultType(clazz);
		List<R> object = (List<R>)getMapper().selectList(jpaCriteria);
		return (List<R>) object; 
    }
	
}
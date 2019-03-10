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

import java.util.Map;

import com.ranphi.phibatis.core.JpaCriteria;
import com.ranphi.phibatis.core.sql.UpdateStatement;

/**
 * 
 * @author Ranphi
 */
public class UpdateWrapper extends SqlWrapper {

	private static final UpdateWrapper INSTANCE = new UpdateWrapper();

	public static UpdateWrapper builder() {
		return INSTANCE;
	}

	public String execute(StatementParameter statementParameter) {
		org.apache.ibatis.annotations.Update updateAnno = statementParameter.getMethod()
				.getAnnotation(org.apache.ibatis.annotations.Update.class);
		String annoVal = updateAnno.value()[0];
		if (!MapperPattern.MAPPER_SET.contains(annoVal)) {
			return null;
		}
		Class<?> entityClass = handleEntityClass(statementParameter);
		Object paramObj = statementParameter.getParamObj();
		if (!(paramObj instanceof Map)) {
			statementParameter.setEntityClass(paramObj.getClass());
		}
		UpdateStatement update = new UpdateStatement(entityClass);
		update.initAliasEntityClassMap();
		if (paramObj instanceof JpaCriteria) {
			JpaCriteria criteria = (JpaCriteria) paramObj;
			criteria.setEntityClass(entityClass);
			update = criteria.transToUpdate();
		}
		update.setParam(paramObj);
		boolean selective = false;
		boolean lockVersion = false;
		if (MapperPattern.LOCK_VERSION.equals(annoVal) || MapperPattern.SELECTIVE_BY_LOCK_VERSION.equals(annoVal)) {
			lockVersion = true;
		}
		if (MapperPattern.SELECTIVE.equals(annoVal) || MapperPattern.SELECTIVE_BY_LOCK_VERSION.equals(annoVal)) {
			selective = true;
		}
		update.setUpdateSelective(selective);
		update.setLockVersion(lockVersion);
		return update.toStatementString();
	}

}

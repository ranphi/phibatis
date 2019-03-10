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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.ranphi.phibatis.core.sql.SqlParser;
import com.ranphi.phibatis.core.sql.wrapper.SqlWrapper;
import com.ranphi.phibatis.core.util.FieldNameUtils;

/**
 * 
 * @author Ranphi
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class ResultSetInterceptor extends SqlWrapper implements Interceptor {

	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final Object[] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) args[0];
		Object parameObject = args[1];
		if (parameObject != null && parameObject instanceof JpaCriteria) {
			MetaObject mappedStatementHandler = getMetaObject(mappedStatement);
			JpaCriteria jpaCriteria = (JpaCriteria) parameObject;
			if (jpaCriteria.getResultType() != null) {
				mappedStatementHandler.setValue("resultMaps[0].type", jpaCriteria.getResultType());
			}
		}
		Object result = invocation.proceed();
		boolean columnSnakeCase = SqlParser.getInstance().getSqlProcessor().getDialect().isSnakeCase();
		
		if(columnSnakeCase){
	        if (result instanceof ArrayList) {
				ArrayList resultList = (ArrayList) result;
				if(resultList.size() > 0){
					Object object = resultList.get(0);
					if (object instanceof Map) {
						boolean isBoot = object instanceof Bootmap;
						Map<String,Object> rowMap = (Map<String,Object>)resultList.get(0);
						boolean isCamelCase = false;
						for(String property : rowMap.keySet()){
				        	if(property.indexOf("_") > -1){
				        		isCamelCase = true;
				        		break;
				        	}
						}
						
						if(isCamelCase){
							if(isBoot){
								List<Bootmap> lists = new LinkedList<Bootmap>();
								for (int i = 0; i < resultList.size(); i++) {
									Bootmap resultMap = (Bootmap) resultList.get(i);
				                    lists.add(wrapRowToBootmap(resultMap));
					            }
								return lists;
							}else{
								List<Map<String,Object>> lists = new LinkedList<Map<String,Object>>();
								for (int i = 0; i < resultList.size(); i++) {
									Map<String,Object> resultMap = (Map<String,Object>) resultList.get(i);
				                    lists.add(wrapRowToMap(resultMap));
					            }
								return lists;
							}
						}
						
					}
				}
	        }else if (result instanceof Map) {
	        	boolean isBoot = result instanceof Bootmap;
	        	if(isBoot){
	        		Bootmap resultMap = (Bootmap)result;
	        		return wrapRowToBootmap(resultMap);
	        	}else{
	        		Map<String,Object> resultMap = (Map<String,Object>)result;
	        		return wrapRowToMap(resultMap);
	        	}
	        }
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}
	
	
	private Bootmap wrapRowToBootmap(Map<String,Object> resultMap){
		Bootmap bootmap = new Bootmap(); 
    	wrapRow(bootmap, resultMap);
    	return bootmap;
	}
	
	
	private Map<String,Object> wrapRowToMap(Map<String,Object> resultMap){
		Map<String,Object> map = new HashMap<String,Object>(); 
    	wrapRow(map, resultMap);
    	return map;
	}
	
	
	private void wrapRow(Map<String,Object> map, Map<String,Object> resultMap){
    	for(String property : resultMap.keySet()){
        	if(property.indexOf("_") > -1){
        		map.put(FieldNameUtils.camelCase(property), resultMap.get(property));
        	}else{
        		map.put(property, resultMap.get(property));
        	}
        }
	}
	
}

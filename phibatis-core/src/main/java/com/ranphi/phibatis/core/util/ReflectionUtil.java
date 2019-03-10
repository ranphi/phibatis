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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Ranphi
 */
public class ReflectionUtil {

	private static final Map<Class<?>, Map<String, Field>> FIELDS_CACHE = new ConcurrentHashMap<Class<?>, Map<String, Field>>(
			256);

	public static Map<String, Field> getDeclaredFields(Class<?> clazz) {
		if (clazz == null) {
			throw new NullPointerException("Class must not be null");
		}
		if (!FIELDS_CACHE.containsKey(clazz)) {
			FIELDS_CACHE.put(clazz, new LinkedHashMap<String, Field>());
			Class<?> targetClass = clazz;
			do {
				Field[] fields = targetClass.getDeclaredFields();
				for (Field field : fields) {
					String name = field.getName();
					try {
						PropertyDescriptor pd = new PropertyDescriptor(name, targetClass);
						if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
							continue;
						}
					} catch (IntrospectionException e) {
						continue;
					}
					Map<String, Field> fieldMap = FIELDS_CACHE.get(clazz);
					if (!fieldMap.containsKey(name)) {
						fieldMap.put(name, field);
					}
				}
				targetClass = targetClass.getSuperclass();
			} while (targetClass != null && targetClass != Object.class);
		}
		return FIELDS_CACHE.get(clazz);
	}

	public static Object getValue(Field field, Object target) {
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(
					"reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	public static void setValue(Field field, Object target, Object value) {
		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(
					"reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	public static Map<String, Object> toMap(Object target) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Field> fieldMap = getDeclaredFields(target.getClass());
		for (String name : fieldMap.keySet()) {
			Field field = fieldMap.get(name);
			Object object = getValue(field, target);
			map.put(name, object);
		}
		return map;
	}

}

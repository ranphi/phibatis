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

import java.util.Date;
import java.util.HashMap;

import com.ranphi.phibatis.core.util.TypeConverter;

/**
 * 
 * @author Ranphi
 */
public class Bootmap extends HashMap<String, Object> {
	private static final long serialVersionUID = -1L;

	public String getString(String key) {
		if (containsKey(key)) {
			return TypeConverter.getString(get(key));
		}
		return null;
	}

	public String getString(String key, String defValue) {
		String value = getString(key);
		return value == null ? defValue : value;
	}

	public Integer getInteger(String key) {
		if (containsKey(key)) {
			return TypeConverter.getInteger(get(key));
		}
		return null;
	}

	public Integer getInteger(String key, Integer defValue) {
		Integer value = getInteger(key);
		return value == null ? defValue : value;
	}

	public Long getLong(String key) {
		if (containsKey(key)) {
			return TypeConverter.getLong(get(key));
		}
		return null;
	}

	public Long getLong(String key, Long defValue) {
		Long value = getLong(key);
		return value == null ? defValue : value;
	}

	public Double getDouble(String key) {
		if (containsKey(key)) {
			return TypeConverter.getDouble(get(key));
		}
		return null;
	}

	public Double getDouble(String key, Double defValue) {
		Double value = getDouble(key);
		return value == null ? defValue : value;
	}

	public Float getFloat(String key) {
		if (containsKey(key)) {
			return TypeConverter.getFloat(get(key));
		}
		return null;
	}

	public Float getFloat(String key, Float defValue) {
		Float value = getFloat(key);
		return value == null ? defValue : value;
	}

	public Boolean getBoolean(String key) {
		if (containsKey(key)) {
			return TypeConverter.getBoolean(get(key));
		}
		return null;
	}

	public Boolean getBoolean(String key, Boolean defValue) {
		Boolean value = getBoolean(key);
		return value == null ? defValue : value;
	}

	public Date getDate(String key) {
		if (containsKey(key)) {
			return TypeConverter.getDate(get(key));
		}
		return null;
	}

	public Date getDate(String key, Date defValue) {
		Date value = getDate(key);
		return value == null ? defValue : value;
	}

	public Short getShort(String key) {
		if (containsKey(key)) {
			return TypeConverter.getShort(get(key));
		}
		return null;
	}

	public Short getShort(String key, Short defValue) {
		Short value = getShort(key);
		return value == null ? defValue : value;
	}

}

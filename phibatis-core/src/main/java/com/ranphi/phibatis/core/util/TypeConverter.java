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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Ranphi
 */
public class TypeConverter {
	private final static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat FORMAT_HOUR = new SimpleDateFormat("yyyy-MM-dd HH");
	private final static SimpleDateFormat FORMAT_MINUTE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat FORMAT_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final String getString(Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	public static final Integer getInteger(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Integer)) {
			return (Integer) value;
		}
		if ((value instanceof Number)) {
			return Integer.valueOf(((Number) value).intValue());
		}
		if ((value instanceof String)) {
			String strVal = (String) value;
			if (strVal.length() == 0) {
				return null;
			}
			return Integer.valueOf(Integer.parseInt(strVal));
		}
		throw new IllegalArgumentException("Can not cast to int, value : " + value);
	}

	public static final Float getFloat(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Number)) {
			return Float.valueOf(((Number) value).floatValue());
		}
		if ((value instanceof String)) {
			String strVal = value.toString();
			if (strVal.length() == 0) {
				return null;
			}
			return Float.valueOf(Float.parseFloat(strVal));
		}
		throw new IllegalArgumentException("Can not cast to float, value : " + value);
	}

	public static final Double getDouble(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Number)) {
			return Double.valueOf(((Number) value).doubleValue());
		}
		if ((value instanceof String)) {
			String strVal = value.toString();
			if (strVal.length() == 0) {
				return null;
			}
			return Double.valueOf(Double.parseDouble(strVal));
		}
		throw new IllegalArgumentException("Can not cast to double, value : " + value);
	}

	public static final Long getLong(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Number)) {
			return Long.valueOf(((Number) value).longValue());
		}
		if ((value instanceof String)) {
			String strVal = (String) value;
			if (strVal.length() == 0) {
				return null;
			}
			return Long.valueOf(Long.parseLong(strVal));
		}
		throw new IllegalArgumentException("Can not cast to long, value : " + value);
	}

	public static final Boolean getBoolean(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Boolean)) {
			return (Boolean) value;
		}
		if ((value instanceof Number)) {
			return Boolean.valueOf(((Number) value).intValue() == 1);
		}
		if ((value instanceof String)) {
			String str = (String) value;
			if (str.length() == 0) {
				return null;
			}
			if ("true".equals(str) || "1".equals(str)) {
				return Boolean.TRUE;
			}
			if ("false".equals(str) || "0".equals(str)) {
				return Boolean.FALSE;
			}
		}
		throw new IllegalArgumentException("Can not cast to int, value : " + value);
	}

	public static final Date getDate(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Calendar)) {
			return ((Calendar) value).getTime();
		}
		if ((value instanceof Date)) {
			return (Date) value;
		}
		long longValue = 0L;
		if ((value instanceof Number)) {
			longValue = ((Number) value).longValue();
		}
		if ((value instanceof String)) {
			String strVal = (String) value;
			try {
				if (strVal.length() == 10) {
					return FORMAT_DATE.parse(strVal);
				} else if (strVal.length() == 13) {
					return FORMAT_HOUR.parse(strVal);
				} else if (strVal.length() == 16) {
					return FORMAT_MINUTE.parse(strVal);
				} else if (strVal.length() == 19) {
					return FORMAT_SECOND.parse(strVal);
				}
			} catch (ParseException e) {
				if (strVal.length() == 0) {
					return null;
				}
				longValue = Long.parseLong(strVal);
			}
		}
		if (longValue <= 0L) {
			throw new IllegalArgumentException("Can not cast to Date, value : " + value);
		}
		return new Date(longValue);
	}

	public static final Short getShort(Object value) {
		if (value == null) {
			return null;
		}
		if ((value instanceof Number)) {
			return Short.valueOf(((Number) value).shortValue());
		}
		if ((value instanceof String)) {
			String strVal = (String) value;
			if (strVal.length() == 0) {
				return null;
			}
			return Short.valueOf(Short.parseShort(strVal));
		}
		throw new IllegalArgumentException("Can not cast to short, value : " + value);
	}

}

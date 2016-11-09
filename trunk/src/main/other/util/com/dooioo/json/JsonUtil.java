package com.dooioo.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.processors.DefaultValueProcessorMatcher;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;
import net.sf.json.util.PropertySetStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: json数据处理类
 * 
 * @author jack lee
 * @version v1.0
 * @author (lastest modification by lgh)
 * @since 2015-10-31 17:18:48
 */
public final class JsonUtil {

	private JsonUtil() {
	}

	public static final Integer DEFAULT_FLOAT_PRECISION = 2;

	private static final Log logger = LogFactory.getLog(JsonUtil.class);

	/**
	 * <p>
	 * Description:将JSON对象转换为MAP, 主要针对含有日期字符串的属性转为Date类型数据
	 * </p>
	 * 
	 * @param jsonObject
	 *            JSON对象
	 * @param dateProps
	 *            含有日期类型的属性
	 * @param dateFormat
	 *            日期类型，默认为yyyy-MM-dd'T'HH:mm:ss
	 */
	public static Map<String, ?> convertToMap(JSONObject jsonObject,
			String[] dateProps, String dateFormat) {
		return convertToMap(jsonObject, dateProps, dateFormat, null);
	}

	/**
	 * <p>
	 * Description: 对象转换JSON属性过滤器
	 * </p>
	 */
	public static class JSONPropertyFilter implements PropertyFilter {
		// 允许json中存在值为null的属性
		private boolean allowBlankValue = false;

		public JSONPropertyFilter() {

		}

		public JSONPropertyFilter(boolean allowBlankValue) {
			this.allowBlankValue = allowBlankValue;
		}

		/**
		 * <p>
		 * Description:属性过滤，子类必须覆盖本方法
		 * </p>
		 * 
		 * @param name
		 *            属性名
		 * @param value
		 *            值
		 * @return 结果为false则略过当前属性
		 */
		protected boolean filter(String name, Object value) {
			return true;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see PropertyFilter#apply(Object,
		 *      String, Object)
		 */
		public boolean apply(Object source, String name, Object value) {
			if (allowBlankValue) {
				return !this.filter(name, value);
			} else {
				return !this.filter(name, value)
						|| JSONNull.getInstance().equals(value)
						|| value == null;
			}
		}

	}

	/**
	 * <p>
	 * Description:获取JsonConfig配置对象
	 * </p>
	 * 
	 * @return
	 */
	public static JsonConfig getJsonConfig() {
		return getJsonConfig(null, null, null);
	}

	/**
	 * @param floatPrecision
	 *            精确度
	 * @return
	 */
	public static JsonConfig getJsonConfig(Integer floatPrecision) {
		return getJsonConfig(null, null, null, floatPrecision, null, null);
	}

	/**
	 * <p>
	 * Description:[方法功能中文描述]
	 * </p>
	 * 
	 * @param floatPrecisionMap
	 *            key :类型为float的字段, value: 保留的精确度
	 * @return
	 */
	public static JsonConfig getJsonConfig(
			Map<String, Integer> floatPrecisionMap) {
		return getJsonConfig(null, null, null, null, floatPrecisionMap, null);
	}

	/**
	 * <p>
	 * Description:获取JsonConfig配置对象
	 * </p>
	 * 
	 * @param dateFormat
	 *            日期格式
	 * @return
	 */
	public static JsonConfig getJsonConfig(String dateFormat) {
		return getJsonConfig(null, dateFormat, null);
	}

	/**
	 * <p>
	 * Description:获取JsonConfig配置对象
	 * </p>
	 * 
	 * @param dateFormat
	 *            日期格式
	 * @param floatPrecision
	 *            精确度
	 * @return
	 */
	public static JsonConfig getJsonConfig(String dateFormat,
			Integer floatPrecision) {
		return getJsonConfig(null, dateFormat, null, floatPrecision, null, null);
	}

	/**
	 * <p>
	 * Description:获取JsonConfig配置对象，VOUtils的getJsonConfig方法增强 （float
	 * 类型数据以字符串型展示,且未在floatPrecisionMap中定义的float默认保留2位小数）
	 * </p>
	 * 
	 * @param dateFormat
	 *            日期格式
	 * @param floatPrecisionMap
	 *            key :类型为float的字段, value: 保留的精确度
	 * @return
	 */
	public static JsonConfig getJsonConfig(String dateFormat,
			Map<String, Integer> floatPrecisionMap) {
		return getJsonConfig(null, dateFormat, null, null, floatPrecisionMap,
				null);
	}

	/**
	 * <p>
	 * Description:获取JsonConfig配置对象，VOUtils的getJsonConfig方法增强
	 * </p>
	 * 
	 * @param excludePropNames
	 *            忽略处理的属性
	 * @param dateFormat
	 *            日期格式，默认为yyyy-MM-dd HH:mm:ss
	 * @param propertyFilter
	 *            属性过滤器，可以为null
	 * @return
	 */
	public static JsonConfig getJsonConfig(final String[] excludePropNames,
			String dateFormat, PropertyFilter propertyFilter) {
		return getJsonConfig(excludePropNames, dateFormat, propertyFilter,
				null, null, null);

	}

	/**
	 * <p>
	 * Description:[方法功能中文描述]
	 * </p>
	 * 
	 * @param excludePropNames
	 *            忽略处理的属性
	 * @param dateFormat
	 *            日期格式，默认为yyyy-MM-dd HH:mm:ss
	 * @param propertyFilter
	 *            属性过滤器，可以为null
	 * @param floatPrecision
	 *            精确度（默认2位）
	 * @param floatPrecisionMap
	 *            key :类型为float的字段, value: 保留的精确度
	 * @param floatIsToString
	 *            是否以字符串格式输出（默认：是，可以确保.00的格式）
	 * @param allowBlankValue
	 *            属性是否允许为null
	 * @return
	 */
	public static JsonConfig getJsonConfig(String[] excludePropNames,
			String dateFormat, PropertyFilter propertyFilter,
			Integer floatPrecision, Map<String, Integer> floatPrecisionMap,
			Boolean floatIsToString, boolean allowBlankValue) {
		if (excludePropNames == null) {
			excludePropNames = new String[] { "me", "entityId", "fields", "deleteFlag" };
		} else {
			String[] tmps = new String[excludePropNames.length + 4];
			System.arraycopy(excludePropNames, 0, tmps, 0,
					excludePropNames.length);
			tmps[excludePropNames.length] = "me";
			tmps[excludePropNames.length + 1] = "entityId";
			tmps[excludePropNames.length + 2] = "fields";
			tmps[excludePropNames.length + 3] = "deleteFlag";
			excludePropNames = tmps;
		}
		if (dateFormat == null) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}

		JsonConfig jsonConfig = _getJsonConfig(dateFormat);

		// 设置 float 类型的valueProcess
		JsonFloatValueProcess jsonFloatValueProcessor = new JsonFloatValueProcess(
				floatPrecision, floatPrecisionMap, floatIsToString);
		jsonConfig.registerJsonValueProcessor(Float.class,
				jsonFloatValueProcessor);

		// 设置 double 类型的valueProcess
		JsonDoubleValueProcess jsonDoubleValueProcessor = new JsonDoubleValueProcess(
				floatPrecision, floatPrecisionMap, floatIsToString);
		jsonConfig.registerJsonValueProcessor(Double.class,
				jsonDoubleValueProcessor);

		// 当格式化对存放类型为String的时间类型
		// JsonStringValueProcess jsonStringValueProcess = new
		// JsonStringValueProcess(dateFormat);
		// jsonConfig.registerJsonValueProcessor(String.class,
		// jsonStringValueProcess);

		if (propertyFilter == null) {
			propertyFilter = new JSONPropertyFilter(allowBlankValue);
		}
		jsonConfig.setJsonPropertyFilter(propertyFilter);
		jsonConfig.registerDefaultValueProcessor(Object.class,
				new DefaultValueProcessor() {
					public Object getDefaultValue(Class type) {
						if (JSONUtils.isArray(type)) {
							return new JSONArray();
						} else if (JSONUtils.isNumber(type)) {
							if (JSONUtils.isDouble(type)) {
								return new Double(0);
							} else {
								return new Integer(0);
							}
						} else if (JSONUtils.isBoolean(type)) {
							return Boolean.FALSE;
						} else if (JSONUtils.isString(type)) {
							return null;
						}
						return null;
					}
				});
		jsonConfig
				.setDefaultValueProcessorMatcher(new DefaultValueProcessorMatcher() {
					public Object getMatch(Class class1, Set set) {
						return Object.class;
					}

				});
		final String[] _excludePropNames = excludePropNames;
		jsonConfig.setJavaPropertyFilter(new PropertyFilter() {
			public boolean apply(Object obj, String name, Object value) {
				return _excludePropNames == null
						|| Arrays.binarySearch(_excludePropNames, name) > -1;
			}

		});
		if (excludePropNames != null) {
			jsonConfig.setExcludes(excludePropNames);
		}
		return jsonConfig;
	}

	/**
	 * 获取Json环境上下文默认设置
	 * 
	 * @param dateFormat
	 *            Date类型的转换格式
	 * @return JsonConfig对象
	 */
	private static JsonConfig _getJsonConfig(String dateFormat) {
		JsonDateValueProcessor beanProcessor = new JsonDateValueProcessor();
		if (dateFormat != null) {
			beanProcessor.setDatePattern(dateFormat);
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.util.Date.class,
				beanProcessor);
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,
				beanProcessor);
		jsonConfig.setPropertySetStrategy(PropertySetStrategy.DEFAULT);
		return jsonConfig;
	}

	/**
	 * <p>
	 * Description:[方法功能中文描述]
	 * </p>
	 * 
	 * @param excludePropNames
	 *            忽略处理的属性
	 * @param dateFormat
	 *            日期格式，默认为yyyy-MM-dd HH:mm:ss
	 * @param propertyFilter
	 *            属性过滤器，可以为null
	 * @param floatPrecision
	 *            精确度（默认2位）
	 * @param floatPrecisionMap
	 *            key :类型为float的字段, value: 保留的精确度
	 * @param floatIsToString
	 *            是否以字符串格式输出（默认：是，可以确保.00的格式）
	 * @return
	 */
	public static JsonConfig getJsonConfig(final String[] excludePropNames,
			String dateFormat, PropertyFilter propertyFilter,
			Integer floatPrecision, Map<String, Integer> floatPrecisionMap,
			Boolean floatIsToString) {
		return getJsonConfig(excludePropNames, dateFormat, propertyFilter,
				floatPrecision, floatPrecisionMap, floatIsToString, false);
	}

	/**
	 * <p>
	 * Description:将json格式的文件转化为JSON对象的字符串
	 * </p>
	 * 
	 * @param file
	 *            JSON数据文件包路径
	 * @param encoding
	 *            编码格式
	 * @return
	 */
	public static String getObjectFromFile(File file, String encoding) {
		BufferedReader reader = null;
		try {
			InputStream input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(input, encoding));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (FileNotFoundException e) {
			logger.fatal(e.getMessage(), e);
			throw new RuntimeException("json文件读取失败" + e.getMessage(), e);
		} catch (IOException e) {
			logger.fatal(e.getMessage(), e);
			throw new RuntimeException("json文件读取失败" + e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.fatal(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * <p>
	 * Description: 获取类对象
	 * </p>
	 * 
	 * @return
	 */
	public static <T> List<T> getBeanList(String json, Class<T> clazz) {
		JsonConfig jsonConfig = getJsonConfig();
		JSONArray jsonArray = JSONArray.fromObject(json, jsonConfig);
		List list = new ArrayList(jsonArray.size());
		JSONObject jsonObject;
		for (Iterator iter = jsonArray.iterator(); iter.hasNext(); list
				.add(JSONObject.toBean(jsonObject, clazz))) {
			jsonObject = (JSONObject) iter.next();
		}
		return list;
	}

	/**
	 * <p>
	 * Description:[判断是否为空,JsonObject 数据空状态有null,字符串null及空]
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj) {
		return obj == null || obj instanceof JSONNull || obj.equals("")
				|| obj.equals("null");
	}

	public static String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		return toJsonObject(obj, null).toString();
	}

	public static String toJsonString(Object obj, JsonConfig config) {
		if (obj == null) {
			return null;
		}
		return toJsonObject(obj, config).toString();
	}

	public static JSONObject toJsonObject(Object obj) {
		if (obj == null) {
			return null;
		}
		return toJsonObject(obj, null);
	}

	public static JSONObject toJsonObject(Object obj, JsonConfig config) {
		if (obj == null) {
			return null;
		}
		if (config == null) {
			config = getJsonConfig();
		}
		JSONObject json = JSONObject.fromObject(obj, config);
		return json;
	}

	/*
	 * 批量过滤bo、vo中不需要的字段,不处理的字段前加 @JsonExtraField
	 */
	public static JSONArray toJSONArray(List list) {
		JSONArray json = new JSONArray();
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				if (obj != null) {
					json.add(toJsonObject(obj));
				}
			}
		}
		return json;
	}

	/**
	 * <p>
	 * Description:将JSON对象转换为MAP, 主要针对含有日期字符串的属性转为Date类型数据
	 * </p>
	 * 
	 * @param jsonObject
	 *            JSON对象
	 * @param dateProps
	 *            含有日期类型的属性
	 * @param dateFormat
	 *            日期类型，默认为yyyy-MM-dd'T'HH:mm:ss
	 * @param excludeProps
	 *            过滤的属性
	 * @return
	 */
	public static Map<String, ?> convertToMap(JSONObject jsonObject,
			String[] dateProps, String dateFormat, String[] excludeProps) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> datePropList = null;
		List<String> excludePropsList = null;
		if (dateProps != null) {
			List<String> tempList = new ArrayList<String>();
			for (int i = 0; i < dateProps.length; i++) {
				tempList.add(dateProps[i]);
			}
			datePropList = tempList;
		} else {
			datePropList = new ArrayList<String>(2);
		}
		if (dateFormat == null) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}

		excludePropsList = excludeProps == null ? new ArrayList<String>(0)
				: Arrays.asList(excludeProps);
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Map.Entry<String, ?> entry = null;
		for (Object entryObj : jsonObject.entrySet()) {
			entry = (Map.Entry<String, ?>) entryObj;
			// 属性过滤
			if (excludePropsList.contains(entry.getKey())) {
				continue;
			}
			if (entry.getValue() == null
					|| (entry.getValue().toString()).trim().equals("")
					|| JSONNull.getInstance().equals(entry.getValue())) {
				map.put(entry.getKey(), null);
			} else if (datePropList.contains(entry.getKey())) {
				try {
					map.put(entry.getKey(),
							format.parse((String) entry.getValue()));
				} catch (ParseException e) {
					logger.fatal(e.getMessage(), e);
				}
			} else {
				map.put(entry.getKey(), entry.getValue());

			}
		}
		return map;
	}
	
	
	public static <T> T toBean(JSONObject json,Class<T> t){
		JSONUtils.getMorpherRegistry().registerMorpher(new SqlDateMorpher());
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher());
		JSONUtils.getMorpherRegistry().registerMorpher(new JavaDateMorpher());
		return (T)JSONObject.toBean(json,t);
	}
}

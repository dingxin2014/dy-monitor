package com.dooioo.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Map;

public class JsonDoubleValueProcess implements JsonValueProcessor {

	private static final Log logger = LogFactory.getLog(JsonDoubleValueProcess.class);

	private Map<String, Integer> precisionMap;

	private boolean isToString = true;

	public void setToString(boolean isToString) {
		this.isToString = isToString;
	}

	public void setPrecisionMap(Map<String, Integer> precisionMap) {
		this.precisionMap = precisionMap;
	}

	public JsonDoubleValueProcess() {
		this.precision = 2;
	}

	public JsonDoubleValueProcess(Integer precision, Map<String, Integer> precisionMap, Boolean isToString) {
		if (!CollectionUtils.isEmpty(precisionMap) && precision == null) {
			precision = 2;
		}
		this.precisionMap = precisionMap;
		this.precision = precision;

		if (isToString != null) {
			this.isToString = isToString;
		}
	}

	private Integer precision;

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return this.process(null, value, jsonConfig);
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return this.process(key, value, jsonConfig);
	}

	private Object process(String key, Object value, JsonConfig jsonConfig) {
		try {
			if (key != null && !CollectionUtils.isEmpty(precisionMap) && precisionMap.containsKey(key)) {
				BigDecimal bd = new BigDecimal((Double) value).setScale(this.precisionMap.get(key), 4);
				return isToString ? bd.toString() : bd.doubleValue();
			}
			else if (this.precision != null) {
				BigDecimal bd = new BigDecimal((Double) value).setScale(this.precision, 4);
				return isToString ? bd.toString() : bd.doubleValue();
			}
		}
		catch (Exception e) {
			// process failure
			logger.fatal(e.getMessage(), e);
		}
		return Double.valueOf(value.toString());
	}
}

package com.dooioo.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsonDateValueProcessor implements JsonValueProcessor {

	private String datePattern = "yyyy-MM-dd HH:mm:ss";

	public JsonDateValueProcessor() {
		super();
	}

	public JsonDateValueProcessor(String format) {
		super();
		this.datePattern = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		Object rValue = value;
		if (value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.UK);
			rValue = sdf.format((Date) value);
		}
		if (value instanceof java.sql.Date) {
			Date d = new Date(((java.sql.Date) value).getTime());
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.UK);
			rValue = sdf.format(d);
		}
		if (value instanceof Timestamp) {
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.UK);
			rValue = sdf.format((Timestamp) value);
		}
		return rValue;

	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String pDatePattern) {
		datePattern = pDatePattern;
	}

}

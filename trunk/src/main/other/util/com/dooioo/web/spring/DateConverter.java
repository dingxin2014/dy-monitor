package com.dooioo.web.spring;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import com.dooioo.util.AppUtils;

public class DateConverter implements Converter<String, Date> {
	@Override
	public Date convert(String source) {
		source = source.trim();
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return AppUtils.parseDate(source);
	}

}

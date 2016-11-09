package com.dooioo.json;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lgh
 * @version: v1.0
 * @author: (lastest modification by lgh)
 */
public class SqlDateMorpher extends AbstractObjectMorpher {
	private static Log logger = LogFactory.getLog(SqlDateMorpher.class);
	private String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-ddTHH:mm:ss", "yyyy-MM-dd" };

	/**
	 * @see AbstractObjectMorpher#morph(Object)
	 */
	@Override
	public Object morph(Object value) {
		if (value == null) {
			return null;
		}

		if (java.sql.Date.class.isAssignableFrom(value.getClass())) {
			return (java.sql.Date) value;
		}

		if (!supports(value.getClass())) {
			throw new MorphException(value.getClass() + " is not supported");
		}

		String strValue = (String) value;
		SimpleDateFormat dateParser = null;
		Date date = null;
		for (int i = 0; i < formats.length; i++) {
			if (dateParser == null) {
				dateParser = new SimpleDateFormat(formats[i]);
			}
			else {
				dateParser.applyPattern(formats[i]);
			}
			try {
				date = dateParser.parse(strValue.toLowerCase());
				if (date != null) {
					continue;
				}
			}
			catch (ParseException pe) {
				// ignore exception, try the next format
			}
		}

		// unable to parse the date
		if (date != null) {
			return new java.sql.Date(date.getTime());
		}
		else {
			logger.warn("Unable to parse the SqlDate: " + value);
			return null;
		}
	}

	/**
	 * @see AbstractObjectMorpher#morphsTo()
	 */
	@Override
	public Class morphsTo() {
		return java.sql.Date.class;
	}

}

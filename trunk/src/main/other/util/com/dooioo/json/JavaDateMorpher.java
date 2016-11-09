package com.dooioo.json;

import com.dooioo.util.AppUtils;
import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: jack lee
 * @version: v1.0
 * @author: (lastest modification by lgh)
 */
public class JavaDateMorpher extends AbstractObjectMorpher {
	private static Log logger = LogFactory.getLog(JavaDateMorpher.class);
	private String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-ddTHH:mm:ss",
			"yyyy-MM-dd" };

	/**
	 * @see AbstractObjectMorpher#morph(Object)
	 */
	@Override
	public Object morph(Object value) {
		if (AppUtils.isEmpty(value)) {
			return null;
		}

		if (Date.class.isAssignableFrom(value.getClass())) {
			return (Date) value;
		}

		if (!supports(value.getClass())) {
			throw new MorphException(value.getClass() + " is not supported");
		}

		String strValue = (String) value;
		SimpleDateFormat dateParser = null;
		Date date = null;
		for (int i = 0; i < formats.length; i++) {
			try {
				if (dateParser == null) {
					dateParser = new SimpleDateFormat(formats[i]);
				} else {
					dateParser.applyPattern(formats[i]);
				}
				date = dateParser.parse(strValue.toLowerCase());
				if (date != null) {
					break;
				}
			} catch (ParseException pe) {
				// ignore exception, try the next format
				// pe.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		// unable to parse the date
		if (date != null) {
			return date;
		} else {
			logger.warn("Unable to parse the JavaDate: " + value);
			return null;
		}
	}

	/**
	 * @see AbstractObjectMorpher#morphsTo()
	 */
	@Override
	public Class morphsTo() {
		return Date.class;
	}

}

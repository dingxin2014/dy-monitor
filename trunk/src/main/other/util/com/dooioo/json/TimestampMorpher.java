package com.dooioo.json;

import com.dooioo.util.AppUtils;
import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: jack lee
 * @version: v1.0
 * @author: (lastest modification by lgh)
 */
public class TimestampMorpher extends AbstractObjectMorpher {

	private static Log logger = LogFactory.getLog(TimestampMorpher.class);
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

		if (Timestamp.class.isAssignableFrom(value.getClass())) {
			return (Timestamp) value;
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
			return new Timestamp(date.getTime());
		} else {
			logger.warn("Unable to parse the Timestamp: " + value);
			return null;
			//throw new MorphException("Unable to parse the date " + value);
		}
	}

	/**
	 * @see AbstractObjectMorpher#morphsTo()
	 */
	@Override
	public Class morphsTo() {
		return Timestamp.class;
	}

}

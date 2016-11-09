package com.dooioo.json;

import net.sf.json.JSONException;
import net.sf.json.util.PropertySetStrategy;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;

/**
 * @author lgh
 * @version v1.0
 * @author (lastest modification by lgh)
 */
public class JsonPropertySetStrategy extends PropertySetStrategy {
	private static final Log logger = LogFactory.getLog(JsonPropertySetStrategy.class);

	public void setProperty(Object bean, String key, Object value) throws JSONException {
		try {
			// Class type = getPropertyType(bean.getClass(), key);
			// PropertyUtils.setSimpleProperty(bean, key, SunyatUtils.getValueByType(type, value));
			PropertyUtils.setSimpleProperty(bean, key, value);
		}
		catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * 查找类中某个域的类型
	 * @param t 类
	 * @param name 域名
	 * @return 返回类型, 找不到域时返回null
	 */
	private Class getPropertyType(Class t, String name) {
		Field field = null;
		try {
			field = t.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			logger.info(e.getMessage());
		}
		if (field == null && t != Object.class) {
			Class c = t.getSuperclass();
			return getPropertyType(c, name);
		}
		if (field == null)
			return null;
		else
			return field.getType();
	}



}

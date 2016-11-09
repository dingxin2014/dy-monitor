package com.dooioo.util;

import com.dooioo.ereceipt.exception.AppException;
import com.dooioo.json.RegExpUtil;
import com.dooioo.plus.oms.dyUser.model.Employee;
import com.dooioo.util.http.CommonModel;
import com.dooioo.util.http.HttpClientUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppUtils {

	private static final Log logger = LogFactory.getLog(AppUtils.class);

	@SuppressWarnings("rawtypes")
	private static final Map cached = new ConcurrentHashMap();

	/**
	 * <p>
	 * Description:判断提供的对象是否为null、空字符“”、或者JSonNull对象
	 * </p>
	 *
	 */
	public static boolean isEmpty(Object obj) {
		return JSONNull.getInstance().equals(obj) || "".equals(obj.toString());
	}

	/**
	 * 解析字符串为date类型
	 *
	 * @param dateStr
	 *            日期时间
	 * @return 根据字符串解析的日期对象
	 */
	public static Date parseDate(String dateStr) {
		String[] formats = new String[] { "yyyy-MM-dd HH:mm:ss SSSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
				"yyyy-MM-dd", "yyyy-MM", "yyyy年MM月", "yyyy" };
		if (!StringUtils.isEmpty(dateStr)) {
			Date date = null;
			for (String s : formats) {
				try {
					SimpleDateFormat format = new SimpleDateFormat(s);
					date = format.parse(dateStr);
				} catch (Exception e) {
					logger.debug("日期转换失败");
				}
				if (date != null) {
					return date;
				}
			}
			return null;
		}
		return null;
	}

	public static String getIp(HttpServletRequest httpRequest) {
		// 获取客户端IP
		String ipAddress = httpRequest.getHeader("Cdn-Src-Ip");// 增加CDN获取ip
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("X-Forwarded-For");
		}

		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				try {
					ipAddress = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					logger.warn(e.getMessage(), e);
				}

			}

		}
		return ipAddress;
	}

	public static void setValue(Object bean, String fieldName, Object val) {
		try {
			Field field = getField(bean.getClass(), fieldName);
			if (field == null) {
				throw new AppException("字段不存在：" + fieldName + " in class " + bean.getClass());
			}
			Class type = field.getType();
			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			String methodName = "set" + fieldName;
			Method m = bean.getClass().getDeclaredMethod(methodName, type);
			m.invoke(bean, getValueByType(type, val));
		} catch (Exception e) {
			logger.warn("字段：" + fieldName + ",value：" + val + "\t" + e.getMessage(), e);
		}
	}

	private static Field getField(Class c, String fieldName) {
		if (c == Object.class) {
			return null;
		}
		Field field;
		try {
			field = c.getDeclaredField(fieldName);
			if (field == null) {
				field = getField(c.getSuperclass(), fieldName);
			} else {
				return field;
			}
		} catch (Exception e) {
			return getField(c.getSuperclass(), fieldName);
		}
		return field;
	}

	@SuppressWarnings("unchecked")
	public static void putUsercode(Long tid, int userCode) {
		cached.put(tid + "usercode", userCode);
	}

	/**
	 * 清楚缓存的用户编码
	 *
	 */
	public static void removeUsercode(long tid) {
		cached.remove(tid + "usercode");
	}

	/**
	 * 根据传入的线程id获取用户编码
	 *
	 */
	public static int getUsercode(long tid) {
		Integer c = (Integer) cached.get(tid + "usercode");
		if (c == null) {
			logger.warn("读取不到用户编码，线程号为：" + tid);
			return -1;
			// throw new AppException("找不到用户编码在线程下：" + tid);
		}
		return c;
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public static void putUsername(long tid, String username) {
		cached.put(tid + "username", username);

	}

	/**
	 *
	 */
	public static void removeUsername(long tid) {
		cached.remove(tid + "username");
	}

	/**
	 * 根据当前线程获取用户名称
	 *
	 */
	public static String getUsername(long tid) {
		String name = (String) cached.get(tid + "username");
		if (name == null) {
			// throw new AppException("找不到用户在线程下：" + tid);
			return "系统";
		}
		return name;
	}

	/**
	 * 缓存公司id，供同一线程中其它地方使用
	 *
	 */
	@SuppressWarnings("unchecked")
	public static void putCompanyId(Long tid, int companyId) {
		cached.put(tid + "companyId", companyId);
	}

	/**
	 * 清楚缓存的公司id
	 *
	 */
	public static void removeCompanyId(long tid) {
		cached.remove(tid + "companyId");
	}

	/**
	 * 根据传入的线程id获取公司id
	 *
	 */
	public static int getCompanyId(long tid) {
		Integer c = (Integer) cached.get(tid + "companyId");
		if (c == null) {
			logger.warn("读取不到公司id，线程号为：" + tid);
			return -1;
			// throw new AppException("找不到公司id在线程下：" + tid);
		}
		return c;
	}

	/**
	 * 缓存用户IP，供同一线程中其它地方使用
	 *
	 */
	@SuppressWarnings("unchecked")
	public static void putUserIP(Long tid, String userIP) {
		cached.put(tid + "userIP", userIP);
	}

	public static Employee getEmp(long tid) {
		return (Employee) cached.get(tid + "employee");
	}

	@SuppressWarnings("unchecked")
	public static void putEmp(long tid, Employee emp) {
		cached.put(tid + "employee", emp);
	}

	public static void removeEmp(long tid) {
		cached.remove(tid + "employee");
	}

	/**
	 * 清楚缓存的用户IP
	 *
	 */
	public static void removeUserIP(long tid) {
		cached.remove(tid + "userIP");
	}

	/**
	 * 根据传入的线程id获取用户IP
	 *
	 */
	public static String getUserIP(long tid) {
		String c = (String) cached.get(tid + "userIP");
		if (c == null) {
			logger.warn("读取不到用户IP，线程号为：" + tid);
			return "未知";
			// throw new AppException("找不到用户IP在线程下：" + tid);
		}
		return c;
	}

	/**
	 * 从GitLab上获取不同环境的JDBC配置信息
	 *
	 * @param env
	 *            运行环境
	 * @return 配置信息键值对
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> loadInternetResource(String env) {
		Map<String, String> rs = new HashMap<>();
		String urlConfig = Configuration.getInstance().getProperty("config.center");
		String modules = Configuration.getInstance().getProperty("config.models");
		String[] urls = modules.split(",");

		CommonModel<String> model;

		for (String url : urls) {
			url = MessageFormat.format(urlConfig, Configuration.getInstance().getUrlSuffix(), url, env);
			model = HttpClientUtils.httpGet(url, null);
			if ("ok".equals(model.getStatus())) {
				String data = model.getData();
				JSONObject dataJson = JSONObject.fromObject(data);
				JSONArray dataArray = JSONArray.fromObject(dataJson.get("propertySources"), new JsonConfig());
				dataJson = JSONObject.fromObject(dataArray.get(0).toString());
				dataArray = JSONArray.fromObject(dataJson.get("source"), new JsonConfig());
				dataJson = JSONObject.fromObject(dataArray.get(0).toString());
				logger.info(">>>>>jdbc config:" + dataJson.toString() + Configuration.getInstance().getUrlSuffix() + url
						+ env);
				if (!JSONNull.getInstance().equals(dataJson)) {
					rs.putAll(dataJson);
				}
			}
		}
		return rs;
	}

	public static String toUpperFirst(String s) {
		char[] cs = s.toCharArray();
		if (cs.length > 0 && cs[0] <= 122 && cs[0] >= 97)
			cs[0] -= 32;
		return String.valueOf(cs);
	}

	public static <T> T isnull(T t1, T t2) {
		if (t1 == null)
			return t2;
		return t1;
	}

	/**
	 * <p>
	 * Description:获取JsonConfig配置对象
	 * </p>
	 *
	 * @return
	 */
	public static JsonConfig getJsonConfig() {
		return getJsonConfig();
	}

	private static Object getValueByType(Class<?> type, Object v) {
		if (v == null) {
			return null;
		}
		if (!(v instanceof String)) {
			return v;
		}
		String value = String.valueOf(v);
		if (type.getName().equals(Boolean.class.getName()) || type.getName().equals(boolean.class.getName())) {

			return !(AppUtils.isEmpty(value) || "false".equals(value.toLowerCase()) || "0".equals(value));

		} else if (type.getName().equals(String.class.getName())) {
			return value;
		} else if (type.getName().equals(Integer.class.getName()) || type.getName().equals(int.class.getName())) {
			value = RegExpUtil.removeSpecialSymbol(value);
			if (RegExpUtil.isInt(value)) {
				return Integer.parseInt(value);
			}
			return type.getName().equals(Integer.class.getName()) ? null : 0;
		} else if (type.getName().equals(Float.class.getName()) || type.getName().equals(float.class.getName())) {
			value = RegExpUtil.removeSpecialSymbol(value);
			if (RegExpUtil.isFloat(value)) {
				return Float.parseFloat(value);
			}
			return 0;
		} else if (type.getName().equals(Long.class.getName()) || type.getName().equals(long.class.getName())) {
			value = RegExpUtil.removeSpecialSymbol(value);
			if (RegExpUtil.isLong(value)) {
				return Long.parseLong(value);
			}
			return 0;
		} else if (type.getName().equals(Double.class.getName()) || type.getName().equals(double.class.getName())) {
			value = RegExpUtil.removeSpecialSymbol(value);
			if (RegExpUtil.isFloat(value)) {
				return Double.parseDouble(value);
			}
			return 0;
		} else if (type.getName().equals(Short.class.getName()) || type.getName().equals(short.class.getName())) {
			value = RegExpUtil.removeSpecialSymbol(value);
			if (RegExpUtil.isShort(value)) {
				return Short.parseShort(value);
			}
			return (short) 0;
		} else if (type.getName().equals(java.util.Date.class.getName())) {
			if (RegExpUtil.isDate(value)) {
				return parseDate(value);
			}
			return value;
		} else if (type.getName().equals(java.sql.Date.class.getName())) {
			if (RegExpUtil.isDate(value)) {
				java.util.Date date = parseDate(value);
				if (date == null)
					return value;
				else
					return new java.sql.Date(date.getTime());
			}
			return value;
		} else if (type.getName().equals(BigDecimal.class.getName())) {
			value = RegExpUtil.removeSpecialSymbol(value);
			if (RegExpUtil.isNull(value)) {
				return null;
			}
			if (RegExpUtil.isNumber(value)) {
				return new java.math.BigDecimal(value);
			}
			return value;
		}
		return null;

	}

	public static long getEndOfDay(Date date) {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 997);
		return todayEnd.getTime().getTime();
	}

	public static long getBeginOfDay(Date date) {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR, 0);
		todayEnd.set(Calendar.MINUTE, 0);
		todayEnd.set(Calendar.SECOND, 0);
		todayEnd.set(Calendar.MILLISECOND, 0);
		return todayEnd.getTime().getTime();
	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 复制文件  也可以判断操作系统调用系统命令复制 效率最快
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while (inStream.read(buffer) != -1) {
					fs.write(buffer);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(File file){
		try {
			if(file.exists())
				file.delete();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
	}
	
}

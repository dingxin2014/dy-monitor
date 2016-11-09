package com.dooioo.util;


import java.lang.reflect.Field;
import java.util.Objects;

import com.dooioo.ereceipt.exception.EReceiptException;

/**
 * <p>Assert for EReceipt System</p>
 * Default throw EReceiptException,if u need,plz throw the exception which extends RuntimeExcetion.
 * @author dingxin
 * @since 2016-7
 */
public class Assert {

	/**
	 * 构造RuntimeException
	 * @param obj
	 * @param message
	 * @param clazz
	 * @return
	 */
	private static RuntimeException generateRuntimeException(String message,Class<? extends RuntimeException> clazz){
		RuntimeException runtimeException = null;
		if(message == null){
			throw new EReceiptException("[Null message] Error. Plz contact Product Manager.");
		}
		try {
			runtimeException = clazz.newInstance();
			Field[] fields = Throwable.class.getDeclaredFields();  
	        for (int i = 0; i < fields.length; i++) {  
	        	if(fields[i].toString().equals("private java.lang.String java.lang.Throwable.detailMessage")){
	        		fields[i].setAccessible(true);  
	        		fields[i].set(runtimeException, message);
	        		break;
	        	}
	        }  
		} catch (InstantiationException | IllegalAccessException e) {
			return new EReceiptException("[exception] Error .Plz contact Product Manager! -->"+e.getMessage());
		}
		if(runtimeException != null)
			return runtimeException;
		else 
			return new EReceiptException("[exception] Error .Plz contact Product Manager!");
	}
	
	public static void isNull(Object obj,String message){
		if(obj != null)
			throw new EReceiptException(message);
	}
	
	public static void isNull(Object obj,String message,Class<? extends RuntimeException> clazz){
			throw generateRuntimeException(message,clazz);
	}
	
	public static void isNull(Object[] objs,String[] message){
		if(objs == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(objs.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = objs .length;
		for(int i = 0;i<length;i++){
			if(objs[i] != null && message[i] != null){
				throw new EReceiptException(message[i]);
			}
		}
	}
	
	public static void isNull(Object[] objs,String[] message,Class<? extends RuntimeException> clazz){
		if(objs == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(objs.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = objs .length;
		for(int i = 0;i<length;i++){
			if(objs[i] != null && message[i] != null){
				throw generateRuntimeException(message[i], clazz);
			}
		}
	}
	
	/**
	 * 抛空校验异常
	 * @param obj
	 * @param message
	 */
	public static void notNull(Object obj,String message){
		if(obj == null && message != null)
			throw new EReceiptException(message);
	}
	
	public static void notNull(Object obj,String message,Class<? extends RuntimeException> clazz){
		if(obj == null && message != null)
			throw generateRuntimeException(message, clazz);
	}
	
	/**
	 * 抛空校验异常
	 * @param objs
	 * @param message
	 */
	public static void notNull(Object[] objs,String[] message){
		if(objs == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(objs.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = objs.length;
		for(int i = 0;i<length;i++){
			if(objs[i] == null && message[i] != null){
				throw new EReceiptException(message[i]);
			}
		}
	}
	
	public static void notNull(Object[] objs,String[] message,Class<? extends RuntimeException> clazz){
		if(objs == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(objs.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = objs.length;
		for(int i = 0;i<length;i++){
			if(objs[i] == null && message[i] != null){
				throw generateRuntimeException(message[i], clazz);
			}
		}
	}
	
	public static void notEmpty(Object obj,String message){
		if(Objects.isNull(obj) && message != null)
			throw new EReceiptException(message);
	}
	
	public static void notEmpty(Object obj,String message,Class<? extends RuntimeException> clazz){
		if(Objects.isNull(obj) && message != null)
			throw generateRuntimeException(message, clazz);
	}
	
	public static void notEmpty(Object[] objs,String[] message){
		if(objs == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(objs.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = objs.length;
		for(int i = 0;i<length;i++){
			if(Objects.isNull(objs[i]) && message[i] != null){
				throw new EReceiptException(message[i]);
			}
		}
	}
	
	public static void notEmpty(Object[] objs,String[] message,Class<? extends RuntimeException> clazz){
		if(objs == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(objs.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = objs.length;
		for(int i = 0;i<length;i++){
			if(Objects.isNull(objs[i]) && message[i] != null){
				throw generateRuntimeException(message[i], clazz);
			}
		}
	}
	
	/**
	 * 
	 * @param b
	 * @param message
	 */
	public static void notTrue(boolean b,String message){
		if(b && message != null)
			throw new EReceiptException(message);
	}
	
	public static void notTrue(boolean b,String message,Class<? extends RuntimeException> clazz){
		if(b && message != null)
			throw generateRuntimeException(message, clazz);
	}
	
	/**
	 * 抛空校验异常 booleans[i] 为true 同时 message[i] != null 
	 * @param booleans
	 * @param message
	 */
	public static void notTrue(boolean[] booleans,String[] message){
		if(booleans == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(booleans.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = booleans.length;
		for(int i = 0;i<length;i++){
			if(booleans[i] && message[i] != null){
				throw new EReceiptException(message[i]);
			}
		}
	}
	
	public static void notTrue(boolean[] booleans,String[] message,Class<? extends RuntimeException> clazz){
		if(booleans == null || message == null){
			throw new EReceiptException("[message] Error .Plz contact Product Manager!");
		}
		if(booleans.length != message.length)
			throw new EReceiptException("[exception length] Error .Plz contact PM !");
		int length = booleans.length;
		for(int i = 0;i<length;i++){
			if(booleans[i] && message[i] != null){
				throw generateRuntimeException(message[i], clazz);
			}
		}
	}
}

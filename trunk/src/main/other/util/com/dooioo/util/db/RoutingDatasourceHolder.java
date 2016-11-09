package com.dooioo.util.db;

import org.apache.commons.lang3.StringUtils;

import com.dooioo.util.Configuration;

public class RoutingDatasourceHolder {

	/**
	 * 只读数据库
	 */
	public static  String READ_DB = "dataSourceRD";

	/**
	 * 读写数据库
	 */
	public static  String READ_WRITE_DB = "dataSourceRW";
	
	static{
		String read = Configuration.getInstance().getProperty("read.database");
		String rw = Configuration.getInstance().getProperty("read.write.database");
		if(!StringUtils.isEmpty(read)){
			READ_DB = read;
		}
		if(!StringUtils.isEmpty(rw)){
			READ_WRITE_DB = rw;
		}
	}
	
	private static ThreadLocal<String> dbThread = new ThreadLocal<>();
	
	public static String getCurrentDatabase(){
		return dbThread.get();
	}
	
	public static void setCurrentDatabase(String dbName){
		dbThread.set(dbName);
	}
	
	public static void clear(){
		dbThread.remove();
	}
}

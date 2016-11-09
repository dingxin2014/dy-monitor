package com.dooioo.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * <p>
 * Description: 告诉系统,此controller访问的权限隶属于哪一个权限code,这里的code与权限系统中的code一致,一个方法可以隶属于多个权限code;
 * 如新门店申请主菜单的code为:rents_menu_shop_0,它附带的访问方法有:index(列表方法),view（详细页面方法），则在index和view两个页面上加上此权限code（rents_menu_shop_0）
 * </p>
 * 
 * @author: jack lee
 * @version: v1.0
 * @author: (lastest modification by jack lee)
 * @since: 2015年10月31日下午6:49:43
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthRef {
	/**
	 * <p>
	 * Discription:隶属于权限code集合
	 * </p>
	 * 
	 * @return
	 */
	public String[] ref();
}

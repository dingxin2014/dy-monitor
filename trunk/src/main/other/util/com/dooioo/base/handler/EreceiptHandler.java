package com.dooioo.base.handler;

import java.util.HashMap;

import com.dooioo.ereceipt.common.model.Request;
import static com.dooioo.util.Assert.notTrue;

/**
 * 为了实现解藕 所有银行实现此接口
 * @author dingxin 
 * @email being_away@qq.com
 *
 */
public interface EreceiptHandler {
	
	/**
	 * in fact, handler is a map for store data,
	 * u can use putValue & getValue method freely
	 * @author dingxin
	 *
	 * @param <K>
	 * @param <V>
	 */
	public abstract class HandlerCallBack<K, V> extends HashMap<K, V>{
		
		private static final long serialVersionUID = 1L;

		public HandlerCallBack<K, V> putValue(K key, V value) {
			put(key, value);
			return this;
		}
		
		public HandlerCallBack<K, V> putValue(K[] keys, V[] values) {
			int length;
			notTrue((length = keys.length) != values.length,"keys's length must equal values's length!");
			for(int i=0; i < length; i++){
				put(keys[i], values[i]);
			}
			return this;
		}
		
		public HandlerCallBack<K, V> removeValue(K key, V value) {
			remove(key, value);
			return this;
		}
		
		/**
		 * 成功回调
		 * @param request
		 */
		public abstract void success(Request request,Object obj);
		
		/**
		 * 失败回调
		 * @param request
		 */
		public abstract void fail(Request request,Object obj);
		
		/**
		 * 错误回调
		 * @param request
		 */
		public abstract void error(Request request,Object obj);
		
		/**
		 * 完成后回调(不管成功还是失败)
		 * @param request
		 */
		public abstract void complete(Request request,Object obj);
		
	}

	void syncFile(Request[] requests, HandlerCallBack<String, Object> callBack);

	void sendRequest(Request[] requests, HandlerCallBack<String, Object> callBack);
}

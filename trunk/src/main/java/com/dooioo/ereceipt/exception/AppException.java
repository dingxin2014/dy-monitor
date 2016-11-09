/**
 * 
 */
package com.dooioo.ereceipt.exception;

/**
 * @author liguohui
 *
 */
public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6113856724131185976L;

	/**
	 * 
	 */
	public AppException() {
		super();
	}

	/**
	 * @param message 错误细节信息
	 */
	public AppException(String message) {
		super(message);
	}

	/**
	 * @param cause 错误原因
	 */
	public AppException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message 错误细节信息
	 * @param cause 错误原因
	 */
	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message 错误细节信息
	 * @param cause 错误原因
	 * @param enableSuppression 是否启用压制
	 * @param writableStackTrace 是否输出错误栈
	 */
	public AppException(String message, Throwable cause,
						boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

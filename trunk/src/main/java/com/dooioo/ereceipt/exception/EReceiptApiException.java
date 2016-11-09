package com.dooioo.ereceipt.exception;

public class EReceiptApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EReceiptApiException() {
		super();
	}

	public EReceiptApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EReceiptApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public EReceiptApiException(String message) {
		super(message);
	}

	public EReceiptApiException(Throwable cause) {
		super(cause);
	}
	
}

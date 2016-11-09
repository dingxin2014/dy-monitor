package com.dooioo.ereceipt.exception;

public class EReceiptException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EReceiptException() {
		super();
	}

	public EReceiptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EReceiptException(String message, Throwable cause) {
		super(message, cause);
	}

	public EReceiptException(String message) {
		super(message);
	}

	public EReceiptException(Throwable cause) {
		super(cause);
	}

	
}

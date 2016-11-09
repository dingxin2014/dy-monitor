package com.dooioo.util.db;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.druid.pool.DruidDataSource;

public class AppDatasource extends DruidDataSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4492811993388312648L;

	public AppDatasource() {
		super();
	}

	public AppDatasource(boolean fairLock) {
		super(fairLock);
	}

	public void setPassword(String password) {
		password = decrypt(password);
		super.setPassword(password);
	}

	private final static String KEY = "TjE5MGQ5";

	private final static String ENT = "Blowfish";

	private static String decrypt(String secret) {
		try {
			byte[] kbytes = KEY.getBytes();
			SecretKeySpec key = new SecretKeySpec(kbytes, ENT);

			BigInteger n = new BigInteger(secret, 16);
			byte[] encoding = n.toByteArray();

			Cipher cipher = Cipher.getInstance(ENT);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decode = cipher.doFinal(encoding);
			return new String(decode);
		} catch (Exception e) {
			return secret;
		}
	}
}

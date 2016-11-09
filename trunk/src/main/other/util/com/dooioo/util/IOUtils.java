/**
 * 
 */
package com.dooioo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;

import com.dooioo.ereceipt.exception.EReceiptException;

/**
 * @author liguohui
 *
 */
public class IOUtils {
	public static void sendFully(OutputStream os, String str, String charsetName)
			throws EReceiptException {
		try {
			if (StringUtils.isEmpty(charsetName)) {
				charsetName = "UTF-8";
			}
			write(str, os, charsetName);
		} catch (Throwable t) {
			throw new EReceiptException(t);
		}
	}

	public static String readFully(InputStream inputStream, String charsetName)
			throws EReceiptException {
		StringWriter stringWriter = new StringWriter();
		InputStreamReader inputStreamReader = null;
		try {
			if (StringUtils.isEmpty(charsetName)) {
				charsetName = "UTF-8";
			}
			inputStreamReader = new InputStreamReader(inputStream, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new EReceiptException("不支持的字符集 " + charsetName,e);
		}
		try {
			copy(inputStreamReader, stringWriter);
			return stringWriter.toString();
		} catch (IOException e) {
			throw new EReceiptException("读取流失败 ",e);
		} finally {
			try {
				stringWriter.close();
			} catch (IOException e) {
				throw new EReceiptException(e);
			}
		}
	}

	public static String readFullyWithoutClose(InputStream inputStream,
			String charsetName) throws EReceiptException {
		StringWriter stringWriter = new StringWriter();
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new EReceiptException("不支持的字符集 " + charsetName,e);
		}
		try {
			copy(inputStreamReader, stringWriter);
			return stringWriter.toString();
		} catch (IOException e) {
			throw new EReceiptException("读取流失败 ",e);
		}
	}

	private static void write(String data, OutputStream output,
			String charsetName) throws EReceiptException {
		if (data == null)
			throw new EReceiptException("要发送的内容为null! ");
		try {
			output.write(data.getBytes(charsetName));
			output.flush();
		} catch (UnsupportedEncodingException e) {
			throw new EReceiptException("不支持的字符集 " + charsetName,e);
		} catch (IOException e) {
			throw new EReceiptException("写入流失败 ",e);
		}
	}

	public static void write(byte[] data, OutputStream output)
			throws IOException {
		if (data != null)
			output.write(data);
	}

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L) {
			return -1;
		}
		return (int) count;
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[4096];
		long count = 0L;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copyLarge(Reader input, Writer output)
			throws IOException {
		char[] buffer = new char[4096];
		long count = 0L;
		int length = 0;
		while (-1 != (length = input.read(buffer))) {
			output.write(buffer, 0, length);
			count += length;
		}
		return count;
	}

	public static void closeOutputStreamQuietly(OutputStream output) {
		try {
			if (output != null)
				output.close();
		} catch (IOException localIOException) {
		}
	}

	public static void closeInputStreamQuietly(InputStream input) {
		try {
			if (input != null)
				input.close();
		} catch (IOException localIOException) {
		}
	}

}

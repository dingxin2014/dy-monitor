package com.dooioo.ftp;

import java.io.Closeable;

public interface IFtpClient extends Closeable {

	public byte[] getBytes(String fileName) throws Exception;
	
	public void download(String fileName, String outputPath) throws Exception;
}

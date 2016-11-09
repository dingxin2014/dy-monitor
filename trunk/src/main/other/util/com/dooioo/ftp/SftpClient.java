package com.dooioo.ftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.dooioo.ereceipt.exception.EReceiptException;
import com.dooioo.util.Assert;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Sftp client
 * @author dingxin
 *
 */
public class SftpClient implements IFtpClient {
	
	private String host;
	private int port;
	private String userName;
	private String password;
	private ChannelSftp channelSftp;
	private Channel channel;
	private Session session;
	
	private boolean init = false;
	
	@SuppressWarnings("unused")
	private SftpClient(){}
	
	public SftpClient(String host,int port,String userName,String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
	
	public void init() throws JSchException{
		JSch jsch = new JSch();
		session = jsch.getSession(userName, host, port);
		session.setPassword(password);
		session.setTimeout(10000);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

		channel = session.openChannel("sftp");
		channel.connect();
		this.channelSftp = (ChannelSftp) channel;
		
		init = true;
	}
	
	private ChannelSftp getChannelSftp() throws Exception{
		if(!init)
			try {
				init();
			} catch (JSchException e) {
				throw new EReceiptException("connect sftp server [host = "+host+" port = "+port+"] failed!", e);
			}
		Assert.notNull(channelSftp, "连接 Sftp 服务器失败！"+" host = "+host+" port ="+port);
		return channelSftp;
	}
	
	@Override
	public byte[] getBytes(String fileName) throws Exception{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		InputStream in = null;
		try{
			in = getChannelSftp().get(fileName);
			byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buffer))) {
				output.write(buffer, 0, n);
			}
			output.flush();
		}finally{
			if(in != null)
				in.close();
		}
		return output.toByteArray();
	}
	

	@Override
	public void download(String fileName, String outputPath) throws Exception {
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			in = getChannelSftp().get(fileName);
			fos = new FileOutputStream(new File(outputPath));
			byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buffer))) {
				fos.write(buffer, 0, n);
			}
			fos.flush();
		} 
		finally{
			if(fos != null)
				fos.close();
			if(in != null)
				in.close();
		}
	}
	
	@Override
	public void close() throws IOException {
		if(channelSftp != null && channelSftp.isConnected())
			channelSftp.quit();
		if(channel != null && channel.isConnected())
			channel.disconnect();
		if(session != null && session.isConnected())
			session.disconnect();
		init = false;
	}


}

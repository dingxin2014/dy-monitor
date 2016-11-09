package com.dooioo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 压缩工具类
 * 
 * @author dingxin
 *
 */
public class ZipUtil {

	private static Log logger = LogFactory.getLog(ZipUtil.class);

	public static void zip(String sourceFile) throws Exception {
		logger.info("压缩中...");
		StringTokenizer st = new StringTokenizer(sourceFile, ".");
		String zipFile = st.nextToken() + ".zip";
		File inputFile = new File(sourceFile);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // 输出流关闭
		logger.info("压缩完成");
	}

	public static void zip(String sourceFile, String zipFile) throws FileNotFoundException, IOException {
		logger.info("压缩 " + sourceFile);
		File inputFile = new File(sourceFile);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // 输出流关闭
		logger.info("压缩完成");
	}

	private static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws IOException { // 方法重载
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + File.separatorChar)); // 创建zip压缩进入点base
				logger.info(base + File.separatorChar);
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + File.separatorChar + fl[i].getName(), bo); // 递归遍历子文件夹
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			logger.info(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
			in.close(); // 输入流关闭
		}
	}

	/**
	 * 解压缩
	 * 
	 * @param zipFile
	 * @param outputPath
	 *            输出路径
	 */
	public static void gunzip(String zipFile, String outputPath) {
		try {
			ZipInputStream Zin = new ZipInputStream(new FileInputStream(zipFile));// 输入源zip路径
			BufferedInputStream Bin = new BufferedInputStream(Zin);
			File Fout = null;
			ZipEntry entry;
			try {
				while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
					Fout = new File(outputPath, entry.getName());
					if (!Fout.exists()) {
						(new File(Fout.getParent())).mkdirs();
					}
					FileOutputStream out = new FileOutputStream(Fout);
					BufferedOutputStream Bout = new BufferedOutputStream(out);
					int b;
					while ((b = Bin.read()) != -1) {
						Bout.write(b);
					}
					Bout.close();
					out.close();
					logger.info(Fout + " 解压成功");
				}
				Bin.close();
				Zin.close();
			} catch (IOException e) {
				logger.info("--->" + e.getMessage());
			}
		} catch (FileNotFoundException e) {
			logger.info("--->" + e.getMessage());
		}
	}

	public static Map<String, byte[]> gunzip(byte[] bytes) {
		Map<String, byte[]> map = new HashMap<>();
		byte[] b = null;
		ZipInputStream Zin = new ZipInputStream(new ByteArrayInputStream(bytes));
		BufferedInputStream Bin = new BufferedInputStream(Zin);
		ZipEntry entry;
		try {
			while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int n = 0;
				while (-1 != (n = Bin.read(buffer))) {
					output.write(buffer, 0, n);
				}
				b = output.toByteArray();
				map.put(entry.getName(), b);
				output.close();
				logger.info(entry.getName() + "解压成功！");
			}
			Bin.close();
			Zin.close();
		} catch (IOException e) {
			logger.info("--->" + e.getMessage());
		}
		return map;
	}

	public static void gunzip(byte[] bytes, String outputPath) {
		ZipInputStream Zin = new ZipInputStream(new ByteArrayInputStream(bytes));
		BufferedInputStream Bin = new BufferedInputStream(Zin);
		ZipEntry entry;
		File Fout;
		try {
			while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
				Fout = new File(outputPath, entry.getName());
				if (!Fout.exists()) {
					(new File(Fout.getParent())).mkdirs();
				}
				FileOutputStream out = new FileOutputStream(Fout);
				BufferedOutputStream Bout = new BufferedOutputStream(out);
				int b;
				while ((b = Bin.read()) != -1) {
					Bout.write(b);
				}
				Bout.close();
				out.close();
				logger.info(entry.getName() + "解压成功！");
			}
			Bin.close();
			Zin.close();
		} catch (IOException e) {
			logger.info("--->" + e.getMessage());
		}
	}

}

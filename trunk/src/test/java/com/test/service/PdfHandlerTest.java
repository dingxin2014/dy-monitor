/**
 * 
 */
package com.test.service;

import java.io.File;

import com.dooioo.util.PdfHandle;


/**
 * @author lixiaobo
 *
 */
public class PdfHandlerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void test() throws Exception{
		try {
			File file = new File("/Users/mac/Desktop/");
			File[] files = file.listFiles();
			String fileName;
			String suffix;
			String sc;
			String s = "业务编号：0000";
			for(File f : files){
				fileName = f.getName();  
		        suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();  
				if("pdf".equals(suffix)){
					sc = PdfHandle.getText(f.getAbsolutePath());
					if(sc!=null&&sc.length()>0&&sc.contains(s)){
						int sLength = s.length();
						int a = sc.lastIndexOf(s)+sLength;
						System.out.println(sc.substring(a, a+8));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

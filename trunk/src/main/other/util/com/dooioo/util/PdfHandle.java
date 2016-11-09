/**
 * 
 */
package com.dooioo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfHandle {
	public static String getText(String file) {
		String s = "";
		File pdffile = new File(file);
		PDDocument pdfdoc = null;
		try {
			pdfdoc = PDDocument.load(pdffile);
			PDFTextStripper stripper = new PDFTextStripper();
			s = stripper.getText(pdfdoc);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pdfdoc != null) {
					pdfdoc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	
	public static String getText(byte[] bytes){
		String s = "";
		PDDocument pdfdoc = null;
		try {
			pdfdoc = PDDocument.load(bytes);
			PDFTextStripper stripper = new PDFTextStripper();
			s = stripper.getText(pdfdoc);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pdfdoc != null) {
					pdfdoc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	public static void toTextFile(String doc, String filename) throws Exception {
		String pdffile = doc;
		PDDocument pdfdoc = PDDocument.load(new File(doc));
		try {
			pdfdoc = PDDocument.load(new File(pdffile));
			PDFTextStripper stripper = new PDFTextStripper();
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			stripper.writeText(pdfdoc, pw);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pdfdoc != null) {
					pdfdoc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("/Users/liguohui/lianjia/中行ftp/293517309_20161009_7802_121212/");
			File[] files = file.listFiles();
			for(File f : files){
				System.out.println("file---"+f);
				String sc = getText(f.getAbsolutePath());
				System.out.print(sc);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

/**
 * 
 */
package com.test.service;

import com.dooioo.fs.facade.Category;
import com.dooioo.fs.facade.IFsFacade;
import com.dooioo.fs.facade.UploadRequest;
import com.test.base.BaseTest;
import org.apache.commons.fileupload.FileUploadException;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.*;
/**
 * @author lixiaobo
 *
 */
public class UploadFileTest extends BaseTest implements ApplicationContextAware {

	@Autowired
	private IFsFacade iFsFacade;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		iFsFacade = context.getBean(IFsFacade.class);
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void uploadFileTest() throws Exception{
		try {
			byte[] b = getBytes("/Users/mac/Desktop/改数据库字段可以为NULL.txt");
			UploadRequest uploadRequest = new UploadRequest();
			uploadRequest.setCategory(Category.OTHER);
			uploadRequest.setDomain("rents");
			uploadRequest.setFileName("test");
			uploadRequest.setFileprops(null);
			uploadRequest.setRemark("aaa");
			com.dooioo.fs.facade.UploadResult newResult=iFsFacade.upload(b,uploadRequest);
			if(newResult.getState() == com.dooioo.fs.facade.UploadResult.STATE_FAIL){
				throw new FileUploadException(newResult.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
  

}

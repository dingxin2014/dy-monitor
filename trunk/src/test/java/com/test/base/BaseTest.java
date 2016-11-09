/**
 * 
 */
package com.test.base;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dooioo.context.util.SpringObjectFactory;

/**
 * @author liguohui
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/test-spring.xml"})
public class BaseTest extends TestCase {

	@Autowired
	protected ApplicationContext ctx;
	
	@BeforeClass
	public static void beforeRun(){
		System.setProperty("appPath", "eReceipt");
	}
	
	@Before
	public void before(){
		SpringObjectFactory.setApplicationContext(ctx);
	}
	
}

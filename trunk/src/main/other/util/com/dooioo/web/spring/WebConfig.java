package com.dooioo.web.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	private static final Log logger = LogFactory.getLog(WebConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		logger.info("----------------Add Resource--------------------------");
		registry.addResourceHandler("/images/**").addResourceLocations("/static/images/");
		registry.addResourceHandler("/js/**").addResourceLocations("/static/js/");
		registry.addResourceHandler("/css/**").addResourceLocations("/static/css/");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/");
	}

}

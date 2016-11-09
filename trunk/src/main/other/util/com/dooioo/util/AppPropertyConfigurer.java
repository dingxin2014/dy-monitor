package com.dooioo.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class AppPropertyConfigurer extends PropertyPlaceholderConfigurer {

	private static Log log = LogFactory.getLog(AppPropertyConfigurer.class);

	@Override
	protected Properties mergeProperties() throws IOException {

		Properties superProps = super.mergeProperties();
		String env = Configuration.getInstance().getEnv();
		superProps.put("env", env);
		String loadGitlab = Configuration.getInstance().getProperty(
				"load.jdbc.from.gitlab");
		log.info(">>>>loadGitlab=" + loadGitlab);
		if (loadGitlab != null && "1".equals(loadGitlab)) {
			Map<String, String> props = AppUtils.loadInternetResource(env);
			superProps.putAll(props);
		}
		Properties pros = Configuration.getInstance().getProperties();
		if(pros != null){
			superProps.putAll(pros);
		}
		log.info(">>>>>当前环境 env:" + superProps.getProperty("env"));
		return superProps;
	}

}

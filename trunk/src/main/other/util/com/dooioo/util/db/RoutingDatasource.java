package com.dooioo.util.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDatasource extends AbstractRoutingDataSource {

    private static final Log logger = LogFactory.getLog(RoutingDatasource.class);
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		String determineDatabase = RoutingDatasourceHolder.getCurrentDatabase();
		logger.debug("determineDatabase--"+determineDatabase);
		return determineDatabase;
	}

}

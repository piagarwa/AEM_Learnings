package com.sling.tv.core.services;

public interface OsgiConfigService {
	public interface SystemKeys {
		String SLING_ADDRESS_SCRUB_URL = "slingAddressScrubURL";
		String SLING_ANTENNA_SERVICE_URL = "slingAntennaServiceURL";
	}
	public String getSlingAddressScrubURL();
	public String getSlingAntennaServiceURL();
}

package com.sling.tv.core.services.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import com.sling.tv.core.services.OsgiConfigService;

@Component(immediate = true, metatype = true, label = "Sling TV : Environment System Settings based on OSGi Configuration", description = " This is a system settings based on OSGi Configuration used for Sling TV Application.")
@Service
public class OsgiConfigServiceImpl implements OsgiConfigService{

	@Property(name = SystemKeys.SLING_ADDRESS_SCRUB_URL, label = "Address Scrub URL", value = StringUtils.EMPTY, description = "Address Scrub End Point URL Used in OTA Component")
	private String slingAddressScrubURL = StringUtils.EMPTY;
	
	@Property(name = SystemKeys.SLING_ANTENNA_SERVICE_URL, label = "Antenna Service URL", value = StringUtils.EMPTY, description = "Antenna Recomendations Titan Service End Point URL Used in OTA Component")
	private String slingAntennaServiceURL = StringUtils.EMPTY;
	
	private Map<String, Object> props = new ConcurrentHashMap<String, Object>();
	
	/**
	 * ServiceLifeCycle method
	 * 
	 * @param props
	 */
	@Activate
	@Modified
	protected void init(final Map<String, Object> props) {
		synchronized (this.props) {
			if (props != null) {
				Set<Entry<String, Object>> entries = props.entrySet();
				for (Entry<String, Object> entry : entries) {
					this.props.put(entry.getKey(), entry.getValue());
				}
				this.slingAddressScrubURL = castToType(props.get(SystemKeys.SLING_ADDRESS_SCRUB_URL), StringUtils.EMPTY);
				this.slingAntennaServiceURL = castToType(props.get(SystemKeys.SLING_ANTENNA_SERVICE_URL), StringUtils.EMPTY);
			}
		}
	}
	
	@Override
	public String getSlingAddressScrubURL() {
		
		return this.slingAddressScrubURL;
	}
	
	@Override
	public String getSlingAntennaServiceURL() {
		
		return this.slingAntennaServiceURL;
	}
	/**
     * 
     * @param obj
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T castToType(final Object obj, final T defaultValue) {
        if (obj != null) {
            if (defaultValue == null) {
                return (T) obj;
            } else {
                if (defaultValue.getClass().isInstance(obj)) {
                    return (T) defaultValue.getClass().cast(obj);
                }
                if (obj instanceof String && defaultValue instanceof Boolean) {
                    return (T) Boolean.valueOf((String) obj);
                }
            }
        }
        return defaultValue;
    }

}

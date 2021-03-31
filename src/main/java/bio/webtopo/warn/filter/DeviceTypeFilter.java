package bio.webtopo.warn.filter;

import RemoteDB.AlarmEvent;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn 版权：南京联创科技 网管科技部
 * 
 */
public class DeviceTypeFilter extends AbstractFilter {
    private int deviceType = 0;

    public DeviceTypeFilter(String deviceType, BaseOperation operation) {
	super(String.valueOf(deviceType), operation);
	deviceType = deviceType.substring(0, deviceType.indexOf("/"));
	this.deviceType = Integer.parseInt(deviceType);
	setType("设备型号");
    }

    public boolean accept(AlarmEvent event) {
	return this.getOperation().compareTo(deviceType, event.m_DeviceType);
    }
}

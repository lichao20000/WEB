package bio.webtopo.warn.column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.AlarmEvent;
import bio.webtopo.warn.filter.ConstantEventEnv;
/**
 * 设备型号
 * @author Administrator
 *
 */
public class _DeviceType extends ColumnObject {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(_DeviceType.class);
	public _DeviceType(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		/**
		 * 设备型号
		 */
		logger.debug("################"+e.m_strDevType);
		return "<td class=td_r>"+ConstantEventEnv.getDeviceModelBySerial(e.m_strDevType)+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

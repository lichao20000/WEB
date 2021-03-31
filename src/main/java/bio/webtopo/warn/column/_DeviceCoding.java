package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 设备ID
 * @author Administrator
 *
 */
public class _DeviceCoding extends ColumnObject {

	public _DeviceCoding(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_DeviceCoding+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

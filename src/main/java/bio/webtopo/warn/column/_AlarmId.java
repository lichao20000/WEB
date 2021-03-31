package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 告警ID
 * @author Administrator
 *
 */
public class _AlarmId extends ColumnObject {

	public _AlarmId(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_AlarmId+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

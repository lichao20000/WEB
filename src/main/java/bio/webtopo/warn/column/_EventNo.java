package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 事件ID
 * @author Administrator
 *
 */
public class _EventNo extends ColumnObject {

	public _EventNo(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_EventNo+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 小区
 * @author Administrator
 *
 */
public class _StrZone extends ColumnObject {

	public _StrZone(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_strZone+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

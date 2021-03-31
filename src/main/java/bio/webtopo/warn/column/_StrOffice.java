package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 局向
 * @author Administrator
 *
 */
public class _StrOffice extends ColumnObject {

	public _StrOffice(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_strOffice+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 告警序列号
 * @author Administrator
 *
 */
public class _Number extends ColumnObject {

	public _Number(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_Number+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

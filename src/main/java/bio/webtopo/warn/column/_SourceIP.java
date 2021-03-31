package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 事件源网元的IP地址
 * @author Administrator
 *
 */
public class _SourceIP extends ColumnObject {

	public _SourceIP(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_SourceIP+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

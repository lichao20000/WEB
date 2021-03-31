package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 事件源网元的名称
 * @author Administrator
 *
 */
public class _SourceName extends ColumnObject {

	public _SourceName(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_SourceName+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

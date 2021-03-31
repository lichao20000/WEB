package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 采集点ID
 * @author Administrator
 *
 */
public class _GatherID extends ColumnObject {

	public _GatherID(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_GatherID+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

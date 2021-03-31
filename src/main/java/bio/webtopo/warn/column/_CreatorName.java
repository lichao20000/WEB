package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 创建事件网元的名称
 * @author Administrator
 *
 */
public class _CreatorName extends ColumnObject {

	public _CreatorName(String id, String name, boolean visible) {
		super(id, name, visible);
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_CreatorName+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}
}

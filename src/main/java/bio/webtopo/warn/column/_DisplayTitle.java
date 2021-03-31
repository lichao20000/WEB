package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 事件显示的标题
 * @author Administrator
 *
 */
public class _DisplayTitle extends ColumnObject {

	public _DisplayTitle(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_DisplayTitle+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

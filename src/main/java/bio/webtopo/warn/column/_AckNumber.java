package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 确认的事件序列号
 * @author Administrator
 *
 */
public class _AckNumber extends ColumnObject {

	public _AckNumber(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r>"+e.m_AckNumber+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 事件显示的内容
 * @author Administrator
 *
 */
public class _DisplayString extends ColumnObject {

	public _DisplayString(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=e_c><div class=content onclick=$.divshow($(this))>"+e.m_DisplayString+"</div></td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

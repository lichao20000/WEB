package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 获得属地
 * @author Administrator
 *
 */
public class _CityColumn extends ColumnObject {

	public _CityColumn(String id, String name, boolean visible) {
		super(id, name, visible);
	}

	public String getValue(AlarmEvent e) {
	    	//样式 td_r 不可换行
		return "<td class=td_r>"+e.m_strCity+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

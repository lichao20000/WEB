package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 业务平台id
 * @author Administrator
 *
 */
public class _Buss_id extends ColumnObject {

	public _Buss_id(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		//return "<td class=td_r>"+e.buss_id+"</td>";
		return "<td class=td_r> </td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

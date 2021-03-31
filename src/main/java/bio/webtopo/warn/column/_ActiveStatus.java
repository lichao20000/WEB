package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
import bio.webtopo.warn.filter.ConstantEventEnv;
/**
 * 告警状态
 * @author Administrator
 *
 */
public class _ActiveStatus extends ColumnObject {

	public _ActiveStatus(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r name=astu>"+ConstantEventEnv.STATUS_NAME_MAP.get((int)e.m_ActiveStatus)+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

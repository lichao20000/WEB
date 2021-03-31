package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
import bio.webtopo.warn.filter.ConstantEventEnv;
/**
 * 告警等级
 * @author Administrator
 *
 */
public class _Severity extends ColumnObject {

	public _Severity(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r name=lev>"+ConstantEventEnv.NUM_LEVEL_MAP.get((int)e.m_Severity)+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

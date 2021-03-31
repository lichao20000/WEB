package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
import bio.webtopo.warn.filter.ConstantEventEnv;
/**
 * 清除状态(1 未清除 2 自动清除  3 手工清除)
 * @author Administrator
 *
 */
public class _ClearStatus extends ColumnObject {

	public _ClearStatus(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r name=cs>"+ConstantEventEnv.STATUS_CLEAR_MAP.get((int)e.m_ClearStatus)+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

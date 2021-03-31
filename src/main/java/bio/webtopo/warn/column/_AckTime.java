package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;

import com.linkage.litms.common.util.StringUtils;
/**
 * 确认时间
 * @author Administrator
 *
 */
public class _AckTime extends ColumnObject {

	public _AckTime(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		return "<td class=td_r name=atime>"+(e.m_AckTime==0?"":StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",e.m_AckTime))+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

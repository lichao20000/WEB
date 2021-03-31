package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;

import com.linkage.litms.common.util.StringUtils;
/**
 * 告警时间
 * @author Administrator
 *
 */
public class _CreateTime extends ColumnObject{
	public _CreateTime(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
	    	//样式 td_r 不可换行
		return "<td class=td_r>"+StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",e.m_CreateTime)+"</td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}
}

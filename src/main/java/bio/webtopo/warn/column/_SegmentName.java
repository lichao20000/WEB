package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;
/**
 * 告警所属云团名称(网络子图)
 * @author Administrator
 *
 */
public class _SegmentName extends ColumnObject {

	public _SegmentName(String id, String name, boolean visible) {
		super(id, name, visible);
		// TODO Auto-generated constructor stub
	}

	public String getValue(AlarmEvent e) {
		// TODO Auto-generated method stub
		//return "<td class=td_r>"+e.segmentName+"</td>";
		return "<td class=td_r> </td>";
	}

	public boolean isvisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

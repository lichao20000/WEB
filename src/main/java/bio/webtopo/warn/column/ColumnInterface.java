package bio.webtopo.warn.column;

import RemoteDB.AlarmEvent;

public interface ColumnInterface {
	//获取列的值
	public String getValue(AlarmEvent e);
	//获取列对应的id
	public String getId();
	//获取列对应的名称
	public String getName();
	//获取列的可见性
	public boolean isvisible();
}

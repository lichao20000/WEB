package bio.webtopo.warn.filter;

import RemoteDB.AlarmEvent;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn 版权：南京联创科技 网管科技部
 * 
 */
public class EventOidFilter extends AbstractFilter
{
	public EventOidFilter(String value,BaseOperation operation)
	{
		super(value,operation);
		setType("事件类型");
	}
	public boolean accept(AlarmEvent event)
	{
		return this.getOperation().compareTo(getValue(),event.m_EventNo);
	}
}

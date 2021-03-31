package bio.webtopo.warn.filter;

import RemoteDB.AlarmEvent;

/**
 * 新增按属地过滤的过滤器
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-4-16
 * @category com.linkage.liposs.bio.webtopo.warn
 * @copyright 南京联创科技 网管科技部
 */
public class CityFilter extends AbstractFilter
{
	public CityFilter(String value, BaseOperation operation)
	{
		super(value, operation);
		setType("属地");
	}

	public boolean accept(AlarmEvent event)
	{
		return this.getOperation().compareTo(getValue(), event.m_strCity);
	}
}

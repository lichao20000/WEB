package bio.webtopo.warn.filter;

import RemoteDB.AlarmEvent;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn
 * 版权：南京联创科技 网管科技部
 *
 */
public class LevelFilter extends AbstractFilter
{
	private int level = 0;
	public LevelFilter(String level,BaseOperation operation){
		super(level, operation);
		this.level = ConstantEventEnv.LEVEL_MAP.get(level);
		setType("告警等级");
	}
	public boolean accept(AlarmEvent event)
	{
		return getOperation().compareTo(event.m_Severity, level);
	}
	public String toString(){
		return this.getType() + getOperation().toString() + this.level;
	}
}

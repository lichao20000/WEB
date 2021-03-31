package bio.webtopo.warn.filter;
/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn
 * 版权：南京联创科技 网管科技部
 *
 */
public class RuleDetail
{
	/**
	 * 
	 */
	private int type = 0;
	/**
	 * 用户定义的名称：设备IP 设备名称 事件OID...
	 */
	private String key = null;
	/**
	 * 用户定义的值：严重告警（4） (病毒事件)1.3.6.1.4.1.10293.100.18.1
	 */
	private String value = null;
	
	private int intValue = 0;
	/**
	 * >= <= > < != =
	 */
	private String operation = null;
	
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getOperation()
	{
		return operation;
	}
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	public int getType()
	{
		return type;
	}
	/**
	 * 如果是空的
	 * @param type
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	public int getIntValue()
	{
		return intValue;
	}
	public void setIntValue(int intValue)
	{
		this.intValue = intValue;
	}
}

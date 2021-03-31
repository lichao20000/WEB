package bio.webtopo.warn.filter;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn 版权：南京联创科技 网管科技部
 * 
 */
public abstract class AbstractFilter implements BaseFilter
{
	private String type = null;
	private String value = null;
	private BaseOperation operation = null;
	
	public AbstractFilter(String value,BaseOperation operation)
	{
		if (value == null)
			throw new NullPointerException("值不能为空!!!");
		if(operation == null)
			throw new NullPointerException("BaseOperation不能为空.");
		this.value = value;
		this.operation = operation;
	}
	public String getValue()
	{
		return value;
	}
	public BaseOperation getOperation()
	{
		return operation;
	}
	public void setOperation(BaseOperation operation)
	{
		if (operation == null)
			throw new NullPointerException("operation不能为空!!!");
		this.operation = operation;
	}
	public void setValue(String value)
	{
		if (value == null)
			throw new NullPointerException("值不能为空!!!");
		this.value = value;
	}
	public String toString(){
		return this.getType() + operation.toString() + this.value;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
}

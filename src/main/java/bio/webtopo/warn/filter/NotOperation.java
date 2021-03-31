package bio.webtopo.warn.filter;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category 
 * 版权：南京联创科技 网管科技部
 *
 */
public class NotOperation extends BaseOperation
{
	private BaseOperation operation = null;
	public NotOperation(BaseOperation oper){
		this.operation = oper;
	}
	public boolean compareTo(int value1, int value2)
	{
		return !operation.compareTo(value1, value2);
	}
	public boolean compareTo(String value1, String value2)
	{
		return !operation.compareTo(value1, value2);
	}
	public String toString(){
		return "!=";
	}
}

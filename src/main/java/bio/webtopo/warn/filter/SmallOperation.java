package bio.webtopo.warn.filter;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category 
 * 版权：南京联创科技 网管科技部
 *
 */
public class SmallOperation extends BaseOperation
{

	public boolean compareTo(int value1, int value2)
	{
			return value1 - value2 < 0;
	}
	public String toString(){
		return "<";
	}
}

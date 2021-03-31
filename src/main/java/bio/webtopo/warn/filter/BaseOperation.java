package bio.webtopo.warn.filter;
/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category 版权：南京联创科技 网管科技部
 * 
 */
public abstract class BaseOperation
{
	/**
	 * 数值比较
	 * @param value1
	 * @param value2
	 * @return
	 */
	public boolean compareTo(int value1, int value2){
		return false;
	}
	/**
	 * 字符串比较
	 * @param value1
	 * @param value2
	 * @return
	 */
	public boolean compareTo(String value1, String value2){
		return false;
	}
}

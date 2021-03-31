package bio.webtopo.warn.filter;


/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn
 * 版权：南京联创科技 网管科技部
 *
 */
public abstract class AbstractLogic implements BaseFilter
{
	private BaseFilter leftFilter = null;
	private BaseFilter rightFilter = null;
	
	public AbstractLogic(BaseFilter l,BaseFilter r){
		if(l == null)
			throw new NullPointerException("左边filter不能为NULL");
		
		this.leftFilter = l;
		this.rightFilter = r;
	}
	public BaseFilter getLeftFilter()
	{
		return leftFilter;
	}
	public BaseFilter getRightFilter()
	{
		return rightFilter;
	}
}

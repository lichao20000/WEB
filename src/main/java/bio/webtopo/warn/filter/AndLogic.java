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
public class AndLogic extends AbstractLogic
{
	public AndLogic(BaseFilter l,BaseFilter r)
	{
		super(l,r);
	}
	public boolean accept(AlarmEvent event)
	{
		//return this.getLeftFilter().accept(event) && (this.getRightFilter() == null ? true : this.getRightFilter().accept(event));
	    return this.getLeftFilter().accept(event) && this.getRightFilter().accept(event);
	}
	public String toString(){
	/*if(this.getRightFilter() == null){
		return  this.getLeftFilter().toString();
	}*/
	    // //如果右边的filter对象为默认的TRUEFILTER
	    return "{"+this.getLeftFilter().toString() + ( this.getRightFilter() == ConstantEventEnv.TRUEFILTER ? "}" : "} 并且 " + this.getRightFilter().toString()); 
	}
}

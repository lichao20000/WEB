package action.hgwip;

import static action.cst.AJAX;
import bio.hgwip.SubnetTreeBIO;

import com.opensymphony.xwork2.ActionSupport;

;
/**
 * ip地址管理的ip详情查询的入口
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-09
 * @category ipmg
 */
public class GetIpInfoAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4817530357283364774L;
	private String subnet_len;// 网段加掩码长度
	private String ajax;
	private SubnetTreeBIO subdio;// 类树
	@Override
	public String execute() throws Exception
	{
		return SUCCESS;
	}
	public String getMenu() throws Exception
	{
		return INPUT;
	}
	public String getTree() throws Exception
	{
		String[] pas = subnet_len.split("/");
		ajax = subdio.getTreeStr(pas[0], pas[2], Integer.parseInt(pas[1]), Integer.parseInt(pas[3]));
		return AJAX;
	}
	public String getSubnet_len()
	{
		return subnet_len;
	}
	public void setSubnet_len(String subnet_len)
	{
		this.subnet_len = subnet_len;
	}
	public void setSubdio(SubnetTreeBIO subdio)
	{
		this.subdio = subdio;
	}
	public String getAjax()
	{
		return ajax;
	}
}

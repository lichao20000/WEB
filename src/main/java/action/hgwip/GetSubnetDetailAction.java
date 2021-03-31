package action.hgwip;

import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import dao.hgwip.SubnetOperationDAO;

/**
 * ip地址管理，获取网段详细信息的action。该action为其他页面提供组件服务
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
public class GetSubnetDetailAction extends ActionSupport
{
	/**
	 * 
	 */
	private SubnetOperationDAO sbdao;// dao类，为了提供子网详情的访问格式
	private static final long serialVersionUID = -3200479361063925868L;// 序列号
	private Map<String, String> subnetDetail;// 子网信息
	private String attr;// 属性值 userstat/subnet/subnetgrp/inetmask/assign/leaf
	@Override
	public String execute() throws Exception
	{
		String[] params = attr.split("/");
		subnetDetail = sbdao.getDetailSubnet(params[1], params[2], Integer
				.parseInt(params[3]));
		return SUCCESS;
	}
	public Map<String, String> getSubnetDetail()
	{
		return subnetDetail;
	}
	public void setSbdao(SubnetOperationDAO sbdao)
	{
		this.sbdao = sbdao;
	}
	public void setAttr(String attr)
	{
		this.attr = attr;
	}
}

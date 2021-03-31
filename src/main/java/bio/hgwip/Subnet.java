package bio.hgwip;

import java.util.Map;

import com.linkage.litms.common.util.DateTimeUtil;


/**
 * 网络地址的数据类
 * @author wangp
 *
 */
public class Subnet
{
	/**
	 * 爷节点
	 */
	private String grandGrp="";

	/**
	 * 父节点
	 */
	private String subnetGrp;

	/**
	 * 父节点的掩码位数
	 */
	private int igroupMask=0;

	/**
	 * 网络地址
	 */
	private String subnet;

	/**
	 * 子网掩码位数
	 */
	private int inetMask;

	/**
	 * 子网掩码
	 */
	private String netMask;

//	/**
//	 * 广播地址
//	 */
//	private String broadcastAddr;
//
	/**
	 * 最小地址
	 */
	private String lowAddr;

	/**
	 * 最大地址
	 */
	private String highAddr;

	/**
	 * 地址数
	 */
	private int totalAddrNum;

	/**
	 * 子网个数,默认子网个数为0
	 */
	private int childCount=0;

	/**
	 * 分配状态，默认未分配
	 */
	private int assign=IPGlobal.NOT_ASSIGN;

	/**
	 * 邮件状态，默认无需发送
	 */
	private int mailStatus=IPGlobal.NOT_SENDMAIL;

	/**
	 * 网段所属的地市ID，与tab_city中的city_id关联
	 */
	private String city_id;

	/**
	 * 备注
	 */
	private String comment="";

	/**
	 * 是否同意分配此地址
	 */
	private int approve=IPGlobal.NOT_CHECK;

	/**
	 * 用途
	 */
	private String purpose="";

	/**
	 * 分配时间
	 */
	private String assignTime;

	/**
	 * ip地址
	 */
	private String fip;

	/**
	 * 最大地址
	 */
	private String fhighaddress;

	/**
	 * 最小地址
	 */
	private String flowaddress;
	
	public Subnet()
	{
		
	}
	
	/**
	 * 根据map（数据库查询结果）获取subnet对象
	 * @param subnetMap
	 */
	public Subnet(Map<String,String> subnetMap)
	{
		this.subnet=subnetMap.get("subnet");
		this.subnetGrp=subnetMap.get("subnetgrp");
		this.inetMask = Integer.parseInt(subnetMap.get("inetmask"));
		if(null!=subnetMap.get("grandgrp"))
		{
			this.grandGrp=subnetMap.get("grandgrp");
		}
		if(null!=subnetMap.get("igroupmask"))
		{
			this.igroupMask=Integer.parseInt(subnetMap.get("igroupmask"));
		}
		
		if(null!=subnetMap.get("approve"))
		{
			this.approve = Integer.parseInt(subnetMap.get("approve"));
		}
		
		if(null!=subnetMap.get("assign"))
		{
			this.assign = Integer.parseInt(subnetMap.get("assign"));
		}
		
		this.assignTime = new DateTimeUtil(Long.parseLong(subnetMap.get("assigntime"))*1000).getDate();
		if(null!=subnetMap.get("purpose"))
		{
			this.purpose = subnetMap.get("purpose");
		}
		
		this.fhighaddress = subnetMap.get("fhighaddress");
		this.fip = subnetMap.get("fip");
	    this.flowaddress = subnetMap.get("flowaddress");
	    if(null!=subnetMap.get("subnetcomment"))
	    {
	    	this.comment = subnetMap.get("subnetcomment");
	    }
	    this.lowAddr=subnetMap.get("lowaddr");
	    this.highAddr=subnetMap.get("highaddr");
	    if(null!=subnetMap.get("childcount"))
	    {
	    	this.childCount = Integer.parseInt(subnetMap.get("childcount"));
	    }
	    if(null!=subnetMap.get("totaladdr"))
	    {
	    	this.totalAddrNum=Integer.parseInt(subnetMap.get("totaladdr"));
	    }
	    if(null!=subnetMap.get("mailstatus"))
	    {
	    	this.mailStatus=Integer.parseInt(subnetMap.get("mailstatus"));
	    }
	    
	    this.city_id=subnetMap.get("city_id");
	    this.netMask = subnetMap.get("netmask");		
	}
	
	
	public Subnet(String subnetGrp,int inetMask,String subnet)
	{
		this.subnetGrp=subnetGrp;
		this.inetMask=inetMask;
		this.subnet = IpTool.getLowAddr(subnet,inetMask);
		this.lowAddr=this.subnet;
		this.highAddr=IpTool.getHighAddr(subnet,inetMask);
		this.fip=IpTool.getFillIP(this.lowAddr);
		this.fhighaddress=IpTool.getFillIP(this.highAddr);
		this.flowaddress=this.fip;
		this.totalAddrNum = IpTool.getHostNumber(inetMask);
		this.netMask= IpTool.getNetMask(inetMask);	
		
	}   
	

	/**
	 * 省局是否同意分配这个地址
	 * 
	 * @return
	 */
	public int getApprove()
	{
		return approve;
	}

	public void setApprove(int approve)
	{
		this.approve = approve;
	}

	/**
	 * 分配状态
	 * 
	 * @return
	 */
	public int getAssign()
	{
		return assign;
	}

	public void setAssign(int assign)
	{
		this.assign = assign;
	}

	/**
	 * 分配时间
	 * 
	 * @return
	 */
	public String getAssignTime()
	{
		return assignTime;
	}

	public void setAssignTime(String assignTime)
	{
		this.assignTime = assignTime;
	}

//	/**
//	 * 广播地址
//	 * 
//	 * @return
//	 */
//	public String getBroadcastAddr()
//	{
//		return broadcastAddr;
//	}
//
//	public void setBroadcastAddr(String broadcastAddr)
//	{
//		this.broadcastAddr = broadcastAddr;
//	}

	/**
	 * 子网个数
	 * 
	 * @return
	 */
	public int getChildCount()
	{
		return childCount;
	}

	public void setChildCount(int childCount)
	{
		this.childCount = childCount;
	}

	/**
	 * 地址所属地址ID
	 * 
	 * @return
	 */
	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	/**
	 * 备注
	 * 
	 * @return
	 */
	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * 最大IP地址
	 * 
	 * @return
	 */
	public String getFhighaddress()
	{
		return fhighaddress;
	}

	public void setFhighaddress(String fhighaddress)
	{
		this.fhighaddress = fhighaddress;
	}

	/**
	 * IP地址
	 * 
	 * @return
	 */
	public String getFip()
	{
		return fip;
	}

	public void setFip(String fip)
	{
		this.fip = fip;
	}

	/**
	 * 最小IP地址
	 * 
	 * @return
	 */
	public String getFlowaddress()
	{
		return flowaddress;
	}

	public void setFlowaddress(String flowaddress)
	{
		this.flowaddress = flowaddress;
	}

	/**
	 * 爷节点
	 * 
	 * @return
	 */
	public String getGrandGrp()
	{
		return grandGrp;
	}

	public void setGrandGrp(String grandGrp)
	{
		this.grandGrp = grandGrp;
	}

	/**
	 * 最大地址
	 * 
	 * @return
	 */
	public String getHighAddr()
	{
		return highAddr;
	}

	public void setHighAddr(String highAddr)
	{
		this.highAddr = highAddr;
	}

	/**
	 * 父节点的子网掩码位数
	 * 
	 * @return
	 */
	public int getIgroupMask()
	{
		return igroupMask;
	}

	public void setIgroupMask(int igroupMask)
	{
		this.igroupMask = igroupMask;
	}

	/**
	 * 子网掩码位数
	 * 
	 * @return
	 */
	public int getInetMask()
	{
		return inetMask;
	}

	public void setInetMask(int inetMask)
	{
		this.inetMask = inetMask;
	}

	/**
	 * 最小地址
	 * 
	 * @return
	 */
	public String getLowAddr()
	{
		return lowAddr;
	}

	public void setLowAddr(String lowAddr)
	{
		this.lowAddr = lowAddr;
	}

	/**
	 * 邮件状态
	 * 
	 * @return
	 */
	public int getMailStatus()
	{
		return mailStatus;
	}

	public void setMailStatus(int mailStatus)
	{
		this.mailStatus = mailStatus;
	}

	/**
	 * 子网掩码
	 * 
	 * @return
	 */
	public String getNetMask()
	{
		return netMask;
	}

	public void setNetMask(String netMask)
	{
		this.netMask = netMask;
	}

	/**
	 * 用途
	 * 
	 * @return
	 */
	public String getPurpose()
	{
		return purpose;
	}

	public void setPurpose(String purpose)
	{
		this.purpose = purpose;
	}

	/**
	 * 网络地址
	 * 
	 * @return
	 */
	public String getSubnet()
	{
		return subnet;
	}

	public void setSubnet(String subnet)
	{
		this.subnet = subnet;
	}

	/**
	 * 父节点
	 * 
	 * @return
	 */
	public String getSubnetGrp()
	{
		return subnetGrp;
	}

	public void setSubnetGrp(String subnetGrp)
	{
		this.subnetGrp = subnetGrp;
	}

	/**
	 * 地址总数
	 * 
	 * @return
	 */
	/**
	 * @return
	 */
	public int getTotalAddrNum()
	{
		return totalAddrNum;
	}

	public void setTotalAddrNum(int totalAddrNum)
	{
		this.totalAddrNum = totalAddrNum;
	}

}

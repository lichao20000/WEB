package bio.hgwip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.hgwip.SubnetOperationDAO;



public class SubnetTreeBIO
{
	private static final Logger LOG = LoggerFactory.getLogger(SubnetTreeBIO.class);
	
	//网段数据库操作类
	private SubnetOperationDAO subnetOperationDao;	

	
	/**
	 * 获取指定网段的所有子节点包括其自身的json数组
	 * @param subnet 网段的ip
	 * @param subnetGrp 网段的父节点
	 * @param inetMask  网段的子网掩码
	 * @param userState 用户状态 0:省局用户，1：地市用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTreeStr(String subnet,String subnetGrp,int inetMask,int userState)
	{
		List<Subnet> subnetList = subnetOperationDao.getAllChildSubnet(subnet,subnetGrp,inetMask);
		
		//整理网段表,key为subnet+"_"+subnetGrp+"_"+inetMask,value:网段
		LOG.debug("subnet_size:"+subnetList.size());
		HashMap<String,ArrayList<Subnet>> subnetMap = new HashMap(subnetList.size()-1);
		Subnet subnetObj = null;
		//没有子节点
		if(subnetList.size()>1)
		{
			String tempSubnetGrp="";
			String tempGrandGroup ="";
			int tempIgroupMask;			
			ArrayList<Subnet> childList =null;
			for(int i=1;i<subnetList.size();i++)
			{
				subnetObj=subnetList.get(i);			
				tempSubnetGrp = subnetObj.getSubnetGrp();
				tempGrandGroup = subnetObj.getGrandGrp();
				tempIgroupMask = subnetObj.getIgroupMask();
				
				//有这个节点的子节点在map中
				if(!subnetMap.containsKey(tempSubnetGrp+"_"+tempGrandGroup+"_"+tempIgroupMask))
				{
					childList = new ArrayList();
					childList.add(subnetObj);
				}
				//map中已经有这个节点的子节点
				else
				{
					childList =subnetMap.get(tempSubnetGrp+"_"+tempGrandGroup+"_"+tempIgroupMask);
					boolean isFind=false;
					for(int j=0;j<childList.size();j++)
					{
						if(subnetObj.getFip().compareTo(childList.get(j).getFip())<0)
						{
							isFind =true;
							childList.add(j,subnetObj);
							break;
						}								
					}
					//始终没找到比subnetObj大的节点
					if(!isFind)
					{
						childList.add(subnetObj);
					}
				}
				subnetMap.put(tempSubnetGrp+"_"+tempGrandGroup+"_"+tempIgroupMask,childList);
				
			}			
		}	
		
		//根节点
		subnetObj =subnetList.get(0);
		
		//clear
		subnetList = null;		
		
		String treeStr=getSubnetTreeStr(subnetObj,subnetMap,userState);
		LOG.debug("treeStr(subnet:"+subnet+"   subnetGrp:"+subnetGrp+"   inetMask:"+inetMask+")   "+treeStr);
		return treeStr;
		
	}
	
	/**
	 * 根据传入的根节点来获取包括其本身的所有子节点的json字符串
	 * @param root 根节点
	 * @param children  这个根节点的所有子节点信息，key：subnet+"_"+subnetGrp+"_"+inetMask
	 *                   value:subnet+"_"+subnetGrp+"_"+inetMask对应的网段的所有子节点组成的ArrayList
	 * @param userState 用户状态 0:省局用户，1：地市用户
	 * @return 包括root节点的json数组
	 */
	private String getSubnetTreeStr(Subnet root,HashMap<String,ArrayList<Subnet>> children,int userState)
	{
		return "["+getSubnetStr(root,children,userState)+"]";
	}
	
	/**
	 * 根据传入的根节点来获取包括其本身的所有子节点的json字符串
	 * @param root 根节点
	 * @param children  这个根节点的所有子节点信息，key：subnet+"_"+subnetGrp+"_"+inetMask
	 *                   value:subnet+"_"+subnetGrp+"_"+inetMask对应的网段的所有子节点组成的ArrayList
	 * @param userState 用户状态 0:省局用户，1：地市用户
	 * @return 包括root节点的json数组
	 */
	private String getSubnetStr(Subnet root,HashMap<String,ArrayList<Subnet>> children,int userState)
	{
		boolean leaf = root.getChildCount()>0?false:true;
		StringBuffer sb= new StringBuffer(1000);
		sb.append("{");
		//设置name属性
		sb.append("'name':'"+root.getSubnet()+"/"+root.getInetMask()+"',");
		//设置id属性
		sb.append("'id':'"+root.getSubnet()+"_"+root.getSubnetGrp()+"_"+root.getInetMask()+"',");
		//设置leaf属性
		sb.append("'leaf':"+leaf+",");
		if(!leaf)
		{
			Subnet childSubnet = null;
			String subnet= root.getSubnet();
			String subnetGrp = root.getSubnetGrp();
			int inetMak = root.getInetMask();
			ArrayList<Subnet> childList = children.get(subnet+"_"+subnetGrp+"_"+inetMak);
			if(null==childList||0==childList.size())
			{
				LOG.debug("child number isn't right!subnet:"+subnet+"   subnetGrp:"+subnetGrp+"     inetMak:"+inetMak);	
			}
			sb.append("'child':[");
			//构造子节点
			for(int i=0;i<childList.size();i++)
			{
				childSubnet =childList.get(i);
				if(i==0)
				{
					sb.append(getSubnetStr(childSubnet,children,userState));
				}
				else
				{
					sb.append(",");
					sb.append(getSubnetStr(childSubnet,children,userState));
				}				
			}
			sb.append("],");			
		}
		/**
		 * 设置特殊属性
		 */
		sb.append("'attr':'");
		sb.append(userState+"/");
		sb.append(root.getSubnet()+"/");
		sb.append(root.getSubnetGrp()+"/");
		sb.append(root.getInetMask()+"/");
		sb.append(root.getAssign()+"/");
		sb.append(leaf); 
		sb.append("'");//特殊属性设置结束		
		sb.append("}");//网段节点设置结束
		
		return sb.toString();
		
		
	}

	public void setSubnetOperationDao(SubnetOperationDAO subnetOperationDao)
	{
		this.subnetOperationDao = subnetOperationDao;
	}

}

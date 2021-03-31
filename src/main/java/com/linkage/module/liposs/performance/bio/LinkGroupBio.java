package com.linkage.module.liposs.performance.bio;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.FluxManagerInterface;
import com.linkage.module.liposs.performance.dao.LinkGroupDao;
import com.linkage.system.utils.DateTimeUtil;
import com.linkage.system.utils.ExcelUtil;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-7-24
 * @category com.linkage.liposs.bio.performance
 * 版权：南京联创科技 网管科技部
 *
 */
public class LinkGroupBio
{
	private static Logger log = LoggerFactory.getLogger(LinkGroupBio.class);

	private LinkGroupDao linkGroupDao;//链路组查询类

	public void setLinkGroupDao(LinkGroupDao linkGroupDao)
	{
		this.linkGroupDao = linkGroupDao;
	}



	/**
	 * 根据excel
	 * @param file  用户上传的excel
	 *              （格式：链路组组别名称、链路组名称、设备IP、端口描述、端口IP，其中链路组名称、设备IP必填，端口描述、端口IP中必须一个必填）
	 * @param area_id   用户域ID
	 * @param city_id   用户属地ID
	 * @return  操作结果map。
	 *           key:resultcode  value:0(成功)，1(有部分失败)
	 *           key:paramerror  value：excel中必填字段不正确
	 *           key:limitdeviceport  value:设备用户没有权限或设备端口在系统中没有找到
	 *           key:notcheck    value:excel中链路组对应的链路组别、用户域与现有系统中不一致
	 */
	@SuppressWarnings("unchecked")
	public HashMap loadBathLinkResultMap(File file,long area_id,String city_id)
	{
		HashMap resultMap = new HashMap();
		List data = ExcelUtil.importToExcel_col(file);
		List<Map> paramErrorList = new ArrayList<Map>();
		List limitList= new ArrayList();
		List notCheckList= new ArrayList();
		List dbFailList = new ArrayList();
		Map<String,Object> recordMap =null;
		Map<String,Map> linkSortByDeviceIP=new HashMap<String,Map>();
		Map deviceMap=null;

		//端口信息
		String deviceip="";
		String portip="";
		String portdesc="";
		String linkGroupName="";
		String groupName="";
		String port_info="";
		String getway="";
		String device_id="";
		String key ="";
		List  deviceList=new ArrayList();

		log.debug(new DateTimeUtil().getLongDate()+"    文件记录数："+data.size());
		//检查用户填写链路内容是不是必填项都填写了，填写不合法给用户提示
		for(int i=0;i<data.size();i++)
		{
			recordMap=(Map<String,Object>)data.get(i);
			groupName=(String)recordMap.get("链路组别名称");
			linkGroupName=(String)recordMap.get("链路组名称");
			deviceip=(String)recordMap.get("设备IP");
			portdesc=(String)recordMap.get("端口描述");
			portip=(String)recordMap.get("端口IP");

			//查看必填项是不是都填写了
			if(isNull(linkGroupName)||isNull(deviceip)||(isNull(portdesc)&&isNull(portip)))
			{
				paramErrorList.add(recordMap);
				data.remove(i);
				i--;
				continue;
			}

			//端口port_info优先使用端口IP、在端口IP为空的情况下，才使用端口描述
			recordMap.put("port_info",portip);
			recordMap.put("getway","5");
			if(isNull(portip)) //端口IP为空
			{
				recordMap.put("port_info",portdesc);
				recordMap.put("getway","2");
			}


			/**
			 * 按照设备IP分类整理端口数据，key:设备IP、value：导入文件中这个设备IP对应的端口数据(key:deviceip+"||"+getway+"||"+port_info)
			 */

			key=deviceip+"||"+(String)recordMap.get("getway")+"||"+(String)recordMap.get("port_info");
			//这个设备IP对应的端口map已存在
			if(linkSortByDeviceIP.containsKey(deviceip))
			{
				deviceMap=(Map)linkSortByDeviceIP.get(deviceip);
				deviceMap.put(key,recordMap);
			}
			else
			{
				deviceMap =new HashMap();
				deviceMap.put(key,recordMap);
				linkSortByDeviceIP.put(deviceip,deviceMap);
			}

//			//查询用户权限范围内是否能看到这个端口或这个端口不能在系统中找到唯一标识得记录
//			deviceList=linkGroupDao.getDeviceInfo(deviceip,(String)recordMap.get("getway"),(String)recordMap.get("port_info"),area_id);
//			if(null==deviceList||0==deviceList.size()||deviceList.size()!=1)
//			{
//				limitList.add(recordMap);
//				data.remove(i);
//				i--;
//				continue;
//			}
//			else
//			{
//				device_id=(String)((Map)deviceList.get(0)).get("device_id");
//				recordMap.put("device_id",device_id);
//			}
		}
		log.debug(new DateTimeUtil().getLongDate()+"    参数校验后的文件记录数："+data.size());
		//clear
		data=null;

		data = new ArrayList<Map>();
		Iterator it =linkSortByDeviceIP.keySet().iterator();
		Map devicePortMap =null;
		Iterator deviceMapIt =null;
		List deviceIDList=null;
		while(it.hasNext())
		{
			deviceip=(String)it.next();
			deviceMap=(Map)linkSortByDeviceIP.get(deviceip);

			//装载这个设备IP对应的用户权限范围内的各设备端口信息
			devicePortMap=linkGroupDao.getDevicePortList(deviceip,area_id);

			//遍历Excel中的端口信息
			deviceMapIt=deviceMap.keySet().iterator();
			while(deviceMapIt.hasNext())
			{
				key=(String)deviceMapIt.next();
				//这个设备在权限范围内并这个端口只对应一个设备,则把这个端口加入data中，供下面操作
				if(devicePortMap.containsKey(key)&&((List)devicePortMap.get(key)).size()==1)
				{
					deviceIDList=(List)devicePortMap.get(key);
					device_id=(String)deviceIDList.get(0);
					((Map)deviceMap.get(key)).put("device_id",device_id);
					data.add((Map)deviceMap.get(key));
				}
				else
				{
					limitList.add((Map)deviceMap.get(key));
				}
			}
		}

		//clear
		it=null;
		linkSortByDeviceIP=null;
		log.debug(new DateTimeUtil().getLongDate()+"    权限校验后的文件记录数："+data.size());

		/**
		 * 下面流程来处理合法端口得情况，可能这些端口已存在链路组中，也有可能不在
		 */
		Map group =null;  //链路组别map
		Map linkGroup =null;  //链路组map
		List linkPortSqlList=new ArrayList();
		long groupid;
		long lg_id;
		long drt_mid;
		long db_acc_oid;
		String db_city_id;
		boolean isGroupExist=false;
		boolean isLinkGroupExist=false;
		long realGroupID;

		//有合法端口的情况下
		if(data.size()>0)
		{
			group=linkGroupDao.getGroupMap();
			linkGroup=linkGroupDao.getLinkGroupMap();
			for(int i=0;i<data.size();i++)
			{
				log.debug(new DateTimeUtil().getLongDate()+"  begin 第"+i+"个端口操作");
				isGroupExist=false;
				isLinkGroupExist=false;
				linkPortSqlList.clear();


				recordMap=(Map)data.get(i);
				groupName=(String)recordMap.get("链路组别名称");
				linkGroupName=(String)recordMap.get("链路组名称");
				deviceip=(String)recordMap.get("设备IP");
				port_info=(String)recordMap.get("port_info");
				getway=(String)recordMap.get("getway");
				device_id=(String)recordMap.get("device_id");

				//链路组别名称不为空，并且在系统中不存在，则创建这个链路组别
				if(!isNull(groupName)&&!group.containsKey(groupName))
				{
					groupid=linkGroupDao.getMaxId("groupid","tm_lg_group_info");
					linkPortSqlList.add(addGroupSQL(groupid,groupName));
					//group.put(groupName,groupid);
				}
				//链路组别名称不为空，系统中链路组别存在
				else if(!isNull(groupName)&&group.containsKey(groupName))
				{
					isGroupExist=true;
					groupid=((BigDecimal)group.get(groupName)).longValue();
				}
				else
				{
					isGroupExist=true;
					groupid=-1;
				}

				//链路组名称不存在
				if(!linkGroup.containsKey(linkGroupName))
				{
					lg_id=linkGroupDao.getMaxId("lg_id","tm_linkgroup");
					drt_mid=linkGroupDao.getMaxId("drt_mid","tm_linkgroup_mportdirection");
					linkPortSqlList.addAll(addLinkGroupSQLList(lg_id,linkGroupName,city_id,area_id,groupid,drt_mid));
					linkPortSqlList.addAll(addDevicePortLinkSQL(lg_id,drt_mid,device_id,getway,port_info));
				}
				else
				{
					isLinkGroupExist=true;
					Map temp=(Map)linkGroup.get(linkGroupName);
					lg_id=((BigDecimal)temp.get("lg_id")).longValue();
					drt_mid=((BigDecimal)temp.get("drt_mid")).longValue();
					db_acc_oid=((BigDecimal)temp.get("acc_oid")).longValue();
					db_city_id=(String)temp.get("acc_city");
					if(temp.get("groupid")==null)
					{
						realGroupID=-1;
					}
					else
					{
						realGroupID=((BigDecimal)temp.get("groupid")).longValue();
					}

					//在链路组别名称、用户域一致的情况下,判断端口是否已配置入链路中，否则给用户提示
					if(groupid!=realGroupID||db_acc_oid!=area_id)
					{
						notCheckList.add(recordMap);
						continue;
					}
					//这个端口已经配置在这个链路组中了
					else if(linkGroupDao.isConfigByDevicePort(lg_id,drt_mid,device_id,getway,port_info))
					{
						continue;
					}
					//端口没有配置在链路组中，则向链路组从表中插入两个流向的数据
					else
					{
						linkPortSqlList.addAll(addDevicePortLinkSQL(lg_id,drt_mid,device_id,getway,port_info));

						//准备好新增端口链路组，通知FluxMangager原始数据入库
						if(!deviceList.contains(device_id))
						{
							deviceList.add(device_id);
						}
					}
				}

				//数据库执行失败，不做下面的更新缓存操作
				if(!linkGroupDao.addLinkGroup(linkPortSqlList))
				{
					dbFailList.add(recordMap);
					continue;
				}



				//原来系统中不存在此链路组别的情况下，把链路组别加到缓存中
				if(!isGroupExist)
				{
					group.put(groupName,BigDecimal.valueOf(groupid));
				}

				//原来系统中不存在此链路组的情况下，把链路组加到缓存中
				if(!isLinkGroupExist)
				{
					Map temp =new HashMap();
					temp.put("lg_id",BigDecimal.valueOf(lg_id));
					temp.put("drt_mid",BigDecimal.valueOf(drt_mid));
					temp.put("acc_oid",BigDecimal.valueOf(area_id));
					temp.put("acc_city",city_id);
					temp.put("groupid",BigDecimal.valueOf(groupid));
					linkGroup.put(linkGroupName,temp);
				}
				log.debug(new DateTimeUtil().getLongDate()+"  end 第"+i+"个端口操作");
			}
		}

		if(deviceList.size()>0)
		{
			String[] device_ids = new String[deviceList.size()];
			deviceList.toArray(device_ids);
			FluxManagerInterface.GetInstance().readDevices(device_ids);
		}

		if(0==paramErrorList.size()&&0==limitList.size()&&0==notCheckList.size()&&dbFailList.size()==0)
		{
			resultMap.put("resultcode","0");
		}
		else
		{
			resultMap.put("resultcode","1");
		}
		log.debug(new DateTimeUtil().getLongDate()+"   paramErrorList_size:"+paramErrorList.size()
				+"    limitList_size:"+limitList.size()+"     notCheckList_size:"+notCheckList.size()
				+"   dbFailList_size:"+dbFailList.size());
		resultMap.put("paramerror",paramErrorList);
		resultMap.put("limitdeviceport",limitList);
		resultMap.put("notcheck",notCheckList);
		resultMap.put("dbfail",dbFailList);

		return resultMap;
	}



	/**
	 * 拼装链路组端口从表数据
	 * @param lg_id     链路组ID
	 * @param drt_mid   链路组主表ID
	 * @param device_id  设备ID
	 * @param getway    采集方式
	 * @param port_info  端口唯一标识
	 * @return  链路组端口从表sql
	 */
	private List addDevicePortLinkSQL(long lg_id,long drt_mid,String device_id,String getway,String port_info)
	{
		List sqlList = new ArrayList();
		String sql="";

		long drt_cid=linkGroupDao.getMaxId("drt_cid","tm_linkgroup_cportdirection");
		sql="insert into tm_linkgroup_cportdirection(drt_cid,lg_id,drt_mid,device_id,ifindex_info,getway,direction)  values(";
		sql+=drt_cid+",";
		sql+=lg_id+",";
		sql+=drt_mid+",";
		sql+="'"+device_id+"','";
		sql+=port_info+"',";
		sql+=getway+",";
		sql+="1)";
		PrepareSQL psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());

		drt_cid++;
		drt_mid++;
		sql="insert into tm_linkgroup_cportdirection(drt_cid,lg_id,drt_mid,device_id,ifindex_info,getway,direction)  values(";
		sql+=drt_cid+",";
		sql+=lg_id+",";
		sql+=drt_mid+",";
		sql+="'"+device_id+"','";
		sql+=port_info+"',";
		sql+=getway+",";
		sql+="2)";
		psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());

		sql="update flux_deviceportconfig set intodb=1 where device_id='";
		sql+=device_id+"' and getway="+getway+" and port_info='"+port_info+"'";
		psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());

		return sqlList;
	}


	/**
	 * 增加链路组别sql
	 * @param groupid
	 * @param groupName
	 */
	private String addGroupSQL(long groupid,String groupName)
	{
		StringBuffer sb = new StringBuffer(512);
		sb.append("insert into tm_lg_group_info(groupid,groupname) values(");
		sb.append(groupid);
		sb.append(",'"+groupName+"')");
		PrepareSQL psql = new PrepareSQL(sb.toString());
		return psql.getSQL();
	}


	/**
	 * 增加新链路组的sql
	 * @param lg_id  链路组ID
	 * @param lg_name 链路组名称
	 * @param city_id 属地ID
	 * @param area_id 用户域id
	 * @param groupid 链路组别ID
	 * @return   返回新增链路组对应的sql队列
	 */
	private List addLinkGroupSQLList(long lg_id,String lg_name,String city_id,long area_id,long groupid,long drt_mid)
	{
		List sqlList = new ArrayList();
		String sql="";

		//准备链路组insert
		sql="insert into tm_linkgroup(lg_id,lg_name,acc_oid,groupid,acc_city) values("
			+lg_id+",'"+lg_name+"',"+area_id+","+groupid+",'"+city_id+"')";
		PrepareSQL psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());


		//准备链路组主表insert
		sql="insert into tm_linkgroup_mportdirection(lg_id,drt_mid,drt_name,drt_desc,drt_direction) values("
			+lg_id+","+drt_mid+",'"+lg_name+"流入','"+lg_name+"流入',1)";
		psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());
		drt_mid+=1;
		sql="insert into tm_linkgroup_mportdirection(lg_id,drt_mid,drt_name,drt_desc,drt_direction) values("
			+lg_id+","+drt_mid+",'"+lg_name+"流出','"+lg_name+"流出',2)";
		psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());

		return sqlList;
	}


	/**
	 * 判断参数是否为空
	 * @param param   参数
	 * @return  true:参数为空  false:参数不为空
	 */
	private boolean isNull(String param)
	{
		boolean result =false;
		if(null==param||"".equals(param))
		{
			result=true;
		}
		return result;
	}
}

package com.linkage.litms.system;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;

public class PingCheck  
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PingCheck.class);
	/**
	 * 查询所有的ACS信息
	 */
	private static String  ACS_SELECT_SQL="select gather_id,tomcat_ip,tomcat_port from tab_tomcat_config ";
	
	/**
	 * 告警入库表
	 */
	private final String ALARM_SQL = "insert into tab_alarm(id,type,mlevel,device_id,status,possiblereason,location,occurtime,cleartime,clearsuggestion,description) values(?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 单态实例
	 */
	private static PingCheck instance = null;
	
	
	/**
	 * 存放最新的ACSPing状态信息(不通的话会存放开始不通的时间)
	 */
	private static HashMap latestPingMap = new HashMap();
	
	
	/**
	 * 存放要入库的ACSPing状态信息
	 */
	private static ArrayList saveFailPingLst = new ArrayList();
	
	
	/**
	 * 存放acs信息
	 */
	private static HashMap ACSInfo = new HashMap();
	
	/**
	 * 单态实例锁
	 */
	private static Object lock = new Object();
	
	/**
	 * ping 检测线程
	 */
	private PingThread thread ;
	
	
	/**
	 * ping检测的单态实例方法
	 * @return
	 */
	public static PingCheck getInstance()
	{
		synchronized(lock)
		{
			if(null==instance)
			{
				instance = new PingCheck();
			}
		}
		
		return instance;
	}
	
	/**
	 * 启ping检测任务
	 *
	 */
	private PingCheck()
	{
		//装载系统内的所有acs信息
		ACSInfo=getACSInfo();
		
		thread = new PingThread();
		thread.start();
	}
	
	
	
	/**
	 * 检测任务
	 * @author wangp
	 *
	 */
	class PingThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				logger.debug("begin ping check!");
				
				//HashMap ACSInfo = getACSInfo();
				String gather_id="";
				String acs_str ="";
				String acs_ip="";
				String acs_port ="";
				boolean flag = false;
				Iterator it = ACSInfo.keySet().iterator();
				ACSPingInfo acs = null;
				//遍历查询ACS状态
				while(it.hasNext())
				{
					gather_id =(String)it.next();
					acs_str = (String)ACSInfo.get(gather_id);
					acs_ip = acs_str.split("\\|")[0];
					acs_port = acs_str.split("\\|")[1];
					flag = isCommunication(acs_ip,acs_port);
					
					/**
					 * 存放最新的ACS状态信息
					 */
					synchronized(latestPingMap)
					{
						//不通的情况，需要判断是否第一次不通，如不是需要保留队列中的不通时间
						if(!flag)
						{
							//表中有这个acs的状态信息，即不是首次检测状态,只有在上次通或这次通的情况需要更新
							if(null!=latestPingMap.get(gather_id))
							{
								acs = (ACSPingInfo)latestPingMap.get(gather_id);
								//上个状态就是不通的,只要更新acsIP
								if(!acs.getACSStatus())
								{
									acs.setACSIp(acs_ip);
								}
								//上个状态是通的，则更新为最新的ACSPing信息
								else
								{
									acs = new ACSPingInfo(gather_id,acs_ip,flag);
								}
							}
							else
							{
								acs = new ACSPingInfo(gather_id,acs_ip,flag);
							}
							
							latestPingMap.put(gather_id,acs);
							
							//保存进入库队列
							acs = new ACSPingInfo(gather_id,acs_ip,flag);					
							synchronized(saveFailPingLst)
							{
								saveFailPingLst.add(acs);
							}	
						}
						//ping 通的情况
						else
						{
							//上次不通的情况,要发出恢复告警
							if(null!=latestPingMap.get(gather_id))
							{
								acs = (ACSPingInfo)latestPingMap.get(gather_id);
								if(!acs.getACSStatus())
								{
									//保存进入库队列
									acs = new ACSPingInfo(gather_id,acs_ip,flag);					
									synchronized(saveFailPingLst)
									{
										saveFailPingLst.add(acs);
									}	
								}
							}
							acs = new ACSPingInfo(gather_id,acs_ip,flag);
							latestPingMap.put(gather_id,acs);
						}		
					}								
				}
				
				//遍历入库队列，准备入库				
				synchronized(saveFailPingLst)
				{
					int[] result =saveToDataBase(saveFailPingLst);
					if(null!=result&&result.length>0)
					{
						saveFailPingLst.clear();
					}
				}
				
				
				//休眠10分钟
				try
				{
					sleep(600000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		}
	}
	
	/**
	 * 消亡这个ping线程
	 *
	 */
	public void stopThread()
	{
		thread.destroy();
	}
	
	
	/**
	 * 获取最新的ping信息
	 * @return
	 */
	public String getLatestPingInfo()
	{
		String str ="";
		ACSPingInfo pingInfo =null;
		synchronized(latestPingMap)
		{
			Iterator it = latestPingMap.values().iterator();
			if(it.hasNext())
			{
				while(it.hasNext())
				{
					str+="<tr bgcolor='#FFFFFF'>";
					pingInfo = (ACSPingInfo)it.next();
					str+="<td align=center>"+pingInfo.getACSName()+"</TD>";
					//状态不通的情况下显示不通时间
					if(!pingInfo.getACSStatus())
					{
						str +="<TD align=center>"+new DateTimeUtil(pingInfo.getACSTime()*1000).getLongDate()+"</TD>";
						str+="<TD align=center><img src='../images/gaojin_red.gif'></TD>";
					}
					else
					{
						str +="<TD align=center> </TD>";
						str+="<TD align=center><img src='../images/gaojin_green.gif'></TD>";
					}
					str+="</TR>";
				}
			}
			else
			{
				str = "<tr bgcolor='#FFFFFF'><td colspan=3 align=center>没有记录</td></TR>";
			}			
		}
		
		return str;
	}
	
	/**
	 * ping信息入库
	 * @param list
	 * @return
	 */
	private int[] saveToDataBase(ArrayList list)
	{
		int[] result = null;
		int alarmID =401001;
		int alarmType=3;
		int alarmLevel =1;
		int status =1;
		String reason ="网络不通";
		String bak="ping 通";
		ArrayList sqlList = new ArrayList();
		ACSPingInfo pingInfo = null;
		PrepareSQL pSQL = new PrepareSQL();
		for(int i=0;i<list.size();i++)
		{
			pingInfo = (ACSPingInfo)list.get(i);
			if(pingInfo.getACSStatus())
			{
				//alarmLevel = 6;
				reason ="";
				bak="与"+pingInfo.getACSName()+"  Ping通";
			}
			else
			{
				//alarmLevel =1;
				reason ="网络不通";
				bak="与"+pingInfo.getACSName()+" Ping不通";
			}
			pSQL.setSQL(ALARM_SQL);
			pSQL.setInt(1,alarmID);
			pSQL.setInt(2,alarmType);				
			pSQL.setInt(3,alarmLevel);
			pSQL.setString(4,pingInfo.getACSIp());
			pSQL.setInt(5,status);
			pSQL.setString(6,reason);
			pSQL.setStringExt(7,null,false);
			pSQL.setLong(8,pingInfo.getACSTime());
			pSQL.setStringExt(9,null,false);
			pSQL.setStringExt(10,null,false);
			pSQL.setString(11,bak);
			logger.debug("pingSQL"+i+":"+pSQL.getSQL());
			sqlList.add(pSQL.getSQL());				
		}		
		result = DataSetBean.doBatch(sqlList);		
		return result;
	}
	
	/**
	 * 获取系统中的所有ACS信息（key：采集点，value：ip+"|"+port）
	 * @return
	 */
	private static HashMap getACSInfo()
	{
		HashMap ACSInfo = new HashMap();
		PrepareSQL psql = new PrepareSQL(ACS_SELECT_SQL);
        psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(ACS_SELECT_SQL);
		Map fields = cursor.getNext();
		String gather_id ="";
		String acs_ip="";
		String acs_port ="";
		while(null!=fields)
		{
			gather_id = (String)fields.get("gather_id");
			acs_ip = (String)fields.get("tomcat_ip");
			acs_port = (String)fields.get("tomcat_port");
			if(!"".equals(gather_id)&&!"".equals(acs_ip)&&!"".equals(acs_port))
			{
				ACSInfo.put(gather_id,acs_ip+"|"+acs_port);
			}
			
			fields = cursor.getNext();
		}
		
		return ACSInfo;
	}
	/**
	 * 检测web和某个IP地址是否想通的方法
	 * @param ip
	 * @return
	 */
	private static boolean isCommunication(String ip,String port)
	{
		boolean result = false;
		InetAddress address = null;
		Socket socket = null;		
		try
		{
			address = InetAddress.getByName(ip);			
			socket = new Socket(address, Integer.parseInt(port));
			if(socket.isConnected())
			{
				result = true;
				socket.close();
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();	
			logger.debug(new DateTimeUtil().getLongDate()+" ping acsIP("+ip+") port("+port+")fail!" );
			
		}
		finally
		{
			if(null!=socket)
			{
				try
				{
					socket.close();
				}
				catch(Exception e1)
				{
					//e1.printStackTrace();
				}				
			}
			
			address = null;
		}
		
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}

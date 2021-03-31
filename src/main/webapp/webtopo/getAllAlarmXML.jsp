<%@ page contentType="text/xml;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.Scheduler,
				com.linkage.litms.webtopo.warn.DeviceWarnInfo,
						   com.linkage.litms.common.util.Encoder,
						   com.linkage.litms.common.util.StringUtils,
						   com.linkage.litms.system.UserRes,
						   com.linkage.litms.common.util.CommonMap,
						   RemoteDB.AlarmEvent,
						   RemoteDB.GatherIDEvent,
						   java.util.*"%>
<%
request.setCharacterEncoding("GBK");
//request.setCharacterEncoding("ISO-8859-1");
//type=device:代表查看的是设备的告警;type=all:代表查看的是所有的告警
//type=config:代表查看的是配置的告警
String type=request.getParameter("type");
String updateTime=request.getParameter("updateTime");
String device_id=null;
String level=null;
if(type!=null && type.equals("device"))
{
	device_id=request.getParameter("device_id");
	level=request.getParameter("level");
}
//获取用户对象信息
UserRes userRes = (UserRes)session.getAttribute("curUser");
//获取用户登录域ID
String areaid = null;
if(userRes.getUser().getAccount().equals("admin"))
	areaid = "0";
else
	areaid = Long.toString(userRes.getAreaId());

//获取用户所管理的采集机列表
List gatherList = userRes.getUserProcesses();

//构造采集点编号和告警最大id结构
/*
GatherIDEvent[] gatherStructArr = null;
if(gatherList.size()>0){
	gatherStructArr = new GatherIDEvent[gatherList.size()];
	for(int i=0;i<gatherList.size();i++){
		gatherStructArr[i] = new GatherIDEvent();
		gatherStructArr[i].gather_id=(String)gatherList.get(i);
		gatherStructArr[i].max_event_id = "0";
	}
}
*/
//Scheduler scheduler = new Scheduler();
//获取所有告警
//AlarmEvent[] list = scheduler.getAllAlarm(areaid,gatherStructArr);

//获取所有告警
DeviceWarnInfo dwi=new DeviceWarnInfo();
ArrayList list=null;
if(type!=null && type.compareTo("device")==0)
{
    dwi.setOnlyWarnInfo(session);
	list=dwi.getAlarmByDeviceID(device_id,level);
	
}
else if(type!=null && type.compareTo("all")==0)
{
    dwi.setOnlyWarnInfo(session);
	list=dwi.getALLAlarmList();
}
else
{
	dwi.setWarnPara(areaid,gatherList);
	dwi.setDeviceWarnInfo(session);
	list=dwi.getConfigAlarmList("0");
	if(list!=null)
	{
	}
}

out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?><Output><Rows>");
AlarmEvent e=null;
if(list!=null)
{
	String device_type=null;
	String status=null;
	String m_DisplayString="";		
	
	for(int i=0;i<list.size();i++){
		e=(AlarmEvent)list.get(i);
		if(e!=null)
		{

			if(DeviceWarnInfo.type_name_map!=null)
			{
				device_type=(String)DeviceWarnInfo.type_name_map.get(String.valueOf(e.m_DeviceType));
				if(device_type==null)
				{
					device_type="未知";
				}
			}
			else
			{
				device_type=String.valueOf(e.m_DeviceType);
			}

			if(DeviceWarnInfo.status_name_map!=null)
			{
				status=(String)DeviceWarnInfo.status_name_map.get(String.valueOf(e.m_ActiveStatus));
				if(status==null)
				{
					status="未知";
				}			
			}			
		
			m_DisplayString=Encoder.AsciiToChineseString(e.m_DisplayString);
			m_DisplayString=StringUtils.ParseXMLCode(m_DisplayString);
			out.println("<Row gatherid='"+ e.m_GatherID+"'>");
			out.println("<ID>"+ e.m_AlarmId +"</ID>");
			out.println("<StrCity>"+ Encoder.AsciiToChineseString(e.m_strCity) +"</StrCity>");
			out.println("<CreatorName>"+ Encoder.AsciiToChineseString(e.m_CreatorName) +"</CreatorName>");
			out.println("<SourceIP>"+e.m_SourceIP +"</SourceIP>");
			out.println("<activestatus>"+ status+"</activestatus>");
			out.println("<CreationTime>"+ StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", e.m_CreateTime)+"</CreationTime>");
			out.println("<Severity>"+ e.m_Severity+"</Severity>");
			out.println("<EventNo>"+ Encoder.AsciiToChineseString(e.m_EventNo)+"</EventNo>");
			out.println("<DeviceType>"+device_type+"</DeviceType>");
			out.println("<SourceName>"+ Encoder.AsciiToChineseString(e.m_SourceName)+"</SourceName>");
			out.println("<DisplayTitle>"+ Encoder.AsciiToChineseString(e.m_DisplayTitle) +"</DisplayTitle>");
			out.println("<DisplayString>"+ m_DisplayString +"</DisplayString>");

			out.println("<Device_id>"+ e.m_DeviceCoding+"</Device_id>");
			out.println("<Gather_id>"+  e.m_GatherID +"</Gather_id>");
			out.println("</Row>");	
		}
	}
	
}
out.println("</Rows></Output>");
%>

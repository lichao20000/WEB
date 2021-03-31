<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "java.util.*,com.linkage.litms.webtopo.Scheduler,RemoteDB.AlarmEvent,com.linkage.litms.webtopo.warn.DeviceWarnInfo,com.linkage.litms.common.util.Encoder,com.linkage.litms.common.util.StringUtils"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
//获取所有告警
List alarmList=null;
Scheduler scheduler = new Scheduler();
alarmList = scheduler.getDeviceWarnFromMC(device_id);
int alarmNum = 0;
if(alarmList != null)
    alarmNum = alarmList.size();
%>
<HTML>
<HEAD>
<TITLE>Alarm XML</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<SCRIPT LANGUAGE="JavaScript">
<!--
//屏蔽页面请稍后的提示框
//parent.showMsgDlg();
function removeAllWarn(){
	for(var i=0;i<parent.arrDev.length;i++){
		parent.arrDev[i].removeWarn();
	}
}
function refreshAlarmList(){
	var alarmNum="<%=alarmNum%>";	
	if(parseInt(alarmNum,10)>0){
		var s = idRefreshAlarm.innerHTML;	
		if(s=="") return;	
		parent.oList.reload(s,"overlay");
	}
}
function start(){		
	refreshAlarmList();
	//屏蔽页面请稍后的提示框
	//parent.closeMsgDlg();
}
//-->
</SCRIPT>
</HEAD>
<BODY onload="javascript:start();">
<xml id="idRefreshAlarm">
<%
if(alarmList!=null)
{
	out.println("<Output><Rows>");
	AlarmEvent e=null;
	String device_type=null;
	String status=null;
	String m_DisplayString="";
	int _index = alarmList.size() - 1;
	for(;_index >= 0;_index --){
		e=(AlarmEvent)alarmList.get(_index);
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
		out.println("<SourceIP>"+ e.m_SourceIP +"</SourceIP>");
		out.println("<activestatus>"+ status+"</activestatus>");
		out.println("<CreationTime>"+ StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", e.m_CreateTime)+"</CreationTime>");
		out.println("<Severity>"+ e.m_Severity+"</Severity>");
		out.println("<EventNo>"+ Encoder.AsciiToChineseString(e.m_EventNo)+"</EventNo>");
		out.println("<DeviceType>"+ device_type+"</DeviceType>");
		out.println("<SourceName>"+ Encoder.AsciiToChineseString(e.m_SourceName)+"</SourceName>");
		out.println("<DisplayTitle>"+ Encoder.AsciiToChineseString(e.m_DisplayTitle) +"</DisplayTitle>");
		out.println("<DisplayString>"+m_DisplayString +"</DisplayString>");
		out.println("<Device_id>"+ e.m_DeviceCoding+"</Device_id>");
		out.println("<Gather_id>"+ e.m_GatherID +"</Gather_id>");
		out.println("</Row>");		
	}
	out.println("</Rows></Output>");
}
%>
</xml>
</BODY>
</HTML>

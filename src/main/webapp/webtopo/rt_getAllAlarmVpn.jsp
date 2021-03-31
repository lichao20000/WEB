<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.VpnScheduler,
				com.linkage.litms.webtopo.warn.WarnVoiceManager,
							java.util.*,
							com.linkage.litms.webtopo.warn.VpnDeviceWarnInfo,
						   com.linkage.litms.common.util.Encoder,
						   com.linkage.litms.common.util.StringUtils,
						   com.linkage.litms.system.UserRes,
						   com.linkage.litms.common.util.CommonMap,
						   RemoteDB.AlarmEvent,
						   RemoteDB.AlarmNum,
						   RemoteDB.GatherIDEvent"%>
<%
request.setCharacterEncoding("GBK");
//type=device:����鿴�����豸�ĸ澯;type=all:����鿴�������еĸ澯
//type=config:����鿴�������õĸ澯
String type=request.getParameter("type");
String device_id=null;
String level=null;
String updateTime=request.getParameter("updateTime");

if(type!=null && type.compareTo("device")==0)
{
	device_id=request.getParameter("device_id");
	level=request.getParameter("level");
}

//��ȡ���и澯
VpnDeviceWarnInfo dwi=new VpnDeviceWarnInfo();
ArrayList alarmList=null;

if(type!=null && type.compareTo("device")==0)
{
	dwi.setOnlyWarnInfo(session);
	alarmList=dwi.getAlarmListByDeviceID(device_id,level);	
}
else if(type!=null && type.compareTo("all")==0)
{
	
	UserRes userRes = (UserRes)session.getAttribute("curUser");
	String areaid = null;
	if(userRes.getUser().getAccount().equals("admin"))
		areaid = "0";
	else
		areaid = Long.toString(userRes.getAreaId());


	//��ȡ�û�������Ĳɼ����б�
	List gatherList = userRes.getUserProcesses();
	dwi.setWarnPara(areaid,gatherList);
	dwi.setDeviceWarnInfo(session);
	//dwi.setOnlyWarnInfo(session);
	alarmList=dwi.getConfigAlarmList(updateTime);
	//alarmList=dwi.getALLAlarmList();
}
//��ȡ�澯�����¸���ʱ��
long newTime=dwi.getUpdateTime();
//��ȡ���ĸ澯������
int alarmNum=alarmList.size();

String warnvoice=null;
String voicetype= "0";
//if(alarmNum>0)
if(false)
{
	String maxid=dwi.getMaxWarnLevel();
	WarnVoiceManager wvmanager=new WarnVoiceManager(request);
	HashMap voiceMap=wvmanager.getWarnVoice();
	String voice=(String)voiceMap.get(maxid);
	if(voice!=null)
	{
		String[] voices=voice.split("\\|\\|\\|");
		warnvoice=voices[0];
	}
}
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
//����ҳ�����Ժ����ʾ��
//parent.showMsgDlg();
function removeAllWarn(){
	for(var i=0;i<parent.arrDev.length;i++){
		parent.arrDev[i].removeWarn();
	}
}
function refreshAlarmList(){
	var newTime="<%=newTime%>";
	var alarmNum="<%=alarmNum%>";	
	parent.updateTime=newTime;
	if(parseInt(alarmNum,10)>0){
		var s = idRefreshAlarm.innerHTML;	
		if(s=="") return;	
		//��smfע�͵�
		//parent.oList.setRemovePattern(5,"4,5");
		//smf	
		parent.oList.reload(s,"overlay");
		//parent.isSendVoice=true;
		//parent.VoiceName="<%=warnvoice%>";
		//parent.VoiceLoop="<%=voicetype%>";
		//parent.VoiceAuto="true";
		//parent.refreshVoice();
	}
}
function start(){		
	refreshAlarmList();
	//����ҳ�����Ժ����ʾ��
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
		if(VpnDeviceWarnInfo.type_name_map!=null)
		{
			device_type=(String)VpnDeviceWarnInfo.type_name_map.get(String.valueOf(e.m_DeviceType));
			if(device_type==null)
			{
				device_type="δ֪";
			}
		}
		else
		{
			device_type=String.valueOf(e.m_DeviceType);
		}

		if(VpnDeviceWarnInfo.status_name_map!=null)
		{
			status=(String)VpnDeviceWarnInfo.status_name_map.get(String.valueOf(e.m_ActiveStatus));
			if(status==null)
			{
				status="δ֪";
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

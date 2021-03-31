<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "java.util.*,
							com.linkage.litms.webtopo.warn.*,
						    com.linkage.litms.common.util.Encoder,
						    com.linkage.litms.common.util.StringUtils,
						    com.linkage.litms.system.UserRes,
						    RemoteDB.AlarmEvent,
						    RemoteDB.AlarmNum,
						    com.linkage.litms.LipossGlobals"%>
<%
request.setCharacterEncoding("GBK");
String isvip=request.getParameter("isvip");
String updateTime=request.getParameter("updateTime");
UserRes userRes = (UserRes)session.getAttribute("curUser");

//fix by 王志猛  这里添加代码是为了解决当session失效的情况下，如果用户在进行操作应当重新进行登陆。
String model = LipossGlobals.getLipossProperty("SetupModel");
if(model==null)
{
	model="";
}
else
{
	model="/"+model;
}
if(userRes == null)
{
  response.sendRedirect(request.getRequestURI().substring(0,request.getRequestURI().indexOf("/", 2))+model+"/login.jsp");
  return;	
}
//end fix 

String areaid = null;
String userDomain=userRes.getUser().getAccount();
if(userDomain!=null&&userDomain.equals("admin"))
	areaid = "0";
else
	areaid = Long.toString(userRes.getAreaId());


//获取用户所管理的采集机列表
List gatherList = userRes.getUserProcesses();
//AlarmEvent[] alarmList=null;
ArrayList alarmList=null;

/*
String str_maxId = request.getParameter("maxid");
//构造采集点编号和告警最大id结构
GatherIDEvent[] gatherStructArr = null;
if(str_maxId != null && str_maxId.length()>0){
	String[] arrGE = str_maxId.split(";");
	String[] arrtmp;
	gatherStructArr = new GatherIDEvent[arrGE.length];
	if(arrGE.length>0)
	{
		for(int i=0;i<arrGE.length;i++){
			gatherStructArr[i] = new GatherIDEvent();
			arrtmp = arrGE[i].split(",");
			gatherStructArr[i].gather_id = arrtmp[0];
			gatherStructArr[i].max_event_id = arrtmp[1];
		}
	}
*/

	
	//alarmList = scheduler.getNewAlarm(areaid,gatherStructArr);
	//String account = userRes.getUser().getAccount();
	//String file = request.getRealPath("webtopo/" + account + "_warnrule.xml");
	DeviceWarnInfo dwi=new DeviceWarnInfo();
	dwi.setWarnPara(areaid,gatherList);
	//dwi.setWarnRulePath(file);
	dwi.setDeviceWarnInfo(session);
	long newTime=dwi.getUpdateTime();
	int alarmNum=0;
	if(isvip.equals("false")){
		alarmList = dwi.getConfigAlarmList(updateTime);
		alarmNum = alarmList.size();
	}
 
	//告警声音模块
	String warnvoice = null;
	//告警声音发生次数 0：一次，1：一直
	String voicetype = "0";
	if(alarmNum > 0){
		String maxid = null;
		WarnVoiceManager wvmanager = new WarnVoiceManager(request);
		String[] voices = null;
		//updateTime等于0，表示第一次打开页面告警信息
		//if(updateTime.equals("0")){
		//如果不是新告警
		if(!dwi.isNewWarn()){
			maxid = dwi.getMaxWarnLevel();
		}else{//新告警的告警级别
			maxid = dwi.getNewWarnLevel();
		}
		//新告警中如果maxid为-1，则不需要查询数据库读取告警声音文件
		if(!maxid.equals("-1")){
			voices = wvmanager.getWarnVoiceNameAndType(maxid);
			if(voices != null && voices.length > 1){
				warnvoice = voices[0];
				voicetype = voices[1];
			}
		}
	}
	//Scheduler scheduler = dwi.getScheduer();	
	String[] arr = (String[])session.getAttribute("allobjs");
	String s = "";
	String link = "";
	//if(arr!=null && arr.length>0){
	//需要从session中取出allobjs并从scheduler中取出告警数量
	//加入告警过滤后，不需要从scheduler中取，过滤中已经统计号告警过滤数量
	
	if(true){
		AlarmNum[] alarmNumList = dwi.getAlarmNum(areaid,arr);
		link = dwi.getAlarmLinkMsg(areaid);
		//AlarmNum[] alarmNumList = scheduler.getAllAlarmNum(areaid,arr);
		StringBuffer sb = new StringBuffer(200);
		for(int i=0;i<alarmNumList.length;i++){
			if(i==0){
				sb.append(alarmNumList[i].id).append(",").append(alarmNumList[i].number);
				sb.append(",").append(alarmNumList[i].level);
			}
			else{
				sb.append(";").append(alarmNumList[i].id).append(",").append(alarmNumList[i].number);
				sb.append(",").append(alarmNumList[i].level);
			}
		}
		s = sb.toString();
		sb = null;
	}
	else{
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
function removeAllWarn(){
	if(parent.opener == null) return;
	for(var i=0;i<parent.opener.arrDev.length;i++){
		if(typeof(parent.opener.arrDev[i]) != "undefined"){
			parent.opener.arrDev[i].removeWarn();
		}
	}
}

function refreshAlarmList(){
	var alarmNum="<%=alarmNum%>";
	var newTime="<%=newTime%>";
	parent.updateTime = newTime;
	if(parseInt(alarmNum,10)>0){
		var str = idRefreshAlarm.innerHTML;
		if(str=="") return;
		parent.oList.reload(str,"overlay");
		//被smf注释掉
		//parent.oList.setRemovePattern(5,"4,5");
		//smf
		try{
			//设备告警处理
			removeAllWarn();
			refreshAlarmNum();
			//声音告警
			makeVoice();
		}catch(e){
			window.status = "提示：Topo图窗口已关闭(" + e.description + ")";
			//alert(e.description);
		}
	}
}
function makeVoice(){
	parent.isSendVoice=true;
	parent.VoiceName="<%=warnvoice%>";
	parent.VoiceLoop="<%=voicetype%>";
	parent.VoiceAuto="true";
	parent.refreshVoice();
}
function refreshAlarmNum(){
	if(parent.opener == null) return;
	var s = idRefreshAlarmNum.innerHTML;
	if(s=="") return;
	list = s.split(";");
	//给main_control.js中idRefreshAlarmNumMain赋值
	parent.opener.idRefreshAlarmNumMain = s;
	parent.opener.idRefreshAlarmNumLink = "<%=link%>";
	for(var i=0;i<list.length;i++){
		arr = list[i].split(",");
		ln = parent.opener.findDevObjByID(arr[0]);
		num = arr[1];
		level = arr[2];
		if(typeof(parent.opener.arrDev[ln]) != "undefined"){
			parent.opener.arrDev[ln].drawWarn(num,level);
		}
	}
	parent.opener.refreashLinkColor();
}

function start(){	
	refreshAlarmList();
	//屏蔽页面请稍后的提示框
	//parent.closeMsgDlg();
}
//-->
</SCRIPT>
</HEAD>
<BODY onload="start();">
<xml id="idRefreshAlarm">
<%
if(alarmList!=null)
{
	out.println("<Output><Rows>");
	AlarmEvent e=null;
	String device_type=null;
	String status=null;
	String m_DisplayString="";
	int _index = alarmNum - 1;//alarmList.size() - 1;
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
		out.println("<activestatus>"+ status +"</activestatus>");		
		out.println("<CreationTime>"+ StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", e.m_CreateTime)+"</CreationTime>");
		out.println("<Severity>"+ e.m_Severity+"</Severity>");
		out.println("<EventNo>"+ Encoder.AsciiToChineseString(e.m_EventNo)+"</EventNo>");
		out.println("<DeviceType>"+device_type+"</DeviceType>");
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
<span id=idRefreshAlarmNum><%=s%></span>
</BODY>
</HTML>

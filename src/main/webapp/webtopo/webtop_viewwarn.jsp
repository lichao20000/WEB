<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.*,
				com.linkage.litms.LipossGlobals"%>
<%
	request.setCharacterEncoding("GBK");
	String type=request.getParameter("type");
	String isvpn = request.getParameter("isvpn");
	String main_control = "main_control.js";
	String topo_res = "net_topo_res.js";
	if(isvpn == null){
	    isvpn = "false";
	}else{
	    main_control = "main_control_vpn.js";
	    topo_res = "vpn_topo_res.js";
	}
	String device_id="";
	String level="";
	String title="综合告警浏览";
	if(type!=null && type.compareTo("device")==0)
	{
		device_id=request.getParameter("device_id");
		level=request.getParameter("level");
		title="设备告警浏览";
	}
	int warn_num=2000;
	if(LipossGlobals.getLipossProperty("webtopo.WARN_ALL_NUM")!=null)
    {
      warn_num=Integer.parseInt(LipossGlobals.getLipossProperty("webtopo.WARN_ALL_NUM"));
    }


%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml" xmlns:mylist>
<?IMPORT NAMESPACE="mylist" IMPLEMENTATION="listview.htc"/>
<HEAD>
<SCRIPT LANGUAGE="JavaScript">
<!--

var oInterval1="";
var updateTime="0";
var warnFlag = false;
var isvpn = <%=isvpn%>;
var page = "rt_getAllAlarm.jsp?type=<%=type%>&device_id=<%=device_id%>&level=<%=level%>&updateTime="+updateTime;
if(isvpn){
	page = "rt_getAllAlarmVpn.jsp?type=<%=type%>&device_id=<%=device_id%>&level=<%=level%>&updateTime="+updateTime;
}
function InitAll()
{
	userPremession="<%=session.getAttribute("ldims")%>";
	fnStart();
	initLV("<%=warn_num%>");
	document.all("childFrm").src= page;
	
}
//告警自动刷新 Add by Hemc
function setAutoRefresh_H(obj){
	var _checked = obj.checked;
	//设置标志位
	warnFlag = _checked;
	if(warnFlag){
		//并且设置刷新频率
		fnStart();
	}else{
		fnStop();
	}
}
function fnStart(){
	if(oInterval1==""){
		oInterval1 =window.setInterval("refreshWin()",60000);
	}
	/*
	if(voInterval2=="")
	{
		voInterval2 =window.setInterval("refreshVoice()",10000);
	}
	*/
}


function refreshWin(){
	document.all("childFrm").src = page;
	//document.all("childFrm").src="rt_getAllAlarm.jsp?type=<%=type%>&device_id=<%=device_id%>&level=<%=level%>&updateTime="+updateTime;	
}

function fnStop(){
	if(oInterval1!=""){
		window.clearInterval(oInterval1);
		oInterval1="";
	}
}

function CloseVoice(){
	
	VoiceLoop="false";
	isSendVoice="true";	
	VoiceAuto="false";
	refreshVoice();

}
//-->
</SCRIPT>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<TITLE><%=title%></TITLE>
<LINK REL="stylesheet" HREF="../css/coolmenu.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/listview.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/webtopo.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/css_green.css" TYPE="text/css">
<STYLE>
    v\:* { BEHAVIOR: url(#default#VML) }
	SPAN,DIV,TD{
		FONT-FAMILY: "宋体", "Tahoma","Arial"; FONT-SIZE: 12px
	}
</STYLE>
</HEAD>

<BODY oncontextmenu="return false" onselectstart="return false" ondragstart="return false"  onload="javascript:InitAll()" leftmargin=0 topmargin=0>
<embed src="" width="0" height="0" hidden loop=0 autostart=true  name="play" volume=1></embed> 
	
<SCRIPT LANGUAGE="JavaScript" SRC="coolmenu.js"></SCRIPT>
 <SCRIPT LANGUAGE="JavaScript" SRC="<%=topo_res%>"></SCRIPT>
 <SCRIPT LANGUAGE="JavaScript" SRC="topo.js"></SCRIPT>
<TABLE width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
<TR>	
	<TD width="100%" valign="top">
	  <TABLE width="100%"  align="left" cellpadding="0" cellspacing="0" class="top_line">	
	  <tr style="display:none">
		<TD align=left class="foot">			
			&nbsp;<IMG SRC="../images/sound_voice2.gif"  WIDTH="18" HEIGHT="17"  BORDER="0" onclick="javascript:CloseVoice();" style="cursor:hand"　 ALT="关闭声音提示">
			&nbsp;&nbsp;&nbsp;<IMG SRC="../images/return.gif"  WIDTH="18" HEIGHT="17"  BORDER="0" onclick="javascript:location.href='../work/WarnSheetView.jsp';" style="cursor:hand"　 ALT="返回实时告警">
		</TD>
	  </tr>
	  <tr>
		<TD align=left class="foot">
			<input type="checkbox" title="设置告警自动刷新" onclick="javascript:setAutoRefresh_H(this);" name="radioWarn" id="autoRefresh" ><label for="autoRefresh">自动</label>
			<a title="告警手动刷新" href="javascript://" onclick="javascript:refreshWin();" name="radioWarn" id="handRefresh"><label for="handRefresh">手动</label></a> 
		</TD>
	  </tr>
	  <TR>		
	  	<TD valign="top" class="top_leftt_line">
		  <TABLE id="idBottomView" width="100%" cellpadding="0" cellspacing="0"  style="display:" ><TR><TD>
		    <mylist:listview ID="oList" dataXML="" width="100%" height="100%" onRowSelected="clickRow()" onmousedown="rightclick()"/>
		  </TD></TR></TABLE>
		</TD>
	  </TR>
	  <TR>
			<TD colspan=2><IFRAME name="childFrm" SRC="" STYLE="width:50;height:50;display:none;"></IFRAME><IFRAME name="childFrm2" SRC="" STYLE="width:50;height:50;display:none;"></IFRAME></TD>
		</TR>
	  </TABLE>
	</TD>
</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript" SRC="<%=main_control%>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="XMLHttp.js"></SCRIPT>
<div id="PendingMessage" style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
  <center>
    <table border="0" >
      <tr>
        <td valign="middle"><img src="../images/cursor_hourglas.gif" border="0" WIDTH="30" HEIGHT="30"></td>
        <td>&nbsp;&nbsp;</td>
        <td valign="middle"><span id=txtLoading style="font-size:14px;font-family: 宋体">请稍等······</span></td>
      </tr>
    </table>
  </center>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
//-->
</SCRIPT>
</BODY>
</HTML>
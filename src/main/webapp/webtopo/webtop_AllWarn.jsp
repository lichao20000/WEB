<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.*,
				com.linkage.litms.LipossGlobals"%>
<%
	request.setCharacterEncoding("GBK");
/*
	String type=request.getParameter("type");
	String device_id="";
	String level="";
	String title="综合告警浏览";
	if(type!=null && type.compareTo("device")==0)
	{
		device_id=request.getParameter("device_id");
		level=request.getParameter("level");
		title="WEBTOPO所有告警";
	}
*/
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

var updateTime = "0";
//是否刷新告警信息,默认为false,即没有告警信息. Add by Hemc 2006-10-25
var warnFlag = false;

function InitAll()
{
	userPremession="<%=session.getAttribute("ldims")%>";
	//fnStart();
	initLV("<%=warn_num%>");
	//如果已经设置了为手动刷新,则直接返回.
	//if(warnFlag == false) return ; 
	refreshAlarm_H();
	
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
		oInterval1 =window.setInterval("refreshAlarm_H()",60000);
	}
}
function refreshAlarm_H(){
	document.all("childFrm").src = "rt_refreshAll.jsp?updateTime="+updateTime+"&isvip=false&refresh="+(new Date()).getTime();
}
function RefreshByHand_H(){
	 refreshAlarm_H();
}
function fnStop(){
	if(oInterval1!=""){
		window.clearInterval(oInterval1);
		oInterval1="";
	}
}

function CloseVoice(){
	//VoiceLoop="false";
	//isSendVoice="true";	
	//VoiceAuto="false";
	//refreshVoice();
}
//-->
</SCRIPT>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<TITLE>所有设备告警</TITLE>
<LINK REL="stylesheet" HREF="./css/coolmenu.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/listview.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/webtopo.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/css_blue.css" TYPE="text/css">
<STYLE>
    v\:* { BEHAVIOR: url(#default#VML) }
	SPAN,DIV,TD{
		FONT-FAMILY: "宋体", "Tahoma","Arial"; FONT-SIZE: 12px
	}
</STYLE>

</HEAD>
<BODY oncontextmenu="return false" onselectstart="return false" ondragstart="return false"  onload="javascript:InitAll()" leftmargin=0 topmargin=0>
<SCRIPT LANGUAGE="JavaScript" SRC="coolmenu.js"></SCRIPT>
 <SCRIPT LANGUAGE="JavaScript" SRC="net_topo_res.js"></SCRIPT>
 <SCRIPT LANGUAGE="JavaScript" SRC="topo.js"></SCRIPT>
 <SCRIPT LANGUAGE="JavaScript" SRC="XMLHttp.js"></SCRIPT>
 <SCRIPT LANGUAGE="JavaScript" SRC="main_control.js"></SCRIPT>
<TABLE width="100%"  border="1" align="center" cellpadding="0" cellspacing="0">
<TR>	
	<TD width="100%" valign="top">
	  <TABLE width="100%"  align="left" cellpadding="0" cellspacing="0" class="top_line">	
	  <tr>
		<TD align=left class="foot">
			<input type="checkbox" title="设置告警自动刷新" onclick="javascript:setAutoRefresh_H(this);" name="radioWarn" id="autoRefresh" ><label for="autoRefresh">自动</label>
			<a title="告警手动刷新" href="javascript://" onclick="javascript:RefreshByHand_H();" name="radioWarn" id="handRefresh"><label for="handRefresh">手动</label></a> 
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
</BODY>
</HTML>
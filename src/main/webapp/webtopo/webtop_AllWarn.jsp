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
	String title="�ۺϸ澯���";
	if(type!=null && type.compareTo("device")==0)
	{
		device_id=request.getParameter("device_id");
		level=request.getParameter("level");
		title="WEBTOPO���и澯";
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
//�Ƿ�ˢ�¸澯��Ϣ,Ĭ��Ϊfalse,��û�и澯��Ϣ. Add by Hemc 2006-10-25
var warnFlag = false;

function InitAll()
{
	userPremession="<%=session.getAttribute("ldims")%>";
	//fnStart();
	initLV("<%=warn_num%>");
	//����Ѿ�������Ϊ�ֶ�ˢ��,��ֱ�ӷ���.
	//if(warnFlag == false) return ; 
	refreshAlarm_H();
	
}
//�澯�Զ�ˢ�� Add by Hemc
function setAutoRefresh_H(obj){
	var _checked = obj.checked;
	//���ñ�־λ
	warnFlag = _checked;
	if(warnFlag){
		//��������ˢ��Ƶ��
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
<TITLE>�����豸�澯</TITLE>
<LINK REL="stylesheet" HREF="./css/coolmenu.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/listview.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/webtopo.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/css_blue.css" TYPE="text/css">
<STYLE>
    v\:* { BEHAVIOR: url(#default#VML) }
	SPAN,DIV,TD{
		FONT-FAMILY: "����", "Tahoma","Arial"; FONT-SIZE: 12px
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
			<input type="checkbox" title="���ø澯�Զ�ˢ��" onclick="javascript:setAutoRefresh_H(this);" name="radioWarn" id="autoRefresh" ><label for="autoRefresh">�Զ�</label>
			<a title="�澯�ֶ�ˢ��" href="javascript://" onclick="javascript:RefreshByHand_H();" name="radioWarn" id="handRefresh"><label for="handRefresh">�ֶ�</label></a> 
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
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="CommonMtd" scope="request" class="com.linkage.litms.uss.CommonMtd" />
<%
	request.setCharacterEncoding("GBK");
	String customerID = request.getParameter("customerID");
	Date dt = new Date();
	long lms = dt.getTime();
	String usernameListHTML = CommonMtd.getDeviceListHTML(customerID);
%>

<%
	//String customerID = request.getParameter("customerID");
	//��ȡ�ͻ���Ϣ
	//String pmeeInfoHTML = GetPmeeInfo.getPmeeInfo(customerID);

	//out.println(pmeeInfoHTML);

%>

<%@page import="java.util.Date"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../js/common.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//<!--
var lms_now = parseInt("<%=lms%>");	//��ǰʱ��
var node_number = 30;		//��ʾ�ڵ���
var subobj_cur=null;		//��ǰѡ�е��ӽڵ����;
var objcn_cur = null;		//��ǰ�ڵ��className
var IsPoll = false; //�Ƿ���ѯ
var username = null;
var curTime = null;
var customerID = <%= customerID%>;

// ----------------------������״���ڽṹ���� modify by YYS 2006-10-9 --------------
//��ԭ��״���ڽṹ�滻Ϊ��������ʽ

//���ɽڵ�HTML add by YYS 2006-10-9
//��ԭ��״���ڽṹ�滻Ϊ��������ʽ
// ------------------------------------------------------------------------------
function initOselect(){
	var sHtml = "";
	var dt,year,month,day;
	var lms_cur = parseInt(lms_now);
	//��ʼ�����нڵ�
	for(var i=0;i<node_number;i++){
		dt = new Date(lms_cur);
		year	= dt.getYear();
		month	= dt.getMonth()+1;
		day		= dt.getDate();
		addOption(year,month,day);
		lms_cur = lms_cur - 24*3600*1000;
	}
	
	//���߷�������ǰʱ���ʼ��������ѯҳ��
	dt = new Date(lms_now);
	year	= dt.getYear();
	month	= dt.getMonth()+1;
	day		= dt.getDate();
	dt = new Date(month+"/"+day+"/"+year);
	refreshData(dt.getTime()/1000);
}

function addOption(year,month,day){
	var nodeStr = year+"-"+month+"-"+day;
	var dt = new Date(month+"/"+day+"/"+year);
	var lms = dt.getTime()/1000;
	
	var oSselect = document.all("queryDate");
	var oOption = document.createElement("OPTION");
	oSselect.options.add(oOption);
	oOption.innerText = nodeStr;
	oOption.value = lms;
}

function userNameSelect() {
	var obj = event.srcElement;
	name = obj.options[obj.selectedIndex].value;
	username = name;

	var page = "customerPmee_inside.jsp?customerID="+customerID+"&username="+username+"&start="+curTime+"&refresh="+Math.random();
	
	showMsgDlg();
	document.all("childFrm").src = page;
}


function refreshData(lms_now){
	
	var obj;
	var lms_start;
	username = document.all.usernameList.value;
	if(lms_now==null){
		obj = event.srcElement;
		lms_start = obj.options[obj.selectedIndex].value;
	}else{//ҳ���ʼ��ʱ����ʹ��
		lms_start = lms_now;
	}
	
	//�ѵ�ǰѡ���ʱ�丳��ȫ�ֱ���curTime
	curTime = lms_start;
	o = document.all;
	if (null == username)	{
		//��ʼʱ�ѵ�һ������username
		username = o.usernameList[0].value;
	}
	var page = "customerPmee_inside.jsp?customerID="+customerID+"&username="+username+"&start="+curTime+"&refresh="+Math.random();
	
	showMsgDlg();
	document.all("childFrm").src = page;

}

function getPolltime(){
	var o = document.frm;
	for(var i=0;i<o.polltime.length;i++){
		if(o.polltime[i].checked)
			return o.polltime[i].value;
	}
	return 5;
}


function reloadtime(v){
	page = document.all("childFrm").src;
	pos = page.indexOf("&polltime=");
	tmp = page.substring(0,pos);
	page = tmp + "&polltime="+ v;
	//alert(page)
	showMsgDlg();
	document.all("childFrm").src = page;
}


function reloadfilter(){
	
	//----------------------���ع��� add by YYS 2006-9-24 ----------------
	var ifcontainChild;//�Ƿ������������
	for(var i=0;i<document.all("ifcontainChild").length;i++){
     	if(document.all("ifcontainChild")[i].checked){
            ifcontainChild=document.all("ifcontainChild")[i].value;
           break;
      	}
   	}
   	//alert(ifcontainChild);
   	//--------------------------------------------------------------------
	
	var obj = document.frm2.filterstatus;
	var s,s1,s2;
	page = document.all("childFrm").src;
	//�滻ˢ�������
	pos1 = page.indexOf("&refresh=");
	pos2 = page.indexOf("&polltime=");
	s1 = page.substring(0,pos1);
	
	if(pos2==-1) s2="";
	else s2=page.substring(pos2,page.length);
	
	//----------------------���ع��� modify by YYS 2006-9-24 --------------
	page = s1+"&refresh="+Math.random()+"&ifcontainChild="+ ifcontainChild +"&city_id="+city_id+s2;
	
	if(!obj[0].checked){
		s="";
		for(var i=1;i<obj.length;i++){
			if(obj[i].checked){
				if(s=="") s = obj[i].value;
				else s+=","+obj[i].value;
			}
		}
		page = s1+"&refresh="+Math.random()+"&filter="+ s +"&ifcontainChild="+ ifcontainChild +"&city_id="+ city_id +s2;
	}
	//--------------------------------------------------------------------

	document.all("childFrm").src = page;
	//alert(document.all("childFrm").src);
}

function TreeMouseOver(obj){
	objcn_cur = obj.className;
	obj.className="clsMouseOver";
}

function TreeMouseOut(obj){
	if(obj.className=="clsCurrentHasFocus") return;
	obj.className=objcn_cur;
}

function TreeMouseDown(obj){
	obj.className="clsMouseDown";
}

function initialize(){
	initOselect();
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}

function doReturn(customerID) {
	 window.location.replace("../index.jsp?CustomerId="+customerID);
}

//-->
</SCRIPT>
<HTML>
<HEAD>
<TITLE></TITLE>
<link href='../css/tags.css' rel='stylesheet' type='text/css'>
</HEAD>
<BODY class='bd'>
<div id="PendingMessage"
	style="position: absolute; z-index: 3; top: 240px; left: 250px; width: 250; height: 60; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size: 14px; font-family: ����">���Եȡ�����������</span></td>
	</tr>
</table>
</center>
</div>
<TABLE cellSpacing=0 cellPadding=0 width=775 align=center border=0>
	<TR>
		<TD valign=top background=../images/title.jpg height=60>
		<TABLE cellSpacing=0 cellPadding=0 width=775 border=0>
			<TR>
				<TD width=59 height=40>&nbsp;</TD>
				<TD class=font1 width=701>�ͻ��ն�������Ϣ</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>

<TABLE align='center' border=0 cellspacing=0 cellpadding=0 width=775>
	<!-- <TR><TD HEIGHT=20>&nbsp;</TD></TR> -->
	<TR>
		<TD align="center">
		<TABLE width="100%" border=0 align='center' cellpadding='1' cellspacing='1' bgcolor='#efefef'>
			<TR class="trr">
				<td class='tdd'>�ÿͻ���Ӧ��ҵ���û���&nbsp;&nbsp;<span id="initCityFilter"><%=usernameListHTML%></span></td>
				<td class='tdd'>��ѡ��ͳ�����ڣ�&nbsp;&nbsp;<select name="queryDate" onchange="refreshData()"></select></td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=2>
			<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME> 
		</TD>
	</TR>
</TABLE>
<div id="statisticDIV"></div>
<SCRIPT LANGUAGE="JavaScript">
//<!--
initialize();
//-->
</SCRIPT>
</BODY>
</HTML>
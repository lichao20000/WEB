<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>

<%
	request.setCharacterEncoding("gb2312");
	String Host = LipossGlobals.getLipossProperty("socket.UserInst.SpeedTestHost");	
	String city=" ";
	if(Host!=null)
	{
		String[] hostInfos=null;
		hostInfos=Host.split(":");
		city=hostInfos[4];	
	}

boolean flag = false;
//String role_name = (String)session.getAttribute("role_name");
Role role = new RoleSyb(Integer.parseInt(String.valueOf(user.getRoleId())));
String role_name = role.getRoleName();
role = null;

if(role_name.toLowerCase().indexOf("system") != -1 || role_name.equals("System-ͳһ")){
	flag = true;
}
%>
<SCRIPT LANGUAGE="JavaScript">
function show()
{
	parent.frame.cols="20%,*";
	showImg.style.display="none";
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;

function resize()
{
	var obj = document.getElementById("viewPage");
	if(isNC6) obj.height=window.innerHeight-20;
}

//д����
function writeDate()
{	
	date=new Date();
	year=date.getYear();
	if(year<2000) year+=1900;
	month=date.getMonth()+1;
	day=date.getDate();
	document.write(year);
	document.write(".");
	document.write(month>9?month:("0"+month));
	document.write(".");
	document.write(day>9?day:("0"+day));
}

//����ת��ͬĿ¼�����л����������
function openWin(object){
	//չ��������νṹ
	show();

	if(object == "rp_manage"){//ת�򵽱���������
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resadmin_tree.jsp";
		document.all('viewPage').src="../treeview/rp_auditingList.jsp";
	}

	if(object == "rp_sum"){//ת����Դͳ�ƽ���
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=1";
		document.all('viewPage').src="../../rpt_userViewData.jsp?querytype=1";
	}
	
	if(object == "rp_check"){//ת�򵽿���ָ�����
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=2";
		document.all('viewPage').src="../../dslam_assess_speed_Form.jsp";
	}

	if(object == "rp_perf"){//ת������ָ�����
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=3";
		document.all('viewPage').src="../../rpt_perf_lgmonths.jsp";
	}
	
	if(object == "CEO"){//ת��CEO��ͼָ�����
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=4";
		document.all('viewPage').src="../../rpt_ceoview.jsp?querytype=1";
		//parent.window.location.href = "../../CEOView.jsp";
	}
}
</script>
<%@page import="com.linkage.liposs.system.dbimpl.RoleSyb"%>
<%@page import="com.linkage.liposs.system.Role"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<style>
a.title:visited {
	font-size: 10pt;
	font-weight: bold;
	color: white;
	text-decoration: none;
}
a.title:hover {
	font-size: 10pt;
	font-weight: bold;
	color: #FF9900;
	text-decoration: underline;
}
a.title:link {
	font-size: 10pt;
	font-weight: bold;
	color:white;
	text-decoration: none;
}
</style>
</head>
<body onload='resize()' onresize='resize()'>
<table border="0" cellpadding="0" cellspacing="0" height=100% width=100%>
  <tr style="background-color:#2951AD;FONT:bold 13px Verdana ;color=white">
		
    <td width=455 height=24> <nobr> <IMG SRC="../img/showtoc.gif" title="��ʾ�˵�" id="showImg" BORDER=0 style="display:none;cursor: pointer; cursor: hand;"
		onmouseover="status='��ʾ�˵�'"		onmouseout="status='�ۺ�����'"	onclick="show()"></IMG> 
		
	  &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('CEO')" class=title>CEO��ͼ</a> 	
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_sum')" class=title>��Դͳ��</a>
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_check')" class=title>����ָ��</a> 
      
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_perf')" class=title>����ָ��</a> 
	  <%if(flag){%>
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_manage')" class=title>�������</a> 
	  <%}%>
      </nobr> </td>
		
    <td  align=right width="532"> 
      <SCRIPT LANGUAGE="JavaScript">
      		document.write("<font color=white>");
			document.write("<nobr>��ǰ����λ�ã�<%=city%>����ϵͳ&nbsp;&nbsp;");
			writeDate();
			document.write("</nobr></font>");
		</SCRIPT>
		</td>
	</tr>
	<tr>
	<%if(session.getAttribute("role_name") == null){%>
    <td colspan="2" bgcolor="lightsteelblue"> <iframe src="../treeview/default.html" id="viewPage" frameborder=0 height=100% width=100% border="0"> 
    </td>
    <%}else{%>
    <td colspan="2" bgcolor="lightsteelblue"> <iframe src="<%=((session.getAttribute("role_name").equals("rpLead"))?"../../rpt_ceoview.jsp?querytype=1":"../treeview/default.html")%>" id="viewPage" frameborder=0 height=100% width=100% border="0"> 
    </td>
    <%}%>
	</tr>
</table>
</body>
</html>





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

if(role_name.toLowerCase().indexOf("system") != -1 || role_name.equals("System-统一")){
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

//写日期
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

//用于转向不同目录树，切换主界面呈现
function openWin(object){
	//展开左边树形结构
	show();

	if(object == "rp_manage"){//转向到报表管理界面
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resadmin_tree.jsp";
		document.all('viewPage').src="../treeview/rp_auditingList.jsp";
	}

	if(object == "rp_sum"){//转向到资源统计界面
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=1";
		document.all('viewPage').src="../../rpt_userViewData.jsp?querytype=1";
	}
	
	if(object == "rp_check"){//转向到考核指标界面
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=2";
		document.all('viewPage').src="../../dslam_assess_speed_Form.jsp";
	}

	if(object == "rp_perf"){//转向到性能指标界面
		parent.window.frames[1].document.all('treeView').src="../treeview/rp_resstat_tree.jsp?rootid=3";
		document.all('viewPage').src="../../rpt_perf_lgmonths.jsp";
	}
	
	if(object == "CEO"){//转向到CEO视图指标界面
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
		
    <td width=455 height=24> <nobr> <IMG SRC="../img/showtoc.gif" title="显示菜单" id="showImg" BORDER=0 style="display:none;cursor: pointer; cursor: hand;"
		onmouseover="status='显示菜单'"		onmouseout="status='综合网管'"	onclick="show()"></IMG> 
		
	  &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('CEO')" class=title>CEO视图</a> 	
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_sum')" class=title>资源统计</a>
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_check')" class=title>考核指标</a> 
      
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_perf')" class=title>性能指标</a> 
	  <%if(flag){%>
      &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:openWin('rp_manage')" class=title>报表管理</a> 
	  <%}%>
      </nobr> </td>
		
    <td  align=right width="532"> 
      <SCRIPT LANGUAGE="JavaScript">
      		document.write("<font color=white>");
			document.write("<nobr>当前所在位置：<%=city%>报表系统&nbsp;&nbsp;");
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





<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page
	import="com.linkage.litms.*,com.linkage.litms.LipossGlobals"%>
<%
	request.setCharacterEncoding("GBK");
	//根据type判断指向哪个topo
	String type = request.getParameter("type");
	if (type == null)
		type = "1";
	int iType = Integer.parseInt(type);
	String idStr = "1/0", resStr = "net_topo_res.js";
	switch (iType) {
	case 1://网络拓扑
		idStr = "1/0";
		resStr = "net_topo_res.js";
		break;
	case 2://主机拓扑
		idStr = "2/0";
		resStr = "host_topo_res.js";
		break;
	case 3://业务视图
		idStr = "3/0";
		resStr = "oper_topo_res.js";
		break;
	case 4://用户视图
		idStr = "4/0";
		resStr = "user_topo_res.js";
		break;
	}
%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml" xmlns:mylist>
<?IMPORT NAMESPACE="mylist" IMPLEMENTATION="listview.htc"/>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<TITLE><%=LipossGlobals.getLipossName()%>--Web Topo</TITLE>
<LINK REL="stylesheet" HREF="./css/coolmenu.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/listview.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/webtopo.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/style.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="../css/css_blue.css" TYPE="text/css">
<STYLE>
    v\:* { BEHAVIOR: url(#default#VML) }
	SPAN,DIV,TD{
		FONT-FAMILY: "宋体", "Tahoma","Arial"; FONT-SIZE: 12px
	}
</STYLE>
</HEAD>

<script language=javascript>
function hide()
{
	if(isIE)
	{
		var fm = window.top.document.getElementById('frame');
		var img = window.top.frames[2];
		
		//存在则执行
		if(typeof(fm) != "undefined" && fm != null){
			fm.cols = "1,*";
		}
		
		//存在则执行
		if(typeof(img) != "undefined" && img != null){
			img = img.document.getElementById('showImg');
			img.style.display='inline';
		}
	}	
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;

</script>

<BODY oncontextmenu="return false" onselectstart="return false"
	ondragstart="return false" leftmargin=0 topmargin=0
	onload="initImageSize();hide();" onresize="resizeView()">
<script src="MenuBar.js"></script>
<SPAN id="idRefreshIMG" style="display:none"></SPAN>
<SPAN id="idRMenuHTML"></SPAN>
<div style="position:absolute;left:10px;top:10px;visibility:hidden"><img
	id="idTmpImg"></div>

<TABLE width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0" style="display:">
	<!-- 
<TR>
	<TD colspan=2 background="images/banner_bg.jpg"><IMG SRC="images/webtopo_banner.jpg" WIDTH="795" HEIGHT="52" BORDER="0" ALT=""></TD>
</TR>
 -->
	<TR>
		<TD width="205" valign="top">
		<TABLE id="idLeftView" width="100%" cellpadding="0" cellspacing="0"
			class="top_right_line" style="display:none">
			<TR height=26>
				<TD background="images/navigation_bg.jpg" width="170">
				&nbsp;&nbsp;<IMG SRC="images/navigation_icon.gif" WIDTH="20"
					HEIGHT="20" BORDER="0" align="absmiddle">&nbsp;&nbsp;<span
					class="nav_icon">导 航 栏</span></TD>
				<TD width="35" nowrap>&nbsp;</TD>
			</TR>
			<TR height="100%">
				<TD colspan=2>
				<div id="idNavigation"
					style="width:205;height:300;overflow:auto;border-top: 1px solid #999999;"><%@ include
					file="leftmenu.jsp"%><IFRAME
					name="childFrm" ID=childFrm SRC=""
					STYLE="width:50;height:50;display:none"></IFRAME><IFRAME
					name="childFrm2" ID=childFrm2 SRC=""
					STYLE="width:50;height:50;display:none"></IFRAME></div>
				</TD>
			</TR>
		</TABLE>
		</TD>
		<TD width="100%" valign="top">
		<TABLE width="100%" align="left" cellpadding="0" cellspacing="0"
			class="top_line">
			<TR bgcolor="#FFFFFF" class="blue_title">
				<TD height="26" align=left id="overMenu">
				<div align="left">
				<table height="22" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="10">&nbsp;</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="GOUP" style="display:none">
						<div align="center"><img src="images/topban_10.gif"
							width="16" height="15"
							onclick="javascript:CMS_Click(null,1,'getParentTopo.jsp','childFrm');"
							title="返回上一层"></div>
						</td>
						<td width="30">
						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="ADDOBJECT"
							style="display:none">
						<div align="center"><img src="images/topban_1.gif"
							name="Image11" width="16" height="15" border="0" name="Image11"
							title="新增对象" onclick="javascript:swapNewObj();"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="ADDLINK"
							style="display:none">
						<div align="center"><img src="images/topban_2.gif"
							name="Image12" width="16" height="15" border="0" name="Image12"
							title="新增链路" onclick="javascript:swapNewLink();"></div>
						</td>
						<td width="30">
						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="LOADTOPO"
							style="display:none">
						<div align="center"><img src="images/topban_6.gif"
							width="16" title="打开拓扑" height="15"></div>
						</td>

						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="REDISCOVERTOPO"
							style="display:none">
						<div align="center"><img src="images/topban_7.gif"
							width="20" height="15" title="重新发现拓扑"
							onclick="javascript:NewAllTopo('<%=type %>');"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="INCREMENTFINDTOPO"
							style="display:none">
						<div align="center"><img src="images/topban_8.gif"
							width="20" height="15" title="增量发现拓扑"
							onclick="javascript:AddTopo('<%=type %>');"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="SAVETOPO"
							style="display:none">
						<div align="center"><img src="images/topban_9.gif"
							width="20" height="15" title="保存拓扑"
							onclick="javascript:SaveTopo(<%=iType%>)"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="SAVEIMG">
              				<div align="center">
              				<img src="images/saveimg.png" width="16" height="15" title="导出拓扑图" onclick="javascript:SaveImg('<%=iType%>');">
              				</div>	
						</td>						
						<td width="30">
						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>

						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="VIEW_ALL_DEVICE"
							style="display:none">
						<div align="center"><img src="images/topban_4.gif"
							width="16" height="15" title="显示所有网元"
							onclick="javascript:ViewDevice('true');"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="VIEW_MANAGED_DEVICE"
							style="display:none">
						<div align="center"><img src="images/topban_5.gif"
							width="16" height="15" title="显示被管网元"
							onclick="javascript:ViewDevice('false');"></div>
						</td>
						<td width="30">
						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>

						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="WEBTOPO_NETVIEW"
							style="display:none">
						<div align="center"><img src="images/topban_4.gif"
							width="16" height="15" title="网络视图"
							onclick="javascript:goNetWork();"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="WEBTOPO_HOSTVIEW"
							style="display:none">
						<div align="center"><img src="images/topban_5.gif"
							width="16" height="15" title="主机视图"
							onclick="javascript:goHost();"></div>
						</td>
						<td width="30">
						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>
						<td nowrap="nowrap" class=trOutimg id="WARN_ATUO_FRESH" style="display:">
							<div align="center">
								<input type="checkbox" title="设置告警自动刷新" onclick="javascript:setAutoRefresh(this);" name="radioWarn" id="autoRefresh" ><label for="autoRefresh">自动</label>
								<a title="告警手动刷新" href="javascript://" onclick="javascript:RefreshByHand();" name="radioWarn" >手动</a>
							</div>
						</td>
					</tr>
				</table>
				</div>
				</TD>
			</TR>
			<TR height="100%">
				<TD background="images/top_background.gif" class="top_leftt_line">
				<SCRIPT LANGUAGE="JavaScript" SRC="coolmenu.js"></SCRIPT> <SCRIPT
					LANGUAGE="JavaScript" SRC="<%=resStr%>"></SCRIPT> <SCRIPT
					LANGUAGE="JavaScript" SRC="topo.js"></SCRIPT>
				<Div id="idWebTopo" style="width:800px;overflow:auto;"
					src="getTopoByIdXML.jsp?pid=<%=idStr%>"
					onmousedown="Main_MouseDown()" onmousemove="Main_MouseMove()"
					onmouseup="Main_MouseUp()">
					<v:rect id=selRect
					style="DISPLAY: none; Z-INDEX:  255; POSITION:  absolute"
					coordsize="21600,21600" strokecolor="#10fc18">
					<v:fill opacity="0"></v:fill>
				</v:rect>
	
				</Div>
				</TD>
			</TR>
			<TR bgcolor="buttonface">
				<TD height="8" class="top_leftt_line" align="center"><IMG
					SRC="images/bottom_show.jpg" WIDTH="66" HEIGHT="8" BORDER="0"
					onmouseover="this.src='images/bottom_show_over.jpg'"
					onmouseout="this.src='images/bottom_show.jpg'" style="cursor:hand"
					onclick="showView('idBottomView','bottom')"></TD>
			</TR>
			<TR>
				<TD width="100%" height="100%" valign="top" class="top_leftt_line">
				<TABLE id="idBottomView" cellpadding="0" cellspacing="0"
					style="display:none;">
					<TR>	<!-- getAllAlarmXML.jsp Remove by Hemc 2006-10-25 -->
						<TD><mylist:listview ID="oList" dataXML=""
							width="600" height="140" onRowSelected="clickRow()"
							onmousedown="rightclick()" /></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript" SRC="main_control.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="left_net_control.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="XMLHttp.js"></SCRIPT>
<span style="display:none"> <!--初始化告警类，获取所有告警的入口-->
<FORM METHOD=POST ACTION="initStaticParam.jsp" name="initForm"
	target="childFrm"><TEXTAREA NAME="curLayerObjList" ROWS="5"
	COLS="80"></TEXTAREA></FORM>
</span>

<span id="idWarn_5" style="display:none">
<TABLE border=0 cellpadding=2 cellspacing=0 width="10" align="center">
	<TR>
		<TD align="center"><B><!--Warn--></B></TD>
	</TR>
</TABLE>
</span>
<span id="idWarn_4" style="display:none">
<TABLE border=0 cellpadding=2 cellspacing=0 width="10" align="center">
	<TR>
		<TD align="center"><B><!--Warn--></B></TD>
	</TR>
</TABLE>
</span>
<span id="idWarn_3" style="display:none">
<TABLE border=0 cellpadding=2 cellspacing=0 width="10" align="center">
	<TR>
		<TD align="center"><B><!--Warn--></B></TD>
	</TR>
</TABLE>
</span>
<span id="idWarn_2" style="display:none">
<TABLE border=0 cellpadding=2 cellspacing=0 width="10" align="center">
	<TR>
		<TD align="center"><B><!--Warn--></B></TD>
	</TR>
</TABLE>
</span>
<span id="idWarn_1" style="display:none">
<TABLE border=0 cellpadding=2 cellspacing=0 width="10" align="center">
	<TR>
		<TD align="center"><B><!--Warn--></B></TD>
	</TR>
</TABLE>
</span>
<span id="idWarn_0" style="display:none">
<TABLE border=0 cellpadding=2 cellspacing=0 width="10" align="center">
	<TR>
		<TD align="center"><B><!--Warn--></B></TD>
	</TR>
</TABLE>
</span>
<!--
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">请稍等······</span></td>
	</tr>
</table>
</center>
</div>
-->
</BODY>
</HTML>

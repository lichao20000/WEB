<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");

	//根据oid判断拓扑进入哪一层
	String oid = request.getParameter("pid");

	//根据type判断指向哪个topo
	String type = request.getParameter("type");
	
	//得到当前层topo图的city_id
	String city_id = request.getParameter("city_id");
	//将city_id存放在session中。
	session.setAttribute("TopoCityID",city_id);
	//Add By Hmc 2006-12-13 删除设备告警session
	session.removeAttribute("webtopo_warn");
	if (type == null)
		type = "1";

	//String idStr = "1/0";
	String resStr = "net_topo_res.js",mainControlStr = "main_control.js";

	int iType = Integer.parseInt(type);
//	switch (iType) {
//	case 1://网络拓扑
//		//idStr = "1/0";
//		resStr = "net_topo_res.js";
//		break;
//	case 2://主机拓扑
//		//idStr = "2/0";
//		resStr = "host_topo_res.js";
//		break;
//	case 3://业务视图
//		//idStr = "3/0";
//		resStr = "oper_topo_res.js";
//		break;
//	case 4://用户视图
//		//idStr = "4/0";
//		resStr = "user_topo_res.js";
//		break;
//		
//	case 5://VPN视图
//		//idStr = "5/0";
//		mainControlStr = "main_control_vpn.js";
//		resStr = "vpn_topo_res.js";
//		break;
//	}
%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml" xmlns:mylist>
<?IMPORT NAMESPACE="mylist" IMPLEMENTATION="listview.htc"/>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<TITLE><%=LipossGlobals.getLipossName()%>--Web Topo</TITLE>
<script src="../Js/jquery.js"></script>
<script src="../Js/jquery.tablesorter_LINKAGE.js"></script>
<script src="./warn/jquery.bussTopo.RealTimeWarn.js"></script>
<script src="./warn/RealTimeWarn.js"></script>
<LINK REL="stylesheet" HREF="./css/coolmenu.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/listview.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/webtopo.css" TYPE="text/css">
<LINK REL="stylesheet" HREF="./css/style.css" TYPE="text/css">
<link rel="stylesheet" href="../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../css/css_ico.css" type="text/css">
<link rel="stylesheet" href="../css/liulu.css" type="text/css">

<STYLE type="text/css">
   #warn_data{
		position:absolute;
		z-index:50000;
		bottom:1px;
		font-style:bold;
		text-align: center;
		height:300px;
		overflow: auto;
		display:none;
	}

    v\:* { BEHAVIOR: url(#default#VML) }
	SPAN,DIV,TD{
		FONT-FAMILY: "宋体", "Tahoma","Arial"; FONT-SIZE: 12px
	}

	/*用于表格排序的图片*/
table.tablesorter thead tr .header {
	background-image: url("../images/bg.gif ");
	background-color: #bdd4ff;
	background-repeat: no-repeat;
	background-position: right;
	cursor: pointer;
}
table.tablesorter thead tr .headerSortUp {
	background-image: url("../images/desc.gif");
}

table.tablesorter thead tr .headerSortDown {
	background-image: url("../images/asc.gif");
}
/*告警等级*/
.level_0 {
	background-color:#FFFFFF;
	color:#000000;
	cursor:default;
		size:14px;
}

.level_1 {
	background-color:#E1ECFB;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_2 {
	background-color:#FFEBB5;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_3 {
	background-color:#FFC351;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_4 {
	background-color:#FFB4B2;
	color:#000000;
	cursor:default;
	size:14px;
}
.level_5{
	background-color:#FF0000;
	color:#000000;
	cursor:default;
	size:14px;
}
/*单击后改变的样式*/
.click{
	background-color:steelblue;
	color: white;
	font-size:14px;
}
.content{
	overflow: hidden;
	text-overflow: ellipsis;
	height: 14px;
	cursor: hand;
	width:400px;
}
.td_r{
	white-space: nowrap;
}
th{
	white-space: nowrap;
}
/*告警内容样式*/
.e_c{
	width:23%;
	white-space: normal;
}
.tr_b{
	font-weight:bold;
}
.tr_italic{
	font-style :italic;
	
}
</STYLE>
</HEAD>

<script language=javascript>
//初始化告警
function getWarninit(){
	var gather=$("input[@name='gather']").val();
	if(gather!=null && gather!=""){
		$.post(
			"RealTimeWarn!getupdateData.action",
			{
				gather_val:gather,
				fetchCount:200,
				columnID:$("input[@name='columnID']").val(),
				ruleid:-2
			},
			function(s){
				var tmp = eval("("+s+")");
				var data=$(tmp.data);
				var lev_0=eval(tmp.lev_0);
				var lev_1=eval(tmp.lev_1);
				var lev_2=eval(tmp.lev_2);
				var lev_3=eval(tmp.lev_3);
				var lev_4=eval(tmp.lev_4);
				var lev_5=eval(tmp.lev_5);
				var get_num=lev_0+lev_1+lev_2+lev_3+lev_4+lev_5;//当前取得告警数据总数
				$("input[@name='gather']").val(tmp.gather);
				if(get_num>0){
					$("#tbody").html(data);
					$("#warn_data").trigger("update");
					$("#warn_data").slideDown(1500);
					window.setTimeout("hidediv()",5000);
				}
			}
		);
	}else{
		$.post(
			"./RealTimeWarn!getALLWarnData.action",
			{
				ruleid:-2,
				fetchCount:200
			},
			function(data){
				$("#warn_data").html(data);
				var lev0 = $("tr[@lev='0']").length;
				var lev1 = $("tr[@lev='1']").length;
				var lev2 = $("tr[@lev='2']").length;
				var lev3 = $("tr[@lev='3']").length;
				var lev4 = $("tr[@lev='4']").length;
				var lev5 = $("tr[@lev='5']").length;
				var num=lev3+lev4+lev5;
				if(num>0){
					$("#warn_data").slideDown(1500);
					window.setTimeout("hidediv()",5000);
				}
			}
		);
	}
	
}
//隐藏告警
function hidediv(){
	$("#warn_data").fadeOut(1000);
}
function test(){
	alert("hello");
}

function hide()
{
	if(isIE)
	{
		var fm = window.top.document.getElementById('frame');
		var img = window.top.frames[2];
		
		//存在则执行
		if(typeof(fm) != "undefined" && fm != null){
			fm.cols = "1,*";
			img.document.getElementById('tdmenu').width="209";
		}
		
		//存在则执行
		if(typeof(img) != "undefined" && img != null){
			img = img.document.getElementById('showImg');
			
			if(typeof(img) != "undefined" && img != null){
				img.style.display='inline';
			}
			
		}
	}	
}

var isNC6 = (document.getElementById && !document.all)?true:false;
var isIE = (document.all)?true:false;


function NetViewDbClick(id){
	var obj = document.all(id);
	obj.dbclick();
}

parent.parent.window.frames[1].document.all('treeView').src = "../getTopoTree.jsp";
</script>

<BODY oncontextmenu="return false" onselectstart="return false"
	ondragstart="return false" leftmargin=0 topmargin=0
	onload="initImageSize();hide();" onresize="resizeView()">
<script src="MenuBar.js"></script>
<SPAN id="idRefreshIMG" style="display:none"></SPAN>
<SPAN id="idRMenuHTML"></SPAN>
<div style="position:absolute;left:10px;top:10px;visibility:hidden"><img
	id="idTmpImg"></div>

<TABLE id="main.jsp" width="100%" border="0" align="center" cellpadding="0"
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
					style="width:205;height:300;overflow:auto;border-top: 1px solid #999999;">
					<%@ include file="leftmenu.jsp"%>
					<IFRAME name="childFrm" ID="childFrm" SRC=""
						STYLE="width:50;height:50;display:none">
					</IFRAME>
					<IFRAME name="childFrm2" ID="childFrm2" SRC=""
						STYLE="width:50;height:50;display:none">
					</IFRAME>
				</div>
				</TD>
			</TR>
		</TABLE>
		</TD>
		<TD width="100%" valign="top">
		<TABLE width="100%" align="left" cellpadding="0" cellspacing="0"
			class="top_line">
			<TR bgcolor="#FFFFFF" class="green_title">
				<TD height="26" align=left id="overMenu" colspan=2>
				<div align="left">
				<table height="22" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="10">&nbsp;</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="GOUP" style="display:none">
						<div align="center"><img src="images/topban_10.gif"
							width="16" height="15"
							onclick="javascript:CMS_Click(null,1,'getParentTopo.jsp?topoType=<%=iType %>','childFrm');"
							title="返回上一层"></div>
						</td>
						<td width="10">&nbsp;</td>
						<td width="30">
						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>
						<!-- <td width="30" class=trOutimg onmouseover="className='trOverimg'"
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
 -->
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="LOADTOPO"
							style="display:none">
						<div align="center"><img src="images/save2.gif"
							width="16" title="保存拓扑" height="15" onclick="javascript:SaveTopo(<%=iType%>)"></div>
						</td>
 						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="SAVEIMG">
              				<div align="center">
              				<img src="images/saveimg.png" width="16" height="15" title="导出拓扑图" onclick="javascript:SaveImg('<%=iType%>');">
              				</div>	
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="SAVEIMG">
              				<div align="center">
              				<img src="images/saveimg.png" width="16" height="15" title="导出所选网元" onclick="javascript:SaveImgSelected('<%=iType%>');">
              				</div>	
						</td>
						<td width="30">
 						<div align="center"><img src="images/topban_line.gif"
							width="30" height="22"></div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="WARN_CONFIG_RULE">
							<div align="center">
								<img src="images/warn_rule.gif" width="10" height="13" title="告警规则" href="javascript://" onclick="javascript:ConfigWarnRule();">
							</div>
						</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="WARN_ALL_VIEW">
                    		<div align="center">
								<img src="images/warn_board.gif" width="16" height="15" title="实时告警牌" href="javascript://" onclick="javascript:ViewAllWarn();" >
							</div>
            			</td>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="WARN_VOICE_CONFIG">	
                    		<div align="center">
								<img src="images/sound.gif" width="14" height="13" title="告警声音" href="javascript://" onclick="javascript:ConfigWarnVoice();">
							</div>												
						</td>
						<td width="30">
						<div align="center">
						<img src="images/topban_line.gif" width="30" height="22">
						</div>
						<td width="30" class=trOutimg onmouseover="className='trOverimg'"
							onmouseout="className='trOutimg'" id="IPBROWSER">
                    		<div align="center">
								<img src="images/go.gif" width="16" height="15" title="IP Browser" href="javascript://" onclick="javascript:IPBrowser();" >
							</div>
            			</td>
						</td>
						<td nowrap="nowrap" class=trOutimg id="WARN_ATUO_FRESH" style="display:none">
							<div align="center">
								<input type="checkbox" title="设置告警自动刷新" onclick="javascript:setAutoRefresh(this);" name="radioWarn" id="autoRefresh" ><label for="autoRefresh">自动</label>
								<a title="告警手动刷新" href="javascript://" onclick="javascript:RefreshByHand();" name="radioWarn" >手动</a>
							</div>
						</td>
						<td nowrap="nowrap" class=trOutimg id="CONFIG_TOPOLAYER" style="display:none">
							<div align="center">
								<a title="将当前Topo层设置为默认层" href="javascript://" onclick="javascript:setTopoLayer(true);">设置Topo层</a>
								<a title="取消设置的默认Topo层" href="javascript://" onclick="javascript:setTopoLayer(false);">撤销设置</a>
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
            			<td width="30"><div align="center"><img src="images/topban_line.gif" width="30" height="22"></div></td>
						<td nowrap class=trOutimg id="WEBTOPO_DEVICE_DATA" style="display:none"> 
							<div>
								<select id="_typeSelect" class="column" onChange="changeSelectType()">
									<option value="3">设备序列号</option>
									<option value="4">用户宽带帐号</option>
									<option value="1">设备IP/域名</option>
									<option value="2">设备名称</option>
									<option value="5">设备型号</option>
								</select>
								<input id="_searchValue" type="text" class="column" style="width:100" onKeydown="if(event.keyCode==13) webtopoSearch()">
								<input type="button" onclick="webtopoSearch()" class=column value="查询" id="btn_QueryDev">
								<span id="divSelectVendor"></span>
								<span id="divSelectDevSerial"></span>
							</div>
						</td>
						<td nowrap class=trOutimg id="WEBTOPO_SEARCHE_DEVICE"><div id="divSelectDevice"></div></td>
						<td nowrap class=trOutimg> 
							<div>
								<select id="titleStyle" class="column">
									<option value="0">按序列号显示</option>
									<option value="1">按ADSL用户显示</option>
									<option value="2">按IPTV用户显示</option>
									<option value="3">按VOIP用户显示</option>
								</select>
								<input type="button" onclick="changeTitle()" class=column value="显示">
							</div>
						</td>
						<TD><IFRAME ID="getTitle" SRC="" STYLE="display:none"></IFRAME></TD>
					</tr>
				</table>
				</div>
				</TD>
			</TR>
			<TR height="100%">
				<td style="display:none">
				</td>
				<TD background="images/top_background.gif" class="top_leftt_line">
				<SCRIPT LANGUAGE="JavaScript" SRC="coolmenu.js"></SCRIPT> 
				<SCRIPT	LANGUAGE="JavaScript" SRC="<%=resStr%>"></SCRIPT> 
				<SCRIPT	LANGUAGE="JavaScript" SRC="topo.js"></SCRIPT>

				<Div id="idWebTopo" style="width:800px;overflow:auto;align:center;valign:center"
					src="getTopoByPidXML.jsp?pid=<%=oid%>&topoType=<%=iType %>"
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
			<TR bgcolor="buttonface" style="display:none">
				<TD height="8" class="top_leftt_line" align="center" colspan=2><IMG
					SRC="images/bottom_show.jpg" WIDTH="66" HEIGHT="8" BORDER="0"
					onmouseover="this.src='images/bottom_show_over.jpg'"
					onmouseout="this.src='images/bottom_show.jpg'" style="cursor:hand"
					onclick="showView('idBottomView','bottom')"></TD>
			</TR>
			<TR height="100%" style="display:none">
				<TD width="100%" height="100%" valign="top" class="top_leftt_line" colspan=2>
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

<SCRIPT LANGUAGE="JavaScript" SRC="<%=mainControlStr%>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="XMLHttp.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="left_net_control.js"></SCRIPT>
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
<div id="warn_data" align="right" width="100%">

</div>
</BODY>
</HTML>
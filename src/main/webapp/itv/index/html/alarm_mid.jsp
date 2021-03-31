
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.module.lims.stb.util.CityUtil"%>
<%@page import="com.linkage.module.lims.system.common.database.Cursor"%>
<%@page import="com.linkage.module.lims.system.common.database.DataSetBean"%>
<%@page import="com.linkage.module.lims.system.UserRes"%>
<%@page import="com.linkage.module.lims.system.User"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%> 
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<title>ITV业务综合网管</title>
<link href="<s:url value='/css/css_blue.css'/>" rel="stylesheet" type="text/css">
<link href="../../../css/layout.css" rel="stylesheet" type="text/css">
<link href="../../../css2/text.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}

body,td,th {
	font-size: 12px;
}
-->
</style>
		<style type="text/css">
<!--
.caption {
	font-size: 12px;
	color: #FFFFFF;
	background-color: #00CCFF;
	padding-left: 5px;
	cursor: default;
	font-family: "Verdana", "Arial";
	border: 1px inset;
}

body {
	background-color: #f6f6f6;
	border: 1px inset;
	overflow: scroll;
}

td {
	font-family: "Verdana", "Arial";
	font-size: 12px;
	border: 0px;
}

td.caption {
	font-family: "Verdana", "Arial";
	font-size: 11px;
	border: 0px;
}

td.td_ {
	font-family: "Verdana", "Arial";
	font-size: 9px;
	border: 0px;
}

.win {
	filter: BlendTrans(duration =     1)
		DropShadow(Color =     #cccccc, OffX =     3, OffY =     3)
		alpha(opacity =     90)
}

a {
	text-decoration: none;
	color: #003399;
}

a:hover {
	color: #FF0000;
}

input {
	font-family: "Verdana", "Arial";
	font-size: 9px;
	border-width: 1px;
}

.statusbar {
	font-family: "Tahoma", "Verdana";
	font-size: 9px;
	color: #999999;
	padding-left: 3px;
}

.button {
	border: 1px outset;
	text-align: center;
}

.navframe {
	padding: 5px;
}
-->
</style>

		<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<style type="text/css">
<!--
.style1 {	color: #FFFFFF;
	font-weight: bold;
}
.style1 {color: #FFFFFF}
.style2 {color: #07698e}
-->
</style>
		<SCRIPT LANGUAGE="JavaScript">
function loadEPGReport()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/FirstPage!getEPGReport.action"/>',
		success : function(data){
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#EPGAKA").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
function loadNFReport()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/FirstPage!getNFReportTwo.action"/>',
		success : function(data){		
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#AKAVOD").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
function loadWarnReport()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/FirstPage!getWarnReport.action"/>',
		success : function(data){		
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#SLCSWarn").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}

function loadISATerminal()
{

	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/ISAPic!getISATerminalReport.action"/>',
		success : function(data){	
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#ISATerminal").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
function loadISAEPGSZX()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/ISAPic!getISAEPGReportTwo.action"/>',
		success : function(data){	
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#ISAEPGSZX").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
function loadEPG()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/ISAPic!getEpg.action"/>',
		success : function(data){	
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#EPG").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
function loadDatainput()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/FirstPage!getDatainputReport.action"/>',
		success : function(data){	
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#datainput").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
function loadTown()
{
	$.ajax({
	    type:'POST',
		url:'<s:url value="/LIMS/menu/FirstPage!getTownReport.action"/>',
		success : function(data){	
			var innerH = "<img  src='<s:url value='" + data + "'/>' >";
			$("#town").attr("innerHTML",innerH);
		},
		error:function(){
			alert('ajax通讯异常');
		}
	});
}
</SCRIPT>

	</head>
	<body onload="" scroll="yes">
		<table width="97%" height="97%" border="0" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" bgcolor="#F1F1F1">
				</td>
				<td valign="top" width="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="19" background="../../../images/back.jpg">
								<span class="text">&nbsp;&nbsp;您当前的置：告警管理主页<br> </span>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr >
							<td valign="top" width="100%">
							<table>
								<tr>
									<td>
								<table width="100%"  border="0" align="center">
							      <tr>
							        <td style=" border-color:#FFFFFF">
							          <table width=345 border="0" align="center" cellpadding="0" cellspacing="0">
							            <tr>
							              <td width="345" height="29" valign="top" background="../../../images/index_1.png"><table width="98%"  border="0" cellspacing="2" cellpadding="0">
							                <tr>
							                  <td width="12">&nbsp;</td>
							                  <td valign="top"><span class="style1">服务质量</span></td>
							                </tr>
							              </table></td>
							            </tr>
							            <tr>
							             <td class="text" background="../../../images/index_2.png"  ><table width="335" border="0" align="center" cellpadding="0" cellspacing="0">
							                <tr>
							                  <td class="bottom_line" width="324" height="238" id="ISATerminal"><span>正在加载中...</span></td>
							                </tr>
							                <tr>
							                	<td id="EPG" width='324' height='238'><span>正在加载中...</span></td>
							                </tr>
							              </table>    
							                </td>
							            </tr>
							            <tr>
							              <td><img src="../../../images/index_3.png" width="345" height="12"></td>
							            </tr>
							          </table>
							          </td>
								</tr>
							</table>         
							          </td>
							        <td style=" border-color:#FFFFFF"><table width=345 border="0" align="center" cellpadding="0" cellspacing="0">
							          <tr>
							            <td width="345" height="29" valign="top" background="../../../images/index_1.png"><table width="98%"  border="0" cellspacing="2" cellpadding="0">
							                <tr>
							                  <td width="12">&nbsp;</td>
							                  <td valign="top"><span class="style1">平台服务</span></td>
							                </tr>
							            </table></td>
							          </tr>
							          <tr>
							            <td class="text" background="../../../images/index_2.png"  ><table width="335" border="0" align="center" cellpadding="0" cellspacing="0">
							                <tr>
							                  <td class="bottom_line" colspan="2" width="324" height="211" id="EPGAKA"><span>正在加载中...</span></td>
							                </tr>
							                <tr>
							                  <td class="bottom_line" width="160" height="255" id="ISAEPGSZX"><span>正在加载中...</span></td>
							                  <td id="SLCSWarn" width="160" height="255"><span>正在加载中...</span></td>
							                </tr>
							            </table></td>
							          </tr>
							          <tr>
							            <td><img src="../../../images/index_3.png" width="345" height="12"></td>
							          </tr>
							        </table></td>
							        <td style=" border-color:#FFFFFF">
							        
							        <table>
							        	<tr>
							        		<td>
							        
							        
							        <table width=345 border="0" align="center" cellpadding="0" cellspacing="0">
							          <tr>
							            <td width="345" height="29" valign="top" background="../../../images/index_1.png"><table width="98%"  border="0" cellspacing="2" cellpadding="0">
							                <tr>
							                  <td width="12">&nbsp;</td>
							                  <td valign="top"><span class="style1">业务层面</span></td>
							                </tr>
							            </table></td>
							          </tr>
							          <tr>
							            <td class="text" background="../../../images/index_2.png"  ><table width="335" border="0" align="center" cellpadding="0" cellspacing="0">
							                <tr>
							                  <td class="bottom_line" width="324" height="200" id="AKAVOD"><span>正在加载中...</span></td>
							                </tr>
							            </table></td>
							          </tr>
							          <tr>
							            <td><img src="../../../images/index_3.png" width="345" height="12"></td>
							          </tr>
							        </table>
							       
							        <table width=345 border="0" align="center" cellpadding="0" cellspacing="0">
							          <tr>
							            <td width="345" height="29" valign="top" background="../../../images/index_1.png"><table width="98%"  border="0" cellspacing="2" cellpadding="0">
							                <tr>
							                  <td width="12">&nbsp;</td>
							                  <td valign="top"><span class="style1">网络层面</span></td>
							                </tr>
							            </table></td>
							          </tr>
							          <tr>
							            <td class="text" background="../../../images/index_2.png"  ><table width="335" border="0" align="center" cellpadding="0" cellspacing="0">
							                <tr>
							                  <td class="bottom_line" width="160" height="230" id="datainput"><span>正在加载中...</span></td>
							                   <td class="bottom_line" width="160" height="230" id="town"><span>正在加载中...</span></td>
							                </tr>
							            </table></td>
							          </tr>
							          <tr>
							            <td><img src="../../../images/index_3.png" width="345" height="12"></td>
							          </tr>
							        </table>
							        </td>
							        	</tr>
							        </table>
							        </td>
							      </tr>
							    </table>
							</td>
						</tr>
					</table>

				</td>
			</tr>
		</table>
	</body>
</html>

<script type="text/javascript">
loadEPGReport();
loadNFReport();
loadWarnReport();
loadISATerminal();
loadISAEPGSZX();
loadDatainput();
loadTown();
loadEPG();
</script>
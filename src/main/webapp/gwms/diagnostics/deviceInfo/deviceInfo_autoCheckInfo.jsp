<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<title>设备信息</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/jquery.blockUI.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	parent.dyniframesize();
});
function EC(leaf){
	if("basicInfo"==leaf){
		var m_bShow;
		m_bShow = (this.basicInfo.style.display=="none");
		this.basicInfo.style.display = m_bShow?"":"none";
	
		sobj = document.all("imgbasic");
		if(m_bShow) {	
			sobj.src = "../../images/up_enabled.gif";
		}
		else{
			sobj.src = "../../images/down_enabled.gif";
		}
		parent.dyniframesize();
	}
}

function selectAll(elmID){
	t_obj = document.getElementsByName(elmID);
	if(!t_obj) return;
	obj = event.srcElement;

	if(obj.checked){
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = true;
		}
	}
	else{
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = false;
		}
	}
}


	function selectOne() {
		var chks = document.getElementsByName("ids");
		var chkAll = document.getElementById("checkboxAll");
		obj = event.srcElement;
		if (!obj.checked) {
			chkAll.checked = false;
		} else {
			var j = 1;
			for (var i = 0; i < chks.length; i++) {
				if (!chks[i].checked) {
					j = 2;
					break;
				}
			}
			if (j == 1) {
				chkAll.checked = true;
			}
		}

	}

	function check() {
		var obj = document.getElementsByName("ids");//选择所有name="interest"的对象，返回数组    
		var ids = '';
		for (var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				ids += "'" + obj[i].value + "',";
			}
		}
		if (ids.length == 0) {
			alert("请至少选择一项检测！");
			return false;
		}
		ids = ids.substring(0, ids.length - 1);

		var device_serialnumber = '<s:property value="deviceInfoMap.device_serialnumber"/>';
		var oui = '<s:property value="deviceInfoMap.oui"/>';
		var deviceId = '<s:property value="deviceId"/>';
		var loid = '<s:property value="deviceInfoMap.username"/>';
		var user_id = '<s:property value="deviceInfoMap.user_id"/>';
		var gw_type = '<s:property value="deviceInfoMap.gw_type"/>';
		var city_id = '<s:property value="deviceInfoMap.city_id"/>';
		parent.block();
		var url = "<s:url value='/gwms/diagnostics/deviceDiagnostics!autoCheck.action'/>";
		$.post(url, {
			device_serialnumber : device_serialnumber,
			oui : oui,
			deviceId : deviceId,
			loid : loid,
			user_id : user_id,
			city_id : city_id,
			gw_type : gw_type,
			ids : ids
		}, function(ajax) {
			parent.unblock();
			alert(ajax);
		});
	}
</script>
</head>

<body>
<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" name="gw_type" value='<s:property value="deviceInfoMap.gw_type"/>' />
<input type="hidden" name="deviceId" value="<s:property value="deviceId"/>" />
<input type="hidden" name="user_id" value="<s:property value="deviceInfoMap.user_id"/>" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" style="background:none;">
						<a href="javascript:EC('basicInfo');" stytle="CURSOR:hand;">
						【基本信息】
						<IMG name="imgbasic" SRC="../../images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">
						</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id=basicInfo>
		<td bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=1 width="100%" >
				<tr align="left">
					<td colspan="4" class="green_title_left">用户信息</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="right" nowrap width="15%">逻辑ID:</td>
					<td width="35%"><s:property value="deviceInfoMap.username"/></td>
					<td class=column align="right" nowrap width="15%">宽带账号:</td>
					<td width="35%"><s:property value="deviceInfoMap.kdname"/></td>
				</tr>
				<tr align="left">
					<td colspan="4" class="green_title_left" >设备信息</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="right" nowrap width="15%">设备序列号:</td>
					<td width="35%"><s:property value="deviceInfoMap.device_serialnumber"/></td>
					<td class=column align="right" nowrap width="15%">设备id:</td>
					<td width="35%"><s:property value="deviceId"/></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="right" nowrap width="15%">设备厂家:</td>
					<td width="35%"><s:property value="deviceInfoMap.vendor_add"/></td>
					<td class=column align="right" width="15%">设备型号:</td>
					<td width="35%"><s:property value="deviceInfoMap.device_model"/></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="right" nowrap width="15%">设备硬件版本:</td>
					<td width="35%"><s:property value="deviceInfoMap.hardwareversion"/></td>
					<td class=column align="right" width="15%">设备软件版本:</td>
					<td width="35%"><s:property value="deviceInfoMap.softwareversion"/></td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align="right" nowrap width="15%">设备注册状态:</td>
					<td width="35%"><s:property value="deviceInfoMap.register"/></td>
					<td class=column align="right" width="15%">IP地址:</td>
					<td width="35%"><s:property value="deviceInfoMap.loopback_ip"/></td>
				</tr>
			</TABLE>
		</td>
	</tr>
	<tr>
		<td HEIGHT=5>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" style="background:none;">
						【检测项信息】
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td bgcolor=#ffffff>
		<table border=0 cellspacing=1 cellpadding=3 width="100%">
			<tr>
				<td align="right">
					<div id="divStep" onclick="javascript:check();" style="cursor:hand">【自动检测】</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td bgcolor=#ffffff>
			<table border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr CLASS="green_foot">
					<td colspan="6"><input type="checkbox" id="checkboxAll" onclick="selectAll('ids')" checked="checked">全选</td>
				</tr>
				<tr>
					<td colspan="6">
						<div id=""></div>
					</td>
				</tr>
				
				<s:if test="deviceCheckProjectList!=null">
					<s:iterator value="deviceCheckProjectList"  status="count">
						<s:if test="#count.index% 6 == 0">
							<tr CLASS="green_foot">
						</s:if>
						<td title="<s:property value="test_require" />"><input type="checkbox" name="ids" onclick="selectOne()" value="<s:property value="id" />" checked="checked"><s:property value="test_name" /></td>
						<s:if test="#count.index != 0 && ((#count.index + 1) % 6) == 0 || #count.last">
							</tr>
							<tr>
								<td colspan="6">
									<div id=""></div>
								</td>
							</tr>
						</s:if>
						
					</s:iterator>
				</s:if>
				
				
			</table>
		</td>
	</tr>
</TABLE>
</FORM>
</body>
</html>

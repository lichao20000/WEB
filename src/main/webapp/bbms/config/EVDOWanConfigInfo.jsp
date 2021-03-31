<%--
EVDO WAN信息-配置上网连接参数
Author: 漆学启
Version: 1.0.0
Date: 2009-10-13
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO WAN信息</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

parent.unblock();

/**
 * 把新增的页面显示出来
 */
function addWanHtml() {

}

/**
 * 新增WAN连接
 */
function addWan() {

}

/**
 * 把编辑页面显示出来
 */
function editWanHtml(deviceId,wanId,wanConnId,wanConnSessId,gatherTime,
					sessType,enable,name,connType,servList,bindPort,
					username,password,ipType,ip,mask,gateway,dnsEnab,dns,
					status,connError,natEnable,pvc,pppAuthProtocol,dialNum,
					workMode,loadPercent,backupItfs,connTrigger) {

	$("td[@id='wanEditTitle']").html("编辑EVDO上网连接");
	$("button[@name='subBtn']").attr({value:" 编辑保存 "});
	$("button[@name='subBtn']").unbind();
	$("button[@name='subBtn']").click(function(){editWan();});
	$("tr[@id='wanConnEdit']").toggle();

	$("select[@name='servList']").val(servList);
	$("select[@name='connTrigger']").val(connTrigger);
	$("select[@name='workMode']").val(workMode);

	//chgWorkMode(workMode);

	if("Main" == workMode){
		$("select[@name='backupItfs']").text(name);
	}else if("Balance" == workMode){
		$("input[@name='loadPercent']").val(loadPercent);
	}

	parent.dyniframesize();

}

/**
 * 详细信息
 */
function showWanDetail(deviceId,wanId,wanConnId,wanConnSessId,gatherTime,
					sessType,enable,name,connType,servList,bindPort,
					username,password,ipType,ip,mask,gateway,dnsEnab,dns,
					status,connError,natEnable,pvc,pppAuthProtocol,dialNum,
					workMode,loadPercent,backupItfs,connTrigger) {

	$("tr[@id='wanConnInfo']").css("display","");

	parent.dyniframesize();

	if(null==name || "null"==name){
		name = "";
	}
	if(null==connType || "null"==connType){
		connType = "";
	}
	if(null==sessType || "null"==sessType){
		sessType = "";
	}
	if(null==servList || "null"==servList){
		servList = "";
	}
	if(null==bindPort || "null"==bindPort){
		bindPort = "";
	}
	if(null==status || "null"==status){
		status = "";
	}
	if(null==workMode || "null"==workMode){
		workMode = "";
	}
	if(null==connTrigger || "null"==connTrigger){
		connTrigger = "";
	}
	if(null==backupItfs || "null"==backupItfs){
		backupItfs = "";
	}
	if(null==loadPercent || "null"==loadPercent){
		loadPercent = "";
	}

	$("td[@id='deteilname']").html(name);
	$("td[@id='deteilconnType']").html(connType);
	$("td[@id='deteilsessType']").html(sessType);
	$("td[@id='deteilservList']").html(servList);
	$("td[@id='deteilbindPort']").html(bindPort);
	$("td[@id='deteilstatus']").html(status);
	$("td[@id='deteilworkMode']").html(workMode);
	$("td[@id='deteilconnTrigger']").html(connTrigger);

	if ("Standby" == workMode) {
		$("td[@id='deteilworkModecode']").html("");
		$("td[@id='deteilworkModevalue']").html("");
	}
	else if("Main" == workMode){
		$("td[@id='deteilworkModecode']").html("备份链路");
		$("td[@id='deteilworkModevalue']").html(backupItfs);
	}else{
		$("td[@id='deteilworkModecode']").html("负荷百分比");
		$("td[@id='deteilworkModevalue']").html(loadPercent);
	}

}

/**
 * 编辑WAN连接
 */
function editWan() {

	var deviceId = $("input[@name='deviceId']").val();
	var wanId = $("input[@name='wanId']").val();
	var wanConnId = $("input[@name='wanConnId']").val();
	var wanConnSessId = $("input[@name='wanConnSessId']").val();

	var servList = $("select[@name='servList']").val();
	var connTrigger = $("select[@name='connTrigger']").val();
	var workMode = $("select[@name='workMode']").val();

	var url = "<s:url value='/bbms/config/EVDOWanACT!config.action'/>";

	if("Standby"==workMode){
		$.post(url,{
			deviceId:deviceId,
			wanId:wanId,
			wanConnId:wanConnId,
			wanConnSessId:wanConnSessId,
			servList:servList,
			connTrigger:connTrigger,
			workMode:workMode
		},function(ajax){

			$("tr[@id='resultTr']").show();
			$("div[@id='result']").html("");
			$("div[@id='result']").html("通知后台:");
			if (ajax == "true")
			{
				$("div[@id='result']").append("成功");
			} else {
				$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">失败</FONT>");
			}
			parent.dyniframesize();
		});
	}else if("Main"==workMode){
		var backupItfs = $("select[@name='workMode']").val();

		$.post(url,{
			deviceId:deviceId,
			wanId:wanId,
			wanConnId:wanConnId,
			wanConnSessId:wanConnSessId,
			servList:servList,
			connTrigger:connTrigger,
			workMode:workMode,
			backupItfs:backupItfs
		},function(ajax){

			$("tr[@id='resultTr']").show();
			$("div[@id='result']").html("");
			$("div[@id='result']").html("通知后台:");
			if (ajax == "true")
			{
				$("div[@id='result']").append("成功");
			} else {
				$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">失败</FONT>");
			}
			parent.dyniframesize();
		});

	}else{
		var loadPercent = $("input[@name='loadPercent']").val();
		if(!IsNumber(loadPercent.val(),"负荷百分比")){
			loadPercent.focus();
			return false;
		}
		$.post(url,{
			deviceId:deviceId,
			wanId:wanId,
			wanConnId:wanConnId,
			wanConnSessId:wanConnSessId,
			servList:servList,
			connTrigger:connTrigger,
			workMode:workMode,
			loadPercent:loadPercent
		},function(ajax){

			$("tr[@id='resultTr']").show();
			$("div[@id='result']").html("");
			$("div[@id='result']").html("通知后台:");
			if (ajax == "true")
			{
				$("div[@id='result']").append("成功");
			} else {
				$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">失败</FONT>");
			}
			parent.dyniframesize();
		});
	}
}

/**
 * 把删除页面显示出来
 */
function delWanHtml(wanId, wanConnId, servList) {
	alert("待处理！");
}

/**
 * 删除WAN连接
 */
function delWan(wanId, wanConnId) {

}

/**
 * "Main"，"Standby",“Balance”默认"Standby"
 */
function chgWorkMode(e){
	if ("Standby" == e) {
		$("td[@id='tdWorkModeTitle']").html("");
		$("td[@id='tdWorkModeText']").html("");
	}
	else if("Main" == e){
		$("td[@id='tdWorkModeTitle']").html("备用链路");
		$("td[@id='tdWorkModeText']").html("<select name='backupItfs'>");
		$("td[@id='tdWorkModeText']").append($("td[@id='tdBackupItfs']").html());
		$("div[@id='result']").append("</select>");
		$("td[@id='tdWorkModeText']").html($("td[@id='tdBackupItfs']").html());
	}else{
		$("td[@id='tdWorkModeTitle']").html("负荷百分比");
		$("td[@id='tdWorkModeText']").html("<input type='text' size='5' name='loadPercent'/>");
	}
}

</script>


</head>

<body>

<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" name="deviceId" value="<s:property value="deviceId"/>" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<tr>
		<TD>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" >
				<tr align="center" height="25">
					<td colspan="3" class="green_title">
						宽带上网
					</td>
				</tr>
				<!--
				<tr align="center" height="25">
					<td colspan="3" align="right" class="column">
						<button name='addImg' onclick="addWanHtml()">新增</button>
					</td>
				</tr>
				 -->
				<tr align="left" id="trnet" STYLE="display:">
					<td colspan="3"  bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
							<tr align="center" bgcolor="#FFFFFF">
								<TD class=column5 align="center">名称</TD>
								<TD class=column5 align="center">连接类型</TD>
								<TD class=column5 align="center">PVC/VLANID</TD>
								<TD class=column5 align="center">IP地址/PPPoE账号</TD>
								<TD class=column5 align="center">连接状态</TD>
								<TD class=column5 align="center">绑定端口</TD>
								<TD class=column5 align="center">服务类型</TD>
								<TD class=column5 align="center">操作</TD>
							</tr>

							<s:iterator var = "wanConfigList" value="wanConfigList">
								<tr align="center" bgcolor="#FFFFFF">
									<TD align="center" ><s:property value="name"/></TD>
									<s:if test="#wanConfigList.connType=='N/A' || #wanConfigList.connType=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property value="connType"/></TD>
							        </s:else>

									<s:if test="#wanConfigList.vlanId=='N/A' || #wanConfigList.vlanId=='null' ">
							           	<TD align="center" id="pvc">PVC:<s:property value="vpi"/>/<s:property value="vci"/></TD>

							            <s:if test="#wanConfigList.username=='N/A' || #wanConfigList.username=='null' ">
											<TD align="center">-</TD>
										</s:if>
										 <s:else>
								            <TD align="center"><s:property value="username"/></TD>
								        </s:else>
							        </s:if>
							        <s:else>
							            <TD align="center">VLANID:<s:property value="vlanId"/></TD>
							            <s:if test="#wanConfigList.ip=='N/A' || #wanConfigList.ip=='null' ">
											<TD align="center">-</TD>
										</s:if>
										 <s:else>
								            <TD align="center"><s:property value="ip"/></TD>
								        </s:else>
							        </s:else>

									<s:if test="#wanConfigList.connStatus=='N/A' || #wanConfigList.connStatus=='null' ">
										<TD align="center">-</TD>
									</s:if>
									 <s:else>
							            <TD align="center"><s:property value="connStatus"/></TD>
							        </s:else>

							        <s:if test="#wanConfigList.bindPort=='N/A' || #wanConfigList.bindPort=='null' ">
										<TD align="center">-</TD>
									</s:if>
									 <s:else>
							            <TD align="center"><s:property value="bindPort"/></TD>
							        </s:else>

							        <s:if test="#wanConfigList.servList=='N/A' || #wanConfigList.servList=='null' ">
										<TD align="center">-</TD>
									</s:if>
									 <s:else>
							            <TD align="center"><s:property value="servList"/></TD>
							        </s:else>

									<TD class=column5 align="center">
										<!--
											<IMG SRC="<s:url value='/images/del.gif'/>" BORDER="0" onclick="" ALT="删除" style="cursor:hand"/>
											<s:if test="#wanConfigList.connMedia=='air’'">
								            	<IMG SRC="<s:url value='/images/edit.gif'/>" BORDER="0"
								            		onclick="editWanHtml('<s:property value='deviceId'/>',
			            							  '<s:property value='wanId'/>',
			            							  '<s:property value='wanConnId'/>',
			            							  '<s:property value='wanConnSessId'/>',
			            							  '<s:property value='gatherTime'/>',
			            							  '<s:property value='sessType'/>',
			            							  '<s:property value='enable'/>',
			            							  '<s:property value='name'/>',
			            							  '<s:property value='connType'/>',
			            							  '<s:property value='servList'/>',
			            							  '<s:property value='bindPort'/>',
			            							  '<s:property value='username'/>',
			            							  '<s:property value='password'/>',
			            							  '<s:property value='ipType'/>',
			            							  '<s:property value='ip'/>',
			            							  '<s:property value='mask'/>',
			            							  '<s:property value='gateway'/>',
			            							  '<s:property value='dnsEnab'/>',
			            							  '<s:property value='dns'/>',
			            							  '<s:property value='status'/>',
			            							  '<s:property value='connError'/>',
			            							  '<s:property value='natEnable'/>',
			            							  '<s:property value='pvc'/>',
			            							  '<s:property value='pppAuthProtocol'/>',
			            							  '<s:property value='dialNum'/>',
			            							  '<s:property value='workMode'/>',
			            							  '<s:property value='loadPercent'/>',
			            							  '<s:property value='backupItfs'/>',
			            							  '<s:property value='connTrigger'/>'
													)" ALT="编辑" style="cursor:hand">
								            	</IMG>
										 	</s:if>
									 	 -->
										<IMG SRC="<s:url value='/images/view.gif'/>" BORDER="0"
											onclick="showWanDetail('<s:property value='deviceId'/>',
	            							  '<s:property value='wanId'/>',
	            							  '<s:property value='wanConnId'/>',
	            							  '<s:property value='wanConnSessId'/>',
	            							  '<s:property value='gatherTime'/>',
	            							  '<s:property value='sessType'/>',
	            							  '<s:property value='enable'/>',
	            							  '<s:property value='name'/>',
	            							  '<s:property value='connType'/>',
	            							  '<s:property value='servList'/>',
	            							  '<s:property value='bindPort'/>',
	            							  '<s:property value='username'/>',
	            							  '<s:property value='password'/>',
	            							  '<s:property value='ipType'/>',
	            							  '<s:property value='ip'/>',
	            							  '<s:property value='mask'/>',
	            							  '<s:property value='gateway'/>',
	            							  '<s:property value='dnsEnab'/>',
	            							  '<s:property value='dns'/>',
	            							  '<s:property value='status'/>',
	            							  '<s:property value='connError'/>',
	            							  '<s:property value='natEnable'/>',
	            							  '<s:property value='pvc'/>',
	            							  '<s:property value='pppAuthProtocol'/>',
	            							  '<s:property value='dialNum'/>',
	            							  '<s:property value='workMode'/>',
	            							  '<s:property value='loadPercent'/>',
	            							  '<s:property value='backupItfs'/>',
	            							  '<s:property value='connTrigger'/>'
											)" ALT="查看" style="cursor:hand">
										</IMG>
									</td>
								</tr>
							</s:iterator>
							<tr align="center" bgcolor="#FFFFFF">
								<TD colspan="11" align="center"><s:property value="corbaMsg"/></TD>
							</tr>
							<!-- 作为编辑时选备用连接选项用 -->
							<tr STYLE="display:none">
								<TD id="tdBackupItfs" >
									<s:iterator var = "wanConfigList" value="wanConfigList">
										<option value="<s:property value="wanId"/>-
														<s:property value="wanConnId"/>-
														<s:property value="wanConnSessId"/>">
											<s:property value="name"/>
										</option>
									</s:iterator>
								</TD>
							</tr>
						</TABLE>
					</td>
				</tr>
			</table>
		</TD>
	</tr>

	<tr height="20">
		<td colspan="4"  width="15" class=column></td>
	</tr>

	<tr align="left" id="wanConnEdit" STYLE="display:none">
		<td colspan="4"  bgcolor=#999999>
			<input type="hidden" name="wanId"/>
			<input type="hidden" name="wanConnId"/>
			<input type="hidden" name="wanConnSessId"/>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<tr align="center">
					<td colspan="4" width="100%" class="green_title" id="wanEditTitle"></td>
				</tr>
				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">服务类型:</TD>
					<TD width="35%" >
						<SELECT class="bk" name="servList" >
							<OPTION value="INTERNET">INTERNET</OPTION>
							<OPTION value="Management_Internet">Management_Internet</OPTION>
							<OPTION value="Management">Management</OPTION>
							<OPTION value="Other">Other</OPTION>
						</SELECT>
					</TD>

					<TD width="15%" class=column align="right">拨号方式:</TD>
					<TD width="35%">
						<SELECT class="bk" name="connTrigger" >
							<OPTION value="OnDemand">OnDemand</OPTION>
							<OPTION value="AlwaysOn">AlwaysOn</OPTION>
							<OPTION value="Manual">Manual</OPTION>
						</SELECT>
					</td>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">工作模式:</TD>
					<TD width="35%" >
						<SELECT name="workMode" class="bk" onclick="chgWorkMode(this.value)">
							<OPTION value="Standby">Standby</OPTION>
							<OPTION value="Main">Main</OPTION>
							<OPTION value="Balance">Balance</OPTION>
						</SELECT>
					</TD>

					<TD width="15%" class=column align="right" id="tdWorkModeTitle"></TD>
					<TD width="35%" id="tdWorkModeText"></td>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD colspan="4" class=column align="right" nowrap >
						<button type="button" name="subBtn" value="" />
					</TD>
				</TR>

				<TR id="resultTr" bgcolor="#FFFFFF" style="display:none">
					<TD class=column align="right" width="15%">执行结果:</TD>
					<TD colspan="3">
						<DIV id="result"></DIV>
					</TD>
				</TR>
			</TABLE>
		</td>
	</tr>

	<tr align="left" id="wanConnInfo" STYLE="display:none">
		<td colspan="4"  bgcolor=#999999>

			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >

				<tr align="center">
					<td colspan="4" width="100%" class="green_title" id="wanDetailTitle">详细信息</td>
				</tr>
				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">连接名称:</TD>
					<TD width="35%" id="deteilname"></TD>

					<TD width="15%" class=column align="right">连接类型:</TD>
					<TD width="35%" id="deteilconnType"></td>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">session类型:</TD>
					<TD width="35%" id="deteilsessType"></TD>

					<TD width="15%" class=column align="right">服务类型:</TD>
					<TD width="35%" id="deteilservList"></td>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">绑定端口:</TD>
					<TD width="35%" id="deteilbindPort"></TD>

					<TD width="15%" class=column align="right">连接状态:</TD>
					<TD width="35%" id="deteilstatus"></td>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">线路工作模式:</TD>
					<TD width="35%" id="deteilworkMode"></TD>

					<TD width="15%" class=column align="right" id="deteilworkModecode">负荷百分比:</TD>
					<TD width="35%" id="deteilworkModevalue"></td>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class=column align="right">拨号方式:</TD>
					<TD width="35%" id="deteilconnTrigger"></td>
					<TD width="15%" class=column align="right"></TD>
					<TD width="35%" id="deteilconnMedia"></TD>
				</TR>

			</TABLE>
		</td>
	</tr>
</TABLE>
</FORM>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置机顶盒信息查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	/** add by chenjie 2011.4.22 **/
	function block() {
		$.blockUI({
			overlayCSS : {
				backgroundColor : '#CCCCCC',
				opacity : 0.6
			},
			message : "<font size=3>正在操作，请稍后...</font>"
		});
	}

	function unblock() {
		$.unblockUI();
	}
	/*------------------------------------------------------------------------------
	 //函数名:		初始化函数（ready）
	 //参数  :	无
	 //功能  :	初始化界面（DOM初始化之后）
	 //返回值:		
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	$(function() {
		<%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		$("TR[@id='biaotou1']").css("display","none");
		<%}%>
		var queryType = "<s:property value="queryType"/>";
		var queryResultType = "<s:property value="queryResultType"/>";
		if ("" != queryType && null != queryType) {
			$("input[@name='queryType']").val(queryType);
		}
		if ("" != queryResultType && null != queryResultType) {
			$("input[@name='queryResultType']").val(queryResultType);
		}
		$("input[@name='startLastTime']").val("");
		$("input[@name='endLastTime']").val("");
	});

	/*------------------------------------------------------------------------------
	 //函数名:		checkip
	 //参数  :	str 待检查的字符串
	 //功能  :	根据传入的参数判断是否为合法的IP地址
	 //返回值:		true false
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function checkip(str) {
		var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
		return pattern.test(str);
	}

	/*------------------------------------------------------------------------------
	 //函数名:		trim
	 //参数  :	str 待检查的字符串
	 //功能  :	根据传入的参数进行去掉左右的空格
	 //返回值:		trim（str）
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function trim(str) {
		if(str){
			return str.replace(/(^\s*)|(\s*$)/g, ""); 
		}else{
			return "";
		}
	}

	/*------------------------------------------------------------------------------
	 //函数名:		queryChange
	 //参数  :	change 1:简单查询、2:高级查询
	 //功能  :	根据传入的参数调整显示的界面
	 //返回值:		调整界面
	 //说明  :	
	 //描述  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function queryDevice() 
	{
		 if('hn_lt'!=trim($("input[@name='instArea']").val()))
		 {
			var username = trim($("input[@name='username']").val());
			var servAccount = trim($("input[@name='servAccount']").val());
			var deviceSerialnumber = trim($("input[@name='deviceSerialnumber']")
					.val());
			var loopbackIp = trim($("input[@name='loopbackIp']").val());
			if ("" != username) {
				$("input[@name='username']").val(username);
			}
			if ("" != deviceSerialnumber && deviceSerialnumber.length < 6) {
				alert("请至少输入最后6位！");
				return false;
			} else {
				$("input[@name='deviceSerialnumber']").val(deviceSerialnumber);
			}
			if ("" != loopbackIp && !checkip(loopbackIp)) {
				alert("请输入正确的IP地址！");
				return false;
			} else {
				$("input[@name='loopbackIp']").val(loopbackIp);
			}
		 }
		 if('jx_dx'!=trim($("input[@name='instArea']").val()))
		 {
			 var loopbackIpSix = trim($("input[@name='loopbackIpSix']").val());
			 if ("" != loopbackIpSix) {
			 	$("input[@name='loopbackIpSix']").val(loopbackIpSix);
			 }
		 }
		document.selectForm.submit();
	}

	function reset() {
		document.selectForm.username.value = "";
		document.selectForm.deviceSerialnumber.value = "";
		document.selectForm.servAccount.value = "";
		document.selectForm.cityId.value = "00";
		document.selectForm.loopbackIp.value = "";
		document.selectForm.addressingType.value = "-1";
		document.selectForm.startLastTime.value = "";
		document.selectForm.endLastTime.value = "";
		document.selectForm.status.value = "-1";
		document.selectForm.cpeMac.value = "";
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ];

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block";
			}
		}
	}
	/* $(function() {
		dyniframesize();
	}); */

	/* $(window).resize(function() {
		dyniframesize();
	}); */
</SCRIPT>
</head>
<body>
	<form name="selectForm" method="post"
		action="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action"/>"
		target="dataForm">
		<input type="hidden" name="queryType" value="2" />
		<input type="hidden" name="queryResultType" value="none" />
		<input type="hidden" name="type" value="<s:property value='type'/>" />
		<input type="hidden" name="instArea" value="<s:property value='instArea'/>" />
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24> <s:if test="type==0">
            	您当前的位置：机顶盒查询统统计
            </s:if> <s:else>
            	您当前的位置：机顶盒查询统统计(带删除)
            </s:else></TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">零配置机顶盒信息查询</th>
			</tr>
			<s:if test="instArea=='hn_lt'">
			<TR id="tr21" STYLE="display:">
				<TD width="10%" class="title_2">业务账号</TD>
				<TD width="40%">
					<input type="text" name="servAccount" value=""
						size="20" maxlength="40" class="bk" />
				</TD>
				<TD width="10%" class="title_2">设备序列号</TD>
				<TD width="40%">
					<input type="text" name="deviceSerialnumber"
						value="" size="20" maxlength="40" class="bk" />
				</TD>
			</TR>
			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">设备MAC地址</TD>
				<TD width="40%">
					<input type="text" name="cpeMac" value=""
						size="23" maxlength="23" class="bk" />
				</TD>
				<TD width="10%" class="title_2">设备属地</TD>
				<TD width="40%">
					<select name="cityId">
						<option value="">==请选择==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
					</select>
				</TD>
			</TR>
			<TR id="tr23" STYLE="display:">
				<TD width="10%" class="title_2">首次上报时间</TD>
				<TD width="40%"><input type="text" name="startLastTime"
					readonly class=bk value="<s:property value="startLastTime" />">
					<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.startLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="13" height="12" border="0"
					alt="选择">&nbsp;~&nbsp; <input type="text" name="endLastTime"
					readonly class=bk value="<s:property value="endLastTime" />">
					<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.endLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="13" height="12" border="0"
					alt="选择"></TD>
				<TD width="10%" class="title_2">最近一次上报IP</TD>
				<TD width="40%"><input type="text" name="loopbackIp" value=""
					size="20" maxlength="40" class="bk" /></TD>
			</TR>
			</s:if>
			<s:else>
				<s:if test="instArea=='sx_lt'">
					<TR id="tr21" STYLE="display:">
						<TD width="10%" class="title_2">设备序列号</TD>
						<TD width="40%">
							<input type="text" name="deviceSerialnumber" 
							value="" size="20" maxlength="40" class="bk" />
						</TD>
						<TD width="10%" class="title_2">设备状态</TD>
						<TD width="40%">
							<s:select list="statusList" name="status"
								headerKey="-1" headerValue="===请选择===" listKey="status_id"
								listValue="status_name" value="status_id" cssClass="bk"
								theme="simple">
							</s:select>
						</TD>
					</TR>
				</s:if>
				<s:else>
					<TR id="tr21" STYLE="display:">
						<TD width="10%" class="title_2">宽带账号</TD>
						<TD width="40%"><input type="text" name="username" value=""
							size="20" maxlength="20" class="bk" /></TD>
						<TD width="10%" class="title_2">设备序列号</TD>
						<TD width="40%"><input type="text" name="deviceSerialnumber"
							value="" size="20" maxlength="40" class="bk" /></TD>
					</TR>
				</s:else>
			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">业务账号</TD>
				<TD width="40%"><input type="text" name="servAccount" value=""
					size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">设备属地</TD>
				<TD width="40%"><select name="cityId" class="bk">
						<option value="">===请选择===</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
				</select></TD>
			</TR>

			<TR id="tr22" STYLE="display:">
				<s:if test="instArea=='jx_dx'">
				<TD width="10%" class="title_2">最近一次上报IP(IPV4地址)</TD>
				</s:if>
				<s:else>
				<TD width="10%" class="title_2">最近一次上报IP</TD>
				</s:else>
				<TD width="40%"><input type="text" name="loopbackIp" value=""
					size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">接入类型</TD>
				<TD width="40%"><select name="addressingType"
					id="addressingType" class="bk">
						<option value="-1">===请选择===</option>
						<option value="PPPoE">PPPoE</option>
						<option value="DHCP">DHCP</option>
				</select></TD>
			</TR>

			<s:if test="instArea=='sx_lt'">
				<TR id="tr23" STYLE="display:">
					<TD width="10%" class="title_2">
						首次上报开始时间
					</TD>
					<TD width="40%">
						<lk:date id="startTime" name="startLastTime" defaultDate="%{startLastTime}" />
					</TD>
					<TD width="10%" class="title_2">
						首次上报结束时间
					</TD>
					<TD width="40%">
						<lk:date id="endTime" name="endLastTime" defaultDate="%{endLastTime}"/>
					</TD>
				</TR>
				<tr>
					<TD width="10%" class="title_2">设备MAC地址</TD>
					<TD width="40%"><input type="text" name="cpeMac" value="" 
						size="23" maxlength="23" class="bk" /></TD>
					<td></td>
					<td></td>
				</tr>
			</s:if>
			<s:else>
				<TR id="tr23" STYLE="display:">
					<TD width="10%" class="title_2">首次上报时间</TD>
					<TD width="40%"><input type="text" name="startLastTime"
						readonly class=bk value="<s:property value="startLastTime" />">
						<img name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.startLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="13" height="12" border="0"
						alt="选择">&nbsp;~&nbsp; <input type="text" name="endLastTime"
						readonly class=bk value="<s:property value="endLastTime" />">
						<img name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.endLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="13" height="12" border="0"
						alt="选择"></TD>
					<TD width="10%" class="title_2">设备MAC地址</TD>
					<TD width="40%"><input type="text" name="cpeMac" value="" 
						size="23" maxlength="23" class="bk" /></TD>
				</TR>
			</s:else>
			
			<TR id="biaotou1"  STYLE="display:">
				<s:if test="instArea=='jx_dx'">
					<TD width="10%" class="title_2">最近一次上报IP(IPV6地址)</TD>
					<TD width="40%"><input type="text" name="loopbackIpSix" value=""
					size="20" maxlength="40" class="bk" /></TD>
				</s:if>
				<s:else>
					<s:if test="instArea!='sx_lt'">
						<TD width="10%" class="title_2">设备状态</TD>
						<TD width="40%">
							<s:select list="statusList" name="status"
								headerKey="-1" headerValue="===请选择===" listKey="status_id"
								listValue="status_name" value="status_id" cssClass="bk"
								theme="simple">
							</s:select>
						</TD>
					</s:if>
				</s:else>
				<s:if test="instArea!='sx_lt'">
					<TD width="10%" class="title_2">失败原因</TD>
					<TD width="40%">
						<select name="failReason">
							<option value="-1">===请选择===</option>
							<s:iterator value="failReasonList">
								<option value="<s:property value="reason_id"/>">
								<s:property value="reason_desc" />
							</option>
							</s:iterator>
						</select>
					</TD>
				</s:if>
			</TR>
			 <%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">终端状态</TD>
				<TD width="40%">
				<select name="device_status"
					id="device_status" class="bk">
						<option value="-1">全部</option>
						<option value="1">已确认</option>
						<option value="0">未确认</option>
				</select></TD>
				<TD width="10%" class="title_2"></TD>
				<TD width="40%"></TD>
			</TR>
			 <%} %> 
			</s:else>
			
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<!-- <input type="button" onclick="javascript:queryDevice()" align="right" class=jianbian
							name="queryButton" value=" 查 询 " /> -->
						<button onclick="javascript:queryDevice()" align="right" class=jianbian> 查 询 </button>
					</div>
				</td>
			</tr>
		</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<style>
	.querytable input[type=text],.querytable select{ width: 160px; }
	.querytable #startTime,.querytable #endTime{ background-color: #f1f1f1; }
</style>
<br>
<br>
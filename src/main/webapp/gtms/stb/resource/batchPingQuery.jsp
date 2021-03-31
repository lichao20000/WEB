<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>网关检测</title>
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


	/**
	* 查询数据
	*/
	function queryData() {
		var deviceIp = $.trim($("#deviceIp").val());
		if ("" != deviceIp && !checkip(deviceIp)) {
			alert("请输入正确的IP地址！");
			return false;
		}
		$("#selectForm").attr("action", "<s:url value='/gtms/stb/resource/batchPingAction!queryDataList.action'/>");
		$("#selectForm").submit();
	}
	
	/**
	* 执行ping命令
	*/
	function pingCmd(){
		if(!confirm("执行过程可能要几分钟，确认执行脚本？")){
			return false;
		}
		block();
		var url = '<s:url value='/gtms/stb/resource/batchPingAction!pingCmd.action'/>';
		$.post(url, {}, function(ajax){
		    alert(ajax);
		    unblock();
		    //location.reload();
		});
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
	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
</head>
<body>
	<form name="selectForm" id="selectForm" method="post" action="" target="dataForm">
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24>您当前的位置：网关检测
            </TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">网关检测</th>
			</tr>

			<TR>
				<TD width="10%" class="title_2">设备IP</TD>
				<TD width="40%"><input type="text" name="deviceIp" id="deviceIp" value="" size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">设备属地</TD>
				<TD width="40%"><select name="cityId" style="width:105px;">
						<option value="">==请选择==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
				</select></TD>
			</TR>
			<TR bgcolor=#ffffff>
				<td class="column" width='15%' align="right">
					开始时间
				</td>
				<td width='35%' align="left">
					<input type="text" name="starttime" class='bk' readonly
						value="<s:property value='starttime'/>">
					<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择">
				</td>
				<td class="column" width='15%' align="right">
					结束时间
				</td>
				<td width='35%' align="left">
					<input type="text" name="endtime" class='bk' readonly
						value="<s:property value='endtime'/>">
					<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择">
				</td>
			</TR>
			<TR>
				<TD width="10%" class="title_2">结果</TD>
				<TD width="40%">
					<select name="result" style="width:105px;">
						<option value="">全部</option>
						<option value="0">失败</option>
						<option value="1">成功</option>
					</select>
				</TD>
				<TD width="10%" class="title_2"></TD>
				<TD width="40%"></TD>
			</TR>

			
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<input type="button" onclick="javascript:queryData()" align="right" class=jianbian
							value=" 查 询 " />
						<input type="button" onclick="javascript:pingCmd()" align="right" class=jianbian
							value=" 执行脚本 " />
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
<br>
<br>
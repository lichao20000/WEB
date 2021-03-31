<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音注册查询</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		document.selectForm.submit();
	}
	
	function change_select(){
		$("select[@name='modelId']").html("");
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==请先选择厂商==</option>");
			$("select[@name='modelId']").attr('disabled',true);
			return;
		}	
		$("select[@name='modelId']").attr('disabled',false);
		$.post(url,{
			gwShare_vendorId:vendorId
		},function(ajax){
			parseMessage(ajax,$("select[@name='modelId']"));
		});
	
}
	
	//解析查询设备型号返回值的方法
	function parseMessage(ajax,field){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			try{
				field.append(option);
			}catch(e){
				alert("设备型号检索失败！");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>
<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/VoiceRegisterQuery!VoiceRegisterQueryInfo.action'/>"  target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							语音注册查询</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">语音注册查询 </th>
						</tr>
	
						<TR>
							<TD class=column width="15%" align='right'>时间</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
							
							<TD class=column width="15%" align='right'>设备序列号</TD>
							<TD width="35%"><input type="text" name="device_sn" id="device_sn" class=bk value=""></TD>
						</TR>
						
						<TR>

							<TD class=column width="15%" align='right'>厂商</TD>
							<TD width="35%"><s:select list="vendorMap" name="vendorId" onchange="change_select()" headerKey="-1" headerValue="==请选择厂商=="
									 listKey="key" listValue="value" cssClass="bk"></s:select>&nbsp;&nbsp;<font color="red">*</font> </TD>
									 
							<TD class="column" width='15%' align="right">设备型号</td>
							<td width='35%' align="left"><select name="modelId" class="bk" disabled="disabled" >
								<option value="-1" selected="selected">==请选择设备型号==</option>
								</select></TD>
						</TR>
						
						<TR>
							<TD  class=column width="15%" align='right'>LOID</TD>
							<TD><input type="text" name="loid" id="loid" class=bk value=""></TD>
							<TD  class=column width="15%" align='right'>终端型号</TD>
							<TD><select name="device_type" class="bk">
									<option value="-1" selected="selected">==请选择==</option>
									<option value="e8-b" >==e8-b==</option>
									<option value="e8-c" >==e8-c==</option>
								</select> </TD>
						</TR>
						
						<TR>
							<TD class=column width="15%" align='right'>语音端口是否启用</TD>
							<TD>
								<select name="enabled" class="bk">
										<option value="-1" selected="selected">==请选择==</option>
										<option value="Enabled" >==e8-b==</option>
										<option value="Disabled" >==e8-c==</option>
								</select>
							</TD>
							<TD class=column width="15%" align='right'>语音端口号码</TD>
							<TD><input type="text" name="voip_phone" id="voip_phone" class=bk value=""></TD>
						</TR>
						
						<TR>
							<TD class=column width="15%" align='right'>语音注册成功状态</TD>
							<TD>
								<select name="status" class="bk">
										<option value="-1" selected="selected">==请选择==</option>
										<option value="up" >==up==</option>
										<option value="Disabled" >==Disabled==</option>
								</select>
							</TD>
							<TD class=column width="15%" align='right'>语音注册失败原因</TD>
							<TD><select name="reason" class="bk">
										<option value="-1" selected="selected">==请选择==</option>
										<option value="0" >==成功==</option>
										<option value="1" >==IAD模块错误==</option>
										<option value="2" >==访问路由不通==</option>
										<option value="3" >==访问服务器无响应==</option>
										<option value="4" >==帐号、密码错误==</option>
										<option value="5" >==未知错误==</option>
								</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button  onclick="query()" id="button" name="button" >&nbsp;查&nbsp;询&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
				<tr>
				<td><iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
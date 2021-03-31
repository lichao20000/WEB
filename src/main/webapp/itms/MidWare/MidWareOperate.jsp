<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">

function add(){
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $("input[@name='status'][@checked]").val();
	if(oui==""){
		alert("请输入厂商类型！");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("请输入设备序列号！");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	if(deviceModel==""){
		alert("请输入设备类型！");
		$("input[@name='deviceModel']").focus();
		return false;
	}
	if(adNumber==""){
		alert("请输入AD编号！");
		$("input[@name='adNumber']").focus();
		return false;
	}
	if(status==""||status=="-1"){
		alert("请选择状态！");
		$("input[@name='status']").focus();
		return false;
	}
	$("input[@name='des']").val("add device");
	var url = '<s:url value='/itms/midware/midWare!add.action'/>'; 
	operate(url);
}

function update(){
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $("input[@name='status'][@checked]").val();
	if(oui==""){
		alert("请输入厂商类型！");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("请输入设备序列号！");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	if(deviceModel==""){
		alert("请输入设备类型！");
		$("input[@name='deviceModel']").focus();
		return false;
	}
	if(adNumber==""){
		alert("请输入AD编号！");
		$("input[@name='adNumber']").focus();
		return false;
	}
	if(status==""||status=="-1"){
		alert("请选择状态！");
		$("input[@name='status']").focus();
		return false;
	}
	$("input[@name='des']").val("change device info");
	var url = '<s:url value='/itms/midware/midWare!update.action'/>'; 
	operate(url);
}

function del(){
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());

	if(oui==""){
		alert("请输入厂商类型！");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("请输入设备序列号！");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	
	$("input[@name='des']").val("delete device");
	var url = '<s:url value='/itms/midware/midWare!delete.action'/>'; 
	operate(url);
}

//中间件操作  
function operate(url){
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $.trim($("input[@name='status'][@checked]").val());
	var des = $.trim($("input[@name='des']").val());
	var area = $.trim($("input[@name='area']").val());
	var group = $.trim($("input[@name='group']").val());
	var phone = $.trim($("input[@name='phone']").val());
	$.post(url,{
		deviceId:deviceId,
		oui:oui,
		deviceSn:deviceSn,
		deviceModel:deviceModel,
		adNumber:adNumber,
		status:status,
		des:des,
		area:area,
		group:group,
		phone:phone
	},function(ajax){	
	    alert(ajax);
	});
}




</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						中间件设备操作
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
			<input type="hidden" name="des" value="">
			<input type="hidden" name="deviceId" value="">
				<table class="querytable">
					<tr>
						<th colspan=4>
							中间件设备操作
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							厂商类型
						</td>
						<td>
							<input type="text" name="oui" value="">
							<font color="red">&nbsp;*</font>
						</td>
						<td class=column align=center width="15%">
							设备序列号
						</td>
						<td>
							<input type="text" name="deviceSn" value="">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							设备类型
						</td>
						<td>
							<input type="text" name="deviceModel" value="">
							<font color="red">&nbsp;*</font>
						</td>
						<td class=column align=center width="15%">
							AD编号
						</td>
						<td>
							<input type="text" name="adNumber" value="">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							状态
						</td>
						<td>
							&nbsp;&nbsp;
							<input type="radio" name="status" value="1" checked>
							&nbsp;&nbsp;可用&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="status" value="0">
							&nbsp;&nbsp;不可用 &nbsp;&nbsp;
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							设备地区属性
						</td>
						<td>
							<input type="text" name="area" value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							设备组属性
						</td>
						<td>
							<input type="text" name="group" value="">
						</td>
						<td class=column align=center width="15%">
							电话号码
						</td>
						<td>
							<input type="text" name="phone" value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="add()">
								&nbsp;新 增&nbsp;
							</button>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="update()">
								&nbsp;更 新&nbsp;
							</button>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="del()">
								&nbsp;删 除&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

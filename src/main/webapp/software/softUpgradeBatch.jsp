<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>软件升级配置页面</title>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	//检查是否选择设备，版本文件，执行方式
function checkForm(){
	var oselect = document.all("device_id");
	var maxDevNum = $("input[@name='maxDevNum']").val();
	if(oselect == null){
		alert("请选择设备！");
		return false;
	}
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			//device_id = oselect.value;
			num = 1;
		}
	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
				//device_id = oselect[i].value;
				num++;
			}
		}
	}
	if(num ==0){
		alert("请选择设备！");
		return false;
	}else if(num > maxDevNum){
		alert("选择设备数不能大于 " + maxDevNum)
		return false;
	}
	if($("select[@name='selectExecuteType']").val() == "-1"){
		alert("请选择策略执行方式");
		$("select[@name='selectExecuteType']").focus();
		return false;
	}
	if($("select[@name='selectSoftfile']").val() == "-1"){
		alert("请选择软件版本文件");
		$("select[@name='selectSoftfile']").focus();
		return false;
	}
	document.all("tijiao").disabled = true;
	return true;
}

//选择版本关联出设备
//厂商、型号级联
function changeSelect(type){
	
	var city_id = $("select[@name='selectCity']").val();
	var vendor_id = $("select[@name='selectVendor']").val();
	var device_model_id = $("select[@name='selectDevModel']").val();
	var devicetype_id = $("select[@name='selectDevType']").val();
	
	//根据厂商查询设备型号，设备版本重置
	if("vendor"==type){
		var url = '<s:url value="/Resource/upgradeBatch!getDevModelSelectList.action"/>';
		$.post(url,{
			selectVendor:vendor_id
		},function(ajax){
			parseMessageModel(ajax);
		});
	}
	//根据型号查询设备版本
	if("model"==type){
		var url = '<s:url value="/Resource/upgradeBatch!getDevTypeSelectList.action"/>';
		$.post(url,{
			selectVendor:vendor_id,
			selectDevModel:device_model_id
		},function(ajax){
			parseMessageType(ajax);
		});
		
	}
	if("devicetype"==type){
		var url = '<s:url value="/Resource/upgradeBatch!getDeviceCheckboxList.action"/>';
		$.post(url,{
			selectCity:city_id,
			selectVendor:vendor_id,
			selectDevModel:device_model_id,
			selectDevType:devicetype_id
		},function(ajax){
			parseMessageDevice(ajax);
		});
		
	}
}

//解析查询设备型号
function parseMessageModel(ajax){
	var devModelObj = $("select[@name='selectDevModel']");
	var str = '';
	devModelObj.html("");
	str = "<option value=\"-1\" selected>==请选择型号==</option>";
	str += ajax;
	devModelObj.append(str);
}

//解析查询设备版本
function parseMessageType(ajax){
	var devTypeObj = $("select[@name='selectDevType']");
	var str = '';
	devTypeObj.html("");
	str = "<option value=\"-1\" selected>==请选择版本==</option>";
	str += ajax;
	devTypeObj.append(str);
}

//解析查询设备版本
function parseMessageDevice(ajax){
	var devListObj = $("div[@id='deviceList']");
	devListObj.html(ajax);
}

//全选按钮,最多只选maxDevNum个
function selectLimitAll(elmID){
	var maxDevNum = $("input[@name='maxDevNum']").val();
	var n = 0;
	var t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length && n < maxDevNum; i++){
					t_obj[i].checked = true;
					n++;
				}
			} else {
				t_obj.checked = true;
			}
		}
	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
			} else {
				t_obj.checked = false;
			}
		}
	}
}
//-->
</SCRIPT>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post"
			ACTION="<s:url value='/Resource/upgradeBatch!strategyExecute.action'/>"
			onSubmit="return checkForm();">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="text">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite" nowrap>
						批量软件升级</td>
						<td nowrap>&nbsp;<img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;建议：设备数不超过100台，策略方式为终端启动.带<font color="red">*</font>的必须选择或输入.
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
						<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4">第一步：设备查询</TH>
							</TR>
							<!-- 选择设备部分 -->
							<TR bgcolor="#FFFFFF">
								<TD align="right" class='column'>属地</TD>
								<TD><select name="selectCity" class="bk">
									<option value="-1">==请选择==</option>
									<s:iterator value="cityList">
										<option value="<s:property value="city_id"/>">==<s:property
											value="city_name" />==</option>
									</s:iterator>
								</select></TD>
								<TD align="right" class='column'><font color="red">*</font>&nbsp;设备厂商</TD>
								<TD><select name="selectVendor"
									onchange="changeSelect('vendor')" class="bk">
									<option value="-1">==请选择==</option>
									<s:iterator value="vendorList">
										<option value="<s:property value="vendor_id"/>">==<s:property
											value="vendor_add" />==</option>
									</s:iterator>
								</select></TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD align="right" class='column'><font color="red">*</font>&nbsp;设备型号</TD>
								<TD><select name="selectDevModel"
									onchange="changeSelect('model')" class="bk">
									<option value="-1">==先选择厂商==</option>
								</select></TD>
								<TD align="right" class='column'><font color="red">*</font>&nbsp;设备版本</TD>
								<TD><select name="selectDevType"
									onchange="changeSelect('devicetype')" class="bk">
									<option value="-1">==先选择型号==</option>
								</select></TD>
							</TR>
							<tr bgcolor="#FFFFFF">
								<td align="right" class='column'><font color="red">*</font>&nbsp;全选<br>
								<input type="checkbox" name="selectAll"
									onclick="selectLimitAll('device_id')" /></td>
								<td colspan="3">
								<div id="deviceList"
									STYLE="width: 500px; height: 150px; overflow: auto;"></div>
								</td>
							</tr>
							<TR>
								<TH colspan="4">第二步：升级配置</TH>
							</TR>
							<!-- 软件升级参数部分 -->
							<TR bgcolor="#FFFFFF">
								<TD align="right" width="15%" class='column'><font color="red">*</font>&nbsp;目标版本</TD>
								<TD width="35%"><select name="selectSoftfile" class="bk">
									<option value="-1">==请选择==</option>
									<s:iterator value="softfileList">
										<option value="<s:property value="softwarefile_name"/>">==<s:property
											value="softwarefile_name" />==</option>
									</s:iterator>
								</select></TD>
								<TD align="right" width="15%" class='column'><font color="red">*</font>&nbsp;策略方式</TD>
								<TD width="35%"><select name="selectExecuteType" class="bk">
									<option value="-1">==请选择==</option>
									<s:iterator value="executeTypeList">
										<option value="<s:property value='type_id'/>">==<s:property
											value="type_name" />==</option>
									</s:iterator>
								</select></TD>
							</TR>
							<TR>
								<TD colspan="4" align="right" CLASS="green_foot">
									<INPUT type="hidden" name="maxDevNum" value="50">
									<INPUT TYPE="submit" name="tijiao" value=" 升 级 " class=btn>&nbsp;&nbsp;
									<INPUT TYPE="reset" value=" 取 消 " class=btn> 
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</td>
			</tr>
		</table>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
	document.frm.selectExecuteType.value=5
//-->
</SCRIPT>

<%@ include file="../foot.jsp"%>
</body>
</html>
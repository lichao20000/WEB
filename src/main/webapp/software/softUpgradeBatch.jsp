<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>�����������ҳ��</title>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	//����Ƿ�ѡ���豸���汾�ļ���ִ�з�ʽ
function checkForm(){
	var oselect = document.all("device_id");
	var maxDevNum = $("input[@name='maxDevNum']").val();
	if(oselect == null){
		alert("��ѡ���豸��");
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
		alert("��ѡ���豸��");
		return false;
	}else if(num > maxDevNum){
		alert("ѡ���豸�����ܴ��� " + maxDevNum)
		return false;
	}
	if($("select[@name='selectExecuteType']").val() == "-1"){
		alert("��ѡ�����ִ�з�ʽ");
		$("select[@name='selectExecuteType']").focus();
		return false;
	}
	if($("select[@name='selectSoftfile']").val() == "-1"){
		alert("��ѡ������汾�ļ�");
		$("select[@name='selectSoftfile']").focus();
		return false;
	}
	document.all("tijiao").disabled = true;
	return true;
}

//ѡ��汾�������豸
//���̡��ͺż���
function changeSelect(type){
	
	var city_id = $("select[@name='selectCity']").val();
	var vendor_id = $("select[@name='selectVendor']").val();
	var device_model_id = $("select[@name='selectDevModel']").val();
	var devicetype_id = $("select[@name='selectDevType']").val();
	
	//���ݳ��̲�ѯ�豸�ͺţ��豸�汾����
	if("vendor"==type){
		var url = '<s:url value="/Resource/upgradeBatch!getDevModelSelectList.action"/>';
		$.post(url,{
			selectVendor:vendor_id
		},function(ajax){
			parseMessageModel(ajax);
		});
	}
	//�����ͺŲ�ѯ�豸�汾
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

//������ѯ�豸�ͺ�
function parseMessageModel(ajax){
	var devModelObj = $("select[@name='selectDevModel']");
	var str = '';
	devModelObj.html("");
	str = "<option value=\"-1\" selected>==��ѡ���ͺ�==</option>";
	str += ajax;
	devModelObj.append(str);
}

//������ѯ�豸�汾
function parseMessageType(ajax){
	var devTypeObj = $("select[@name='selectDevType']");
	var str = '';
	devTypeObj.html("");
	str = "<option value=\"-1\" selected>==��ѡ��汾==</option>";
	str += ajax;
	devTypeObj.append(str);
}

//������ѯ�豸�汾
function parseMessageDevice(ajax){
	var devListObj = $("div[@id='deviceList']");
	devListObj.html(ajax);
}

//ȫѡ��ť,���ֻѡmaxDevNum��
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
						�����������</td>
						<td nowrap>&nbsp;<img src="../images/attention_2.gif" width="15"
							height="12"> &nbsp;���飺�豸��������100̨�����Է�ʽΪ�ն�����.��<font color="red">*</font>�ı���ѡ�������.
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
								<TH colspan="4">��һ�����豸��ѯ</TH>
							</TR>
							<!-- ѡ���豸���� -->
							<TR bgcolor="#FFFFFF">
								<TD align="right" class='column'>����</TD>
								<TD><select name="selectCity" class="bk">
									<option value="-1">==��ѡ��==</option>
									<s:iterator value="cityList">
										<option value="<s:property value="city_id"/>">==<s:property
											value="city_name" />==</option>
									</s:iterator>
								</select></TD>
								<TD align="right" class='column'><font color="red">*</font>&nbsp;�豸����</TD>
								<TD><select name="selectVendor"
									onchange="changeSelect('vendor')" class="bk">
									<option value="-1">==��ѡ��==</option>
									<s:iterator value="vendorList">
										<option value="<s:property value="vendor_id"/>">==<s:property
											value="vendor_add" />==</option>
									</s:iterator>
								</select></TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD align="right" class='column'><font color="red">*</font>&nbsp;�豸�ͺ�</TD>
								<TD><select name="selectDevModel"
									onchange="changeSelect('model')" class="bk">
									<option value="-1">==��ѡ����==</option>
								</select></TD>
								<TD align="right" class='column'><font color="red">*</font>&nbsp;�豸�汾</TD>
								<TD><select name="selectDevType"
									onchange="changeSelect('devicetype')" class="bk">
									<option value="-1">==��ѡ���ͺ�==</option>
								</select></TD>
							</TR>
							<tr bgcolor="#FFFFFF">
								<td align="right" class='column'><font color="red">*</font>&nbsp;ȫѡ<br>
								<input type="checkbox" name="selectAll"
									onclick="selectLimitAll('device_id')" /></td>
								<td colspan="3">
								<div id="deviceList"
									STYLE="width: 500px; height: 150px; overflow: auto;"></div>
								</td>
							</tr>
							<TR>
								<TH colspan="4">�ڶ�������������</TH>
							</TR>
							<!-- ��������������� -->
							<TR bgcolor="#FFFFFF">
								<TD align="right" width="15%" class='column'><font color="red">*</font>&nbsp;Ŀ��汾</TD>
								<TD width="35%"><select name="selectSoftfile" class="bk">
									<option value="-1">==��ѡ��==</option>
									<s:iterator value="softfileList">
										<option value="<s:property value="softwarefile_name"/>">==<s:property
											value="softwarefile_name" />==</option>
									</s:iterator>
								</select></TD>
								<TD align="right" width="15%" class='column'><font color="red">*</font>&nbsp;���Է�ʽ</TD>
								<TD width="35%"><select name="selectExecuteType" class="bk">
									<option value="-1">==��ѡ��==</option>
									<s:iterator value="executeTypeList">
										<option value="<s:property value='type_id'/>">==<s:property
											value="type_name" />==</option>
									</s:iterator>
								</select></TD>
							</TR>
							<TR>
								<TD colspan="4" align="right" CLASS="green_foot">
									<INPUT type="hidden" name="maxDevNum" value="50">
									<INPUT TYPE="submit" name="tijiao" value=" �� �� " class=btn>&nbsp;&nbsp;
									<INPUT TYPE="reset" value=" ȡ �� " class=btn> 
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
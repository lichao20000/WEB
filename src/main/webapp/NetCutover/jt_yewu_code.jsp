<%--
FileName	:
Author		: liuli
Date		: 2007年4月15日
Desc		: 新版本流量.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.FileSevice" />
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%request.setCharacterEncoding("GBK");
            //String drt_name1 = request.getParameter("drt_name1");
			String str_service_id = request.getParameter("lg_id");
			String strSQL = "select distinct devicetype_id,device_model_id from tab_devicetype_info ";
			Cursor cursor = DataSetBean.getCursor(strSQL);
			String strModelList = FormUtil.createListBox(cursor,
					"devicetype_id", "device_model_id", true, "", "");
			String strData = DeviceAct.getCodeHtml(request);
			
			//设备厂商
			String strVendorList = deviceAct.getVendorList(true, "", "");
			
			String cityNone = com.linkage.litms.netcutover.CommonForm.getCityCheckBox("");

			%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '添加';
}

function delWarn(drt_name1){
	if(confirm("真的要删除该业务代码嘛？\n本操作所删除的不能恢复！！！")){
		document.frm.drt_name1.value = drt_name1;
		document.frm.action.value="delete";
		document.frm.submit();
	}
	else{
		return false;
	}
}

function showChild(parname){
	if(parname == "vendor_id")
	{
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;
	}
	else if(parname=='devicetype_id'){
		var obj = event.srcElement;
		var devicetype_id = obj.options[obj.selectedIndex].value;
		
		if(parseInt(devicetype_id) == 0){
			document.all("devicelist").innerHTML="<select name=\"asas\" class=bk><option value=-1>请先选择设备型号</option></select>";
		}else{
			document.all("childFrm").src = "gettemplatelist_from.jsp?devicetype_id="+ devicetype_id;
		}
	}
}

function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.drt_name.value,'业务代码')){
		obj.drt_name.focus();
		obj.drt_name.select();
		return false;
	}
	
	j=0;
	var citys="";
	for (var i=0;i<obj.elements.length;i++)
    {
		var e = obj.elements[i];
		if (e.name == 'city' && e.checked==true)
		{
			citys=citys+";"+obj.elements[i].value;
			j++;
		}
    }
    citys = citys + ";";

	if(citys=="")
	{
		alert("至少要选择一个属地");
		return false;
	}
	
	obj.cityList.value = citys;
	return true;
}

function ChkAllCity()
{
	for (var i=0;i<frm.elements.length;i++)
	{
	var e = frm.elements[i];
	if (e.name!="cityAll")
	   e.checked = frm.cityAll.checked;
	}
}

function editCode(servicecode,template_id,devicetype_id,cityList){
	document.frm.drt_name.value = servicecode;
	document.frm.drt_name.readOnly = true;
	
	page = "initYewuCode.jsp?template_id="+template_id+"&devicetype_id="+devicetype_id+"&cityList="+cityList;
	document.all("childFrm2").src = page;
	
	document.frm.action.value = "update";
}

function newCode(){
	document.frm.drt_name.value = "";
	document.frm.drt_name.readOnly = false;
	document.frm.vendor_id.value = "-1";
	document.all("div_devicetype").innerHTML = "<select name=devicetype_id class='bk'><option value='-1'>--请先选择厂商--</option></select>";
	document.all("devicelist").innerHTML = "<select name='event_name12' class=bk><option value=-1>=请先选择设备型号=</option></select>";
	document.all("cityCheck").innerHTML = "<%=cityNone%>";
	
	document.frm.action.value = "add";
	
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="jt_yewu_code_from_save.jsp"
			onsubmit="return CheckForm()">
			<input type="hidden" name="drt_name1" value="">
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">新增业务代码</div>
				</td>
				<td><img src="../images/attention_2.gif" width="15" height="12">业务代码信息</td>
			</tr>
		</table>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="blue_title">
						<TH width='20%' class=column>业务名称</TH>
						<TH width='20%' class=column>业务代码</TH>
						<TH width='10%' class=column>设备型号</TH>
						<TH width='20%' class=column>模板名称</TH>
						<TH width='20%' class=column>属地</TH>
						<TH width='10%' class=column>操作</TH>
					</TR>
					<%=strData%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="blue_title">
						<TH colspan="4" align="center" > 编辑业务代码信息 <a href="#" onclick="newCode()">新增业务代码</a></TH>
					</TR>
					<TR bgcolor="#999999">
						<TD align="center" bgcolor="#FFFFFF">业务代码</TD>
						<TD colspan=3 bgcolor="#FFFFFF">
						<INPUT TYPE="hidden" NAME="lg_id" value=<%=str_service_id%>> 
						 <INPUT TYPE="text" NAME="drt_name" maxlength=25 size=20 class=bk>
						&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center">厂商:</TD>
						<TD align="left"><div id="div_vendor"><%=strVendorList %></div></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center">设备型号:</TD>
						<TD>
							<div id="div_devicetype">
								<select name=devicetype_id class="bk">
									<option value="-1">--请先选择厂商--</option>
								</select>
							</div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center" width="180">模板名称</TD>
						<td bgcolor="#FFFFFF">
						<span id=devicelist><select name="event_name12" class=bk>
							<option value=-1>=请先选择设备型号=</option>
						</select></span></td>
					</TR>
					<tr bgcolor="#FFFFFF">
                        <td> <div align="center">请选择属地<br><input type=checkbox name=cityAll onclick="javascript:ChkAllCity();">全选</div></td>
                        <td>
                            <div id=cityCheck><%=cityNone%></div>
                            <input type="hidden" name="cityList" value="">
						</td>
                    </tr>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" align="right" CLASS="blue_foot"><INPUT
							TYPE="submit" value=" 保 存 " class=btn>&nbsp; <INPUT TYPE="hidden"
							name="action" value="add">
							 <INPUT TYPE="hidden" name="lg_id" value=<%=str_service_id %>> 
							 <INPUT TYPE="reset" value=" 重 写 "
							class=btn>&nbsp; <input type="button" name="btn1" value=" 返 回 "
							class=btn
							onclick="javascript:history.go(-1);"></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>

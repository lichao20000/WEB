<%--
FileName	:
Author		: liuli
Date		: 2007年4月15日
Desc		: 新版本流量.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,com.linkage.litms.common.util.*"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.FileSevice" />
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("GBK");
	String str_service_id = request.getParameter("lg_id");
	String servicecode = new String(request.getParameter("servicecode").getBytes());
    servicecode = java.net.URLDecoder.decode(servicecode,"GBK");
	
	String service_id = request.getParameter("service_id");
	//String Service_name = request.getParameter("Service_name");
	String template_id = request.getParameter("template_id");
	String devicetype_id = request.getParameter("devicetype_id");
	
	String strSQL2 = "select oui from tab_devicetype_info where devicetype_id="+devicetype_id;
	Cursor cursor2 = DataSetBean.getCursor(strSQL2);
	Map fileds = cursor2.getNext();
	String pos = "";
	if (fileds != null){
		pos = (String)fileds.get("oui");
	}
	
	String strVendorList = deviceAct.getVendorList(true, pos, "");
	
	String strSQL = "select distinct  a.devicetype_id, a.device_model + '(' + a.softwareversion + ')' as device_model,b.devicetype_id  from tab_devicetype_info a,tab_template b where a.devicetype_id=b.devicetype_id";
	if (pos != null && !"".equals(pos)){
		strSQL += " and a.oui = '" + pos + "'";
	}
	Cursor cursor = DataSetBean.getCursor(strSQL);
	String strModelList = FormUtil.createListBox(cursor,
			"devicetype_id", "device_model", true, devicetype_id, "");

	String strSQL1 = "select distinct template_id,template_name from tab_template where devicetype_id="
			+ devicetype_id + "  order by template_id ";
	Cursor cursor1 = DataSetBean.getCursor(strSQL1);
	String strModelList1 = FormUtil.createListBox(cursor1,
			"template_id", "template_name", false, template_id, "");
	
	
	
	String cityList = request.getParameter("cityList");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function showChild(parname){
	if(parname == "vendor_id")
	{
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;
	}
	else if(parname=='devicetype_id'){
		var obj = event.srcElement;
		var pid = obj.options[obj.selectedIndex].value;
		if(parseInt(pid) == 0) 
			document.all("devicelist").innerHTML="<select name=\"asas\" class=bk><option value=-1>请先选择设备型号</option></select>";
		else
			document.all("childFrm").src = "gettemplatelist_from.jsp?devicetype_id="+ pid;
	}
}

function CheckForm(){
	var obj = document.frm;
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

function GoList(page){
	this.location = page;
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
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<link href="../css/css_blue.css" rel="stylesheet" type="text/css">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="jt_yewu_code_from_save.jsp"
			onsubmit="return CheckForm()">
		<table width="100%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">编辑业务代码</div>
				</td>
				<td><img src="../images/attention_2.gif" width="15" height="12">编辑业务代码信息</td>
			</tr>
		</table>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#999999">
						<TD align="center" bgcolor="#FFFFFF">业务代码</TD>
						<TD colspan=3 bgcolor="#FFFFFF"><INPUT TYPE="text"
							NAME="d_name" maxlength=25 size=20 value=<%=servicecode%>
							disabled> <INPUT TYPE="hidden" name="qwe"
							value=<%=servicecode%>> &nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center">厂商</TD>
						<TD colspan=3><%=strVendorList%></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center">设备型号</TD>
						<TD colspan=3><div id="div_devicetype"><%=strModelList%></div></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="center" width="180">模板名称</TD>
						<td bgcolor="#FFFFFF"><span id=devicelist><%=strModelList1%></span></td>
					</TR>
					<tr bgcolor="#FFFFFF">
                        <td> <div align="center">请选择属地<br><input type=checkbox name=cityAll onclick="javascript:ChkAllCity();">全选</div></td>
                        <td>
                            <%=com.linkage.litms.netcutover.CommonForm.getCityCheckBox(cityList)%>
                            <input type="hidden" name="cityList" value="">
						</td>
                    </tr>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4" align="right" CLASS="blue_foot"><INPUT
							TYPE="submit" value=" 保 存 " class=btn>&nbsp; <INPUT
							TYPE="hidden" name="action" value="update"> <INPUT
							TYPE="hidden" name="idf" value=<%=service_id%>> <INPUT
							TYPE="hidden" name="lg_id" value=<%=str_service_id %>></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>

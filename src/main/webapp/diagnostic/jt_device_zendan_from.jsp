<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.FileSevice" />
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%request.setCharacterEncoding("GBK");
			String strTemplateList = DeviceAct.getdeviceTypeList();

			%>

<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
function CheckForm(){	
	var obj = document.frm;
    if(!IsNull(obj.ruler_name.value,'规则名称')){
		obj.ruler_name.focus();
		return false;
	}
	else if(obj.event_oid.value == "-1"){
		alert("请选择事件名称！");
		obj.event_oid.focus();
		return false;
	}
	 else if(obj.level.value=="0"){
	 alert("请选择告警等级！");
     return false;
   }
    else if(obj.resource_type_id.value=="-1"){
	 alert("请选择资源类型！");
	 obj.resource_type_id.focus();
     return false;
   }
   else if(!CheckChkBox())
	{
		alert("请选择采集点！");
		return false;
	}
	else if(!IsNull(obj.ruler_time.value,'作用时间')){
		obj.ruler_time.focus();
		return false;
	} 

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
		<FORM NAME="frm" METHOD="POST" ACTION="jt_sevice_from_save.jsp"
			onSubmit="return CheckForm();">
		<table width="80%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<tr bgcolor="#FFFFFF" class="blue_title">
						<td colspan="6">设备诊断</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>
						<div align="center">设备:</div>
						</td>
						<td colspan="5"><%=strTemplateList%></td>
					</tr>

					<tr bgcolor="#FFFFFF">
						<td nowrap>
						<div align="center">命令:</div>
						</td>
						<td><select name="dengji" class="form_kuang">
							<option value="0">Ping命令</option>
							<option value="1">其它命令</option>
						</select></td>
						<td><INPUT TYPE="submit" value=" 相关参数 " class=btn></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="6" class="blue_foot">
						<div align="right">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr colspan="4" align="right" class="green_foot">
								<td width="18%">
								<div align="right"><INPUT TYPE="submit" value=" 诊 断 " class=btn>&nbsp;&nbsp;
								<INPUT TYPE="hidden" name="action" value="add"> &nbsp;&nbsp;</div>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap colspan="6" class="blue_foot">
						<div align="left">诊断结果:</div>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="6">
						<div align="right">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><textarea name="file_desc" cols="100" rows="10"></textarea>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>

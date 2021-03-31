<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
request.setCharacterEncoding("GBK");

%>

<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>

<SCRIPT LANGUAGE="JavaScript">

var did;

function CheckForm(){

	if(!deviceSelectedCheck()){
		alert("请选择设备！");
		return false;
	}

	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				did = oSelect[i].value;
			}
		}
	}
	if(oSelect.checked){
		did = oSelect.value;
	}
	return true;
}

</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									参数实例管理
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									配置终端时区配置
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							设备查询
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>加载中....</span></div>
						</td>
					</TR>
					<TR>
						<TH colspan="4" align="center">
							时区配置
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													配置方式
												</TD>
												<TD colspan=3 width="85%">
													<input type="radio" name="type" id="rd1" class=btn value="1" checked><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" id="rd2" class=btn value="2"><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="para1" style="display:none">
												<td align="right">时区</td>
												<td colspan=3><input type="text" name="timeZone"></td>
											</TR>
											<TR bgcolor="#FFFFFF" id="para2" style="display:none">
												<td align="right">时区名称</td>
												<td colspan=3><input type="text" name="timeZoneName" readonly="readonly"></td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" style="display:none" onclick="ExecMod();" value=" 设 置 " class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="getStatus();" value=" 获 取 " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
												<TH colspan="4">操作结果</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
												<td colspan="4" valign="top" class=column>
												<div id="div_ping"
													style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
												</td>
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
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function getStatus() {
     		if(CheckForm()){
		        page = "TimeZoneConfigStatus.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "正在获取设备时区信息，请耐心等待....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}
	}

     function ExecMod(){
     		if(CheckForm()){
		        page = "TimeZoneConfigSave.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&timeZone=" + encodeURIComponent($("input[@name='timeZone']").val())+"&timeZoneName="+encodeURIComponent($("input[@name='timeZoneName']").val())
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "正在配置设备时区信息，请耐心等待....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
//-->
</SCRIPT>
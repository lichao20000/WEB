<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>

<jsp:include page="../share/selectDeviceJs.jsp">
        <jsp:param name="selectType" value="checkbox"/>
        <jsp:param name="jsFunctionName" value=""/>
</jsp:include>

<%
request.setCharacterEncoding("GBK");

DateTimeUtil dt=new DateTimeUtil();
//String startime=dt.getDate()+" 00:00:00";
String endtime=dt.getDate();

%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var diviceIds = "";
function CheckForm(){
	
//	if(document.frm.starttime.value == ""){
//		alert("请选择开始时间!");
//		document.frm.startimg.focus();
//		return false;
//	}else if(document.frm.endtime.value==""){

	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("请选择设备！");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			  num++;
			  break;
			}
		}

 	}
 	if(num ==0){
		alert("请选择设备！");
		return false;
	}
	if(document.frm.endtime.value==""){
		alert("请选择结束时间!");
		document.frm.endimg.focus();
		return false;
	}
	return true;
}

function getDeviceIds() {
	diviceIds = "";
	var oselect = document.all("device_id");
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			diviceIds = oselect.value;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
				diviceIds += oselect[i].value + ",";
			}
		}
		diviceIds += 0;
 	}
	return diviceIds;
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
			<FORM NAME="frm">
				<input type="hidden" name="strDevice" value="">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									定制终端使用统计
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									设备在线时长统计。
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													设备在线时长统计
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD colspan="4">
													<div id="selectDevice">
										                <span>加载中....</span>
										        	</div>
												</TD>
											</TR>
											
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													选择时间:
												</TD>
												<TD colspan=3 width="85%">
													<input type="text" name="endtime" value="<%=endtime%>" readonly class=bk>
													<img name="endimg" onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',false,'whyGreen')" src="../images/search.gif" width="15" height="12" border="0" alt="选择">(YYYY-MM-DD)
													<label style="color:  #FF0000">*</label>
												</TD>
												
											</TR>
											<tr bgcolor="#FFFFFF" style="display:;">
												<td align="right" width="15%">选择报表类型:</td>
												<td colspan=3 width="85%">
													<select name="sel_type" onchange="chgDesc()">
														<option value="1">日报表</option>
														<option value="2">周报表</option>
														<option value="3">月报表</option>
													</select>&nbsp;&nbsp;
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" onclick="ExecMod();" value=" 查 询 " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
												<TH colspan="4">查询结果</TH>
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
			<IFRAME ID=childFrm SRC="" STYLE="display:none;"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none;"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none;"></IFRAME>
		</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	 function chgDesc(type) {
	 	switch(type){
			case "1"://日报表
				document.all("dateDesc").innerHTML=" （当前所选日期）";
				break;
			case "2"://周报表
				document.all("dateDesc").innerHTML=" （当前日期所在的周）";
				break;
			case "3"://月报表
				document.all("dateDesc").innerHTML=" （当前日期所在的月）";
				break;
		}
	 }
     function ExecMod(){
     		if(CheckForm()){
				//ChangeTime($("select[@name='sel_type']").val());
			
		        page = "dev_ontime_static_data.jsp?device_id=" + getDeviceIds()
		        	+ "&starttime=" + $("input[@name='endtime']").val() //document.frm.starttime.value
		        	+ "&type=" + document.frm.sel_type.value
		        	//+ "&endtime=" + getCaleToSec(document.frm.endtime.value + " 23:59:59")
		        	+ "&refresh=" + new Date().getTime();
		        //alert(page);
				document.all("div_ping").innerHTML = "正在采集在线时长，请耐心等待....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
	//转换时间
	function ChangeTime(type){
		switch(type){
			case "1"://日报表
				$("input[@name='starttime']").val($.getTime($("input[@name='endtime']").val(),"day","-",1,false));
				break;
			case "2"://周报表
				$("input[@name='starttime']").val($.getTime($("input[@name='endtime']").val(),"week","-",1,false));
				break;
			case "3"://月报表
				$("input[@name='starttime']").val($.getTime($("input[@name='endtime']").val(),"month","-",1,false));
				break;
		}
	}
	//将时间转换为秒
	function getCaleToSec(time){
		time=time.replace("-","/").replace("-","/");
		var t=new Date(time);
		return t.getTime()/1000;
	}

//-->
</SCRIPT>
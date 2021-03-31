<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<%@ include file="../head.jsp"%>

<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<%
String isJs = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript">

$(function()
{
	initDate();
	 var isFirst = '<s:property value="no_query"/>' ;
	 var isJs= "<%=isJs%>";
    if(isFirst!='true' && isJs=='js_dx'){
			$("input[@name='button']").attr("disabled", false);
		}
});
function do_test() {
	$("input[@name='button']").attr("disabled", true);
	frm.submit();
}
var flag = false;
//初始化时间
function initDate()
{
	//初始化时间  开启 by zhangcong 2011-06-02
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	hour = theday.getHours();
	mimute = theday.getMinutes();
	second = theday.getSeconds();


	var tempStartTime = $("input[@name='starttime']").val();
	//确定时间,其中flag为重置的标识 true表示为重置
	if(null==tempStartTime||""==tempStartTime||true==flag){
		//modify by zhangcong 如果是从功能菜单进来，开始时间默认为当年的第一天2011-06-02
		$("input[@name='starttime']").val(year+"-01-01 00:00:00");
	}else{
		//日期框的时间为选定的时间
		$("input[@name='starttime']").val(tempStartTime);
	}

	var tempEndTime = $("input[@name='endtime']").val();
	if(null==tempEndTime||""==tempEndTime||true==flag){
		//modify by zhangcong 结束时间默认为当天
		$("input[@name='endtime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
		//$("input[@name='endtime']").val($.now("-",true));
	}else{
		$("input[@name='endtime']").val(tempEndTime);
	}
	flag = false;
}



function exportUserInfo() {
	var page = "../Resource/exportUser!getInfoExcel.action?username1=" + 		document.frm.username1.value
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + document.frm.cityId.value
		+ "&bindState=" + document.frm.bindState.value
		+ "&timeType=" + document.frm.timeType.value
		+"&gw_type=" + document.frm.gw_type.value;
	if(document.frm.bindState.value=="1"){
		if(document.frm.isXJ.value=="1"){
			page = page + "&ouiState=" + document.frm.ouiState.value;
		}
	}
	if(document.frm.isXJ.value=="0"){
		page = page + "&packageId=" + document.frm.packageId.value;
	}
	document.all("childFrm").src =page;
}

function goPage(offset) {
	var page = "../Resource/exportUser.action?stroffset=" + offset
		+ "&username1=" + document.frm.username1.value
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + document.frm.cityId.value
		+ "&bindState=" + document.frm.bindState.value
		+ "&timeType=" + document.frm.timeType.value
		+"&gw_type=" + document.frm.gw_type.value;
	if(document.frm.bindState.value=="1"){
		if(document.frm.isXJ.value=="1"){
			page = page + "&ouiState=" + document.frm.ouiState.value;
		}
	}
	if(document.frm.isXJ.value=="0"){
		page = page + "&packageId=" + document.frm.packageId.value;
	}

	window.location.href = page;
}

//reset
function resetFrm() {
	//document.frm.starttime.value="";
	//document.frm.endtime.value="";
	//初始化时间 modify by zhangcong 2011-06-03
	flag = true;
	initDate();
	document.frm.username1.value="";
	document.frm.cityId.value="-1";
	if(document.frm.isXJ.value=="0"){
	document.frm.packageId.value="-1";
	}
	document.frm.timeType.value="-1";

	if(document.frm.bindState.value=="1" && document.frm.isXJ.value=="1")
	{
		document.frm.ouiState.value="-1";
	}
}

//查看bbms的用户相关的信息
function bbmsUserInfo(user_id){
	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}


//编辑bbms用户
function bbmsEditUser(user_id){
	window.location.href = "AddEGWUserInfoForm.jsp?user_id="+user_id+"&gwOptType=22";
}

//删除bbms用户
function bbmsDelUser(user_id){
	if (confirm("确定要删除该用户和用户所对应的设备吗？\n本操作所删除的不能恢复！！！")){
		window.location.href = "EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="+user_id;
	}
}


//查看itms用户相关的信息
function itmsUserInfo(user_id){
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

//编辑itms用户
function itmsEditUser(user_id){
	window.location.href = "../gwms/resource/hgwcustManage.action?userId="+ user_id;
}

//删除itms用户
function itmsDelUser(user_id){
	if (confirm("确定要删除该家庭网关用户吗？\n本操作所删除的不能恢复！！！")){
		window.location.href = "GwUserInfoSave.jsp?gwOptType=11&action=delete&user_id="+user_id;
	}
}

</script>

<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<form name="frm"
				action="<s:url value='/Resource/exportUser.action'/>" method="POST">
				<input name="gw_type" type="hidden"
					value="<s:property value='gw_type'/>">
				<input type="hidden" name="isXJ" value="<s:property value='isXJ' />">
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										用户资源
									</td>
									<td>
										&nbsp;
										<img src="../images/attention_2.gif" width="15" height="12">
										&nbsp;
										<s:if test="bindState==1">未绑定设备的用户</s:if>
										<s:else>已绑定设备的用户</s:else>
										,选择时间类型确定所要查询的时间。
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<th colspan="4">
										用户查询
									</th>
								</tr>
								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										用户账号
									</td>
									<td width='35%' align="left">
										<input name="username1" type="text" class='bk'
											value="<s:property value='username1'/>">
									</td>
									<td class="column" width='15%' align="right">
										查询时间类型
									</td>
									<td width='35%' align="left">
										<select name="timeType" class="bk">
											<option value="-1"
												<s:property value='"-1".equals(timeType)?"selected":""'/>>
												==请选择==
											</option>
											<option value="1"
												<s:property value='"1".equals(timeType)?"selected":""'/>>
												==工单受理时间==
											</option>
											<s:if test="bindState==2">
												<option value="2"
													<s:property value='"2".equals(timeType)?"selected":""'/>>
													==绑定时间==
												</option>
											</s:if>
											<option value="3"
												<s:property value='"3".equals(timeType)?"selected":""'/>>
												==开户时间==
											</option>
										</select>
									</td>

								</TR>
								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										开始时间
									</td>
									<td width='35%' align="left">
										<input type="text" name="starttime" class='bk' readonly
											value="<s:property value='starttime'/>">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
									</td>
									<td class="column" width='15%' align="right">
										结束时间
									</td>
									<td width='35%' align="left">
										<input type="text" name="endtime" class='bk' readonly
											value="<s:property value='endtime'/>">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
									</td>
								</TR>

								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										属&nbsp;&nbsp;&nbsp;&nbsp;地
									</td>
									<td width='35%' align="left"
										colspan="<s:property value='"1".equals(isXJ)?"3":"1"' />">
										<select name="cityId" class="bk">
											<option value="-1">
												==请选择==
											</option>
											<s:iterator var="citylist" value="CityList">
												<option value="<s:property value='#citylist.city_id'/>"
													<s:property value="#citylist.city_id.equals(cityId)?'selected':''"/>>
													==
													<s:property value="#citylist.city_name" />
													==
												</option>
											</s:iterator>
										</select>
									</td>

									<s:if test="isXJ==0">
										<td class="column" width='15%' align="right">
											套&nbsp;&nbsp;&nbsp;&nbsp;餐
										</td>
										<td width='35%' align="left">
											<select name="packageId" class="bk">
												<option value="-1">
													==请选择==
												</option>
												<s:iterator var="packagelist" value="PackageList">
													<option
														value="<s:property value='#packagelist.serv_package_id'/>"
														<s:property value="#packagelist.serv_package_id.equals(packageId)?'selected':''"/>>
														==
														<s:property value="#packagelist.serv_package_name" />
														==
													</option>
												</s:iterator>
											</select>
										</td>
									</s:if>
								</TR>
								<s:if test="bindState==1&&isXJ==1">
									<TR bgcolor=#ffffff>
										<td class="column" width='15%' align="right">
											用户设备
										</td>
										<td width='35%' align="left" colspan="3">
											<select name="ouiState" class="bk">
												<option value="-1"
													<s:property value='"-1".equals(ouiState)?"selected":""'/>>
													==请选择==
												</option>
												<option value="1"
													<s:property value='"1".equals(ouiState)?"selected":""'/>>
													==设备OUI为空==
												</option>
												<option value="2"
													<s:property value='"2".equals(ouiState)?"selected":""'/>>
													==未找到网关设备==
												</option>
											</select>
										</td>
									</TR>
								</s:if>
								<TR>
									<td class="green_foot" colspan="4" align="right">
										<input type="hidden" value="<s:property value="bindState"/>"
											name="bindState">
										<input class=jianbian name="button" type="button"
											onclick="do_test();" value=" 查 询 ">
										<INPUT class=jianbian TYPE="button" value=" 重 置 "
											onclick="resetFrm()">
										<s:if test='#session.isReport=="1"'>
											<INPUT TYPE="button" value=" 导 出 " class=jianbian
												onclick="exportUserInfo()">
										</s:if>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<tr>
						<td bgcolor=#ffffff>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<s:iterator value="title" status="status">
										<td class="green_title">
											<s:property value="title[#status.index]" />
										</td>

									</s:iterator>
									<td class="green_title" align='center' width="10%">
										操作
									</td>
								</tr>
								<s:iterator var="data" value="data" status="status">

									<s:if test="strBar!=''">
										<TR>
											<TD class="green_foot" COLSPAN="9" align="right">
												<s:property value="#data.strBar" escapeHtml="false" />
											</TD>
										</TR>
									</s:if>
									<s:else>
										<tr bgcolor="#ffffff">
											<s:if test="infoType == 1">
												<td class="column" align='center'>
													<s:property value="#data.city_name" />
												</td>
												<td class="column" align='center'>
													<s:property value="#data.user_type" />
												</td>
												<td class="column" align='center'>
													<s:property value="#data.username" />
												</td>

												<td class="column" align='center'>
													<s:property value="#data.opendate" />
												</td>
												<s:if test="isXJ==0">
													<td class="column" align='center' width="10%">
														<s:property value="#data.serv_package_name" />
													</td>
												</s:if>
												<s:if test="bindState==2||isXJ==1">
													<td class="column" align='center'>
														<s:property value="oui" />
														-
														<s:property value="#data.device_serialnumber" />
													</td>
													<td class="column" align='center'>
														<s:property value="#data.binddate" />
													</td>
												</s:if>
												<s:if test="isJSITMS==1&&bindState==1">
													<td class="column" align='center' width="10%">
														<s:property value="#data.iTV" />
													</td>
												</s:if>
												<td class="column" align='center'>
													<s:property value="#data.dealdate" />
												</td>
												<td class="column" align='center' width="10%">
													<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
														ALT='编辑'
														onclick="itmsEditUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
														ALT='删除'
														onclick="itmsDelUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
														ALT="详细信息"
														onclick="itmsUserInfo('<s:property value="user_id"/>')"
														style="cursor: hand">
												</td>
											</s:if>
											<s:else>
												<td class="column" align='center'>
													<s:property value="#data.city_name" />
												</td>
												<td class="column" align='center'>
													<s:property value="#data.user_type" />
												</td>
												<td class="column" align='center'>
													<s:property value="#data.username" />
												</td>

												<td class="column" align='center'>
													<s:property value="#data.opendate" />
												</td>
												<s:if test="isXJ==0">
													<td class="column" align='center' width="10%">
														<s:property value="#data.serv_package_name" />
													</td>
												</s:if>
												<s:if test="bindState==2||isXJ==1||isHljDx==1">
													<td class="column" align='center'>
														<s:property value="#data.oui" />
														-
														<s:property value="#data.device_serialnumber" />
													</td>
													<td class="column" align='center'>
														<s:property value="#data.binddate" />
													</td>
												</s:if>
												<td class="column" align='center'>
													<s:property value="#data.dealdate" />
												</td>
												<td class="column" align='center' width="10%">
													<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
														ALT='编辑'
														onclick="bbmsEditUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
														ALT='删除'
														onclick="bbmsDelUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
														ALT="详细信息"
														onclick="bbmsUserInfo('<s:property value="user_id"/>')"
														style="cursor: hand">
												</td>
											</s:else>

										</tr>
									</s:else>
								</s:iterator>
								<tr STYLE="display: none">
									<s:if test="infoType == 1">
										<td colspan="4">
											<iframe id="childFrm" src=""></iframe>
										</td>
									</s:if>
									<s:else>
										<td colspan="4">
											<iframe id="childFrm" src=""></iframe>
										</td>
									</s:else>
								</tr>
							</TABLE>
						</td>
					</tr>
				</table>
			</form>
		</TD>
	</TR>
</TABLE>
<br>
<%@ include file="../foot.jsp"%>

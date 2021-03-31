<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<%@ include file="../head.jsp"%>

<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript">
 $(function() {
	//初始化时间
	//init();
	changeOrderType();
	var orderType = '<s:property value="orderType" />';
	if(orderType==""){
		orderType="-1";
	}
	$("select[@name='orderType']").attr("value",orderType);

});
        //初始化时间
function init() {
	$("input[@name='starttime']").val($.now("-",false));
	$("input[@name='endtime']").val($.now("-",true));
}

function do_test() {
	frm.submit();
}

function exportUserInfo() {
	var page = "../Resource/exportUser!userByOrderExcel.action?bindState=" + 		document.frm.bindState.value
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + document.frm.cityId.value
		+ "&bigOrderType=" + document.frm.bigOrderType.value
		+ "&orderType=" + document.frm.orderType.value;
	document.all("childFrm").src =page;
}

function goPage(offset) {
	var page = "../Resource/exportUser!userByOrder.action?stroffset=" + offset
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + document.frm.cityId.value
		+ "&bindState=" + document.frm.bindState.value
		+ "&bigOrderType=" + document.frm.bigOrderType.value
		+ "&orderType=" + document.frm.orderType.value;

	window.location.href = page;
}

//reset
function resetFrm() {
	document.frm.starttime.value="";
	document.frm.endtime.value="";
	document.frm.cityId.value="-1";
	document.frm.bindState.value="-1";
	document.frm.bigOrderType.value="-1";
	changeOrderType();
	document.frm.orderType.value="-1";
}

function changeOrderType(){
	if(document.frm.bigOrderType.value=="1"){
		document.getElementById("orderType").options.length=0;
		document.getElementById("orderType").options.add(new Option("==请选择==","-1"));
		document.getElementById("orderType").options.add(new Option("==ADSL==","1"));
		document.getElementById("orderType").options.add(new Option("==普通LAN==","2"));
		document.getElementById("orderType").options.add(new Option("==普通光纤==","3"));
	}else if(document.frm.bigOrderType.value=="2"){
		document.getElementById("orderType").options.length=0;
		document.getElementById("orderType").options.add(new Option("==请选择==","-1"));
		document.getElementById("orderType").options.add(new Option("==专线LAN==","4"));
		document.getElementById("orderType").options.add(new Option("==专线光纤==","5"));
	}else{
		document.getElementById("orderType").options.length=0;
		document.getElementById("orderType").options.add(new Option("==请选择==","-1"));
		document.getElementById("orderType").options.add(new Option("==ADSL==","1"));
		document.getElementById("orderType").options.add(new Option("==普通LAN==","2"));
		document.getElementById("orderType").options.add(new Option("==普通光纤==","3"));
		document.getElementById("orderType").options.add(new Option("==专线LAN==","4"));
		document.getElementById("orderType").options.add(new Option("==专线光纤==","5"));
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
				action="<s:url value='/Resource/exportUser!userByOrder.action'/>"
				method="POST">
				<input name="infoType" type="hidden"
					value="<s:property value='infoType'/>">
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
										&nbsp; 按订单类型查询用户，时间为开户时间
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
									<td class="column" width='15%' align="right">
										绑定状态
									</td>
									<td width='35%' align="left">
										<select name="bindState" class="bk">
											<option value="-1"
												<s:property value='"-1".equals(bindState)?"selected":""'/>>
												==请选择==
											</option>
											<option value="2"
												<s:property value='"2".equals(bindState)?"selected":""'/>>
												==已绑定==
											</option>
											<option value="1"
												<s:property value='"1".equals(bindState)?"selected":""'/>>
												==未绑定==
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
										<img
											onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D',true,'whyGreen')"
											src="<s:url value='../images/search.gif'/>" width="15"
											height="12" border="0" alt="选择">
									</td>
									<td class="column" width='15%' align="right">
										结束时间
									</td>
									<td width='35%' align="left">
										<input type="text" name="endtime" class='bk' readonly
											value="<s:property value='endtime'/>">
										<img
											onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
											src="<s:url value='/images/search.gif'/>" width="15"
											height="12" border="0" alt="选择">
									</td>
								</TR>
								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										上网方式
									</td>
									<td width='35%' align="left">
										<select name="bigOrderType" class="bk"
											onchange="changeOrderType()">
											<option value="-1"
												<s:property value='"-1".equals(bigOrderType)?"selected":""'/>>
												==请选择==
											</option>
											<option value="1"
												<s:property value='"1".equals(bigOrderType)?"selected":""'/>>
												==PPoE拨号==
											</option>
											<option value="2"
												<s:property value='"2".equals(bigOrderType)?"selected":""'/>>
												==静态IP==
											</option>
										</select>
									</td>
									<td class="column" width='15%' align="right">
										订单类型
									</td>
									<td width='35%' align="left" id="allOrderType">
										<select name="orderType" class="bk">
											<option value="-1"
												<s:property value='"-1".equals(orderType)?"selected":""'/>>
												==请选择==
											</option>
											<option value="1"
												<s:property value='"1".equals(orderType)?"selected":""'/>>
												==ADSL==
											</option>
											<option value="2"
												<s:property value='"2".equals(orderType)?"selected":""'/>>
												==普通LAN==
											</option>
											<option value="3"
												<s:property value='"3".equals(orderType)?"selected":""'/>>
												==普通光纤==
											</option>
											<option value="4"
												<s:property value='"4".equals(orderType)?"selected":""'/>>
												==专线LAN==
											</option>
											<option value="5"
												<s:property value='"5".equals(orderType)?"selected":""'/>>
												==专线光纤==
											</option>
										</select>
									</td>
								</TR>
								<TR>
									<td class="green_foot" colspan="4" align="right">
										<input class="btn" name="button" type="button"
											onclick="do_test();" value=" 查 询 ">
										<INPUT CLASS="btn" TYPE="button" value=" 重 置 "
											onclick="resetFrm()">

									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<tr>
						<td bgcolor=#ffffff height="25">
							<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0'
								ALT='导出列表' style='cursor: hand' onclick="exportUserInfo()">
						</td>
					</tr>
					<tr>
						<td bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<td class="green_title" align='center' width="15%">
										属地
									</td>
									<td class="green_title" align='center' width="15%">
										开户时间
									</td>
									<td class="green_title" align='center' width="20%">
										客户名称
									</td>
									<td class="green_title" align='center' width="15%">
										用户账号
									</td>
									<td class="green_title" align='center' width="15%">
										订单类型
									</td>
									<td class="green_title" align='center' width="10%">
										绑定状态
									</td>
									<td class="green_title" align='center' width="10%">
										操作
									</td>
								</tr>
								<s:if test="data.size()>0">
									<s:iterator value="data">
										<s:if test="strBar!=''">
											<TR>
												<TD class="green_foot" COLSPAN="9" align="right">
													<s:property value="strBar" escapeHtml="false" />
												</TD>
											</TR>
										</s:if>
										<s:else>
											<tr bgcolor="#ffffff">
												<td class="column" align='center'>
													<s:property value="city_name" />
												</td>
												<td class="column" align='center'>
													<s:property value="opendate" />
												</td>
												<td class="column" align='center'>
													<s:property value="customer_name" />
												</td>
												<td class="column" align='center'>
													<s:property value="username" />
												</td>
												<td class="column" align='center'>
													<s:property value="ordertype" />
												</td>
												<td class="column" align='center'>
													<s:if test='bandstate=="已绑定"'>
													已绑定
												</s:if>
													<s:else>
														<font color="red"> 未绑定 </font>
													</s:else>
												</td>
												<td class="column" align='center' width="10%">
													<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
														ALT="详细信息"
														onclick="bbmsUserInfo('<s:property value="user_id"/>')"
														style="cursor: hand">
												</td>
											</tr>
										</s:else>
									</s:iterator>
								</s:if>
								<tr STYLE="display: none">
									<s:if test="infoType == 1">
										<td colspan="7">
											<iframe id="childFrm" src=""></iframe>
										</td>
									</s:if>
									<s:else>
										<td colspan="7">
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

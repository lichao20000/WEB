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
//��ʼ��ʱ��
function initDate()
{
	//��ʼ��ʱ��  ���� by zhangcong 2011-06-02
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	hour = theday.getHours();
	mimute = theday.getMinutes();
	second = theday.getSeconds();


	var tempStartTime = $("input[@name='starttime']").val();
	//ȷ��ʱ��,����flagΪ���õı�ʶ true��ʾΪ����
	if(null==tempStartTime||""==tempStartTime||true==flag){
		//modify by zhangcong ����Ǵӹ��ܲ˵���������ʼʱ��Ĭ��Ϊ����ĵ�һ��2011-06-02
		$("input[@name='starttime']").val(year+"-01-01 00:00:00");
	}else{
		//���ڿ��ʱ��Ϊѡ����ʱ��
		$("input[@name='starttime']").val(tempStartTime);
	}

	var tempEndTime = $("input[@name='endtime']").val();
	if(null==tempEndTime||""==tempEndTime||true==flag){
		//modify by zhangcong ����ʱ��Ĭ��Ϊ����
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
	//��ʼ��ʱ�� modify by zhangcong 2011-06-03
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

//�鿴bbms���û���ص���Ϣ
function bbmsUserInfo(user_id){
	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}


//�༭bbms�û�
function bbmsEditUser(user_id){
	window.location.href = "AddEGWUserInfoForm.jsp?user_id="+user_id+"&gwOptType=22";
}

//ɾ��bbms�û�
function bbmsDelUser(user_id){
	if (confirm("ȷ��Ҫɾ�����û����û�����Ӧ���豸��\n��������ɾ���Ĳ��ָܻ�������")){
		window.location.href = "EGWUserInfoSave.jsp?gwOptType=21&action=delete&user_id="+user_id;
	}
}


//�鿴itms�û���ص���Ϣ
function itmsUserInfo(user_id){
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

//�༭itms�û�
function itmsEditUser(user_id){
	window.location.href = "../gwms/resource/hgwcustManage.action?userId="+ user_id;
}

//ɾ��itms�û�
function itmsDelUser(user_id){
	if (confirm("ȷ��Ҫɾ���ü�ͥ�����û���\n��������ɾ���Ĳ��ָܻ�������")){
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
										�û���Դ
									</td>
									<td>
										&nbsp;
										<img src="../images/attention_2.gif" width="15" height="12">
										&nbsp;
										<s:if test="bindState==1">δ���豸���û�</s:if>
										<s:else>�Ѱ��豸���û�</s:else>
										,ѡ��ʱ������ȷ����Ҫ��ѯ��ʱ�䡣
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
										�û���ѯ
									</th>
								</tr>
								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										�û��˺�
									</td>
									<td width='35%' align="left">
										<input name="username1" type="text" class='bk'
											value="<s:property value='username1'/>">
									</td>
									<td class="column" width='15%' align="right">
										��ѯʱ������
									</td>
									<td width='35%' align="left">
										<select name="timeType" class="bk">
											<option value="-1"
												<s:property value='"-1".equals(timeType)?"selected":""'/>>
												==��ѡ��==
											</option>
											<option value="1"
												<s:property value='"1".equals(timeType)?"selected":""'/>>
												==��������ʱ��==
											</option>
											<s:if test="bindState==2">
												<option value="2"
													<s:property value='"2".equals(timeType)?"selected":""'/>>
													==��ʱ��==
												</option>
											</s:if>
											<option value="3"
												<s:property value='"3".equals(timeType)?"selected":""'/>>
												==����ʱ��==
											</option>
										</select>
									</td>

								</TR>
								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										��ʼʱ��
									</td>
									<td width='35%' align="left">
										<input type="text" name="starttime" class='bk' readonly
											value="<s:property value='starttime'/>">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">
									</td>
									<td class="column" width='15%' align="right">
										����ʱ��
									</td>
									<td width='35%' align="left">
										<input type="text" name="endtime" class='bk' readonly
											value="<s:property value='endtime'/>">
										<img name="shortDateimg"
											onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��">
									</td>
								</TR>

								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										��&nbsp;&nbsp;&nbsp;&nbsp;��
									</td>
									<td width='35%' align="left"
										colspan="<s:property value='"1".equals(isXJ)?"3":"1"' />">
										<select name="cityId" class="bk">
											<option value="-1">
												==��ѡ��==
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
											��&nbsp;&nbsp;&nbsp;&nbsp;��
										</td>
										<td width='35%' align="left">
											<select name="packageId" class="bk">
												<option value="-1">
													==��ѡ��==
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
											�û��豸
										</td>
										<td width='35%' align="left" colspan="3">
											<select name="ouiState" class="bk">
												<option value="-1"
													<s:property value='"-1".equals(ouiState)?"selected":""'/>>
													==��ѡ��==
												</option>
												<option value="1"
													<s:property value='"1".equals(ouiState)?"selected":""'/>>
													==�豸OUIΪ��==
												</option>
												<option value="2"
													<s:property value='"2".equals(ouiState)?"selected":""'/>>
													==δ�ҵ������豸==
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
											onclick="do_test();" value=" �� ѯ ">
										<INPUT class=jianbian TYPE="button" value=" �� �� "
											onclick="resetFrm()">
										<s:if test='#session.isReport=="1"'>
											<INPUT TYPE="button" value=" �� �� " class=jianbian
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
										����
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
														ALT='�༭'
														onclick="itmsEditUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
														ALT='ɾ��'
														onclick="itmsDelUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
														ALT="��ϸ��Ϣ"
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
														ALT='�༭'
														onclick="bbmsEditUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
														ALT='ɾ��'
														onclick="bbmsDelUser('<s:property value="user_id"/>')"
														style='cursor: hand'>
													<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
														ALT="��ϸ��Ϣ"
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

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
	//��ʼ��ʱ��
	//init();
	changeOrderType();
	var orderType = '<s:property value="orderType" />';
	if(orderType==""){
		orderType="-1";
	}
	$("select[@name='orderType']").attr("value",orderType);

});
        //��ʼ��ʱ��
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
		document.getElementById("orderType").options.add(new Option("==��ѡ��==","-1"));
		document.getElementById("orderType").options.add(new Option("==ADSL==","1"));
		document.getElementById("orderType").options.add(new Option("==��ͨLAN==","2"));
		document.getElementById("orderType").options.add(new Option("==��ͨ����==","3"));
	}else if(document.frm.bigOrderType.value=="2"){
		document.getElementById("orderType").options.length=0;
		document.getElementById("orderType").options.add(new Option("==��ѡ��==","-1"));
		document.getElementById("orderType").options.add(new Option("==ר��LAN==","4"));
		document.getElementById("orderType").options.add(new Option("==ר�߹���==","5"));
	}else{
		document.getElementById("orderType").options.length=0;
		document.getElementById("orderType").options.add(new Option("==��ѡ��==","-1"));
		document.getElementById("orderType").options.add(new Option("==ADSL==","1"));
		document.getElementById("orderType").options.add(new Option("==��ͨLAN==","2"));
		document.getElementById("orderType").options.add(new Option("==��ͨ����==","3"));
		document.getElementById("orderType").options.add(new Option("==ר��LAN==","4"));
		document.getElementById("orderType").options.add(new Option("==ר�߹���==","5"));
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
										�û���Դ
									</td>
									<td>
										&nbsp;
										<img src="../images/attention_2.gif" width="15" height="12">
										&nbsp; ���������Ͳ�ѯ�û���ʱ��Ϊ����ʱ��
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
									<td class="column" width='15%' align="right">
										��״̬
									</td>
									<td width='35%' align="left">
										<select name="bindState" class="bk">
											<option value="-1"
												<s:property value='"-1".equals(bindState)?"selected":""'/>>
												==��ѡ��==
											</option>
											<option value="2"
												<s:property value='"2".equals(bindState)?"selected":""'/>>
												==�Ѱ�==
											</option>
											<option value="1"
												<s:property value='"1".equals(bindState)?"selected":""'/>>
												==δ��==
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
										<img
											onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D',true,'whyGreen')"
											src="<s:url value='../images/search.gif'/>" width="15"
											height="12" border="0" alt="ѡ��">
									</td>
									<td class="column" width='15%' align="right">
										����ʱ��
									</td>
									<td width='35%' align="left">
										<input type="text" name="endtime" class='bk' readonly
											value="<s:property value='endtime'/>">
										<img
											onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
											src="<s:url value='/images/search.gif'/>" width="15"
											height="12" border="0" alt="ѡ��">
									</td>
								</TR>
								<TR bgcolor=#ffffff>
									<td class="column" width='15%' align="right">
										������ʽ
									</td>
									<td width='35%' align="left">
										<select name="bigOrderType" class="bk"
											onchange="changeOrderType()">
											<option value="-1"
												<s:property value='"-1".equals(bigOrderType)?"selected":""'/>>
												==��ѡ��==
											</option>
											<option value="1"
												<s:property value='"1".equals(bigOrderType)?"selected":""'/>>
												==PPoE����==
											</option>
											<option value="2"
												<s:property value='"2".equals(bigOrderType)?"selected":""'/>>
												==��̬IP==
											</option>
										</select>
									</td>
									<td class="column" width='15%' align="right">
										��������
									</td>
									<td width='35%' align="left" id="allOrderType">
										<select name="orderType" class="bk">
											<option value="-1"
												<s:property value='"-1".equals(orderType)?"selected":""'/>>
												==��ѡ��==
											</option>
											<option value="1"
												<s:property value='"1".equals(orderType)?"selected":""'/>>
												==ADSL==
											</option>
											<option value="2"
												<s:property value='"2".equals(orderType)?"selected":""'/>>
												==��ͨLAN==
											</option>
											<option value="3"
												<s:property value='"3".equals(orderType)?"selected":""'/>>
												==��ͨ����==
											</option>
											<option value="4"
												<s:property value='"4".equals(orderType)?"selected":""'/>>
												==ר��LAN==
											</option>
											<option value="5"
												<s:property value='"5".equals(orderType)?"selected":""'/>>
												==ר�߹���==
											</option>
										</select>
									</td>
								</TR>
								<TR>
									<td class="green_foot" colspan="4" align="right">
										<input class="btn" name="button" type="button"
											onclick="do_test();" value=" �� ѯ ">
										<INPUT CLASS="btn" TYPE="button" value=" �� �� "
											onclick="resetFrm()">

									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<tr>
						<td bgcolor=#ffffff height="25">
							<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0'
								ALT='�����б�' style='cursor: hand' onclick="exportUserInfo()">
						</td>
					</tr>
					<tr>
						<td bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<td class="green_title" align='center' width="15%">
										����
									</td>
									<td class="green_title" align='center' width="15%">
										����ʱ��
									</td>
									<td class="green_title" align='center' width="20%">
										�ͻ�����
									</td>
									<td class="green_title" align='center' width="15%">
										�û��˺�
									</td>
									<td class="green_title" align='center' width="15%">
										��������
									</td>
									<td class="green_title" align='center' width="10%">
										��״̬
									</td>
									<td class="green_title" align='center' width="10%">
										����
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
													<s:if test='bandstate=="�Ѱ�"'>
													�Ѱ�
												</s:if>
													<s:else>
														<font color="red"> δ�� </font>
													</s:else>
												</td>
												<td class="column" align='center' width="10%">
													<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
														ALT="��ϸ��Ϣ"
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

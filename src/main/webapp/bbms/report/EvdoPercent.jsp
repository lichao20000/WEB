<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
<!--

function ToExcel() {
	var page="../../bbms/report/evdoPercent!getExcel.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&type=" + $("input[@name='type'][@checked]").val();
	document.all("childFrm").src=page;
	//window.open(page);
}

function do_test() {
	if($("input[@name='type'][@checked]").val()==undefined){
	alert("��ѡ��ͳ������!");
	return false;
	}
	frm.submit();
}

function refresh(){
	window.location.href=window.location.href;
}

//reset
function resetFrm() {
	document.frm.starttime.value="";
	document.frm.endtime.value="";
	document.frm.device_serialnumber.value="";
	document.frm.loopback_ip.value="";
	document.frm.timeType.value="-1";
}
//-->
</script>

<form name="frm"
	action="<s:url value='/bbms/report/evdoPercent.action'/>" method="POST">

	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							EVDO����ռ��
						</TD>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" value="1" name="type"
								<s:property value='"1".equals(type)?"checked":""'/>>
							��������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" value="2" name="type"
								<s:property value='"2".equals(type)?"checked":""'/>>
							����ҵ
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							��ʼʱ��
						</td>
						<td width='30%' align="left">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>" size="15">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">(YYYY-MM-DD)
						</td>
						<td class="column" width='15%' align="right">
							����ʱ��
						</td>
						<td width='30%' align="left">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>" size="15" >
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">(YYYY-MM-DD)
						</td>
						<td class="column" width='10%' align="center">
							<input class="btn" name="button" type="button"
								onclick="do_test();" value=" ͳ �� ">
						</td>
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
				<td>
					<table width="100%" border=0 cellspacing=1 cellpadding=2
						bgcolor=#999999 id=userTable>
						<tr>
							<td class="green_title" align='center' width="25%">
								<s:if test="type==1">
									������
								</s:if>
								<s:if test="type==2">
									��ҵ
								</s:if>
							</td>
							<td class="green_title" align='center' width="25%">
								������
							</td>
							<td class="green_title" align='center' width="25%">
								EVDO����
							</td>
							<td class="green_title" align='center' width="25%">
								ռ��
							</td>
						</tr>
						<s:if test="data.size()>0">
							<s:iterator value="data">
								<tr>
									<td class="column" align='center' width="25%">
										<s:if test="type==1">
											<s:property value="city_name" />
										</s:if>
										<s:if test="type==2">
											<s:property value="cust_type_name" />
										</s:if>
									</td>
									<td class="column" align='center' width="25%">
										<s:property value="total" />
									</td>
									<td class="column" align='center' width="25%">
										<s:property value="evdototal" />
									</td>
									<td class="column" align='center' width="25%">
										<s:property value="percent" />
									</td>
								</tr>
							</s:iterator>
						</s:if>
					</table>
				</td>
			</tr>
			<tr bgcolor=#999999>
				<td>
					<table border=0 cellspacing=1 cellpadding=2 width="100%"
						align="center">
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="center" width="40">
								<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand' onclick="ToExcel()">
							</td>
							<td class=column1 align="right">
								<strong> ͳ�����ڣ�<s:property value="starttime" />��<s:property value="endtime" /> </strong>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		<tr STYLE="display: none">
			<td>
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</form>

<%@ include file="../foot.jsp"%>

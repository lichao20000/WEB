<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript">
	$(function()
	{

	});

	function ToExcel(starttime1, endtime1) {
		var page="<s:url value='/gwms/report/queryBatchRestartDevice!getBatchRestartDevExcel.action'/>?"+ "starttime1=" +
            starttime1 + "&endtime1=" + endtime1;
		document.all("childFrm").src=page;
	}

	function do_test() {
		$("input[@name='button']").attr("disabled", true);
		frm.submit();
	}

	function getRestartDevDetail(city_id, numType, starttime1, endtime1) {
		var page="<s:url value='/gwms/report/queryBatchRestartDevice!queryBatchRestartDevDetailList.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1 + "&cityId=" + city_id + "&numType=" + numType;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

<form name="frm"
	action="<s:url value='/gwms/report/queryBatchRestartDevice!queryBatchRestartDevList.action"'/>"
	method="POST">
<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td height="20"></td>
	</tr>
	<TR>
		<TD>
		<TABLE width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="164" align="center" class="title_bigwhite">���Ź������������ѯ</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; ʱ�䷶Χȷ����Ҫ��ѯ���豸��</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th colspan="4">���Ź������������ѯ</th>
			</tr>

			<TR bgcolor=#ffffff>
				<td class="column" width='15%' align="right">��ʼʱ��</td>
				<td width='35%' align="left"><input type="text"
					name="starttime" class='bk' readonly
					value="<s:property value='starttime'/>"> <img
					name="shortDateimg"
					onClick="WdatePicker({el:document.frm.starttime, dateFmt:'yyyy-MM-dd', skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></td>
				<td class="column" width='15%' align="right">����ʱ��</td>
				<td width='35%' align="left"><input type="text" name="endtime"
					class='bk' readonly value="<s:property value='endtime'/>">
				<img name="shortDateimg"
					onClick="WdatePicker({el:document.frm.endtime, dateFmt:'yyyy-MM-dd', skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></td>
			</TR>

			<TR>
				<td class="green_foot" colspan="4" align="right">
					<input class=jianbian name="button" type="button" onclick="do_test();" value=" �� ѯ ">
					<%--<INPUT TYPE="button" value=" �� �� " class=jianbian onclick="ToExcel()">--%>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#ffffff>&nbsp;</td>
	</tr>

	<s:if test="data==null">
		<!-- ��һ����ɼ��������κβ�ѯ -->
	</s:if>
	<s:else>
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<td class="green_title" align='center' style="display: none" >city_id</td>
						<td class="green_title" align='center' width="20%">����</td>
						<td class="green_title" align='center' width="15%">�����û���</td>
						<td class="green_title" align='center' width="15%">�����ɹ���</td>
						<td class="green_title" align='center' width="15%">����ʧ����</td>
						<td class="green_title" align='center' width="15%">���������û���</td>
						<td class="green_title" align='center' width="10%">������(%)</td>
						<td class="green_title" align='center' width="10%">�����ɹ���(%)</td>
					</tr>

					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<td class=column nowrap align="center" style="display: none"><s:property value="cityId" /></td>
								<td class=column nowrap align="center"><s:property value="cityName" /></td>

								<td class=column nowrap align="center">
									<a href="javascript:getRestartDevDetail('<s:property value="cityId" />', 'deviceNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>');">
										<s:property value="deviceNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getRestartDevDetail('<s:property value="cityId" />', 'successNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>');">
										<s:property value="successNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getRestartDevDetail('<s:property value="cityId" />', 'failedNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>');">
										<s:property value="failedNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getRestartDevDetail('<s:property value="cityId" />', 'improveNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>');">
										<s:property value="improveNum" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="improveRate" /></td>
								<td class=column nowrap align="center"><s:property value="successRate" /></td>
							</tr>
						</s:iterator>
					</s:if>

					<s:else>
						<tr>
							<td colspan=8 align=left class=column>ϵͳû����ص��豸��Ϣ!</td>
						</tr>
					</s:else>

				</table>
				</td>
			</tr>



<%--
		<s:if test="data.size()>0">
			<tr>
				<td align="right"><lk:pages
					url="/gwms/resource/queryDevice!gopageDeviceOperate.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</s:if>--%>

	</s:else>

    <tfoot>
        <tr>
            <td colspan="10">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
                     style='cursor: hand'
                     onclick="ToExcel('<s:property value="starttime1"/>', '<s:property value="endtime1"/>')">
            </td>
        </tr>

    </tfoot>


	<TR>
		<TD HEIGHT=20 align="center">
		<div id="_process"></div>
		</TD>
	</TR>

    <tr STYLE="display: none">
        <td colspan="5">
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</table>
</form>

<%@ include file="../foot.jsp"%>

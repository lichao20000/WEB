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
	$(function() {
        var starttime = '<s:property value='starttime'/>';
        var endtime = '<s:property value='endtime'/>';
	});


	function ToExcel(starttime1, endtime1) {
		var page="<s:url value='/gwms/report/singleBusEquipment!getSingleBusEquipmentExcel.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1;
		document.all("childFrm").src=page;
	}

	function do_test() {
		$("input[@name='button']").attr("disabled", true);
		frm.submit();
	}

	function getSingleBusEquipmentDetail(city_id, starttime1, endtime1, servTypeId) {
		var page="<s:url value='/gwms/report/singleBusEquipment!querySingleBusEquipmentDetailList.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1 + "&cityId=" + city_id + "&servTypeId=" + servTypeId;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

<form name="frm"
	action="<s:url value='/gwms/report/singleBusEquipment!getSingleBusEquipmentList.action"'/>"
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
				<TD width="164" align="center" class="title_bigwhite">����ҵ��ͳ���豸��Դ�����ѯ</TD>
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
				<th colspan="4">����ҵ��ͳ���豸��Դ�����ѯͳ��</th>
			</tr>
		</TABLE>


		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

			<TR bgcolor=#ffffff>
				<%--<TD class=column width="15%" align='right'>����</TD>
				<TD width="35%" colspan="3"><s:select list="cityList" name="city_id"
													  headerKey="00" headerValue="��ѡ������" listKey="city_id"
													  listValue="city_name" cssClass="bk"></s:select></TD>

				<TD class=column width="15%" align='right'>
					ҵ������
				</TD>
				<TD width="35%">
					<SELECT name="servTypeId" >
						<option value="">
							==��ѡ��==
						</option>
						<option value="10">
							==�������==
						</option>
						<option value="11">
							==IPTV==
						</option>
						<option value="14">
							==VoIP==
						</option>
					</SELECT>
				</TD>
--%>
				<td class="column" width='15%' align="right">��ͨʱ��</td>
				<td width='35%' align="left"><input type="text"
													name="starttime" class='bk' readonly
													value="<s:property value='starttime'/>"> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.frm.starttime, dateFmt:'yyyy-MM-dd HH:mm:ss', skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12" border="0"
						alt="ѡ��"></td>

				<td class="column" width='15%' align="right"> -- </td>
				<td width='35%' align="left"><input type="text" name="endtime"
													class='bk' readonly value="<s:property value='endtime'/>">
					<img name="shortDateimg"
						 onClick="WdatePicker({el:document.frm.endtime, dateFmt:'yyyy-MM-dd HH:mm:ss', skin:'whyGreen'})"
						 src="../../images/dateButton.png" width="15" height="12" border="0"
						 alt="ѡ��"></td>

			</TR>


			<%--<TR bgcolor=#ffffff id="timeSearch">

			</TR>--%>

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
						<td class="green_title" align='center' width="5%">����</td>
						<td class="green_title" align='center' width="5%">��ͥ���ؿ��ҵ��</td>
						<td class="green_title" align='center' width="6%">��ͥ����VOIPҵ��</td>
						<td class="green_title" align='center' width="6%">��ͥ����IPTVҵ��</td>
					</tr>

					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<td class=column nowrap align="center" style="display: none"><s:property value="cityId" /></td>
								<td class=column nowrap align="center"><s:property value="cityName" /></td>

								<td class=column nowrap align="center">
									<a href="javascript:getSingleBusEquipmentDetail('<s:property value="cityId" />', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '10');">
										<s:property value="broadbandNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSingleBusEquipmentDetail('<s:property value="cityId" />', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '14');">
										<s:property value="voipNum" />
									</a>
								</td>

								<td class=column nowrap align="center">
									<a href="javascript:getSingleBusEquipmentDetail('<s:property value="cityId" />',  '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '11');">
										<s:property value="iptvNum" />
									</a>
								</td>

							</tr>
						</s:iterator>
					</s:if>

					<s:else>
						<tr>
							<td colspan=19 align=left class=column>ϵͳû����ص��豸��Դ��Ϣ!</td>
						</tr>
					</s:else>

				</table>
				</td>
			</tr>

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

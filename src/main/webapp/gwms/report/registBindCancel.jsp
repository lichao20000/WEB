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
		var page="<s:url value='/gwms/report/registBindCancel!getRegistBindCancelExcel.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1;
		document.all("childFrm").src=page;
	}

	function do_test() {
		$("input[@name='button']").attr("disabled", true);
		frm.submit();
	}

	/*function getSingleBusEquipmentDetail(city_id, starttime1, endtime1, servTypeId) {
		var page="<s:url value='/gwms/report/singleBusEquipment!querySingleBusEquipmentDetailList.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1 + "&cityId=" + city_id + "&servTypeId=" + servTypeId;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}*/

</script>

<form name="frm"
	action="<s:url value='/gwms/report/registBindCancel!getRegistBindCancelList.action"'/>"
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
				<TD width="164" align="center" class="title_bigwhite">终端注册、绑定和注销状态统计报表查询</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; 时间范围确定所要查询的设备。</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th colspan="4">终端注册、绑定和注销状态统计报表查询统计</th>
			</tr>
		</TABLE>


		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

			<TR bgcolor=#ffffff>

				<td class="column" width='15%' align="right">设备入网时间</td>
				<td width='35%' align="left"><input type="text"
													name="starttime" class='bk' readonly
													value="<s:property value='starttime'/>"> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.frm.starttime, dateFmt:'yyyy-MM-dd HH:mm:ss', skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12" border="0"
						alt="选择"></td>
				<td class="column" width='15%' align="right"> -- </td>
				<td width='35%' align="left"><input type="text" name="endtime"
													class='bk' readonly value="<s:property value='endtime'/>">
					<img name="shortDateimg"
						 onClick="WdatePicker({el:document.frm.endtime, dateFmt:'yyyy-MM-dd HH:mm:ss', skin:'whyGreen'})"
						 src="../../images/dateButton.png" width="15" height="12" border="0"
						 alt="选择"></td>

			</TR>


			<%--<TR bgcolor=#ffffff id="timeSearch">

			</TR>--%>

			<TR>
				<td class="green_foot" colspan="4" align="right">
					<input class=jianbian name="button" type="button" onclick="do_test();" value=" 查 询 ">
					<%--<INPUT TYPE="button" value=" 导 出 " class=jianbian onclick="ToExcel()">--%>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#ffffff>&nbsp;</td>
	</tr>

	<s:if test="data==null">
		<!-- 第一次入采集，不做任何查询 -->
	</s:if>
	<s:else>
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<td class="green_title" align='center' style="display: none" >city_id</td>
						<td class="green_title" align='center' width="5%">属地</td>
						<td class="green_title" align='center' width="5%">注册设备数</td>
						<td class="green_title" align='center' width="6%">绑定设备数</td>
						<td class="green_title" align='center' width="6%">注销设备数</td>
					</tr>

					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<td class=column nowrap align="center" style="display: none"><s:property value="cityId" /></td>
								<td class=column nowrap align="center"><s:property value="cityName" /></td>

								<td class=column nowrap align="center">
									<s:property value="registNum" />
								</td>
								<td class=column nowrap align="center">
									<s:property value="bindNum" />
								</td>

								<td class=column nowrap align="center">
									<s:property value="cancelNum" />
								</td>


								<%--<td class=column nowrap align="center">
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
								</td>--%>

							</tr>
						</s:iterator>
					</s:if>

					<s:else>
						<tr>
							<td colspan=19 align=left class=column>系统没有相关的设备资源信息!</td>
						</tr>
					</s:else>

				</table>
				</td>
			</tr>

	</s:else>

    <tfoot>
        <tr>
            <td colspan="10">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
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

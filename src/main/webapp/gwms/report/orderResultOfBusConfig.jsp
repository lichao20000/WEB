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
		var page="<s:url value='/gwms/report/orderResultOfBusConfig!getOrderResultOfBusConfigExcel.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1;
		document.all("childFrm").src=page;
	}

	function do_test() {
		$("input[@name='button']").attr("disabled", true);
		frm.submit();
	}

	function getOrderResultOfBusConfigDetail(city_id, starttime1, endtime1, servTypeId, numType) {
		var page="<s:url value='/gwms/report/orderResultOfBusConfig!queryOrderResultOfBusConfigDetailList.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1
			+ "&cityId=" + city_id + "&servTypeId=" + servTypeId + "&numType=" + numType;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

<form name="frm"
	action="<s:url value='/gwms/report/orderResultOfBusConfig!getOrderResultOfBusConfigList.action"'/>"
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
				<TD width="164" align="center" class="title_bigwhite">??????????????????????????????</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; ????????????????????????????</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th colspan="4">??????????????????????????????????</th>
			</tr>
		</TABLE>


		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

			<TR bgcolor=#ffffff>
				<%--<TD class=column width="15%" align='right'>????</TD>
				<TD width="35%" colspan="3"><s:select list="cityList" name="city_id"
													  headerKey="00" headerValue="??????????" listKey="city_id"
													  listValue="city_name" cssClass="bk"></s:select></TD>

				<TD class=column width="15%" align='right'>
					????????
				</TD>
				<TD width="35%">
					<SELECT name="servTypeId" >
						<option value="">
							==??????==
						</option>
						<option value="10">
							==????????==
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
				<td class="column" width='15%' align="right">????????</td>
				<td width='35%' align="left"><input type="text"
													name="starttime" class='bk' readonly
													value="<s:property value='starttime'/>"> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.frm.starttime, dateFmt:'yyyy-MM-dd HH:mm:ss', skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12" border="0"
						alt="????"></td>
				<td class="column" width='15%' align="right"> -- </td>
				<td width='35%' align="left"><input type="text" name="endtime"
													class='bk' readonly value="<s:property value='endtime'/>">
					<img name="shortDateimg"
						 onClick="WdatePicker({el:document.frm.endtime, dateFmt:'yyyy-MM-dd HH:mm:ss', skin:'whyGreen'})"
						 src="../../images/dateButton.png" width="15" height="12" border="0"
						 alt="????"></td>

			</TR>


			<%--<TR bgcolor=#ffffff id="timeSearch">

			</TR>--%>

			<TR>
				<td class="green_foot" colspan="4" align="right">
					<input class=jianbian name="button" type="button" onclick="do_test();" value=" ?? ?? ">
					<%--<INPUT TYPE="button" value=" ?? ?? " class=jianbian onclick="ToExcel()">--%>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#ffffff>&nbsp;</td>
	</tr>

	<s:if test="data==null">
		<!-- ?????????????????????????? -->
	</s:if>
	<s:else>
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<td class="green_title" align='center' style="display: none" >city_id</td>
						<td class="green_title" align='center' width="5%">????</td>
						<td class="green_title" align='center' width="5%">??????????</td>
						<td class="green_title" align='center' width="5%">??????????????????</td>
						<td class="green_title" align='center' width="5%">??????????????????(%)</td>
						<td class="green_title" align='center' width="5%">VOIP??????</td>
						<td class="green_title" align='center' width="5%">VOIP??????????????</td>
						<td class="green_title" align='center' width="5%">VOIP??????????????(%)</td>
						<td class="green_title" align='center' width="5%">IPTV??????</td>
						<td class="green_title" align='center' width="5%">IPTV??????????????</td>
						<td class="green_title" align='center' width="5%">IPTV??????????????(%)</td>

					</tr>

					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<td class=column nowrap align="center" style="display: none"><s:property value="cityId" /></td>
								<td class=column nowrap align="center"><s:property value="cityName" /></td>

								<td class=column nowrap align="center">
									<a href="javascript:getOrderResultOfBusConfigDetail('<s:property value="cityId" />', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '10', '');">
										<s:property value="broadbandNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getOrderResultOfBusConfigDetail('<s:property value="cityId" />', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '10', '1');">
										<s:property value="broadbandSuccessNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<s:property value="broadbandSuccessRate" />
								</td>

								<td class=column nowrap align="center">
									<a href="javascript:getOrderResultOfBusConfigDetail('<s:property value="cityId" />', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '14', '');">
										<s:property value="voipNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getOrderResultOfBusConfigDetail('<s:property value="cityId" />', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '14', '1');">
										<s:property value="voipSuccessNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<s:property value="voipSuccessRate" />
								</td>


								<td class=column nowrap align="center">
									<a href="javascript:getOrderResultOfBusConfigDetail('<s:property value="cityId" />',  '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '11', '');">
										<s:property value="iptvNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getOrderResultOfBusConfigDetail('<s:property value="cityId" />',  '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '11', '1');">
										<s:property value="iptvSuccessNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<s:property value="iptvSuccessRate" />
								</td>
							</tr>
						</s:iterator>
					</s:if>

					<s:else>
						<tr>
							<td colspan=19 align=left class=column>??????????????????????????!</td>
						</tr>
					</s:else>

				</table>
				</td>
			</tr>

	</s:else>

    <tfoot>
        <tr>
            <td colspan="10">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='????????'
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

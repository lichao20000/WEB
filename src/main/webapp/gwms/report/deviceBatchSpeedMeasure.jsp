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
        var taskName = '<s:property value='taskName'/>';

        if(undefined != taskName && "null" != taskName && "" != taskName){
            $("#searchType_1").attr("checked","true");
            $("#taskNameSearch").show();
            $("#timeSearch").hide();
        }
	    else if (undefined != starttime && "null" != starttime && "" != starttime) {
            $("#searchType_2").attr("checked","true");
            $("#taskNameSearch").hide();
            ("#timeSearch").show();
		}
		else {
            $("#searchType_1").click();
        }


        /*$("#taskNameSearch").show();
        $("#timeSearch").hide();
        $("input[@name='taskName']").val("");
        $("input[@name='starttime']").val("");
        $("input[@name='endtime']").val("");*/
	});

    function initSearchDate() {
        //��ȡϵͳ��ǰʱ��
        var nowdate = new Date();
        var endYear = nowdate.getFullYear();
        var endMonth = nowdate.getMonth()+1;
        var endDay = nowdate.getDate();
        $("input[@name='endtime']").val(endYear+"-" + endMonth + "-" + endDay);

        //��ȡϵͳǰһ�ܵ�ʱ��
        var oneweekdate = new Date(nowdate-7*24*3600*1000);
        var startYear = oneweekdate.getFullYear();
        var startMonth = oneweekdate.getMonth()+1;
        var startDay = oneweekdate.getDate();
        $("input[@name='starttime']").val(startYear+"-" + startMonth + "-" + startDay);
    }

    function searchTypeChange(type) {
        if ("task" === type) {
            $("#taskNameSearch").show();
            $("#timeSearch").hide();
            $("input[@name='taskName']").val("");
            $("input[@name='starttime']").val("");
            $("input[@name='endtime']").val("");
        }
        else {
            $("#taskNameSearch").hide();
            $("#timeSearch").show();
            $("input[@name='taskName']").val("");
            initSearchDate();
        }
    }


	function ToExcel(starttime1, endtime1, taskName) {
		var page="<s:url value='/gwms/report/queryBatchSpeedMeasure!getBatchSpeedMeasureExcel.action'/>?"+ "starttime1=" +
            starttime1 + "&endtime1=" + endtime1 + "&taskNameEncode=" + encodeURI(encodeURI(taskName));
		document.all("childFrm").src=page;
	}

	function do_test() {
		$("input[@name='button']").attr("disabled", true);
		frm.submit();
	}

	function getSpeedMeasureDetail(city_id, downlink, numType, starttime1, endtime1, taskName) {
		var page="<s:url value='/gwms/report/queryBatchSpeedMeasure!queryBatchSpeedMeasureDetailList.action'/>?"+ "starttime1=" + starttime1 + "&endtime1=" + endtime1 + "&cityId=" + city_id + "&downlink=" + downlink + "&numType=" + numType + "&taskNameEncode=" + encodeURI(encodeURI(taskName));
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

<form name="frm"
	action="<s:url value='/gwms/report/queryBatchSpeedMeasure!queryBatchSpeedMeasureList.action"'/>"
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
				<TD width="164" align="center" class="title_bigwhite">�������ٽ����ѯ</TD>
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
				<th colspan="4">�������ٽ����ѯͳ��</th>
			</tr>

			<TR bgcolor=#ffffff>
				<td class="column" width='50%' align="left">
					<input type="radio" name="searchType" id="searchType_1" value="1" onclick="searchTypeChange('task')">����������ѡ��
				</td>
				<td class="column" width='50%' align="left">
					<input type="radio" name="searchType" id="searchType_2" value="2" onclick="searchTypeChange('time')">��ʱ���ѡ��
				</td>
			</TR>

		</TABLE>

		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR bgcolor=#ffffff id="taskNameSearch">
				<td class="column" width='25%' align="left">��������(֧�ֶ�������ö��ŷָ�)</td>
				<td width='75%' align="left">
					<input type="text" name="taskName" class='bk' style="width: 33%"
					value="<s:property value='taskName'/>"></td>
			</TR>

		</TABLE>

		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR bgcolor=#ffffff id="timeSearch">
				<td class="column" width='25%' align="left">��ʼʱ��</td>
				<td width='25%' align="left"><input type="text"
					name="starttime" class='bk' readonly
					value="<s:property value='starttime'/>"> <img
					name="shortDateimg"
					onClick="WdatePicker({el:document.frm.starttime, dateFmt:'yyyy-MM-dd', skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></td>
				<td class="column" width='25%' align="left">����ʱ��</td>
				<td width='25%' align="left"><input type="text" name="endtime"
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
						<td class="green_title" align='center' width="5%">����</td>
						<td class="green_title" align='center' width="5%">��������</td>
						<td class="green_title" align='center' width="5%">�������</td>
						<td class="green_title" align='center' width="5%">��������(%)</td>
						<td class="green_title" align='center' width="5%">����100M����</td>
						<td class="green_title" align='center' width="5%">����100M�������</td>
						<td class="green_title" align='center' width="5%">����100M�����(%)</td>
						<td class="green_title" align='center' width="5%">����200M����</td>
						<td class="green_title" align='center' width="5%">����200M�������</td>
						<td class="green_title" align='center' width="6%">����200M�����(%)</td>
						<td class="green_title" align='center' width="5%">����300M����</td>
						<td class="green_title" align='center' width="5%">����300M�������</td>
						<td class="green_title" align='center' width="6%">����300M�����(%)</td>
						<td class="green_title" align='center' width="5%">����500M����</td>
						<td class="green_title" align='center' width="5%">����500M�������</td>
						<td class="green_title" align='center' width="6%">����500M�����(%)</td>
						<td class="green_title" align='center' width="5%">����1000M����</td>
						<td class="green_title" align='center' width="6%">����1000M�������</td>
						<td class="green_title" align='center' width="6%">����1000M�����(%)</td>
					</tr>

					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<td class=column nowrap align="center" style="display: none"><s:property value="cityId" /></td>
								<td class=column nowrap align="center"><s:property value="cityName" /></td>

								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', 'allRate', 'allNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="measureAllNum" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', 'allRate', 'accessStandard', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="accStandAllNum" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="accStandAllRate" /></td>


								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '100M', 'allNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="measureAllNum_100" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '100M', 'accessStandard', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="accStandAllNum_100" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="accStandAllRate_100" /></td>


								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '200M', 'allNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="measureAllNum_200" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '200M', 'accessStandard', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="accStandAllNum_200" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="accStandAllRate_200" /></td>


								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '300M', 'allNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="measureAllNum_300" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '300M', 'accessStandard', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="accStandAllNum_300" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="accStandAllRate_300" /></td>


								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '500M', 'allNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="measureAllNum_500" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '500M', 'accessStandard', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="accStandAllNum_500" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="accStandAllRate_500" /></td>


								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '1000M', 'allNum', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="measureAllNum_1000" />
									</a>
								</td>
								<td class=column nowrap align="center">
									<a href="javascript:getSpeedMeasureDetail('<s:property value="cityId" />', '1000M', 'accessStandard', '<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>');">
										<s:property value="accStandAllNum_1000" />
									</a>
								</td>
								<td class=column nowrap align="center"><s:property value="accStandAllRate_1000" /></td>

							</tr>
						</s:iterator>
					</s:if>

					<s:else>
						<tr>
							<td colspan=19 align=left class=column>ϵͳû����صĲ��ٽ����Ϣ!</td>
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
                     onclick="ToExcel('<s:property value="starttime1"/>', '<s:property value="endtime1"/>', '<s:property value="taskName"/>')">
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

<!-- ���������ͷ�ļ� -->
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!-- ��ӵ������� -->
<%@page import="java.util.Map"%>
<jsp:useBean id="deviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<HTML>
<head>
<TITLE>���տ�ͨ�û�����ѯ</TITLE>
<!-- ��ӵ�������ʽ�ͽű� -->
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<!-- ��дJS�ű� -->
<script type="text/javascript">
	function queryDataBindSuccess(cityId, startTime, endTime) {
		var page = "<s:url value='/itms/report/queryOpenBusiness!gopageUserList.action'/>?"
				+ "city_id="
				+ cityId
				+ "&starttime="
				+ startTime
				+ "&endtime="
				+ endTime + "&dataType=2";
		/*����һ������*/
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	function queryDataBindFail(cityId, startTime, endTime) {
		var page = "<s:url value='/itms/report/queryOpenBusiness!gopageUserList.action'/>?"
				+ "city_id="
				+ cityId
				+ "&starttime="
				+ startTime
				+ "&endtime="
				+ endTime + "&dataType=3";
		/*����һ������*/
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	/**
	 * ��ѯ�����û�ͳ����Ŀ��ϸ��Ϣ!
	 **/
	function queryDataTotal(cityId, startTime, endTime) {
		/*����һ���첽�����Action��ִ�еĽ��*/
		var page = "<s:url value='/itms/report/queryOpenBusiness!gopageUserList.action'/>?"
				+ "city_id="
				+ cityId
				+ "&starttime="
				+ startTime
				+ "&endtime="
				+ endTime + "&dataType=1";
		/*����һ������*/
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	//��ʽ��ʱ��
	function formatDate(strValue) {
		var str1 = strValue.substring(strValue.indexOf("-") + 1, strValue
				.lastIndexOf("-"));
		str1 = (str1.length == 1) ? "0" + str1 : str1;
		var str2 = strValue.substring(strValue.lastIndexOf("-") + 1);
		str2 = (str2.length == 1) ? "0" + str2 : str2;
		strValue = strValue.substring(0, strValue.indexOf("-") + 1) + str1
				+ "-" + str2;
		return strValue;
	}

	//��2004-10-20������ʱ��ת����long��
	function DateParse(strValue) {
		strValue = strValue.substring(5, 10) + "-" + strValue.substring(0, 4);
		return Date.parse(strValue);
	}
	function strTime2Second(dateStr) {

		var temp = dateStr.split(' ')
		var date = temp[0].split('-');
		var time = temp[1].split(':');

		var reqReplyDate = new Date();
		reqReplyDate.setYear(date[0]);
		reqReplyDate.setMonth(date[1] - 1);
		reqReplyDate.setDate(date[2]);
		reqReplyDate.setHours(time[0]);
		reqReplyDate.setMinutes(time[1]);
		reqReplyDate.setSeconds(time[2]);

		return Math.floor(reqReplyDate.getTime() / 1000);
	}

	/**	
	 *�ύ��ѯ
	 ***/
	function queryData(city_id, city_name, startTime, endTime) {
		/*��ȡ���յĿ�ͨ�û���Ϣ*/
		$("input[@name='button']").attr("disabled", true);
		var url = "itms/report/queryOpenBusiness!getDayReport.action";
		var dataList = document.getElementById("dataList");

		var queryDate = document.getElementById("QueryData");
		queryDate.style.display = "";
		dataList.style.display = "";
		dataList.innerHTML = "���ڲ�ѯ�����Ե�....";
		$.post(url, {
			city_id : city_id,
			city_name : city_name,
			starttime : startTime,
			endtime : endTime
		}, function(mesg) {/* mesg�Ǻ�̨ҳ�� */
			dataList.innerHTML = mesg;
			$("input[@name='button']").attr("disabled", false);
			queryDate.style.display = "none";
			/*�����صı����ʾ����*/
			dataList.style.display = "";
		});

	}

	/**
	 *�����ѯ��ť���¼�
	 **/
	function do_query() {
		var startTime = strTime2Second(frm.starttime.value);
		var endTime = strTime2Second(frm.endtime.value);
		/**
		 * �����ȡ���������б��ֵ��ע��JQuery����name���Ի�ȡֵ�ĸ�ʽ��
		 **/
		var city_id = $("select[name='strCityList']").val();
		var city_name = $("select[name='strCityList']").find("option:selected")
				.text();
		if (city_id == "-1"||city_name=="��ѡ������"||city_id==null||city_id=="" ) {
			alert("��ѡ����Ҫ��ѯ������!");
			return;
		}
		var d = new Date();
		var today = d.getTime() / 1000;
		if (startTime > endTime) {
			window.alert("��ѯ��ʼʱ�䲻�ܴ��ڲ�ѯ����ʱ��!");
			return;
		}
		if (endTime > today) {
			window.alert("��ѯ����ʱ�䲻�ܴ���ϵͳ��ǰʱ��!");
			return;
		}
		queryData(city_id, city_name, startTime, endTime);
	}

	function queryDataForExcel(city_id, startTime, endTime) {
		var city_id = $("select[name='strCityList']").val();
		var page = "<s:url value='/itms/report/queryOpenBusiness!getExcel.action'/>?"
				+ "&starttime="
				+ startTime
				+ "&city_id="
				+ city_id
				+ "&endtime=" + endTime;
		document.all("childFrm").src = page;
	}

	//����ʱ�������,��frame�����еĶ�������
	function resetFrm(obj) {
		obj.reset();
		var dataList = document.getElementById("dataList");
		dataList.style.display = "none";
		city_id = $("select[name='strCityList']").value = "-1";
	}
</script>
</head>
<body>
	<form name="frm"
		action="<s:url value='/itms/report/queryOpenBusiness!queryUserAll.action"'/>"
		method="POST">

		<input type="hidden" name="gw_type"
			value="<s:property value='gw_type'/>">

		<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td height="20"></td>
			</tr>
			<TR>
				<TD>
					<TABLE width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<TR>
							<TD width="164" align="center" class="title_bigwhite">���տ�ͨ�û���</TD>
							<td>&nbsp; <img
								src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12"> &nbsp;��ͨ�û��б�,ѡ��ʱ������ȷ����Ҫ��ѯ��ʱ�䡣
							</td>
						</TR>
					</TABLE>
				</TD>
			</TR>

			<tr>
				<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%">
						<tr>
							<th colspan="4">���տ�ͨ�û�����ѯ</th>
						</tr>
						<!-- ѡ������ -->
						<%-- <tr bgcolor=#ffffff>
							<td class="column" width='15%' align="right">����</td>
							<td width='35%' align="left" colspan="3"><%=strCityList%></td>
						</tr> --%>

						<tr bgcolor=#ffffff>
							<td class="column" width='15%' align="right">����</td>
							<td width='35%' align="left" colspan="3"><s:select
									name="strCityList" list="strCityList" cssClass="bk"
									headerKey="-1" headerValue="==��ѡ������==" listKey="city_id"
									listValue="city_name" onchange="showChild();">
								</s:select></td>
						</tr>

						<tr bgcolor=#ffffff>
							<td class="column" width='15%' align="right">��ʼʱ��</td>
							<td width='35%' align="left"><input type="text"
								name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>"> <img
								onclick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��"></td>
							<td class="column" width='15%' align="right">����ʱ��</td>
							<td width='35%' align="left"><input type="text"
								name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>"> <img
								onclick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��"></td>
						</tr>
						<!-- ������ť -->
						<tr>
							<td class="green_foot" colspan="4" align="right"><input
								class=jianbian name="button" type="button"
								onclick="javascript:do_query()" value=" ͳ��"> <INPUT
								class=jianbian TYPE="button" value=" �� ��  "
								onclick="resetFrm(document.frm)"></td>
						</tr>
					</table>
				</TD>
			</TR>

			<!-- ����ǰ�ɫ��  -->
			<tr>
				<td bgcolor=#ffffff>&nbsp;</td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						���ڲ�ѯ�����Ե�....</div>
				</td>
			</tr>
			<!-- ��ʾ���,�ڸñ������ʾ����HTML-->
			<tr>
				<TD><div id="dataList" align=center></div></TD>
			</tr>
		</table>
	</form>
	<iframe id="childFrm" name="childFrm" style="display: none"></iframe>
</body>
<%@ include file="/foot.jsp"%>
</HTML>
<!-- 必须包含的头文件 -->
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!-- 添加的依赖包 -->
<%@page import="java.util.Map"%>
<jsp:useBean id="deviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<HTML>
<head>
<TITLE>当日开通用户数查询</TITLE>
<!-- 添加的依赖样式和脚本 -->
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT type="text/javascript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<!-- 编写JS脚本 -->
<script type="text/javascript">
	function queryDataBindSuccess(cityId, startTime, endTime) {
		var page = "<s:url value='/itms/report/queryOpenBusiness!gopageUserList.action'/>?"
				+ "city_id="
				+ cityId
				+ "&starttime="
				+ startTime
				+ "&endtime="
				+ endTime + "&dataType=2";
		/*弹出一个窗口*/
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
		/*弹出一个窗口*/
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	/**
	 * 查询所有用户统计数目详细信息!
	 **/
	function queryDataTotal(cityId, startTime, endTime) {
		/*返回一个异步请求的Action的执行的结果*/
		var page = "<s:url value='/itms/report/queryOpenBusiness!gopageUserList.action'/>?"
				+ "city_id="
				+ cityId
				+ "&starttime="
				+ startTime
				+ "&endtime="
				+ endTime + "&dataType=1";
		/*弹出一个窗口*/
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}

	//格式化时间
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

	//将2004-10-20此样的时间转换成long型
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
	 *提交查询
	 ***/
	function queryData(city_id, city_name, startTime, endTime) {
		/*获取当日的开通用户信息*/
		$("input[@name='button']").attr("disabled", true);
		var url = "itms/report/queryOpenBusiness!getDayReport.action";
		var dataList = document.getElementById("dataList");

		var queryDate = document.getElementById("QueryData");
		queryDate.style.display = "";
		dataList.style.display = "";
		dataList.innerHTML = "正在查询，请稍等....";
		$.post(url, {
			city_id : city_id,
			city_name : city_name,
			starttime : startTime,
			endtime : endTime
		}, function(mesg) {/* mesg是后台页面 */
			dataList.innerHTML = mesg;
			$("input[@name='button']").attr("disabled", false);
			queryDate.style.display = "none";
			/*将隐藏的表格显示出来*/
			dataList.style.display = "";
		});

	}

	/**
	 *点击查询按钮的事件
	 **/
	function do_query() {
		var startTime = strTime2Second(frm.starttime.value);
		var endTime = strTime2Second(frm.endtime.value);
		/**
		 * 这里获取属地下拉列表的值！注意JQuery根据name属性获取值的格式！
		 **/
		var city_id = $("select[name='strCityList']").val();
		var city_name = $("select[name='strCityList']").find("option:selected")
				.text();
		if (city_id == "-1"||city_name=="请选择属地"||city_id==null||city_id=="" ) {
			alert("请选择需要查询的属地!");
			return;
		}
		var d = new Date();
		var today = d.getTime() / 1000;
		if (startTime > endTime) {
			window.alert("查询开始时间不能大于查询结束时间!");
			return;
		}
		if (endTime > today) {
			window.alert("查询结束时间不能大于系统当前时间!");
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

	//重置时间和属地,该frame中所有的都会重置
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
							<TD width="164" align="center" class="title_bigwhite">当日开通用户数</TD>
							<td>&nbsp; <img
								src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12"> &nbsp;开通用户列表,选择时间类型确定所要查询的时间。
							</td>
						</TR>
					</TABLE>
				</TD>
			</TR>

			<tr>
				<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%">
						<tr>
							<th colspan="4">当日开通用户数查询</th>
						</tr>
						<!-- 选择属地 -->
						<%-- <tr bgcolor=#ffffff>
							<td class="column" width='15%' align="right">属地</td>
							<td width='35%' align="left" colspan="3"><%=strCityList%></td>
						</tr> --%>

						<tr bgcolor=#ffffff>
							<td class="column" width='15%' align="right">属地</td>
							<td width='35%' align="left" colspan="3"><s:select
									name="strCityList" list="strCityList" cssClass="bk"
									headerKey="-1" headerValue="==请选择属地==" listKey="city_id"
									listValue="city_name" onchange="showChild();">
								</s:select></td>
						</tr>

						<tr bgcolor=#ffffff>
							<td class="column" width='15%' align="right">开始时间</td>
							<td width='35%' align="left"><input type="text"
								name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>"> <img
								onclick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择"></td>
							<td class="column" width='15%' align="right">结束时间</td>
							<td width='35%' align="left"><input type="text"
								name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>"> <img
								onclick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择"></td>
						</tr>
						<!-- 操作按钮 -->
						<tr>
							<td class="green_foot" colspan="4" align="right"><input
								class=jianbian name="button" type="button"
								onclick="javascript:do_query()" value=" 统计"> <INPUT
								class=jianbian TYPE="button" value=" 重 置  "
								onclick="resetFrm(document.frm)"></td>
						</tr>
					</table>
				</TD>
			</TR>

			<!-- 间隔是白色的  -->
			<tr>
				<td bgcolor=#ffffff>&nbsp;</td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						正在查询，请稍等....</div>
				</td>
			</tr>
			<!-- 显示表格,在该表格中显示的是HTML-->
			<tr>
				<TD><div id="dataList" align=center></div></TD>
			</tr>
		</table>
	</form>
	<iframe id="childFrm" name="childFrm" style="display: none"></iframe>
</body>
<%@ include file="/foot.jsp"%>
</HTML>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>预检预修告警查询引擎</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/ids/AlarmQuery!getIdsarmInfoList.action' />";
		mainForm.submit();
		$("button[@id='btn1']").css("display", "");
		$("button[@id='btn2']").css("display", "none");
	}

	function insert() {
		var url = "<s:url value='/ids/AlarmQuery!insert.action' />";
		var alarmname = $("select[name='alarmname']").val();
		var alarmcode = $("select[name='alarmcode']").val();
		var alarmlevel = $("select[name='alarmlevel']").val();
		var alarmobject = $("select[name='alarmobject']").val();
		var hour = $("input[name='hour']").val();
		var count = $("input[name='count']").val();
		var temperature = $("input[name='temperature']").val();
		var timedelay = $("input[name='timedelay']").val();
		var lightpower = $("input[name='lightpower']").val();
		var packetloss = $("input[name='packetloss']").val();
		if (alarmname == "") {
			alert("请选择告警名称！");
			return false;
		}
		if (alarmcode == "") {
			alert("请选择告警代码！");
			return false;
		}
		if (alarmlevel == "") {
			alert("请选择告警级别！");
			return false;
		}
		if (alarmobject == "") {
			alert("请选择派单对象！");
			return false;
		}
		$.post(url, {
			alarmname : alarmname,
			alarmname : alarmname,
			alarmcode : alarmcode,
			alarmlevel : alarmlevel,
			alarmobject : alarmobject,
			hour : hour,
			count : count,
			temperature : temperature,
			timedelay : timedelay,
			lightpower : lightpower,
			packetloss : packetloss
		});
		query();
	}

	function del(id) {
		if (confirm("确定要删除该条信息！")) {
			var url = "<s:url value='/ids/AlarmQuery!delete.action' />?id="
					+ id;
			$.post(url);
			query();
			return true;
		} else {
			return false;
		}
	}

	function edit(id, alarm_name, alarm_code, alarm_level, alarm_period,
			alarm_count, rx_power, temperature, delay, loss_pp, send_sheet_obj) {
		$("input[name='id']").val(id);
		if (alarm_name == "语音注册失败") {
			alarm_name = "11";
		} else if (alarm_name == "光路劣化") {
			alarm_name = "22";
		} else if (alarm_name == "PPPOE拨号失败") {
			alarm_name = "33";
		} else if (alarm_name == "环境感知告警") {
			alarm_name = "44";
		} else if (alarm_name == "网络质量告警") {
			alarm_name = "55";
		} else if (alarm_name == "网络质量关联分析告警") {
			alarm_name = "66";
		} else if (alarm_name == "光路劣化光联分析") {
			alarm_name = "77";
		} else {
			alarm_name = "";
		}

		if (alarm_level == "紧急") {
			alarm_level = "10";
		} else if (alarm_level == "重要") {
			alarm_level = "20";
		} else if (alarm_level == "次要") {
			alarm_level = "30";
		} else {
			alarm_level = "";
		}

		if (send_sheet_obj == "客户类") {
			send_sheet_obj = "1";
		} else if (send_sheet_obj == "网络类") {
			send_sheet_obj = "2";
		} else {
			send_sheet_obj = "";
		}
		$("select[name='alarmname'] option[value='" + alarm_name + "']").attr(
				"selected", "selected");
		$("select[name='alarmcode'] option[value='" + alarm_code + "']").attr(
				"selected", "selected");
		$("select[name='alarmlevel'] option[value='" + alarm_level + "']")
				.attr("selected", "selected");
		$("select[name='alarmobject'] option[value='" + send_sheet_obj + "']")
				.attr("selected", "selected");
		$("input[name='hour']").val(alarm_period);
		$("input[name='count']").val(alarm_count);
		$("input[name='temperature']").val(temperature);
		$("input[name='lightpower']").val(rx_power);
		$("input[name='timedelay']").val(delay);
		$("input[name='packetloss']").val(loss_pp);
		$("input[name='id']").val(id);
		$("button[@id='btn1']").css("display", "none");
		$("button[@id='btn2']").css("display", "");
	}

	function update() {
		var alarmname = $("select[name='alarmname']").val();
		var alarmcode = $("select[name='alarmcode']").val();
		var alarmlevel = $("select[name='alarmlevel']").val();
		var alarmobject = $("select[name='alarmobject']").val();
		var id = $("input[name='id']").val();
		var hour = $("input[name='hour']").val();
		var count = $("input[name='count']").val();
		var temperature = $("input[name='temperature']").val();
		var timedelay = $("input[name='timedelay']").val();
		var lightpower = $("input[name='lightpower']").val();
		var packetloss = $("input[name='packetloss']").val();
		if (alarmname == "") {
			alert("请选择告警名称！");
			return false;
		}
		if (alarmcode == "") {
			alert("请选择告警代码！");
			return false;
		}
		if (alarmlevel == "") {
			alert("请选择告警级别！");
			return false;
		}
		if (alarmobject == "") {
			alert("请选择派单对象！");
			return false;
		}
		if (confirm("确定要修改该条信息！")) {
			var url = "<s:url value='/ids/AlarmQuery!update.action' />";
			$.post(url, {
				id : id,
				alarmname : alarmname,
				alarmname : alarmname,
				alarmcode : alarmcode,
				alarmlevel : alarmlevel,
				alarmobject : alarmobject,
				hour : hour,
				count : count,
				temperature : temperature,
				timedelay : timedelay,
				lightpower : lightpower,
				packetloss : packetloss
			});
			query();
			$("button[@id='btn1']").css("display", "");
			$("button[@id='btn2']").css("display", "none");
			return true;
		} else {
			return false;
		}
	}

	function clean() {
		$("select[name='alarmname'] option[value='']").attr("selected",
				"selected");
		$("select[name='alarmcode'] option[value='']").attr("selected",
				"selected");
		$("select[name='alarmlevel'] option[value='']").attr("selected",
				"selected");
		$("select[name='alarmobject'] option[value='']").attr("selected",
				"selected");
		$("input[name='hour']").val("");
		$("input[name='count']").val("");
		$("input[name='temperature']").val("");
		$("input[name='lightpower']").val("");
		$("input[name='timedelay']").val("");
		$("input[name='packetloss']").val("");
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/ids/AlarmQuery!getIdsarmInfoListExcel.action' />";
		mainForm.submit();
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="selectForm" name="selectForm" action="" target="dataForm">
		<table>
			<input name="id" type="hidden" value="">
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								预检预修告警查询引擎</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />告警次数（同一条告警）, N个小时M次告警（N,M可配置）</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">预检预修告警信息设计查询条件</th>
						</tr>

						<TR>
							<TD class="column" width='15%' align="right">告警名称</TD>
							<TD width="35%"><select name="alarmname" class="bk">
									<option value="" selected="selected">==请选择==</option>
									<option value="11">语音注册失败</option>
									<option value="22">光路劣化</option>
									<option value="33">PPPOE拨号失败</option>
									<option value="44">终端环境</option>
									<option value="55">网络质量</option>
									<option value="66">网络综合质量</option>
									<option value="77">光路劣化关联</option>
							</select></TD>
							<TD class="column" width='15%' align="right">告警代码</td>
							<TD width="35%"><select name="alarmcode" class="bk">
									<option value="" selected="selected">==请选择==</option>
									<option value="500001">500001</option>
									<option value="500002">500002</option>
									<option value="500003">500003</option>
									<option value="500004">500004</option>
									<option value="500005">500005</option>
									<option value="500006">500006</option>
									<option value="500007">500007</option>
							</select></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">告警级别</TD>
							<TD width="35%"><select name="alarmlevel" class="bk">
									<option value="" selected="selected">==请选择==</option>
									<option value="10">紧急</option>
									<option value="20">重要</option>
									<option value="30">次要</option>
							</select></TD>
							<TD class="column" width='15%' align="right">派单对象</td>
							<TD width="35%"><select name="alarmobject" class="bk">
									<option value="" selected="selected">==请选择==</option>
									<option value="1">客户类</option>
									<option value="2">网络类</option>
							</select></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">N个小时</TD>
							<TD width="35%"><input type="text" name="hour" size="20"
								maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">M次</TD>
							<TD width='35%' align="left"><input type="text" name="count"
								size="20" maxlength="30" class=bk /></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">温度(大于等于)</TD>
							<TD width="35%"><input type="text" name="temperature"
								size="20" maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">光功率(小于等于)</TD>
							<TD width='35%' align="left"><input type="text"
								name="lightpower" size="20" maxlength="30" class=bk /></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">时延(大于等于)</TD>
							<TD width="35%"><input type="text" name="timedelay"
								size="20" maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">丢包率(大于等于)</TD>
							<TD width='35%' align="left"><input type="text"
								name="packetloss" size="20" maxlength="30" class=bk /></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查&nbsp询&nbsp;</button>
								<button style="display:;" id="btn1" onclick="insert()">&nbsp;保&nbsp存&nbsp;</button>
								<button style="display: none;" id="btn2" onclick="update()">&nbsp;保&nbsp存&nbsp;</button>
								<button onclick="clean()">&nbsp;清&nbsp空&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
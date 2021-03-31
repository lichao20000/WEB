<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ԥ��Ԥ�޸澯��ѯ����</title>
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
			alert("��ѡ��澯���ƣ�");
			return false;
		}
		if (alarmcode == "") {
			alert("��ѡ��澯���룡");
			return false;
		}
		if (alarmlevel == "") {
			alert("��ѡ��澯����");
			return false;
		}
		if (alarmobject == "") {
			alert("��ѡ���ɵ�����");
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
		if (confirm("ȷ��Ҫɾ��������Ϣ��")) {
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
		if (alarm_name == "����ע��ʧ��") {
			alarm_name = "11";
		} else if (alarm_name == "��·�ӻ�") {
			alarm_name = "22";
		} else if (alarm_name == "PPPOE����ʧ��") {
			alarm_name = "33";
		} else if (alarm_name == "������֪�澯") {
			alarm_name = "44";
		} else if (alarm_name == "���������澯") {
			alarm_name = "55";
		} else if (alarm_name == "�����������������澯") {
			alarm_name = "66";
		} else if (alarm_name == "��·�ӻ���������") {
			alarm_name = "77";
		} else {
			alarm_name = "";
		}

		if (alarm_level == "����") {
			alarm_level = "10";
		} else if (alarm_level == "��Ҫ") {
			alarm_level = "20";
		} else if (alarm_level == "��Ҫ") {
			alarm_level = "30";
		} else {
			alarm_level = "";
		}

		if (send_sheet_obj == "�ͻ���") {
			send_sheet_obj = "1";
		} else if (send_sheet_obj == "������") {
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
			alert("��ѡ��澯���ƣ�");
			return false;
		}
		if (alarmcode == "") {
			alert("��ѡ��澯���룡");
			return false;
		}
		if (alarmlevel == "") {
			alert("��ѡ��澯����");
			return false;
		}
		if (alarmobject == "") {
			alert("��ѡ���ɵ�����");
			return false;
		}
		if (confirm("ȷ��Ҫ�޸ĸ�����Ϣ��")) {
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

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ]

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
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
								Ԥ��Ԥ�޸澯��ѯ����</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />�澯������ͬһ���澯��, N��СʱM�θ澯��N,M�����ã�</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">Ԥ��Ԥ�޸澯��Ϣ��Ʋ�ѯ����</th>
						</tr>

						<TR>
							<TD class="column" width='15%' align="right">�澯����</TD>
							<TD width="35%"><select name="alarmname" class="bk">
									<option value="" selected="selected">==��ѡ��==</option>
									<option value="11">����ע��ʧ��</option>
									<option value="22">��·�ӻ�</option>
									<option value="33">PPPOE����ʧ��</option>
									<option value="44">�ն˻���</option>
									<option value="55">��������</option>
									<option value="66">�����ۺ�����</option>
									<option value="77">��·�ӻ�����</option>
							</select></TD>
							<TD class="column" width='15%' align="right">�澯����</td>
							<TD width="35%"><select name="alarmcode" class="bk">
									<option value="" selected="selected">==��ѡ��==</option>
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
							<TD class="column" width='15%' align="right">�澯����</TD>
							<TD width="35%"><select name="alarmlevel" class="bk">
									<option value="" selected="selected">==��ѡ��==</option>
									<option value="10">����</option>
									<option value="20">��Ҫ</option>
									<option value="30">��Ҫ</option>
							</select></TD>
							<TD class="column" width='15%' align="right">�ɵ�����</td>
							<TD width="35%"><select name="alarmobject" class="bk">
									<option value="" selected="selected">==��ѡ��==</option>
									<option value="1">�ͻ���</option>
									<option value="2">������</option>
							</select></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">N��Сʱ</TD>
							<TD width="35%"><input type="text" name="hour" size="20"
								maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">M��</TD>
							<TD width='35%' align="left"><input type="text" name="count"
								size="20" maxlength="30" class=bk /></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">�¶�(���ڵ���)</TD>
							<TD width="35%"><input type="text" name="temperature"
								size="20" maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">�⹦��(С�ڵ���)</TD>
							<TD width='35%' align="left"><input type="text"
								name="lightpower" size="20" maxlength="30" class=bk /></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">ʱ��(���ڵ���)</TD>
							<TD width="35%"><input type="text" name="timedelay"
								size="20" maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">������(���ڵ���)</TD>
							<TD width='35%' align="left"><input type="text"
								name="packetloss" size="20" maxlength="30" class=bk /></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;��&nbspѯ&nbsp;</button>
								<button style="display:;" id="btn1" onclick="insert()">&nbsp;��&nbsp��&nbsp;</button>
								<button style="display: none;" id="btn2" onclick="update()">&nbsp;��&nbsp��&nbsp;</button>
								<button onclick="clean()">&nbsp;��&nbsp��&nbsp;</button>
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
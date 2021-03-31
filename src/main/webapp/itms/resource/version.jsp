<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�汾ͳ�Ʋ�ѯ</title>
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
		document.selectForm.submit();
	}
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];
	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";
	function dyniframesize() {
		var dyniframe = new Array();
		for ( var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
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
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}

	$(function() {
		dyniframesize();
		Init();
	});

	$(window).resize(function() {
		dyniframesize();
	});

	function Init() {
		gwShare_change_select("vendor", "-1");
	}

	function gwShare_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value='/itms/resource/gwDeviceQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/itms/resource/gwDeviceQuery!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendor']").val();
			$("select[@name='devicetype']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			if("-1"==vendorId){
				$("select[@name='devicemodel']").html("<option value='-1'>==����ѡ���豸����==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='devicemodel']"),selectvalue);
			});
			break;
		}		
	}

	function gwShare_parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");

		option = "<option value='-1' selected>==��ѡ��==</option>";
		field.append(option);
		for ( var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if (selectvalue == xValue) {
				flag = false;
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"' selected>==" + xText
						+ "==</option>";
			} else {
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"'>==" + xText
						+ "==</option>";
			}
			try {
				field.append(option);
			} catch (e) {
				alert("�豸�ͺż���ʧ�ܣ�");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}

	function gaoji(val) {
		var tempVal = $("input[name='temval']").val();
		if (1 == val) {
			$("input[name='temval']").val(val);
			$("button[name='gaojibtn1']").css("display", "none");
			$("button[name='jiandan']").css("display", "");
			$("tr[@id='gaoji1']").css("display", "");
			$("tr[@id='gaoji2']").css("display", "");
		}
		if (2 == val) {
			$("input[name='temval']").val(val);
			$("button[name='jiandan']").css("display", "none");
			$("button[name='gaojibtn1']").css("display", "");
			$("tr[@id='gaoji1']").css("display", "none");
			$("tr[@id='gaoji2']").css("display", "none");
		}
	}

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/VersionQuery!queryVersionDevExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/VersionQuery!queryVersionDev.action' />";
	}

	function OpenDetail(softwareversion) {
		var vendor = $.trim($("select[@name='vendor']").val());
		var devicemodel = $.trim($("select[@name='devicemodel']").val());
		var versionSpecification = $.trim($(
				"select[@name='versionSpecification']").val());
		var specName = $.trim($("select[@name='specName']").val());
		var deviceType = $.trim($("select[@name='deviceType']").val());
		var accessWay = $.trim($("select[@name='accessWay']").val());
		var voiceAgreement = $.trim($("select[@name='voiceAgreement']").val());
		var zeroconf = $.trim($("select[@name='zeroconf']").val());
		var mbbndwidth = $.trim($("select[@name='mbbndwidth']").val());
		var ipvsix = $.trim($("select[@name='ipvsix']").val());
		var temval = $.trim($("input[@name='temval']").val());
		var page = "<s:url value='/itms/resource/VersionQuery!queryDetail.action'/>?vendor="
				+ vendor
				+ "&devicemodel="
				+ devicemodel
				+ "&versionSpecification="
				+ versionSpecification
				+ "&specName="
				+ specName
				+ "&deviceType="
				+ deviceType
				+ "&accessWay="
				+ accessWay
				+ "&voiceAgreement="
				+ voiceAgreement
				+ "&zeroconf="
				+ zeroconf
				+ "&mbbndwidth="
				+ mbbndwidth
				+ "&ipvsix="
				+ ipvsix
				+ "&temval="
				+ temval
				+ "&softwareversion=" + softwareversion;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/VersionQuery!queryVersionDev.action'/>"
		target="dataForm">
		<input type="hidden" name="temval" value="2" />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								�汾ͳ�Ʋ�ѯ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> �汾ͳ�Ʋ�ѯ</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">�汾ͳ�Ʋ�ѯ</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>�豸����</TD>
							<TD width="35%"><select name="vendor" class="bk"
								onchange="gwShare_change_select('deviceModel','-1')">
							</select></TD>

							<TD class=column width="15%" align='right'>�豸�ͺ�</TD>
							<TD width="35%"><select name="devicemodel" class="bk"
								onchange="gwShare_change_select('devicetype','-1')">
									<option value="">==��ѡ����==</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>�汾�淶</TD>
							<TD width="35%"><select name="versionSpecification"
								class="bk">
									<option value="">===��ѡ��===</option>
									<option value="0">��</option>
									<option value="1">��</option>
									<option value="1">��</option>
									<option value="0">��</option>
							</select></TD>
							<TD class=column width="15%" align='right'>�ն˹��</td>
							<TD width="35%"><select name="specName" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="1">��ͥ����</option>
									<option value="2">��������</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>�豸����</td>
							<TD width="35%"><select name="deviceType" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="2">E8C</option>
									<option value="1">E8B</option>
							</select></TD>
							<TD class=column width="15%" align='right'>���뷽ʽ</td>
							<TD width="35%"><select name="accessWay" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="1">ADSL</option>
									<option value="2">LAN</option>
									<option value="3">EPON</option>
									<option value="4">GPON</option>
							</select></TD>
						</TR>
						<TR id="gaoji1" style="display: none;">
							<TD class=column width="15%" align='right'>����Э��</td>
							<TD width="35%"><select name="voiceAgreement" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="1">����SIP</option>
									<option value="2">IMS SIP</option>
									<option value="3">H.248</option>
							</select></TD>
							<TD class=column width="15%" align='right'>������������</td>
							<TD width="35%"><select name="zeroconf" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="1">֧��</option>
									<option value="2">��֧��</option>

							</select></TD>
						</TR>
						<TR id="gaoji2" style="display: none;">
							<TD class=column width="15%" align='right'>���״���</td>
							<TD width="35%"><select name="mbbndwidth" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="1">֧��</option>
									<option value="2">��֧��</option>
							</select></TD>
							<TD class=column width="15%" align='right'>IPv6</td>
							<TD width="35%"><select name="ipvsix" class="bk">
									<option value="">===��ѡ��===</option>
									<option value="1">֧��</option>
									<option value="2">��֧��</option>
							</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="gaoji('1')" id="button" name="gaojibtn1">��
									�� �� ѯ</button>
								<button onclick="gaoji('2')" id="button" name="jiandan"
									style="display: none;">�� �� �� ѯ</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button onclick="query()" id="button" name="button">�� ѯ
								</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td height="80%"><iframe id="dataForm" name="dataForm"
						height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
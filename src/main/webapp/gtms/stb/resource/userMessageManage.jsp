<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���Ϣ����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
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
	function add() {
		var url = "<s:url value='/gtms/stb/resource/userMessage!time.action'/>";
		window
				.open(url, "",
						"left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	$(function() {
		//setValue();
		dyniframesize();
	});
	$(function() {
		gwShare_change("city", "-1");
	});
	function gwShare_change(type, selectvalue) {
		switch (type) {
		case "city":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNextChild.action'/>";
			$.post(url, {}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='gwShare_cityId']"),
						selectvalue);
				$("select[@name='cityId']").html(
						"<option value='-1'>==����ѡ������==</option>");
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
			var cityId = $("select[@name='gwShare_cityId']").val();
			if ("-1" == cityId) {
				$("select[@name='gwShare_cityId']").html(
						"<option value='-1'>==����ѡ������==</option>");
				break;
			}
			$.post(url, {
				citynext : cityId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='citynext']"),
						selectvalue);
				$("select[@name='cityId']").html(
						"<option value='-1'>==����ѡ������==</option>");
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
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
	$(window).resize(function() {
		dyniframesize();
	});

	function query() {
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
		$("button[@name='button']").attr("disabled", true);
		var cityId = $.trim($("select[@name='city_id']").val());
		document.frm.submit();

	}
</script>
</head>

<body>
	<form name="frm" id="frm" method="post"
		action="<s:url value='/gtms/stb/resource/userMessage!query.action'/>"
		target="dataForm">

		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								�û���Ϣ����</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ��ʼʱ��ͽ���ʱ��Ϊ�û�����������<font color="red">*</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">�û���Ϣ��ѯ</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">ҵ���˺ţ�</TD>
							<TD align="left" width="35%"><INPUT TYPE="text"
								NAME="servaccount"  id="servaccount" maxlength=30 class=bk size=20></TD>
							<TD align="right" class=column width="15%">ƽ̨���ͣ�</TD>
							<TD width="35%" nowrap><s:select list="platformTypeList"
									name="platformType" headerKey="-1" headerValue="��ѡ��ƽ̨����"
									listKey="platform_id" listValue="platform_name" cssClass="bk"></s:select></TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">MAC��ַ��</TD>
							<TD align="left" width="35%"><INPUT TYPE="text" NAME="MAC"
								maxlength=30 class=bk size=20></TD>
							<TD align="right" class=column width="15%">�û����飺</TD>
							<TD width="35%" nowrap><s:select list="userGroupIDList"
									name="userGroupID" headerKey="-1" headerValue="��ѡ���û�����"
									listKey="group_id" listValue="group_name" cssClass="bk"></s:select></TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class="column" width="15%">�� ��</TD>
							<TD align="left" width="35%"><select name="gwShare_cityId" id="gwShare_cityId"
								class="bk" onchange="gwShare_change('cityid','-1')">
									<option value="-1">==��ѡ��==</option>
							</select></TD>
							<TD align="right" class="column" width="15%">�¼��� ��</TD>
							<TD align="left" width="35%"><select name="citynext" 
								class="bk">
									<option value="-1">����ѡ������</option>
							</select></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%"><input type="text" name="starttime" readonly
								value="<s:property value='starttime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" /></TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%"><input type="text" name="Invalidtime" readonly
								value="<s:property value='endtime'/>"> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" /></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>�����˺�</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="stbuser"
								maxlength=30 class=bk size=20></TD>
								<TD class=column width="15%" align='right'>�ֻ�����</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="iptvBindPhone"
								maxlength=30 class=bk size=20></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class="foot" width="100%">
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
								<!-- <button onclick="add()">&nbsp;�� ��&nbsp;</button> -->
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
		</table>
		<br>
	</form>
</body>
</html>

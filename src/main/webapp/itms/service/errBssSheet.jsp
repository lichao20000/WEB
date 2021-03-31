<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����BSS����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		var _reciveDateStart = $("input[@name='reciveDateStart']").val();
		var _reciveDateEnd = $("input[@name='reciveDateEnd']").val();
		//var _cityId = $("select[@name='cityId']").val();
		var _username = $("input[@name='username']").val();
		var _resultSheet = $("select[@name='resultSheet']").val();
		var _sheetType = $("select[@name='sheetType']").val();
		$("td[@id='sheetContent']").hide();
		var cityId = $.trim($("select[@name='cityId']").val());
		if (cityId == "-1") {
			alert("��ѡ������");
			return false;
		}
		document.selectForm.submit();
		/**var url = "<s:url value='/itms/service/errorSheet!query.action'/>";
		$.post(url, {
			reciveDateStart : _reciveDateStart,
			reciveDateEnd : _reciveDateEnd,
			username : _username,
			resultSheet : _resultSheet,
			sheetType : _sheetType,
			cityId : cityId
		}, function(mesg) {
			$("td[@id='sheetList']").html(mesg);
		}); **/
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
						
						{
							dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
						}

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
		//setValue();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
<form name="selectForm"
	action="<s:url value='/itms/service/errorSheet!query.action'/>"
	target="dataForm"><input type="hidden" name="selectType"
	value="0" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
				����BSS������ѯ</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12" /> ��ѯ����BSS�������</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<TR>
				<th colspan="4">����BSS����</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">���ܿ�ʼʱ��</TD>
				<TD width="30%"><input type="text" name="reciveDateStart"
					value='<s:property value="reciveDateStart" />' readonly class=bk>
				<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.reciveDateStart,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></TD>

				<TD class=column align="right" width="20%">���ܽ���ʱ��</TD>
				<TD width="30%"><input type="text" name="reciveDateEnd"
					value='<s:property value="reciveDateEnd" />' readonly class=bk>
				<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.reciveDateEnd,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">����</TD>
				<TD width="30%"><s:select list="cityList" name="cityId"
					headerKey="-1" headerValue="��ѡ������" listKey="city_id"
					listValue="city_name" value="cityId" cssClass="bk"></s:select></TD>
				<TD class=column align="right" nowrap>LOID</TD>
				<TD><INPUT TYPE="text" NAME="username" maxlength=50 class=bk
					value="">
				</div>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">��������</TD>
				<TD width="30%"><select name="sheetType" class=bk>
					<option value="-1">==��ѡ��ҵ������==</option>
					<option value="20">��������</option>
					<option value="22">����ҵ��</option>
					<option value="21">IPTVҵ��</option>
					<option value="14">VOIPҵ��</option>
				</select></TD>
				<TD class=column align="right" nowrap>�ص����</TD>
				<TD><select name="resultSheet" class=bk>
					<option value="-1">==��ѡ��ص����==</option>
					<option value="1">�޴�ҵ������</option>
					<option value="2">�޴˲�������</option>
					<option value="3">�ֶ���Ŀ����</option>
					<option value="4">ҵ������ʱ�䲻��</option>
					<option value="5">�޴��豸</option>
					<option value="6">�޴��û�</option>
					<option value="7">���ز���</option>
					<option value="8">�ʺ��ظ�</option>
					<option value="9">���ȱ��LINKGE</option>
					<option value="10">���򲻶�</option>
					<option value="11">���뷽ʽ����</option>
					<option value="12">VlanID�Ų���</option>
					<option value="13">�ն����Ͳ���</option>
					<option value="31">IPTV�ն˸�������ȷ</option>
					<option value="32">��ͨ�˿ڲ���ȷ</option>
					<option value="33">IPTV��������˺Ų��Ϸ�</option>
					<option value="41">Voip��֤�˻����Ϸ�</option>
					<option value="42">Voip��֤���벻�Ϸ�</option>
					<option value="43">Sip��������ַ���Ϸ�</option>
					<option value="44">Sip�������˿ڲ��Ϸ�</option>
					<option value="45">����Sip��������ַ���Ϸ�</option>
					<option value="46">����Sip�������˿ڲ��Ϸ�</option>
					<option value="47">Voip��·�˿ڲ��Ϸ�</option>
					<option value="48">VOIP�߼��绰���벻�Ϸ�</option>
					<option value="51">LOID���Ϸ�</option>
					<option value="52">LOID�Ѿ�����</option>
					<option value="53">LOID�����ڣ������߽�������</option>
				</select></TD>
			</TR>
			<TR>
				<td colspan="4" align="right" class=foot>
				<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
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

		<!-- <td id="sheetList"></td> -->
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
	<tr>
		<td id="sheetContent"></td>
	</tr>
</table>
<br>
</form>
</body>
</html>
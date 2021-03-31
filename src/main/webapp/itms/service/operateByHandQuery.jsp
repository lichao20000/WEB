<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ֹ�������־��ѯ</title>
<%
	/**
	 * ITMS�ֹ�������־��ѯ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-05-15
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {

		document.selectForm.submit();
	}

	function ToExcel() {
		var starttime = $("input[@name='startOpenDate']").val();
		var endtime = $("input[@name='endOpenDate']").val();
		var city_id = $("select[@name='city_id']").val();
		var servType = $("select[@name='servType']").val();
		var username = Trim($("input[@name='username']").val());
		var resultType = $("select[@name='resultType']").val();

		var url = "<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfoCount.action'/>";
		$.post(url, {starttime : starttime,
					 endtime : endtime,
					 city_id : city_id,
					 servType : servType,
					 username : username,
					 resultType : resultType
					},
						function(ajax) {
							var total = parseInt(ajax);
							if (ajax > 100000) {
								alert("������̫��֧�ֵ��� ");
								return;
							} else {
								
								var mainForm = document
										.getElementById("selectForm");
								mainForm.action = "<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfoExcel.action'/>"
								mainForm.submit();
								mainForm.action = "<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfo.action'/>"
							}
						});
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
	<form id="form" name="selectForm"
		action="<s:url value='/gtms/service/operateByHandQuery!getOperateByHandInfo.action'/>"
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
								�ֹ�����������־</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ʱ��Ϊ����ʱ��</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">ҵ��һ���·��ɹ���ͳ��</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
						</TR>


						<TR>

							<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>

							<TD class="column" width='15%' align="right">��������</td>
							<td width='35%' align="left"><select name="servType"
								class="bk">
									<option value="-1"
										<s:property value='"-1".equals(servType)?"selected":""'/>>
										==��ѡ��==</option>
									<option value="20"
										<s:property value='"20".equals(servType)?"selected":""'/>>
										==�û�����==</option>
									<option value="10"
										<s:property value='"10".equals(servType)?"selected":""'/>>
										==���ҵ��==</option>
									<option value="11"
										<s:property value='"11".equals(servType)?"selected":""'/>>
										==IPTVҵ��==</option>
									<option value="14"
										<s:property value='"14".equals(servType)?"selected":""'/>>
										==VOIPҵ��==</option>
							</select></TD>

						</TR>
						<TR>
							<TD class="column" width='15%' align="right">������</TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">ִ�н��</TD>
							<TD width='35%' align="left" id="allOrderType"><select
								name="resultType" class="bk">
									<option value="-1"
										<s:property value='"-1".equals(resultType)?"selected":""'/>>
										==��ѡ��==</option>
									<option value="1"
										<s:property value='"1".equals(resultType)?"selected":""'/>>
										==�ɹ�==</option>
									<option value="0"
										<s:property value='"0".equals(resultType)?"selected":""'/>>
										==ʧ��==</option>
							</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;ͳ��&nbsp;</button>
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
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Ԥ��Ԥ�޸澯��Ϣ</title>
<%
	/**
	 *  Ԥ��Ԥ�޸澯��Ϣ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
	 * @category
	 */
%>
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

	function ToExcel() {
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/ids/IdsAlarmInfo!getIdsarmInfoListExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/ids/IdsAlarmInfo!getIdsarmInfoList.action' />";
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

<body >
	<form id="form" name="selectForm"
		action="<s:url value='/ids/IdsAlarmInfo!getIdsarmInfoList.action'/>"
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
								Ԥ��Ԥ�޸澯��Ϣ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
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
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0"
								alt="ѡ��"></TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12" border="0"
								alt="ѡ��"></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">�豸���к�</TD>
							<TD width="35%"><input type="text" name="device_id"
								size="20" maxlength="30" class=bk /></TD>

							<TD class="column" width='15%' align="right">LOID</TD>
							<TD width='35%' align="left"><input type="text" name="loid"
								size="20" maxlength="30" class=bk /></TD>
						</TR>

						<TR>

							<TD class="column" width='15%' align="right">�澯����</TD>
							<TD width="35%"><input type="text" name="alarm_count"
								size="20" maxlength="30" class=bk /></TD>
							<TD class="column" width='15%' align="right">�澯����</td>
							<TD width="35%"><select name="alarm_code" class="bk">
									<option value="" selected="selected">==��ѡ��==</option>
									<%--
									<option value="1001">����ע��ʧ��</option>
									<option value="1002">��·�ӻ�</option>
									<option value="1003">PPPOE����ʧ��</option>
									<option value="1004">������֪�澯</option>
									<option value="1005">���������澯</option>
									<option value="1006">������������</option>--%>
									<option value="500001">����ע��ʧ��</option>
									<option value="500002">��·�ӻ�</option>
									<option value="500003">PPPOE����ʧ��</option>
									<option value="500004">�ն˻���</option>
									<option value="500005">��������</option>
									<option value="500006">�����ۺ�����</option>
									<option value="500007">��·�ӻ�����</option>
							</select></TD>
						</TR>
						<TR>

							<TD class=column width="15%" align='right'>�澯���</TD>
							<td width='35%' align="left"><SELECT name="alarm_type"
								class="bk">
									<%--<option value="-1" selected="selected">==��ѡ��==</option> --%>
									<option value="1" selected="selected">==��ǰ�澯==</option>
									<option value="2">==��ʷ�澯==</option>
							</SELECT></td>
							<TD class="column" width='15%' align="right">�澯���</TD>
							<TD width='35%' align="left"><SELECT name="is_release"
								class="bk">
									<option value="-1" selected="selected">==��ѡ��==</option>
									<option value="1">==��==</option>
									<option value="0">==��==</option>
							</SELECT></TD>
						</TR>
						<!-- <TR>
							<TD class="column" width='15%' align="right">�澯Ԥ���</TD>
							<TD width="35%"><SELECT name="is_pre_release" class="bk">
									<option value="-1" selected="selected">==��ѡ��==</option>
									<option value="1">==��==</option>
									<option value="0">==��==</option>
							</SELECT></TD>
							<TD class="column" width='15%' align="right">�Ƿ��ɵ�</TD>
							<TD width='35%' align="left"><SELECT name="is_send_sheet"
								class="bk">
									<option value="-1" selected="selected">==��ѡ��==</option>
									<option value="1">==��==</option>
									<option value="0">==��==</option>
							</SELECT></TD>

						</TR> -->
						<TR>
							<TD class=column width="15%" align='right'>����</TD>
							<TD colspan="3" width="35%"><s:select list="cityList"
									name="city_id" headerKey="-1" headerValue="��ѡ������"
									listKey="city_id" listValue="city_name" cssClass="bk"></s:select></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;��&nbspѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td >
					<div style="overflow:scroll;">
					<iframe id="dataForm" name="dataForm" height="0" 
						frameborder="0"  width="100%" src="" style="overflow:scroll;overflow-x:hidden"></iframe>
					</div>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
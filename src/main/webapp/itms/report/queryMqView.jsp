<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>Mq����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"/>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"/>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	function queryMqList() {
		var mqId = $("select[@name='mqId']").val();
		var topicName = $.trim($("input[@name='topicName']").val());
		var frm = document.getElementById("frm");
		frm.action = "<s:url value='/itms/report/queryMq!getMqListByMq.action'/>";
		frm.submit();
	}

	function getDetail(mqId,topicName,gathertime,starttime,endtime){
		var page="<s:url value='/itms/report/queryMq!getMqDetail.action'/>?"
			+ "mqId=" + mqId 
			+ "&topicName=" +topicName
			+ "&gathertime=" +gathertime
			+ "&starttime=" +starttime
			+ "&endtime=" +endtime;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<TABLE>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<th>MQ���</th>
						<td align="left"><img
							src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<form name="frm" id="frm" target="dataForm">
					<table class="querytable">
						<tr>
							<th colspan=4>MQ��ѯ</th>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align="center" width="15%">��ʼʱ��</td>
							<td width="35%"><input type="text" name="starttime"
								class='bk' readonly value="<s:property value='starttime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" /></td>
							<td class=column align="center" width="15%">����ʱ��</td>
							<td width="35%"><input type="text" name="endtime"
								class='bk' readonly value="<s:property value='endtime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" /></td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align="center" width="15%">MQ�豸</td>
							<td width="35%"><s:select list="mqList" name="mqId"
									headerKey="-1" headerValue="ȫ��" listKey="id"
									listValue="mq_ipaddress" cssClass="select" style="width:120px;"></s:select>
							</td>
							<td class=column align="center" width="15%">MQ����</td>
							<td width="35%"><input type="text" name="topicName"/></td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=foot colspan=4 align=right>
								<button id="queryButton" onclick="queryMqList();">&nbsp;��ѯ&nbsp;</button>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</TABLE>
	<div class="content">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
</body>
<%@ include file="/foot.jsp"%>
</html>

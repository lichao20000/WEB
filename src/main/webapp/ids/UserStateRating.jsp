<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���ǰ���绷������</title>
<link rel="stylesheet" href="../css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../css3/global.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">


$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}
	function query() {
		document.selectForm.action="<s:url value='/ids/UserStateRating!queryPonStatusByES.action'/>";
		document.selectForm.submit();
		
	}
</script>
</head>
</html>
<body>
	<form id="form" name="selectForm" method="post" action="" target="dataForm">
		<input type="hidden" id="ctype" value="1">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<th>�û�����״̬����</th>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<tr leaf="simple">
							<td align="right" width="10%" class="column">��ʼʱ��</td>
							<td width="25%"><input type="text" name="startTime" readonly
								class=bk value="<s:property value="startDate" />"> <img name="shortdateimg"
								onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" height="12" border="0" alt="ѡ��" />
							</td>
							<td align="right" width="10%" class="column">����ʱ��</td>
							<td width="25%"><input type="text" name="endTime" readonly
								class=bk value="<s:property value="endDate" />"> <img name="shortdateimg"
								onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" height="12" border="0" alt="ѡ��" />
							</td>
							<td align="right" width="10%" class="column">��ѯʱ������</td>
							<td width="20%"><select id="queryTimeType" name="quertTimeType">
									<option value="0">==��ѡ��==</option>
									<option value="1" selected="selected">==�ϱ�ʱ��==</option>
									<option value="2">==���ʱ��==</option>
							</select></td>
						</tr>
						<tr leaf="simple">
							<td align="right" class="column">�豸���к�</td>
							<td><input type="text" name="deviceSerialnumber" class=bk></td>
							<td align="right" class="column">LOID</td>
							<td><input type="text" name="loid" class=bk></td>
						</tr>
						<tr>
							<td colspan="6" align="right" class=foot>
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
						scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../foot.jsp"%>
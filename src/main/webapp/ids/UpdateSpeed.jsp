
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������޸�</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript">

		function updateAcc(){
			var devSn=$.trim($("#devSn").val());
			var divValue=$.trim($("#divId").html());
			var netSpeed=$.trim($("#netSpeed").val());
			
			if(devSn==''){
				alert('���������豸���кŽ��в�ѯ���ٽ����޸�');
				return false;
			}
			if(divValue=='û�в�ѯ���'){
				alert('���豸�����ʱ���û�в�ѯ������������޸�');
				return false;
			}
			if(divValue==''){
				alert('���Ȳ�ѯ���豸�Ƿ������ʱ��д��ڣ��ٽ����޸�');
				return false;
			}
			if(netSpeed=='-1'){
				alert('����ѡ�����ʣ��ٽ����޸�');
				return false;
			}
			
			var url = "<s:url value='/ids/addSimSpeedAcc!updateRate.action'/>";
			$.post(url, {
             	devSn:devSn,
             	netRate:netSpeed
             }, function(ajax) {
            	$("#divId").html('');
             	if(ajax=='-1'){
            		alert("�޸�����ʧ��");
            	}else{
            		alert("�޸����ʳɹ�");
            	}
             });
        }
        
        function searchAcc(){
        	 var devSn=$.trim($("#devSn").val());
        	 if(devSn=='' || $.trim(devSn).length<6){
        		 alert('�����벻С����λ���豸���к�');
        		 return false;
        	 }
        	 var url = "<s:url value='/ids/addSimSpeedAcc!searchAcc.action'/>";
             $.post(url, {
             	devSn:devSn
             }, function(ajax) {
             	if(ajax==''){
            		alert("û�в�ѯ���");
            		$("#divId").html('û�в�ѯ���')
            		return false;
            	}else if(ajax=='toManyResults'){
            		alert("��ѯ��������Ϣ,�����������豸���кŽ��в�ѯ");
        			$("#divId").html('')
        		return false;
        		}
             	var resArr = ajax.split("##");
             	$("#divId").html("��ǰ���ʣ�"+resArr[0]);
             	$("#devSn").val(resArr[1]);
             });
        }       
        
        function clearDivValue(){
        	$("#divId").html('');
        }
        </script>
</head>

<body>
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							���������޸�</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> ���������޸�</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">���������޸�</th>
					</tr>
					<TR>
						<td class="column" width='15%' align="right">�豸���к�</td>
						<TD width="35%">
						<input id="devSn" value="" maxlength="40" onfocus="clearDivValue()"/>
						<div id='divId'></div>
						</TD>
						<td class="column" width='15%' align="right">��������</td>
						<TD width="35%"><s:select list="netRateList" id="netSpeed"
								listKey="rate" listValue="rate" headerKey="-1" headerValue="��ѡ��"
								cssClass="bk" onchange="queryAccount()"></s:select> (M)</TD>
					</tr>
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="searchAcc()">&nbsp;�� ѯ&nbsp;</button>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="updateAcc()">&nbsp;�� ��&nbsp;</button>
						</td>
					</TR>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
<%@ include file="../foot.jsp"%>
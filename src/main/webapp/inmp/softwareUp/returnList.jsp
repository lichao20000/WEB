<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�汾��ѯ</title>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});

var returnMap = "<s:property value='returnMap' />";
var return_type = "<s:property value='return_type' />";
function refresh(){
	var refreshUrl = "<s:url value='/gtms/config/qoeFunctionAct!refrsehMsg.action'/>";
	$("#refreshBtn").attr("disabled",true);
	var device_Id = $("input[@name='device_Id']").val();
	$.post(
	         refreshUrl,{
	         deviceIds : device_Id
	        } ,function(ajax){
	        	var TestDownloadStatus = ajax.split(",")[0];
	        	if("Success"==TestDownloadStatus){
							TestDownloadStatus="�ɹ�";
						}else if("Fail"==TestDownloadStatus){
							TestDownloadStatus="ʧ��";
						}else if("Stop"==TestDownloadStatus){
							TestDownloadStatus="ֹͣ";
						}else if("Running"==TestDownloadStatus){
							TestDownloadStatus="������";
						}
						var MonitorStatus = ajax.split(",")[1];
						if(""==MonitorStatus){
							MonitorStatus=" ";
						}else if("Success"==MonitorStatus){
							MonitorStatus="�ɹ�";
						}else if("Fail"==MonitorStatus){
							MonitorStatus="ʧ��";
						}else if("Stop"==MonitorStatus){
							MonitorStatus="ֹͣ";
						}else if("Running"==MonitorStatus){
							MonitorStatus="������";
						}
	         		$("#TestDownloadStatus").text(TestDownloadStatus);
							$("#MonitorStatus").text(MonitorStatus);
							$("#refreshBtn").attr("disabled",false);
	       });
}

$(function() {
	window.parent. $("tr[@id='trData']").hide();
	if("single_on" != return_type){
		if("success"==return_type){
			alert("ִ�гɹ�");
		}else if("1"==return_type){
			alert("�豸δ���û�");
		}else if("2"==return_type){
			alert("�ն˰汾��֧��QOE����");
		}else if("-5"==return_type){
			alert("���ú�̨Ԥ��ģ��ʧ�ܣ�");
		}else {
			alert("��ʾ�ڵ��ȡʧ�ܣ���ȷ�ϰ汾�Ƿ�֧��QOE���ܣ�");
		}
		$("#showTable").hide();
	}else{
		$("#showTable").show();
		var sn = "<s:property value='returnMap["sn"]' />";
		var loid = "<s:property value='returnMap["loid"]' />";
		var TestDownloadStatus = "<s:property value='returnMap["TestDownloadStatus"]' />";
		var MonitorStatus = "<s:property value='returnMap["MonitorStatus"]' />";
		var device_Id = "<s:property value='returnMap["deviceId"]' />";
		$("input[@name='device_Id']").val(device_Id);
		if(-1==TestDownloadStatus){
			TestDownloadStatus="�豸���Ӳ��ϣ�";
		}else if(-2==TestDownloadStatus){
			TestDownloadStatus="�豸����Ϊ�գ�";
		}else if(-3==TestDownloadStatus){
			TestDownloadStatus="�豸����������";
		}else if(-4==TestDownloadStatus){
			TestDownloadStatus="δ֪����ԭ��";
		}else if(-6==TestDownloadStatus){
			TestDownloadStatus="��������QOE,���Ժ���ˢ��";
		}
		if("Success"==TestDownloadStatus){
			TestDownloadStatus="�ɹ�";
		}else if("Fail"==TestDownloadStatus){
			TestDownloadStatus="ʧ��";
		}else if("Stop"==TestDownloadStatus){
			TestDownloadStatus="ֹͣ";
		}else if("Running"==TestDownloadStatus){
			TestDownloadStatus="������";
		}
		if(""==MonitorStatus){
			MonitorStatus=" ";
		}else if("Success"==MonitorStatus){
			MonitorStatus="�ɹ�";
		}else if("Fail"==MonitorStatus){
			MonitorStatus="ʧ��";
		}else if("Stop"==MonitorStatus){
			MonitorStatus="ֹͣ";
		}else if("Running"==MonitorStatus){
			MonitorStatus="������";
		}
		
		$("#sn").html("");
		$("#loid").html("");
		$("#TestDownloadStatus").html("");
		$("#MonitorStatus").html("");
		$("#sn").text(sn);
		$("#loid").text(loid);
		$("#TestDownloadStatus").text(TestDownloadStatus);
		$("#MonitorStatus").text(MonitorStatus);
	}
	parent.dyniframesize();
});
</script>
</head>

<body>
	
	<table border=1 cellspacing=0 cellpadding=2 width="100%" id="showTable"
						align="center" style="display:none">
			<tr>
			<th colspan="5" >QOE���ܲ��Խ��</th><input type="hidden" name="device_Id" value="" />
			</tr>
			<tr>
				<th>�豸���к�</th>
				<th>LOID</th>
				<th>QOE�����������״̬���</th>
				<th>QOE�������״̬</th>
				<th>����</th>
			</tr>
			<tr>
				<td id="sn" align="center"></td>
				<td id="loid" align="center"></td>
				<td id="TestDownloadStatus" align="center"></td>
				<td id="MonitorStatus" align="center"></td>
				<td  align="center"> <INPUT TYPE="button" id="refreshBtn" value="ˢ��" class=btn onclick="refresh()"></td>
			</tr>
	</table>
</body>

</html>
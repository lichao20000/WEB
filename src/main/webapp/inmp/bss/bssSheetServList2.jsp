<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���ѯ</title>
<link rel="stylesheet" href="<s:url value="/css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/inmp/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/inmp/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">

	$(function() {
		parent.dyniframesize();
	});
	function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts){
		if(deviceId==""){
			alert("�û�δ���豸�����Ȱ��豸���ټ��");
			return;
		}
		if(confirm('���¼����ǽ���ҵ����Ϊδ��״̬��ȷʵҪ������?')){
			var url = "<s:url value='/inmp/bss/bssSheetServ!callPreProcess.action'/>";
			$.post(url,{
				userId:userId,
				deviceSN:deviceSN,
				deviceId:deviceId,
				servTypeId:servTypeId,
				servstauts:servstauts,
				oui:oui
		    },function(ajax){
		    	if(ajax=="1"){
		    		alert("��Ԥ���ɹ���");
		    		query();
		    	}else if(ajax=="-1"){
		    		alert("����Ϊ�գ�");
		    	}else if(ajax=="-2"){
		    		alert("��Ԥ��ʧ�ܣ�");
		    	}
		    });
			//$("td[@id='temp123']").html("<font color='red'>δ��</font>");
			//$('#resultStr',window.parent.document).html("<font color='red'>02586588146���¼���ɹ���</font>");
		}
	}
	function DetailDevice(device_id){
		var strpage = "<s:url value='/inmp/resource/DeviceShow.jsp'/>?device_id=" + device_id;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	 function GoContent(user_id,gw_type){
		 gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		 if(gw_type=="2"){
		 	var strpage="<s:url value='/inmp/resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }else{
			var strpage="<s:url value='/inmp/resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }
			window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
		}
	function configInfo(userId, oui, deviceSN, deviceId, servTypeId,servstauts, wanType,open_status) {
		//alert("userId"+userId+"oui"+oui+"deviceSN"+deviceSN+"deviceId"+deviceId+"servTypeId"+servTypeId+"servstauts"+servstauts+"wanType"+wanType);
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!getConfigInfo.action'/>";
		$.post(url,{
			userId:userId,
			oui:oui,
			deviceSN:deviceSN,
			deviceId:deviceId,
			servTypeId:servTypeId,
			servstauts:servstauts,
			wanType:wanType,
			openstatus:open_status
	    },function(mesg){
	    	$('#configInfoEm',window.parent.document).show();
	    	$('#configInfo',window.parent.document).show();
	    	$('#configInfo',window.parent.document).html(mesg);
	    });
	}

	function bssSheet(userId, cityId, servTypeId) {
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!getBssSheet.action'/>";
		$.post(url, {
			userId : userId,
			cityId : cityId,
			servTypeId : servTypeId
		}, function(mesg) {
			$('#bssSheetInfo', window.parent.document).show();
			$('#bssSheetInfo', window.parent.document).html(mesg);
		});
	}

	function bssSheet2(username) {
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!getBssSheet2.action'/>";
		$.post(url, {
			username : username
		}, function(mesg) {
			$('#bssSheetInfo', window.parent.document).show();
			$('#bssSheetInfo', window.parent.document).html(mesg);
		});
	}

	function checkdevice(username) {
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!checkdevice.action'/>";
		$.post(url, {
			username : username
		}, function(mesg) {
			//$('#bssSheetInfo',window.parent.document).show();
			//$('#bssSheetInfo',window.parent.document).html(mesg);
			if (mesg == "") {
				alert("��LOID��Ӧ�豸�����ڣ�");
			} else {
				alert("��LOID��Ӧ�豸���ڣ�<br>���ȷ���鿴�豸��ϸ��Ϣ��");
				DetailDevice(mesg);
			}

		});
	}

	function DetailDevice(device_id) {
		var strpage = "../../inmp/resource/DeviceShow.jsp?device_id=" + device_id;	
		
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}



	function ToExcel(){
		
	//	var form =$('#form', window.parent.document);
	//	var mainForm=parent.document.getElementById("form");
		var mainForm = window.parent.document.getElementById("form");
		mainForm.action="<s:url value='/inmp/bss/bssSheetServ!toExcel.action'/>"
		mainForm.submit();
		mainForm.action="<s:url value='/inmp/bss/bssSheetServ!getBssCustomerServInfo.action'/>";
		
	}
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>BSS�û���Ϣ</caption>
	<thead>
		<tr>
			<th>LOID</th>
			<th>����</th>
			<th>BSS����ʱ��</th>
			<!-- <th>ITMS����ʱ��</th> -->
			<th>BSS�ն�����</th>
			<th>�豸���к�</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="bssSheetServList!=null">
			<s:if test="bssSheetServList.size()>0">
				<s:iterator value="bssSheetServList">
					<tr>
						<td>
						<a href="javascript:GoContent('<s:property value="user_id" />',1);">
							<s:property value="username" />
						</a>
						</td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="dealdate" /></td>
						<td><s:property value="type_id" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><a href="javascript:bssSheet2('<s:property value="username" />')">BSS����</a>|
							<a href="javascript:getServ('<s:property value="user_id" />')">ҵ������</a>
						</td>
					</tr>
				</s:iterator>
			
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>ϵͳû�и��û���ҵ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			
			<td colspan="8" align="right">
			<lk:pages
				url="/inmp/bss/bssSheetServ!getBssCustomerServInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
			<tr>
			<td colspan="8">
				<IMG SRC="/images/inmp/excel.gif" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
<script>
function batchReset()
{
		var url = "bssSheetServBatchReset.jsp";
		window.open(url,"","left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
}

function getServ(userId)
{
	// gw_type
	var gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
	var url = "<s:url value='/inmp/bss/bssSheetServ!getServByUser.action'/>?userId=" + userId + "&gw_type=" + gw_type;
	window.open(url,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>

</html>
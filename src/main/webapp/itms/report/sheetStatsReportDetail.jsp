<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ����Ϣ��ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		window.opener.parent.dyniframesize();
	});
	function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts){
		if(deviceId==""){
			alert("�û�δ���豸�����Ȱ��豸���ټ��");
			return;
		}
		if(confirm('���¼����ǽ���ҵ����Ϊδ��״̬��ȷʵҪ������?')){
			var url = "<s:url value='/itms/service/bssSheetServ!callPreProcess.action'/>";
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
		    		//window.location.reload();
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
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id=" + device_id;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	 function GoContent(user_id,gw_type){
		 if(gw_type=="2"){
		 	var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }else{
			var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }
			window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
		}
	function configInfo(userId, oui, deviceSN, deviceId, servTypeId,servstauts, wanType,open_status) {
		//alert("userId"+userId+"oui"+oui+"deviceSN"+deviceSN+"deviceId"+deviceId+"servTypeId"+servTypeId+"servstauts"+servstauts+"wanType"+wanType);
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServ!getConfigInfo.action'/>";
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
	    	$('#configInfoEm',window.document).show();
	    	$('#configInfo',window.document).show();
	    	$('#configInfo',window.document).html(mesg);
	    });
	}

	function bssSheet(userId, cityId, servTypeId) {
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServ!getBssSheet.action'/>";
		$.post(url, {
			userId : userId,
			cityId : cityId,
			servTypeId : servTypeId
		}, function(mesg) {
			$('#bssSheetInfo', window.document).show();
			$('#bssSheetInfo', window.document).html(mesg);
		});
	}

	function bssSheet2(username) {
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServ!getBssSheet2.action'/>";
		$.post(url, {
			username : username
		}, function(mesg) {
			$('#bssSheetInfo', window.document).show();
			$('#bssSheetInfo', window.document).html(mesg);
		});
	}

/**
	function checkdevice(username) {
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/bssSheetServ!checkdevice.action'/>";
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
**/

function DetailDevice(device_id) {
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
	
 function GoContent(user_id,gw_type){
		 if(gw_type=="2"){
		 	var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }else{
			var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }
			window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function ToExcel()
{
	var url = "<s:url value='/itms/report/sheetStatsReport!getDetailExcel.action'/>?cityId="+cityId.value+"&openStatus="+openStatus.value;
	
	// ��һ����ȡ��ҳ��ļ�����ѯ����

	url += "&deviceType=" + deviceType.value + "&startOpenDate=" + startOpenDate.value + "&endOpenDate=" + endOpenDate.value + "&servTypeId=" + servTypeId.value;
	mainForm.action = url;
	mainForm.submit();
}

/**
function configDetailInfo(strategyId,deviceSN)
{
	var page = "<s:url value='/itms/service/bssSheetServ!getConfigDetail.action'/>?strategyId="+strategyId+"&deviceSN="+deviceSN;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
	var page = "<s:url value='/itms/service/bssSheetServ!getConfigLogInfo.action'/>?"
		+ "deviceSN=" + deviceSN
		+ "&deviceId=" + deviceId
		+ "&servTypeId=" + servTypeId
		+ "&servstauts=" + servstauts
		+ "&wanType=" + wanType;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}

function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}
**/
</script>

</head>

<body>
<form id="mainForm" method="post" target="_self" action = "">
</form>
<div style="display:none"> 
	<input type="hidden" id="cityId" value="<s:property value="cityId" />" />
	<input type="hidden" id="openStatus" value="<s:property value="openStatus" />" />
	<input type="hidden" id="deviceType" value="<s:property value="deviceType" />" />
	<input type="hidden" id="startOpenDate" value="<s:property value="startOpenDate" />" />
	<input type="hidden" id="endOpenDate" value="<s:property value="endOpenDate" />" />
	<input type="hidden" id="servTypeId" value="<s:property value="servTypeId" />" />
</div>
<table class="listtable" id="listTable">
	<caption>ҵ����Ϣ</caption>
	<thead>
		<tr>
			<th>LOID</th>
			<th>����</th>
			<th>BSS����ʱ��</th>
			<th>ҵ������</th>
			<th>�豸���к�</th>
			<th>��ͨ״̬</th>
			<th>BSS�ն�����</th>
		<!-- 	<th>����</th>  -->
		</tr>
	</thead>
	<tbody>
		<s:if test="detailReportList!=null">
			<s:if test="detailReportList.size()>0">
				<s:iterator value="detailReportList">
					<s:if test='open_status=="1"'>
						<tr>
							<td>
								<a href="javascript:GoContent('<s:property value="user_id" />',1);">
									<s:property value="username" />
								</a>	
							</td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="dealdate" /></td>
							<td><s:property value="serv_type_name" /></td>
							<td>
								<a
								href="javascript:DetailDevice('<s:property value="device_id" />');">
								<s:property value="device_serialnumber" />
								</a>
							</td>
							<td>�ɹ�</td>
							<!-- <td>
								<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />')">������Ϣ</a>|
								<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />')">BSS����</a>
							</td> -->
							<td>
								<s:if test="type_id==2">
									E8-C
								</s:if>
								<s:elseif test="type_id==1">
									E8-B
								</s:elseif>
								<s:else>
									
								</s:else>
							</td>
							
						</tr>
					</s:if>
					<s:elseif test='open_status=="0"'>
						<tr>
							<td>
								<a href="javascript:GoContent('<s:property value="user_id" />',1);">
									<font color="#008000">
										<s:property value="username" />
									</font>
								</a>	
							</td>
							<td><font color="#008000"><s:property value="city_name" /></font></td>
							<td><font color="#008000"><s:property value="dealdate" /></font></td>
							<td><font color="#008000"><s:property value="serv_type_name" /></font></td>
							<td>
								<a href="javascript:DetailDevice('<s:property value="device_id" />');">
									<font color="#008000">
										<s:property value="device_serialnumber" />
									</font>
								</a>
							</td>
							<td><font color="#008000">δ��</font></td>
							<!-- <td>
								<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />')">������Ϣ</a>|
								<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />')">BSS����</a>
							</td> -->
							<td>
								<s:if test="type_id==2">
									E8-C
								</s:if>
								<s:elseif test="type_id==1">
									E8-B
								</s:elseif>
								<s:else>
									
								</s:else>
							</td>
						</tr>
					</s:elseif>
					<s:elseif test='open_status=="-1"'>
						<tr>
							<td>
								<a href="javascript:GoContent('<s:property value="user_id" />',1);">
									<font color="red">
										<s:property value="username" />
									</font>
								</a>	
							</td>
							<td><font color="red"><s:property value="city_name" /></font></td>
							<td><font color="red"><s:property value="dealdate" /></font></td>
							<td><font color="red"><s:property value="serv_type_name" /></font></td>
							<td>
								<a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
									<font color="red"><s:property value="device_serialnumber" /></font>
									</a>
							</td>
							<td><font color="red">ʧ��</font><input type='hidden'
							  value='<s:property value="user_id" />|<s:property value="device_id" />|<s:property value="oui" />|<s:property value="device_serialnumber" />|<s:property value="serv_type_id" />|<s:property value="serv_status" />'
							  name="resetParam" /></td>
							  <!-- <td>
								<a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />')">������Ϣ</a>|
								<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />')">BSS����</a>
								<s:if test='serv_type_name=="VoIP"'>|
									<a
										href="javascript:resetData('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />')">���¼���</a>
								</s:if>
							</td> -->
							<td>
								<s:if test="type_id==2">
									E8-C
								</s:if>
								<s:elseif test="type_id==1">
									E8-B
								</s:elseif>
								<s:else>
									
								</s:else>
							</td>
						</tr>
					</s:elseif>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=5>û�в�ѯ����¼!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=5>û�в�ѯ����¼!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
	 <tr>
			<td colspan="8" align="right" nowrap>
			<IMG style="float:left; padding-top:5px" SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ToExcel()">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<lk:pages
				url="/itms/report/sheetStatsReport!getDetailReport.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr> 	
	</tfoot>
</table>

<!-- BSS������Ϣ��������Ϣ -->
<table style="width:100%">
	<tr>
		<td height="25" id="configInfoEm" style="display: none" >

		</td>
	</tr>
	<tr>
		<td colspan=5 id="configInfo" style="display: none">

		</td>
	</tr>
	<tr>
		<td height="25">

		</td>
	</tr>
	<tr>
		<td id="bssSheetInfo" style="display: none">

		</td>
	</tr>
</table>
</body>
<script>

/**
if(listTable.innerHTML.indexOf("���¼���") != -1)
{
	batchBut.style.display = "inline";
}
else
{
	batchBut.style.display = "none";
}
function batchReset()
{
		var url = "bssSheetServBatchReset2.jsp";
		window.open(url,"","left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
}
**/
</script>

</html>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û�ҵ���ѯ</title>
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
			var url = "<s:url value='/itms/service/bssSheetServSDLT!callPreProcess.action'/>";
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
	     gw_type = document.getElementById("gw_type").value;
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
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getConfigInfo.action'/>";
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
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssSheet.action'/>";
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
		var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssSheet2.action'/>";
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
		var url = "<s:url value='/itms/service/bssSheetServSDLT!checkdevice.action'/>";
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
	
	
function configDetailInfo(strategyId,deviceSN)
{
	var page = "<s:url value='/itms/service/bssSheetServSDLT!getConfigDetail.action'/>?strategyId="+strategyId+"&deviceSN="+deviceSN;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
	var page = "<s:url value='/itms/service/bssSheetServSDLT!getConfigLogInfo.action'/>?"
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
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>BSSҵ����Ϣ</caption>
	<thead>
		<tr>
			<th>ҵ������</th>
			<th>�豸���к�</th>
			<th>BSS����ʱ��</th>
			<th>��ͨ״̬</th>
			<th>�豸�ͺ�</th>
			<th>�ر�汾</th>
			<th>����汾</th>
			<th>Ӳ���汾</th>
			<th>���з�ʽ</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<input type="hidden" value='<s:property value="gw_type" />' id="gw_type"/>
		<s:if test="bssSheetServList!=null">
			<s:if test="bssSheetServList.size()>0">
				<s:iterator value="bssSheetServList">
					<s:if test='open_status=="1"'>
						<tr>
							<td><s:property value="serv_type_name" /></td>
							<td><a
								href="javascript:DetailDevice('<s:property value="device_id" />');">
							<s:property value="device_serialnumber" /> </a></td>
							<td><s:property value="dealdate" /></td>
							<td>�ɹ�</td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="specversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="hardwareversion" /></td>

							<td><s:if test="access_style_relay_id==1">
										ADSL
									</s:if> <s:if test="access_style_relay_id==2">
										LAN
									</s:if> <s:if test="access_style_relay_id==3">
										����
									</s:if></td>
							<td><a
								href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />')">������Ϣ</a>|
							<a
								href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />')">BSS����</a>
							</td>
						</tr>
					</s:if>
					<s:elseif test='open_status=="0"'>
						<tr>
							<td><font color="#008000"><s:property
								value="serv_type_name" /></font></td>
							<td><font color="#008000"> <a
								href="javascript:DetailDevice('<s:property value="device_id" />');">
							<s:property value="device_serialnumber" /> </a> </font></td>
							<td><font color="#008000"><s:property
								value="dealdate" /></font></td>
							<td><font color="#008000">δ��</font></td>

							<td><font color="#008000"><s:property
								value="device_model" /></font></td>
							<td><font color="#008000"><s:property
								value="specversion" /></font></td>

							<td><font color="#008000"><s:property
								value="softwareversion" /></font></td>

							<td><font color="#008000"><s:property
								value="hardwareversion" /></font></td>


							<td><s:if test="access_style_relay_id==1">
								<font color="#008000"> ADSL</font>
							</s:if> <s:if test="access_style_relay_id==2">
								<font color="#008000">LAN</font>
							</s:if> <s:if test="access_style_relay_id==3">
								<font color="#008000">����</font>
							</s:if></td>

							<td><a
								href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />')">������Ϣ</a>|
							<a
								href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />')">BSS����</a>
							</td>
						</tr>
					</s:elseif>
					<s:elseif test='open_status=="-1"'>
						<tr>
							<td><font color="red"><s:property
								value="serv_type_name" /></font></td>
							<td><a
								href="javascript:DetailDevice('<s:property value="device_id" />');">
							<font color="red"><s:property value="device_serialnumber" /></font>
							</a></td>
							<td><font color="red"><s:property value="dealdate" /></font></td>
							<td><font color="red">ʧ��</font><input type='hidden'
								value='<s:property value="user_id" />|<s:property value="device_id" />|<s:property value="oui" />|<s:property value="device_serialnumber" />|<s:property value="serv_type_id" />|<s:property value="serv_status" />'
								name="resetParam" /></td>
                            <td><font color="red"><s:property
								value="device_model" /></font></td>
							<td><font color="red"><s:property value="specversion" /></font></td>
							<td><font color="red"><s:property
								value="softwareversion" /></font></td>
							<td><font color="red"><s:property
								value="hardwareversion" /></font></td>

							<td><s:if test="access_style_relay_id==1">
								<font color="red"> ADSL</font>
							</s:if> <s:if test="access_style_relay_id==2">
								<font color="red">LAN</font>
							</s:if> <s:if test="access_style_relay_id==3">
								<font color="red">����</font>
							</s:if></td>
                           	<td><a
								href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />')">������Ϣ</a>|
							<a
								href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />')">BSS����</a>
							<s:if test='serv_type_name=="VoIP"'>|
									<a
									href="javascript:resetData('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />')">���¼���</a>
							</s:if></td>
						</tr>
					</s:elseif>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>ϵͳû�и��û���ҵ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>

			<td colspan="13" align="right"><!-- <a href="#" 
				onclick="batchReset()" id=batchBut>��������</a> -->&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<!-- 
			<lk:pages
				url="/itms/service/bssSheetServSDLT!getBssSheetServInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" />
			 -->
		</tr>
	</tfoot>
</table>

<!-- BSS������Ϣ��������Ϣ -->
<table style="width: 100%">
	<tr>
		<td height="25" id="configInfoEm" style="display: none"></td>
	</tr>
	<tr>
		<td colspan=5 id="configInfo" style="display: none"></td>
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
	<tr>
		<td id="bssSheetInfo" style="display: none"></td>
	</tr>
</table>
</body>
<script>

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
</script>

</html>
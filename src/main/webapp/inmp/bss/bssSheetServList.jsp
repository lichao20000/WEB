<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * e8-cҵ���ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="../../Js/inmp/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="../../Js/inmp/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}

	// ���¼���
	function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
		if (deviceId == "") {
			alert("�û�δ���豸�����Ȱ��豸���ټ��");
			return;
		}
		if (confirm('���¼����ǽ���ҵ����Ϊδ��״̬��ȷʵҪ������?')) {
			var url = "<s:url value='/inmp/bss/bssSheetServ!callPreProcess.action'/>";
			$.post(url, {
				userId : userId,
				deviceSN : deviceSN,
				deviceId : deviceId,
				servTypeId : servTypeId,
				servstauts : servstauts,
				oui : oui
			}, function(ajax) {
				if (ajax == "1") {
					alert("��Ԥ���ɹ���");
					//parent.query();
				} else if (ajax == "-1") {
					alert("����Ϊ�գ�");
				} else if (ajax == "-2") {
					alert("��Ԥ��ʧ�ܣ�");
				}
			});
			//$("td[@id='temp123']").html("<font color='red'>δ��</font>");
			//$('#resultStr',window.parent.document).html("<font color='red'>02586588146���¼���ɹ���</font>");
		}
	}

	function DetailDevice(device_id) {
		var gw_type = "";
		var strpage = "<s:url value='../resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	function GoContent(user_id, gw_type) {
		gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		var strpage = "<s:url value='../resource/HGWUserRelatedInfo.jsp'/>?user_id="
				+ user_id;
		if (gw_type == "2") {
			strpage = "<s:url value='../resource/EGWUserRelatedInfo.jsp'/>?user_id="
					+ user_id;
		}
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	// ������Ϣ	
	function configInfo(userId, oui, deviceSN, deviceId, servTypeId,
			servstauts, wanType, open_status,serUsername) {
		//var gw_type = window.parent.getElementsByName("gw_type")[0].value;
		//alert("gw_type:" + gw_type);
		//alert("userId"+userId+"oui"+oui+"deviceSN"+deviceSN+"deviceId"+deviceId+"servTypeId"+servTypeId+"servstauts"+servstauts+"wanType"+wanType);
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!getConfigInfo.action'/>";
		$.post(url, {
			userId : userId,
			oui : oui,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			wanType : wanType,
			serUsername : serUsername,
			openstatus : open_status
		}, function(mesg) {
			$('#configInfoEm', window.parent.document).show();
			$('#configInfo', window.parent.document).show();
			$('#configInfo', window.parent.document).html(mesg);
		});
	}

	// BSS����
	function bssSheet(userId, cityId, servTypeId, type_name, serUsername) {
		// gw_type
		var gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!getBssSheet.action'/>";
		$.post(url,
				{
					userId : userId,
					cityId : cityId,
					servTypeId : servTypeId,
					type_name : type_name,
					serUsername : serUsername,
					gw_type : gw_type,
					netServUp : window.parent.document
							.getElementsByName("netServUp")[0].value
				}, function(mesg) {
					$('#bssSheetInfo', window.parent.document).show();
					$('#bssSheetInfo', window.parent.document).html(mesg);
				});
	}

	function bssSheet2(username) {
		$('#bssSheetDetail', window.parent.document).hide();
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
		$('#bssSheetDetail', window.parent.document).hide();
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
		var strpage = "../resource/DeviceShow.jsp?device_id=" + device_id;

		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	// ҵ���·�
	function sendSheet(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
		if (deviceId == "") {
			alert("�û�δ���豸�����Ȱ��豸�����·�ҵ��");
			return;
		}
		if (confirm('ȷ��Ҫ�·�ҵ����?')) {
			var url = "<s:url value='/inmp/bss/bssSheetServ!callPreProcess.action'/>";
			$.post(url, {
				userId : userId,
				servTypeId : servTypeId,
				deviceId : deviceId,
				oui : oui,
				deviceSN : deviceSN,
				servstauts : servstauts
			}, function(ajax) {
				if (ajax == "1") {
					alert("��Ԥ���ɹ���");
					//parent.query();
				} else if (ajax == "-1") {
					alert("����Ϊ�գ�");
				} else if (ajax == "-2") {
					alert("��Ԥ��ʧ�ܣ�");
				}
			});
		}
	}

	function configDetail(user_id, serv_type_id,serUsername) {
		// gw_type
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/inmp/bss/bssSheetServ!getBssIssuedConfigDetail.action'/>";
		$.post(url, {
			user_id : user_id,
			serUsername : serUsername,
			serv_type_id : serv_type_id
		}, function(mesg) {
			$('#bssSheetDetail', window.parent.document).show();
			$('#bssSheetDetail', window.parent.document).html(mesg);
		});
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>BSSҵ����Ϣ</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>����</th>
				<ms:inArea areaCode="js_dx" notInMode="false">
					<th>�û�������Դ</th>
				</ms:inArea>
				<th>BSS����ʱ��</th>
				<th>ҵ������</th>
				<%
					if (LipossGlobals.inArea("sd_lt")) {
				%>
				<th>�û��ʺ�</th>
				<%
					} else {
				%>
				<th>ҵ���ʺ�</th>
				<%
					}
				%>
				<th>BSS�ն˹��</th>
				<th>�豸���к�</th>
				<th>��ͨ״̬</th>
				<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
					<th>BSS�ն�����</th>
				</ms:inArea>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="bssSheetServList!=null">
				<s:if test="bssSheetServList.size()>0">
					<s:iterator value="bssSheetServList">
						<s:if test='open_status=="1"'>
							<tr>
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<s:property value="username" />
								</a></td>
								<td><s:property value="city_name" /></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
									<td><s:property value="user_type_id" /></td>
								</ms:inArea>
								<td><s:property value="dealdate" /></td>
								<td><s:property value="serv_type" /></td>
								<td><s:property value="serUsername" /></td>
								<td><s:property value="spec_name" /></td>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a></td>
								<td>�ɹ�</td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /></td>
								</ms:inArea>

								<td><a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">������Ϣ</a>|
									<s:if test='serv_type_id!="18"'>
										<a
											href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />')">BSS����</a>
									</s:if> <s:if
										test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
								| <a
											href="'<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">����ģ��</a>
									</s:if></td>
							</tr>
						</s:if>
						<s:elseif test='open_status=="0"'>
							<tr>
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<font color="#008000"><s:property value="username" />
									</font>
								</a></td>
								<td><font color="#008000"><s:property
											value="city_name" /> </font></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
									<td><font color="#008000"><s:property
												value="user_type_id" /> </font></td>
								</ms:inArea>
								<td><font color="#008000"><s:property
											value="dealdate" /> </font></td>
								<td><font color="#008000"><s:property
											value="serv_type" /> </font></td>
								<td><font color="#008000"><s:property
												value="serUsername" /></td>
								<td><font color="#008000"><s:property
											value="spec_name" /></font></td>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<font color="#008000"><s:property
												value="device_serialnumber" /> </font>
								</a></td>
								<td><font color="#008000">δ��</font></td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /></td>
								</ms:inArea>
								<td><a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">������Ϣ</a>|
									<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />')">BSS����</a>|
									<s:if test='device_id != null&&device_id != ""'>
										<a
											href="javascript:sendSheet('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />')">ҵ���·�</a>|
							</s:if> <s:if
										test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
										<a
											href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">����ģ��</a>
									</s:if></td>
							</tr>
						</s:elseif>
						<s:elseif test='open_status=="-1"'>
							<tr>
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<font color="red"><s:property value="username" /> </font>
								</a></td>
								<td><font color="red"><s:property value="city_name" /></font></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
								<td><font color="red"><s:property
												value="user_type_id" /> </font></td>
								</ms:inArea>
								<td><font color="red"><s:property value="dealdate" />
								</font></td>
								<td><font color="red"><s:property value="serv_type" />
								</font></td>
								<td><font color="red"><s:property
												value="serUsername" /></font></td>
								<td><font color="red"><s:property value="spec_name" />
								</font></td>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<font color="red"><s:property
												value="device_serialnumber" /> </font>
								</a></td>
								<td><font color="red">ʧ��</font></td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /> <input type='hidden'
										value='<s:property value="user_id" />|<s:property value="device_id" />|<s:property value="oui" />|<s:property value="device_serialnumber" />|<s:property value="serv_type_id" />|<s:property value="serv_status" />'
										name="resetParam" /></td>
								</ms:inArea>
								<td><a
									href="javascript:configInfo('<s:property value="user_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="device_id" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />','<s:property value="wan_type" />','<s:property value="open_status" />','<s:property value="serUsername" />')">������Ϣ</a>|
									<a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />')">BSS����</a>|
									<s:if
										test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
										<a
											href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">����ģ��</a>|
								</s:if> <!--<s:if test='serv_type=="VOIP"'>--> <a
									href="javascript:resetData('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />')">���¼���</a>
									<!--</s:if>--></td>
							</tr>
						</s:elseif>
						<s:else>
							<tr>
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<s:property value="username" />
								</a></td>
								<td><s:property value="city_name" /></td>
								<ms:inArea areaCode="js_dx" notInMode="false">
									<td><s:property value="user_type_id" /></td>
								</ms:inArea>
								<td></td>
								<td></td>
								<td></td>
								<td><s:property value="spec_name" /></td>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a></td>
								<td></td>
								<ms:inArea areaCode="jl_dx,sd_lt" notInMode="true">
									<td><s:property value="type_name" /></td>
								</ms:inArea>

								<td><a
									href="javascript:bssSheet2('<s:property value="username" />')">BSS����</a>

									<s:if
										test='serv_type_id=="10" || serv_type_id=="11" || serv_type_id=="14"'>
								 | <a
											href="javascript:configDetail('<s:property value="user_id" />','<s:property value="serv_type_id" />','<s:property value="serUsername" />')">����ģ��</a>
									</s:if> <!--<s:if test='device_serialnumber==""||device_serialnumber==null'>
								<a
									href="javascript:checkdevice('<s:property value="username" />')">����豸</a>
							</s:if>--></td>
							</tr>
						</s:else>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<ms:inArea areaCode="jl_dx">
							<td colspan=11>ϵͳû�и��û���ҵ����Ϣ!</td>
						</ms:inArea>
						<ms:inArea areaCode="jl_dx" notInMode="true">
							<td colspan=11>ϵͳû�и��û���ҵ����Ϣ!</td>
						</ms:inArea>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<ms:inArea areaCode="jl_dx">
						<td colspan=11>ϵͳû�д��û�!</td>
					</ms:inArea>
					<ms:inArea areaCode="jl_dx" notInMode="true">
						<td colspan=11>ϵͳû�д��û�!</td>
					</ms:inArea>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<ms:inArea areaCode="jl_dx">
					<td colspan="10" align="right">
						<%--<a href="#" onclick="batchReset()" 
				id=batchBut>��������</a>--%> &nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
							url="/inmp/bss/bssSheetServ!getBssSheetServInfo.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
				</ms:inArea>
				<ms:inArea areaCode="jl_dx" notInMode="true">
					<td colspan="11" align="right">
						<%--<a href="#" onclick="batchReset()" 
				id=batchBut>��������</a>--%> &nbsp;&nbsp;&nbsp;&nbsp; <ms:simplePages
							url="/inmp/bss/bssSheetServ!getBssSheetServInfo.action" />
					</td>
				</ms:inArea>
			</tr>
			<s:if test='#session.isReport=="1"'>
				<tr>
					<ms:inArea areaCode="jl_dx">
						<td colspan="10" align="right"><IMG
							SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
							style='cursor: hand' onclick="ToExcel()"></td>
					</ms:inArea>
					<ms:inArea areaCode="jl_dx" notInMode="true">
						<td colspan="11" align="right"><IMG
							SRC="/itms/images/excel.gif" BORDER='0' ALT='�����б�'
							style='cursor: hand' onclick="ToExcel()"></td>
					</ms:inArea>
				</tr>
			</s:if>
		</tfoot>
	</table>
</body>
<script>
	function batchReset() {
		var url = "bssSheetServBatchReset.jsp";
		window
				.open(url, "",
						"left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
</script>

</html>
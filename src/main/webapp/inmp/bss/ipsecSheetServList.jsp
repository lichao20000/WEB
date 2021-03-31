
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%String absPath=request.getContextPath(); %>
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
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	function DetailDevice(device_id) {
		var gw_type = "";
		var strpage = "<s:url value='/itms/service/ipsecSheetServ!getSingleDeviceInfo.action'/>?deviceId="+ device_id;
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

	/* function GoContent(user_id, gw_type) {
		   var strpage = "<s:url value='/itms/service/bssSheetByHand4HBLT.action'/>?gw_type=1&user_id="
               + user_id;
			window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
		} */

	// BSS����
	function bssSheet(userId, cityId, servTypeId, type_name, serUsername,realBindPort) {
		// gw_type
		var gw_type = window.parent.document.getElementsByName("gw_type")[0].value;
		//alert("gw_type:" + gw_type);
		$('#bssSheetDetail', window.parent.document).hide();
		$('#bssSheetInfo', window.parent.document).hide();
		$('#configInfoEm', window.parent.document).hide();
		$('#configInfo', window.parent.document).hide();
		var url = "<s:url value='/itms/service/ipsecSheetServ!queryBssSheet.action'/>";
		$.post(url,
				{
					user_id : userId,
					cityId : cityId,
					servTypeId : servTypeId,
					type_name : type_name,
					serUsername : serUsername,
					gw_type : gw_type,
					realBindPort:realBindPort
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
		var url = "<s:url value='/itms/service/bssSheetServ!getBssSheet2.action'/>";
		$.post(url, {
			username : username
		}, function(mesg) {
			$('#bssSheetInfo', window.parent.document).show();
			$('#bssSheetInfo', window.parent.document).html(mesg);
		});
	}

 function DetailDevice(device_id) {
		var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	} 

	// ҵ���·�
	function sendSheet(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
		//var servTypeId="2701";
		if (deviceId == "") {
			alert("�û�δ���豸�����Ȱ��豸�����·�ҵ��");
			return;
		}
		if (confirm('ȷ��Ҫ�·�ҵ����?')) {
			var url = "<s:url value='/itms/service/ipsecSheetServ!callPreProcess.action'/>";
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
				} else if (ajax == "-1") {
					alert("����Ϊ�գ�");
				} else if (ajax == "-2") {
					alert("��Ԥ��ʧ�ܣ�");
				}
			});
		}
	}
//����
function ToExcel()
{
	var mainForm = window.parent.document.getElementById("form");
	mainForm.action="<s:url value='/itms/service/ipsecSheetServ!toExcel.action'/>";
	mainForm.submit();
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>IPSECҵ����Ϣ</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>����</th>
				<th>IPSEC����ʱ��</th>
				<th>ҵ������</th>
				<th>ҵ���˺�</th>
				<th>�豸���к�</th>
				<th>��ͨ״̬</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="ipsecSheetServList!=null">
				<s:if test="ipsecSheetServList.size()>0">
					<s:iterator value="ipsecSheetServList">
						<s:if test='open_status=="1"'>
							<tr>
								<td><%-- <a href="javascript:GoContent('<s:property value="user_id" />',1);">
										<s:property value="username" />
								</a> --%><s:property value="username" /></td>
								<td><s:property value="city_name" /></td>
								<td><s:property value="opendate" /></td>
								<td><s:property value="serv_type" /></td>
								<td><s:property value="serUsername" /></td>
								<td><%-- <a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a> --%><s:property value="device_serialnumber" /></td>
								<td>�ɹ�</td>
								<td><s:if test='serv_type_id!="18"'>
										<a
											href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />')">IPSEC����</a>
									</s:if></td>
							</tr>
						</s:if>
						<s:elseif test='open_status=="0"'>
							<tr>
								<td><%-- <a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<font color="#008000"><s:property value="username" />
									</font>
								</a> --%><font color="#008000"><s:property value="username" />
									</font></td>
								<td><font color="#008000"><s:property
											value="city_name" /> </font></td>
								<td><font color="#008000"><s:property
											value="opendate" /> </font></td>
								<td><font color="#008000"><s:property
											value="serv_type" /> </font></td>
								<td><font color="#008000"><s:property
											value="serUsername" /></td>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<font color="#008000"><s:property value="device_serialnumber" /> </font>
								</a><%-- <font color="#008000"><s:property value="device_serialnumber" /> </font> --%></td>
								<td><font color="#008000">δ��</font></td>
								<td><a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />')">IPSEC����</a>|
									<s:if test="device_id != null&&device_id !='' ">
										<a
											href="javascript:sendSheet('<s:property value="user_id" />','<s:property value="device_id" />','<s:property value="oui" />','<s:property value="device_serialnumber" />','<s:property value="serv_type_id" />','<s:property value="serv_status" />')">ҵ���·�</a>
									</s:if></td>
							</tr>
						</s:elseif>
						<s:elseif test='open_status=="-1"'>
							<tr>
								<td><%-- <a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<font color="red"><s:property value="username" /> </font>
								</a> --%><font color="red"><s:property value="username" /> </font></td>
								<td><font color="red"><s:property value="city_name" /></font></td>
								<td><font color="red"><s:property value="opendate" />
								</font></td>
								<td><font color="red"><s:property value="serv_type" />
								</font></td>
								<td><font color="red"><s:property
											value="serUsername" /></font></td>
								<td><%-- <a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<font color="red"><s:property value="device_serialnumber" /> </font>
								</a> --%><font color="red"><s:property value="device_serialnumber" /> </font></td>
								<td><font color="red">ʧ��</font></td>
								<td><a
									href="javascript:bssSheet('<s:property value="user_id" />','<s:property value="city_id" />','<s:property value="serv_type_id" />','<s:property value="type_name" />','<s:property value="serUsername" />')">IPSEC����</a>
								</td>
							</tr>
						</s:elseif>
						<%-- <s:else>
							<tr>
								<td><a
									href="javascript:GoContent('<s:property value="user_id" />',1);">
										<s:property value="username" />
								</a></td>
								<td><s:property value="city_name" /></td>
								<td></td>
								<td></td>
								<td></td>
								<td><a
									href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a><s:property value="device_serialnumber" /></td>
								<td></td>
								<td><a
									href="javascript:bssSheet2('<s:property value="username" />')">IPSEC����</a>
								</td>
							</tr>
						</s:else> --%>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>ϵͳû�и��û���ҵ����Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>ϵͳû�д��û�!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10" align="right"><lk:pages
						url="/itms/service/ipsecSheetServ!getIpsecSheetServInfo.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr>
				<td colspan="10" align="right"><img alt="�������"
					src="<%=absPath%>/images/excel.gif" border="0" style='cursor: hand'
					onclick="ToExcel()"></td>
			</tr>
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
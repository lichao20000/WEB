<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ѯ���</title>
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

	function GoContent(digit_map) {
		var url="<s:url value='/itms/resource/SoundGraphsQuery!number.action'/>?"
			+"&digit_map="+digit_map;
		window.open(url, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>ҵ������</th>
				<th>ִ��ʱ��</th>
				<th>�·����ʱ��</th>
				<th>����״̬</th>
				<th>���Խ��</th>
				<th>�������</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="userid !=null">
		<s:if test="userid.size()>0">
		<s:if test="userid.size()==1">
		<s:if test="DeviceId!=null">
		<s:if test="DeviceId.size()>0">
		<s:if test="DeviceId.size()==1">
		<s:iterator value="DeviceId">
		<s:if test="device_id!=null">
		<s:if test="digit_map!=null">
		<s:if test="Date != null ">
			<s:if test="Date.size() > 0">
				<s:iterator value="Date">
					<tr>
						<td>
							<s:property value="business_name" />
						</td>
						<td>
							<s:property value="start_time" />
						</td>
						<td>
							<s:property value="time" />
						</td>
						<td>
							<s:property value="status" />
						</td>
						<s:if test="result_id==1">
						<td>
							�ɹ�
						</td>
						</s:if>
						<s:elseif test="result_id==10000">
						<td>
							δ��
						</td>
						</s:elseif>
						<s:else>
						<td>
							ʧ��
						</td>
						</s:else>
						<td>
							<s:property value="result_desc" />
						</td>
						<td align="center">
							<a href="javascript:GoContent('<s:property value='digit_map' />')">������Ϣ</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
				<tr>
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> ������������ҵ�� </td>
			</tr>
		</s:else> 
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> ���û�δ���豸�� </td>
			</tr>
		</s:else>
		</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> ���û��Ƕ������û�,��ͨ��VOIP��֤�����VOIP�绰�����ѯ�� </td>
			</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left>������������ҵ�� </td>
			</tr>
		</s:else>
		
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> ������������ҵ�� </td>
			</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> �˺Ŷ�Ӧ����û��������LOID��ѯ�� </td>
			</tr>
		</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> ������˺Ų����ڣ� </td>
			</tr>
		</s:else>
		
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left> ������˺Ų����ڣ� </td>
			</tr>
		</s:else>
		
		<tfoot>
		<tr>
			<td colspan="7" align="right">
		 	<lk:pages
				url="/itms/resource/SoundGraphsQuery!query.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
	</tbody>
	</table>
</body>
</html>
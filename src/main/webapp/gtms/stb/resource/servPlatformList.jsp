
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%String absPath=request.getContextPath(); %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import=" flex.messaging.util.URLDecoder; "%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ��ƽ̨���Ͳ�ѯ�����ѯ</title>
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
	function update(platform_id,platform_name,remark) {
		var url = "<s:url value='/gtms/stb/resource/stbservplatform!queryplatformname.action'/>?platformid="+platform_id;
		window.open(url, "","left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	function delete1(platform_id,platform_name)
	{
		var sure = window.confirm("ȷ��ɾ����");
		if(!sure)
		{
			return;
		}
		 var url="<s:url value='/gtms/stb/resource/stbservplatform!deleteservPlatform.action'/>";
		 $.post(url,{
			 platformid:platform_id
			},function(ajax){
				if(ajax==1)
				{
					alert("ɾ���ɹ�!");
					window.parent.Query();
				}else
				{
					alert("ɾ��ʧ��!");
				}
			});
	}
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>IPSECҵ����Ϣ</caption>
		<thead>
			<tr>
				<th>ҵ��ƽ̨����ID</th>
				<th>ҵ��ƽ̨��������</th>
				<th>��ע</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="date!=null">
				<s:if test="date.size()>0">
					<s:iterator value="date">
							<tr align="center">
								<td><s:property value="platform_id" /></td>
								<td><s:property value="platform_name" /></td>
								<td><s:property value="remark" /></td>
								<td><a href="javascript:update('<s:property value="platform_id" />','<s:property value="platform_name" />','<s:property value="remark" />')">�༭</a>|
								<a href="javascript:delete1('<s:property value="platform_id" />','<s:property value="platform_name" />')">ɾ��</a>
								</td>
							</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>ϵͳû�и��û���ҵ����Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>ϵͳû�д��û�!</td>
				</tr>
			</s:else>
		</tbody>
	</table>
</body>
</html>
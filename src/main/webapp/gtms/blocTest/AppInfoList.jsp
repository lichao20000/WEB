<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�汾��ѯ</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">

	<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
		parent.dyniframesize();
		
	});

</script>

</head>

<body>
	<input type='hidden' name="appuuid_jg" value="<s:property value="appuuid" />" />
	<table class="listtable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>APPUUID</th>
				<th>APP����</th>
				<th>APP������</th>
				<th>APP�汾</th>
				<th>APP����ʱ��</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="appInfoList!=null">
				<s:if test="appInfoList.size()>0">
					<s:iterator value="appInfoList">
						<tr>
							<td><s:property value="appuuid" /></td>
							<td><s:property value="app_name" /></td>
							<td><s:property value="app_vendor" /></td>
							<td><s:property value="app_version" /></td>
							<td><s:property value="app_publish_time" /></td>
								<td>
								<a href="javascript:delDevice('<s:property value="id" />','<s:property value="filepath" />')">ɾ��</a>|
								<a href="javascript:editDevice('<s:property value="id" />','<s:property value="appuuid" />','<s:property value="app_name" />','<s:property value="app_desc" />','<s:property value="app_vendor" />','<s:property value="app_version" />','<s:property value="app_publish_status" />','<s:property value="filepath" />')">�༭</a>| 
								<a href="javascript:publish('<s:property value="id" />')">����</a>|
								<a href="javascript:detailDevice('<s:property value="id" />')">��ϸ��Ϣ</a>
							</td>
						</tr>
						</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>û�в�ѯ������豸��</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>û�в�ѯ������豸��</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/gtms/blocTest/MaintainInfoAction!querymaintainAppInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>


	</table>
</body>
<script>
	function publish(id){
	var url = "<s:url value='/gtms/blocTest/MaintainInfoAction!pubAppInfo.action'/>";
	$.post(url,{
		pubId:id
	},function(ajax){
		if("1" == ajax){
			alert("�����ɹ�");
		}else{
				alert("����ʧ��");
		}
			
		
		
	});
}
function delDevice(id,filepath){
	if(!delWarn()){
			return;
	}
	var url = "<s:url value='/gtms/blocTest/MaintainInfoAction!delAppInfo.action'/>";
	$.post(url,{
		delId:id,
		delName:filepath
	},function(ajax){
		if("1|1" == ajax){
			alert("ɾ���ɹ�");
		}else if("0|1" ==ajax){
			alert("����ɾ��ʧ��");
		}else if("1|0" == ajax){
			alert("�ļ�ɾ��ʧ��");
		}else{
				alert("�������ļ���ɾ��ʧ��");
		}
			
		
		
	});
}

function editDevice(updateid,appuuid,app_name,app_desc,app_vendor,app_version,app_publish_status,filepath){
	parent.showAddPart(true);
	parent.defaulValue(updateid,appuuid,app_name,app_desc,app_vendor,app_version,app_publish_status,filepath);
	
}

function detailDevice(detailid){
	window.open("<s:url value='/gtms/blocTest/MaintainInfoAction!queryDetailAppInfo.action'/>?detailid="+detailid,"","left=20,top=20,height=450,width=800,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}
function delWarn(){
	if(confirm("���Ҫɾ�����豸�汾��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}
</script>
</html>
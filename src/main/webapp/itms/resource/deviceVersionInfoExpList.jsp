<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�汾���ѯ</title>
<%
	/**
	 * @author chenjie
	 * @version 1.0
	 * @since 2012-12-14
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
	
	$(function(){
		parent.dyniframesize();
	});

</script>

</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>�豸����</th>
			<th>�豸�ͺ�</th>
			<th>�ض��汾</th>

			<th>Ӳ���汾</th>
			<th>����汾</th>
			<th>�Ƿ�淶</th>
			<th>�Ƿ����</th>
			<th>�豸����</th>
			<th>���з�ʽ</th>
			<th>�ն˹��</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceVersionList!=null">
			<s:if test="deviceVersionList.size()>0">
				<s:iterator value="deviceVersionList">
					<tr>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="device_model_name" /></td>
						<td><s:property value="specversion" /></td>
						<td><s:property value="hardversion" /></td>
						<td><s:property value="softversion" /></td>
						<td>
							<s:if test="1==is_normal">
								��
							</s:if>
							<s:else>
								��
							</s:else>
						</td>
						<td><s:if test="is_check==1">
										�������
									</s:if> <s:else>
							<font color='red'>δ����</font>
						</s:else></td>
						<td><s:if test="rela_dev_type_id==1">
										e8-b
									</s:if> <s:if test="rela_dev_type_id==2">
										e8-c
									</s:if> 
									<s:if test="rela_dev_type_id==3">A8-B</s:if>
									<s:if test="rela_dev_type_id==4">A8-C</s:if>
									</td>
						<td><s:property value="type_name" /></td>
						<td><s:property value="specName" /></td>
						<td>
						<a
						    href="javascript:delDevice('<s:property value="id" />','<s:property value="file_path" />')">ɾ��</a>|
						<a 
							href="javascript:editDevice('<s:property value="id" />')">�༭</a>
						</td>

					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>û�в�ѯ������豸�汾�⣡</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11>û�в�ѯ������豸�汾�⣡</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="11" align="right"><lk:pages
				url="/itms/resource/deviceVersionManageACT!queryDeviceVersion.action" styleClass=""
				showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>

</table>
<FORM method="post" action="" id="mainForm" name="mainForm" >
	<input type="hidden" name="filepath" id="filepath" />
</FORM>
</body>
<script>

// �༭
function editDevice(id)
{     
	var url = "<s:url value='/itms/resource/deviceVersionManageACT!queryForModify.action' />" + "?id=" + id;
	var ans = window.showModalDialog(url,"",'dialogHeight:500px;dialogWidth:800px');

	if(ans == "1")
	{
		window.parent.queryDevice();
	}
}

// ɾ��
function delDevice(id,path)
{
	if(!delWarn())
		return;
	var url = "<s:url value="/itms/resource/deviceVersionManageACT!deleteDeviceVersion.action"/>";

	$.post(url,{
		id:id,
		filepath:path},
	function(msg){
		var result = parseInt(msg);
		if(result == 0)
		{
			alert("ɾ��ʧ�ܣ�");
			return;
		}
		else
		{
			alert("ɾ���ɹ���");
			// ���²�ѯ
			window.parent.queryDevice();
		}
	});
}

function delWarn(){
	if(confirm("ȷ��Ҫɾ����\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

// ����
function download(path)
{
	if(confirm("ȷ��Ҫ�����ļ�:[" + path + "]?"))
	{
		var url = "<s:url value="/itms/resource/deviceVersionManageACT!download.action"/>";
		$("input[@id='filepath']").val(path);
		
		document.mainForm.action = url;
		document.mainForm.submit();
		return;
	}
	
}

// չʾ�ļ�·��
function show(path)
{	
	alert("�ļ���" + path);
}
</script>
</html>
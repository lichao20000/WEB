<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�汾��ѯ</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">

$(function(){
	//parent.dyniframesize();
});
</script>
</head>
<style>
.listtable tr:hover{
	background-color: #8dbbdb;
}
</style>
<body>

	<table class="listtable">
		<thead>
			<tr>
				<th>�豸����</th>
				<th>�豸�ͺ�</th>
				<th>�ض��汾</th>
				<th>Ӳ���汾</th>
				<th>����汾</th>
				<th>�Ƿ�Ϊ���°汾</th>
				<th>�Ƿ����</th>
				<th>�豸�汾����</th>
				<th>���з�ʽ</th>
				<th>�ն˹��</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="devVersionListTree!=null">
				<s:if test="devVersionListTree.size()>0">
					<s:iterator value="devVersionListTree">
						<tr>
							<td><s:property value="vendor_add" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="specversion" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td>
								<s:if test="1==is_normal">
									��
								</s:if>
								<s:else>
									��
								</s:else>
							</td>
							<td>
								<s:if test="is_check==1">
										�������
								</s:if>
								<s:else>
									<font color='red'>δ���</font>
								</s:else>
							</td>
							<td>
								<s:property value="devVersionType" />
							</td>
							<td><s:property value="type_name" /></td>
							<td><s:property value="specName" /></td>
							<td>
								<a href="javascript:delDevice('<s:property value="devicetype_id" />',
																'<s:property value="gw_type" />',
																'<s:property value="vendor_id" />',
																'<s:property value="device_model_id" />',
																'<s:property value="is_check" />')">ɾ��</a>|

									<a href="javascript:editDeviceAH('<s:property value="devicetype_id" />')">�༭</a>|
								<a href="javascript:editDeviceType('<s:property value="devicetype_id" />',
								'<s:property value="rela_dev_type_id" />','<s:property value="vendor_id" />',
								'<s:property value="device_model_id" />' )">�༭�豸����</a>|
								<a href="javascript:detailDevice('<s:property value="devicetype_id" />',
																'<s:property value="spec_id" />')">��ϸ��Ϣ</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>û�в�ѯ������豸��</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>û�в�ѯ������豸��</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/itms/resource/deviceTypeInfo!getDevVersionList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
		</tfoot>

	</table>
</body>
<script>


function detailDevice(devicetype_id,specId){
	window.open("<s:url value='/itms/resource/deviceTypeInfo!queryDetail.action'/>?deviceTypeId="+devicetype_id+"&detailSpecId="+specId,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}


//�༭ �����ڱ༭ҳ������ device_version_type ��ߵ�������
function editDeviceAH(device_type_id)
{
	window.open("<s:url value='/itms/resource/deviceTypeInfo!addDevVersionAHLT.action'/>?deviceTypeId=" + device_type_id,"",
			"left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}




//���༭�豸����
function editDeviceType(device_type_id,rela_dev_type_id,vendorId,modelId){
	if(rela_dev_type_id === "" ){
		rela_dev_type_id=-1;
	}

	window.parent.document.getElementsByName("rela_dev_type_edit")[0].value = rela_dev_type_id;
	window.parent.document.getElementById("updateId").value=device_type_id;
	window.parent.document.getElementById("vendorId").value=vendorId;
	window.parent.document.getElementById("modelId").value=modelId;
	var total =  <s:property value="totalRowCount_splitPage"/>;
	window.parent.document.getElementById("total").value=total;
	parent.showEditDeviceTypePart(true);
}

//ɾ���豸�ͺ�
function delDevice(id,gw_type,vendorId,modelId,isCheck)
{
	if(!delWarn())
		return;
	var url = "<s:url value='/itms/resource/deviceTypeInfo!deleteDevice.action'/>";
	
	$.post(url,{
		deleteID:id,
		gw_type:gw_type
	},function(ajax){
		var result = parseInt(ajax);
		if(result == 0)
		{
			alert("���豸�汾��������豸��������ɾ����");
			return;
		}
		if(result > 0)
		{
			alert("�豸�ͺ�ɾ���ɹ�");
			
			// ��ͨ��ʽ�ύ
			var total =  <s:property value="totalRowCount_splitPage"/>;
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value='/itms/resource/deviceTypeInfo!getDevVersionList.action'/>" +
					"?vendorId=" + vendorId + "&modelId=" + modelId + "&isCheck=" + isCheck + "&devVersionTotal=" + (total - 1);
			form.submit();
		}
		else
		{
			alert("�豸�ͺ�ɾ��ʧ�ܣ�");
		}
	});
}

function delWarn(){
	return confirm("���Ҫɾ�����豸�汾��\n��������ɾ���Ĳ��ָܻ�������");
}

function delCheckWarn(){
	return confirm("ȷ�����ͨ����");
}

function toExcel(taskId,upResult){
	var url =  "<s:url value='/itms/resource/deviceTypeInfo!exportList.action'/>?deviceTypeId="+devicetype_id;
	document.all("childFrm").src=url;
}

function checkDevice(id,vendorId,modelId){
	if(!delCheckWarn){
		return;
	}
	var url = "<s:url value='/itms/resource/deviceTypeInfo!updateIsCheck.action'/>";
	$.post(url,{
		deviceTypeId:id
	},function(ajax){
		var result = parseInt(ajax);
		if(result > 0)
		{
			alert("����豸�ɹ�! ");
			var total =  <s:property value="totalRowCount_splitPage"/> - 1;
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value='/itms/resource/deviceTypeInfo!getDevVersionList.action'/>" +
					"?vendorId=" + vendorId + "&modelId=" + modelId + "&devVersionTotal=" + total;
			form.submit();
		}
		else
		{
			alert("����豸ʧ��");
		}
	});
}
</script>
</html>
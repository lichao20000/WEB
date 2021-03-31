<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ģ���ѯ</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
	//var instArea = window.parent.document.getElementById("instArea").value;
});

$(function(){
	parent.dyniframesize();
});
</script>
</head>

<body>
	<%-- <input type='hidden' name="editDeviceType" value="<s:property value="editDeviceType" />" /> --%>
	<table class="listtable">
		<caption>��ѯ���</caption>
		<thead>
			<tr>
				<th>ģ������</th>
				<th>ҵ������</th>
				<!-- <th>VLANID</th> -->
				<th>�޸�ʱ��</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="deviceList!=null">
				<s:if test="deviceList.size()>0">
					<s:iterator value="deviceList">
						<tr>
							<td align="center"><s:property value="name" /></td>
							<td align="center">������</td>
							<%-- <td align="center"><s:property value="vlanid" /></td> --%>
							<td align="center"><s:property value="update_time" /></td>
							<td align="center">
								<a href="javascript:detailDevice('<s:property value="id" />')">��ϸ��Ϣ</a>|
								<a href="javascript:editDevice('<s:property value="id" />')">�༭</a>|
								<a href="javascript:delDevice('<s:property value="id" />')">ɾ��</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11>û�в�ѯ�������Ϣ��</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/itms/resource/servTemplate!init.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
		</tfoot>

	</table>
</body>
<script>
//�༭�豸����
function detailDevice(id)
{     
	window.open("<s:url value='/gwms/resource/servTemplate!queryDetail.action' />?id="+id);
}


function editDevice(id)
{     
	window.open("<s:url value='/gwms/resource/servTemplate!queryDetail4Edit.action' />?id="+id);
	//var returnVal = window.showModalDialog("<s:url value='/gwms/resource/servTemplate!queryDetail4Edit.action' />?id="+id,"","height=800,width=1500,status=yes,toolbar=no,menubar=no,location=no");
}


//ɾ��
function delDevice(id)
{
	if(confirm("���Ҫɾ����ģ����\n��������ɾ���Ĳ��ָܻ�������")){
		$.ajax({
    	    type:"POST",
    	    async:false,
    	    url:"<s:url value='/gwms/resource/servTemplate!deleteDevice.action' />?id="+id,
    	    dataType:"text",
    	    data: {},
    	    success:function (data) {
    	    	if(data==1){
    	    		alert("ģ��ɾ���ɹ�");
    	    	}
    	    	else
    	 		{
    	 			alert("ɾ��ģ��ʱ�쳣,�������Ա��ϵ");
    	 		}
    	 		window.location.reload();
    	 		//window.opener.Init();
    	    }
    	});
		return true;
	}
	else{
		return false;
	}
}


function toExcel(taskId,upResult){
	var url =  "<s:url value='/itms/resource/deviceTypeInfo!exportList.action'/>?deviceTypeId="+devicetype_id;
	document.all("childFrm").src=url;
}


</script>
</html>
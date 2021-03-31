<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * ԭʼ������ѯ
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
	function go(id){
		alert(id);
	}

	$(function() {
		parent.dyniframesize();
	});
	
	function editOUI(id,vendonName,ouiId,ouiDesc,remark,type){
		parent.addeditOUI(id,vendonName,ouiId,ouiDesc,remark,type);
	}
	function deleteOUI(id){
		if(!delWarn()){
			return;
		}
		var url = "<s:url value="/itms/resource/deviceOUIInfoACT!deleteOUI.action"/>";
		$.post(url,{
			id:id
		},function(ajax){
			if(ajax == "1")
			{
				alert("OUIɾ���ɹ�");
				var form = window.parent.document.getElementById("form");
				form.action = "<s:url value="/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action"/>";
				form.submit();
				return;
			}else
			{
				alert("OUIɾ��ʧ�ܣ�");
			}
		});
	}
	
	
	function delWarn(){
		if(confirm("���Ҫɾ����OUI��\n��������ɾ���Ĳ��ָܻ�������")){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>�豸OUI��Ϣ</caption>
	<thead>
		<tr>
			<th>���</th>
			<th>��������</th>
			<th>OUI</th>
			<th>OUI����</th>
			<th>��ע</th>
			<th>����ʱ��</th>
			<th>����</th>
			
		</tr>
	</thead>
	<tbody>
		<s:if test="dataList!=null">
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
						<tr>
							<td><s:property value="id" /></td>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="oui" /></td>
							<td><s:property value="vendor_add" /></td>
							<td><s:property value="remark" /></td>
							<td><s:property value="add_date" /></td>
							<td>
							<s:if test='%{userName=="admin" || userName=="szxadmin"}'>
								<a href="javaScript:editOUI('<s:property value='id'/>','<s:property value="vendor_name" />','<s:property value="oui" />','<s:property value="vendor_add" />','<s:property value="remark" />','2');" >�༭</a>|
								<a href="javaScript:deleteOUI('<s:property value='id'/>');" >ɾ��</a>
							</s:if>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="7" align="center" >OUI��Ϣ������!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="7" >ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="7"  align="right">
		 	<lk:pages
				url="/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
</table>
	
</body>
</html>
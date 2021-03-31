<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * 原始工单查询
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
				alert("OUI删除成功");
				var form = window.parent.document.getElementById("form");
				form.action = "<s:url value="/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action"/>";
				form.submit();
				return;
			}else
			{
				alert("OUI删除失败！");
			}
		});
	}
	
	
	function delWarn(){
		if(confirm("真的要删除该OUI吗？\n本操作所删除的不能恢复！！！")){
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
	<caption>设备OUI信息</caption>
	<thead>
		<tr>
			<th>序号</th>
			<th>厂商名称</th>
			<th>OUI</th>
			<th>OUI描述</th>
			<th>备注</th>
			<th>更新时间</th>
			<th>操作</th>
			
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
								<a href="javaScript:editOUI('<s:property value='id'/>','<s:property value="vendor_name" />','<s:property value="oui" />','<s:property value="vendor_add" />','<s:property value="remark" />','2');" >编辑</a>|
								<a href="javaScript:deleteOUI('<s:property value='id'/>');" >删除</a>
							</s:if>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="7" align="center" >OUI信息不存在!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="7" >系统没有此用户!</td>
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
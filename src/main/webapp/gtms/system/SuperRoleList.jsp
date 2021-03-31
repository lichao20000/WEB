<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%--
	/**
	 * 超级权限查询
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title></title>
		
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
		<script language="JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/Js/edittable.js"/>"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<table class="listtable">
			<caption>超级权限列表</caption>
			<thead>
				<tr>
					<th>权限名称</th>
					<th>权限编码</th>
					<th>控制方式</th>
					<th>用户/角色名称</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="dataList.size()>0">
					<s:iterator value="dataList">
						<tr>
							<td><s:property value="auth_name" /></td>
							<td><s:property value="auth_code" /></td>
							<td><s:property value="relation_name" /></td>
							<td><s:property value="user_name" /></td>
							<td> &nbsp; &nbsp; &nbsp;
							<!-- a href="javascript:editSuperRole('<s:property value="auth_id"/>')" >编辑</a> &nbsp; | &nbsp;-->
							<a href="javascript:deleteSuperRole('<s:property value="auth_id"/>','<s:property value="relation_id" />')">删除</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=9>系统没有相关的用户信息!</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="5">
						<span style="float: right;">  
							<lk:pages url="/gtms/system/superRoleManage!getAllRecords.action" styleClass=""
								showType="" isGoTo="true" changeNum="true" />
						</span>
					</td>
				</tr>
			</tfoot>
			<tr STYLE="display: none">
				<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</table>
	</body>
	<%@ include file="../../foot.jsp"%>
<script type="text/javascript">
	function deleteSuperRole(auth_id,relation_id){
		var sure = window.confirm("确定删除吗？");
		if(!sure)
		{
			return;
		}
	   var url = "<s:url value='/gtms/system/superRoleManage!deleteSuperRole.action'/>";
		$.post(url,{
			auth_id : auth_id,
			relation_id : relation_id
		},function(ajax){
			if(ajax =="1" ){
				alert("删除成功");
			}else{
				alert("删除失败");	
			}
			window.location.href=window.location.href;
		});
	}
	function editSuperRole(auth_id){
		   var url = "<s:url value='/gtms/system/superRoleManage!preUpdateSuperRole.action'/>?auth_id="+auth_id;
		   window.location.href =  url ;
		}
</script>
	
</html>
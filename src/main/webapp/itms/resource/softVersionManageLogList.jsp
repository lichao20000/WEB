<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>


<script type="text/javascript">

	$(function(){
		parent.dyniframesize();
	});


   var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
	
</script>


<table width="100%" class="listtable" id=userTable>
	<thead>
       <tr>
          <th width="10%"> 设备厂商</th>
          <th width="10%"> 设备型号</th>
          <th width="10%"> 硬件版本</th>
          <th width="15%"> 原始版本</th>
          <th width="15%"> 目标版本</th>
          <th width="12%"> 对应关系类型</th>
          <th width="5%"> 操作类型</th>
          <th width="8%"> 操作人员</th>
          <th width="15%"> 操作时间</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="softVersionLogList != null ">
			<s:if test="softVersionLogList.size() > 0">
				<s:iterator value="softVersionLogList">
					<tr>
						<td>
							<s:property value="vendorName" />
						</td>
						<td>
							<s:property value="device_model" />
						</td>
						<td>
							<s:property value="hardwareversion" />
						</td>
						<td>
							<s:property value="origSoftWareVersion" />
						</td>
						<td>
							<s:property value="targetSoftWareVersion" />
						</td>
						<td>
							<s:property value="tempName" />
						</td>
						<td align="center">
							 <s:property value="operate_type" />
						</td>
						<td align="center">
							 <s:property value="loginname" />
						</td>
						<td align="center">
							 <s:property value="operateTime" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9 align=left> 没有查询到相关数据！ </td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right">
		 	<lk:pages
				url="/itms/resource/softVerManage!getVersionLogList.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
</table>
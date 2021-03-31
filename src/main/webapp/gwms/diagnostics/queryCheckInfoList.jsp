<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>


<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>


<table width="100%" class="listtable" id=userTable>
	<thead>
       <tr>
          <th width="10%">检测人账号</th>
          <th width="15%">LOID</th>
          <th width="15%">序列号</th>
          <th width="8%">厂商</th>
          <th width="10%">型号</th>
          <th width="10%">软件版本</th>
          <th width="7%">硬件版本</th>
          <th width="5%">是否合格</th>
          <th width="10%">检测时间</th>
          <th width="5%">状态</th>
          <th width="5%">操作</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="checkDetailList != null ">
			<s:if test="checkDetailList.size() > 0">
				<s:iterator value="checkDetailList">
					<tr>
						<td align="center">
							<s:property value="acc_loginname" />
						</td>
						<td align="center">
							<s:property value="loid" />
						</td>
						<td align="center">
							<s:property value="device_serialnumber" />
						</td>
						<td align="center">
							<s:property value="vendorName" />
						</td>
						<td align="center">
							<s:property value="device_model" />
						</td>
						<td align="center">
							<s:property value="softwareversion" />
						</td>
						<td align="center">
							<s:property value="hardwareversion" />
						</td>
						<td align="center">
							<s:property value="is_qualified" />
						</td>
						<td align="center">
							<s:property value="test_time" />
						</td>
						<td align="center">
							<s:property value="status" />
						</td>
						<td align="center">
							<s:set var="state" value="#checkDetailList.state"/>
							 <s:if test='state=="1"'>
								<a href="javascript:download('<s:property value='file_path' />','<s:property value='device_serialnumber' />')">下载</a>
							</s:if>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11 align=left> 没有查询到相关数据！ </td>
			</tr>
		</s:else>
	</tbody>
		<tfoot>
			<tr>
				<td colspan="11" align="right">
			 	<lk:pages
					url="/gwms/diagnostics/deviceDiagnostics!getCheckList.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
</table>

<script LANGUAGE="JavaScript">
	function download(filePath,device_serialnumber){

		var url="<s:url value='/gwms/diagnostics/deviceDiagnostics!downLoad.action'/>";
		url+="?filePath="+filePath+"&device_serialnumber="+device_serialnumber+""
		window.location.href=url;
	}
</script>

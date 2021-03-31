<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.system.*"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>

<table width="100%" class="listtable" id=userTable>
    <caption> 组播参数下发业务信息 </caption>
	<thead>
       <tr>
          <th width="12%"> LOID</th>
          <th width="7%"> 属地</th>
          <th width="12%"> 执行时间</th>
          <th width="16%"> 设备厂商</th>
          <th width="17%"> 设备型号</th>
          <th width="16%"> 软件版本</th>
          <th width="14%"> 设备序列号</th>
          <th width="6%"> 开通状态</th>
          
       </tr>
	</thead>
	<tbody>
		<s:if test="MulticastBatchList!=null ">
			<s:if test="MulticastBatchList.size()>0">
				<s:iterator value="MulticastBatchList">
					<tr>
						<td>
							<a
								href="javascript:GoContent('<s:property value="user_id" />');">
									<s:property value="loid" />
							</a>
						</td>
						<td>
							<s:property value="city_name" />
						</td>
						<td>
							<s:property value="setTime" />
						</td>
						<td>
							<s:property value="vendor_add" />
						</td>
						<td>
							<s:property value="device_model" />
						</td>
						<td>
							<s:property value="softwareversion" />
						</td>
						<td>
							<a href="javascript:DetailDevice('<s:property value="device_id" />');">
										<s:property value="device_serialnumber" />
								</a>
						</td>
						<td>
							<s:property value="status" />
						</td>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8 align=left> 没有查询到相关数据！ </td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
			<tr>
				<td colspan="8" align="right">&nbsp;&nbsp;&nbsp;&nbsp; <lk:pages
						url="/gwms/config/countMulticastBatch!queryList.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
					<td colspan="8" align="right"><IMG
						SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
						style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
	</tfoot>
</table>
		
<script LANGUAGE="JavaScript">
$(function() {
	parent.dyniframesize();
});

function ToExcel() {
	parent.ToExcel();
}

function DetailDevice(device_id) {
	var gw_type = "";
	var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="
			+ device_id;
	window
			.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function GoContent(user_id) {
	var strpage = "<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id="
			+ user_id;
	window
			.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>
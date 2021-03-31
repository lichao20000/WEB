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
    <caption> 组播下移参数统计 </caption>
	<thead>
       <tr>
          <th width="20%"> 区域</th>
          <th width="20%"> 采集数量</th>
          <th width="20%"> 开启</th>
          <th width="20%"> 未开启</th>
          <th width="20%"> 采集失败</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="multicastDownwardsList!=null ">
			<s:if test="multicastDownwardsList.size()>0">
				<s:iterator value="multicastDownwardsList" begin='0' end='multicastDownwardsList.size()-2'>
					<tr>
					    <td align="center">
							<s:property value="cityName" />
						</td>
						<td align="center">
							<a href="javascript:detail('gatherCounts','<s:property value="cityId" />','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
									<s:property value="total" />
							</a>
						</td >
						<td align="center">
						 <a href="javascript:detail('openCounts','<s:property value="cityId" />','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
							<s:property value="openCounts" />
						 </a>
						</td>
						<td align="center">
						    <a href="javascript:detail('closeCounts','<s:property value="cityId" />','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
							 <s:property value="closeCounts" />
							</a>
						</td>
						<td align="center">
						   <a href="javascript:detail('failCounts','<s:property value="cityId" />','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
							 <s:property value="failCounts" />
							</a>
						</td>
						</tr>
				</s:iterator>
				<tr style="BACKGROUND-COLOR: #ccecff">
									<td align="center">
									  小计
									</td>
									<td align="center">
									<input type="hidden" id="hiddenCityId" value='<s:property value="multicastDownwardsList.get(multicastDownwardsList.size()-1).get('cityId')"/>'>
									<a class='green_link' href="javascript:detail('gatherCounts','all','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
										<s:property value="multicastDownwardsList.get(multicastDownwardsList.size()-1).get('gatherTotalCounts')" />
									</a>
									</td>
									<td align="center"><a class='green_link'
										href="javascript:detail('openCounts','all','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
											<s:property
												value="multicastDownwardsList.get(multicastDownwardsList.size()-1).get('openTotailCounts')" />
									</a></td>
									<td align="center"><a class='green_link'
										href="javascript:detail('closeCounts','all','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
											<s:property
												value="multicastDownwardsList.get(multicastDownwardsList.size()-1).get('closeTotalCounts')" />
									</a></td>
									<td align="center"><a class='green_link'
										href="javascript:detail('failCounts','all','<s:property value="deviceModelId" />','<s:property value="vendorId" />');">
											<s:property
												value="multicastDownwardsList.get(multicastDownwardsList.size()-1).get('gatherFailTotalCounts')" />
									</a></td>
								</tr>
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

function detail(type,cityId,deviceModelId,vendorId){
	var hiddenCityId = $("#hiddenCityId").val();
	if(cityId == "all"){
		if(null != hiddenCityId && hiddenCityId != undefined && hiddenCityId != "-1" && hiddenCityId != "" && hiddenCityId != "00"){
			cityId = hiddenCityId;
		}
	}
	
	var strpage = "<s:url value='/gwms/config/countMulticastBatch!mcDownwardsDetail.action'/>?cityId="+ cityId+"&type="+type
			+"&vendorId="+vendorId+"&deviceModelId="+deviceModelId;
	window .open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

 
</script>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">
function ToExcel(cityId) 
{
	var gw_type=$.trim($("input[@name='gw_type']").val());
	var servTypeId =  $.trim($("input[@name='servTypeId']").val());
	var startTime = $.trim($("input[@name='starttime']").val());
	var endTime = $.trim($("input[@name='endtime']").val());
	
	var page="<s:url value='/gtms/resource/countByServTypeId!toExcel.action'/>?"
			+ "&cityId=" + cityId
			+ "&gw_type=" + gw_type
			+ "&startTime="+startTime
			+ "&endTime="+endTime
			+ "&servTypeId="+servTypeId;
	document.all("childFrm").src=page;
}

function detail(cityId,servTypeId)
{
	var gw_type=$.trim($("input[@name='gw_type']").val());
	var startTime = $.trim($("input[@name='starttime']").val());
	var endTime = $.trim($("input[@name='endtime']").val());

	var page="<s:url value='/gtms/resource/countByServTypeId!getDetail.action'/>?"
			+ "cityId=" + cityId 
			+ "&servTypeId=" +servTypeId
			+ "&isOpen=2"   // 已开通业务详细信息
			+ "&gw_type=" + gw_type
			+ "&startTime="+startTime
			+ "&endTime="+endTime;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
</SCRIPT>

<%@ include file="../../head.jsp"%>
<BR>
<BR>
<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
<input type="hidden" name="servTypeId" value="<s:property value='servTypeId'/>">
<input type="hidden" name="starttime" value="<s:property value='startTime'/>">
<input type="hidden" name="endtime" value="<s:property value='endTime'/>">
<table class="listtable" width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	<thead>
		<tr>
			<s:if test="'10,14'==servTypeId">
				<th colspan=3>已开通业务设备统计查询 </th>
			</s:if>
			<s:else>
				<th colspan=2>已开通业务设备统计查询 </th>
			</s:else>
		</tr>
		<tr>
			<th>属  地</th>
			<s:if test="'10'==servTypeId || servTypeId=='10,14' ">
				<th>家庭网关宽带业务</th>
			</s:if>
			<s:if test="'14'==servTypeId || servTypeId=='10,14' ">
				<th>家庭网关VOIP业务</th>
			</s:if>
			<s:if test="'11,14'==servTypeId">
				<th>家庭网关纯语音业务</th>
			</s:if>
		</tr>
	</thead>
	<tbody>
		<s:if test="haveResultList!=null && haveResultList.size()>0">
			<s:iterator value="haveResultList">
				<tr>
					<td ><s:property value="city_name" /></td>
					<s:if test="'10'==servTypeId || servTypeId=='10,14' ">
						<td >
							<s:if test="0==internetValue">
								<s:property value="internetValue" />
							</s:if>
							<s:else>
								<a href="javascript:onclick=detail('<s:property value="city_id"/>','10');">
									<s:property value="internetValue" />
								</a>
							</s:else>
						</td>
					</s:if>
					
					<s:if test="'14'==servTypeId || servTypeId=='10,14' ">
						<td >
							<s:if test="0==voipValue">
								<s:property value="voipValue" />
							</s:if>
							<s:else>
								<a href="javascript:onclick=detail('<s:property value="city_id"/>','14');">
									<s:property value="voipValue" />
								</a>
							</s:else>
						</td>
					</s:if>
					
					<s:if test="'11,14'==servTypeId">
						<td >
							<s:if test="0==voipValue">
								<s:property value="voipValue" />
							</s:if>
							<s:else>
								<a href="javascript:onclick=detail('<s:property value="city_id"/>','11,14');">
									<s:property value="voipValue" />
								</a>
							</s:else>
						</td>
					</s:if>
				</tr>
			</s:iterator>
			<TR>
				<s:if test="'10,14'==servTypeId">
					<TD colspan="3">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表' 
							style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>')">
					</TD>
				</s:if>
				<s:else>
					<TD colspan="2">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表' 
							style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>')">
					</TD>
				</s:else>
			</TR>
		</s:if>
		<s:else>
			<TR>
				<s:if test="'10,14'==servTypeId">
					<TD colspan="3">系统中没有检索到相关数据！</TD>
				</s:if>
				<s:else>
					<TD colspan="2">系统中没有检索到相关数据！</TD>
				</s:else>
			</TR>
		</s:else>
	</tbody>
	<TR STYLE="display: none">
		<TD colspan="3">
			<iframe id="childFrm" src=""></iframe>
		</TD>
	</TR>
</table>
<%@ include file="../../foot.jsp"%>
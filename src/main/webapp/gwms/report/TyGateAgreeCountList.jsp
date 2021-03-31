<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>天翼网关版本一致率报表报表</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>本地网</th>
						<th>天翼网关总数</th>
						<th>非统一版本天翼网关数</th>
						<th>版本一致率</th>
					</tr>

					<s:iterator value="countList" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<td  class=column>
								<strong><s:property value="city_name"/></strong>
							</td>
							<td>
								<a href="javascript:getDetailList('<s:property value="city_id"/>',
																	'<s:property value="startTime"/>',
																	'<s:property value="endTime"/>',
																	'-1',
																	'<s:property value="gwType"/>',
						 											'<s:property value="isExcludeUpgrade"/>',
						 											'<s:property value="recent_start_Time"/>',
																 	'<s:property value="recent_end_Time"/>'
																 	);">
					 				<s:property value="allTyCount"/>
						 		</a>
							</td>
							<td>
								<a href="javascript:getDetailList('<s:property value="city_id"/>',
																	'<s:property value="startTime"/>',
																	'<s:property value="endTime"/>',
																	'0',
																	'<s:property value="gwType"/>',
						 											'<s:property value="isExcludeUpgrade"/>',
						 											'<s:property value="recent_start_Time"/>',
																 	'<s:property value="recent_end_Time"/>'
																	);">
					 				<s:property value="notTyCount"/>
						 		</a>
							</td>
							<td>
								<s:property value="rate"/>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForExcel('<s:property value="cityId"/>',
						 										'<s:property value="startTime"/>',
						 										'<s:property value="endTime"/>',
						 										'<s:property value="gwType"/>',
						 										'<s:property value="isExcludeUpgrade"/>',
						 										'<s:property value="recent_start_Time"/>',
																'<s:property value="recent_end_Time"/>'
						 										);">
					 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
						<td class=column1 align="right">
<%-- 							<strong> --%>
<%-- 								统计截止时间：<s:property value="entTime"/> --%>
<%-- 							</strong> --%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>厂商</th>
						<th>型号</th>
						<th>硬件版本</th>
						<th>非最新版本数</th>
						<th>最新版本数</th>
						<th>天翼网关总数</th>
						<th>天翼网关版本统一率</th>
					</tr>

					<s:iterator value="countList" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<td  class=column>
								<s:property value="vendor_add"/>
							</td>
							<td  class=column>
								<s:property value="device_model"/>
							</td>
							<td  class=column>
								<s:property value="hardwareversion"/>
							</td>
							<td>
								<a href="javascript:getdetailListJXDX('<s:property value="gwType"/>',
																 '<s:property value="vendor_id"/>',
																 '<s:property value="device_model_id"/>',
																 '<s:property value="isExcludeUpgrade"/>',
																 '<s:property value="startTime"/>',
																 '<s:property value="endTime"/>',
																 '<s:property value="recent_start_Time"/>',
																 '<s:property value="recent_end_Time"/>',
																 '<s:property value="hardwareversion"/>',
																 '0');">
					 				<s:property value="sum2"/>
						 		</a>
							</td>
							<td>
								<a href="javascript:getdetailListJXDX('<s:property value="gwType"/>',
																 '<s:property value="vendor_id"/>',
																 '<s:property value="device_model_id"/>',
																 '<s:property value="isExcludeUpgrade"/>',
																 '<s:property value="startTime"/>',
																 '<s:property value="endTime"/>',
																 '<s:property value="recent_start_Time"/>',
																 '<s:property value="recent_end_Time"/>',
																 '<s:property value="hardwareversion"/>',
																 '1');">
					 				<s:property value="sum1"/>
						 		</a>
							</td>
							<td>
								<a href="javascript:getdetailListJXDX('<s:property value="gwType"/>',
																 '<s:property value="vendor_id"/>',
																 '<s:property value="device_model_id"/>',
																 '<s:property value="isExcludeUpgrade"/>',
																 '<s:property value="startTime"/>',
																 '<s:property value="endTime"/>',
																 '<s:property value="recent_start_Time"/>',
																 '<s:property value="recent_end_Time"/>',
																 '<s:property value="hardwareversion"/>',
																 '-1');">
					 				<s:property value="total"/>
						 		</a>
							</td>
							<td>
								<s:property value="total_percent"/>
							</td>
						</tr>
					</s:iterator>
					<tr bgcolor="#FFFFFF">
						<td colspan="6">
							 <Strong>主流天翼网关版本统一率</Strong>
						</td>
						<td colspan="6">
							<Strong><s:property value="percent"/></Strong>
						</td>
					</tr>

				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForExcelJXDX('<s:property value="gwType"/>',
																 '<s:property value="vendorId"/>',
																 '<s:property value="deviceModelId"/>',
																 '<s:property value="isExcludeUpgrade"/>',
																 '<s:property value="startTime"/>',
																 '<s:property value="endTime"/>',
																 '<s:property value="recent_start_Time"/>',
																 '<s:property value="recent_end_Time"/>');">
					 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

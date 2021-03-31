<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">

<table class="listtable">
	<caption>统计结果</caption>
	<thead>
		<tr>
			<th rowspan="2">属地</th>
			<th rowspan="2">总配置数</th>
			<th rowspan="2">成功</th>
			<ms:inArea areaCode="sd_dx" notInMode="true">
			<th rowspan="2">执行中</th>
			</ms:inArea>
			<th rowspan="2">未触发</th>
			<th colspan="2">失败</th>
			<th rowspan="2">成功率</th>
		</tr>
		<tr>
			<th>等待重做</th>
			<th>彻底失败</th>
		</tr>
	
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<ms:inArea areaCode="sd_dx" notInMode="false">
					<td>
						<s:property value="city_name" /> 
					</td>
					</ms:inArea>
					<ms:inArea areaCode="sd_dx" notInMode="true">
					<td>
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<s:if test='city_name!="省中心"'>
								<a href="javascript:countBycityId('<s:property value="city_id"/>',
								'<s:property value="starttime"/>',
								'<s:property value="endtime"/>',
								'<s:property value="vendor_id"/>',
								'<s:property value="device_model_id"/>');">
								<s:property value="city_name" />
								</a>
							</s:if>
							<s:else>
								<s:property value="city_name" />
							</s:else>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
							<a href="javascript:countBycityId('<s:property value="city_id"/>',
							'<s:property value="starttime"/>',
							'<s:property value="endtime"/>',
							'<s:property value="vendor_id"/>',
							'<s:property value="device_model_id"/>');">
							<s:property value="city_name" />
							</a>
						</ms:inArea>

					</td>
					</ms:inArea>
					<td>
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<s:if test='city_name!="省中心"'>
								<a href="javascript:openDev('<s:property value="city_id"/>',
								'<s:property value="starttime1"/>',
								'<s:property value="endtime1"/>',
								'','','',
								'<s:property value="vendor_id"/>',
								'<s:property value="device_model_id"/>');">
								<s:property value="allup" />
								</a>
							</s:if>
							<s:else>
								<s:property value="allup" />
							</s:else>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
						<a href="javascript:openDev('<s:property value="city_id"/>',
													'<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>',
													'','','',
													'<s:property value="vendor_id"/>',
													'<s:property value="device_model_id"/>');">
							<s:property value="allup" /> 
						</a>
						</ms:inArea>
					</td>
					<td>
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<s:if test='city_name!="省中心"'>
								<a href="javascript:openDev('<s:property value="city_id"/>',
								'<s:property value="starttime1"/>',
								'<s:property value="endtime1"/>',
								'100','1','',
								'<s:property value="vendor_id"/>',
								'<s:property value="device_model_id"/>');">
								<s:property value="successnum" />
								</a>
							</s:if>
							<s:else>
								<s:property value="successnum" />
							</s:else>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
						<a href="javascript:openDev('<s:property value="city_id"/>',
													'<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>',
													'100','1','',
													'<s:property value="vendor_id"/>',
													'<s:property value="device_model_id"/>');">
							<s:property value="successnum" /> 
						</a>
						</ms:inArea>
					</td>
					<ms:inArea areaCode="sd_dx,jx_dx" notInMode="true">
					<td>
						<a href="javascript:openDev('<s:property value="city_id"/>',
													'<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>',
													'1234','','',
													'<s:property value="vendor_id"/>',
													'<s:property value="device_model_id"/>');">
							<s:property value="runningnum" /> 
						</a>
					</td>
					</ms:inArea>
					<ms:inArea areaCode="jx_dx" notInMode="false">
						<td>
							<s:if test='city_name!="省中心"'>
							<a href="javascript:openDev('<s:property value="city_id"/>',
							'<s:property value="starttime1"/>',
							'<s:property value="endtime1"/>',
							'1234','','',
							'<s:property value="vendor_id"/>',
							'<s:property value="device_model_id"/>');">
							<s:property value="runningnum" />
							</a>
							</s:if>
							<s:else>
								<s:property value="runningnum" />
							</s:else>
						</td>
					</ms:inArea>

					<td>
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<s:if test='city_name!="省中心"'>
								<a href="javascript:openDev('<s:property value="city_id"/>',
								'<s:property value="starttime1"/>',
								'<s:property value="endtime1"/>',
								'0','0','',
								'<s:property value="vendor_id"/>',
								'<s:property value="device_model_id"/>');">
								<s:property value="noupnum" />
								</a>
							</s:if>
							<s:else>
								<s:property value="noupnum" />
							</s:else>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
						<a href="javascript:openDev('<s:property value="city_id"/>',
													'<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>',
													'0','0','',
													'<s:property value="vendor_id"/>',
													'<s:property value="device_model_id"/>');">
							<s:property value="noupnum" /> 
						</a>
						</ms:inArea>
					</td>
					<td>
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<s:if test='city_name!="省中心"'>
								<a href="javascript:openDev('<s:property value="city_id"/>',
								'<s:property value="starttime1"/>',
								'<s:property value="endtime1"/>',
								'0','not1','',
								'<s:property value="vendor_id"/>',
								'<s:property value="device_model_id"/>');">
								<s:property value="nextnum" />
								</a>
							</s:if>
							<s:else>
								<s:property value="nextnum" />
							</s:else>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
						<a href="javascript:openDev('<s:property value="city_id"/>',
													'<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>',
													'0','not1','',
													'<s:property value="vendor_id"/>',
													'<s:property value="device_model_id"/>');">
							<s:property value="nextnum" /> 
						</a>
						</ms:inArea>
					</td>
					<td>
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<s:if test='city_name!="省中心"'>
								<a href="javascript:openDev('<s:property value="city_id"/>',
								'<s:property value="starttime1"/>',
								'<s:property value="endtime1"/>',
								'100','not1','',
								'<s:property value="vendor_id"/>',
								'<s:property value="device_model_id"/>');">
								<s:property value="failnum" />
								</a>
							</s:if>
							<s:else>
								<s:property value="failnum" />
							</s:else>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
						<a href="javascript:openDev('<s:property value="city_id"/>',
													'<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>',
													'100','not1','',
													'<s:property value="vendor_id"/>',
													'<s:property value="device_model_id"/>');">
							<s:property value="failnum" /> 
						</a>
						</ms:inArea>
					</td>
					<td>
						<s:property value="percent" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<ms:inArea areaCode="sd_dx" notInMode="true">
			<td colspan="8">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>',
															'<s:property value="starttime1"/>',
															'<s:property value="endtime1"/>',
															'<s:property value="vendorId"/>',
															'<s:property value="deviceModelId"/>')">
			</td>
			</ms:inArea>
			<ms:inArea areaCode="sd_dx" notInMode="false">
			<td colspan="7">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>',
															'<s:property value="starttime1"/>',
															'<s:property value="endtime1"/>',
															'<s:property value="vendorId"/>',
															'<s:property value="deviceModelId"/>')">
			</td>
			</ms:inArea>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

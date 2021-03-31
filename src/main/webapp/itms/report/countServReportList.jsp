<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">


<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<thead>
		<tr>
			<th>
				������
			</th>
			<th>
				���û���
			</th>
			<th>
				����û�
			</th>
			<th>
				IPTV�û�
			</th>
			<th>
				VOIP�û�
			</th>
			<th>
				���ռ��
			</th>
			<th>
				ITVռ��
			</th>
			<th>
				VOIPռ��
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr style="vertical-align:middle; text-align:center;">
					<td>
						<s:property value="city_name"  />
					</td>
					<td>
						<s:if test="totalNum==0">
							<s:property value="totalNum" />
						</s:if>
						<s:else>
							<a href="javascript:openDetails('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="specId"/>','<s:property value="is_active"/>','10,11,14');">
								<s:property value="totalNum" />
							</a>
						</s:else>
					</td>
					<td>
						<s:if test="kuandaiNum==0">
							<s:property value="kuandaiNum" />
						</s:if>
						<s:else>
							<a href="javascript:openDetails('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="specId"/>','<s:property value="is_active"/>','10');">
								 <s:property value="kuandaiNum" /> 
							</a>
						</s:else>
					</td>
					<td>
						<s:if test="IPTVNum==0">
							<s:property value="IPTVNum" />
						</s:if>
						<s:else>
							<a href="javascript:openDetails('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="specId"/>','<s:property value="is_active"/>','11');">
								<s:property value="IPTVNum" /> 
							</a>
						</s:else>
					</td>
					<td>
						<s:if test="VOIPNum==0">
							<s:property value="VOIPNum" />
						</s:if>
						<s:else>
							<a href="javascript:openDetails('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="specId"/>','<s:property value="is_active"/>','14');">
								 <s:property value="VOIPNum" /> 
							</a>
						</s:else>
					</td>
					<td>
						<s:property value="kuandai_scale"  />
					</td>
					<td>
						<s:property value="IPTV_scale" />
					</td>
					<td>
						<s:property value="VOIP_scale" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' 
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="specId"/>','<s:property value="is_active"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="9">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>



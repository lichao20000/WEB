<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td>
			<table width="100%" border=0 cellspacing=1 cellpadding=2
				bgcolor=#999999 id=userTable>
				<tr>
					<th colspan="4">
						查询结果
					</th>
				</tr>
				<tr>
					<td class="green_title" align='center' width="25%">
						属地
					</td>
					<td class="green_title" align='center' width="25%">
						已部署数
					</td>
					<td class="green_title" align='center' width="25%">
						未部署数
					</td>
					<td class="green_title" align='center' width="25%">
						部署率
					</td>
				</tr>
				<s:if test="data.size()>0">
					<s:iterator value="data">
						<tr class="column">
							<td class="" align='center' width="25%">
								<s:if test="isAll==1">
									<strong><s:property value="city_name" /> </strong>
								</s:if>
								<s:else>
									<s:property value="city_name" />
								</s:else>
							</td>
							<td class="" align='center' width="25%">
								<a
									href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','1','<s:property value="isAll"/>');">
									<s:property value="detotal" /> </a>

							</td>
							<td class="" align='center' width="25%">
								<a
									href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','0','<s:property value="isAll"/>');">
									<s:property value="nototal" /> </a>
							</td>
							<td class="" align='center' width="25%">
								<s:property value="percent" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
			</table>
		</td>
	</tr>
	<tr bgcolor=#999999>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%"
				align="center">
				<tr bgcolor="#FFFFFF">
					<td class=column align="center" width="40">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0'
							ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
					</td>
					<td class=column align="right">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>



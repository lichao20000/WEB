<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>�������ذ汾һ���ʱ�����</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>������</th>
						<th>������������</th>
						<th>��ͳһ�汾����������</th>
						<th>�汾һ����</th>
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
<%-- 								ͳ�ƽ�ֹʱ�䣺<s:property value="entTime"/> --%>
<%-- 							</strong> --%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

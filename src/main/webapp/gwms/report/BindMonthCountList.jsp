<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>绑定率月报表</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>属地</th>
						<th>上报上来的与用户有绑定关系的资源数量</th>
						<th>CRM工单过来的在用用户数</th>
						<th>设备占用户比例</th>
					</tr>

					<s:iterator value="countList" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<td  class=column>
								<s:if test="'true'==hasChild">
									<a href="javascript:queryData('<s:property value="city_id"/>','<s:property value="endDataInt"/>');">
					 					<strong><s:property value="city_name"/></strong>
						 			</a>
								</s:if>
								<s:else>
									<strong><s:property value="city_name"/></strong>
								</s:else>
							</td>
							<td>
								<a href="javascript:openDevice('<s:property value="city_id"/>','<s:property value="endDataInt"/>','<s:property value="gwType"/>');">
					 				<s:property value="deviceCount"/>
						 		</a>
							</td>
							<td>
								<a href="javascript:openUser('<s:property value="city_id"/>','<s:property value="endDataInt"/>','<s:property value="gwType"/>');">
					 				<s:property value="userCount"/>
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
						 	<a href="javascript:queryDataForExcel('<s:property value="cityId"/>','<s:property value="endDataInt"/>');">
					 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
						<td class=column1 align="right">
							<strong>
								统计截止时间：<s:property value="endData"/>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

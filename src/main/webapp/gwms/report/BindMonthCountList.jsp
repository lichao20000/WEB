<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" align="center">
				<strong>�����±���</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>����</th>
						<th>�ϱ����������û��а󶨹�ϵ����Դ����</th>
						<th>CRM���������������û���</th>
						<th>�豸ռ�û�����</th>
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
								ͳ�ƽ�ֹʱ�䣺<s:property value="endData"/>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

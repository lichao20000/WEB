<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="50%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th>ส๔ตุ</th>
						<th>H248</th>
						<th>SIP</th>
					</tr>

					<s:iterator value="dataList" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<td  class=column>
								<s:property value="city_name"/>
							</td>
							<td  class=column>
							<a href="javascript:getdetailList('<s:property value="gw_type"/>',
															  '<s:property value="city_id"/>',
															  'h248');">
								<s:property value="h248"/>
							</a>
							</td>
							<td  class=column>
							<a href="javascript:getdetailList('<s:property value="gw_type"/>',
															  '<s:property value="city_id"/>',
															  'sip');">
								<s:property value="sip"/>
							</a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="50%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForExcel('<s:property value="gw_type"/>',
																 '<s:property value="cityId"/>');">
					 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

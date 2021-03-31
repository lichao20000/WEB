<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title></title>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	</head>
	<body>
		<table class="listtable">
			<caption>
				ͳ�ƽ��
			</caption>
			<thead>
				<tr>
					<th> ������ </th>
					<th> �������� </th>
					<th>  δ�·���������</th>
					<th>  �·���������</th>
					<th>  �·��ɹ���</th>
					<th>  �·�ʧ����</th>
					<th> ���·��ɹ��� </th>
				</tr>
			</thead>
			<tbody>
				<s:if test="data.size()>0">
					<s:iterator value="data" var="map1">
						<tr>
							<td align="center">
								<a href="javascript:countBycityId('<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="selectTypeId"/>');">
									<s:property value="cityName" /> </a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('3','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>');">
									<s:property value="totalNum" /> </a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('0','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>');">
									<s:property value="notOpenNum" /> </a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('2','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>');">
									<s:property value="openNum" /></a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('1','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>');">
									<s:property value="successOpenNum" /> </a>
							</td>
							<td align="center">
								<a href="javascript:openHgw('-1','<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>');">
									<s:property value="failOpenNum" /> </a>
							</td>
							<td align="center">
								<s:property value="successRate" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tfoot>
						<tr>
							<td align="left">
								û�������Ϣ
							</td>
						</tr>
					</tfoot>
				</s:else>
			</tbody>
			<tfoot>
				<tr>
					<td colspan='7'>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
							style='cursor: hand'
							onclick="ToExcel('<s:property value="cityId"/>','<s:property value="start"/>','<s:property value="end"/>','<s:property value="parentCityId"/>','<s:property value="selectTypeId"/>')"/>
					</td>
				</tr>
			</tfoot>
			<tr STYLE="display: none">
				<td colspan="7">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</body>
</html>

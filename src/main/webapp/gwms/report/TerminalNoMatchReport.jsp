<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

		function getDetailList(city_id){
			var page="<s:url value='/gwms/report/terminalNoMatchReport!queryDetailList.action'/>?"+ "cityId=" + city_id;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
		
		function getNoMatchDetailList(city_id){
			var page="<s:url value='/gwms/report/terminalNoMatchReport!queryNoMatchDetailList.action'/>?"+ "cityId=" + city_id;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
		
		function getSrcapDevDetailList(city_id){
			var page="<s:url value='/gwms/report/terminalNoMatchReport!querySrcapDevDetailList.action'/>?"+ "cityId=" + city_id;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
		function getOldDevDetail(city_id){
        	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldDevDetailList.action'/>?"+ "cityId=" + city_id;
        	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
        }
		function getOldCompleteDevDetail(city_id){
        	var page="<s:url value='/gwms/report/terminalNoMatchReport!queryOldCompleteDevDetailList.action'/>?"+ "cityId=" + city_id;
        	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
        }
</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="90%" align="center" style="margin-top: 50px">
		<tr>
			<th class=column1 height="25" align="center"><strong>�ն��������ײͲ�ƥ��ͱ����ն˱���</strong>
			</th>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999">
					<tr bgcolor="#FFFFFF">
						<th rowspan="2">����</th>
						<th colspan="4">�ն��������ײͲ�ƥ��</th>
						<th colspan="5">�Ͼ��ն˵ĸ���</th>
						<th rowspan="2" colspan="2">�����ն��ٴ�����������4��30�պ��ֹ���ߣ�</th>
						<th rowspan="2" colspan="2">�ն��������ײͲ�ƥ��������</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<th>T0ֵ</th>
						<th>���������</th>
						<th>�ۼ������</th>
						<th>��ƥ���ն��굥</th>
						<th>T0ֵ</th>
                        <th>δ�����</th>
                        <th>δ������굥</th>
                        <th>�����</th>
                        <th>������굥</th>
					</tr>
					<s:iterator value="data">
						<tr bgcolor="#FFFFFF">
							<td class=column width="8%">
								<strong><s:property value="city_name" /></strong>
							</td>
							<td width="6%">
								<s:property value="t0num" />
							</td>
							<td width="7%">
								<s:property value="daynomatchnum" />
							</td>
							<td width="7%">
								<s:property value="allnomatchnum" />
							</td>
							<td width="8%">
								<a href="javascript:getNoMatchDetailList('<s:property value="city_id" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
							<td width="7%">
                            	<s:property value="oldt0" />
                            </td>
                            <td width="7%">
                                <s:property value="oldnum" />
                            </td>
                            <td width="8%">
                                <a href="javascript:getOldDevDetail('<s:property value="city_id" />');">
                                    		��ϸ��Ϣ
                                </a>
                            </td>
                            <td width="7%">
                                <s:property value="old_complete_num" />
                            </td>
                            <td width="10%">
                                <a href="javascript:getOldCompleteDevDetail('<s:property value="city_id" />');">
                                    		��ϸ��Ϣ
                                </a>
                            </td>
                            
							<td width="7%">
								<s:property value="scrapdevnum" />
							</td>
							<td width="8%">
								<a href="javascript:getSrcapDevDetailList('<s:property value="city_id" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
							<td width="5%">
								<s:property value="alladdnomatchnum" />
							</td>
							<td width="8%">
								<a href="javascript:getDetailList('<s:property value="city_id" />');">
					 				��ϸ��Ϣ
						 		</a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>

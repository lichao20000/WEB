<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PON�ۺ������ӻ���·����</title>
<%
	/**
	 * e8-cҵ���ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>�ۺ������ӻ���·��Ϣ </caption>
	<thead>
		<tr>
			<th>����</th>
			<th>E8C�ն�����</th>
			<th>E8C�ն˹�·�ӻ���</th>
			<th>�ӻ���·ռ��</th>
			<th>�⹦��С��30DB��¼��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="ponlist!=null">
			<s:if test="ponlist.size()>0">
				<s:iterator value="ponlist">
						<tr>
							<td>
								<s:property value="area_name" />
							</td>
							<td>
								<s:property value="e8cValue" />
							</td>
							<td>
							<a href="javascript:openPon('<s:property value="city_id"/>')">
								<s:property value="ponValue" />
							</a>
							</td>
							<td>
								<s:property value="pert" />
							</td>
							<td>
							<a href="javascript:openLessPon('<s:property value="city_id"/>')">
								<s:property value="lessPonValue" />
							</a>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=5>ϵͳ��û�в�ѯ����Ҫ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=5>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="5" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER="0" ALT="�����б�" style="cursor: hand"  onclick="javaScript:ToExcel();">
			</td>
		</tr>
		<tr>
			<td colspan="5" align="left">
				<b>
					��ע���ӻ���·�������ִ�������5�Σ�������;&nbsp;&nbsp;&nbsp;�⹦��С��30DB��¼��:���ִ�������5�Σ������������ҹ⹦��С��30DB;
				</b>
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>
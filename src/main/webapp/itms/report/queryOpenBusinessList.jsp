<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25" colspan="5" align="center"><strong>
					�����û���ѯͳ�� </strong></td>
		</tr>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<!-- ������ı��� -->
					<tr>
						<s:iterator value="title" status="status">
							<td class="green_title"><s:property
									value="title[#status.index]" /></td>
						</s:iterator>
					</tr>

					<!-- ��ʼ�������� -->
					<s:if test="userMap.size()>0">
						<s:iterator value="userMap">
							<tr bgcolor="#ffffff">

								<!-- ������Ϣ����ʾ�������ص����ƣ�������ID -->
								<!-- ���¼�������Ҫ����¼�hasCityId��ʾ�ܲ��ܱ���� -->
								<s:if test='"true".equals(hasCityId)'>
									<td class=column nowrap align="center"><a
										href="javascript:queryData('<s:property value="city_id"/>','<s:property value="city_name"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="city_name" />
									</a></td>
								</s:if>
								<!-- û���¼����� -->
								<s:else>
									<td class=column nowrap align="center"><s:property
											value="city_name" /></td>
								</s:else>

								<!-- �û����� -->
								<!-- �������Ϊ0��û�а취����ģ� -->
								<s:if test='"0".equals(total+"")'>
									<td class=column nowrap align="center"><s:property
											value="total" /></td>
								</s:if>
								<!-- ������Ϊ0 -->
								<s:else>
									<td class=column nowrap align="center"><a
										href="javascript:queryDataTotal('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="total" />
									</a></td>
								</s:else>

								<!-- ��ͨ�ɹ��� -->
								<!-- �������Ϊ0��û�а취����ģ� -->
								<s:if test='"0".equals(success+"")'>
									<td class=column nowrap align="center"><s:property
											value="success" /></td>
								</s:if>
								<!-- ������Ϊ0 -->
								<s:else>
									<td class=column nowrap align="center"><a
										href="javascript:queryDataBindSuccess('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="success" />
									</a></td>
								</s:else>


								<!-- ��ͨʧ����-->
								<!-- �������Ϊ0��û�а취����ģ� -->
								<s:if test='"0".equals(fail+"")'>
									<td class=column nowrap align="center"><s:property value="fail" /></td>
								</s:if>
								<!-- ������Ϊ0 -->
								<s:else>
									<td class=column nowrap align="center"><a
										href="javascript:queryDataBindFail('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="fail" />
									</a></td>
								</s:else>

								<!-- �ɹ�����û�а취����� -->
								<td class=column nowrap align="center"><s:property
										value="pert" /></td>
								<%-- 

								<td class=column nowrap align="center"><s:property
										value="city_name" /></td>
								<td class=column nowrap align="center"><s:property
										value="total" /></td>
								<td class=column nowrap align="center"><s:property
										value="succcess" /></td>
								<td class=column nowrap align="center"><s:property
										value="fail" /></td>
								<td class=column nowrap align="center"><s:property
										value="pert" /></td> --%>
							</tr>
						</s:iterator>
					</s:if>

					<!-- ϵͳû����Ϣ -->
					<s:else>
						<tr>
							<td colspan=8 align=left class=column>ϵͳû����ص��û���Ϣ!</td>
						</tr>
					</s:else>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="right" bgcolor="#000000">
					<s:if test='"print".equals(isReport)'>
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="right" width="100"><a
								href="javascript:window.print()">��ӡ</a></td>
						</tr>
					</s:if>
					<s:else>
						<tr bgcolor="#FFFFFF">
							<td class=column1 align="right" width="100"><a
								href="javascript:queryDataForExcel('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
									<img src="../../images/excel.gif" border="0" width="16"
									height="16"></img>
							</a></td>
						</tr>
					</s:else>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
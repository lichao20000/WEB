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
					当日用户查询统计 </strong></td>
		</tr>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<!-- 遍历表的标题 -->
					<tr>
						<s:iterator value="title" status="status">
							<td class="green_title"><s:property
									value="title[#status.index]" /></td>
						</s:iterator>
					</tr>

					<!-- 开始遍历数据 -->
					<s:if test="userMap.size()>0">
						<s:iterator value="userMap">
							<tr bgcolor="#ffffff">

								<!-- 属地信息，显示的是属地的名称，而不是ID -->
								<!-- 有下级城市需要点击事件hasCityId表示能不能被点击 -->
								<s:if test='"true".equals(hasCityId)'>
									<td class=column nowrap align="center"><a
										href="javascript:queryData('<s:property value="city_id"/>','<s:property value="city_name"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="city_name" />
									</a></td>
								</s:if>
								<!-- 没有下级城市 -->
								<s:else>
									<td class=column nowrap align="center"><s:property
											value="city_name" /></td>
								</s:else>

								<!-- 用户总数 -->
								<!-- 如果数量为0是没有办法点击的！ -->
								<s:if test='"0".equals(total+"")'>
									<td class=column nowrap align="center"><s:property
											value="total" /></td>
								</s:if>
								<!-- 数量不为0 -->
								<s:else>
									<td class=column nowrap align="center"><a
										href="javascript:queryDataTotal('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="total" />
									</a></td>
								</s:else>

								<!-- 开通成功数 -->
								<!-- 如果数量为0是没有办法点击的！ -->
								<s:if test='"0".equals(success+"")'>
									<td class=column nowrap align="center"><s:property
											value="success" /></td>
								</s:if>
								<!-- 数量不为0 -->
								<s:else>
									<td class=column nowrap align="center"><a
										href="javascript:queryDataBindSuccess('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="success" />
									</a></td>
								</s:else>


								<!-- 开通失败数-->
								<!-- 如果数量为0是没有办法点击的！ -->
								<s:if test='"0".equals(fail+"")'>
									<td class=column nowrap align="center"><s:property value="fail" /></td>
								</s:if>
								<!-- 数量不为0 -->
								<s:else>
									<td class=column nowrap align="center"><a
										href="javascript:queryDataBindFail('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
											<s:property value="fail" />
									</a></td>
								</s:else>

								<!-- 成功率是没有办法点击的 -->
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

					<!-- 系统没有信息 -->
					<s:else>
						<tr>
							<td colspan=8 align=left class=column>系统没有相关的用户信息!</td>
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
								href="javascript:window.print()">打印</a></td>
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
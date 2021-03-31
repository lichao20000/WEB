<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>山东联通RMS平台机顶盒设备统计</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function queryDataForExcel(){
		var page = "<s:url value='/itms/resource/stbDeviceCount!deviceCountList.action'/>?returnType=excel"
		document.all("childFrm").src=page;
	}
	function getDetailInfo(vendorId,cityId){
		var page="<s:url value='/itms/resource/stbDeviceCount!getDetailInfo.action'/>?"
			+"vendorId="+vendorId
			+ "&cityId=" + cityId;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>
</head>
<body>
	<table class="listtable">
		<caption>
			机顶盒设备统计
		</caption>
		<thead>
			<tr bgcolor="#FFFFFF">
				<s:iterator value="titleList" var="titleList" status="servSt">
					<th align="center">
						<s:property />
					</th>
				</s:iterator>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td nowrap class=column align='right'>
						<s:property value="vendorName" />
					</td>
					<s:iterator value="cityList" var="map2">
						<td align="center">
							<s:iterator value="map1" var="map1id" status="st">
								<s:if test="key.equals(city_id)">
									<s:if test="value==0">
										<s:property value="value" />
									</s:if>
									<s:else>
										<a href="javascript:getDetailInfo('<s:property value="vendorId"/>','<s:property value="city_id"/>');">
											<s:property value="value" />
										</a>
									</s:else>
								</s:if>
							</s:iterator>
						</td>
					</s:iterator>
					<td align="center">
						<s:if test="total==0">
							<s:property value="total" />
						</s:if>
						<s:else>
							<a href="javascript:getDetailInfo('<s:property value="vendorId"/>','<s:property value="city_id"/>');">
								<s:property value="total" />
							</a>
						</s:else>
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td class=column  colspan='<s:property value="titleList.size()" />'>
					&nbsp;&nbsp;
					<a href="javascript:queryDataForExcel();">
		 				<img src="../../images/excel.gif"  border="0" width="16" height="16"></img>
			 		</a>
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

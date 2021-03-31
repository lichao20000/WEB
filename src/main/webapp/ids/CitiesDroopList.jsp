<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>地市光衰详细列表</title>
<link rel="stylesheet" href="../css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../css3/global.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function openCus(cityId,starttime,endtime,operType){
	var page="<s:url value='/ids/cityDroop!queryByDroopList.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime
		+ "&cityId=" + cityId
		+ "&operType=" + operType;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}
</script>
</head>
<body>
<table class="listtable">
	<caption>
		地市光衰达标数据展示
	</caption>
	<thead>
		<tr>
			<th width="34%">
				区域
			</th>
			<th width="33%">
				光衰达标数
			</th>
			<th width="33%">
				光衰未达标数
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list.size()>0">
			<s:iterator value="list">
				<tr bgcolor="#FFFFFF">
					<td>
							<s:property value="cityName" />
					</td>
					<td>
						<a href="javascript:openCus('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','1');">
							<s:property value="droopNum" /> </a>
					</td>
					<td>
						<a href="javascript:openCus('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','2');">
							<s:property value="disDroopNum" /> </a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tr STYLE="display: none">
		<td colspan="2">
			<iframe id="childFrm" name="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
</body>
</html>
<%@ include file="../foot.jsp"%>

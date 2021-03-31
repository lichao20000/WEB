<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>全省非4K机顶盒分布结果</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
		parent.isShowButton(true);
		parent.isCountDesc("");
	});
	
	function toExcel()
	{
		var is_exp=$("input[@name='is_exp']").val();
		if(is_exp==0){
			$("input[@name='is_exp']").val("1");
			parent.query('exp',$("input[@name='queryTime']").val());
		}else{
			alert("报表正在导出中，请勿重复操作！");
		}
	}
</script>
</head>

<body>
<input type='hidden' name="is_exp" value="0" />
<input type='hidden' name="queryTime" value="<s:property value='queryTime' />" />
<table>
<tr>
	<td>
		<table class="listtable">
			<caption>全省非4K机顶盒分布</caption>
			<thead>
				<tr>
					<th>单位</th>
					<th>华为</th>
					<th>创维</th>
					<th>中兴</th>
					<th>烽火</th>
					<th>长虹</th>
					<th>百事通</th>
					<th>杰赛</th>
					<th>其他</th>
					<th>小计</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="data!=null && data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="hw_num" /></td>
							<td><s:property value="cw_num" /></td>
							<td><s:property value="zx_num" /></td>
							<td><s:property value="fh_num" /></td>
							<td><s:property value="ch_num" /></td>
							<td><s:property value="bst_num" /></td>
							<td><s:property value="js_num" /></td>
							<td><s:property value="other_num" /></td>
							<td><s:property value="city_all_num" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=10>没有统计到数据！</td>
					</tr>
				</s:else>
			</tbody>
			<tfoot>
				<tr id="exp_img">
					<td colspan="10">
						<img src="<s:url value="/images/excel.gif"/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()">
					</td>
				</tr>
			</tfoot>
			<tr STYLE="display: none">
				<td colspan="10">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>
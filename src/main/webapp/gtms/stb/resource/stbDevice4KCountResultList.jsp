<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>全省4K机顶盒分布及软探针部署报表结果</title>
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
			<caption>全省4K机顶盒分布及软探针部署报表</caption>
			<thead>
				<tr>
					<th rowspan="2">单位</th>
					<th colspan="2">华为</th>
					<th colspan="2">创维</th>
					<th colspan="2">中兴</th>
					<th colspan="2">烽火</th>
					<th colspan="2">长虹</th>
					<th colspan="2">百事通</th>
					<th colspan="2">杰赛</th>
					<th colspan="2">其他</th>
					<th colspan="2">小计</th>
				</tr>
				<tr>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
					<th>4K机顶盒数量</th>
					<th>部署软探针数量</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="data!=null && data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="hw_4k_num" /></td>
							<td><s:property value="hw_probe_num" /></td>
							<td><s:property value="cw_4k_num" /></td>
							<td><s:property value="cw_probe_num" /></td>
							<td><s:property value="zx_4k_num" /></td>
							<td><s:property value="zx_probe_num" /></td>
							<td><s:property value="fh_4k_num" /></td>
							<td><s:property value="fh_probe_num" /></td>
							<td><s:property value="ch_4k_num" /></td>
							<td><s:property value="ch_probe_num" /></td>
							<td><s:property value="bst_4k_num" /></td>
							<td><s:property value="bst_probe_num" /></td>
							<td><s:property value="js_4k_num" /></td>
							<td><s:property value="js_probe_num" /></td>
							<td><s:property value="other_4k_num" /></td>
							<td><s:property value="other_probe_num" /></td>
							<td><s:property value="sum_4k_num" /></td>
							<td><s:property value="sum_probe_num" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=19>没有统计到数据！</td>
					</tr>
				</s:else>
			</tbody>
			<s:if test="data!=null && data.size()>0">
			<tfoot>
				<tr>
					<td colspan="19">
						<img src="<s:url value="/images/excel.gif"/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()">
					</td>
				</tr>
			</tfoot>
			</s:if>
			<tr STYLE="display: none">
				<td colspan="19">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>
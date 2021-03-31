<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>零配置BSS工单统计</title>
<lk:res />
<script type="text/javascript">

   $(function(){
	    parent.showIframe();
	   	var h = $("body").attr("scrollHeight");
	   	//alert("h :" + h);
	   	parent.setDataSize(h + 50);
   });

</script>
</head>
<body>

<table width="100%" class="listtable" align="center" style="margin-top:10px;">
	<thead>
		<tr>
			<th class="title_1">属地</th>
			<th class="title_1">业务帐号</th>
			<th class="title_1">操作类型</th>
			<th class="title_1">接入方式</th>
			<th class="title_1">产品ID</th>
			<th class="title_1">BSS工单时间</th>
		</tr>
	</thead>
	<tbody>
	    <s:if test="dtos.size()>0">

		<s:iterator var="bss" value="dtos">
			<tr>
				<td ><s:property value="#bss.cityName" /></td>
				<td ><s:property value="#bss.servAccount" /></td>
				<td ><s:property value="#bss.operation" /></td>
				<td ><s:property value="#bss.addressingType" /></td>
				<td ><s:property value="#bss.prodId" /></td>
				<td ><s:property value="#bss.bssDate" /></td>
			</tr>
		</s:iterator>
		</s:if>
		<s:else>
		   <tr>
		      <td colspan="7" >
		         <div style="text-align: center">
		            查询无数据
		         </div>
		      </td>
		   </tr>
		</s:else>

	</tbody>
	<tfoot>
	<s:if test="dtos.size()>0">
		<tr >

			<td   class="foot"  colspan="7" >
			<div style="float:right">
			<lk:pages url="/gtms/stb/resource/stbBssQuery!query.action" isGoTo="true" />
			</div>
			</td>

		</tr>
	</s:if>
	</tfoot>


</table>

</body>
</html>

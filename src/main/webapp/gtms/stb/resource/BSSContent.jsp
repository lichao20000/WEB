<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>������BSS����ͳ��</title>
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
			<th class="title_1">����</th>
			<th class="title_1">ҵ���ʺ�</th>
			<th class="title_1">��������</th>
			<th class="title_1">���뷽ʽ</th>
			<th class="title_1">��ƷID</th>
			<th class="title_1">BSS����ʱ��</th>
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
		            ��ѯ������
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

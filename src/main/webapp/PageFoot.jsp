<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<table>
	<tr>
		<td align="right">��<s:property value="totalCount"/>����¼&nbsp;</td>
		<td>��<s:property value="totalPage"/>ҳ&nbsp;</td>
		<td>ÿҳ<s:property value="numperpage"/>��&nbsp;</td>
		<s:if test= "currentPage == 1">
			<td>[��ҳ]&nbsp;</td>
			<td>[��ҳ]&nbsp;</td>
		</s:if>
		<s:else>
			<td><a href="javascript:goPage(1, 0)">[��ҳ]</a>&nbsp;</td>
			<td><a href="javascript:goPage(<s:property value="currentPage"/>, -1)">[��ҳ]</a>&nbsp;</td>
		</s:else>
		<td>[��<s:property value="currentPage"/>ҳ]&nbsp;</td>
		<s:if test="currentPage == totalPage">
			<td>[��ҳ]&nbsp;</td>
			<td>[βҳ]&nbsp;</td>
		</s:if>
		<s:else>
			<td><a href="javascript:goPage(<s:property value="currentPage"/>, 1)">[��ҳ]</a>&nbsp;</td>
			<td><a href="javascript:goPage(<s:property value="totalPage"/>, 0)">[βҳ]</a>&nbsp;</td>
		</s:else>
	</tr>
	<input type="hidden" name="currentPage" value="">
</table>

<script language="javascript">

function goPage(pagenum, x){
	var currentPage = pagenum + x;
	if(currentPage <= 0){
		currentPage = 1;
	}
	var last = <s:property value="totalPage"/>;
	if(currentPage > last){
		currentPage = last;
	}
	document.all.currentPage.value = currentPage;
	doQuerySubmit();
}
//��ѯ������Ҫ����
function resetPage(){
	document.all.currentPage.value = 1;
}

</script>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<table>
	<tr>
		<td align="right">共<s:property value="totalCount"/>条纪录&nbsp;</td>
		<td>分<s:property value="totalPage"/>页&nbsp;</td>
		<td>每页<s:property value="numperpage"/>条&nbsp;</td>
		<s:if test= "currentPage == 1">
			<td>[首页]&nbsp;</td>
			<td>[上页]&nbsp;</td>
		</s:if>
		<s:else>
			<td><a href="javascript:goPage(1, 0)">[首页]</a>&nbsp;</td>
			<td><a href="javascript:goPage(<s:property value="currentPage"/>, -1)">[上页]</a>&nbsp;</td>
		</s:else>
		<td>[第<s:property value="currentPage"/>页]&nbsp;</td>
		<s:if test="currentPage == totalPage">
			<td>[下页]&nbsp;</td>
			<td>[尾页]&nbsp;</td>
		</s:if>
		<s:else>
			<td><a href="javascript:goPage(<s:property value="currentPage"/>, 1)">[下页]</a>&nbsp;</td>
			<td><a href="javascript:goPage(<s:property value="totalPage"/>, 0)">[尾页]</a>&nbsp;</td>
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
//查询方法需要调用
function resetPage(){
	document.all.currentPage.value = 1;
}

</script>
<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.system.*"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<style>
.column{text-align:center !important}
</style>

<input type="hidden" value="<s:property value='bindDevTable' />" id="tableVal" />
<table width="100%" class="listtable" id=userTable height="auto">
    <caption> 绑定终端版本统计 </caption>
	<thead>
       <tr>
          <th width="10%"> 厂家</th>
          <th width="20%"> 型号</th>
          <th width="20%"> 硬件版本</th>
          <th width="20%"> 软件版本</th>
          <th width="10%"> 版本类型</th>
          <th width="10%"> 总量</th>
          <th width="10%"> 占比</th>
       </tr>
	</thead>
	<tbody id="userBody">
	</tbody>
	<tfoot>
	<tr>
	  <td colspan="8" align="right">
		<a href="javascript:toExport();">
			<IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='导出当前页' style='cursor: hand'>
		</a>
		</td>
	</tr>
	</tfoot>
</table>
		
<script LANGUAGE="JavaScript">
$(function() {
	//parent.dyniframesize();
	init();
	parent.hide();
});

function init(){
	var data = $("#tableVal").val();
	if(data == null || data == undefined || data ==""){
		data = "没有查询到相关数据!";
	}
	$("#userBody").html(data);
}

function toExport(){
	var url = "bindDevTypeStatExport.jsp";
 	window.location.href=url;
}

 
</script>
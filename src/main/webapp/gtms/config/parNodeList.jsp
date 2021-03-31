<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>e8-c规范版本查询列表</title>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
	<style type="text/css">
		.itta_button{width:70px;height:25px;line-height:25px;display: inline-block;color:#04143a;text-decoration: underline; cursor:pointer;margin:0 5px;}
		.itta_button:hover{color:#fff;background-color:#0170bd;}
	</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	function dodownload(task_id,type){
		var form = document.getElementById("frm");
		
			  form.action ="<s:url value='/gtms/config/paramNodeBatchConfigAction!download.action'/>?task_id="+task_id+"&fileType="+type;
				form.submit();
		
		}
		
	
	
	$(function() {
		parent.dyniframesize();
	});

	$(function(){
		$("input[@name='button']",parent.document).attr("disabled", false);
		$("input[@name='button']",parent.document).css("backgroundColor","#0066ff");
		$("#QueryData",parent.document).html("");
	});
</script>
</head>
<body>
	<div class="content">
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
				<tr>
				   
				   <th>定制人</th>
				   <th>文件名称</th>
				   <th>定制时间</th>
					 <th>操作</th>
				</tr>
				
				<s:if test="rlist!=null">
					<s:if test="rlist.size()>0">
						<s:iterator value="rlist">
							<tr>
								<td><s:property value="loginname"/></td>
								<td><s:property value="filename"/></td>
								<td><s:property value="customtime"/></td>
								<td><a href="javascript:dodownload('<s:property value="task_id"/>','txt');" class="itta_button">txt下载</a>
									<a href="javascript:dodownload('<s:property value="task_id"/>','xls');" class="itta_button">xls下载</a>
									</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
					<tr>
						<td colspan="9" align="left">
							<font color="red">没有相关记录信息!</font>
						</td>
					</tr>
					</s:else>
					</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="left">
							<font color="red">没有相关记录信息!</font>
						</td>
					</tr>
				</s:else>
				<tr>
					<td colspan="9" align="right">
						<lk:pages    
							url="/gtms/config/paramNodeBatchConfigAction!getNodeBatchList.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
				</tr>
			</table>
		</div>
	</div>
	<form method="post" action="" id="frm" name="frm" target="dataForm">
			<input name="nameDowmload" id="nameDowmload" type="hidden" value=""/>
	</form>
</body>
<!-- <div id="wall" class="wall" style="width:2500;height:590; margin: -1000;display: none" ></div> -->
</html>
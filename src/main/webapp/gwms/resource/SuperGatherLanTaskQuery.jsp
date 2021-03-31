<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script language="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">
$(function(){
	doQuery();
});

function deleteTask(taskId){
	var url="<s:url value='/gwms/resource/superGatherLanTask!taskDel.action'/>";
	$.post(url,{
		taskId:taskId
	},function(ajax){				
		// 定制失败
		if("0" == ajax){
			alert("任务删除失败");
			return;
		}
		alert("任务删除成功！");
		doQuery();
	});
}

function detailTask(taskId){
	var url="<s:url value='/gwms/resource/superGatherLanTask!taskDeatil.action'/>?taskId=" + taskId;
	window.open(url,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function doQuery(){
	frm.submit();
}
</script>
<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						批量采集LAN任务列表
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						时间条件是任务创建时间
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" method="POST" action="<s:url value="/gwms/resource/superGatherLanTask!taskList.action"/>" target="dataForm" >
				<table class="querytable">
					<tr>
					<th colspan=4>
						批量采集LAN任务列表
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							开始时间
						</td>
						<td width="35%">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">							
						</td>
						<td class=column width="15%">
							结束时间
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">						
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">&nbsp;查 询&nbsp;</button>&nbsp;&nbsp;&nbsp;
							<button onclick="reset()">&nbsp;重 置&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="600px !important" frameborder="0"  scrolling="no" width="100%" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
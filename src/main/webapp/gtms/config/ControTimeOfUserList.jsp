<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">
var	width=600;    
var height=400;
//初始化时间
function doDelete(cuId){
	var sure = window.confirm("确定删除吗？");
	if(!sure)
	{
		return;
	}
	var url = "<s:url value='/gtms/config/timeSet!delete.action'/>";  
	$.post(url,{
		cuId : cuId
	},function(ajax){
	    if(ajax=="1"){
	    	alert("已成功删除");
	    }else{
	    	alert("删除失败");
	    }
	    window.location.href = window.location.href ;
	});
}
function openUpdate(typeId,conTime,cuId){
   var url = "../config/timeSet!preUpdate.action?cuId="+cuId+"&conTime="+conTime+"&typeId="+typeId;
   var sFeatures = 'dialogWidth='+width+'px;';
	   sFeatures += 'dialogHeight='+ height+'px';
	   sFeatures += 'resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	window.showModalDialog(url,'',sFeatures); 	
}
</script>
<table class="listtable">
				<caption>查询结果</caption>
				<thead>
					<tr bgcolor="#FFFFFF" >
						<th class=title_1 align=center width="25%">Id</th>
						<th class=title_1 align=center width="25%">时间类型</th>
						<th class=title_1 align=center width="25%">时间</th>
						<th class=title_1 align=center width="25%">操作</th>
					</tr>
				</thead>
				<s:if test="timeList.size()>0">
					<s:iterator value="timeList">
						<tr>
							<td class=title_4 align=center width="25%"><s:property value="cuid" /></td>
							<td class=title_4 align=center width="25%">
									<s:if test="1==type_id">用户受理时间</s:if>
							</td>
							<td class=title_4 align=center width="25%"><s:property value="con_time" /></td>
							<td class=title_4 align=center width="25%">
								<a href="javascript:openUpdate('<s:property value="type_id" />',
																 '<s:property value="con_time"/>',
																 '<s:property value="cuid" />')">修改</a>&nbsp;&nbsp;|&nbsp;&nbsp;
								<a href="javascript:doDelete('<s:property value="cuid" />')">删除</a>
							</td>
						</tr>
					</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>系统没有相关的信息!</td>
				</tr>
			</s:else>
</table>
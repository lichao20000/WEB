<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script language="JavaScript">
var	width=500;    
var height=200;
function openAdd(){
	
   var url = "<s:url value='/gtms/diagnostic/TemplateUnitAdd.jsp'/>";
   var sFeatures = 'dialogWidth='+width+'px;';
	   sFeatures += 'dialogHeight='+ height+'px';
	   sFeatures += 'resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	window.showModalDialog(url ,'',sFeatures); 
}

function openDelete(unitId){
	var sure = window.confirm("确定删除吗？");
	if(!sure)
	{
		return;
	}
	var url = "<s:url value='/gtms/diagnostic/templateUnitManage!delete.action'/>"; 
	$.post(url,{
		unitId : unitId 
	},function(ajax){
		if(ajax =="1" ){
			alert("删除成功");
		}else{
			alert("删除失败");	
		}
		window.location.href=window.location.href;
	});
}
function openEdit(unitId){
   var url = "../diagnostic/templateUnitManage!preUpdate.action?unitId="+unitId;
   var sFeatures = 'dialogWidth='+width+'px;';
	   sFeatures += 'dialogHeight='+ height+'px';
	   sFeatures += 'resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	window.showModalDialog(url,'',sFeatures); 	
}
</script>
</head>
<body>
<TABLE >
	<tr>
		<td>
			<table class="green_gargtd" >
				<tr>
					<th>
						模版单元管理
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
				<table class="querytable">
					<tr>
						<th colspan=4>
							模版单元管理
						</th>
						
					</tr>
					<TR bgcolor="#FFFFFF"  STYLE="">
						<td class=column width="100%" colspan=4>
							<button  id="addButton"  onclick="openAdd()">&nbsp;新&nbsp;增&nbsp;</button>
						</td>
						
					</TR>
					<tr bgcolor=#ffffff>
							<td class=title_1 align="center" width="25%">
								模版单元名称
							</td>
							<td class=title_1 align="center" width="35%">
								模版单元地址
							</td>
							<td class=title_1 align="center" width="25%">
								模版单元创建时间
							</td >
							<td class=title_1 align="center" width="15%">
								操作
							</td>
						</tr>
						<s:if test="null != unitList">
						<s:iterator value="unitList">
							<tr bgcolor=#ffffff>
								<td class=title_4 align=center width="25%">
									<s:property value="unit_name"/>
								</td>
								<td class=title_4 align=center width="35%">
									<s:property value="unit_url"/>
								</td>
								<td class=title_4 align=center width="25%"> 
									<s:property value="unit_time"/>
								</td>
								<td class=title_4 align=center width="15%">
									<a href="javascript:openEdit('<s:property value="id"/>')" >编辑</a> &nbsp; | &nbsp;
									<a href="javascript:openDelete('<s:property value="id"/>')">删除</a>
								</td>
							</tr>
						</s:iterator>
						</s:if>
				</table>
		</td>
	</tr>

</TABLE>
</body>
</html>
<%@ include file="/foot.jsp"%>

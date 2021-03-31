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
	var sure = window.confirm("ȷ��ɾ����");
	if(!sure)
	{
		return;
	}
	var url = "<s:url value='/gtms/diagnostic/templateUnitManage!delete.action'/>"; 
	$.post(url,{
		unitId : unitId 
	},function(ajax){
		if(ajax =="1" ){
			alert("ɾ���ɹ�");
		}else{
			alert("ɾ��ʧ��");	
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
						ģ�浥Ԫ����
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
							ģ�浥Ԫ����
						</th>
						
					</tr>
					<TR bgcolor="#FFFFFF"  STYLE="">
						<td class=column width="100%" colspan=4>
							<button  id="addButton"  onclick="openAdd()">&nbsp;��&nbsp;��&nbsp;</button>
						</td>
						
					</TR>
					<tr bgcolor=#ffffff>
							<td class=title_1 align="center" width="25%">
								ģ�浥Ԫ����
							</td>
							<td class=title_1 align="center" width="35%">
								ģ�浥Ԫ��ַ
							</td>
							<td class=title_1 align="center" width="25%">
								ģ�浥Ԫ����ʱ��
							</td >
							<td class=title_1 align="center" width="15%">
								����
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
									<a href="javascript:openEdit('<s:property value="id"/>')" >�༭</a> &nbsp; | &nbsp;
									<a href="javascript:openDelete('<s:property value="id"/>')">ɾ��</a>
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

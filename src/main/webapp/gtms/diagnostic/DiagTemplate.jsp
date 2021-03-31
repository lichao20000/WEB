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
var height=300;
function openAdd(){
	
   var url = "<s:url value='/gtms/diagnostic/diagTemplate!preAdd.action'/>";
   var sFeatures = 'dialogWidth='+width+'px;';
	   sFeatures += 'dialogHeight='+ height+'px';
	   sFeatures += 'resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	window.showModalDialog(url ,'',sFeatures); 
}

function openDelete(diagId){
	var sure = window.confirm("ȷ��ɾ����");
	if(!sure)
	{
		return;
	}
	var url = "<s:url value='/gtms/diagnostic/diagTemplate!delete.action'/>"; 
	$.post(url,{
		diagId : diagId 
	},function(ajax){
		if(ajax == "1" ){
			alert("ɾ���ɹ�");
		}else{
			alert("ɾ���ɹ�");	
		}
		window.location.href=window.location.href; 
	});
}
function openEdit(diagId){
   var url = "../diagnostic/diagTemplate!preUpdate.action?diagId="+diagId;
   var sFeatures = 'dialogWidth='+width+'px;';
	   sFeatures += 'dialogHeight='+ height+'px';
	   sFeatures += 'resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	window.showModalDialog(url,'',sFeatures); 	
}
</script>
</head>
<body>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd" >
				<tr>
					<th>
						ģ�����
					</th>
					<td>z
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							ģ�����
						</th>
						
					</tr>
					<TR bgcolor="#FFFFFF"   STYLE="">
						<td class="column" colspan="4" align="right" width="100%">
							<button  id="addButton"  onclick="openAdd()">&nbsp;��&nbsp;��&nbsp;</button>
						</td>
						
					</TR>
					<TR bgcolor="#FFFFFF"   STYLE="">
						<td class="column" colspan="4" align="right" width="100%" height="15">
							 
						</td>
						
					</TR>
						<tr bgcolor=#ffffff>
							<td class=title_1 align=center width="25%">
								ģ������
							</td>
							<td class=title_1 align=center width="25%">
								������
							</td>
							<td class=title_1 align=center width="25%">
								����ʱ��
							</td >
							<td class=title_1 align=center >
								 ����
							</td>
						</tr>
						<s:if test="null != list">
						<s:iterator value="list">
							<tr bgcolor=#ffffff>
								<td class=title_4 align=center width="25%">
									<s:property value="template_name"/>
								</td>
								<td class=title_4 align=center width="25%">
									<s:property value="acc_loginname"/>
								</td>
								<td class=title_4 align=center width="25%"> 
									<s:property value="template_time"/>
								</td>
								<td class=title_4 align=center width="25%">
									<a href="javascript:openEdit('<s:property value="id"/>')" >�༭</a> &nbsp; | &nbsp;
									<a href="javascript:openDelete('<s:property value="id"/>')">ɾ��</a>
								</td>
							</tr>
						</s:iterator>
						</s:if>
				</table>
			</form>
		</td>
	</tr>

</TABLE>
</body>
</html>
<%@ include file="/foot.jsp"%>

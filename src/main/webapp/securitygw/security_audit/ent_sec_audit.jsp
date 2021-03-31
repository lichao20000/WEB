<!-- 
@author 史亮(5224) tel:13814027591
@version 1.0
@since 2008-03-31
@category 企业安全审计
 -->
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>企业安全审计</title>
<SCRIPT LANGUAGE="JavaScript" src="/Js/rightMenu.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script language="JavaScript" src= "<s:url value="/securitygw/js/coolmenu1_0_2.js"/>"></script>
<script language="JavaScript" src="<s:url value="/securitygw/js/coolmenu_res.js"/>"></script>

<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_ico.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/menu_list.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
@import url(../css/css_ico.css);
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
a{margin-bottom:-1px;}
-->
</style>

<script language="JavaScript">
<!--
window.attachEvent("onload",function(){
	//drawMenuBar("idMenuBar");
});
//-->
</script>
<script language="JavaScript" type="text/JavaScript">
<!--

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>

<style type="text/css">
<!--
.style2 {	font-size: 10pt;
	font-weight: bold;
}
-->
</style>

<style type="text/css">
<!--
.style4 {line-height: 17px; color: #FFFFFF; text-indent: 2pt; font-family: Arial, "Arial Black", "宋体", "黑体"; font-size: 9pt;}
-->
</style>
</head>

<body onLoad="MM_preloadImages('../images/left_jton.jpg')">
<s:hidden name="deviceId"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="10"></td>
		<td>
		<table width="99%" border="1" cellpadding="0" cellspacing="0" align="center"
			class="table">
			<tr class="tab_title">
				<td colspan="3" class="title_white"><s:property value="customerName"/> --- <img
					src="<s:url value="/securitygw/images/attention_2.gif"/>" width="15" height="12">
				注：点击图表显示详情</td>
			</tr>
			<tr>
				<td colspan="3" class="trOver_blue">
				<div align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="text">
						<div align="center">上网行为分析</div>
						</td>
						<td width="6%"><a href="#"><img
							src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10"
							border="0">TOP</a></td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
			<tr>
				<td class="text" align="center" width="343"  height="146">
				  
				<a href="<s:url value="/securitygw/NetFluxAnalyse!toNetFluxAnalyse.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param><s:param name="remark"><s:property value="customerName"/></s:param></s:url>"> <img
					src="<s:url value="/securitygw/NetFluxAnalyse!getSmallFluxToday.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param></s:url>"  
					border="0"></a>
				</td>
				<td class="text" align="center" width="343" >
				  
				<a href="<s:url value="/securitygw/PBWebTopN.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param><s:param name="remark"><s:property value="customerName"/></s:param></s:url>">  <img
					src="<s:url value="/securitygw/PBWebTopNAction!getSmallWebTopNDay.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param></s:url>"  
					border="0"></a>
				</td>
				<td class="text" align="center" width="343" >
				  
				<a href="<s:url value="/securitygw/EntWebTopn!toEntWebTopn.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param><s:param name="remark"><s:property value="customerName"/></s:param></s:url>"> <img src="<s:url value="/securitygw/EntWebTopn!getSmallWebTopnToday.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param></s:url>"
					  border="0"></a>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="trOver_blue">
				<div align="center"><span class="style2"></span>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="text">
						<div align="center">企业安全审计</div>
						</td>
						<td width="6%"><a href="#"><img
							src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10"
							border="0">TOP</a></td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
			<tr>
				<td class="text" width="343" align="center" height="146">
				  
				<a href="<s:url value="/securitygw/VirusReport.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param><s:param name="remark"><s:property value="customerName"/></s:param></s:url>"> <img
					src="<s:url value="/securitygw/VirusReport!getPreView.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param> </s:url>" 
					 
					border="0" ></a>
				</td>
				<td class="text" width="343" align="center" >
				  
				<a href="<s:url value="/securitygw/AttackReport.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param><s:param name="remark"><s:property value="customerName"/></s:param></s:url>"> <img
					src="<s:url value="/securitygw/AttackReport!getPreView.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param></s:url>"  
					border="0"></a>
				</td>
				<td class="text" width="343" align="center">
				  
				<a href="<s:url value="/securitygw/FilterReport.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param><s:param name="remark"><s:property value="customerName"/></s:param></s:url>"> <img
					src="<s:url value="/securitygw/FilterReport!getPreView.action"><s:param name="deviceid"><s:property value="deviceId"/></s:param></s:url>"  
					border="0"></a>
				</td>
			</tr>

			<tr>
				<td colspan="3" class="trOver_blue">
				<div align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="text">
						<div align="center">维护管理</div>
						</td>
						<td width="6%"><a href="#"><img
							src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10"
							border="0">TOP</a></td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
			<tr>
				<td class="text" align="center" width="343"  height="146">
				  
				<a href="<s:url value="/securitygw/SgwPerformance.action"><s:param name="device_id"><s:property value="deviceId"/></s:param><s:param name="desc"><s:property value="customerName"/></s:param><s:param name="class1">1</s:param></s:url>"> <img src="<s:url value="/securitygw/SgwPerformanceHomePage!getPerformanceChart.action"><s:param name="device_id"><s:property value="deviceId"/></s:param><s:param name="srcType"><s:property value="small"/></s:param>
				<s:param name="class1">1</s:param><s:param name="reportType">1</s:param></s:url>"
				  border="0"></a>
				</td>
				<td class="text" align="center" width="343" >
				  
				<a href="<s:url value="/securitygw/SgwPerformance.action"><s:param name="device_id"><s:property value="deviceId"/></s:param><s:param name="desc"><s:property value="customerName"/></s:param><s:param name="class1">2</s:param></s:url>"> <img src="<s:url value="/securitygw/SgwPerformanceHomePage!getPerformanceChart.action"><s:param name="device_id"><s:property value="deviceId"/></s:param><s:param name="srcType"><s:property value="small"/></s:param>
				<s:param name="class1">2</s:param><s:param name="reportType">1</s:param></s:url>"
					 border="0"></a>
				</td>
				<td class="text" align="center" width="343" >
				  
				<a href="<s:url value="/securitygw/SgwPerformance.action"><s:param name="device_id"><s:property value="deviceId"/></s:param><s:param name="desc"><s:property value="customerName"/></s:param><s:param name="class1">8</s:param></s:url>"> <img src="<s:url value="/securitygw/SgwPerformanceHomePage!getPerformanceChart.action"><s:param name="device_id"><s:property value="deviceId"/></s:param><s:param name="srcType"><s:property value="small"/></s:param>
				<s:param name="class1">8</s:param><s:param name="reportType">1</s:param></s:url>"
					 border="0"></a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td></td>
		<td height="8"></td>
	</tr>
</table>
</body>
</html>

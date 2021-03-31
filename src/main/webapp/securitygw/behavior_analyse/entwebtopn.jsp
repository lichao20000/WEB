<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="refresh" content="600000">
<head>
<title>企业上网统计</title>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_ico.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
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
a {margin-bottom:-1px;}
.style2 { font-size: 10pt; font-weight: bold;}
.style4 {line-height: 17px; color: #FFFFFF; text-indent: 2pt; font-family: Arial, "Arial Black", "宋体", "黑体"; font-size: 9pt;}
-->
</style>
<SCRIPT LANGUAGE="JavaScript" src="/Js/rightMenu.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script language="JavaScript" src="<s:url value="/Js/MyCalendar.js"/>"></script>
<script language="JavaScript" src="<s:url value="/securitygw/js/coolmenu1_0_2.js"/>"></script>
<script language="JavaScript" src="<s:url value="/securitygw/js/coolmenu_res.js"/>"></script>
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

function checkQuery() {
  var frm = document.frmQuery;
  var str = frm.datetime.value;
  if(str=="")return false;
  var ymd=str.split("-");
  var seldate = new Date(ymd[0],ymd[1]-1,ymd[2],23,59,59);
  var st = seldate.getTime()/1000; 
  frm.time.value = st;
  //alert(st);
  return true;
}

//-->
</script>
</head>
<body onclick="parent.document_click()">
<jsp:include page="entlinks.jsp" flush="false" />
<table width="99%" border="0" cellpadding="0" cellspacing="0" align="center">
 <tr>
  <td>
  <form name="frmQuery" action="EntWebTopn!toEntWebTopn.action" onSubmit="checkQuery()"><input
   type=hidden name="deviceid" value="<s:property value="deviceid"/>"> <input type=hidden
   name="time" value="0"> <input type=hidden name="remark" value="<s:property value="remark"/>">
  <table width="99%" border="1" cellpadding="0" cellspacing="0" class="table">
   <tr class="tab_title">
    <td colspan="2" class="title_white">企业WEB浏览Top10 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><input
     name="datetime" type="text" class="form_kuang" value="<s:property value="datetime"/>" size="12"><input
     type="button" class="jianbian" onClick="showCalendar('day',event)" value="▼"></span><input
     type="submit" class="jianbian" value="查 询">
     <!-- <input type="button" class="jianbian" value="高级查询"
     onclick="$.open('EntWebTopn!popup.action?<s:property value="parstr"/>','740px','400px','200px','200px','false')">
      -->
      </td>
   </tr>
   <tr>
    <td colspan="2" class="tr_glory">
    <div align="center">&nbsp;</div>
    </td>
   </tr>
   <tr>
    <td colspan="2" class="trOver_blue">
    <div align="center"><span class="style2"></span>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
      <td>
      <div align="center">日报表</div>
      </td>
      <td width="6%"><a href="#"><img src="<s:url value="/securitygw/images/button_back.gif"/>"
       width="15" height="10" border="0">返回</a></td>
     </tr>
    </table>
    </div>
    </td>
   </tr>
   <tr>
    <td colspan="2" class="tr_white">
    <div align="center"><a href="#"><img border="0" src="<s:url value="%{dayUrl}"/>" /> </a><br>
    <br>
    <div id="txtTop10Today" style="display:none"><s:property value="top10Today" /></div>
    <div id="htmTop10Today"></div>
    <br>
    </div>
    </td>
   </tr>
   <tr>
    <td colspan="2" class="trOver_blue">
    <div align="center">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
      <td>
      <div align="center">周报表</div>
      </td>
      <td width="6%"><a href="#"><img src="<s:url value="/securitygw/images/button_back.gif"/>"
       width="15" height="10" border="0">返回</a></td>
     </tr>
    </table>
    </div>
    </td>
   </tr>
   <tr>
    <td colspan="2" class="tr_white">
    <div align="center"><a href="#"><img border="0" src="<s:url value="%{weekUrl}"/>"> </a> <br>
    <br>
    <div id="txtWeekDaysTop10" style="display:none"><s:property value="top10ByWeek" /></div>
    <div id="htmWeekDaysTop10"></div>
    <br>
    </div>
    </td>
   </tr>
   <tr>
    <td colspan="2" class="trOver_blue">
    <div align="center"><span class="style2"></span>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
      <td>
      <div align="center">月报表</div>
      </td>
      <td width="6%"><a href="#"><img src="<s:url value="/securitygw/images/button_back.gif"/>"
       width="15" height="10" border="0">返回</a></td>
     </tr>
    </table>
    </div>
    </td>
   </tr>
   <tr>
    <td colspan="2" class="tr_white">
    <div align="center"><a href="#"><img border="0" src="<s:url value="%{monthUrl}"/>"> </a><br>
    <br>
    <div id="txtMonthDaysTop10" style="display:none"><s:property value="top10ByMonth" /></div>
    <div id="htmMonthDaysTop10"></div>
    <br>
    </div>
    </td>
   </tr>
  </table>
  <p>&nbsp;</p>
  </form>
  </td>
 </tr>
 <tr>
  <td></td>
  <td height="8"></td>
 </tr>
</table>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoPage(tem) {
	this.location=tem
}

function loadTopnTables() {
  document.getElementById("htmTop10Today").innerHTML = document.getElementById("txtTop10Today").innerText;
  document.getElementById("htmWeekDaysTop10").innerHTML = document.getElementById("txtWeekDaysTop10").innerText;
  document.getElementById("htmMonthDaysTop10").innerHTML = document.getElementById("txtMonthDaysTop10").innerText;  
}

loadTopnTables();

//-->
</SCRIPT>
</body>

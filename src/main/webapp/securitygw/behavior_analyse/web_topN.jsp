<!--
@author Zhaof,suixz
@version 1.0
@since 2008-03-31
@category WEB浏览TopN
 -->
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="refresh" content="600000">
<html>
	<head>
		<title>员工WEB浏览Top</title>
<SCRIPT LANGUAGE="JavaScript" src="/Js/rightMenu.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script language="JavaScript" src= "<s:url value="/securitygw/js/coolmenu1_0_2.js"/>"></script>
<script language="JavaScript" src="<s:url value="/securitygw/js/coolmenu_res.js"/>"></script>

<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<link href="../css/menu_list.css" rel="stylesheet" type="text/css">
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

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/Calendar.js"/>"></SCRIPT>
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

//add by suixz(5253) 2008-5-6
//高级查询事件
function highQuery(){
	$.open("<s:url value='/securitygw/webTopNHighQuery.action'/>?deviceId="+"<s:property value="deviceid"/>"+"&remark="+"<s:property value="remark"/>","740px","400px","200px","200px","false");
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

<body onclick="parent.document_click()" onLoad="MM_preloadImages('<s:url value="/securitygw/images/left_jton.jpg"/>')">
<table width="100%"  border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td><table width="99%"  border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="left" valign="top"><table width="98%"  border="0" cellpadding="0" cellspacing="0" class="table">
                  <tr class="tab_title">
                    <td colspan="3" class="title_white"><s:property value="device_name"/></td>
                  </tr>
                  <tr class="tr_white">
                    <td class="tr_yellow">设备IP</td>
                    <td colspan="2" class="text"><s:property value="loopback_ip"/></td>
                    </tr>
                  <tr class="trOver_blue">
                    <td class="text">时间段</td>
                    <td class="text">员工IP</td>
                    <td class="text">Top-1浏览web次数</td>
                    </tr>
                  <tr class="tr_white">
                    <td class="tr_yellow">日</td>
					<td class="text"><s:property value="top1.day1ip"/></td>
					<td class="text"><s:property value="top1.day1times"/></td>
                    </tr>
                  <tr class="tr_glory">
                    <td class="tr_yellow">周</td>
					<td class="text"><s:property value="top1.week1ip"/></td>
					<td class="text"><s:property value="top1.week1times"/></td>
                    </tr>
                  <tr class="tr_white">
                    <td class="tr_yellow">月</td>
					<td class="text"><s:property value="top1.month1ip"/></td>
					<td class="text"><s:property value="top1.month1times"/></td>
                    </tr>
                  <tr class="tr_glory">
                    <td class="tr_yellow">备注</td>
                    <td colspan="2" class="text"><s:property value="remark"/></td>
                    </tr>
              </table></td>
        <td align="right" valign="top">
			<%@ include file="navigation.jsp"%>
		</td>
      </tr>
    </table>
        <br>
          <table width="99%"  border="0" cellpadding="0" cellspacing="0" class="table">
            <tr class="tab_title">
              <td><span class="title_white">员工WEB浏览Top<s:property value="topN"/>&nbsp;&nbsp;
                    <input id="startDate" name="textfield" readonly="true" type="text" class="bk" value="<s:property value="start"/>" size="12">&nbsp;
                    <input name="button2322" type="button" class="jianbian" onClick="showCalendar('day',event)" value="▼">
                  <input name="query" type="button" onclick="query()" class="jianbian" value="查 询">
                  <!-- <input name="query" type="button" onclick="highQuery()" class="jianbian" value="高级查询"> -->
				  </td>
            </tr>
            <tr>
              <td class="trOver_blue">
                <div align="center"><span class="style2"></span>
                  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td><div align="center">日报表</div></td>
                      <td width="6%"><a href="#"><img src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10" border="0">返回</a></td>
                    </tr>
                  </table>
                </div></td>
            </tr>
		<tr>
			<td class="tr_white">
				<div align="center"><br>
					<a href="securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&date=<s:property value="start"/>&step=1&prot_type=0"><img border="0" src="<s:url value="%{imgUrlDay}"/>" /><br>
					</a>
				</div><br>
				<div align="center">
					<table width="670" border="0" cellpadding="0" cellspacing="0" class="table">
						<s:set id="lenD" var="lenD" value="dataDay.size()"></s:set>
						<tr>
							<td class="tr_yellow">排名</td>
							<td class="tr_yellow">用户</td>
							<td class="tr_yellow">次数</td>
							<td class="tr_yellow">占总量百分比</td>
						</tr>
						<s:iterator var="mapD" value="dataDay" status="st">
						<tr>
						<td class="tr_yellow">Top-<s:property value="#st.count"/></td>
						<td class="tr_white"><s:property value="#mapD.srcip"/></td>
						<td class="tr_white"><s:property value="#mapD.times"/></td>
						<td class="tr_white"><s:property value="percentageOfDay[#st.index]"/></td>
						</tr>
						</s:iterator>
					</table>
				</div><br>
			</td>
		</tr>
            <tr>
              <td class="trOver_blue">
                <div align="center">
                  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td><div align="center">周报表</div></td>
                      <td width="6%"><a href="#"><img src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10" border="0">返回</a></td>
                    </tr>
                  </table>
                </div></td>
            </tr>
		<tr>
			<td class="tr_white">
				<div align="center"><br>
					<a href="securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&date=<s:property value="start"/>&step=7&prot_type=0"><img border="0" src="<s:url value="%{imgUrlWeek}"/>" /><br>
					</a>
				</div><br>
				<div align="center">
					<table width="670" border="0" cellpadding="0" cellspacing="0" class="table">
						<s:set id="lenW" var="lenW" value="dataWeek.size()"></s:set>
						<tr>
							<td class="tr_yellow">排名</td>
							<td class="tr_yellow">用户</td>
							<td class="tr_yellow">次数</td>
							<td class="tr_yellow">占总量百分比</td>
						</tr>
						<s:iterator var="mapW" value="dataWeek" status="st">
						<tr>
						<td class="tr_yellow">Top-<s:property value="#st.count"/></td>
						<td class="tr_white"><s:property value="#mapW.srcip"/></td>
						<td class="tr_white"><s:property value="#mapW.times"/></td>
						<td class="tr_white"><s:property value="percentageOfWeek[#st.index]"/></td>
						</tr>
						</s:iterator>
					</table>
				</div><br>
			</td>
		</tr>
            <tr>
              <td class="trOver_blue">
                <div align="center"><span class="style2"></span>
                  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td><div align="center">月报表</div></td>
                      <td width="6%"><a href="#"><img src="<s:url value="/securitygw/images/button_back.gif"/>" width="15" height="10" border="0">返回</a></td>
                    </tr>
                  </table>
                </div></td>
            </tr>
		<tr>
			<td class="tr_white">
				<div align="center"><br>
					<a href="securitygw/PBDetailAction.action?deviceid=<s:property value="deviceid"/>&date=<s:property value="start"/>&step=30&prot_type=0"><img border="0" src="<s:url value="%{imgUrlMonth}"/>" /><br>
					</a>
				</div><br>
				<div align="center">
					<table width="670" border="0" cellpadding="0" cellspacing="0" class="table">
						<s:set id="lenM" var="lenM" value="dataMonth.size()"></s:set>
						<tr>
							<td class="tr_yellow">排名</td>
							<td class="tr_yellow">用户</td>
							<td class="tr_yellow">次数</td>
							<td class="tr_yellow">占总量百分比</td>
						</tr>
						<s:iterator var="mapM" value="dataMonth" status="st">
						<tr>
						<td class="tr_yellow">Top-<s:property value="#st.count"/></td>
						<td class="tr_white"><s:property value="#mapM.srcip"/></td>
						<td class="tr_white"><s:property value="#mapM.times"/></td>
						<td class="tr_white"><s:property value="percentageOfMon[#st.index]"/></td>
						</tr>
						</s:iterator>
					</table>
				</div><br>
			</td>
		</tr>
	</table>
      <p>&nbsp;</p></td>
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
//-->
</SCRIPT>

</body>
</html>

<SCRIPT LANGUAGE="JavaScript">
<!--
	function query() {
		var date = document.getElementById("startDate").value;
		var url = "/epsgwms/securitygw/PBTopN.action?deviceid=" + "<s:property value="deviceid"/>" + "&remark=" + "<s:property value="remark"/>" + "&start=" + date;
		window.location = url;
	}
//-->
</SCRIPT>

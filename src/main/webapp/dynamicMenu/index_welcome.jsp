<%--
Author		: liuli
Date		: 2006-10-13
Desc		: 手工录入公告信息
==========================================================
Author		: yanhj(yanhj@lianchuang.com)
Date		: 2008-4-7
Desc		: "服务热线"背景图片
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
List tem_List = CityDAO.getNextCityIdsByCityPid(user.getCityId());
String telecom = LipossGlobals.getLipossProperty("telecom");

tem_List.add(user.getCityId());
String image = "";
if(LipossGlobals.IsETMS()){
	image = "inside_1_bbms.jpg";
} else {
	if(telecom.equals("CUC")){
		image = "inside_1_lt_sd.jpg";
	}else if(telecom.equals("CMC")) {
		image = "inside_1_yd.jpg";
	}else{
		image = "inside_1.jpg";
	}
}
%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.system.dbimpl.*"%>
<%@page import="com.linkage.litms.system.*"%>
<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>无标题文档</title>
<script type="text/javascript" src="js/boot.js"></script>

<script language="JavaScript" type="text/JavaScript">
<!--

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
function MM_dragLayer(objName,x,hL,hT,hW,hH,toFront,dropBack,cU,cD,cL,cR,targL,targT,tol,dropJS,et,dragJS) { //v4.01
  //Copyright 1998 Macromedia, Inc. All rights reserved.
  var i,j,aLayer,retVal,curDrag=null,curLeft,curTop,IE=document.all,NS4=document.layers;
  var NS6=(!IE&&document.getElementById), NS=(NS4||NS6); if (!IE && !NS) return false;
  retVal = true; if(IE && event) event.returnValue = true;
  if (MM_dragLayer.arguments.length > 1) {
    curDrag = MM_findObj(objName); if (!curDrag) return false;
    if (!document.allLayers) { document.allLayers = new Array();
      with (document) if (NS4) { for (i=0; i<layers.length; i++) allLayers[i]=layers[i];
        for (i=0; i<allLayers.length; i++) if (allLayers[i].document && allLayers[i].document.layers)
          with (allLayers[i].document) for (j=0; j<layers.length; j++) allLayers[allLayers.length]=layers[j];
      } else {
        if (NS6) { var spns = getElementsByTagName("span"); var all = getElementsByTagName("div"); 
          for (i=0;i<spns.length;i++) if (spns[i].style&&spns[i].style.position) allLayers[allLayers.length]=spns[i];}
        for (i=0;i<all.length;i++) if (all[i].style&&all[i].style.position) allLayers[allLayers.length]=all[i]; 
    } }
    curDrag.MM_dragOk=true; curDrag.MM_targL=targL; curDrag.MM_targT=targT;
    curDrag.MM_tol=Math.pow(tol,2); curDrag.MM_hLeft=hL; curDrag.MM_hTop=hT;
    curDrag.MM_hWidth=hW; curDrag.MM_hHeight=hH; curDrag.MM_toFront=toFront;
    curDrag.MM_dropBack=dropBack; curDrag.MM_dropJS=dropJS;
    curDrag.MM_everyTime=et; curDrag.MM_dragJS=dragJS;
    curDrag.MM_oldZ = (NS4)?curDrag.zIndex:curDrag.style.zIndex;
    curLeft= (NS4)?curDrag.left:(NS6)?parseInt(curDrag.style.left):curDrag.style.pixelLeft; 
    if (String(curLeft)=="NaN") curLeft=0; curDrag.MM_startL = curLeft;
    curTop = (NS4)?curDrag.top:(NS6)?parseInt(curDrag.style.top):curDrag.style.pixelTop; 
    if (String(curTop)=="NaN") curTop=0; curDrag.MM_startT = curTop;
    curDrag.MM_bL=(cL<0)?null:curLeft-cL; curDrag.MM_bT=(cU<0)?null:curTop-cU;
    curDrag.MM_bR=(cR<0)?null:curLeft+cR; curDrag.MM_bB=(cD<0)?null:curTop+cD;
    curDrag.MM_LEFTRIGHT=0; curDrag.MM_UPDOWN=0; curDrag.MM_SNAPPED=false; //use in your JS!
    document.onmousedown = MM_dragLayer; document.onmouseup = MM_dragLayer;
    if (NS) document.captureEvents(Event.MOUSEDOWN|Event.MOUSEUP);
  } else {
    var theEvent = ((NS)?objName.type:event.type);
    if (theEvent == 'mousedown') {
      var mouseX = (NS)?objName.pageX : event.clientX + document.body.scrollLeft;
      var mouseY = (NS)?objName.pageY : event.clientY + document.body.scrollTop;
      var maxDragZ=null; document.MM_maxZ = 0;
      for (i=0; i<document.allLayers.length; i++) { aLayer = document.allLayers[i];
        var aLayerZ = (NS4)?aLayer.zIndex:parseInt(aLayer.style.zIndex);
        if (aLayerZ > document.MM_maxZ) document.MM_maxZ = aLayerZ;
        var isVisible = (((NS4)?aLayer.visibility:aLayer.style.visibility).indexOf('hid') == -1);
        if (aLayer.MM_dragOk != null && isVisible) with (aLayer) {
          var parentL=0; var parentT=0;
          if (NS6) { parentLayer = aLayer.parentNode;
            while (parentLayer != null && parentLayer.style.position) {             
              parentL += parseInt(parentLayer.offsetLeft); parentT += parseInt(parentLayer.offsetTop);
              parentLayer = parentLayer.parentNode;
          } } else if (IE) { parentLayer = aLayer.parentElement;       
            while (parentLayer != null && parentLayer.style.position) {
              parentL += parentLayer.offsetLeft; parentT += parentLayer.offsetTop;
              parentLayer = parentLayer.parentElement; } }
          var tmpX=mouseX-(((NS4)?pageX:((NS6)?parseInt(style.left):style.pixelLeft)+parentL)+MM_hLeft);
          var tmpY=mouseY-(((NS4)?pageY:((NS6)?parseInt(style.top):style.pixelTop) +parentT)+MM_hTop);
          if (String(tmpX)=="NaN") tmpX=0; if (String(tmpY)=="NaN") tmpY=0;
          var tmpW = MM_hWidth;  if (tmpW <= 0) tmpW += ((NS4)?clip.width :offsetWidth);
          var tmpH = MM_hHeight; if (tmpH <= 0) tmpH += ((NS4)?clip.height:offsetHeight);
          if ((0 <= tmpX && tmpX < tmpW && 0 <= tmpY && tmpY < tmpH) && (maxDragZ == null
              || maxDragZ <= aLayerZ)) { curDrag = aLayer; maxDragZ = aLayerZ; } } }
      if (curDrag) {
        document.onmousemove = MM_dragLayer; if (NS4) document.captureEvents(Event.MOUSEMOVE);
        curLeft = (NS4)?curDrag.left:(NS6)?parseInt(curDrag.style.left):curDrag.style.pixelLeft;
        curTop = (NS4)?curDrag.top:(NS6)?parseInt(curDrag.style.top):curDrag.style.pixelTop;
        if (String(curLeft)=="NaN") curLeft=0; if (String(curTop)=="NaN") curTop=0;
        MM_oldX = mouseX - curLeft; MM_oldY = mouseY - curTop;
        document.MM_curDrag = curDrag;  curDrag.MM_SNAPPED=false;
        if(curDrag.MM_toFront) {
          eval('curDrag.'+((NS4)?'':'style.')+'zIndex=document.MM_maxZ+1');
          if (!curDrag.MM_dropBack) document.MM_maxZ++; }
        retVal = false; if(!NS4&&!NS6) event.returnValue = false;
    } } else if (theEvent == 'mousemove') {
      if (document.MM_curDrag) with (document.MM_curDrag) {
        var mouseX = (NS)?objName.pageX : event.clientX + document.body.scrollLeft;
        var mouseY = (NS)?objName.pageY : event.clientY + document.body.scrollTop;
        newLeft = mouseX-MM_oldX; newTop  = mouseY-MM_oldY;
        if (MM_bL!=null) newLeft = Math.max(newLeft,MM_bL);
        if (MM_bR!=null) newLeft = Math.min(newLeft,MM_bR);
        if (MM_bT!=null) newTop  = Math.max(newTop ,MM_bT);
        if (MM_bB!=null) newTop  = Math.min(newTop ,MM_bB);
        MM_LEFTRIGHT = newLeft-MM_startL; MM_UPDOWN = newTop-MM_startT;
        if (NS4) {left = newLeft; top = newTop;}
        else if (NS6){style.left = newLeft; style.top = newTop;}
        else {style.pixelLeft = newLeft; style.pixelTop = newTop;}
        if (MM_dragJS) eval(MM_dragJS);
        retVal = false; if(!NS) event.returnValue = false;
    } } else if (theEvent == 'mouseup') {
      document.onmousemove = null;
      if (NS) document.releaseEvents(Event.MOUSEMOVE);
      if (NS) document.captureEvents(Event.MOUSEDOWN); //for mac NS
      if (document.MM_curDrag) with (document.MM_curDrag) {
        if (typeof MM_targL =='number' && typeof MM_targT == 'number' &&
            (Math.pow(MM_targL-((NS4)?left:(NS6)?parseInt(style.left):style.pixelLeft),2)+
             Math.pow(MM_targT-((NS4)?top:(NS6)?parseInt(style.top):style.pixelTop),2))<=MM_tol) {
          if (NS4) {left = MM_targL; top = MM_targT;}
          else if (NS6) {style.left = MM_targL; style.top = MM_targT;}
          else {style.pixelLeft = MM_targL; style.pixelTop = MM_targT;}
          MM_SNAPPED = true; MM_LEFTRIGHT = MM_startL-MM_targL; MM_UPDOWN = MM_startT-MM_targT; }
        if (MM_everyTime || MM_SNAPPED) eval(MM_dropJS);
        if(MM_dropBack) {if (NS4) zIndex = MM_oldZ; else style.zIndex = MM_oldZ;}
        retVal = false; if(!NS) event.returnValue = false; }
      document.MM_curDrag = null;
    }
    if (NS) document.routeEvent(objName);
  } return retVal;
}
function loadwin(obj){
	with(MM_findObj(obj))with(style){
		filters[0].apply();
		display='';
		filters[0].play();
	}
}
function cs(captionBG,bodyBG,tableBG){
oldBody=document.body;
	with(oldBody){
		var newBody=cloneNode();
		style.filter='blendtrans(duration=1)';
		filters[0].apply();
		with(document.styleSheets[0]){
			with(rules[0].style){backgroundColor=captionBG;}
			with(rules[1].style){backgroundColor=bodyBG;}
			with(rules[2].style){backgroundColor=tableBG}
		}
		filters[0].play();
		setTimeout(function(){
				if(oldBody!=null){
					oldBody.applyElement(newBody, "inside")
					oldBody.swapNode(newBody);
					oldBody.removeNode(true);
					}
				},1500);
	}
}
//-->
</script>
<link href="./css/css_blue.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
body,td,th {
	font-size: 12px;
}
-->
</style>
<style type="text/css">
<!--
.caption {
	font-size: 12px;
	color: #FFFFFF;
	background-color: #00CCFF;
	padding-left: 5px;
	cursor: default;
	font-family: "Verdana", "Arial";
	border: 1px inset;
}
body {
	background-color: #f6f6f6;
	border: 1px inset;
	overflow: hidden;
}

td {
	font-family: "Verdana", "Arial";
	font-size: 12px;
	border: 0px;
}
td.caption {
	font-family: "Verdana", "Arial";
	font-size: 11px;
	border: 0px;
}
td.td_ {
	font-family: "Verdana", "Arial";
	font-size: 9px;
	border: 0px;
}
.win {
	filter:BlendTrans(duration=1) DropShadow(Color=#cccccc, OffX=3, OffY=3) alpha(opacity=90)
}
a {
	text-decoration: none;
	color: #003399;
}
a:hover {
	color: #FF0000;
}
input {
	font-family: "Verdana", "Arial";
	font-size: 9px;
	border-width: 1px;
}
.statusbar {
	font-family: "Tahoma", "Verdana";
	font-size: 9px;
	color: #999999;
	padding-left: 3px;
}
.button {
	border: 1px outset;
	text-align: center;
}
.navframe {
	padding: 5px;
}
-->
</style>

<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;

}
-->
</style>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.showImg.style.display="none";
	function GoSameDevice1(pages)
{
	var page;
	page=pages;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function GoSameDevice2(pages)
{
	var page;
	page=pages;
	window.open(page,"","left=100,top=100,width=750,height=450,resizable=no,scrollbars=yes");
}
//-->
</SCRIPT>

</head>

<body>

<table width="100%" height="97%" border="0" cellpadding="0" cellspacing="0" style="word-wrap:break-word;">
  <tr>
   <td valign="top" bgcolor="#F1F1F1"><img src="../images/left_top.jpg" width="200" height="19">
      <table width="160"  border="0" align="center" cellpadding="0" cellspacing="0" >
        <tr>
          <td height="30" valign="top" background="../images/left_title.gif"><table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td width="23%"><div align="center"><img src="../images/icon_4.gif" width="17" height="20"></div></td>
              <td width="77%" height="22"><span class="style1">用户信息</span></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td background="../images/left_back.gif" bgcolor="#FFFFFF"><table width="100%"  border="0" cellspacing="1" cellpadding="5">
            <tr>
              <td class="text"> <div align="center"><strong><img src="../images/ico_4.gif" width="13" height="12"> 姓名</strong></div></td>
              <td class="bottom_line"> <%=user.getUserInfo().get("per_name")%></td>
            </tr>
            <tr>
              <td class="text"> <div align="center"><strong><img src="../images/ico_4.gif" width="13" height="12"> 账号</strong></div></td>
              <td class="bottom_line"><%=user.getAccount()%></td>
            </tr>
            <tr>
              <%Map<String,String> cityNameMap = CityDAO.getCityIdCityNameMap();
              String cityNameAll=user.getCityId();
              String[] city_name=cityNameAll.split(",");
              String cityName1="";
              for(int i=0;i<city_name.length;i++)
              {
            	  cityName1 +=cityNameMap.get(city_name[i])+",";
              }
             String cityName=cityName1.substring(0, cityName1.length()-1);
              %>
              <td class="text"> <div align="center"><strong><img src="../images/ico_4.gif" width="13" height="12"> 属地</strong></div></td>
              <td class="bottom_line"   >
              <div style=" width: 80px; height:50px;overflow-x:hidden;overflow-y:scroll"><%=cityName%></div>
             </td>
            </tr>
            <tr>
              <td class="text"> <div align="center"><strong><img src="../images/ico_4.gif" width="13" height="12"> 部门</strong></div></td>
              <td class="bottom_line"><%=user.getUserInfo().get("per_dep_oid")%></td>
            </tr>
            <tr>
              <td class="text"> <div align="center"><strong><img src="../images/ico_4.gif" width="13" height="12"> 职务</strong></div></td>
              <td class="bottom_line"><%=user.getUserInfo().get("per_jobtitle")%></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td><img src="../images/left_down.gif" width="160" height="16"></td>
        </tr>
      </table>
      <br>
      <%if(!"nx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
      <table width="160"  border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="30" valign="top" background="../images/left_title.gif">
          <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="23%" height="20"><div align="center"><img src="../images/ico_1.gif" width="14" height="12"></div></td>
                <td width="77%" height="22"><span class="style1">客服热线</span></td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td background="../images/left_back.gif" bgcolor="#FFFFFF"><table width="100%"  border="0" cellspacing="1" cellpadding="5">
   <!-- 
              <tr>
                <td class="text">
                  <div align="right"><strong><img src="../images/ico_2.gif" width="12" height="12"> </strong></div></td>
                <td class="bottom_line"> <span class="input"><strong><em>025-83753913</em></strong></span></td>
              </tr>
              <tr>
                <td class="text">
                  <div align="right"><strong><img src="../images/ico_3.gif" width="12" height="12"></strong></div></td>
                <td class="bottom_line"><strong class="input"><em>025-83753909</em></strong></td>
              </tr>
   -->
              <tr>
                <td class="text">
                  <div align="center"></div></td>
                <td class="text">工作时间<br>
                  星期一到星期五<br>
                8:30-17:30</td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td><img src="../images/left_down.gif" width="160" height="16"></td>
        </tr>
    </table>
    <%} %>
    <!-- 新web -->
    <%if("1".equals(LipossGlobals.getLipossProperty("newWeb.exist"))
    	  && !"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
     <br>
     <table width="160"  border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="30" valign="top" background="../images/left_title.gif">
          <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="23%" height="20"><div align="center"><img src="../images/icon_link.png" width="14" height="12"></div></td>
                <td width="77%" height="22"><span class="style1">新功能</span></td>
              </tr>
          </table>
          </td>
        </tr>
        <tr>
          <td background="../images/left_back.gif" bgcolor="#FFFFFF"><table width="100%"  border="0" cellspacing="1" cellpadding="5">
              <tr>
                <td class="text">
                  <div align="center"></div></td>
                <td class="text"><a href='javascript:forwardNewEdit()'>进入新版本</a></td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td><img src="../images/left_down.gif" width="160" height="16"></td>
        </tr>
    </table>、
    <%} %>
    <!-- 新web结束 -->
    </td>
    <td valign="top" width="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19" background="../images/back.jpg">
    <span class="text">&nbsp;&nbsp;您好！您的当前位置：首页 
			
	</span>
    </td>
  </tr>
  <tr>
    <td valign="top" width="100%">		<table width="97%"  border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
			<td background="../images/k_1.gif" width="5" height="5"></td>
			<td background="../images/k_2.gif"></td>
			<td background="../images/k_3.gif" width="5" height="5"></td>
		  </tr>
		  <tr>
			<td width="700" background="../images/k_7.gif"><img src="../images/<%=image%>" width="695" height="304"><br></td>
			<td valign="top"><br>
			    <br>			    <br>
	            <br>            </td><td width="5" background="../images/k_8.gif"></td>
		  </tr>
		  <tr>
			<td width="700">
			
			
			
			</td>
			<td background="../images/k_5.gif"> </td>
			<td><img src="../images/k_6.gif" width="5" height="5"></td>
		  </tr>
		</table>
  </tr>
</table>		
		
	</td>
  </tr>
</table>
<%if(!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
<div id="results" style="position:absolute; left:212px; top:25px; width:575px; z-index:1;display:none;" class="win" onMouseDown="MM_dragLayer('results','',0,0,400,18,true,false,-1,-1,-1,-1,204,68,50,'',false,'')">
	<table width="400" border="1" cellpadding="0" cellspacing="0" bgcolor="#eeeeee">
		<tr>
			<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="caption">系统公告</td>
						<td class="td_" width="12" class="button"><a href="#" onClick="with(MM_findObj('resultswin').style)display=display=='none'?'':'none'">%</a></td>
						<td class="td_" width="12"class="button"><a href="#" onClick="MM_showHideLayers('results','','hide')">X</a></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="resultswin">
			<td height="318" valign="top" class="navframe">
				<aiframe name="mainframe" id="mainframe" src="http://www.chinaz.com" width="100%" height="100%" frameborder="0" scrolling="auto">
				<table width="98%"  border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td height="67" colspan="2" valign="top" class="kuang_yellow"><div align="center"><span class="text"><img src="../images/inside_3.jpg" width="500" height="23"><br>
                  </span>
                  </div>
                  <table width="95%"  border="0" align="center" cellpadding="3" cellspacing="2">
                    <tr>
                      <td class="bottom_line"><span class="text"><img src="../images/ico_6.gif" width="13" height="11">这里放一些通知、公告信息.如本月系统升级，给大家带不便请见谅。</span></td>
                    </tr>
                    <tr>
                      <td class="bottom_line"><span class="text"><img src="../images/ico_6.gif" width="13" height="11"> 最新功能介绍 </span>
                      <br>
                      <marquee height="40" behavior="scroll"  direction="up"  loop="-1" scrollamount="2"  onmouseover=this.stop() onmouseout=this.start()>
	                    <%
	                        String sql1 = "select * from tab_broad_info where titletype=2 ";
                            // teledb
                            if (DBUtil.GetDB() == 3) {
                                sql1 = "select city_id, id, title from tab_broad_info where titletype=2 ";
                            }
                            com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql1);
                            psql.getSQL();
	                        Cursor cursor1 = DataSetBean.getCursor(sql1);
	                        Map fields1 = cursor1.getNext();
	                  	    while(fields1 != null){ 
	                  	  	String[] tem_city_id = ((String)fields1.get("city_id")).split(","); 
	                  	  	for(int i=0;i<tem_city_id.length;i++){
	                  	  		if(tem_List.contains(tem_city_id[i])){
	                  	  		out.print("<a target='_blank' href='viewNews2.jsp?id="+ fields1.get("id") +"'>"+"<br>"+"<img src='../images/index_main_ico_1.gif'>" + fields1.get("title") + "</a> ");	            				         		
	                  	  		break;
	                  	  		}
	                  	     	
	                  	  	}	                  	                  		          				
	          				      fields1 = cursor1.getNext();
	          			    }              	                    	                    	                  	               
	                    %>
                    	</marquee>                      
                      </td>
                    </tr>
                    <tr>
                      <td class="bottom_line"><span class="text"><img src="../images/ico_6.gif" width="13" height="11"> 公告 </span>
                      <br>
                      <marquee height="60" behavior="scroll"  direction="up"  loop="-1" scrollamount="2"  onmouseover=this.stop() onmouseout=this.start()>
	                    <%
	                    String sql2 = "select * from tab_broad_info where titletype=1 ";
                        // teledb
                        if (DBUtil.GetDB() == 3) {
                            sql2 = "select city_id, id, title from tab_broad_info where titletype=1 ";
                        }
                        com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(sql2);
                        psql2.getSQL();
                        Cursor cursor2 = DataSetBean.getCursor(sql2);
                        Map fields2 = cursor2.getNext();
                  	       while(fields2 != null){
                  	    	 //String[] tem_city_id = ((String)fields2.get("city_id")).split(",");
 	                  	  	String[] tem_city_id1 = ((String)fields2.get("city_id")).split(","); 
 	                  	 for(int i=0;i<tem_city_id1.length;i++){
	                  	  		if(tem_List.contains(tem_city_id1[i])){
	                  	  		 out.print("<a target='_blank' href='viewNews1.jsp?id="+ fields2.get("id") +"'>"+"<br>"+"<img src='../images/index_main_ico_1.gif'>" + fields2.get("title") +"</a> ");	       				         		
	                  	  		 break;
	                  	  		}
	                  	     	
	                  	  	}	                  	               			            				
	            			fields2 = cursor2.getNext();
                  	    }
	                    %>
                    	</marquee>
                      </td>
                    </tr>
                    <tr>
                      <td class="bottom_line"><span class="text"><img src="../images/ico_6.gif" width="13" height="11"> 提示 </span>
                      <br>
                      <marquee height="40" behavior="scroll"  direction="up"  loop="-1" scrollamount="2"  onmouseover=this.stop() onmouseout=this.start()>
                      <%        
                      String sql3 = "select * from tab_broad_info where titletype=3 ";
                      // teledb
                      if (DBUtil.GetDB() == 3) {
                          sql3 = "select city_id, id, title from tab_broad_info where titletype=3 ";
                      }
                      com.linkage.commons.db.PrepareSQL psql3 = new com.linkage.commons.db.PrepareSQL(sql3);
                      psql3.getSQL();
                      Cursor cursor3 = DataSetBean.getCursor(sql3);
                      Map fields3 = cursor3.getNext();
                  	    while(fields3 != null){                   	  
                  	  	String[] tem_city_id2 = ((String)fields3.get("city_id")).split(",");
                  		 for(int i=0;i<tem_city_id2.length;i++){
	                  	  		if(tem_List.contains(tem_city_id2[i])){
	                  	  		out.print("<a target='_blank' href='viewNews.jsp?id="+ fields3.get("id") +"'>"+"<br>"+"<img src='../images/index_main_ico_1.gif'>" + fields3.get("title") + "</a> ");	            				         					
	                  	  		break;
	                  	  		}
	                  	     	
	                  	  	}                  	           		          				
          				   fields3 = cursor3.getNext();
          			   }                              
                      %>         
                       </marquee> 
                      </td>
                    </tr>
                  </table>      
                <p align="right">&nbsp;</p></td>
              </tr>
            </table>
				</aiframe>
			</td>
		</tr>
		<tr>
			<td height="14" class="statusbar">Ready!</td>
		</tr>
	</table>
<br>
</div>
<% } %>
</body>
</html>

<script type="text/javascript">
<%if(!"sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
VJBox.Import("RightEdge");
new VJBox.Class.webUI.RightEdge("results");

loadwin("results");
<% } %>
function forwardNewEdit(){
	top.window.location.href = "newWeb.jsp";
}
</script>

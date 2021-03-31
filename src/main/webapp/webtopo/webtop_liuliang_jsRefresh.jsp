<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.text.*,java.util.*" %>
<%@ page import= "com.linkage.liposs.common.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>流量实时显示</title>

</head>

<body onload="loadData()">
<%@ include file="../head.jsp"%>
<%@ include file="../opentoolbar.jsp"%>
<% 
  	request.setCharacterEncoding("GBK");
	String device_id= request.getParameter("device_id");
%>

<div id="PendingMessage"
	style="position:absolute;z-index:3;top:0px;left:0px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">请稍等······</span></td>
	</tr>
</table>
</center>
</div>

<table width="780" border="0" cellspacing="0" cellpadding="0">
  <tr>
	<td>是否自动刷新：<INPUT TYPE="radio" NAME="isRefresh" onclick="changIsRefresh(true)" checked>是
	<INPUT TYPE="radio" NAME="isRefresh" onclick="changIsRefresh(false)">否&nbsp;&nbsp; &nbsp;&nbsp; 
	刷新频率选择：<INPUT
		TYPE="radio" NAME="polltime" value="3"
		onclick="refreshIframe(this.value)">3分钟&nbsp;&nbsp; <INPUT
		TYPE="radio" NAME="polltime" value="5" checked
		onclick="refreshIframe(this.value)">5分钟&nbsp;&nbsp; <INPUT
		TYPE="radio" NAME="polltime" value="10"
		onclick="refreshIframe(this.value)">10分钟&nbsp;&nbsp; <INPUT
		TYPE="radio" NAME="polltime" value="15"
		onclick="refreshIframe(this.value)">15分钟&nbsp;&nbsp; <INPUT
		TYPE="radio" NAME="polltime" value="30"
		onclick="refreshIframe(this.value)">30分钟</td>
  </tr>
  <tr>
		<td width="100%%">
		 <iframe id="childframe" style="overflow:auto;width:790px;height:600px;"></iframe>
			</td>
          </tr>
      </table>
    </td>
  </tr>
</table>

<script language="javascript">
var isRefresh = true;
var reloadtime = getReloadtime();

function getReloadtime(){
	var o = document.all('polltime');
	for(var i=0;i<o.length;i++){
		if(o.polltime[i].checked){
			return o.polltime[i].value;
			break;
		}
	}
	return 5;
}

function loadData(){
	document.all("childframe").src = "webtop_liuliang.jsp?device_id=<%=device_id%>&polltime="+reloadtime;
	showMsgDlg();
	setIFrameHeight('childframe');
}

function changIsRefresh(v){
		isRefresh = v;
		var tem_v = getReloadtime();
		refreshIframe(tem_v);
}

function refreshIframe(v){
	//用户选择刷新时
	//alert("reloadtime in with isRefresh "+isRefresh+" and reloadtime "+v);
	
	if(isRefresh==true){
		page = document.all("childframe").src;
		pos = page.indexOf("&polltime=");
		tmp = page.substring(0,pos);
		page = tmp + "&polltime="+ v+"&tt="+new Date().getTime();
		//alert("page "+page)
		//showMsgDlg();
		document.all("childframe").src = page;
		showMsgDlg();
		setIFrameHeight('childframe');
	}
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}

/**
 * iframe高度处理
 * @author zxub 2006-09-29
 */

/**
 * 设置iframe高度等于内部页面高度，用于内部页面
 */ 
function setParentHeight(_iframeId)
{    
    if (parent.setIFrameHeight) return;
    
    if (window.addEventListener) //firefox
    {       
        var _action=function()
        {            
            var _iframe=parent.document.getElementById(_iframeId);
            if (!_iframe) return;
            _iframe.height=_iframe.contentDocument.body.offsetHeight+16;
        }   
        window.addEventListener("load", _action, false);
    }
    else if (window.attachEvent) //IE
    {
        var _action=function()
        {
            if (!parent.document.getElementById(_iframeId)) return;
            parent.document.getElementById(_iframeId).height=document.body.scrollHeight;
        }
        window.attachEvent("onload", _action);
    }
}

/**
 * 设置iframe高度等于内部页面高度，用于父级页面
 */
function setIFrameHeight(_iframeId)
{    
    if (window.addEventListener) //firefox
    {          
        var _action=function()
        {       
            var _iframe=document.getElementById(_iframeId);
            if (!_iframe) return;
            _iframe.height=_iframe.contentDocument.body.scrollHeight;
            _iframe.onload=function()
            {
                this.height=this.contentDocument.body.offsetHeight+16;
            }
        }
        window.addEventListener("load", _action, false);
    }
    else if (window.attachEvent) //IE
    {
        var _action=function()
        {        
            if (!document.getElementById(_iframeId)) return;
            document.getElementById(_iframeId).height=document.frames[_iframeId].document.body.scrollHeight;
            document.getElementById(_iframeId).onreadystatechange=function()
            {
                if (this.readyState=="complete")
                {
                    this.height=document.frames[_iframeId].document.body.scrollHeight;
                }
            }
        }
        window.attachEvent("onload", _action);
    }
}
</script>
</body>
</html>

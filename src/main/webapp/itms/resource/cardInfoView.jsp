<%--
Author		: chenjie(67371)
Date		: 2011-5-7
--%>
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<SCRIPT LANGUAGE="JavaScript">

//-----------------ajax----------------------------------------
  var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
   
   //ajax一个通用方法
	function sendRequest(method,url,object){
		request.open(method, url, true);
		request.onreadystatechange = function(){refreshPage(object);};
		request.send(null);
	}
	function refreshPage(object){
		if (request.readyState == 4) {
    		if (request.status == 200) {
        		object.innerHTML = request.responseText;
			} else{
				alert("status is " + request.status);
			}
		}
	}
/**
	function sendRequest2(method, url, object,sparam){
		request.open(method, url, true);
		request.onreadystatechange = function(){
			refreshPage(object);
			doSomething(sparam);
		};
		request.send(null);
	}
	**/
//---------------------------------------------------------------

</SCRIPT>


<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp" %>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" >
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">卡信息查询</div>
				</td>
				<td>&nbsp;</td>
			</tr>
	</table>
	<!-- 高级查询part -->
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<tr>
			<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
						<tr><th colspan="4" id="gwShare_thTitle">高级查询</th></tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">LOID</TD>
							<TD align="left" width="35%">
								<INPUT TYPE="text" NAME="username" size=20
									 class=bk maxlength=40>&nbsp;<font color="#FF0000"></font>
							</TD>
							<TD align="right" class=column width="15%">卡序列号</TD>
							<TD width="35%">
								<INPUT TYPE="text" NAME="card_serialnumber" maxlength=64
									 class=bk size=20>&nbsp;<font color="#FF0000"></font>
							</TD>
						</TR>
						<!-- 
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">在线状态</TD>
							<TD align="left" colspan=3>
								<select name="online_status" class="bk">
									<option value="-1">==请选择==</option>
									<option value="1">在线</option>
									<option value="0">下线</option>
								</select>
							</TD>
						</TR>
						 -->
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
								onclick="javascript:gwShare_queryChange('1');" name="gwShare_jiadan" value="简单查询" />
								<input type="button" class=jianbian style="CURSOR:hand" style="display:none " 
								onclick="javascript:gwShare_queryChange('2');" name="gwShare_gaoji" value="高级查询" />
								<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
								onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="导入查询" />
								<input type="button" onclick="javascript:doQuery()" class=jianbian 
								name="gwShare_queryButton" value=" 查 询 " />
								<input class=jianbian onclick="" type="reset"
							    value=" 重 置 " />
							</td>
						</tr>
				</table>
			</td>
		</tr>
	</table>
	</FORM>
	<!-- 展示结果part -->
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999 id="idData">
              	<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>

<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

// 查询
function doQuery()
{
	trimAll();
	var url = "<s:url value='/itms/resource/cardInfo!queryCard.action'/>";
	var username = $("input[@name='username']").val();
	var card_serialnumber = $("input[@name='card_serialnumber']").val();
	var online_status = $("select[@name='online_status']").val();
	
	/*
	$.post(url,{
		vendor:vendor,
		device_model:device_model,
		hard_version:hard_version,
		soft_version:soft_version,
		is_check:is_check,
		rela_dev_type:rela_dev_type
	},function(ajax){
		alert("success");
	});
	*/
	
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = url;
	form.target = "dataForm";
	form.submit();
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

/** 工具方法 **/
/*LTrim(string):去除左边的空格*/
function LTrim(str){
    var whitespace = new String("　 \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):去除右边的空格*/
function RTrim(str){
    var whitespace = new String("　 \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):去除字符串两边的空格*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//全部trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}
</SCRIPT>



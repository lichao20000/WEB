<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/commonUtil.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<%
	String gw_type = request.getParameter("gw_type");
	if(null == gw_type ||  "".equals(gw_type)){
		gw_type="1";
	}
%>

<SCRIPT LANGUAGE="JavaScript">


function do_query(){
	$("div[@id='QueryData']").html("");
	setTimeout("gwShare_queryDevice()", 2000);
}


function gwShare_queryDevice(){
	var	width=800;
	var height=450;
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("请先上传文件！");
        return;
    }
    var url="<s:url value="/gwms/report/multicastDownReport!getFileSize.action"/>?gwShare_fileName="+gwShare_fileName;
    $.post(url,{
    },function(ajax){
            if (ajax>2000)
            {
                alert("文件清单数量超过最大数2000!");
                return false;
            }
            else
            {
                document.gwShare_selectForm.submit();
            }
    });
}

function toExcel(){
    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("请先上传文件！");
        return;
    }

    var url="<s:url value="/gwms/report/multicastDownReport!getFileSize.action"/>?gwShare_fileName="+gwShare_fileName;
    $.post(url,{
    },function(ajax){
            if (ajax>2000)
            {
                alert("文件清单数量超过最大数2000!");
                return false;
            }
            else
            {
                var page="<s:url value='/gwms/report/multicastDownReport!toExcel.action'/>?gwShare_fileName="+gwShare_fileName;
                document.all("childFrm").src=page;
            }
    });

}

function checkFileSize(){

    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("请先上传文件！");
        return;
    }
 	var url="<s:url value="/gwms/report/multicastDownReport!getFileSize.action"/>?gwShare_fileName="+gwShare_fileName;
    $.post(url,{
    },function(ajax){
            if (ajax>2000)
            {
                alert("文件清单数量超过最大数2000!");
                return false;
            }
            return true;
    });
}



//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize()
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}




</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="gwShare_selectForm" action="<s:url value="/gwms/report/multicastDownReport!queryDeviceList.action"/>"
    target="dataForm" >
<input type="hidden" name="gwShare_queryType" value="3" />
<input type="hidden" name="gwShare_import_value" value="none" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
        <td>
            <table width="100%" height="30" border="0" cellspacing="0"
                cellpadding="0" class="green_gargtd">
                <tr>
                    <td width="162" align="center" class="title_bigwhite" nowrap>
                        组播下移报表查询
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr id="gwShare_tr31" bgcolor="#FFFFFF" >
					<td align="right" width="15%">提交文件</td>
					<td colspan="5" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32">
					<td CLASS="green_foot" align="right">注意事项</td>
					<td colspan="5" CLASS="green_foot">
					1、需要导入的文件格式限于Excel、文本文件，即xls格式、txt格式。 <br>
					2、文件的第一行为标题行，即【用户账号】。
					 <br>
					3、文件只有一列。
					 <br>
					4、文件行数不要超过2000，超过2000请多批次导入。
					<br>
                    5、在上传文件后，可不查询直接导出结果文件。
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="3" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_query()" class=jianbian
						name="gwShare_queryButton" value=" 查询 " />
					</td>
					<td colspan="3" align="right" class="green_foot" width="100%">
                        <input type="button" onclick="javascript:toExcel()" class=jianbian
                        name="gwShare_queryButton" value=" 导出 " />
                    </td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
        <td>
            <iframe id="dataForm" name="dataForm" height="500" frameborder="0"
                scrolling="auto" width="100%" src=""></iframe>
        </td>

    </tr>
    <tr STYLE="display: none">
        <td>
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</TABLE>
</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
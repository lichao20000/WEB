<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="x-ua-compatible" content="IE=7" >
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/slide.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<title>批量参数配置定制查询</title>
<script language="JavaScript">

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
	dyniframesize();
	timeDefault();
});

function timeDefault(){
	var date = new Date();
	 var dt = date;
	 var seperator1 = "-"; 
	 var year = date.getFullYear();
	 var month = date.getMonth() + 1;
   var strDate = date.getDate();
  
   if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
   
    var currentdate = year + seperator1 + month + seperator1 + strDate+" 23:59:59";
    
    
   var beforetdate = year + seperator1 + month + seperator1 + "01 00:00:00";
   $("input[@name='starttime']").val(beforetdate);
	 $("input[@name='endtime']").val(currentdate);
}

$(window).resize(function(){
	dyniframesize();
}); 

function doQuery(){
    $("#data").show();
    $("#QueryData").html("正在查询，请稍等....");
	$("input[@name='button']").attr("disabled", true);
	$("input[@name='button']").css("backgroundColor","#AAAAAA");
	var form = document.getElementById("frm");
	form.action ="<s:url value='/gtms/config/paramNodeBatchConfigAction!getNodeBatchList.action'/>";
	form.submit();
}

</script>
</head>
<body>
	<form name="frm" id="frm" method="post" target="dataForm">
		<div class="it_main">
			<table width="100%" border="0" cellspacing="10" cellpadding="0" class="mainSearch" bgcolor="#f2f5ff">
				<tr>
				    <td class="tit">定制人:</td>
				  <td>
						<input type="text" name="customId" "/>
					</td>
				    <td class="tit">开始时间:</td>
				    <td>
				    	<input type="text" name="starttime" readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
				    </td>
				    <td class="tit">结束时间:</td>
				    <td>
				    	<input type="text" name="endtime" readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
				    </td>
				  </tr>
				  <tr>
				    <td class="tit">文件名称:</td>
				    <td>
						<input class="imp_ipt" type="text" name="file_Name" "/>
						
					  </td>
				  </tr>
			</table>
			<div class="mainSearch_btn" style="background-color: #d2dff8">
				 <input name="button" type="button" onclick="doQuery();" class="it_btn" value="查询" />
			</div>
			<div id="data" style="display: none">
				<div id="QueryData">
					正在查询，请稍等....
				</div>
			</div>
			<div style="height: 20px;">
			</div>
			<div class="it_table">
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</div>
			<div id="divData" style="display: none;position: absolute;z-index: 1200">
				<div id="QueryData">
				</div>
			</div>
		</div>	
		
	</form>
</body>
</html>
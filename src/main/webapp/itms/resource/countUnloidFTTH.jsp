
<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">


//-----------------ajax----------------------------------------
  var request = false;
  var portNumber=1;
  var addPortNumber=0;
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








function clearData()
{
	document.getElementsByName("vendor_add")[0].value=-1;
	change_model('deviceModel','-1');
	document.getElementsByName("speversion")[0].value="";
	document.getElementsByName("hard_version_add")[0].value="";
	document.getElementsByName("soft_version_add")[0].value="";
	document.getElementsByName("is_check_add")[0].value=0;
	document.getElementsByName("rela_dev_type_add")[0].value=1;

	document.getElementById("updateId").value="-1";

	document.getElementById("actLabel").innerHTML="添加";



}

$(function(){

	setValue();
});


function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	document.getElementById("startTime").value=year+"-"+month+"-"+day+" 00:00:00";
	document.getElementById("endTime").value=year+"-"+month+"-"+day+" 23:59:59";
//	document.getElementById("startTime").value="";
//	document.getElementById("endTime").value="";

}



</SCRIPT>

</head>
<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">FTTH未上报LOID设备</div>
				</td>
				<td><img src="/itms/images/attention_2.gif"
					width="15" height="12"> 查询时间为设备最近上报平台的时间，设备类型为eb-c</td>

			</tr>
		</table>
		<!-- 高级查询part -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">FTTH未上报LOID设备查询</th>
					</tr>

				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">开始时间</TD>
						<TD align="left" width="35%"><lk:date id="startTime"
							name="startTime" type="all" /></TD>
						<TD align="right" class=column width="15%">结束时间</TD>
						<TD align="left" width="35%"><lk:date id="endTime"
							name="endTime" type="all" /></TD>
					</TR>

				<TR bgcolor=#FFFFFF>
									<td class="column" width='15%' align="right">
										属&nbsp;&nbsp;&nbsp;&nbsp;地
									</td>
									<td width='35%' align="left"
										colspan="3">
										<select name="cityId" class="bk">
											<option value="-1">
												==请选择==
											</option>
											<s:iterator var="citylist" value="CityList">
												<option value="<s:property value='#citylist.city_id'/>"
													<s:property value="#citylist.city_id.equals(cityId)?'selected':''"/>>
													==
													<s:property value="#citylist.city_name" />
													==
												</option>
											</s:iterator>
										</select>
									</td>
</TR>


				<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						 <input type="button"
							onclick="javascript:queryDevice()" class=jianbian
							name="gwShare_queryButton" value=" 查 询 " /> <input type="reset"
							class=jianbian name="gwShare_reButto" value=" 重 置 " /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- 展示结果part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>

</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">



//查询
function queryDevice()
{
	trimAll();

	var url = "<s:url value="/itms/resource/countFtthACT!queryUnloid.action"/>";

	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/itms/resource/countFtthACT!queryUnloid.action"/>";

	form.submit();

	showAddPart(false);
}





function save()
{
	trimAll();
	if(!CheckForm())
		return;
	var url = "<s:url value="/itms/resource/deviceTypeInfo!addDevType.action"/>";
	var vendor = $("select[@name='vendor_add']").val();
	var device_model = $("select[@name='device_model_add']").val();
	var speversion = $("input[@name='speversion']").val();
	var hard_version = $("input[@name='hard_version_add']").val();
	var soft_version = $("input[@name='soft_version_add']").val();
	var is_check = $("select[@name='is_check_add']").val();
	var rela_dev_type = $("select[@name='rela_dev_type_add']").val();
	var deviceTypeId = $("input[@id='updateId']").val();
	var typeId = $("select[@name='type_id']").val();
	var is_uim = $("select[@name='is_uim']").val();
	var is_ftth = $("select[@name='is_ftth']").val();
	var protocol2 = $("input[@name='protocol2']");
	var protocol1 = $("input[@name='protocol1']");
	var protocol0 = $("input[@name='protocol0']");
    var servertype="";

    if (protocol2.attr("checked") == true) {
		servertype=servertype+"2";
	}
	if (protocol1.attr("checked") == true) {
		if(servertype == ""){
			servertype="1";
		}
		else{
			servertype=servertype+"@1";
		}

	}
	if (protocol0.attr("checked") == true) {
		if(servertype == ""){
			servertype="0";
		}
		else{
			servertype=servertype+"@0";
		}

	}

  var port_name =document.getElementsByName("port_name");
     var portInfo="";
     var allPort="";
     if(port_name.length>0)
         {
    	 var port_dir =document.getElementsByName("port_dir");
         var port_type =document.getElementsByName("port_type");
         var port_desc =document.getElementsByName("port_desc");


         for(var i=0;i<port_name.length;i++){
             if(port_name[i].value==""){
            	 alert("端口名称不能为空");
            	 return;
             }
             if(port_dir[i].value==""){
            	 alert("端口路径不能为空");
            	 return;
             }
             if(port_type[i].value==""){
            	 alert("端口类型不能为空");
            	 return;
             }
             portInfo=portInfo+ port_name[i].value+"@";
        	 portInfo=portInfo+port_dir[i].value+"@";
        	 portInfo=portInfo+port_type[i].value+"@";
        	 portInfo=portInfo+port_desc[i].value+"#";
        }
     }

	$.post(url,{
		deviceTypeId:deviceTypeId,
		vendor:vendor,
		device_model:device_model,
		hard_version:encodeURIComponent(hard_version),
		speversion:encodeURIComponent(speversion),
		soft_version:encodeURIComponent(soft_version),
		is_check:is_check,
		typeId:typeId,
		rela_dev_type:rela_dev_type,
		is_ftth:is_ftth,
		servertype:servertype,
		portInfo:encodeURIComponent(portInfo)

	},function(ajax){
		alert(ajax);
		if(ajax.indexOf("成功") != -1)
		{
			// 普通方式提交
			var form = document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
			//form.target = "dataForm";
			 var port_name =document.getElementsByName("port_name");
			 var j=port_name.length;

			 if(port_name.length>0){
				  for(var i=0;i<j;i++){
					  var tr=port_name[0].parentNode.parentNode;
	            	  var tbody=tr.parentNode;
		               tbody.removeChild(tr);
		         }
			 }

			 form.submit();

		}
	});

	showAddPart(false);
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

// 某些字段不允许编辑
function disableLabel(tag)
{
	$("select[@name='vendor_add']").attr("disabled",tag);
	$("select[@name='device_model_add']").attr("disabled",tag);
	//增加判断，如果为空，则允许编辑 modify by zhangcong@2011-09-26
	//if(trim(document.all("speversion").value) == '')
	//{
		$("input[@name='speversion']").attr("disabled",false);
	//}else
	//{
	//	$("input[@name='speversion']").attr("disabled",true);
	//}
	$("input[@name='hard_version_add']").attr("disabled",tag);
	$("input[@name='soft_version_add']").attr("disabled",tag);
}

// 隐藏页面下面的添加区域
function showAddPart(tag)
{
	if(tag)
		$("table[@id='addTable']").show();
	else
		$("table[@id='addTable']").hide();
}

$(function(){

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
</html>

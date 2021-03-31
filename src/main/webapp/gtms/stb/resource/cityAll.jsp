<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
String jktest1 = request.getParameter("jktest");
String cityid="";
if(jktest1!=""&&jktest1!=null)
{
	cityid=request.getParameter("cityid");
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>

</head>
<body>
	<br>
	<form action="" method="post" name="batchexform" onsubmit="">
		<input type="hidden" name="cityIds" id="cityIds" value="" /> <input
			type="hidden" name="city_id" id="city_id" value="<%=cityid%>" />
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">选择属地</td>
			</tr>
			<TR>
				<TD class="title_2" align="center" width="15%">一级属地</TD>
				<TD width="85%" colspan="3">
					<div id="firstCity"></div>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">二级属地</TD>
				<TD width="85%" colspan="3">
					<div id="secondCity"></div>
				</TD>
			</TR>
			<!--  <TR>
					<TD class="title_2" align="center" width="15%">
						三级属地
					</TD>
					<TD width="85%" colspan="3">
						<div id="thirdCity">
							
						</div>
					</TD>
				</TR> -->
			<TR>
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button id="doButton" onClick="returnTime();">确定</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button id="doButton" onclick="javascript:window.close();"">
							取消</button>
					</div>
				</td>
			</TR>
		</table>
	</form>
	<br>
</body>
<script type="text/javascript">
var cityid='<%=cityid%>';
function returnTime()
{
	var firstCitys="";
	$("input[@name='firstCitys'][@checked]").each(function(){ 
		firstCitys += $(this).val()+",";
    });
	if(firstCitys!=""){
		firstCitys=firstCitys.substring(0,firstCitys.length-1);
	}
	var secondCitys="";
	$("input[@name='secondCitys'][@checked]").each(function(){ 
		secondCitys += $(this).val()+",";
    });
	if(secondCitys!=""){
		secondCitys=secondCitys.substring(0,secondCitys.length-1);
	}
	var url ="<s:url value='/gtms/stb/resource/cityAll!getcity.action'/>";
	$.post(url,{
		firstCitys:firstCitys,
		secondCitys:secondCitys
	},function(ajax)
	{
		
			if(ajax!="00")
			{
			window.returnValue = ajax+",01";
			}else{
			window.returnValue = ajax;
			}
		
		window.close();
	});
	//var citys=firstCitys+","+secondCitys;
	
}
function getfirstCity()
{
	var firstCitys="";
	$("input[@name='firstCitys'][@checked]").each(function(){ 
		firstCitys += $(this).val()+",";
    });
	if(firstCitys!=""){
		firstCitys=firstCitys.substring(0,firstCitys.length-1);
	}
	var url ="<s:url value='/gtms/stb/resource/cityAll!getsecondCity.action'/>";
	$.post(url,{
		firstCitys:firstCitys
	},function(ajax)
	{
		$("input[@name='secondCitys']").attr("checked",false);
		$("input[@name='secondCitys']").attr("disabled","");
		var checkBoxArray = ajax.split("#");
		for(var i=0;i<checkBoxArray.length;i++){
			$("input[name='secondCitys']").each(function(){
				if($(this).val()==checkBoxArray[i]){
					$(this).attr("checked","checked");
					$(this).attr("disabled","disabled");
				}
			})
		}


	});
}

		$(function() {
			var url = "<s:url value='/gtms/stb/resource/cityAll!getfirstCity.action'/>";
				$("div[@id='firstCity']").html("");
				$("div[@id='secondCity']").html("");
				$.post(url,{
				},function(ajax){
					if(ajax!=""){
						var fristcity=ajax.split(",");
						var lineData = fristcity[0].split("#");
						var instName = '<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
						if(typeof(lineData)&&typeof(lineData.length)){
							for(var i=0;i<lineData.length;i++){
								var oneElement = lineData[i].split("$");
								var xValue = oneElement[0];
								var xText = oneElement[1];
								if((xValue!=""&&xValue!=null)&&(xText!=""&&xText!=null))
								{
									//xValue!="01"为“空区域”，仅在重庆使用，暂时只对重庆做区分
									if("cq_dx" == instName)
									{
										var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='firstCitys' id='firstCitys'  onclick='getfirstCity()' value='"+xValue+"'>"+xText+"  ";
										$("div[@id='firstCity']").append(checkboxtxt);
									}else if("cq_dx" != instName && xValue!="01")
									{
										var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='firstCitys' id='firstCitys'  onclick='getfirstCity()' value='"+xValue+"'>"+xText+"  ";
										$("div[@id='firstCity']").append(checkboxtxt)
									}
								}
								}
							}
						var lineData1 = fristcity[1].split("|");
						for(var j=0;j<lineData1.length;j++){
						var lineData2=lineData1[j].split("#");
						if(typeof(lineData2)&&typeof(lineData2.length)){
							for(var i=1;i<lineData2.length;i++){
								var oneElement = lineData2[i].split("$");
								var xValue = oneElement[0];
								var xText = oneElement[1];
								var checkboxtxt = "&nbsp;&nbsp;&nbsp;<input type='checkbox' name='secondCitys' id='secondCitys' value='"+xValue+"'>"+xText+"  ";
								$("div[@id='secondCity']").append(checkboxtxt);
								}
							var checkboxtxt1 = "<br><br> ";
							$("div[@id='secondCity']").append(checkboxtxt1);
							}
						}
						 jk();  
						}else{
							$("div[@id='firstCity']").append("没有属地！");
						}
				});
	}); 
		function jk(){
			var city_id=$("input[name='city_id']").val();
			url="<s:url value='/gtms/stb/resource/cityAll!getFirstCityList.action'/>";
			if(city_id!=""&&city_id!=null){
				$.post(url,{
					city_id:city_id
				},function(ajax){
					if(ajax!=""){
						var checkBoxArray = ajax.split(",");
						for(var j=0;j<checkBoxArray.length;j++){
							$("input[name='firstCitys']").each(function(){
								if($(this).val()==checkBoxArray[j]){
									$(this).attr("checked","checked");
								}
							})
						}
						for(var i=0;i<checkBoxArray.length;i++){
							$("input[name='secondCitys']").each(function(){
								if($(this).val()==checkBoxArray[i]){
									$(this).attr("checked","checked");
									$(this).attr("disabled","disabled");
								}
							})
						}
						$("input[name='city_id']").val("");
						}else{
							$("div[@id='firstCity']").append("没有属地！");
						}
				});
			}
		}
</script>
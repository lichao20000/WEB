<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});
     function ExecMod(){
     		if(CheckForm()){
     			var _device_id = $("input[@name='textDeviceId']").val();	
				if(_device_id == null || _device_id == ""){
					alert("请先查询设备!");
					return false;
				}
		        page = "jt_device_zendan_from4_save.jsp?device_id=" +_device_id + "&oid_type=2&type=1";
				document.all("div_ping").innerHTML = "正在载入诊断结果，请耐心等待....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
          }
	
	function CheckForm(){
		var _device_id = $("input[@name='textDeviceId']").val();	
		if(_device_id == null || _device_id == ""){
			alert("请先查询设备!");
			return false;
		}	
		return true;		
	}
	
	function deviceResult(returnVal){
		
		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		$("input[@name='textDeviceId']").val("");
		
		var city_id;
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
			
			city_id = $.trim(returnVal[2][i][4]);
		}
		
		document.getElementById("tr002").style.display = "none";
		
		$("tr[@id='trDeviceResult']").css("display","");
		var device_id = $("input[@name='textDeviceId']").val();
		 $("td[@id='select_map']").html("正在查询模板...");
		queryDigitMap(device_id,city_id);
	}
	
	function queryDigitMap(device_id,city_id)
	{
		var url = "<s:url value='/itms/config/digitMapConfig!queryForConfig.action'/>";
		$.post(url,{
           device_id: device_id, cityId: city_id
           },function(ajax){
	           $("td[@id='select_map']").html(ajax);
	           
		         if(ajax.indexOf('font') == -1)
		         {
		         	$("input[id='doButton']").attr("disabled", false);
		         }
		         else
		         {
		       	    $("input[id='doButton']").attr("disabled", true);
		         }
         });
         
        
	}
	
	
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframe1"]

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


$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>

<%@ include file="../../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION=""
				onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											单台终端下发配置
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<td colspan="4">
										<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
										设备属地：
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										设备序列号：
										<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<td nowrap align="right" class=column width="15%">
										数图模板配置：
									</td>
									<td colspan=3 id="select_map">
										<font color=red>请先查询设备！</font>
									</td>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" value="下发配置" class=btn id="doButton" disabled
														onclick="doConfig()">
													&nbsp;&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
						<!--  	<TR bgcolor="#FFFFFF">
									<TH colspan="4">
										终端下发配置结果
									</TH>
								</TR> -->	
								<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
									<td colspan="4" valign="top" class=column>
										<div id="div_strategy"
											style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
									</td>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>

<script>
function doConfig()
{
	
	var map_id = $("select[@id='map_id']").val();
	var device_id = $("input[@name='textDeviceId']").val();
	var gw_type =  <%=request.getParameter("gw_type")%>;
	if(device_id == "")
	{
		alert("请先选择设备！");
		return;
	}
	if(map_id == "")
	{
		alert("请先选择模板！");
		return;
	}
		
	var url = "<s:url value='/itms/config/digitMapConfig!doConfig.action'/>";
	//alert(gw_type);
		$.post(url,{
              map_id: map_id, 
              device_id: device_id,
         	  gw_type:gw_type
           },function(ajax){
	           	var s = ajax.split(";");
			    if(s[0]=="-1"){
			        alert(s[1]);
				}
				if(s[0]=="1"){
					var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
					var strategyId = s[2];
					$.post(url,{
		           		strategyId:strategyId
		            },function(ajax){
		          	   	$("div[@id='div_strategy']").html("");
						$("div[@id='div_strategy']").append(ajax);
		            });			
				}
            });
		document.getElementById("tr002").style.display = "block";
}
</script>
</body>
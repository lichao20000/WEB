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
    function init(){
    	
      var access_type_span=document.getElementById("access_type_span");
     
      document.getElementsByName("access_type")[0].value=access_type_span.innerHTML;
        if(access_type_span.innerHTML == ""){
         document.getElementsByName("access_type")[0].value=1;
         }
      

       var access_type_span=document.getElementById("device_type_span");
       document.getElementsByName("device_type")[0].value=access_type_span.innerHTML;

        if(access_type_span.innerHTML == "" ){
    	   document.getElementsByName("device_type")[0].value=1;
       }

       var access_type_span=document.getElementById("is_card_apart_span");
       document.getElementsByName("is_card_apart")[0].value=access_type_span.innerHTML;
       if(access_type_span.innerHTML == "" ){
          
         document.getElementsByName("is_card_apart")[0].value=0;
       }

        var access_type_span=document.getElementById("wireless_type_span");
       document.getElementsByName("wireless_type")[0].value=access_type_span.innerHTML;
       if(access_type_span.innerHTML == ""){
    	   document.getElementsByName("wireless_type")[0].value=1;

       }

        var access_type_span=document.getElementById("voip_protocol_span");
       document.getElementsByName("voip_protocol")[0].value=access_type_span.innerHTML;
   if (access_type_span.innerHTML == ""){
	   document.getElementsByName("voip_protocol")[0].value=1;
	 
	   document.getElementById("wan_num").value=0;
	   document.getElementById("lan_num").value=0;
	   document.getElementById("voip_num").value=0;
	   document.getElementById("wlan_num").value=0;
	   document.getElementById("wireless_num").value=0;
	   document.getElementById("wireless_size").value=0;
   }
    







 

      
    }
	

</SCRIPT>
<%@ include file="/toolbar.jsp"%>
<%@ include file="/itms/resource/DeviceType_Info_util.jsp"%>
<body onload="init();">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM id="addForm" name="addForm" method="post" action="" target="">
		<!-- 添加和编辑part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center"
			id="addTable">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

					<tr>
						<th colspan="4" id="gwShare_thTitle">设备版本配置查询</th>
					</tr>
					<!-- id="gwShare_tr21" -->
					<tr bgcolor="#FFFFFF" id="gwShare_tr21">
						<td align="right" class=column width="15%">上行方式</td>
						<td align="left" width="35%"><span id="span"
							style="display: none"><s:property
							value="configMap.access_type" /></span> <select name="access_type"
							class="bk">
							<option value="1">DSL</option>
							<option value="2">Ethernet</option>
							<option value="3">EPON</option>
							<option value="4">GPON</option>
						</select> <span id="access_type_span" style="display: none"><s:property
							value="configMap.access_type" /></span></td>
						<td align="right" class=column width="15%">设备类型</td>
						<td align="left" width="35%"><select name="device_type"
							class="bk">
							<option value="1">标准型e8-B网关</option>
							<option value="2">普及型e8-B网关</option>
							<option value="3">标准型e8-C网关</option>
							<option value="4">AP外置型e8-C网关</option>
							<option value="5">其他ITMS管理的终端类型</option>
						</select> <span id='device_type_span' style="display: none"><s:property
							value="configMap.device_type" /></span></td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">wan口名称</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="wan_name" maxlength=20 class=bk size=20
							value="<s:property value="configMap.wan_name"/>"> <s:property
							value="wan_name" /> &nbsp;</td>
						<td align="right" class=column width="15%">wan口数量</td>
						<td align="left" width="35%"><INPUT TYPE="text" id="wan_num"
							NAME="wan_num" maxlength=2 class=bk size=2
							value="<s:property value="configMap.wan_num"/>">&nbsp;</td>

					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">wan口处理能力</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="wan_can" maxlength=20 class=bk size=20
							value="<s:property value="configMap.wan_can"/>">&nbsp;</td>
						<td align="right" class=column width="15%">lan口名称</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="lan_name" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_name"/>">&nbsp;</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">lan口数量</td>
						<td align="left" width="35%"><INPUT TYPE="text" id= "lan_num"
							NAME="lan_num" maxlength=2 class=bk size=2
							value="<s:property value="configMap.lan_num"/>">&nbsp;</td>
						<td align="right" class=column width="15%">lan口处理能力</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="lan_can" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
					</tr>

					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">语音口名称</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="voip_name" maxlength=30 class=bk size=20
							value="<s:property value="configMap.voip_name"/>">&nbsp;
						</td>
						<td align="right" class=column width="15%">语音口数量</TD>
						<td align="left" width="35%"><INPUT TYPE="text"  id="voip_num"
							NAME="voip_num" maxlength=2 class=bk size=2
							value="<s:property value="configMap.voip_num"/>">&nbsp;</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">语音处理能力</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="voip_can" maxlength=20 class=bk size=20
							value="<s:property value="configMap.voip_can"/>">&nbsp;</td>
						<td align="right" class=column width="15%">wlan口名称</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="wlan_name" maxlength=20 class=bk size=20
							value="<s:property value="configMap.wlan_name"/>">&nbsp;
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">wlan口数量</td>
						<td align="left" width="35%"><INPUT TYPE="text" id="wlan_num"
							NAME="wlan_num" maxlength=2 class=bk size=2
							value="<s:property value="configMap.wlan_num"/>">&nbsp;</td>
						<td align="right" class=column width="15%">wlan口处理能力</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="wlan_can" maxlength=20 class=bk size=20
							value="<s:property value="configMap.wlan_can"/>">&nbsp;</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">天线根数</td>
						<td align="left" width="35%"><INPUT TYPE="text" id="wireless_num"
							NAME="wireless_num" maxlength=2 class=bk size=2
							value="<s:property value="configMap.wireless_num"/>">&nbsp;
						</td>
						<td align="right" class=column width="15%">天线尺寸</td>
						<td align="left" width="35%"><INPUT TYPE="text" id="wireless_size"
							NAME="wireless_size" maxlength=2 class=bk size=2
							value="<s:property value="configMap.wireless_size"/>">&nbsp;
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21">
						<td align="right" class=column width="15%">是否机卡分离</td>
						<td align="left" width="35%"><select name="is_card_apart"
							class="bk">
							<option value="0">否</option>
							<option value="1">是</option>
						</select> <span id='is_card_apart_span'
							style="display: none"><s:property
							value="configMap.is_card_apart" /></span>
							</td>
						<td align="right" class=column width="15%">天线类型</td>
						<td align="left" width="35%"><select name="wireless_type"
							class="bk">
							<option value="0">内置</option>
							<option value="1">外置</option>
						</select> <span id="wireless_type_span" style="display: none"><s:property
							value="configMap.wireless_type" /></span></td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21">
						<td align="right" class=column width="15%">语音协议</td>
						<td align="left" width="35%"><select name="voip_protocol"
							class="bk">

							<option value="0">IMS SIP\软交换SIP\H.248</option>
							<option value="1">IMS SIP</option>
							<option value="2">软交换</option>
							<option value="3">H.248</option>
						</select> <span id="voip_protocol_span" style="display: none"><s:property
							value="configMap.voip_protocol" /></span></td>
						<td colspan="4" align="right" CLASS=green_foot><input
							type='hidden' id="updateId" value="-1" /> <!--  	<INPUT TYPE="button"
							onclick="javascript:save();" value="保 存 " class=jianbian>--> <INPUT
							TYPE="button" onclick="javascript:abc();" class="jianbian"
							value="保 存"> <INPUT TYPE="hidden" name="action"
							value="add"> <input type="hidden" name="devicetype_id"
							value="<s:property value="devicetype_id"/>"></td>
					</tr>


				</TABLE>
				</TD>
			</TR>

		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=15><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>
</body>
<SCRIPT LANGUAGE="JavaScript">
parent.dyniframesize();

function abc()
{

	trimAll();
    var url = "<s:url value='/gwms/blocTest/DeviceInfoQuery!add.action'/>";
	var access_type = $("select[@name='access_type']").val();
    var device_type = $("select[@name='device_type']").val();
    var is_card_apart = $("select[@name='is_card_apart']").val();
    var wan_name = $("input[@name='wan_name']").val();
    var wan_num = $("input[@name='wan_num']").val();
   
    if(!/^[\d]+$/gi.test(wan_num))
    	{
    	alert("请输入数字");
    	$("input[@name='wan_num']").focus();
    	$("input[@name='wan_num']").select();
    	return false;
		}
    var wan_can = $("input[@name='wan_can']").val();
    var lan_name = $("input[@name='lan_name']").val();
    var lan_num = $("input[@name='lan_num']").val();
      if(!/^[\d]+$/gi.test(lan_num))
  	   {
  	       alert("请输入数字");
    	   $("input[@name='lan_num']").focus();
    	   $("input[@name='lan_num']").select();
    	   return false;
		}
    var lan_can = $("input[@name='lan_can']").val();
    var wlan_name = $("input[@name='wlan_name']").val();
    var wlan_num = $("input[@name='wlan_num']").val();
    if(!/^[\d]+$/gi.test(wlan_num))
    	{
   	     alert("请输入数字");
   	     $("input[@name='wlan_num']").focus();
    	$("input[@name='wlan_num']").select();
    	return false;
		}
    var wlan_can = $("input[@name='wlan_can']").val();
    var voip_name = $("input[@name='voip_name']").val();
    var voip_num = $("input[@name='voip_num']").val();
   
    if(!/^[\d]+$/gi.test(voip_num))
    	{
    	alert("请输入数字");
    	$("input[@name='voip_num']").focus();
        $("input[@name='voip_num']").select();
        return false;
		}
    var voip_can = $("input[@name='voip_can']").val();
    var voip_protocol = $("select[@name='voip_protocol']").val();
    var wireless_type = $("select[@name='wireless_type']").val();
    var wireless_num = $("input[@name='wireless_num']").val();
 
      if(!/^[\d]+$/gi.test(wireless_num))
     	{
    	alert("请输入数字");
    	$("input[@name='wireless_num']").focus();
        $("input[@name='wireless_num']").select();
         return false;
		}
    var wireless_size = $("input[@name='wireless_size']").val();
   
    if(!/^[\d]+$/gi.test(wireless_size))
   	{
   	    alert("请输入数字");
   	    $("input[@name='wireless_size']").focus();
    	$("input[@name='wireless_size']").select();
	}
	var devicetype_id = $("input[@name='devicetype_id']").val();
  
    $.post(url,{
	    	access_type:access_type,
			device_type:device_type,
			is_card_apart:is_card_apart,
			wan_name:wan_name,
			wan_num:wan_num,
			wan_can:encodeURIComponent(wan_can),
			lan_name:lan_name,
			lan_num:lan_num,
			lan_can:encodeURIComponent(lan_can),
			wlan_name:wlan_name,
			wlan_num:wlan_num,
			wlan_can:encodeURIComponent(wlan_can),
			voip_name:voip_name,
			voip_num:voip_num,
			voip_can:encodeURIComponent(voip_can),
			voip_protocol:voip_protocol,
			wireless_type:wireless_type,
			wireless_num:wireless_num,
			wireless_size:wireless_size,
			devicetype_id:devicetype_id
			},
		function(ajax){
	
	
		
		if(ajax.indexOf("1")!= -1 )
		{
			// 普通方式提交
		     alert("保存成功");
			//var form =window.parent.document.getElementById("mainForm");
			//form.action = "<s:url value="/gwms/blocTest/DeviceInfoQuery!query.action"/>";
			//alert(form.action);
			//form.submit();

		    var form = window.parent.document.getElementById("mainForm");
		 	form.action = "<s:url value="/gwms/blocTest/DeviceInfoQuery!queryConfig.action"/>";
		 	window.parent.document.getElementById("devicetype_id").value=devicetype_id;
		    form.target ="editForm";
		 	form.submit();
		}

		else{
			 alert("失败");

			}

});
}


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
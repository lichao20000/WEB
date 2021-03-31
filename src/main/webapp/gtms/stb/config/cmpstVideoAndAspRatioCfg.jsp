
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var deviceId = '<s:property value="deviceId"/>';

	init();
	function init(){
		
		$("input[@name='aspectRatio'][@checked]").val("");
		$("input[@name='compositeVideo'][@checked]").val("");
		
		
		$("input[@type='radio']").each(function(){
			$(this).attr("disabled",true);
		});
		
		/**var aspectRatio = '<s:property value="aspectRatio"/>';
		var compositeVideo = '<s:property value="compositeVideo"/>';
		if(aspectRatio=="16:9"){
			$("input[@name='aspectRatio'][@value=16:9]").attr("checked",true);
		}else if(aspectRatio=="4:3"){
			$("input[@name='aspectRatio'][@value=4:3]").attr("checked",true);
		}else{
		}
		if(compositeVideo=="PAL"){
			$("input[@name='compositeVideo'][@value=PAL]").attr("checked",true);
		}else if(compositeVideo=="NTSC"){
			$("input[@name='compositeVideo'][@value=NTSC]").attr("checked",true);
		}else if(compositeVideo=="720P"){
			$("input[@name='compositeVideo'][@value=720P]").attr("checked",true);
		}else if(compositeVideo=="1080I"){
			$("input[@name='compositeVideo'][@value=1080I]").attr("checked",true);
		}else{
		}**/
	} 
	function superGather() {
		$('#superGather').attr("disabled",true);
		var url = "<s:url value='/gtms/stb/config/baseConfig!cmpstVideoAndAspRatioGather.action'/>";
		$.post(url, {
			deviceId : deviceId
		}, function(ajax) {
			$('#superGather').attr("disabled",false);
			var relts = ajax.split("#");
			if(relts[0]=="-1"){
				alert(relts[1]);
			}else{
				if(relts[2]=="16:9"){
					$("input[@name='aspectRatio'][@value=16:9]").attr("checked",true);
				}else if(relts[2]=="4:3"){
					$("input[@name='aspectRatio'][@value=4:3]").attr("checked",true);
				}else if(relts[2]=="default"){
					$("input[@name='aspectRatio'][@value=default]").attr("checked",true);
				}
				if(relts[1]=="PAL"){
					$("input[@name='compositeVideo'][@value=PAL]").attr("checked",true);
				}else if(relts[1]=="NTSC"){
					$("input[@name='compositeVideo'][@value=NTSC]").attr("checked",true);
				}else if(relts[1]=="720P"){
					$("input[@name='compositeVideo'][@value=720P]").attr("checked",true);
				}else if(relts[1]=="1080I"){
					$("input[@name='compositeVideo'][@value=1080I]").attr("checked",true);
				}else if(relts[1]=="1080P"){
					$("input[@name='compositeVideo'][@value=1080P]").attr("checked",true);
				}
			}
		});
	}
	function config() {
		$('#superGather').attr("disabled",true);
		if (checkForm() == false) {
			return false;
		} else {
		//	var compositeVideo = $("input[@name='compositeVideo'][@checked=true]").val();
		//	var aspectRatio = $("input[@name='aspectRatio'][@checked=true]").val();
		
		var SpModel = $("input[@name='pxModel'][@checked]").val();
		var SpzsModel = $("input[@name='spzsModel'][@checked]").val();
		
		if("1"!=SpModel){
			SpModel = "0";
		}
		
		if("1"!=SpzsModel){
			SpzsModel = "0";
		}
		
		var compositeVideo;
		var	compositeVideo_objs = document.getElementsByName("compositeVideo");
		for(var i=0; i<compositeVideo_objs.length; i++)
		{
			if(compositeVideo_objs[i].checked == true)
			{
				compositeVideo = compositeVideo_objs[i].value;
			}
		}
		
		var aspectRatio;
		var aspectRatio_objs = document.getElementsByName("aspectRatio");
		for(var i=0; i<aspectRatio_objs.length; i++)
		{
			if(aspectRatio_objs[i].checked == true)
			{
				aspectRatio = aspectRatio_objs[i].value;
			}
		}
			var url = "<s:url value='/gtms/stb/config/baseConfig!configCmpstVideoAndAspRatio.action'/>";
			$.post(url, {
				deviceId : deviceId,
				compositeVideo : compositeVideo,
				aspectRatio : aspectRatio,
				spModel:SpModel,
				spzsModel:SpzsModel
			}, function(ajax) {
				$('#superGather').attr("disabled",false);
				alert(ajax);
			});
		}
	}
	function checkForm() {
     
		var AspectRatio = $("input[@name='aspectRatio'][@checked]").val();
		var CompositeVideo = $("input[@name='compositeVideo'][@checked]").val();
		
		var SpModel = $("input[@name='pxModel'][@checked]").val();
		var SpzsModel = $("input[@name='spzsModel'][@checked]").val();
		
		if(SpModel == "pxModel"){
			if (AspectRatio == "" || AspectRatio == undefined) {
				alert("请选择屏显模式");
				return false;
			}
		}
		
		if(SpzsModel == "spzsModel"){
			if (CompositeVideo == "" || CompositeVideo == undefined) {
				alert("请选择视频输出制式");
				return false;
			}
		}
		
		return true;
	}
	
	function isgo(msg){
		if(1 == msg){
			if($("input[@name='pxModel']").attr("checked")==true){
				$("input[@name='aspectRatio']").attr("disabled",false);
			}else{
				$("input[@name='aspectRatio']").attr("disabled",true);
				$("input[@name='aspectRatio'][@checked]").val("");
			}
		}
		
		if(2 == msg){
			if($("input[@name='spzsModel']").attr("checked")==true){
				$("input[@name='compositeVideo']").attr("disabled",false);
			}else{
				$("input[@name='compositeVideo']").attr("disabled",true);
				$("input[@name='compositeVideo'][@checked]").val("");
			}
		}
	}
</SCRIPT>
<TABLE class="querytable" width="100%">
	<tr>
		<Td colspan="4" class="title_1">机顶盒视频输出制式和屏显模式配置信息</Td>
	</tr>
	<TR>
		<TD class="title_2">屏显模式 &nbsp;&nbsp;&nbsp;<input type="checkbox" name="pxModel" value="1" onchange="isgo(1)">  </TD>
		<TD colspan=3><input type="radio" name="aspectRatio" value="16:9">
		16:9&nbsp;&nbsp; <input type="radio" name="aspectRatio" value="4:3">
		4:3&nbsp;&nbsp; <input type="radio" name="aspectRatio" value="default">
		default</TD>
	</TR>
	<TR>
		<TD class="title_2" width="15%">视频输出制式&nbsp;&nbsp;<input type="checkbox" name="spzsModel" value="1" onchange="isgo(2)"></TD>
		<TD colspan=3><input type="radio" name="compositeVideo"
			value="PAL"> PAL&nbsp;&nbsp; <input type="radio"
			name="compositeVideo" value="NTSC"> NTSC&nbsp;&nbsp; <input
			type="radio" name="compositeVideo" value="720P">
		720P(高清)&nbsp;&nbsp; <input type="radio" name="compositeVideo"
			value="1080I"> 1080I(高清)&nbsp;&nbsp; <input type="radio" name="compositeVideo"
			value="1080P"> 1080P(高清)<font color="red">(机顶盒暂不支持配置1080P)</font>
			</TD>

	</TR>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<button  id="superGather" onclick="superGather()">采集</button>
		<button id="config" onclick="config()">配置</button>
		</div>
		</td>
	</tr>
	<TR style="display: none" id="tr_config">
		<td colspan="4" valign="top" class=column>
		<div id="div_config" style="width: 100%; z-index: 1; top: 100px">
		</div>
		</td>
	</TR>
</TABLE>


<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
	request.setCharacterEncoding("GBK");
  String gw_type = request.getParameter("gw_type"); 
  String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<script type="text/javascript" src="../Js/jquery.js"/></script>

<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%=gw_type%>" ;
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

});

function checkForm(){
	
	var excute_type_radios = document.all("excute_typ");
    var excute_type = "";
    var s;
    for(var i=0;i<excute_type_radios.length;i++)
    {
      if(excute_type_radios[i].checked)
	  {
	    excute_type = excute_type_radios[i].value;
	    break;
	  }
    }
    
	var enable = $("input[@name='enable'][@checked]").val();
	var timelist = $("select[@name='timelist']").val();

	var obj = document.frm.paralist_a;
    
	s="";
	for(var i=0;i<obj.length;i++){
		if(obj[i].checked){
			if(s=="") s = obj[i].value;
			else s+=","+obj[i].value;
		}
	}
	$("input[@name='paralist']").val(s);
	var deviceSN = $("input[@name='deviceSN']").val();
	<%
		if("xj_dx".equals(InstArea)){
			%>
			var serverurl = $("select[@name='serverurl']").val();
			<%
		}else{
			%>
			var serverurl = $("input[@name='serverurl']").val();
			<%
		}
	%>
	
	var tftp_port = $("input[@name='tftp_port']").val();
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	
	
	if(excute_type==0&&deviceSN=="")
	{
		alert("请选择设备！");
		return false;
	}
	
	if(excute_type==1&&gwShare_fileName=="")
	{
		alert("请导入设备！");
		return false;
	}
	
	if(excute_type==2){
		var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
		var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
		var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
		var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
		if(parseInt(gwShare_cityId) == -1){
			alert("请选择属地！");
			return false;
		}
		if(parseInt(gwShare_vendorId) == -1){
			alert("请选择厂商！");
			return false;
		}
		if(parseInt(gwShare_deviceModelId) == -1){
			alert("请选择设备型号！");
			return false;
		}
	}
	
	if(tftp_port=="" || $.trim(tftp_port)==0){
		alert("请填写端口！");
		return false;
	}
	
	if(serverurl=="" || $.trim(serverurl)==0){
		alert("请填写文件上传路径！");
		return false;
	}
	
	if(enable==1&&s=="")
	{
		alert("请选择参数！");
		return false;
	}
	
	
	$("input[@name='excute_type']").attr("value",excute_type);
		
    $("tr[@id='trData']").show();
    $("#btn").attr("disabled",true);
    $("div[@id='QueryData']").html("正在努力为您统计，请稍等....");
    var form = document.getElementById("frm");
	form.action ="statusMsgUpload.action"; 
	form.submit();
    form.action ="statusMesUpload!batchUp.action"; 

}

function displayType(type)
{
	document.getElementById("dataForm").contentWindow.document.body.innerText = "";
	if (type == 1)
	{
		$("tr[@id='device']").css("display","none");
		$("tr[@id='gwShare_tr21']").css("display","none");
		$("tr[@id='gwShare_tr22']").css("display","none");
		$("tr[@id='gwShare_tr23']").css("display","none");
		$("tr[@id='gwShare_tr24']").css("display","none");
		$("tr[@id='upload']").css("display","");
		$("tr[@id='gwShare_tr32']").css("display","");
		$("input[name=enable]:eq(1)").attr("disabled","");

		
	}else if(type == 2)
		{
		gwShare_change_select("city","-1");
		gwShare_change_select("vendor","-1");
		$("tr[@id='gwShare_tr21']").css("display","");
		$("tr[@id='gwShare_tr22']").css("display","");
		$("tr[@id='gwShare_tr23']").css("display","");
		$("tr[@id='gwShare_tr24']").css("display","");
		$("tr[@id='device']").css("display","none");
		$("tr[@id='upload']").css("display","none");
		$("tr[@id='gwShare_tr32']").css("display","none");
		$("input[name=enable]:eq(1)").attr("disabled","disabled");
		$("input[name=enable]:eq(0)").attr("checked","checked");
		}
	else{
		$("tr[@id='device']").css("display","");
		$("tr[@id='upload']").css("display","none");
		$("tr[@id='gwShare_tr32']").css("display","none");	
		$("tr[@id='gwShare_tr21']").css("display","none");
		$("tr[@id='gwShare_tr22']").css("display","none");
		$("tr[@id='gwShare_tr23']").css("display","none");
		$("tr[@id='gwShare_tr24']").css("display","none");
		$("input[name=enable]:eq(1)").attr("disabled","");
	}
		
}

function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "../inmp/bss/gwDeviceQuery!getCityNextChild.action";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "../inmp/bss/gwDeviceQuery!getVendor.action";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备版本==</option>");
			});
			break;
		case "deviceModel":
			var url = "../inmp/bss/gwDeviceQuery!getDeviceModel.action";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备版本==</option>");
				break;
			}
			
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备版本==</option>");
			});
			break;
		case "devicetype":
			var url = "../inmp/bss/gwDeviceQuery!getDevicetype.action";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			var gwShare_isBatch = "is_check";
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备版本==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId,
				isBatch:gwShare_isBatch
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("未知查询选项！");
			break;
	}	
}

/*------------------------------------------------------------------------------
//函数名:		deviceSelect_parseMessage
//参数  :	ajax 
          	类似于XXX$XXX#XXX$XXX
          field
          	需要加载的jquery对象
//功能  :	解析ajax返回参数
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//解析查询设备型号返回值的方法
function gwShare_parseMessage(type,ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		//return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	if("city" == type)
	{
		option = "<option value='-1' selected>==全部==</option>";
	}
	else
	{
		option = "<option value='-1' selected>==请选择==</option>";
	}
	
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			if(option.indexOf('undefined')==-1){
				field.append(option);
			}
		}catch(e){
			alert("设备型号检索失败！");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}


</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<form name="frm" action="statusMesUpload!batchUp.action" onsubmit="return checkForm()" target="dataForm">	
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
			<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
				<tr>
					<td width="162">
						<div align="center" class="title_bigwhite">状态信息上报功能开启和关闭定制</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<TR>
		<TD>
			<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
				<TR >
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=0 width="100%" id="outTable">
							<TR>
								<TH bgcolor="#ffffff" colspan="4" align="center">状态信息上报功能开启和关闭定制</TH>
								<INPUT TYPE="text" name="s" class="bk" STYLE="display:none">
							</TR>
							<TR bgcolor="#FFFFFF" >
								<TD width="30%" align="left" colspan=5>
									<input type="radio" name="excute_typ" value="0" onclick="displayType(this.value)" checked>
									单台设备定制
									<input type="radio" name="excute_typ" value="1" onclick="displayType(this.value)">
									导入文件定制
									<input type="radio" name="excute_typ" value="2" onclick="displayType(this.value)">
									高级查询
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" width="4%" id="device">
								<TD class=column align="right" width="15%" nowrap>设备序列号</TD>
								<TD width="85%" colspan=3>
									<INPUT TYPE="text" name="deviceSN" class="bk">&nbsp;
								</TD>
							</TR>
					
							<tr bgcolor="#FFFFFF" id="upload" STYLE="display:none">
								<td align="right" width="15%">提交文件</td>
								<td colspan="3" width="85%">
									<div id="importUsername">
										<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="../gwms/share/FileUpload.jsp" height="20" width="100%"></iframe>
										<input type="hidden" name=gwShare_fileName value=""/>
										<input type="hidden" name=paralist value=""/>
										<input type="hidden" name=excute_type value=""/>
									</div>
								</td>
							</tr>
							<tr id="gwShare_tr32" style="display:none">
								<td CLASS="green_foot" align="right">注意事项</td>
								<td colspan="3" CLASS="green_foot">
									1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。
									 <br>
									2、文件的第一行为标题行，即【设备序列号】。
									 <br>
									3、文件只有一列。
									 <br>
									4、文件行数不要太多，以免影响性能。
								</td>
							</tr>
							
							
				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="display:none">
					<TD align="right" class=column width="15%">属    地</TD>
					<TD align="left" width="35%">
						<select name="gwShare_cityId" class="bk">
							<option value="-1">==全部==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">上线状态</TD>
					<TD width="35%">
						<select name="gwShare_onlineStatus" class="bk">
							<option value="-1">==全部==</option>
							<option value="0">下线</option>
							<option value="1">在线</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr22" STYLE="display:none">
					<TD align="right" class=column width="15%">厂    商</TD>
					<TD width="35%">
						<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
							<option value="-1">==请选择==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">设备型号</TD>
					<TD align="left" width="35%">
						<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
							<option value="-1">请先选择厂商</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr23"  STYLE="display:none">	
					<TD align="right" class=column width="15%">设备版本</TD>
					<TD width="35%">
						<select name="gwShare_devicetypeId" class="bk"">
							<option value="-1">请先选择设备型号</option>
						</select>
					</TD>	
					<TD align="right" class=column width="15%">是否绑定</TD>
					<TD width="35%">
						<select name="gwShare_bindType" class="bk">
							<option value="-1">==全部==</option>
							<option value="0">未绑定</option>
							<option value="1">已绑定</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr24"  STYLE="display:none">
					<TD align="right" class=column width="15%">设备序列号</TD>
					<TD width="35%">
						<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="25" class="bk"/>
						<font color="red">*可模糊匹配</font>
					</TD>
					<TD align="right" class=column width="15%" ><font id="gwShare_td1"  STYLE="display:none">自定义查询</font></TD>
					<TD width="35%">
						<input type="checkbox" name="chbx_isMactchSQL" id="chbx_isMactchSQL"  STYLE="display:none" onclick="spellSQL(this);">
					</TD>
				</TR>


				
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="10%" nowrap>上报周期</TD>
									<TD width="40%" colspan="3" ><select name="timelist" class="bk" style='width:150px'>
										<option value="15">15</option>
										<option value="20">20</option>
										<option value="25">25</option>
										<option value="30">30</option>
										<option value="35">35</option>
										<option value="40">40</option>
										<option value="45">45</option>
										<option value="50">50</option>
										<option value="55">55</option>
										<option value="60">60</option>
									</select>
									</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="10%" nowrap>端口</TD>
								<TD width="30%">
									<%
									if("xj_dx".equals(InstArea)){
										%>
										<INPUT TYPE="text" name="tftp_port" class="bk" value="6169">&nbsp;
										<%
									}else{
										%>
										<INPUT TYPE="text" name="tftp_port" class="bk">&nbsp;
										<%
									}
									%>
									
								</TD>
								<TD class=column align="right" width="10%" nowrap>文件上传路径</TD>
								<TD width="40%">
									<%
									if("xj_dx".equals(InstArea)){
										%>
										<select name="serverurl">
										<option value="10.0.1.6" selected="selected">10.0.1.6</option>
										<option value="10.0.1.7">10.0.1.7</option>
										<option value="10.0.1.8">10.0.1.8</option>
										</select>
										<%
									}else{
										%>
										<INPUT TYPE="text" name="serverurl" class="bk">&nbsp;
										<%
									}
									%>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="10%" nowrap>是否开启</TD>
									<TD width="90%" colspan=3>
										<input type="radio" name="enable" value="1" checked>开启
										<input type="radio" name="enable" value="0">关闭
									</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="10%" nowrap>参数列表</TD>
								 <TD width="90%" colspan=3>
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="1">物理状态&nbsp; 
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="2">语音业务注册状态&nbsp; 
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="3">语音业务注册失败原因&nbsp; 
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="4">PPP拨号上网连接状态&nbsp;
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="5">拨号错误代码&nbsp;
									 <ms:inArea areaCode="jx_dx" notInMode="false">
										<INPUT TYPE="checkbox" NAME="paralist_a" value="6">LAN口状态&nbsp;
										<INPUT TYPE="checkbox" NAME="paralist_a" value="7">PON口状态&nbsp;
									 </ms:inArea>
									 <ms:inArea areaCode="nmg_dx" notInMode="false">
										<INPUT TYPE="checkbox" NAME="paralist_a" value="8">性能状态&nbsp;
									 </ms:inArea>
								 </TD>
							 </TR>
							<TR class="green_foot">
								<TD  align="right" height="23" colspan=4>
									<input type=button name="queryBtn" value="定制 " onclick="checkForm();">
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
		
				<tr id="trData" style="display: none" >
					<td>
					<TABLE border=0 cellspacing=1 cellpadding=0  >
						<tr>
						<td>
							<div id="QueryData" style="top: 150px">
								正在努力为您查询，请稍等....
							</div>
						</td>
						</tr>
					</TABLE>
					</td>
				</tr>
			</TABLE>
		</TD>
	</TR>
</TABLE>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr><td>
			<div class="it_table" >
				<iframe id="dataForm" align="center" name="dataForm" height="0" frameborder="0" scrolling="no" width="98%" src=""></iframe>
			</div>
			</td></tr>
			</TABLE>
</form>


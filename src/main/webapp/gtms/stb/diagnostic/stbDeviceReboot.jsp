<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">

	var gw_type = "<%=request.getParameter("gw_type")%>";

	$(function(){
		gwShare_setGaoji();
		
	});
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		}
	}
	/*------------------------------------------------------------------------------
	//函数名:		queryChange
	//参数  :	change 1:简单查询、2:高级查询
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value="/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action"/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			$("input[@name='deviceId']").val(returnVal[0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
  	function ExecMod(){ 
  		if(CheckForm()){
  			$('#reStart_btn').attr("disabled",true);
  			$("div[@id='div_ping']").html("正在重启，请稍后...");
			var url = "<s:url value='/gtms/stb/diagnostic/systemCmd!reboot.action'/>";
			var deviceId = $("input[@name='deviceId']").val();
			$.post(url,{
				deviceId:deviceId,
				gw_type:gw_type
			},function(ajax){
			    $("div[@id='div_ping']").html("");
				$("div[@id='div_ping']").append(ajax);
				$('#reStart_btn').attr("disabled",false);
			});	
		}else{
			return false;
		}	
    }

	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("请查询设备！");
			return false;
		}
		return true;					
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：设备重启
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td>
			<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION=""
				onSubmit="return CheckForm();">
				<table width="100%" class="querytable">
					<input type="hidden" value="" name="deviceId" />
					<TR id="trDeviceResult" style="display: none">
						<td class=title_2 width="25%">
							设备序列号
						</td>
						<td id="tdDeviceSn" width="25%">
						</td>
						<td class=title_2 width="25%">
							属地
						</td>
						<td id="tdDeviceCityName" width="25%">
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" class=foot>
							<div align="right">
								<button id="reStart_btn" onclick="ExecMod()"> 重 启 </button>
							</div>
						</td>
					</tr>
					<TR>
						<TH colspan="4">
							重启操作结果
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td colspan="4" valign="top" class=column>
							<div id="div_ping"
								style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
						</td>
					</TR>
				</table>
			</FORM>
		</TD>
	</TR>
</TABLE>


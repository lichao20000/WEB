<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

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
	//函数名:		queryDevice
	//参数  :	change 1:简单查询、2:高级查询
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var queryType =  $("input[@name='queryType']").val();
		var queryParam =  $("input[@name='queryParam']").val();
		var queryField =  $("input[@name='queryField']").val();
		alert(queryField);
		var url="<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceList.action'/>?queryResultType=radio&queryType="+queryType+"&queryParam="+queryParam+"&queryField="+queryField;
		
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
	
	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("请查询设备！");
			return false;
	    }
		return true;
	}

	function getPara() {
		if(!CheckForm()){
			return;
		}
		if(document.frm.paraV.value==''){
			alert("参数不能为空");
			document.frm.paraV.focus();
			document.frm.paraV.select();
			return false;
		}
		$("tr[@id='trTableView']").css("display","");
		$("div[@id='tableView']").html("<BR><BR>正在获取,请稍候...");
		var url = "<s:url value='/gtms/stb/config/paraModeValue!getOneValue.action'/>";;
		$.post(url,{
			deviceId:document.frm.deviceId.value,
			paraV:document.frm.paraV.value
		},function(ajax){
		    $("div[@id='tableView']").html("");
			$("div[@id='tableView']").append(ajax);
		});
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：参数值获取
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

	<tr>
		<td>
			<FORM NAME="frm" METHOD="post" ACTION=""
				onsubmit="return CheckForm()">
				<table width="100%" class="querytable">
					<input type="hidden" value="" name="deviceId" />
					<TR>
						<TH colspan="4" class=title_1>
							配置选项
						</TH>
					</TR>
					<TR id="trDeviceResult" style="display: none">
						<td class=title_2 width="25%">
							设备序列号:
						</td>
						<td id="tdDeviceSn" width="25%">
						</td>
						<td class=title_2 width="25%">
							属地:
						</td>
						<td id="tdDeviceCityName" width="25%">
						</td>
					</tr>

					<TR bgcolor="#FFFFFF">
						<TD class=title_2 width="25%" nowrap>
							参数
						</TD>
						<TD width="85%" colspan="3">
							<INPUT TYPE="text" NAME="paraV" size="80" maxlength="200"
								class="bk" />
							&nbsp;
							<font color="#FF0000">*</font>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD colspan="4" align="right" class=foot>
							<div class="right">
								<button onclick="getPara()" name="getParam">
									获 取
								</button>
							</div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="trTableView" style="display: none">
						<TD colspan="4">
							<DIV id="tableView" style="overflow: auto; display: ;"
								align="center"></DIV>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</td>
	</tr>
</table>

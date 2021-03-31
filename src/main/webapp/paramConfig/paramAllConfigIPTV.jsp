<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	String gwType = request.getParameter("gw_type");

	//read_flag 判断是否为查询或者操作菜单
	//1代表查询菜单， 2代表操作菜单
	String read_flag = request.getParameter("read_flag");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function CheckForm(){
 	var __device_id = $("input[@name='device_id']").val();

	if(__device_id == null || __device_id == ""){
		alert("请先查询设备!");
		return false;
	}
	return true;
}

function deviceResult(returnVal){
		
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("#resultDIV").html("");
	document.all("result").style.display="none";
	var deviceId = $("input[@name='device_id']").val();
	getParameValues(deviceId);
}


function getParameValues(deviceId){
	
	var gwType =<%=gwType%>;
	document.all("message").style.display="";
	var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!getPreResult.action'/>";
		$.post(url,{
			   deviceIds:deviceId,
               gw_type:gwType
           },function(ajax){
        	   if(null != ajax && "" != ajax)
        	   {
        		   document.all("message").style.display="none";
        		   document.all("result").style.display="";
        		   var result = ajax.split(";");
            	   $("input[@name='paramNodePath1']").val(result[0]);
            	   $("input[@name='paramValue1']").val(result[1]);
            	   $("input[@name='paramType1']").val(result[2]);
            	   $("input[@name='paramNodePath2']").val(result[3]);
            	   $("input[@name='paramValue2']").val(result[4]);
            	   $("input[@name='paramType2']").val(result[5]);
        	   }
        	   
            });
	}

function doExecute(){
	
	 var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!doConfigAll.action'/>"; 
	 // $("#doButton").attr("disabled",true);
	 var gwType =<%=gwType%>;
	 var paramNodePath = ',' + $("input[@name='paramNodePath1']").val() + ',' + $("input[@name='paramNodePath2']").val();
	 var paramValue = ',' + $("input[@name='paramValue1']").val() + ',' + $("input[@name='paramValue2']").val();
	 var paramType = ',' + $("input[@name='paramType1']").val() + ',' + $("input[@name='paramType2']").val();
	 var deviceIds = $("input[@name='device_id']").val();
	 if(deviceIds == null || deviceIds == ""){
		alert("请先查询设备!");
		return ;
	 }
	 $.post(
	         url,{
	         deviceIds : deviceIds,
	         paramNodePath : paramNodePath,
	         paramValue : paramValue,
	         paramType : paramType,
	         gw_type : gwType
	  } ,function(ajax){
	          $("#resultDIV").html("");
	          //$("#exeButton").attr("disabled",false);
	          if("1"==ajax){
	               $("#resultDIV").append("后台执行成功");
	          }else{
	               $("#resultDIV").append("后台执行失败");
	          }
	      });
	}
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									组播VLAN参数实例管理</td>
								<td nowrap><img src="../images/attention_2.gif" width="15"
									height="12"> 对选择的设备进行参数实例配置。</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4"><%@ include
							file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" 
						   ACTION="<s:url value='/gtms/config/paramNodeBatchConfigAction!doConfigAll.action'/>"
							onsubmit="return CheckForm()">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center" style="display:none" id="result">
								<tr>
								<input type="hidden" name="device_id" value="">
								</tr>
								<TD bgcolor=#999999>
								<table border=0 cellspacing=1 cellpadding=2 width="100%" class="querytable">
								<TR bgcolor="#FFFFFF">
									<td nowrap align="right" class=column width="20%">
										参数节点路径
									</td>
									<td width="70%" colspan="3" >
										<input type="text" id="paramNodePath1" name="paramNodePath1"  style="width:800px;" 
											value=""  readonly="readonly" > 
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td align="right" class=column width="20%">参数值</td>
									<td width="30%" >
										<input type="text" id="paramValue1" name="paramValue1" value=""/>
									</td>
								</TR>
								<input type="text" id="paramType1" name="paramType1" style="display:none" value="" />
								<!-- <input type="hidden" id="paramType1" name="paramType1" value="" /> -->
								<TR bgcolor="#FFFFFF">
									<td nowrap align="right" class=column width="20%">
										参数节点路径
									</td>
									<td width="70%" colspan="3" >
										<input type="text" id="paramNodePath2" name="paramNodePath2"  style="width:800px;" 
											value=""  readonly="readonly"  > 
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td align="right" class=column width="20%">参数值</td>
									<td width="30%" >
										<input type="text" id="paramValue2" name="paramValue2" value=""/>
									</td>
								</TR>
								<input type="text" id="paramType2" name="paramType2" style="display:none" value="" />
								<!-- <input type="hidden" id="paramType2" name="paramType2" value="" /> -->
								<TR bgcolor="#FFFFFF">
									<td colspan="4" align="right">
									<button type="button" id="exeButton" name="exeButton"
										onclick="doExecute();" class=btn>
										&nbsp;执&nbsp;行 &nbsp;
									</button>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="left" class="green_foot">
										<div id="resultDIV"></div>
								    </TD>
								</TR>
								</table>
								</TD>
							</TABLE>
							<TR bgcolor="#FFFFFF">
								<TD width="100%" style="display:none" id="message">
								正在采集,请等待...
								</TD>
							</TR>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
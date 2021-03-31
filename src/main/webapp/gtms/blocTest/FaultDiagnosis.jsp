<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function CheckForm(){
 	var __device_id = $("input[@name='deviceId']").val();

	if(__device_id == null || __device_id == ""){
		alert("请先查询设备!");
		return false;
	}
	return true;
}

function deviceResult(returnVal){
		
	$("TABLE[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='deviceId']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=new Array()

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function templateChange(){
	iframeids.length=0;
	$("td[@id='diagnoFormList']").html("");
	var diag_id = $("select[@name='tempId']").val();
	var deviceId = $("input[@name='deviceId']").val();
	var gwType = $("input[@name='gw_type']").val();
	//alert(diag_id == -1);
	if(diag_id != -1){
		var getTempUintByTId = '<s:url value="/gtms/diagnostic/faultDiag!getUintById.action"/>';
		$.post(getTempUintByTId,{
			diagId:diag_id
		},function(ajax){
			//alert(ajax);
			var s = ajax.split(",");
			for (var i=0;i<s.length;i++){
				var sArry = s[i].split(";");
				var url2='<s:url value="/'+sArry[1]+'"/>';
				if(url2.indexOf("?")==-1)
				{
					$("td[@id='diagnoFormList']").append("&nbsp;&nbsp;<iframe id='dataForm"+i+"' name='dataForm' frameborder='0' scrolling='auto' width='99%' src='"+url2+"?deviceId="+deviceId+"&gw_type="+gwType+"'></iframe>");
				}else
				{
					$("td[@id='diagnoFormList']").append("&nbsp;&nbsp;<iframe id='dataForm"+i+"' name='dataForm' frameborder='0' scrolling='auto' width='99%' src='"+url2+"&deviceId="+deviceId+"&gw_type="+gwType+"'></iframe>");
				}
				iframeids[i]="dataForm"+i;
			}
			
		});
	}
}

function dyniframesize() {
		var dyniframe=new Array()
		for (i=0; i<iframeids.length; i++){
			if (document.getElementById){
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera){
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
			if ((document.all || document.getElementById) && iframehide=="no"){
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
	    		tempobj.style.display="block"
			}
		}
}
function autoToDoDiagnosis(){
	for(var iframIndex=0; iframIndex<iframeids.length; iframIndex++){
		//var iframeObj = document.getElementById(iframeids[i]);
		var iframeObj = $(window.frames[iframeids[iframIndex]].document);
		if(null != iframeObj){
			$(window.frames[iframeids[iframIndex]].document).find("button:contains('诊断')").click();
		}
	}
	
}

$(window).resize(function(){
		dyniframesize();
	}); 

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									定制故障诊断
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									配置诊断模板根据模板诊断设备。
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" target="dataForm" >
							<input type="hidden" name="deviceId" value="">
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="trDeviceResult" style="display: none">
											<TR bgcolor="#FFFFFF" >
												<td nowrap align="right" class=column width="15%">
													设备属地
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td nowrap align="right" class=column width="15%">
													设备序列号
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>
												<td>
													<input type="hidden" name ="gw_type" value="<%=gwType %>"/>
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD nowrap align="right" class=column width="15%">
													选择模板
												</TD>
												<TD colspan="4" nowrap align="left" class=column width="15%">
													<s:select list="list" name="tempId" headerKey="-1"
														headerValue="请选择模板" listKey="id" listValue="template_name"
														cssClass="bk" onchange="templateChange();"></s:select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="5" align="right" class="green_foot">
													<INPUT TYPE="button" onclick="autoToDoDiagnosis()" value="一键诊断" class=btn>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20 id="diagnoFormList" align="center">
			&nbsp;
			
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
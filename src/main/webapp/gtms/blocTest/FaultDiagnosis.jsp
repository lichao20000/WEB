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
		alert("���Ȳ�ѯ�豸!");
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

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=new Array()

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
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
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera){
	     			dyniframe[i].style.display="block"
	     			//����û����������NetScape
	     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	      			//����û����������IE
	     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			 }
	   		}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
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
			$(window.frames[iframeids[iframIndex]].document).find("button:contains('���')").click();
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
									���ƹ������
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									�������ģ�����ģ������豸��
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
													�豸����
												</td>
												<td id="tdDeviceCityName" width="35%">
												</td>
												<td nowrap align="right" class=column width="15%">
													�豸���к�
												</td>
												<td id="tdDeviceSn" width="35%">
												</td>
												<td>
													<input type="hidden" name ="gw_type" value="<%=gwType %>"/>
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD nowrap align="right" class=column width="15%">
													ѡ��ģ��
												</TD>
												<TD colspan="4" nowrap align="left" class=column width="15%">
													<s:select list="list" name="tempId" headerKey="-1"
														headerValue="��ѡ��ģ��" listKey="id" listValue="template_name"
														cssClass="bk" onchange="templateChange();"></s:select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="5" align="right" class="green_foot">
													<INPUT TYPE="button" onclick="autoToDoDiagnosis()" value="һ�����" class=btn>
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
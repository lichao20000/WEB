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
					alert("���Ȳ�ѯ�豸!");
					return false;
				}
		        page = "jt_device_zendan_from4_save.jsp?device_id=" +_device_id + "&oid_type=2&type=1";
				document.all("div_ping").innerHTML = "����������Ͻ���������ĵȴ�....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
          }
	
	function CheckForm(){
		var _device_id = $("input[@name='textDeviceId']").val();	
		if(_device_id == null || _device_id == ""){
			alert("���Ȳ�ѯ�豸!");
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
		 $("td[@id='select_map']").html("���ڲ�ѯģ��...");
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
	
	
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["myiframe1"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
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
											��̨�ն��·�����
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
										�豸���أ�
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										�豸���кţ�
										<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<td nowrap align="right" class=column width="15%">
										��ͼģ�����ã�
									</td>
									<td colspan=3 id="select_map">
										<font color=red>���Ȳ�ѯ�豸��</font>
									</td>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" value="�·�����" class=btn id="doButton" disabled
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
										�ն��·����ý��
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
		alert("����ѡ���豸��");
		return;
	}
	if(map_id == "")
	{
		alert("����ѡ��ģ�壡");
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
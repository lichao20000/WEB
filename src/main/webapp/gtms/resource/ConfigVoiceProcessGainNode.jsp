<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type" />';

var node_1 = "InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.";
var node_2 = "VoiceProcessing.TransmitGain";  // ��������
var node_3 = "VoiceProcessing.ReceiveGain";   // ��������

var strNode = ""; 

$(function(){
	gwShare_setGaoji();
});
	
function CheckForm(){
	var _device_id = $("input[@name='textDeviceId']").val();	
	if(_device_id == null || _device_id == ""){
		return false;
	}	
	return true;		
}
	
function deviceResult(returnVal){
	
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	$("input[@name='textDeviceId']").val("");
	
	$("button[id='getButton']").attr("disabled", false);
	
	var city_id;
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
		
		city_id = $.trim(returnVal[2][i][4]);
	}
	
	document.getElementById("tr002").style.display = "none";
	
	$("tr[@id='trDeviceResult']").css("display","");
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

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION="" >
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
									�·�����ڵ�
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
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
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<button TYPE="button" class=jianbian id="getButton" onclick="getGainNode()">
													��ȡ����ڵ�</button>
												</td>
											</tr>
										</table>
									</td>
								</tr>
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
	
	<tr><td colspan="4"><br></td></tr>
	
	<TR id="tr01" style="display:none">
		<TD>
			<FORM NAME="frm2" METHOD="POST" ACTION="" onSubmit="return CheckForm2();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text" id="allDatas" >
								<TR>
									<TH colspan="4">
										��������ڵ��·�
									</TH>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>

	<TR id="tr02" style="display:none">
		<TD>
			<table width="95%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td bgcolor="#FFFFFF">
						<table width="100%" border=0 align="center" cellpadding="1"
							cellspacing="1" bgcolor="#999999" class="text" id="allDatas" >
							<TR>
								<TH colspan="4">
									��������ڵ��·�
								</TH>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD>��ȡʱ��ϳ��������ĵȴ�.....</TD>
							</TR>
						</table>
					</td>
				</tr>
			</table>
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

function getGainNode(){

	if(!CheckForm()){
		alert("���Ȳ�ѯ�豸!");
		return false ;
	}
	
	// Ϊ��ֹ�ظ������ť���ڵ��һ�κ󽫰�ť��Ϊ��ɫ(���ɵ�)
	$("button[id='getButton']").attr("disabled", true);

	$("tr[@id='tr01']").css("display","none");
	$("tr[@id='tr02']").css("display","");
	
	
	var port_name = document.getElementsByName("port_name");
	var j = port_name.length;
			  
	if(port_name.length > 0){
		for(var i=0;i<j;i++){
			var tr = port_name[0].parentNode.parentNode;
			var tbody = tr.parentNode;
			tbody.removeChild(tr);   
		}
	}
	
	var lineNode = "";
	var gainOutName = "";  // ��������Name
	var gainInName = "";   // �������� Name
	var configName = "";
	
	var portNumber=1;
	
		
	var htmlStr_1 = "<TR bgcolor=\"#FFFFFF\">"+
				  	"  <TD nowrap align=\"right\" class=column width=\"15%\">";
	var htmlStr_2 = "    ������·&nbsp;";
	var htmlStr_3 = "  </TD>"+
				  	"  <TD width=\"\" colspan=\"3\">";
				  	//"     <INPUT TYPE=\"radio\" NAME=\"config\" value=\"1\" checked>&nbsp;������"+
				  	//"     <INPUT TYPE=\"radio\" NAME=\"config\" value=\"0\">&nbsp;����"+
	var htmlStr_6 = "     <INPUT type=\"hidden\" name=\"port_name\" size=\"20\" class=\"bk\" >"+  //�˴���INPUTԪ����ʵ�����ã�ֻ��������
				  	"  </TD>"+
				  	"</TR>"+
				  	"<TR isShow=\"tr069\"  bgcolor=\"#FFFFFF\">"+
				  	"  <TD nowrap align=\"right\" class=column width=\"15%\">"+
				  	"    �������棺"+
				  	"  </TD>"+
	              	"  <TD width=\"35%\">";
					//"     <input type=\"text\" name=\"gainOut\" value=\"\" class=bk size=20>"+
	var htmlStr_8 = "  </TD>"+
	              	"  <TD nowrap align=\"right\" class=column width=\"15%\">"+
	              	"    �������棺"+
	              	"  </TD>"+
				  	"  <TD width=\"35%\">";
				  	//"     <input type=\"text\" name=\"gainIn\" value=\"\" class=bk size=20>"+
	var htmlStr_10 = "     <INPUT type=\"hidden\" name=\"port_name\" size=\"20\" class=\"bk\" >"+  //�˴���INPUTԪ����ʵ�����ã�ֻ��������
				  	"  </TD>"+
				  	"</TR>";
				  	
	var htmlStr_11 = "<tr bgcolor=\"#FFFFFF\">"+
					 "   <td colspan=\"4\">"+
					 "      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
					 "         <tr align=\"right\" CLASS=\"green_foot\">"+
					 "            <td>"+
					 "               <button TYPE=\"button\" class=jianbian id=\"doButton\" onclick=\"doConfig()\">"+
					 "                 ����ڵ��·�</button>"+
					 "            </td>"+
					 "         </tr>"+
					 "     </table>"+
					 "     <INPUT type=\"hidden\" name=\"port_name\" size=\"20\" class=\"bk\" >"+ //�˴���INPUTԪ����ʵ�����ã�ֻ��������
					 "   </td>"+
					 "</tr>";
	
	var device_id = $("input[@name='textDeviceId']").val();
	
	var url = "<s:url value='/gtms/resource/configGainNode!getGainNode.action'/>";
	
	$.post(url,{
		gw_type:gw_type,
		node_1:node_1,
		node_2:node_2,
		node_3:node_3,
		deviceId:device_id
	},function(ajax){
		var reg = new RegExp("^-1");
		if(reg.test(ajax)){
			$("tr[@id='tr01']").css("display","none");
			$("tr[@id='tr02']").css("display","none");
			alert(ajax.split(",")[1]);
			$("button[id='getButton']").attr("disabled", false);
			return false;
		}else{
		
			$("tr[@id='tr01']").css("display","");
			$("tr[@id='tr02']").css("display","none");
			
			strNode = ajax.substring(0,ajax.length-1).split("|");
			
			for(i=0; i<strNode.length; i++){
			
				lineNode = strNode[i].split(";"); // ��0λ����·��Ϣ����1λ�Ǻ������棬��2λ�Ǻ�������
				
				configName = "config_"+lineNode[0];
				gainOutName = "gainOut_"+lineNode[0];
				gainInName = "gainIn_"+lineNode[0];
				
				var htmlStr_4 = " <INPUT TYPE=\"radio\" NAME=\""+configName+"\" onclick= \"config('"+lineNode[0]+"')\" value=\"1\" checked>&nbsp;������";
				var htmlStr_5 = " <INPUT TYPE=\"radio\" NAME=\""+configName+"\" onclick= \"config('"+lineNode[0]+"')\" value=\"0\">&nbsp;����"
				var htmlStr_7 = " <input type=\"text\" name=\""+gainOutName+"\" value=\"\" disabled=true class=bk size=20>"
				var htmlStr_9 = " <input type=\"text\" name=\""+gainInName+"\" value=\"\" disabled=true class=bk size=20>"
				
				$("#allDatas tr:last-child").after(htmlStr_1 + htmlStr_2 + lineNode[0] + "��" + htmlStr_3 + htmlStr_4 + htmlStr_5 + 
												   htmlStr_6 + htmlStr_7 + htmlStr_8 + htmlStr_9 + htmlStr_10);
				
				document.getElementsByName(gainOutName)[0].value = lineNode[1];  // ��������
				document.getElementsByName(gainInName)[0].value = lineNode[2];   // ��������
				
				portNumber++;
			}
			
			// �������ڵ��·���ť
			$("#allDatas tr:last-child").after(htmlStr_11);
			portNumber++;
			
			// Ĭ�ϲ����ã���������ڵ��·�����ť�ûң����ɵ����
			$("button[id='doButton']").attr("disabled", true);
		}
		// ҳ��չʾ�ɹ��󣬽���ť�ͷ�(�ɵ��)
		$("button[id='getButton']").attr("disabled", false);
	});
}


function config(lineNum){

	var configValue = $("input[@name='config_"+lineNum+"']:checked").val();
	// ����
	if("0" == configValue){
		$("input[@name='gainOut_"+lineNum+"']").attr("disabled",false);
		$("input[@name='gainIn_"+lineNum+"']").attr("disabled",false);
	}
	// ������
	else{
		$("input[@name='gainOut_"+lineNum+"']").attr("disabled",true);
		$("input[@name='gainIn_"+lineNum+"']").attr("disabled",true);
	}
	
	for(i=0; i<strNode.length; i++){
		lineNode = strNode[i].split(";"); // ��0λ����·��Ϣ����1λ�Ǻ������棬��2λ�Ǻ�������
		var configValue = $("input[@name='config_"+lineNode[0]+"']:checked").val();
		
		if("0" == configValue){
			$("button[id='doButton']").attr("disabled", false);
			break; // ������·��ֻҪ��һ��Ϊ���ã�������ڵ��·�����ť���ã���ʱ����ѭ���������ж�������
		}else{
			$("button[id='doButton']").attr("disabled", true);
		}
	}
}


// �·�����ڵ�
function doConfig()
{
	var gainValue = "";
	
	for(i=0; i<strNode.length; i++){
	
		lineNode = strNode[i].split(";"); // ��0λ����·��Ϣ����1λ�Ǻ������棬��2λ�Ǻ�������
		var configValue = $("input[@name='config_"+lineNode[0]+"']:checked").val();
		
		// ����
		if("0" == configValue){
			var gainOut = $("input[@name='gainOut_"+lineNode[0]+"']").val();
			var gainIn = $("input[@name='gainIn_"+lineNode[0]+"']").val();
			
			if("" == gainOut){
				alert("�������治�ܿգ�");
				return false;
			}
			if("" == gainIn){
				alert("�������治��Ϊ�գ�");
				return false;
			}
			
			if("" == gainValue){
				gainValue = node_1+lineNode[0]+"."+node_2+";"+gainOut+"|"+node_1+lineNode[0]+"."+node_3+";"+gainIn ;
			}else{
				gainValue = gainValue+"|"+node_1+lineNode[0]+"."+node_2+";"+gainOut+"|"+node_1+lineNode[0]+"."+node_3+";"+gainIn ;
			}
		}
	}
	
	$("button[id='doButton']").attr("disabled", true);
	
	var device_id = $("input[@name='textDeviceId']").val();
	var url = "<s:url value='/gtms/resource/configGainNode!doConfigGainNode.action'/>";
	
	$.post(url,{
		gw_type:gw_type,
		deviceId:device_id,
		gainValue:gainValue
	},function(ajax){
		alert(ajax);
		$("button[id='doButton']").attr("disabled", false);
	});
}
</script>
</body>
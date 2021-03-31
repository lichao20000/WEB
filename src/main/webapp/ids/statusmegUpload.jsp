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
		alert("��ѡ���豸��");
		return false;
	}
	
	if(excute_type==1&&gwShare_fileName=="")
	{
		alert("�뵼���豸��");
		return false;
	}
	
	if(excute_type==2){
		var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
		var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
		var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
		var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
		if(parseInt(gwShare_cityId) == -1){
			alert("��ѡ�����أ�");
			return false;
		}
		if(parseInt(gwShare_vendorId) == -1){
			alert("��ѡ���̣�");
			return false;
		}
		if(parseInt(gwShare_deviceModelId) == -1){
			alert("��ѡ���豸�ͺţ�");
			return false;
		}
	}
	
	if(tftp_port=="" || $.trim(tftp_port)==0){
		alert("����д�˿ڣ�");
		return false;
	}
	
	if(serverurl=="" || $.trim(serverurl)==0){
		alert("����д�ļ��ϴ�·����");
		return false;
	}
	
	if(enable==1&&s=="")
	{
		alert("��ѡ�������");
		return false;
	}
	
	
	$("input[@name='excute_type']").attr("value",excute_type);
		
    $("tr[@id='trData']").show();
    $("#btn").attr("disabled",true);
    $("div[@id='QueryData']").html("����Ŭ��Ϊ��ͳ�ƣ����Ե�....");
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
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�汾==</option>");
			});
			break;
		case "deviceModel":
			var url = "../inmp/bss/gwDeviceQuery!getDeviceModel.action";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�汾==</option>");
				break;
			}
			
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�汾==</option>");
			});
			break;
		case "devicetype":
			var url = "../inmp/bss/gwDeviceQuery!getDevicetype.action";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			var gwShare_isBatch = "is_check";
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�汾==</option>");
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
			alert("δ֪��ѯѡ�");
			break;
	}	
}

/*------------------------------------------------------------------------------
//������:		deviceSelect_parseMessage
//����  :	ajax 
          	������XXX$XXX#XXX$XXX
          field
          	��Ҫ���ص�jquery����
//����  :	����ajax���ز���
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//������ѯ�豸�ͺŷ���ֵ�ķ���
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
		option = "<option value='-1' selected>==ȫ��==</option>";
	}
	else
	{
		option = "<option value='-1' selected>==��ѡ��==</option>";
	}
	
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			if(option.indexOf('undefined')==-1){
				field.append(option);
			}
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
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
						<div align="center" class="title_bigwhite">״̬��Ϣ�ϱ����ܿ����͹رն���</div>
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
								<TH bgcolor="#ffffff" colspan="4" align="center">״̬��Ϣ�ϱ����ܿ����͹رն���</TH>
								<INPUT TYPE="text" name="s" class="bk" STYLE="display:none">
							</TR>
							<TR bgcolor="#FFFFFF" >
								<TD width="30%" align="left" colspan=5>
									<input type="radio" name="excute_typ" value="0" onclick="displayType(this.value)" checked>
									��̨�豸����
									<input type="radio" name="excute_typ" value="1" onclick="displayType(this.value)">
									�����ļ�����
									<input type="radio" name="excute_typ" value="2" onclick="displayType(this.value)">
									�߼���ѯ
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" width="4%" id="device">
								<TD class=column align="right" width="15%" nowrap>�豸���к�</TD>
								<TD width="85%" colspan=3>
									<INPUT TYPE="text" name="deviceSN" class="bk">&nbsp;
								</TD>
							</TR>
					
							<tr bgcolor="#FFFFFF" id="upload" STYLE="display:none">
								<td align="right" width="15%">�ύ�ļ�</td>
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
								<td CLASS="green_foot" align="right">ע������</td>
								<td colspan="3" CLASS="green_foot">
									1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��
									 <br>
									2���ļ��ĵ�һ��Ϊ�����У������豸���кš���
									 <br>
									3���ļ�ֻ��һ�С�
									 <br>
									4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
								</td>
							</tr>
							
							
				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="display:none">
					<TD align="right" class=column width="15%">��    ��</TD>
					<TD align="left" width="35%">
						<select name="gwShare_cityId" class="bk">
							<option value="-1">==ȫ��==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">����״̬</TD>
					<TD width="35%">
						<select name="gwShare_onlineStatus" class="bk">
							<option value="-1">==ȫ��==</option>
							<option value="0">����</option>
							<option value="1">����</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr22" STYLE="display:none">
					<TD align="right" class=column width="15%">��    ��</TD>
					<TD width="35%">
						<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
							<option value="-1">==��ѡ��==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">�豸�ͺ�</TD>
					<TD align="left" width="35%">
						<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
							<option value="-1">����ѡ����</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr23"  STYLE="display:none">	
					<TD align="right" class=column width="15%">�豸�汾</TD>
					<TD width="35%">
						<select name="gwShare_devicetypeId" class="bk"">
							<option value="-1">����ѡ���豸�ͺ�</option>
						</select>
					</TD>	
					<TD align="right" class=column width="15%">�Ƿ��</TD>
					<TD width="35%">
						<select name="gwShare_bindType" class="bk">
							<option value="-1">==ȫ��==</option>
							<option value="0">δ��</option>
							<option value="1">�Ѱ�</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr24"  STYLE="display:none">
					<TD align="right" class=column width="15%">�豸���к�</TD>
					<TD width="35%">
						<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="25" class="bk"/>
						<font color="red">*��ģ��ƥ��</font>
					</TD>
					<TD align="right" class=column width="15%" ><font id="gwShare_td1"  STYLE="display:none">�Զ����ѯ</font></TD>
					<TD width="35%">
						<input type="checkbox" name="chbx_isMactchSQL" id="chbx_isMactchSQL"  STYLE="display:none" onclick="spellSQL(this);">
					</TD>
				</TR>


				
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="10%" nowrap>�ϱ�����</TD>
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
								<TD class=column align="right" width="10%" nowrap>�˿�</TD>
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
								<TD class=column align="right" width="10%" nowrap>�ļ��ϴ�·��</TD>
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
									<TD class=column align="right" width="10%" nowrap>�Ƿ���</TD>
									<TD width="90%" colspan=3>
										<input type="radio" name="enable" value="1" checked>����
										<input type="radio" name="enable" value="0">�ر�
									</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="10%" nowrap>�����б�</TD>
								 <TD width="90%" colspan=3>
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="1">����״̬&nbsp; 
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="2">����ҵ��ע��״̬&nbsp; 
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="3">����ҵ��ע��ʧ��ԭ��&nbsp; 
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="4">PPP������������״̬&nbsp;
									 <INPUT TYPE="checkbox" NAME="paralist_a" value="5">���Ŵ������&nbsp;
									 <ms:inArea areaCode="jx_dx" notInMode="false">
										<INPUT TYPE="checkbox" NAME="paralist_a" value="6">LAN��״̬&nbsp;
										<INPUT TYPE="checkbox" NAME="paralist_a" value="7">PON��״̬&nbsp;
									 </ms:inArea>
									 <ms:inArea areaCode="nmg_dx" notInMode="false">
										<INPUT TYPE="checkbox" NAME="paralist_a" value="8">����״̬&nbsp;
									 </ms:inArea>
								 </TD>
							 </TR>
							<TR class="green_foot">
								<TD  align="right" height="23" colspan=4>
									<input type=button name="queryBtn" value="���� " onclick="checkForm();">
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
								����Ŭ��Ϊ����ѯ�����Ե�....
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


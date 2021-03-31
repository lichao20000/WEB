<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%
String device_id = request.getParameter("device_id");
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var device_id ="<%=device_id%>";
//ˢ��ʱ�� ��λ����
var refreshTime =10;
//ˢ�¶�ʱ��
var timeId;

window.onload = function()
{
 timeId = setInterval(getData,refreshTime*60*1000);
}

//��ȡչʾ������
function getData()
{
 //�����ѯDLS״̬������
 //doDLSRequestAct();
 doPPOEAndIPAct();
 
}

function doWLanAct()
{
  //alert("doWLanAct");
  var url ="paramList_data.jsp";
  var Container="WLAN";
  var paras = "type=4";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  
  CreateAct(url,paras,Container);
}

function doDLSRequestAct()
{ 
  //alert("doDLSRequestAct");
  var url ="paramList_data.jsp";
  var Container="DLS";
  var paras = "type=1";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  
  CreateAct(url,paras,Container);
}

function doPPOEAndIPAct()
{
  //alert("doPPOEAndIPAct");
  var url ="paramList_data.jsp";
  var paras = "type=2";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  var Container="PPOEAndIP";
  CreateAct(url,paras,Container);
}

function doLanAct()
{
  //alert("doLanAct");
  var url ="paramList_data.jsp";
  var paras = "type=3";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  var Container="LAN";
  CreateAct(url,paras,Container);
}



//����URL�Ͳ�������AJAX����
function CreateAct(url,pars,Container){
     //alert("wp");
	//pars = encodeURI(encodeURI(pars));
	var myAjax
		= new Ajax.Request(
							url,
							{
								//encoding:"GBK",
								method:"post",
								parameters:pars,
								onFailure:showError,
								onSuccess:function(req){
									showData(Container,req);
								},
								onLoading:function(req){
									showLoading(Container,req);
								},
								onException:function(req){
									doException(Container,req);
								}
							 }
						  );
}


/*
//ע��ص�����
Ajax.Responders.register({
	onCreate:function(){
		refreshBottun();
	},
	onComplete:function(){
		if(Ajax.activeRequestCount == 0){
			closeMsgDlg();
		}
	}
});
*/
//AJAX�ص�����
//�����������Ƽ�ÿ�������ı�ʶλclass
//��������ݲ�������
function showData(Container,req){    
	if(Container=="DLS"){	
		$("idLayerView").innerHTML = req.responseText;		
		$("DLS").innerHTML = $("DLS_").innerHTML;			
	}else if(Container=="PPOEAndIP"){
		$("idLayerView1").innerHTML = req.responseText;
		$("PPOE").innerHTML = $("PPPOE_").innerHTML;
		$("IP").innerHTML = $("IP_").innerHTML;
		$("ATM").innerHTML = $("ATM_").innerHTML;	
		//����Lan���ݵĲ�ѯ����
        doLanAct();	
	}
	else if(Container=="LAN")
	{
	   $("idLayerView2").innerHTML = req.responseText;
	   $("LAN").innerHTML = $("LAN_").innerHTML;
	   //����Wlan���ݵĲ�ѯ����
       doWLanAct();
	}
	else if(Container=="WLAN")
	{
	   $("idLayerView3").innerHTML = req.responseText;
	   $("WLAN").innerHTML = $("WLAN_").innerHTML;
	   //����DLS����
	   doDLSRequestAct();
	}
}

//�ݲ����ȡ������ʱ�Ĺ���Ч��
function showLoading(Container,req){
	//$("sp_mrtg" + index).innerHTML = "����ͼ��...<a href=\"javascript://\" onclick='refreshMrtg()'>ˢ��</a>";
	//$("sp_mrtg" + index).innerHTML = "<img src=../images/loading.gif>";
}

//������Ϣ
function showError(req){
	//����ģʽ
	if(__debug)
		$("debug").innerHTML = req.responseText;
}

//�쳣��׽
function doException(Container,req){

}
</SCRIPT>
<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD>
		<TABLE border=0 cellspacing=1 cellpadding=1 width="100%">			
			<TR>
				<TH class=yellow_title>PPPOE״̬������</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="PPPOE" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>IP��ַ����</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="IP" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>ATM VC״̬������</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="ATM" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>LAN�˿�״̬������</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="LAN" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>WLAN�˿�״̬������</TH>
			</TR>
			<TR>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td>
				<DIV id="WLAN" align=center></DIV>
				</table>
				</TD>
			</TR>
			<TR>
				<TH class=yellow_title>DLS״̬���ٶ�</TH>
			</TR>
			<TR>
				<TD>
					<TABLE class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td><DIV id="DLS" align=center></DIV></td></tr>
					</TABLE>			
				</TD>
			</TR>
		</TABLE>		
		<DIV id="idLayerView" style="overflow:auto;width:80%;display:none;">idLayerView</div>		
		<DIV id="idLayerView1" style="overflow:auto;width:80%;display:none;">idLayerView1</div>		
		<DIV id="idLayerView2" style="overflow:auto;width:80%;display:none;">idLayerView2</div>	
		<DIV id="idLayerView3" style="overflow:auto;width:80%;display:none;">idLayerView3</div>	
		</TD></TR>	
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
getData();
</SCRIPT>

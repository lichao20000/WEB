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
 //�����ѯLAN״̬������
 doLanAct();
 
}

function doLanAct()
{
  //alert("doLanAct");
  var url ="paramList_data.jsp";
  var paras = "type=3";
  paras +="&device_id="+device_id;
  paras += "&tt=" + new Date().getTime();
  var Container="LAN";
  
  $("LAN").innerHTML = "���ڻ�ȡ���ݣ����Ժ�...";
  
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

//AJAX�ص�����
//�����������Ƽ�ÿ�������ı�ʶλclass
//��������ݲ�������
function showData(Container,req)
{    
	$("idLayerView2").innerHTML = req.responseText;
	$("LAN").innerHTML = $("LAN_").innerHTML;	
	iframeAutoFit(); 
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

function iframeAutoFit(){
	try{
		if(window!=parent){
			var a = parent.document.getElementsByTagName("IFRAME");
			for(var i=0; i<a.length; i++){
				if(a[i].contentWindow==window){
					var h1=0, h2=0;
					a[i].parentNode.style.height = a[i].offsetHeight +"px";
					a[i].style.height = "10px";
					if(document.documentElement&&document.documentElement.scrollHeight)
						h1=document.documentElement.scrollHeight;
					if(document.body) h2=document.body.scrollHeight;
					var h=Math.max(h1, h2);
					if(document.all) {h += 4;}
					if(window.opera) {h += 1;}
					a[i].style.height = a[i].parentNode.style.height = h +"px";
				}
			}
		}
	}
	catch (ex){}
}
</SCRIPT>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD>
		<TABLE border=0 cellspacing=1 cellpadding=1 width="100%">			
			<TR height=30>
				<TH class=yellow_title>LAN�˿�״̬������</TH>
			</TR>
			<TR height=210>
				<TD>
				<table class=text cellSpacing=0 cellPadding=0 align=center bgColor=black border=0 width="100%"
							bordercolorlight="#000000" bordercolordark="#FFFFFF">							
							<tr><td class=column><DIV id="LAN" align=center></DIV></td></tr>
				</table>
				</TD>
			</TR>			
		</TABLE>			
		<DIV id="idLayerView2" style="overflow:auto;width:80%;display:none;">idLayerView2</div>			
		</TD></TR>	
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
getData();
</SCRIPT>

<%--
Author		: chenjie(67371)
Date		: 2011-5-7
--%>
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<SCRIPT LANGUAGE="JavaScript">

//-----------------ajax----------------------------------------
  var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
   
   //ajaxһ��ͨ�÷���
	function sendRequest(method,url,object){
		request.open(method, url, true);
		request.onreadystatechange = function(){refreshPage(object);};
		request.send(null);
	}
	function refreshPage(object){
		if (request.readyState == 4) {
    		if (request.status == 200) {
        		object.innerHTML = request.responseText;
			} else{
				alert("status is " + request.status);
			}
		}
	}
/**
	function sendRequest2(method, url, object,sparam){
		request.open(method, url, true);
		request.onreadystatechange = function(){
			refreshPage(object);
			doSomething(sparam);
		};
		request.send(null);
	}
	**/
//---------------------------------------------------------------

</SCRIPT>


<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp" %>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" >
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">����Ϣ��ѯ</div>
				</td>
				<td>&nbsp;</td>
			</tr>
	</table>
	<!-- �߼���ѯpart -->
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<tr>
			<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
						<tr><th colspan="4" id="gwShare_thTitle">�߼���ѯ</th></tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">LOID</TD>
							<TD align="left" width="35%">
								<INPUT TYPE="text" NAME="username" size=20
									 class=bk maxlength=40>&nbsp;<font color="#FF0000"></font>
							</TD>
							<TD align="right" class=column width="15%">�����к�</TD>
							<TD width="35%">
								<INPUT TYPE="text" NAME="card_serialnumber" maxlength=64
									 class=bk size=20>&nbsp;<font color="#FF0000"></font>
							</TD>
						</TR>
						<!-- 
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">����״̬</TD>
							<TD align="left" colspan=3>
								<select name="online_status" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="1">����</option>
									<option value="0">����</option>
								</select>
							</TD>
						</TR>
						 -->
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
								onclick="javascript:gwShare_queryChange('1');" name="gwShare_jiadan" value="�򵥲�ѯ" />
								<input type="button" class=jianbian style="CURSOR:hand" style="display:none " 
								onclick="javascript:gwShare_queryChange('2');" name="gwShare_gaoji" value="�߼���ѯ" />
								<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
								onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="�����ѯ" />
								<input type="button" onclick="javascript:doQuery()" class=jianbian 
								name="gwShare_queryButton" value=" �� ѯ " />
								<input class=jianbian onclick="" type="reset"
							    value=" �� �� " />
							</td>
						</tr>
				</table>
			</td>
		</tr>
	</table>
	</FORM>
	<!-- չʾ���part -->
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999 id="idData">
              	<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>

<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

// ��ѯ
function doQuery()
{
	trimAll();
	var url = "<s:url value='/itms/resource/cardInfo!queryCard.action'/>";
	var username = $("input[@name='username']").val();
	var card_serialnumber = $("input[@name='card_serialnumber']").val();
	var online_status = $("select[@name='online_status']").val();
	
	/*
	$.post(url,{
		vendor:vendor,
		device_model:device_model,
		hard_version:hard_version,
		soft_version:soft_version,
		is_check:is_check,
		rela_dev_type:rela_dev_type
	},function(ajax){
		alert("success");
	});
	*/
	
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = url;
	form.target = "dataForm";
	form.submit();
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
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
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

/** ���߷��� **/
/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//ȫ��trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}
</SCRIPT>



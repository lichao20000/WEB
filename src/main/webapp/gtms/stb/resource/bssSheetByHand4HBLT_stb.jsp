<%--
���������ֹ�����  ������
Author: fanjm
Version: 1.0.0
Date: 2017-04-23
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<HEAD>
<title>�ն�ҵ���·�</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckFormForm.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
var loginCityId = "-1";
String.prototype.replaceAll = function(oldStr,newStr) { 
    return this.replace(new RegExp(oldStr,"gm"),newStr); 
}
function reset(){
	$("input[@name='obj.loid']").val("");
	cleanValue();
}

function cleanValue()
{
	setTime();
	$("#stbSheet").hide();
	
	$("select[@name='obj.cityId']").val(loginCityId);
	
	$("input[@id='stbServ']").attr("checked",false);
	
	$("input[@name='obj.stbUserID']").val("");
	$("input[@name='obj.stbUserPwd']").val("");
	$("input[@name='obj.stbNTP1']").val("");
	$("input[@name='obj.stbNTP2']").val("");
	$("input[@name='obj.stbBrowserURL1']").val("");
}




$(function() {
	var gw_type = '<s:property value="gw_type" />';
	var loid = '<s:property value="loid" />';
	loginCityId = '<s:property value="cityId" />';
	$("select[@name='obj.cityId']").val(loginCityId);
	
	$("#stbhead").show();
	
	if(loid != ""){
		$("input[@name='obj.loid']").val(loid);
		checkLoid();
		return ;
	}
	
	setTime();
	
	$("input[@name='obj.loid']").blur(function () 
	{
		if($("input[@name='obj.loid']").val()!="")
		 checkLoid();
	})
});



function setTime(){
	var dstr = "";
	var d = new Date();
	
	dstr += d.getFullYear();
	if(d.getMonth()+1<10)
	{
		dstr += '0';
		dstr += d.getMonth()+1;
	}
	else
	{
		dstr += d.getMonth()+1;
	}
	
	if(d.getDate()<10)
	{
		dstr += '0';
		dstr += d.getDate();
	}
	else
	{
		dstr += d.getDate();
	}
	
	if(d.getHours()<10)
	{
		dstr += '0';
		dstr += d.getHours();
	}
	else
	{
		dstr += d.getHours();
	}
	
	if(d.getMinutes()<10)
	{
		dstr += '0';
		dstr += d.getMinutes();
	}
	else
	{
		dstr += d.getMinutes();
	}
	
	dstr += "00";
	$("input[@name='obj.dealDate']").val(dstr);
}



// ���ڶ�̬��ʾ����ҵ�񣨿����IPTV��H248,sip��
function showSheet(objId,name){
    if($("#"+objId).attr("checked")) {
       document.getElementById(name).style.display="";
    } else {
        document.getElementById(name).style.display="none";
    }
}
// reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
function reg_verify(addr){
	//������ʽ
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg)) {
    	//IP��ַ��ȷ
        return true;
    } else {
    	//IP��ַУ��ʧ��
         return false;
    }
}


//���LOID�Ƿ����
function checkLoid()
{
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�գ�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	cleanValue();
	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4HBLT!checkStbLoid.action'/>";
	$.post(url,{
		"obj.loid":$("input[@name='obj.loid']").val()
	},function(ajax){
		if("000" == ajax)
		{
			alert("LOID����ʹ�á�");
		}
		else
		{
			//alert(ajax);
			$("#stbServ").attr("checked",false); 
			var relt = ajax.split("|");
			$("select[@name='obj.cityId']").val(relt[8]);
			
			$("#stbServ").attr("checked",true); 
			$("input[@name='obj.stbUserID']").val(relt[98]);
			$("input[@name='obj.stbUserPwd']").val(relt[99]);
			$("input[@name='obj.stbNTP1']").val(relt[100]);
			$("input[@name='obj.stbNTP2']").val(relt[101]);
			$("input[@name='obj.stbBrowserURL1']").val(relt[102]);
			
			showSheet('stbServ','stbSheet');
		}
	});
}


//ҵ�������ύ
function delBusiness()
{
	var stbServ = $("input[@id='stbServ'][checked]").val();
	
	var loid = $("input[@name='obj.loid']").val();
	var cityId = $("select[@name='obj.cityId']").val();
	
	
	if("" == loid)
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}

	if("-1" == $("select[@name='obj.cityId']").val())
	{
		alert("���ز���Ϊ�ա�");
		$("select[@name='obj.cityId']").focus();
		return false;
	}
	
	
	/* if((netServTypeId == "" && iptvServTypeId == "" && hvoipServTypeId == "")||(netServTypeId != "" && iptvServTypeId != "" && hvoipServTypeId != "")){
		var r=confirm("ȷ��Ҫɾ����ǰ�û�����ҵ���� ��");
	  	if (r==false){
	    	return ;
	    }
	} */
	if(stbServ != ""){
		var r=confirm("ȷ��Ҫɾ��������ҵ���� ��");
	  	if (r==false){
	    	return ;
	    }
	}
	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4HBLT!delStbBusiness.action'/>";
	$.post(url,{
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.loid":loid,
		"obj.stbServ":$("input[@id='stbServ'][checked]").val(),
		"obj.cityId":cityId
	},function(ajax){
		alert(ajax);
		$("button[@name='delBtn']").attr("disabled", true);
		checkLoid();
	});
	//�һ���ť
	$("button[@name='delBtn']").attr("disabled", true);
}



//����ҵ���ύ
function doBusiness()
{
	var loidvalue = "";
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	

	if("-1" == $("select[@name='obj.cityId']").val())
	{
		alert("���ز���Ϊ�ա�");
		$("select[@name='obj.cityId']").focus();
		return false;
	}
	/* if("-1" == $("select[@name='obj.deviceType']").val())
	{
		alert("��ѡ���ն����͡�");
		$("select[@name='obj.deviceType']").focus();
		return false;
	} */
	
	
	var stbServ = $("input[@id='stbServ'][checked]").val();
	
	if("25" != stbServ)
	{
		alert("��ѡ��Ҫ�ύ��ҵ�񹤵�");
		return;
	}
	else
	{
		if("" == $("input[@name='obj.stbUserID']").val())
		{
			alert("ҵ���˺Ų���Ϊ�ա�");
			$("input[@name='obj.stbUserID']").focus();
			return false;
		}
		else if("" == $("input[@name='obj.stbUserPwd']").val())
		{
			alert("ҵ���˺����벻��Ϊ�ա�");
			$("input[@name='obj.stbUserPwd']").focus();
			return false;
		}
		else if("" == $("input[@name='obj.stbNTP1']").val())
		{
			alert("NTP1����Ϊ�ա�");
			$("input[@name='obj.stbNTP1']").focus();
			return false;
		}
		else if("" == $("input[@name='obj.stbNTP2']").val())
		{
			alert("NTP2����Ϊ�ա�");
			$("input[@name='obj.stbNTP2']").focus();
			return false;
		}
		else if("" == $("input[@name='obj.stbBrowserURL1']").val())
		{
			alert("��֤��ַ1����Ϊ�ա�");
			$("input[@name='obj.stbBrowserURL1']").focus();
			return false;
		}
	}
	
	
	var r=confirm("ȷ��Ҫ�ύ������ҵ���� ��");
  	if (r==false){
    	return ;
    }
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4HBLT!stbDoBusiness.action'/>";
	$.post(url,{
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		
		"obj.stbServ":stbServ,
		"obj.stbUserID":$("input[@name='obj.stbUserID']").val(),
		"obj.stbUserPwd":$("input[@name='obj.stbUserPwd']").val(),
		"obj.stbNTP1":$("input[@name='obj.stbNTP1']").val(),
		"obj.stbNTP2":$("input[@name='obj.stbNTP2']").val(),
		"obj.stbBrowserURL1":$("input[@name='obj.stbBrowserURL1']").val()
		
		
	},function(ajax){
		alert(ajax);
		//�һ���ť
		$("button[@name='subBtn']").attr("disabled", false);
		checkLoid();
	});
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
}


</script>
</HEAD>
<body>
<FORM NAME="frm" action="" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD colspan="4">
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">�������ֹ�����</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15" height="12"></td>
					</tr>
				</table>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr align="left">
						<input type="hidden" id="userServTypeId" name = "obj.userServTypeId" value="20">
						<input type="hidden" id="userOperateId" name = "obj.userOperateId" value="1">
						<input type="hidden"  name = "obj.cmdId" value="FROMWEB-0000001">
						<input type="hidden"  name = "obj.authUser" value="itms">
						<input type="hidden"  name = "obj.authPwd" value="123">
						<td colspan="4" class="green_title_left">
						�������û�����
						</td>
					</tr>
					<tbody id="jirRuBssSheet" >
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">LOID:</TD>
							<TD width="35%" >
								<input type="text" id="loid" name="obj.loid" class=bk value="">&nbsp;
								
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button type="button" name="subButton" onclick="checkLoid()">���LOID�Ƿ����</button>
								<!-- onblur="checkLoid()" -->
							</TD>
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<s:select list="cityList" name="obj.cityId"
								headerKey="-1" headerValue="��ѡ������" listKey="city_id"
								listValue="city_name" value="cityId" cssClass="bk"></s:select>
							<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
							<TD width="35%" colspan="3">
								<input type="text" name="obj.dealDate" class=bk value="">
							<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>
					
					
					<tr id="stbhead" align="left" style="display:none">
						<td colspan="4" class="green_title_left">
						<input type="checkbox" value="25" id="stbServ" onclick="showSheet('stbServ','stbSheet');"/>
						������ҵ�񹤵�
						</td>
					</tr>
					<tbody id="stbSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">ҵ���˺�:</TD>
							<TD width="35%">
								<input type="text" name="obj.stbUserID" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">ҵ���˺�����:</TD>
							<TD width="35%">
								<input type="text" name="obj.stbUserPwd" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">NTP1:</TD>
							<TD width="35%" >
								<input type="text" name="obj.stbNTP1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">NTP2:</TD>
							<TD width="35%">
								<input type="text" name="obj.stbNTP2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">��֤��ַ1:</TD>
							<TD width="35%" colspan="3">
								<input type="text" name="obj.stbBrowserURL1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>	
					
					
					<TR align="left" id="doBiz" >
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" onclick="delBusiness()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" onclick="reset()">��&nbsp;&nbsp;��</button>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
<%@ include file="../../../foot.jsp"%>
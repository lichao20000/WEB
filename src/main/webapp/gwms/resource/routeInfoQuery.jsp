<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>·���û���ѯ</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

  <%
  
  String isBatch = request.getParameter("isBatch");
  String gw_type = request.getParameter("gw_type");
  if(null == gw_type ||  "".equals(gw_type)){
	  gw_type="1";  
  }
  boolean isMatchSQL = false;
  
long roleId = ((UserRes)session.getAttribute("curUser")).getUser().getRoleId();
String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
//���յ�ʡ���Ĺ���ԱȨ�޲ſ����Զ�sql��ѯ�豸
if("ah_dx".equals(shortName)&&(1==roleId||2==roleId))
{
	isMatchSQL = true;
}
boolean allSelect = "ah_dx".equals(shortName);
%>


<SCRIPT LANGUAGE="JavaScript">
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["mainForm","dataForm"]

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

$(window).resize(function(){
	dyniframesize();
}); 

/* reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
������򷵻�true�����򣬷���false��*/


//��֤��������ĳ����Ƿ�Ϸ�
function do_test()
{
	var single_condition = $.trim($("input[@name='routeBean.single_condition']").val());
		$("input[@name='routeBean.single_condition']").val(single_condition);
	  var queryType = $("select[@name='routeBean.queryType']").val();
	  
	 	if(""==single_condition){
	 		$("input[@name='routeBean.query_type']").val("2");
	}else if(""!=single_condition){
		
		if("1" == queryType){
			if(single_condition.length<6&&single_condition.length>0){
			alert("�������������6λ�豸���кŽ��в�ѯ��");
			document.mainForm.single_condition.focus();
			return false;
		}
		}
		$("input[@name='routeBean.query_type']").val("1");
	}
	
	return true;
}

function do_query(){
	
	if(!do_test()){
		return;
	}
	
	setTimeout("gwShare_queryDevice()", 2000);
}



/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryDevice(){
	$("input[@name='gwShare_queryButton']").attr("disabled",true);
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/gwms/resource/routeInfoQuery!queryRouteInfo.action"/>";
	form.submit();
}



/*------------------------------------------------------------------------------
//������:		��ʼ��������ready��
//����  :	��
//����  :	��ʼ�����棨DOM��ʼ��֮��
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function timeDefault(){
	var date = new Date();
	 var dt = date;
	 var seperator1 = "-"; 
	 var seperator2 = ":"; 
	 var year = date.getFullYear();
	 var month = date.getMonth() + 1;
   var strDate = date.getDate();
  
   if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
   
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    
    dt.setMonth( dt.getMonth()-6 );
   	 var yearBefore = dt.getFullYear();
	 var monthBefore = dt.getMonth() + 1;
   var strDateBefore = dt.getDate();
   if (monthBefore >= 1 && monthBefore <= 9) {
        monthBefore = "0" + monthBefore;
    }
    if (strDateBefore >= 0 && strDateBefore <= 9) {
        strDateBefore = "0" + strDateBefore;
    }
   var beforetdate = yearBefore + seperator1 + monthBefore + seperator1 + strDateBefore;
   $("input[@name='routeBean.start_time']").val(beforetdate);
	 $("input[@name='routeBean.end_time']").val(currentdate);
}



$(function(){
	timeDefault();
	gwShare_change_select("routeBean.city","-1");
	gwShare_change_select("routeBean.vendor","-1");
	var gw_type=<%=gw_type%>;
	$("input[@name='routeBean.gw_type']").val(gw_type);
});

/*------------------------------------------------------------------------------
//������:		��дreset
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ��3�������ѯ
//����  :	��ҳ���������
//����ֵ:		ҳ������
//˵��  :	
//����  :	Create 2010-4-26 of By qxq
------------------------------------------------------------------------------*/
function gwShare_revalue(){
		$("input[@name='routeBean.single_condition']").val("");
		$("input[@name='routeBean.query_type']").val("");
		$("select[@name='routeBean.queryType']").attr("value",'-1');
	  $("select[@name='routeBean.gwShare_cityId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_vendorId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_deviceModelId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_devicetypeId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_netType']").attr("value",'2');
    timeDefault();
		gwShare_change_select('routeBean.deviceModel','-1');
	
}



/*------------------------------------------------------------------------------
//������:		deviceSelect_change_select
//����  :	type 
	            vendor      �����豸����
	            deviceModel �����豸�ͺ�
	            devicetype  �����豸�汾
//����  :	����ҳ������̡��ͺż�����
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "routeBean.city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_cityId']"),selectvalue);
			});
			break;
		case "routeBean.vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_vendorId']"),selectvalue);
				$("select[@name='routeBean.gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "routeBean.deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='routeBean.gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='routeBean.gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_deviceModelId']"),selectvalue);
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "routeBean.devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='routeBean.gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='routeBean.gwShare_deviceModelId']").val();
			var gwShare_isBatch = <%=isBatch %>;
			if("-1"==deviceModelId){
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId,
				isBatch:gwShare_isBatch
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_devicetypeId']"),selectvalue);
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
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	if("routeBean.city" == type && true==<%=allSelect%>)
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
			field.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}

function showMsg(){
	var ind = $("#queryType ").val();
	if("1" == ind){
		$("#warnMsg").show();
	}else{
		$("#warnMsg").hide();
	}
}

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="mainForm" action="" target="dataForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td >
			<table border=0 cellspacing=1 cellpadding=2 width="98%" align="center" bgcolor=#999999 >
				<tr><th colspan="4" id="gwShare_thTitle">·���û���ѯ</th></tr>
				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="display:">
					<input type="hidden" name="routeBean.query_type"
							value=''>
							<input type="hidden" name="routeBean.gw_type"
							value=''>
					<TD width="15%" class=column align="right">
						<select id="queryType" name="routeBean.queryType" class="bk" onchange="showMsg()">
							<option value="1">�豸���к�</option>
							<option value="2">����˺�</option>
							<option value="3">�߼�ID</option>
							<option value="4">�绰����</option>
						</select>
					</TD>
					<TD align="left" width="35%"><input type="text" name="routeBean.single_condition" value="" size="25" maxlength="" />
						<font color="red" id ="warnMsg" >*��ģ��ƥ��</font></TD>
					<TD align="right" class=column width="15%">��    ��</TD>
					<TD align="left" width="35%">
						<select name="routeBean.gwShare_cityId" class="bk" >
							<option value="-1">==ȫ��==</option>
						</select>
					</TD>
					
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr22" STYLE="display:">
					<TD align="right" class=column width="15%">��    ��</TD>
					<TD width="35%">
						<select name="routeBean.gwShare_vendorId" class="bk" onchange="gwShare_change_select('routeBean.deviceModel','-1')">
							<option value="-1">==��ѡ��==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">�豸�ͺ�</TD>
					<TD align="left" width="35%">
						<select name="routeBean.gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('routeBean.devicetype','-1')">
							<option value="-1">����ѡ����</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr23"  STYLE="display:">	
					<TD align="right" class=column width="15%">�豸�汾</TD>
					<TD width="35%">
						<select name="routeBean.gwShare_devicetypeId" class="bk"">
							<option value="-1">����ѡ���豸�ͺ�</option>
						</select>
					</TD>	
					<TD align="right" class=column width="15%">������ʽ</TD>
					<TD width="35%">
						<select name="routeBean.gwShare_netType" class="bk">
							<option value="2" selected = "selected">·��</option>
							<option value="1">�Ž�</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr24"  STYLE="display:">
				<td align="right" class=column width="15%">
													��ʼʱ�䣺
												</td>
												<td align="left" width="35%">
													<input type="text" id="start_time" name="routeBean.start_time" readonly  class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('start_time'),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="ѡ��">
												</td>
					<td align="right" class=column width="15%">
													����ʱ�䣺
												</td>
												<td align="left" width="35%">
													<input type="text" id="end_time" name="routeBean.end_time" readonly  class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('end_time'),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="ѡ��">
												</td>
				</TR>
			
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_query()" class=jianbian 
						name="gwShare_queryButton" value=" �� ѯ " />
						<input type="button" class=jianbian onclick="javascript:gwShare_revalue()" 
						name="gwShare_reButto" value=" �� �� " />
					</td>
				</tr>
				
			</table>
		</td>
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
		<TR>
				<TD bgcolor=#ffffff id="idData" align="center"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="98%" src=""></iframe></TD>
			</TR>
</TABLE>
</form>
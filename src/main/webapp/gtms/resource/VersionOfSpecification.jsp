<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<%--
	/**
	 * �淶�汾��ѯҳ��
	 *
	 * @author ����(����) Tel:�绰
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>


<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<lk:res />

<SCRIPT LANGUAGE="JavaScript">

$(function(){
	Init();
});


function Init(){
	// ��ʼ������
	gwShare_change_select("vendor","-1");
	
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/gtms/resource/deviceVersionInfo!queryList.action"/>";
	form.target = "dataForm";
	form.submit();
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
		case "city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='devicetypeId']"),selectvalue);
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
function gwShare_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
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



function queryReset(){
    gwShare_change_select("vendor","-1");
    $("select[@name='rela_dev_type']").val("-1");
}



//��ѯ
function query(){
	var vendorId = $("select[@name='gwShare_vendorId']").val();
	var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
	var devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
	var bssDevPort = $("select[@name='bssDevPort']").val();
	
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = '<s:url value="/gtms/resource/deviceVersionInfo!queryList.action"/>';
	form.submit();
	
}


</SCRIPT>

</head>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162"> <div align="center" class="title_bigwhite">�淶�汾��ѯ</div> </td>
				<td> <img src="/itms/images/attention_2.gif" width="15" height="12"></td>
			</tr>
		</table>
		<!-- �߼���ѯpart -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">�淶�汾��ѯ</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�豸����</TD>
						<TD align="left" width="35%">
							<select name="vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD width="35%">
							<select name="deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">����ѡ����</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�豸�汾</TD>
						<TD align="left" width="35%">
							<select name="devicetypeId" class="bk"">
								<option value="-1">����ѡ���豸�ͺ�</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸����</TD>
						<TD width="35%" >
							<select name="rela_dev_type" class="bk">
							<option value="-1">==��ѡ��==</option>
							<option value="1">e8-b</option>
							<option value="2">e8-c</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�豸�ն˹��</TD>
						<TD align="left" width="35%" colspan="3">
							<select name="bssDevPort" class="bk"">
								<option value="-1">����ѡ���豸�ն˹��</option>
								<s:if test="bssDevPortLsit!=null">
									<s:if test="bssDevPortLsit.size()>0">
										<s:iterator value="bssDevPortLsit">
											<option value="<s:property value="spec_id"/>"> <s:property value="spec_name"/> </option>
										</s:iterator>
									</s:if>
								</s:if>
							</select>
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button"
							onclick="javascript:query()" class=jianbian
							name="gwShare_queryButton" value=" �� ѯ " /> <input type="button"
							class=jianbian name="gwShare_reButto" value=" �� �� "
							onclick="javascript:queryReset();" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<input type='hidden' name="gw_type" value="<s:property value="gw_type" />" />
		</FORM>
		<!-- չʾ���part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		
		</TD>
	</TR>
</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

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
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>
</html>
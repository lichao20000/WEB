<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>

<title>�豸��ѯ</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function gwShare_queryField_selected(value){
	$("input[@name='gwShare_queryField_temp']").val(value.value);
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
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    if("1"==gwShare_queryType){
		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		url = url + "&gwShare_queryField=" + gwShare_queryField;
		url = url + "&gwShare_queryParam=" + gwShare_queryParam;
	}else if("2"==gwShare_queryType){
	 	var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
        var gwShare_onlineStatus = $("select[@name='gwShare_onlineStatus']").val();
        var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
        var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
        var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
        var gwShare_bindType = $("select[@name='gwShare_bindType']").val();
        var gwShare_deviceSerialnumber = $("input[@name='gwShare_deviceSerialnumber']").val();
        
        url = url + "&gwShare_cityId=" + gwShare_cityId;
        url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
        url = url + "&gwShare_vendorId=" + gwShare_vendorId;
        url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
        url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
        url = url + "&gwShare_bindType=" + gwShare_bindType;
        url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
	}else {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
	}
	url = url + "&refresh=" + new Date().getTime();

	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	if(typeof(returnVal)=='undefined'){
		return;
	}else{
		deviceResult(returnVal);
	}
}

/*------------------------------------------------------------------------------
//������:		��ʼ��������ready��
//����  :	��
//����  :	��ʼ�����棨DOM��ʼ��֮��
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
$(function(){
	var queryType = "<s:property value="queryType"/>";
	var queryResultType = "<s:property value="queryResultType"/>";
	var startQuery = "<s:property value="startQuery"/>";
	if(""!=queryType && null!=queryType){
		$("input[@name='gwShare_queryType']").val(queryType);
	}
	if(""!=queryResultType && null!=queryResultType){
		$("input[@name='gwShare_queryResultType']").val(queryResultType);
	}
	gwShare_queryChange(queryType);
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//������������������ʱȥ������Ӧƥ��
	//$.autoMatch3("<s:url value='/gwms/share/gwDeviceQuery!getDeviceSn.action'/>",str,"#");
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
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	if("1"==gwShare_queryType){
		$("input[@name='gwShare_queryField']").get(0).checked = true;
		$("input[@name='gwShare_queryParam']").val("");
	}else if("2"==gwShare_queryType){
		$("select[@name='gwShare_cityId']").attr("value",'-1')
		$("select[@name='gwShare_onlineStatus']").attr("value",'-1')
		$("select[@name='gwShare_vendorId']").attr("value",'-1')
		$("select[@name='gwShare_bindType']").attr("value",'-1')
       	$("input[@name='gwShare_deviceSerialnumber']").val("");
		gwShare_change_select('deviceModel','-1');
	}
}

/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryChange(change){
	var gwShare_gaoji_value = $("input[@name='gwShare_gaoji_value']").val();
	var gwShare_import_value = $("input[@name='gwShare_import_value']").val();
	switch (change){
		case "2":
			/**
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("2");
			$("input[@name='gwShare_jiadan']").css("display","");
			$("input[@name='gwShare_gaoji']").css("display","none");
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","");
			$("tr[@id='gwShare_tr22']").css("display","");
			$("tr[@id='gwShare_tr23']").css("display","");
			$("tr[@id='gwShare_tr24']").css("display","");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val(" �� ѯ ");
			**/
			//���������
			//gwShare_change_select("city","-1");
			gwShare_change_select("vendor","-1");
			break;
		case "4":
			gwShare_change_select_stb("vendor","-1");
			break;
		case "3":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("3");
			$("input[@name='gwShare_jiadan']").css("display","");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("input[@name='gwShare_queryButton']").val("�����ļ�");
			break;
		case "1":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("1");
			$("input[@name='gwShare_jiadan']").css("display","none");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val(" �� ѯ ");
			break;
		default:
			break;
	}
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
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
				//$("select[@name='vendor']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor']").val();
			if("-1"==vendorId){
				$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
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
//����  :	�˷������ڻ������豸�汾��ѯ
------------------------------------------------------------------------------*/
function gwShare_change_select_stb(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
				//$("select[@name='vendor']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor']").val();
			if("-1"==vendorId){
				$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
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

/*------------------------------------------------------------------------------
//������:		gwShare_setGaoji
//����  :	
//����  :	���ظ߼���ѯ
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setGaoji(){
	$("input[@name='gwShare_gaoji_value']").val("");
	$("input[@name='gwShare_gaoji']").css("display","");
}

/*------------------------------------------------------------------------------
//������:		gwShare_setGaoji
//����  :	
//����  :	���ظ߼���ѯ
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	$("input[@name='gwShare_import']").css("display","");
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
<form name="gwShare_selectForm" action="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="1" />
<input type="hidden" name="gwShare_queryResultType" value="radio" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_gaoji_value" value="none" />
<input type="hidden" name="gwShare_import_value" value="none" />

</form>
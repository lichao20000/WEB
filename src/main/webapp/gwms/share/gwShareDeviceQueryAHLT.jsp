<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/commonUtil.js"/>"></script>
<%
	String isBatch = request.getParameter("isBatch");
	String gw_type = request.getParameter("gw_type");
	String CQ_softUp = request.getParameter("CQ_softUp");
	if(null == gw_type ||  "".equals(gw_type)){
		gw_type="1";  
	}
	boolean isMatchSQL = false;
  
	long roleId = ((UserRes)session.getAttribute("curUser")).getUser().getRoleId();
	String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	//���յ�ʡ���Ĺ���ԱȨ�޲ſ����Զ�sql��ѯ�豸
	if("ah_dx".equals(shortName)&&(1==roleId||2==roleId)){
		isMatchSQL = true;
	}
	boolean allSelect = "ah_dx".equals(shortName);
	String isOnline_time = request.getParameter("online_time");
%>

<SCRIPT LANGUAGE="JavaScript">

var area = '<%=shortName%>';

/* reg_verify - ��ȫ���������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
������򷵻�true�����򣬷���false��*/
function reg_verify(addr)
{
	//�������ʽ
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg)){
    	//IP��ַ��ȷ
        return true;
    }else{
    	//IP��ַУ��ʧ��
         return false;
    }
}

//��֤��������ĳ����Ƿ�Ϸ�
function do_test()
{
	//��ȡ��������ݣ�trimһ��
	//var gwShare_queryParam = document.gwShare_selectForm.gwShare_queryParam.value; 
	  var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		//$.trim($("input[@name='gwShare_queryParam']").val());
		gwShare_queryParam = $.trim(gwShare_queryParam);
	
	//������������  ע��by zhangchy 2011-09-21 
	// ע��ԭ���� ����Ǹ߼���ѯ�Ļ���gwShare_queryParam.lengthʼ��Ϊ0�����Ի�һֱalert("�������ѯ������"); ���ܽ��в�ѯ
	// �˶��߼�����������˸�д�����ּ򵥲�ѯ �� �߼���ѯ 
	//if(0 == gwShare_queryParam.length)
	//{
	//	alert("�������ѯ������");
	//	//document.gwShare_selectForm.gwShare_queryParam.focus();
	//	$("input[@name='gwShare_queryParam']").focus();
	//	return false;
	//}
	
	//�򵥲�ѯ������������  add by zhangchy 2011-09-21 -----begin ---------------
	var title = document.getElementById("gwShare_thTitle").innerHTML;
	if(title == "�� �� �� ѯ"){
		if(0 == gwShare_queryParam.length){
			alert("�������ѯ������");
			//document.gwShare_selectForm.gwShare_queryParam.focus();
			$("input[@name='gwShare_queryParam']").focus();
			return false;
		}	
	}else{  
		if(title == "�� �� �� ѯ" || title == "�� �� �� �� �� ѯ"){
			return true;
		}

       // �߼���ѯ���������豸SN��֧��
		var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
		var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
		var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
		var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
		var gwShare_isBatch = <%=isBatch %>;
		var isShowGaoJi=$("input[@name='isShowGJ']").val();
		/* ������������������ѯ������IP��ַ�Σ� */
		var gwShare_start_ip=$("input[@name='gwShare_start_ip']").val();
		var gwShare_end_ip=$("input[@name='gwShare_end_ip']").val();
		var curProvince = '<%=shortName%>';
		//�������������ж�����
		try {
			if(!"gs_dx" == curProvince){
				if(typeof(iscqsoft) == 'undefined'){
					if("1"!=isShowGaoJi	&& parseInt(gwShare_cityId)==-1 && false==<%=allSelect%>){
						alert("����ѡ�����أ�");
						return false;
					}
				}
				else if(iscqsoft==true){
					if(cqSoftCitys==""){
						alert("����ѡ�����أ�");
						return false;
					}
					
				}
			}
			
			<%-- else{
				if("1"!=isShowGaoJi	&& parseInt(gwShare_cityId)==-1 && false==<%=allSelect%>){
					alert("����ѡ�����أ�");
					return false;
				}
			} --%>
		} catch (e) {
		}
		
		//�ӱ����� �ͺ� ��ѡ,����ȥ����Щ�豸
		if(!"gs_dx" == curProvince){
			if($("input[@name='hblt_SpeedTest_flg']").val()!="true" && "1"!=isShowGaoJi){
				if(parseInt(gwShare_vendorId) == -1){
					alert("����ѡ���̣�");
					return false;
				}
				if(parseInt(gwShare_deviceModelId) == -1){
					alert("����ѡ���豸�ͺţ�");
					return false;
				}
			}
			
			if("1"!=isShowGaoJi){
				if("1"==gwShare_isBatch){
					if(parseInt(gwShare_devicetypeId) == -1){
						alert("����ѡ���豸�汾��");
						return false;
					}
				}
			}
		}
		
		
		/**
		*  IPУ��
		*/
		//��⵽IP��ַ������Ž���У��
		if(""!=gwShare_start_ip && typeof(gwShare_start_ip)!="undefined" && !checkIP(gwShare_start_ip))
		{
			return false;
		}
		if(""!=gwShare_end_ip && typeof(gwShare_start_ip)!="undefined" && !checkIP(gwShare_end_ip))
		{
			return false;
		}
		if (""!=gwShare_start_ip && typeof(gwShare_start_ip)!="undefined" && ""!=gwShare_end_ip
				&& typeof(gwShare_end_ip)!="undefined"){
			if(convertIP(gwShare_start_ip)>convertIP(gwShare_end_ip))
			{
				alert("��ʼIP���ܴ�����ֹIP");
				return false;
			}
		}
		gwShare_spellSQL();
	}
	// --------  add by zhangchy 2011-09-21 ----- end ---------------
	
	//��ȡѡ�������
	var gwShare_queryFields = document.getElementsByName("gwShare_queryField");
	
	//"�豸���к�"��ѡ��
	if(gwShare_queryFields[0].checked)
	{
		if(gwShare_queryParam.length<6&&gwShare_queryParam.length>0){
			alert("�������������6λ�豸���кŽ��в�ѯ��");
			document.gwShare_selectForm.gwShare_queryParam.focus();
			return false;
		}
	}
	//"�豸IP"��ѡ��
	else if(gwShare_queryFields[1].checked)
	{
		if(!reg_verify(gwShare_queryParam))
		{
			alert("������Ϸ���IP��ַ��");
			document.gwShare_selectForm.gwShare_queryParam.focus();
			return false;
		}
	}//"�û��ʺ�"��ѡ��   
	else if(gwShare_queryFields[2].checked)
	{
		if(gwShare_queryParam.length<6&&gwShare_queryParam.length>0){
			alert("�������������6λLOID���в�ѯ��");
			document.gwShare_selectForm.gwShare_queryParam.focus();
			return false;
		}
	}
//	gwShare_spellSQL();
	//���û���쳣��������ѯ
	return true;
}

function do_query(){
	$("div[@id='QueryData']").html("");
	if(!do_test()){
		return;
	}
	setTimeout("gwShare_queryDevice()", 2000);
}

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
	var batchType=$("input[@name='batchType']").val();
	if(batchType!=0)
	{
		querynumber();
		return ;
	}else
	{
	var url="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
	}
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    if("1"==gwShare_queryType){
		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
		url = url + "&gwShare_queryField=" + gwShare_queryField;
		url = url + "&gwShare_queryParam=" + gwShare_queryParam;
	}else if("2"==gwShare_queryType){
		var chbx_isMactchSQL = $("input[@name='chbx_isMactchSQL']").val();
		var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
	 	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
	 	try {
	 		if(typeof(iscqsoft) != 'undefined' && true==iscqsoft){
	 			gwShare_cityId = cqSoftCitys;
	 		}
		} catch (e) {
			// TODO: handle exception
		}
        var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
        var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
        var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
        var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
        var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
        var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
        var gwShare_matchSQL = $.trim($("textarea[@name='gwShare_matchSQL1']").val() +" " +$("textarea[@name='gwShare_matchSQL']").val());
        gwShare_matchSQL = gwShare_matchSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
        var proName = '<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
        if(proName == "ah_dx" || proName == "jl_lt"){
        	url = url + "&gwShare_matchSQL=" + gwShare_matchSQL;
        }
        var isExist = '<%=isOnline_time%>';
        if(proName == "gs_dx" &&  isExist == "true"){
        	var online_time = $("input[name='online_time']").val();
        	url = url + "&online_time=" + online_time;
        }
        //url = url + "&gwShare_matchSQL=" + gwShare_matchSQL;
        url = url + "&gwShare_cityId=" + gwShare_cityId;
        url = url + "&gwShare_nextCityId=" + gwShare_nextCityId;
        url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
        url = url + "&gwShare_vendorId=" + gwShare_vendorId;
        url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
        url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
        url = url + "&gwShare_bindType=" + gwShare_bindType;
        url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
        //����������������IP��Χ����
        var gwShare_start_ip=$("input[@name='gwShare_start_ip']").val();
		var gwShare_end_ip=$("input[@name='gwShare_end_ip']").val();
		if(typeof(gwShare_start_ip) == "undefined")
		{
			gwShare_start_ip = "";
		}
		if(typeof(gwShare_end_ip) == "undefined")
		{
			gwShare_end_ip = "";
		}
        url = url + "&gwShare_start_ip=" + gwShare_start_ip;
        url = url + "&gwShare_end_ip=" + gwShare_end_ip;
        try {
        	if(typeof(iscqsoft) != 'undefined' && true==iscqsoft){
        		url = url + "&iscqsoft=" + iscqsoft;
            }
		} catch (e) {
			// TODO: handle exception
		}
        
	}else if("4"==gwShare_queryType){
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
		$.post(url,{
			},function(ajax){
					var _rs = new Array();
					if (ajax>2000)
					{
						alert("�豸�������������2000!");
						return;
					}
					if(confirm("ѡ����豸����Ϊ:"+ajax+",�Ƿ����?")){
						_rs[0] = ajax;
					}else{
						return;
					}
					_rs[1] = gwShare_fileName;
					_rs[2] = gwShare_queryType;
					deviceUpResult(_rs);
			});
		return;
	}else {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	
		var instName = '<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
		if("hb_lt" == instName){
			//�ӱ�
			if(""==gwShare_fileName){
				alert("�����ϴ��ļ���");
				return;
			}
			var wei = gwShare_fileName.split(".")[1];
			if("txt" != wei){
				alert("ϵͳ�ݲ�֧�ַ�txt��ʽ���ļ�");
				return;
			}
			
		}else{
			if(""==gwShare_fileName){
				alert("�����ϴ��ļ���");
				return;
			}
		}
		
		url = url + "&gwShare_fileName=" + gwShare_fileName;
		//��ҳ���Ǻӱ���ͨ����
		if($("input[@name='hblt_SpeedTest_flg']").val()=="true"){
			var url="<s:url value='/gwms/resource/batchHttpTest!saveUpFile.action'/>";
			
			$.post(url,{
				gwShare_fileName:gwShare_fileName
			},function(ajax){	
				$("input[@name='fileName_st']").val(ajax);
				alert("������ɣ�");
				return ;
			});
			$("input[@name='deviceIds']").val("0");
			return ;
		}
	}
	url = url +"&refresh=" + new Date().getTime();
	url=url+"&gw_type="+<%=gw_type %>;
	if($("#chbx_isMactchSQL").attr("checked")==true)
    {
		var gwShare_matchSQL = $("textarea[@name='gwShare_matchSQL1']").val() +" " +$("textarea[@name='gwShare_matchSQL']").val();
		gwShare_matchSQL = gwShare_matchSQL.replace(new RegExp("'", 'g'), "[");
		if(gwShare_matchSQL == ""){
	         alert("SQLΪ�ջ����﷨��������");
	         return false;
	    }
		url="<s:url value='/gwms/share/gwDeviceQuery!queryDeviceListBySQL.action'/>?gwShare_matchSQL="+gwShare_matchSQL+"&gwShare_queryResultType="+gwShare_queryResultType;
    }
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	if(!has_showModalDialog) return;
	if(typeof(returnVal)=='undefined'){
		returnVal = window.returnValue;
		deviceResult(returnVal);
		//return;
	}else{
		deviceResult(returnVal);
	}
}

function querynumber(){
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value='/gwms/share/gwDeviceQuery!querynumber.action'/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
   if("2"==gwShare_queryType){
		var chbx_isMactchSQL = $("input[@name='chbx_isMactchSQL']").val();
	 	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
        var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
        var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
        var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
        var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
        var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
        var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
        var gwShare_matchSQL = $.trim($("textarea[@name='gwShare_matchSQL1']").val() +" " +$("textarea[@name='gwShare_matchSQL']").val());
        gwShare_matchSQL = gwShare_matchSQL.replace(new RegExp("'", 'g'), "[");
        var gwShare_start_ip = $.trim($("input[@name='gwShare_start_ip']").val());
        var gwShare_end_ip = $.trim($("input[@name='gwShare_end_ip']").val());
        if(typeof(gwShare_start_ip) == "undefined")
		{
			gwShare_start_ip = "";
		}
		if(typeof(gwShare_end_ip) == "undefined")
		{
			gwShare_end_ip = "";
		}
        //alert(gwShare_matchSQL);
        url = url + "&gwShare_matchSQL=" + gwShare_matchSQL;
        url = url + "&gwShare_cityId=" + gwShare_cityId;
        url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
        url = url + "&gwShare_vendorId=" + gwShare_vendorId;
        url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
        url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
        url = url + "&gwShare_bindType=" + gwShare_bindType;
        url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
        url = url + "&gwShare_start_ip=" + gwShare_start_ip;
        url = url + "&gwShare_end_ip=" + gwShare_end_ip;
    	$.post(url,{
		},function(ajax){
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+ajax+"</strong></font>");
		});
	return;
	}else if("4"==gwShare_queryType){
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
		$.post(url,{
			},function(ajax){
				$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+ajax+"</strong></font>");
			});
		return;
	}else {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
		$.post(url,{
		},function(ajax){
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+ajax+"</strong></font>");
				});
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
	//���������������س�ʼ��
	try {
		if(typeof(iscqsoft) != 'undefined' && true==iscqsoft){
			$("#cqsoftCity").show();
			$("#commonCity").hide();
		}
	} catch (e) {
		// TODO: handle exception
	}
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
	var gwShare_upimport_value = $("input[@name='gwShare_upimport_value']").val();
	var batchType=$("input[@name='batchType']").val();
	if(true==<%=isMatchSQL%>)
	{
		var chbx_isMactchSQL = document.getElementsByName("chbx_isMactchSQL");
		chbx_isMactchSQL[0].checked=false;
	
		$("font[@id='gwShare_td1']").css("display","");
		$("input[@id='chbx_isMactchSQL']").css("display","");
		$("textarea[@name='gwShare_matchSQL']").val("");
		$("textarea[@name='gwShare_matchSQL1']").val("");
	}
	switch (change){
		case "2":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("2");
			if(batchType!=0)
				{
				$("input[@name='gwShare_jiadan']").css("display","none");
				}
			else
				{
				$("input[@name='gwShare_jiadan']").css("display","");
				}
			$("input[@name='gwShare_gaoji']").css("display","none");
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("input[@name='gwShare_up_import']").css("display",gwShare_upimport_value);
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","");
			$("tr[@id='gwShare_tr22']").css("display","");
			$("tr[@id='gwShare_tr23']").css("display","");
			$("tr[@id='gwShare_tr24']").css("display","");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val(" �� ѯ ");
			//���������
			gwShare_change_select("city","-1");
			gwShare_change_select("vendor","-1");
			break;
		case "3":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("3");
			if(batchType==0)
			{
				$("input[@name='gwShare_jiadan']").css("display","");
			}
			else
			{
				$("input[@name='gwShare_jiadan']").css("display","none");
			}
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display","none");
			$("input[@name='gwShare_up_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("input[@name='gwShare_queryButton']").val("�����ļ�");
			var isBatchSpeedTest = $("input[name='hblt_BatchSpeedTest_flag']").val();
		 	if(isBatchSpeedTest == "true" && ("hb_lt" == "<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>")){
				$("#batch_test").show();
				$("#batch_test").next().hide(); 
				document.gwShare_loadForm.window.enableTemplete(true); //����ҳ��
			} 
			break;
		case "4":
			$("th[@id='gwShare_thTitle']").html("�� �� �� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("4");
			if(batchType!=0)
			{
			$("input[@name='gwShare_jiadan']").css("display","none");
			}
			else
			{
			$("input[@name='gwShare_jiadan']").css("display","");
			}
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display","none");
			$("input[@name='gwShare_up_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("input[@name='gwShare_queryButton']").val("���������ļ�");
			break;
		case "1":
			$("th[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("1");
			$("input[@name='gwShare_jiadan']").css("display","none");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("input[@name='gwShare_up_import']").css("display",gwShare_upimport_value);
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
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
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "nextCity":
			if($("input[@name='hblt_SpeedTest_flg']").val()!="true"){
				return false;
			}
			var city_id = $("select[@name='gwShare_cityId']").val();
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChildCore.action"/>";
			$.post(url,{
				gwShare_cityId:city_id
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_nextCityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			var gwShare_isBatch = <%=isBatch %>;
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
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
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	if("city" == type && true==<%=allSelect%>)
	{
		option = "<option value='-1' selected>==ȫ��==</option>";
	}
	else
	{
		option = "<option value='-1' selected>==��ѡ��==</option>";
	}
	
	// ɽ����ͨ �ֵ���ֻ��鵽һ�����أ�Ĭ��ѡ�е�ǰ����
	if ("sx_lt" == area && "city" == type && lineData.length == 1) {
		option = '';
        // ���ֻ��һ�����ؾͲ�����ѡ
    } else {
      option = "<option value='-1' selected>==��ѡ��==</option>";
    }
	
	//�ӱ�ѡ���ȫ��������ʡ���ĵ��������ʾ�ؼ�����
	if($("input[@name='hblt_SpeedTest_flg']").val()=="true" && "nextCity" == type){
		option = "<option value='-1' selected>==ȫ��==</option>";
	}
	
	if(option != ''){
    	field.append(option);
    }
	
	//�ӱ�ѡ��ȫ��������ʡ���ģ������ؼ�����������valueΪ-1
	if($("input[@name='hblt_SpeedTest_flg']").val()=="true" && "nextCity" == type){
		var city_id = $("select[@name='gwShare_cityId']").val();
		if("-1" == city_id || "00" == city_id){
			$("#nextCityDiv").hide();
			return false;
		}
	}
	
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue || ("sx_lt" == area && "city" == type && lineData.length == 1)){
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
	//�ӱ�ѡ���ȫ��������ʡ���ĵ��������ʾ�ؼ�����
	if($("input[@name='hblt_SpeedTest_flg']").val()=="true" && "nextCity" == type){
		$("#nextCityDiv").show();
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

function gwShare_setOnlyGaoji(){
	$("input[@name='gwShare_gaoji_value']").val("");
	gwShare_queryChange("2");
	$("input[@name='gwShare_jiadan']").css("display","none");
}

function gwShare_setUpImport(){
	$("input[@name='gwShare_upimport_value']").val("");
	$("input[@name='gwShare_up_import']").css("display","");
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
function spellSQL(obj) {
	//gwShare_spellSQL();
	if(!do_test())
	{
		var chbx_isMactchSQL = document.getElementsByName("chbx_isMactchSQL");
		chbx_isMactchSQL[0].checked=false;
		return;
	}
	if(obj.checked) {
		$("tr[@id='gwShare_tr25']").css("display","");
		$("tr[@id='gwShare_tr26']").css("display","");
		gwShare_spellSQL();
    } else {
    	$("tr[@id='gwShare_tr25']").css("display","none");
		$("tr[@id='gwShare_tr26']").css("display","none");
		$("textarea[@name='gwShare_matchSQL1']").val("");
		$("textarea[@name='gwShare_matchSQL']").val("");
    }
}

function gwShare_spellSQL(){
	var url = "<s:url value='/gwms/share/gwDeviceQuery!queryDeviceListSQL.action'/>";
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
   
	
	var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
    var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
    var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
    var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
    var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
    var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
    var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
    var refresh= new Date().getTime();
    var gw_type= <%=gw_type%>;
    var iscqsoft1 = "";
    //����������������IP��Χ��ѯ
    var gwShare_start_ip=$("input[@name='gwShare_start_ip']").val();
	var gwShare_end_ip=$("input[@name='gwShare_end_ip']").val();
	if(typeof(gwShare_start_ip) == "undefined")
	{
		gwShare_start_ip = "";
	}
	if(typeof(gwShare_end_ip) == "undefined")
	{
		gwShare_end_ip = "";
	}
    try {
    	if(typeof(iscqsoft) != 'undefined' && true==iscqsoft){
        	gwShare_cityId = cqSoftCitys;
        	iscqsoft1 = "true";
    	}
    }
    catch (e) {
    }
    $.post(url,{
		gwShare_queryResultType:gwShare_queryResultType,
		gwShare_queryType:gwShare_queryType,
		gwShare_cityId:gwShare_cityId,
		gwShare_nextCityId:gwShare_nextCityId,
		gwShare_onlineStatus:gwShare_onlineStatus,
		gwShare_vendorId:gwShare_vendorId,
		gwShare_deviceModelId:gwShare_deviceModelId,
		gwShare_devicetypeId:gwShare_devicetypeId,
		gwShare_bindType:gwShare_bindType,
		gwShare_deviceSerialnumber:gwShare_deviceSerialnumber,
		refresh:refresh,
		gw_type:gw_type,
		iscqsoft:iscqsoft1,
		gwShare_start_ip:gwShare_start_ip,
		gwShare_end_ip:gwShare_end_ip
    },function(ajax){
    	$("textarea[@name='gwShare_matchSQL1']").val(ajax);
    });    	
}

function setIsGJ(value){
	$("input[@name='isShowGJ']").val(value);
}


var cqSoftCitys = "";
function ChkAllCity(){
	if ($('#cityAll').attr('checked')) {
		cqSoftCitys = "";
		$("input:checkbox[name='city']").each(function() { // ����name=test�Ķ�ѡ��
			$(this).attr("checked",true);
			cqSoftCitys = cqSoftCitys+""+$(this).val()+",";
		});
	}
	else{
		cqSoftCitys = "";
		$("input:checkbox[name='city']").each(function() { // ����name=test�Ķ�ѡ��
			$(this).attr("checked",false);
		});
	}
}

function ChkCity(){
	cqSoftCitys = "";
	var i = 0;
	$("input:checkbox[name='city']:checked").each(function() { // ����name=test�Ķ�ѡ��
		i++;
		cqSoftCitys = cqSoftCitys+""+$(this).val()+",";  // ÿһ����ѡ�����ֵ
	});
	if(i>=40){
		$("#cityAll").attr("checked",true);
	}
}

//ת��IP��ַΪ����
function convertIP(ip)
{
	var ipArray = ip.split(".");
	var newIP = "";
	for(var i=0;i<ipArray.length;i++)
	{
		if(ipArray[i].length==1){
			ipArray[i] = "00"+ipArray[i];
		}else if(ipArray[i].length==2){
			ipArray[i] = "0"+ipArray[i];
		}
		newIP += ipArray[i] + ".";
	}
	return newIP.substring(0,newIP.length-1);
}

function checkIP(ip)
{
	var exp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	var rev = false;
	if(!ip.match(exp)){
		alert("IP��ַ��ʽ����ȷ");
	}else{
		rev = true;
	}
	return rev;
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
<input type="hidden" name="gwShare_upimport_value" value="none" />
<input type="hidden" name="isBatch" value="<%=isBatch %>" />
<input type="hidden" name="batchType" 	value="0">
<input type="hidden" name="isShowGJ" value="0" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr><th colspan="6" id="gwShare_thTitle" STYLE="display:">�� �� �� ѯ</th></tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr11" STYLE="display:">
					<td colspan="6" align="center" width="100%">
						<div>
							<input type="input" class="bk" name="gwShare_queryParam" size="64" maxlength="64"/>
							<!-- <label style="CURSOR:hand" onclick="javascript:gwShare_queryChange('2');">�߼���ѯ</label> -->
							<br /> 
							<div id="gwShare_msgdiv" STYLE="display:none" > 
									
							</div>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr12" STYLE="display:">
					<td colspan="6" align="center" width="100%">
						<input type="radio" class=jianbian name="gwShare_queryField" value="deviceSn" checked onclick="gwShare_queryField_selected(this)"/> �豸���к� &nbsp;&nbsp;
						<input type="radio" class=jianbian name="gwShare_queryField" value="deviceIp" onclick="gwShare_queryField_selected(this)"/> �豸IP &nbsp;&nbsp;
						<input type="radio" class=jianbian name="gwShare_queryField" value="username" onclick="gwShare_queryField_selected(this)"/>
						<ms:inArea areaCode="sx_lt" >
							Ψһ��ʶ
						</ms:inArea>
						<ms:inArea areaCode="sx_lt" notInMode="true">
							LOID
						</ms:inArea>
						&nbsp;&nbsp;
						<input type="radio" class=jianbian name="gwShare_queryField" value="kdname" onclick="gwShare_queryField_selected(this)"/> �����˺� &nbsp;&nbsp;
						<input type="radio" class=jianbian name="gwShare_queryField" value="voipPhoneNum" onclick="gwShare_queryField_selected(this)"/> VOIP�绰���� &nbsp;&nbsp;
						<!-- <input type="radio" class=jianbian name="gwShare_queryField" value="all" onclick="gwShare_queryField_selected(this)"/> ȫ �� &nbsp;&nbsp; -->
					</td>
				</tr>
				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="display:none">
					<TD align="right" class=column width="15%">��    ��</TD>
					<TD align="left" width="35%">
					<div id="commonCity" style="display: ">
						<div style="float: left;">
							<select name="gwShare_cityId" class="bk" onchange="gwShare_change_select('nextCity','-1')">
								<option value="-1">==ȫ��==</option>
							</select>
						</div>
						<div style="float: left; display: none;" id="nextCityDiv">
							<select name="gwShare_nextCityId" class="bk" >
								<option value="-1">==ȫ��==</option>
							</select>
						</div>
					</div>
					<div id="cqsoftCity" style="display: none">
						<INPUT onclick="ChkAllCity()" type="checkbox" value="" id="cityAll">ȫѡ
						<INPUT type="checkbox" value="00" name="city" onclick="ChkCity()">ʡ����
						<INPUT type="checkbox" value="QL0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CA0" name="city" onclick="ChkCity()">�ɽ
						<INPUT type="checkbox" value="QP0" name="city" onclick="ChkCity()">��ˮ
						<INPUT type="checkbox" value="QQ0" name="city" onclick="ChkCity()">ǭ��
						<INPUT type="checkbox" value="QS0" name="city" onclick="ChkCity()">ʯ��
						<INPUT type="checkbox" value="QX0" name="city" onclick="ChkCity()">��ɽ
						<INPUT type="checkbox" value="WC0" name="city" onclick="ChkCity()">�ǿ�
						<INPUT type="checkbox" value="WF0" name="city" onclick="ChkCity()">���
						<INPUT type="checkbox" value="WK0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="WL0" name="city" onclick="ChkCity()">��ƽ
						<INPUT type="checkbox" value="WS0" name="city" onclick="ChkCity()">��ɽ
						<INPUT type="checkbox" value="WW0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="WX0" name="city" onclick="ChkCity()">��Ϫ
						<INPUT type="checkbox" value="WY0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="WZ0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CY0" name="city" onclick="ChkCity()">������
						<INPUT type="checkbox" value="CZ0" name="city" onclick="ChkCity()">�潭
						<INPUT type="checkbox" value="FF0" name="city" onclick="ChkCity()">�ᶼ
						<INPUT type="checkbox" value="FH0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="FL0" name="city" onclick="ChkCity()">�ϴ�
						<INPUT type="checkbox" value="FW0" name="city" onclick="ChkCity()">��¡
						<INPUT type="checkbox" value="CB0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CC0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CD0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CE0" name="city" onclick="ChkCity()">�뽭
						<INPUT type="checkbox" value="CF0" name="city" onclick="ChkCity()">�山
						<INPUT type="checkbox" value="CG0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CH0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CI0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CJ0" name="city" onclick="ChkCity()">��������
						<INPUT type="checkbox" value="CK0" name="city" onclick="ChkCity()">�ϴ�
						<INPUT type="checkbox" value="CL0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CM0" name="city" onclick="ChkCity()">��ɿ�
						<INPUT type="checkbox" value="CN0" name="city" onclick="ChkCity()">�ϰ�
						<INPUT type="checkbox" value="CP0" name="city" onclick="ChkCity()">ɳƺ��
						<INPUT type="checkbox" value="CR0" name="city" onclick="ChkCity()">�ٲ�
						<INPUT type="checkbox" value="CS0" name="city" onclick="ChkCity()">����
						<INPUT type="checkbox" value="CT0" name="city" onclick="ChkCity()">ͭ��
						<INPUT type="checkbox" value="CU0" name="city" onclick="ChkCity()">����
					</div>
					</TD>
					<TD align="right" class=column width="15%" >����״̬</TD>
					<TD width="35%"  colspan="3">
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
					<TD align="right" class=column width="15%" >�豸�ͺ�</TD>
					<TD align="left" width="35%"  colspan="3">
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
					<TD align="right" class=column width="15%" >�Ƿ��</TD>
					<TD width="35%" colspan="3">
						<select name="gwShare_bindType" class="bk">
							<option value="-1">==ȫ��==</option>
							<option value="0">δ��</option>
							<option value="1">�Ѱ�</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr24"  STYLE="display:none" >
					<TD align="right" class=column width="15%">�豸���к�</TD>
					<TD width="35%">
						<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="25" class="bk"/>
						<font color="red">*��ģ��ƥ��</font>
					</TD>
					<!-- ����������������Ҫ���ܸ���IP�β�ѯ -->
					<!-- �����������У�Ϊ����Ӧ�����Ĳ�ѯ�ֶ� -->
					<%  if("cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && "1".equals(request.getParameter("CQ_softUp"))){%>
					<TD align="right" class=column width="15%" ><font id="gwShare_IP"  >�豸IP��Χ</font></TD>
					<TD width="35%" >
						<input type="text" name="gwShare_start_ip" value="" maxlength="15"  class="bk" size="25" /> -
						<input type="text" name="gwShare_end_ip" value="" maxlength="15"  class="bk" size="25" />
						<font color="red">*�ֱ�������ʼIP�ͽ���IP</font>
					</TD>
					<% } else if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) && "true".equals(isOnline_time)){%>
					<TD align="right" class=column width="15%" >����ʱ��(��)</TD>
					 <TD width="35%" >
						<input type="text" name="online_time" value="" maxlength="15"  class="bk" size="25" />
					</TD>
					<% }else{%>
					 <TD align="right" class=column width="15%" > </TD>
					 <TD width="35%"  width="15%" > </TD>
					<%} %>
					<TD align="right" class=column width="15%" id="gwShare_td1"  STYLE="display:none"><font >�Զ����ѯ</font></TD>
					<TD width="35%" STYLE="display:none">
						<input type="checkbox" name="chbx_isMactchSQL" id="chbx_isMactchSQL"  onclick="spellSQL(this);">
					</TD>
				</TR>
				 
				<TR bgcolor="#FFFFFF" id="gwShare_tr25"  STYLE="display:none">
					<TD align="right" class=column width="15%">�����ɵ�SQL</TD>
					<TD width="35%" colspan="5">
					    <textarea name="gwShare_matchSQL1" cols="100" rows="4" class=bk readonly="readonly">
					    </textarea>
						<font color="red">*�����ɵ�SQL</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr26"  STYLE="display:none">
					<TD align="right" class=column width="15%">�Զ����ѯ</TD>
					<TD width="35%" colspan="5">
					    <textarea name="gwShare_matchSQL" cols="100" rows="4" class=bk></textarea>
						<font color="red">*����SQL</font>
					</TD>
				</TR>
				<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="5" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="5" CLASS="green_foot">
					
					
					 <%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
					 <div id="batch_test" style="display:none">
					    1����Ҫ������ļ���ʽ�����ı��ļ�����txt��ʽ ��
					    <br>
					    2���ļ��ĵ�һ��Ϊ�����У�����devSerNo�������ߡ�username��������devSerNoΪ�豸���кţ�username�������˺�
					 </div>
					 <div>
					    1����Ҫ������ļ���ʽ����Excel���ı��ļ�����txt��ʽ �� <br>
					    2���ļ��ĵ�һ��Ϊ�����У������û��˺š������ߡ��豸���кš���
					 </div>
					 <%}else{ %>
					 1����Ҫ������ļ���ʽ����Excel���ı��ļ�����txt��ʽ �� <br>
					2���ļ��ĵ�һ��Ϊ�����У������û��˺š������ߡ��豸���кš���
					<%} %>
					 <br>
					3���ļ�ֻ��һ�С�
					 <br>
					4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="6" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian style="CURSOR:pointer;display: none" 
						onclick="javascript:gwShare_queryChange('1');" name="gwShare_jiadan" value="�򵥲�ѯ" />
						<ms:inArea areaCode="js_dx" notInMode="true">
							<input type="button" class=jianbian style="CURSOR:pointer;display:none " 
							onclick="javascript:gwShare_queryChange('2');" name="gwShare_gaoji" value="�߼���ѯ" />
						</ms:inArea>
						<input type="button" class=jianbian style="CURSOR:pointer;display:none" onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="�����ѯ" />
						
						<input type="button" class=jianbian style="CURSOR:pointer;display: none" 
						onclick="javascript:gwShare_queryChange('4');" name="gwShare_up_import" value="���������ѯ" />
						
						<input type="button" onclick="javascript:do_query()" class=jianbian 
						name="gwShare_queryButton" value=" �� ѯ " />
						<input type="button" class=jianbian onclick="javascript:gwShare_revalue()" 
						name="gwShare_reButto" value=" �� �� " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
</form>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ɼ�LAN1������</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/JavaScript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/JavaScript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/JavaScript" src="<s:url value="/Js/commonUtil.js"/>"></script>
</head>
<script language="JavaScript">

	function queryChange(change){
		switch (change){
			case "2":
				$("input[@name='queryType']").val("2");
				$("input[@name='jiandan']").css("display","");
				$("input[@name='gaoji']").css("display","none");
				$("input[@name='import']").css("display","");
				
				$("th[@id='thTitle']").html("�� �� �� ��");
				$("tr[@id='tr11']").css("display","none");
				$("tr[@id='tr12']").css("display","none");
				$("tr[@id='tr21']").css("display","");
				$("tr[@id='tr22']").css("display","");
				$("tr[@id='tr23']").css("display","");
				$("tr[@id='tr31']").css("display","none");
				$("tr[@id='tr32']").css("display","none");
				$("input[@name='queryButton']").val(" �� �� ");
				
				//���������
				change_select("city","-1");
				change_select("vendor","-1");
				
				break;
			case "3":
				$("input[@name='queryType']").val("3");
				$("input[@name='jiandan']").css("display","");
				$("input[@name='gaoji']").css("display","");
				$("input[@name='import']").css("display","none");
				
				$("th[@id='thTitle']").html("�� �� �� ��");
				$("tr[@id='tr11']").css("display","none");
				$("tr[@id='tr12']").css("display","none");
				$("tr[@id='tr21']").css("display","none");
				$("tr[@id='tr22']").css("display","none");
				$("tr[@id='tr23']").css("display","none");
				$("tr[@id='tr31']").css("display","");
				$("tr[@id='tr32']").css("display","");
				$("input[@name='queryButton']").val("�� ��");
				break;
			case "1":
				$("input[@name='queryType']").val("1");
				$("input[@name='jiandan']").css("display","none");
				$("input[@name='gaoji']").css("display","");
				$("input[@name='import']").css("display","");
				
				$("th[@id='thTitle']").html("�� ̨ �� ѯ");
				$("tr[@id='tr11']").css("display","");
				$("tr[@id='tr12']").css("display","");
				$("tr[@id='tr21']").css("display","none");
				$("tr[@id='tr22']").css("display","none");
				$("tr[@id='tr23']").css("display","none");
				$("tr[@id='tr31']").css("display","none");
				$("tr[@id='tr32']").css("display","none");
				$("input[@name='queryButton']").val(" �� �� ");
				break;
			default:
				break;
		}
	}
	
	/*------------------------------------------------------------------------------
	//������:		change_select
	//����  :	type 
		            vendor      �����豸����
		            deviceModel �����豸�ͺ�
		            deviceType  �����豸�汾
	//����  :	����ҳ������̡��ͺż�����
	//����ֵ:		
	//˵��  :	
	//����  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
				$.post(url,{
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='cityId']"),selectvalue);
				});
				break;
			case "nextCity":
				var city_id = $("select[@name='cityId']").val();
				if("-1"==city_id){
					$("select[@name='nextCityId']").html("<option value='-1'>==ȫ��==</option>");
					break;
				}
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChildCore.action"/>";
				$.post(url,{
					gwShare_cityId:city_id
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='nextCityId']"),selectvalue);
				});
				break;
			case "vendor":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
				$.post(url,{
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='vendorId']"),selectvalue);
					$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
					$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				});
				break;
			case "deviceModel":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
				var vendorId = $("select[@name='vendorId']").val();
				if("-1"==vendorId){
					$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
					$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='deviceModelId']"),selectvalue);
					$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				});
				break;
			case "deviceType":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
				var vendorId = $("select[@name='vendorId']").val();
				var deviceModelId = $("select[@name='deviceModelId']").val();
				if("-1"==deviceModelId){
					$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId,
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='deviceTypeId']"),selectvalue);
				});
				break;	
			default:
				alert("δ֪��ѯѡ�");
				break;
		}	
	}
	
	/*------------------------------------------------------------------------------
	//������:		
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
	function parseMessage(type,ajax,field,selectvalue){
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
	//������:		��дreset
	//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ��3�������ѯ
	//����  :	��ҳ���������
	//����ֵ:		ҳ������
	//˵��  :	
	//����  :	Create 2010-4-26 of By qxq
	------------------------------------------------------------------------------*/
	function revalue(){
		var queryType = $("input[@name='queryType']").val();
		if("1"==queryType){
			$("input[@name='queryField']").get(0).checked = true;
			$("input[@name='queryParam']").val("");
		}else if("2"==queryType){
			$("select[@name='cityId']").attr("value",'-1')
			$("select[@name='onlineStatus']").attr("value",'-1')
			$("select[@name='vendorId']").attr("value",'-1')
			change_select('deviceModel','-1');
		}
	}
	
	function do_work(){
		// ��ť�һ�
		$("input[@name='queryButton']").attr("disabled", true);
		
		if(!do_test()){
			$("input[@name='queryButton']").attr("disabled", false);
			return;
		}
		setTimeout("queryDevice()", 2000);
	}
	
	//��֤��������ĳ����Ƿ�Ϸ�
	function do_test()
	{
		var queryType = $("input[@name='queryType']").val();
		// ��̨�ɼ�
		if(queryType == "1"){
			
			//��ȡ��������ݣ�trimһ��
			var queryParam = $.trim($("input[@name='queryParam']").val());		
			if(0 == queryParam.length){
				alert("������ɼ�������");
				$("input[@name='queryParam']").focus();
				return false;
			}
			
			//��ȡѡ�������
			var queryFields = document.getElementsByName("queryField");
			
			//"�豸���к�"��ѡ��
			if(queryFields[0].checked)
			{
				if(queryParam.length<6 && queryParam.length>0){
					alert("�������������6λ�豸���кŽ��в�ѯ��");
					$("input[@name='queryParam']").focus();
					return false;
				}
			}//"�豸IP"��ѡ��
			else if(queryFields[1].checked)
			{
				if(!reg_verify(queryParam))
				{
					alert("������Ϸ���IP��ַ��");
					$("input[@name='queryParam']").focus();
					return false;
				}
			}//"�û��ʺ�"��ѡ��   
			else if(queryFields[2].checked)
			{
				if(queryParam.length<6 && queryParam.length>0){
					alert("�������������6λLOID���в�ѯ��");
					$("input[@name='queryParam']").focus();
					return false;
				}
			}
		}
		else if(queryType == "2"){
			
			var cityId = $("select[@name='cityId']").val();
			var nextCityId = $("select[@name='nextCityId']").val();
			if("-1" == nextCityId && ("-1" == cityId || "00" == cityId)){
				alert("����ѡ��һ���������أ�");
				return false;
			}
		}
		else if(queryType == "2"){
			var fileName = $("input[@name='gwShare_fileName']").val();
			if("" == fileName){
				alert("�����ϴ��ļ���");
				return false;
			}
			if ("xls" != fileName[fileName.length - 1] && "XLS" != fileName[fileName.length-1] && 
					"txt" != fileName[fileName.length-1] && "TXT" != fileName[fileName.length-1]) {
				valert("��֧�ֺ�׺Ϊxls��txt���ļ�");
				return false;
			}
		}
		return true;
	}
	
	/* reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
	������򷵻�true�����򣬷���false��*/
	function reg_verify(addr)
	{
		//������ʽ
	    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

	    if(addr.match(reg)){
	    	//IP��ַ��ȷ
	        return true;
	    }else{
	    	//IP��ַУ��ʧ��
	         return false;
	    }
	}
	
	/*------------------------------------------------------------------------------
	//������:		queryChange
	//����  :	change 1:��̨�ɼ���2:�߼�����
	//����  :	���ݴ���Ĳ���������ʾ�Ľ���
	//����ֵ:		��������
	//˵��  :	
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=450; 
		var queryType = $("input[@name='queryType']").val();
		
		// ��̨�ɼ�
		if("1" == queryType){
			
			// У���豸�Ƿ����
			var url="<s:url value='/gwms/resource/superGatherLanTask!validateSingleGather.action'/>";
			
			$.post(url,{
				queryField:$("input[@name='queryField'][@checked]").val(),
				queryParam:$.trim($("input[@name='queryParam']").val())
			},function(ajax){
				if("empty" == ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert("��ѯ�����豸��Ϣ�����������룡");
					return;
				}
				
				if("many" == ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert("�����������Բ�ѯ�������豸��Ϣ�����������룡");
					return;
				}
				
				var url1="<s:url value='/gwms/resource/superGatherLanTask!singleSuperGather.action'/>?deviceId=" + ajax;
				window.showModalDialog(url1 ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
				$("input[@name='queryButton']").attr("disabled", false);
			});
		}
		
		// �߼�����
		if("2" == queryType){
			var url="<s:url value='/gwms/resource/superGatherLanTask!createTask.action'/>";
			var cityId = "";
			var preCityId = $("select[@name='cityId']").val();
			var nextCityId = $("select[@name='nextCityId']").val();
			
			cityId = nextCityId;
			if("-1" == nextCityId){
				cityId = preCityId;
			}
			
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			var deviceTypeId = $("select[@name='deviceTypeId']").val();
			var onlineStatus = $("select[@name='onlineStatus']").val();
			$.post(url,{
				cityId:cityId,
				vendorId:vendorId,
				deviceModelId:deviceModelId,
				deviceTypeId:deviceTypeId,
				onlineStatus:onlineStatus
			},function(ajax){
				// ����ʧ��
				if("" != ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert(ajax);
					return;
				}				
				alert("�����Ƴɹ���");
				// ���Ƴɹ���ת����ҳ��
				var url1 = "<s:url value='/gwms/resource/SuperGatherLanTaskQuery.jsp'/>";
				window.open(url1,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
				$("input[@name='queryButton']").attr("disabled", false);
				//window.showModalDialog(url1 ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			});
			
		}
		
		// ���붨��
		if("3" == queryType){
			var url="<s:url value='/gwms/resource/superGatherLanTask!createFileTask.action'/>";
			var fileName = $("input[@name='gwShare_fileName']").val();
			$.post(url,{
				fileName:fileName
			},function(ajax){
				// ����ʧ��
				if("" != ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert(ajax);
					return;
				}
				alert("�����Ƴɹ���");
				// ���Ƴɹ���ת����ҳ��
				var url1 = "<s:url value='/gwms/resource/SuperGatherLanTaskQuery.jsp'/>";
				window.open(url1,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
				$("input[@name='queryButton']").attr("disabled", false);
				//window.showModalDialog(url1 ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			});
		}
	}
</script>

<form name="selectForm" action="" target="dataForm">

<input type="hidden" name="queryType" value="1" />
<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr><th colspan="6" id="thTitle" style="display:">�� ̨ �� ��</th></tr>
				<tr bgcolor="#FFFFFF" id="tr11" style="display:">
					<td colspan="6" align="center" width="100%">
						<div>
							<input class="bk" name="queryParam" size="64" maxlength="64"/>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF" id="tr12" style="display:">
					<td colspan="6" align="center" width="100%">
						<input type="radio" name="queryField" value="deviceSn" checked /> �豸���к� &nbsp;&nbsp;
						<input type="radio" name="queryField" value="deviceIp"/> �豸IP &nbsp;&nbsp;
						<input type="radio" name="queryField" value="username"/> LOID &nbsp;&nbsp;
						<input type="radio" name="queryField" value="kdname"/> ����˺� &nbsp;&nbsp;
						<input type="radio" name="queryField" value="voipPhoneNum"/> VOIP�绰���� &nbsp;&nbsp;
					</td>
				</tr>
				<tr bgcolor="#ffffff" id="tr21" style="display:none">
					<td align="right" class=column width="15%">��    ��</td>
					<td align="left" width="35%">
						<div style="float: left;">
							<select name="cityId" class="bk" onchange="change_select('nextCity','-1')">
								<option value="-1">==ȫ��==</option>
							</select>
						</div>
						
					</td>
					<td align="right" class=column width="15%" >�¼�����</td>
					<td align="left" width="35%"  colspan="3">
						<div style="float: left;" id="nextCityDiv">
							<select name="nextCityId" class="bk" >
								<option value="-1">==ȫ��==</option>
							</select>
						</div>
					</td>
				</tr>
				<tr bgcolor="#ffffff" id="tr22" style="display:none">
					<td align="right" class=column width="15%">��    ��</td>
					<td width="35%">
						<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
							<option value="-1">==��ѡ��==</option>
						</select>
					</td>
					<td align="right" class=column width="15%" >�豸�ͺ�</td>
					<td align="left" width="35%"  colspan="3">
						<select name="deviceModelId" class="bk" onchange="change_select('deviceType','-1')">
							<option value="-1">����ѡ����</option>
						</select>
					</td>
				</tr>
				<tr bgcolor="#ffffff" id="tr23"  style="display:none">	
					<td align="right" class=column width="15%">�豸�汾</td>
					<td width="35%">
						<select name="deviceTypeId" class="bk"">
							<option value="-1">����ѡ���豸�ͺ�</option>
						</select>
					</td>	
					<td align="right" class=column width="15%" >����״̬</td>
					<td width="35%"  colspan="3">
						<select name="onlineStatus" class="bk">
							<option value="-1">==ȫ��==</option>
							<option value="0">����</option>
							<option value="1">����</option>
						</select>
					</td>
				</tr>
				<tr id="tr31" bgcolor="#ffffff"  style="display:none">
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="5" width="85%">
						<div id="importusername">
							<iframe name="loadform" frameborder=0 scrolling=no src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
							<input type="hidden" name="gwShare_fileName" value=""/>
						</div>
					</td>
				</tr>
				<tr id="tr32" style="display:none">
					<td class="green_foot" align="right">ע������</td>
					<td colspan="5" class="green_foot">
						1����Ҫ������ļ���ʽ����xls���ı��ļ�����txt��ʽ ��
						 <br>
						2���ļ��ĵ�һ��Ϊ�����У������û��˺š������ߡ��豸���кš���
						 <br>
						3���ļ�ֻ��һ�С�
						 <br>
						4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td colspan="6" align="right" class="green_foot" width="100%">
						<input type="button" style="cursor:pointer;display: none" onclick="javascript:queryChange('1');" name="jiandan" value="��̨�ɼ�" />
						<input type="button" style="cursor:pointer;" onclick="javascript:queryChange('2');" name="gaoji" value="�߼�����" />						
						<input type="button" style="cursor:pointer;" onclick="javascript:queryChange('3');" name="import" value="���붨��" />
						<input type="button" onclick="javascript:do_work()" name="queryButton" value=" �� �� " />
						<input type="button" onclick="javascript:revalue()"name="rebutto" value=" �� �� " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
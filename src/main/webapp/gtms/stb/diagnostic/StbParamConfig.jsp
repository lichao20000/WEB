<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">

	var gw_type = "<%=request.getParameter("gw_type")%>";
	
	$(function(){
		gwShare_setGaoji();
		changeSelect("city", "-1");
	});
	
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
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
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value="/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action"/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			$("input[@name='deviceId']").val(returnVal[0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
  	function ExecMod(){ 
  		if(CheckForm()){
  			$('#ping_btn').attr("disabled",true);
  			$("div[@id='div_ping']").html("�����·������Ժ�...");
			var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!paramConfig.action'/>";
			var servName = $("#servName").val();
			var servPasswd = $("#servPasswd").val();
			var deviceId = $("input[@name='deviceId']").val();
			$.post(url,{
				deviceId:deviceId,
				servName:servName,
				servPasswd:servPasswd,
				gw_type:gw_type
			},function(ajax){
			    $("div[@id='div_ping']").html("");
				$("div[@id='div_ping']").append(ajax);
				$('#ping_btn').attr("disabled",false);
			});	
		}else{
			return false;
		}	
    }

	function CheckForm(){
		var servName = $("#servName").val();
		var servPasswd = $("#servPasswd").val();
		
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("���ѯ�豸��");
			return false;
	    }
		if(servName==""){
			alert("������ҵ���˺ţ�");
			return false;
	    }
        if(servPasswd==""){
        	alert("������ҵ�����룡");
			return false;
		}
       
		return true;					
	}
	
	
	
	function changeSelect(type, selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
					$("select[@name='ipType']").html("<option value='-1'>==��ѡ����==</option>");
					$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
				});
				break;
				
			case "ipType":
				var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!getIpTypeByCityId.action'/>";
				var cityId = $("select[@name='cityId']").val();
				
				if("-1"==cityId){
					$("select[@name='ipType']").html("<option value='-1'>==��ѡ����==</option>");
					$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
					break;
				}
				
				$.post(url,{
					cityId:cityId
				},function(ajax){
					if("" == ajax){
						$("select[@name='ipType']").html("<option value='-1'>==δ����������==</option>");
						$("select[@name='ipValue']").html("<option value='-1'>==δ����������==</option>");
						$("input[name=hostIp]").val("");
					}else{
						gwShare_parseMessage(ajax,$("select[@name='ipType']"),selectvalue);
						$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
					}
				});
				break;
				
			case "ipValue":
				var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!getIpByIpType.action'/>";
				var cityId = $("select[@name='cityId']").val();
				var ipType = $("select[@name='ipType']").val();
				
				if("-1"==ipType){
					$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
					break;
				}
				$.post(url,{
					cityId:cityId,
					ipType:ipType
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='ipValue']"),selectvalue);
				});
				break;	
				
			default:
				alert("δ֪��ѯѡ�");
				break;
		}	
	}
	
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
				alert("����ʧ�ܣ�");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
	

</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	����ǰ��λ�ã���������
		</TD>
	</TR>
</TABLE>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR >
		<td  >
			<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>
	<tr>
		<td bgcolor="#FFFFFF">
			<table width="100%"  class="querytable">
				<FORM NAME="frm" METHOD="POST" ACTION="" onSubmit="return CheckForm();">
					<input type="hidden" value="" name="deviceId"/>
					<TR  >
						<Td colspan="4" class="title_1">
							����ѡ��
						</Td>
					</TR>
					<tr id="line1" style="display:">
						<td nowrap class="title_2" width="10%">
								ҵ���˺�
						</td>
						<td width="40%">
							<input type="text" id="servName" class="bk" size="18"
								value="" >&nbsp;
						</td>
						<td nowrap class="title_2" width="10%">
								ҵ������
						</td>
						<td width="40%">
							<input type="text" id="servPasswd" class="bk"
								size="16" value="">
						</td>
					</tr>
					<tr >
						<td class="foot" colspan="4">
							<div align="right">
								<button id="ping_btn" onclick="ExecMod()"> �����·�</button>
							</div>
						</td>
					</tr>
					<TR  >
						<TH colspan="4">
							�·����
						</TH>
					</TR>
					<TR >
						<td colspan="4" valign="top">
							<div id="div_ping"
								style="width:100%; height:120px; z-index:1; top: 100px;"></div>
						</td>
					</TR>
				</FORM>
			</table>
		</td>
	</tr>
</table>
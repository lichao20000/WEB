<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet"
	type="text/css">
<lk:res />

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
	var pwd = '<%=request.getParameter("pwd") %>';
	
	$(function(){
		gwShare_setGaoji();
	});
	var deviceId = "";
	
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			//$("input[@name='deviceId']").val(returnVal[0]);
			deviceId = returnVal[2][i][0];
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
			//$("td[@id='tdDeviceSn']").append("-");
			//$("td[@id='tdDeviceSn']").append();
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
			$("td[@id='tdDeviceIP']").html(returnVal[2][i][3]);
			$("tr[@id='configtr']").show();
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
		var url="<s:url value='/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action'/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if (returnVal){
			deviceId = returnVal[0];
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	function cmpstVideoAndAspRatioCfg(){
		$('#cmpstVideoAndAspRatioCfg').addClass("hover");
		$('#config').removeClass("hover");
		if(CheckForm()){
			//(1)������ť�û�
			$("#cmpstVideoAndAspRatioCfg").removeAttr("onclick");
			$("li[@id='cmpstVideoAndAspRatioCfg']");
			$("tr[@id='config']").css("display","");
		    $("div[@id='config']").html("���ڲ��������Ժ�...");
			var url = "<s:url value='/gtms/stb/config/baseConfig!cmpstVideoAndAspRatio.action'/>";
			$.post(url,{
				deviceId:deviceId
			},function(ajax){
				$("div[@id='config']").html("");
				$("div[@id='config']").append(ajax);
				//��2������ť�ָ�
				$("#cmpstVideoAndAspRatioCfg").replaceWith("<li id='cmpstVideoAndAspRatioCfg' class='out' onclick='cmpstVideoAndAspRatioCfg();'>��Ƶ�����ʽ������ģʽ����</li>"); 
			});
		}
	}
	//��������
	function baseConfig(){
		$('#baseConfig').addClass("hover");
		$('#config').removeClass("hover");
		if(CheckForm()){
			//block();
			//(1)������ť�û�
			//$("#baseConfig").attr("onclick","");
			$("#baseConfig").removeAttr("onclick");
			$("li[@id='baseConfig']");
			$("tr[@id='config']").css("display","");
		    $("div[@id='config']").html("���ڲ��������Ժ�...");
			var url = "<s:url value='/gtms/stb/config/baseConfig!getBaseConfig.action'/>";
			//alert(pwd);
			$.post(url,{
				deviceId:deviceId,
				pwd:pwd
			},function(ajax){
				$("div[@id='config']").html("");
				$("div[@id='config']").append(ajax);
				//��2������ť�ָ�
				$("#baseConfig").replaceWith("<li id='baseConfig' class='out' onclick='baseConfig();'>��ȡ��������</li>"); 
				//$.unblockUI();
			});
		}
	}
	
	//��ʷ������Ϣ
	function historyConfig(){
		$('#historyConfig').addClass("hover");
		$('#config').removeClass("hover");
		if(CheckForm()){
			//block();
			//(1)������ť�û�
			//$("#historyConfig").attr("onclick","");
			$("#historyConfig").removeAttr("onclick");
			$("li[@id='historyConfig']");
			$("tr[@id='config']").css("display","");
		    $("div[@id='config']").html("���ڲ��������Ժ�...");
			var url = "<s:url value='/gtms/stb/config/baseConfig!getHistoryConfig.action'/>";
			//alert(pwd);
			$.post(url,{
				deviceId:deviceId,
				pwd:pwd
			},function(ajax){
				$("div[@id='config']").html("");
				$("div[@id='config']").append(ajax);
				//��2������ť�ָ�
				$("#historyConfig").replaceWith("<li id='historyConfig' class='out' onclick='historyConfig();'>��ʷ������Ϣ</li>"); 
				//$.unblockUI();
			});
		}
	}
	
	function config(){
		$('#baseConfig').removeClass("hover");
		$('#config').addClass("hover");
	}
	function CheckForm(){
		if(deviceId==""){
			alert("���ѯ�豸��");
			return false;
	    }
		return true;
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã��豸���
		</TD>
	</TR>
</TABLE>
<table width="98%" class="querytable" align="center">
	<TR>
		<td colspan="4">
			<%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>
	<TR id="trDeviceResult" style="display: none">
		<td nowrap class="title_2" width="15%">
			�豸���к�
		</td>
		<td id="tdDeviceSn" width="35%">
		</td>
		<td nowrap class="title_2" width="15%">
			����
		</td>
		<td id="tdDeviceCityName" width="35%">
		</td>
	</tr>
	<tr id="trDeviceResult" style="display: none">
		<td nowrap class="title_2" width="15%">
			�豸IP
		</td>
		<td id="tdDeviceIP" width="35%">
		</td>
		<td nowrap class="title_2" width="15%">
		</td>
		<td width="35%">
		</td>
	</tr>
	<tr id="configtr" style="display: none">
		<td colspan="4">
			<div class="tab_Menubox" class="center" style="width: 98%">
				<ul>
					<li id="baseConfig" class="out" onclick="baseConfig();">
						��ȡ��������
					</li>
					<!--  <li class="out" id="config" onclick="config();">
						��չ����
					</li>-->
					<li id="cmpstVideoAndAspRatioCfg" class="out" onclick="cmpstVideoAndAspRatioCfg();">
						��Ƶ�����ʽ������ģʽ����
					</li>
					<li id="historyConfig" class="out" onclick="historyConfig();">
						��ʷ������Ϣ
					</li>
				</ul>
			</div>
		</td>
	</tr>
	<tr id="config" style="display: none">
		<td colspan="4">
			<div id="config" style="width: 100%; z-index: 1; top: 100px">
				���ڲ�ѯ�����Ե�....
			</div>
		</td>
	</tr>
</table>


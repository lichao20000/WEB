<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

	$(function(){
	gwShare_setGaoji();
	
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
	//������:		queryDevice
	//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
	//����  :	���ݴ���Ĳ���������ʾ�Ľ���
	//����ֵ:		��������
	//˵��  :	
	//����  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var queryType =  $("input[@name='queryType']").val();
		var queryParam =  $("input[@name='queryParam']").val();
		var queryField =  $("input[@name='queryField']").val();
		alert(queryField);
		var url="<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceList.action'/>?queryResultType=radio&queryType="+queryType+"&queryParam="+queryParam+"&queryField="+queryField;
		
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
	
	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("���ѯ�豸��");
			return false;
	    }
		return true;
	}

	function getPara() {
		if(!CheckForm()){
			return;
		}
		if(document.frm.paraV.value==''){
			alert("��������Ϊ��");
			document.frm.paraV.focus();
			document.frm.paraV.select();
			return false;
		}
		$("tr[@id='trTableView']").css("display","");
		$("div[@id='tableView']").html("<BR><BR>���ڻ�ȡ,���Ժ�...");
		var url = "<s:url value='/gtms/stb/config/paraModeValue!getOneValue.action'/>";;
		$.post(url,{
			deviceId:document.frm.deviceId.value,
			paraV:document.frm.paraV.value
		},function(ajax){
		    $("div[@id='tableView']").html("");
			$("div[@id='tableView']").append(ajax);
		});
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã�����ֵ��ȡ
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td>
			<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
		</td>
	</TR>

	<tr>
		<td>
			<FORM NAME="frm" METHOD="post" ACTION=""
				onsubmit="return CheckForm()">
				<table width="100%" class="querytable">
					<input type="hidden" value="" name="deviceId" />
					<TR>
						<TH colspan="4" class=title_1>
							����ѡ��
						</TH>
					</TR>
					<TR id="trDeviceResult" style="display: none">
						<td class=title_2 width="25%">
							�豸���к�:
						</td>
						<td id="tdDeviceSn" width="25%">
						</td>
						<td class=title_2 width="25%">
							����:
						</td>
						<td id="tdDeviceCityName" width="25%">
						</td>
					</tr>

					<TR bgcolor="#FFFFFF">
						<TD class=title_2 width="25%" nowrap>
							����
						</TD>
						<TD width="85%" colspan="3">
							<INPUT TYPE="text" NAME="paraV" size="80" maxlength="200"
								class="bk" />
							&nbsp;
							<font color="#FF0000">*</font>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD colspan="4" align="right" class=foot>
							<div class="right">
								<button onclick="getPara()" name="getParam">
									�� ȡ
								</button>
							</div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="trTableView" style="display: none">
						<TD colspan="4">
							<DIV id="tableView" style="overflow: auto; display: ;"
								align="center"></DIV>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</td>
	</tr>
</table>

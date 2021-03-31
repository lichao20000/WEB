<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
<SCRIPT LANGUAGE="JavaScript">
//ϵͳ����
    //����
	function ExecMod(){
		//$("input[@name='btnValue4Customer']").attr("value",btnValue4Cust);
		//$("input[@name='custCheck']").attr("value","1");
		
		//������
		var taskName =  $("#taskName").val();
		if(taskName == null || $.trim(taskName) == ""){
			alert("������������");
			return false;
		}
		
		
		var isImportCustomer = importCustomer();
		if(!isImportCustomer){
			return false;
		}
		
		var mainForm = document.getElementById("batchexform");
		mainForm.action = "<s:url value='/ids/testSpeedTask!saveTestSpeedTask.action' />";
		mainForm.submit();
		
	}
	
	//����У��
	function checkChinese(str){
		var regTest = /^[\u4e00-\u9fa5]+$/;
		var flag = false;
		if(str != null && $.trim(str) != ""){
			if(str.indexOf("\\") != -1){
				var strArr = str.split("\\");
				str = strArr[strArr.length-1];
			}
			//alert(str);
	    	for(var i=0 ; i<str.length ; i++){
				var word = str.substring(i,i+1);
				if(regTest.test(word)){
					flag = true ; 
					break;
				}
			}
	    }
		return flag;
	}

    //�����ʺ���Ϣ
    function importCustomer(){
    	var myFileCustomer = $("input[@name='uploadCustomer']").val();
		if(""==myFileCustomer){	
			alert("��ѡ���ļ�!");
			return false;
		}
		var filetCustomer = myFileCustomer.split(".");
		if(filetCustomer.length<2)
		{
			alert("��ѡ���ļ�!");
			return false;
		}
		if("xls" == filetCustomer[filetCustomer.length-1] || "txt" == filetCustomer[filetCustomer.length-1])
		{
			var file2 = myFileCustomer.split("\\");
			var fileName2 = file2[file2.length-1];
			$("input[@name='uploadFileName4Customer']").attr("value",fileName2);
			return true;
		}else
		{
			alert("��֧�ֺ�׺Ϊxls��txt���ļ�");
			return false;
		}
    }
    
	function toExportCustExcel()
	{
		$("form[@name='batchexform']").attr("action","testSpeedTask!downloadTemplateCustExcel.action");
		$("form[@name='batchexform']").submit();
	}
	
	function toExportCustTxt()
	{
		$("form[@name='batchexform']").attr("action","testSpeedTask!downloadTemplateCustTxt.action");
		$("form[@name='batchexform']").submit();
	}
	
	//ȥ���ո�
	function trim(str){
      return str.replace(/(^\s*)|(\s*$)/g,"");
	}
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã��������ٶ���
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="testSpeedTask!saveTestSpeedTask.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="" id="batchexform">
			<input type="hidden" name="custSG"   value="">
			<input type="hidden" name="custCheck"  value="">
			<input type="hidden" name="btnValue4Customer"   value="">
			
			
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						�������ٶ���
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="15%">��������</TD>
					<TD colspan="3"> <input type="text" id="taskName" name="taskName" width="500" maxlength="50"> </TD>
				</TR>
				<tr id="importCustomer" >
					<td class="title_2" align="center" width="15%">
						��ѡ���ļ�
					</td>
					<td width="85%" colspan="3">
						<s:file label="�ϴ�" theme="simple" name="uploadCustomer"></s:file>
						<font color="red">*</font>
						<a href="javascript:void(0);" onclick="toExportCustExcel();"><font color="red">����xls��ʽģ��</font></a>
						&nbsp;&nbsp;
						<font color="red">*</font>
						<a href="javascript:void(0);" onclick="toExportCustTxt();"><font color="red">����txt��ʽģ��</font></a>
						<input type="hidden" name="uploadFileName4Customer" value=""/>
					</td>
				</tr>
				
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button id="doButton" onclick="ExecMod()">
								����
							</button>
						</div>
					</td>
				</TR>
			</table>
		</s:form>
		<br>  
		<br>
		
	</body>

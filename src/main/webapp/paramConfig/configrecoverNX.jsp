<%--
FileName	: softUpgrade.jsp
Date		: 2007��5��10��
Desc		: �������.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<jsp:useBean id="versionManage" scope="request"
	class="com.linkage.litms.software.VersionManage" />
<%
	request.setCharacterEncoding("gbk");
	com.linkage.litms.resource.DeviceAct deviceAct = new com.linkage.litms.resource.DeviceAct();
	String gatherList = deviceAct.getGatherList(session, "", "", true);

	//�豸�ͺ�������
	//String deviceModel = versionManage.getDeviceTypeList("", true);

	/* String file_path = versionManage.getFilePath_3("file_path","","",true); */

	//�豸����
	String strVendorList = deviceAct.getVendorList(true, "", "");
	
	//�������
	String opeResult = request.getParameter("operResult");
	
	//��������
	String gw_type = request.getParameter("gw_type");
	
	String file_path = versionManage.getFilePath_3("file_path","","",true,gw_type);
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<%if(null!=opeResult)
    {  
    	if("true".equals(opeResult))
    	{
    		out.println("alert('���Զ��Ƴɹ���');");
    	}
    	else if("false".equals(opeResult))
    	{
    		out.println("alert('���Զ���ʧ�ܣ�');");
    	}
    	else if("notAllow".equals(opeResult))
    	{
    		out.println("alert('һ���豸���ܶ������������������ԣ�������ѡ���豸��')");
    	}
    }
%>
	var isOnlyOne = false;
	
	function selectAll(elmID){
		//var needFilter=false;
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
					//needFilter = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
		
//		page = "showFilePath.jsp?device_id=" + "&device_model_id=" +document.all.softwareversion.value+"&needFilter="+needFilter;
//		document.all("childFrm2").src = page;
		
	}
	function showChild(param){
		var page ="";
		if(param == "gather_id"){
			document.all("div_vendor").innerHTML = "<%=strVendorList%>";
		}
		if(param == "softwareversion"){
			var gw_type = '<%= gw_type %>';
			document.frm.device.checked = false;
			page = "showDeviceList.jsp?gather_id="+document.frm.gather_id.value +"&vendor_id="+document.frm.vendor_id.value+ "&devicetype_id="+document.frm.softwareversion.value+"&gw_type="+gw_type;
			document.all("childFrm").src = page;
		}
		if(param == "vendor_id"){
			page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
			document.all("childFrm1").src = page;		
			
		}if(param == "device_model_id"){
		    page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&device_model_id="+ encodeURIComponent(document.frm.device_model_id.value);
			document.all("childFrm2").src = page;	
		}
		if(param == "file_path"){
			if(document.frm.file_path.value != -1){
				var file = document.frm.file_path.value;
				var arrFile = file.split("\|");
				document.frm.file_size.value=arrFile[2];
				document.frm.filename.value=arrFile[1];
			}else{
				document.frm.file_size.value="";
				document.frm.filename.value="";			
			}
		}
	}
	
	
	function CheckForm(){
	/**
		var hguser=document.all("hguser").value;
	    var telephone= document.all("telephone").value;
	    var checkradios = document.all("checkType");
	    var checkType = "";
	    for(var i=0;i<checkradios.length;i++)
	    {
	      if(checkradios[i].checked)
		  {
		    checkType = checkradios[i].value;
		    break;
		  }
	    }
    */
    
	    var excuteRadios = document.all("excute_type");
	    var excute_type = "";
	    for(var i=0;i<excuteRadios.length;i++)
	    {
	       if(excuteRadios[i].checked)
	       {
	          excute_type = excuteRadios[i].value;
	          break;
	       }
	    }
    
	    if(excute_type==2&&document.all("task_name").value=="")
	    {
	       alert("����д�������ƣ�");
	       document.frm.task_name.focus();
	       return false;
	    }  
    
    
		if(document.frm.gather_id.value == -1){
			alert("��ѡ��ɼ��㣡");
			document.frm.gather_id.focus();
			return false;
	  	}
	  	if(document.frm.vendor_id.value == -1){
			alert("��ѡ���̣�");
			document.frm.vendor_id.focus();
			return false;
	 	}
	 	if(document.frm.device_model_id.value == -1){
			alert("��ѡ���豸�ͺţ�");
			document.frm.device_model_id.focus();
			return false;
	 	}
	 	if(document.frm.softwareversion.value == -1){
			alert("��ѡ���豸�汾��");
			document.frm.softwareversion.focus();
			return false;
		}
		/**	
		if(""==hguser && ""==telephone)
		{
			alert("����д�û�����绰���룡");
			document.all("hguser").focus();
			return false;
		}
		*/
		var oselect = document.all("device_id");
		//alert(oselect);
		if(oselect == null){
			alert("��ѡ���豸��");
			return false;
		}
		var num = 0;
		if(typeof(oselect.length)=="undefined"){
			if(oselect.checked){
				num = 1;
			}
		}else{
			for(var i=0;i<oselect.length;i++){
				if(oselect[i].checked){
					num++;
				}
			}

		}
		if(num == 0){
			alert("��ѡ���豸��");
			return false;
		}
		var obj = document.frm;	
//-------------------------------------------------------------------
		if(obj.filetype.value == -1){
			alert("��ѡ���ļ�·����");
			bj.filetype.focus();
			return false;
		}
		if(isEmpty(obj.file_size.value)){//�ļ���С
			alert("�ļ���С����Ϊ��");
			obj.file_size.focus();
			obj.file_size.select();
			return false;
		}	
		if(isEmpty(obj.filename.value)){//�ļ���
			alert("�ļ�������Ϊ��");
			obj.filename.focus();
			obj.filename.select();
			return false;
		}
		if(isEmpty(obj.delay_time.value)){
			alert("ʱ�Ӳ���Ϊ��");
			obj.delay_time.focus();
			obj.delay_time.select();
			return false;
		}else{
			if(!IsNumber(obj.delay_time.value,"ʱ��")){
				obj.delay_time.focus();
				obj.delay_time.select();
				return false;
			}
		}			
	}
/**	
//�����û�����绰�����ѯ�豸
function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("����д�û�����绰���룡");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="showDeviceList.jsp?hguser="+hguser+"&telephone="+telephone+"&needFilter=true&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}
*/

/**
function ShowDialog(param)
{
  //�����û�����ѯ
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
  }
  
  //�����豸�汾����ѯ
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
  }
}
*/
function displayType(param)
{
   //���ѡ������ִ�л�ƻ�ִ������
   if(1==param||0==param)
   {
      tr0.style.display="none";
   }
   
   //ѡ���Զ�ִ������
   if(2==param)
   {
      tr0.style.display="";
   }
}


function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="recoverSend.jsp"
				onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										����ʵ������
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										��ѡ����豸�������ûָ���
										
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													�����·�����
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
												<!-- <TD align="right" width="20%">
													ҵ������:
												</TD>
												<TD width="30%">
													<select name="service_id" class="bk">
														<option value="1">
															���ûָ�
														</option>
													</select>
												</TD> -->
												<TD align="right" width="20%">
													ִ�з�ʽ:
												</TD>
												<TD colspan="3">
													<input type="radio" name="excute_type" value="0" onclick="displayType(this.value)" checked>
													����ִ��
													<input type="radio" name="excute_type" value="1" onclick="displayType(this.value)">
													�ƻ�ִ��
													<input type="radio" name="excute_type" value="2" onclick="displayType(this.value)">
													���Ʋ���
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr0" STYLE="display:none">
											    <TD align="right" width="15%">
											    �������ƣ�
											    </TD>
											    <TD width="35%">
											    <input type=text name="task_name" value="" class="bk">
											    </TD>
											    <TD align="right" width="15%">
													����ִ��ʱ��:
												</TD>
												<TD width="35%">
													<select name="auto_excutetime_type" class="bk">
														<option value="1">
															�豸��ʼ��װ��һ������ʱ�Զ�����
														</option>
														<option value="2">
															�豸Periodic Inform�Զ�����
														</option>
														<option value="3">
															�豸��������ʱ�Զ�����
														</option>
														<option value="4">
															�豸�´����ӵ�ITMSʱ�Զ�����
														</option>
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
												<TD align="right" width="20%">
													�ɼ���:
												</TD>
												<TD width="30%">
													<%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													����:
												</TD>
												<TD width="30%">
													<div id="div_vendor">
														<select name="vendor" class="bk">
															<option value="-1">
																--����ѡ��ɼ���--
															</option>
														</select>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">
												
												<TD align="right" width="20%">
													�豸�ͺ�:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name="device_model_id" class="bk">
															<option value="-1">
																--����ѡ����--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													�豸�汾:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--��ѡ���豸--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<!--  
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="20%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="hguser" value="" class="bk">
												</TD>
												<TD align="right" width="20%">
													�û��绰����:
												</TD>
												<TD width="30%">
													<input type="text" name="telephone" value="" class="bk">
													<input type="button" class=btn value="��ѯ" onclick="relateDevice()">
												</TD>
											</TR>
											-->
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													�豸�б�:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')"
														name="device">
													ȫѡ
												</TD>
												<TD colspan="3">
													<div id="idLayer"
														style="overflow:scroll;width:95%;height:'150px'">
														<span id="div_device">����ѡ���豸�汾��</span>
													</div>
												</TD>
											</TR>
											<!-- <TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ؼ���:
												</TD>
												<TD width="30%">
													<input type="text" name="keyword" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="20%">
													�ļ�����:
												</TD>
												<TD width="30%">
													<select name="filetype" class="bk" readOnly>
														<option value="3 Vendor Configuration File">
															3 Vendor Configuration File
														</option>
												</TD>
											</TR> -->
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ļ�·��:
												</TD>
												<TD width="" colspan="3">
													<div id="filepath">
														<%=file_path%>
													</div>
												</TD>
											</TR>
											<!-- 
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="username" maxlength=255 class=bk
														size=20>
												</TD>
												<TD align="right" width="20%">
													����:
												</TD>
												<TD width="30%">
													<input type="text" name="passwd" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>											
											 -->
											 <input type="hidden" name="username" maxlength=255 class=bk size=20>
											 <input type="hidden" name="passwd" maxlength=255 class=bk size=20>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ļ���С:
												</TD>
												<TD width="30%">
													<input type="text" name="file_size" maxlength=255 class=bk
														size=20 readOnly>
												</TD>
												<TD align="right" width="20%">
													�ļ���:
												</TD>
												<TD width="30%">
													<input type="text" name="filename" maxlength=255 class=bk
														size=20>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													ʱ��:
												</TD>
												<TD colspan="3">
													<input type="text" name="delay_time" maxlength=255 class=bk
														size=20 value="0">
													&nbsp;&nbsp;(��λ:s)
												</TD>

											</TR>
											<!-- 
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													�ɹ�URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="sucess_url" class=bk size=80>
												</TD>

											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="20%">
													ʧ��URL:
												</TD>
												<TD colspan="3">
													<input type="text" name="fail_url" class=bk size=80>
												</TD>

											</TR>
											 -->
											 <input type="hidden" name="sucess_url" class=bk size=80>
											 <input type="hidden" name="fail_url" class=bk size=80>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">

													<INPUT TYPE="reset" value=" �� �� " class=btn>
													&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													<input type ="hidden" name="auto_type" value="2">
													&nbsp;
													<INPUT TYPE="submit" value=" �� �� " class=btn>
													<input type='hidden' name='service_id' value="1">		
													<input type='hidden' name='keyword' value="download_config">
													<input type='hidden' name='filetype' value="3 Vendor Configuration File">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<%
deviceAct = null;
%>

<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"  pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">

//---------------------------------------------------------------
$(function(){
	Init();
});

function Init(){
	// ��ʼ������,�ն����ͣ��Ӵ���Ա
	change_select("vendor","-1");
	change_select("workOpts","-1");
	change_select("reception","-1");
}

function query(){
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/memBook!queryList.action'/>";
	//form.target = "dataForm";
	form.submit();
}
</SCRIPT>
<script type="text/javascript">
</script>
</head>
<%@ include file="/toolbar.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<td HEIGHT=20>&nbsp;</td>
	</TR>
	<TR>
		<td>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">��������¼</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">�������뵼������������</td>
			</tr>
		</table>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%"
						align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">��������¼��ѯ</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">ҵ����</td>
							<td align="left" width="35%">
							 <input TYPE="text" NAME="busNo" maxlength=30 class=bk size=20 />
							</td>
							<td align="right" class=column width="15%">�ն˳���</td>
							<td width="35%">
							<select name="vendor" onchange="change_select('specType','-1')" class="bk">
							</select>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">�ն�����</td>
							<td align="left" width="35%">
							 <select name="spec_type" class="bk">
								<option value="-1">==��ѡ����==</option>
							</select>
							</td>
							<td align="right" class=column width="15%">�ն��ͺ�</td>
							<td width="35%" nowrap>
							 <INPUT TYPE="text" NAME="spec_model" maxlength=30 class=bk size=20>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">Ӳ���汾</td>
							<td align="left" width="35%">
							 <INPUT TYPE="text" NAME="hardware" maxlength=30 class=bk size=20>
							</td>
							<td align="right" class=column width="15%">����汾</td>
							<td width="35%">
							 <INPUT TYPE="text" NAME="software" maxlength=30 class=bk size=20>
							</td>
						</TR>
						 <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">��������</td>
							<td align="left" width="35%">
							<select name="workOpts" class="bk"></select>
							</td>
							<td align="right" class=column width="15%">��ϵ��Ա</td>
							<td width="35%">
							 <INPUT TYPE="text" NAME="connPerson" maxlength=30 class=bk size=20>
							</td>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">��ϵ��ʽ</td>
							<td align="left" width="35%">
								 <INPUT TYPE="text" NAME="connPhone" maxlength=30 class=bk size=20>
							</td>
							<td align="right" class=column width="15%">�Ӵ���Ա</td>
							<td align="left" width="35%">
							 <select name="reception" class='bk'></select>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">��ʼʱ��</td>
							<td align="left" width="35%">
							<%-- <lk:date id="startTime" name="startTime" type="all" /> --%>
							<input type="text" name="startTime" readonly class=bk value='<s:property value="startTime" />'>
							 <img name="shortDateimg" id="startTime"
										onClick="WdatePicker({el:document.mainForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</td>
							<td align="right" class=column width="15%">����ʱ��</td>
							<td align="left" width="35%">
							<%-- <lk:date id="endTime" name="endTime" type="all" /> --%>
							<input type="text" name="endTime" readonly id="endTime" class=bk value='<s:property value="endTime" />'>
							 <img name="shortDateimg"
										onClick="WdatePicker({el:document.mainForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</td>
						</TR>
	
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button"
									onclick="javascript:query()" class=jianbian
									name="gwShare_queryButton" value=" �� ѯ " /> 
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" �� �� "
									onclick="javascript:add();" />
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" �� �� "
									onclick="javascript:imp();" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</FORM>
		<!-- չʾ���part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<td bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></td>
			</TR>
		</TABLE>
		<FORM id="addForm" name="addForm" target="" method="post" action="" enctype="multipart/form-data">
		
		<!-- ��Ӻͱ༭part -->
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center"
			id="addTable" style="display: none">
			<TR>
				<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"
					id="allDatas">
					<TR>
						<TH colspan="2" align="center"><SPAN id="actLabel">���</SPAN><SPAN
							id="WorkMemLabel"></SPAN>��������¼</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<td class=column align="right">�豸����</td>
						<td>
						<select name="vendor_add" class="bk" onchange="change_select_add('specType','-1')">
						</select> <font color="#FF0000">*</font></td>
					</TR>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<td class=column align="right">�ն�����</td>
						<td>
						<select name="spec_type_add" class="bk">
							<option value="-1">==��ѡ��==</option>
						</select> <font color="#FF0000">*</font></td>
					</TR>

					<TR bgcolor="#FFFFFF">
						<td class=column align="right">�ն��ͺ�</td>
						<td>
						 <INPUT TYPE="text" NAME="spec_model_add" maxlength=30 class=bk size=20></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">Ӳ���汾</td>
						<td><INPUT TYPE="text" NAME="hardware_add"
							maxlength=30 class=bk size=20></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">����汾</td>
						<td><INPUT TYPE="text" NAME="software_add"
							maxlength=30 class=bk size=20></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">��������</td>
						<td><select name="workOpts_add" class="bk">
							<option value="-2" selected>==��ѡ��==</option>
						</select>&nbsp;<font color="#FF0000">*</font></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">����״̬</td>
						<td>
						<select name="work_status" class="bk" disabled>
							<option oname="0" value="δ���" selected>==δ���==</option>
							<option oname="1" value="������" >==������==</option>
							<option oname="2" value="�����" >==�����==</option>
						</select></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">�Ӵ���Ա</td>
						<td>
						<select name="reception_add" class="bk"></select>&nbsp;<font color="#FF0000">*</font>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">������ϵ��Ա</td>
						<td>
						  <input type="text" name="connPerson_add">
						</td>
					</TR>

					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">������Ա��ϵ��ʽ</td>
						<td><input type="text" width="30%" id="2"
							name="phone_add" value=""></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">��ʼʱ��</td>
						<td>
								<%-- <lk:date id="startOpenDate" name="startOpenDate" type="all" /> --%>
							<input type="text" name="startOpenDate" readonly id="startOpenDate" class=bk>
							 <img name="shortDateimg"
										onClick="WdatePicker({el:document.addForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">&nbsp;<font color="#FF0000">*</font>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">����ʱ��</td>
						<td>
							<%-- <lk:date id="endOpenDate" name="endOpenDate" type="all" /> --%>
							<input type="text" name="endOpenDate" readonly id="endOpenDate" class=bk>
							 <img name="shortDateimg"
										onClick="WdatePicker({el:document.addForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">	 
						</td>
					</TR>
					 
					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">��������</td>
						<td width="35%">
						 <textarea rows="3" cols="60" name="content_add" maxlength="500" placeholder="�������500��"></textarea>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<td class=column align="right">��ע</td>
						<td>
							 <textarea rows="3" cols="60" name="commit_add" maxlength="100" placeholder="�������100��"></textarea>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="importPNG" >
						<td class=column align="right">�ϴ�����</td>
						<td>
						 <div id="importUsername_png">
							<iframe name="gwShare_loadForm" id="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
						</div>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="importPNG_msgs" style="display:none">
						<td class=column align="right">�ϴ�����</td>
						<td id="importPNG_msgs_th">
						  
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
					   <td CLASS="green_foot" align="right">ע������</td>
					   <td CLASS="green_foot">
					   <br>
					     1����֧��word��pdf��exl��png��jpg��ʽ�ϴ��� <br>
					     2��ÿ�������ļ���С��10M��<br>
					   </td>
					</TR>
				</TABLE>
				</td>
			</TR>
			<TR>
				<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
					<TR bgcolor="#FFFFFF">
						<td align="right" CLASS=green_foot><INPUT TYPE="button"
							onclick="javascript:save()" value=" �� �� " class=jianbian>&nbsp;&nbsp;
							<input type="hidden" name="operType">
							<input type="hidden" name="busno_edit">
						 </td>
					</TR>
				</TABLE>
				</td>
			</TR>

		</TABLE>  
		</FORM>
		<!-- �����޸�end -->
		<!-- ���� start-->
		<FORM NAME="impFileForm" id="impFileForm" METHOD="post" ACTION="">
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="impFileTable" style="display: none;">
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"  style="height:20px;line-height: 20px;">
					
						<TR>
							<TH colspan="6" align="center"><SPAN id="impLabel">��������¼����</SPAN></TH>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">����¼</TD>
							<TD colspan=5 id="shebeixinghao">
							  <div id="importUsername">
							   <iframe name="gwShare_loadForm_import" id="gwShare_loadForm_import" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="FileUpload.jsp"/>" height="30" width="100%">
							   </iframe>
							   <input type="hidden" name="gwShare_fileName" value=""/>
						     </div>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="">
					        <td CLASS="green_foot" align="right">ע������</td>
					        <td colspan="5" CLASS="green_foot">
					          1���ļ���ʽ��֧��.csv,�ļ��ĵ�һ�����ݲ����,��ʽΪ���ն˳���,�ն�����,�ն��ͺ�,Ӳ���汾,����汾,��������,������ϵ��,������ϵ��ʽ,�Ӵ���Ա,��ʼʱ��,����ʱ��,��������,���״̬,��ע;<br/>
					          2���ն˳���,�ն�����,��������,�Ӵ���ԱΪ������;<br>
					          3����ʼʱ��ͽ���ʱ��ĸ�ʽΪyyyy-MM-dd HH:mm:ss,״̬������,0:δ���  1:������ 2:�����;<br>
					          4���������벻��������������ֻ�ܵ����޸�; <br>
					          5����������һ����������Ҫ̫�� <br>
					          <a href="eImportUserTemplate.jsp">�������ģ��</a>
					        </td>
					</TR>
						<TR>
							<td colspan="6" CLASS=green_foot align="right">
								<input type="button" id="impBtn" value="�� ��" class="jianbian" onclick="javascript:importWork()">
							</td>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		<!-- ����end -->
		</td>
	</TR>
	<TR>
		<td HEIGHT=20><IFRAME ID="childFrm" name="childFrm" SRC="" STYLE="display: none"></IFRAME>&nbsp;</td>
	</TR>

</TABLE>
<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
</body>
<%@ include file="/foot.jsp"%>

<!-- ��˲���form�ı��ύ���� -->
<script type="text/javascript">
function showAddPart(html, tag)
{
	if(tag){
		$("table[@id='"+html+"']").show();
	}
	else{
		$("table[@id='"+html+"']").hide();
	}
}

//���impFileTable
function add() {
	$("#actLabel").html("���");
	$("input[name='operType']").val(1); // ��ʾ����
	showAddPart("impFileTable",false);
	showAddPart("addTable",true);
	resetForm("addForm","startOpenDate"); 
	var start = $("input[@name='startOpenDate']").val();
	$("input[@name='endOpenDate']").attr("disabled",true);
	$("select[name='vendor_add']").val("-1");
	$("select[name='spec_type_add']").val("-1");
	$("select[name='workOpts_add']").val("-1");
	$("select[name='work_status']").val("δ���");
	$("select[name='work_status']").attr("disabled",true);
	$("select[name='reception_add']").val("-1");
	reUpload();
}

//���form,
function resetForm(id,basideId){
	  //document.getElementById(id).reset();
	  $(':input','#'+id).not(':button,:submit,:reset,:hidden,#'+basideId).val('').removeAttr('checked').removeAttr('selected');
}
function imp(){
	showAddPart("impFileTable",true);
	showAddPart("addTable",false);
	$("#gwShare_loadForm_import")[0].contentWindow.initPage("import");//��ʾ��ǰ�ļ��������ǵ���ҳ������
}

function reUpload(){
	$("#importPNG_msgs").hide();
	$("#importPNG").show();
	$("#gwShare_loadForm")[0].contentWindow.initPage("addorupdate");
}

function reLoadFile(){
	$("#gwShare_loadForm")[0].contentWindow.initPage("addorupdate");
}

function save()
{
	var vendorName = "";
	var vendorId = $("select[@name='vendor_add']").val();
	if(vendorId != -1){
		vendorName = $("select[@name='vendor_add'] option:selected").val();
	}
	
	if(vendorName == "" || vendorName == null || vendorName == undefined){
		alert("��ѡ���豸����");
		return false;
	}
	var specType = "";
	var specId = $("select[@name='spec_type_add']").val();
	if(specId != -1){
		specType = $("select[@name='spec_type_add'] option:selected").val();
	}
	if(specType == "" || specType == null || specType == undefined){
		alert("��ѡ���ն�����");
		return false;
	}
	
	var specModel = $("input[@name='spec_model_add']").val();
	var hardware = $("input[@name='hardware_add']").val();
	var software = $("input[@name='software_add']").val();
	
	var workOpts = "";
	var workOptId = $("select[@name='workOpts_add']").val();
	if(workOptId != -1){
		workOpts = $("select[@name='workOpts_add'] option:selected").val();
	}
	if(workOpts == "" || workOpts == null || workOpts == undefined){
		alert("��ѡ��������");
		return false;
	}
	
	var reception = "";
    var reception_id = $("select[@name='reception_add']").val();
    if(reception_id != -1){
    	reception = $("select[@name='reception_add'] option:selected").val();
    }
    if(reception == "" || reception == null || reception == undefined){
		alert("��ѡ��Ӵ���Ա");
		return false;
	}
	var connPerson = $("input[@name='connPerson_add']").val();
	var phone = $("input[@name='phone_add']").val();
	var start = $("input[@name='startOpenDate']").val();
	if(start == "" || start == null || start == undefined){
		alert("��ѡ��ʼʱ��");
		return false;
	}
	var end = $("input[@name='endOpenDate']").val();
	var content = $("textarea[@name='content_add']").val();
	var remark = $("textarea[@name='commit_add']").val();
	var filename = $("input[@name='gwShare_fileName']").val();
	
	//var url = "<s:url value='/itms/resource/memBook!operWorkMem.action'/>"
	var url = "memBook!operWorkMem.action";
	var operType = $("input[name='operType']").val();
	var busNo = ""
	if(operType == 2){
		busNo = $("input[name='busno_edit']").val();
	}
	//var status = $("select[name='work_status']").attr("oname");
	var status = $("select[@name='work_status'] option:selected").attr("oname");
	var data = "operType="+operType+"&busNo="+busNo+"&vendor="+vendorName+"&spec_type="+specType+"&spec_model="+specModel+"&reception="+reception
	+"&hardware="+hardware+"&software="+software+"&workOpts="+workOpts+"&workContent="+content+"&connPerson="+connPerson+
	"&connPhone="+phone+"&startTime="+start+"&endTime="+end+"&status="+status+"&remark="+remark+"&gwShare_fileName="+filename;
	data = encodeURI(encodeURI(data));
	$.ajax({
		url : url,
		dataType : 'json',
		type : 'POST',
		//scriptCharset : 'GBK',
		contentType:'application/x-www-form-urlencoded; charset=GBK',
		data : data,
	   success : function(ret){
		   alert(ret.responseText);
		   if(operType == 1){
			   $("#gwShare_loadForm")[0].contentWindow.reloadUrl();
		   }
		   showAddPart("addTable",false);
		   query();
	   },
	   error : function(ret){
		   alert(ret.responseText);
		   if(operType == 1){
			   $("#gwShare_loadForm")[0].contentWindow.reloadUrl();
		   }
		   showAddPart("addTable",false);
			query();
	   }
	});
}


function importWork(){
	var fileName = $("input[name='gwShare_fileName']").val();
	var url = "<s:url value='/itms/resource/memBook!impFileParse.action'/>";
	fileName = "gwShare_fileName="+fileName;
	data = encodeURI(encodeURI(fileName));
	$.ajax({
		url : url,
		dataType : 'json',
		type : 'POST',
		contentType:'application/x-www-form-urlencoded; charset=GBK',
		data : data,
	   success : function(ret){
		   alert(ret.responseText);
		   $("#gwShare_loadForm_import")[0].contentWindow.reloadUrl();
			showAddPart("impFileTable",false);

	   },
	   error : function(ret){
		   alert(ret.responseText);
		   $("#gwShare_loadForm_import")[0].contentWindow.reloadUrl();
			showAddPart("impFileTable",false);

	   }
	});
}

//����
function toExport(){
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/memBook!getExcelOut.action'/>";
	form.submit();
}
//��ѯ�漰
function change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value='/itms/resource/memBook!getVendors.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
			});
			break;
		case "specType":
			var url = "<s:url value='/itms/resource/memBook!getSpecType.action'/>";
			var vendorId = $("select[@name='vendor'] option:selected").attr("oname");
			if("-1"==vendorId){
				$("select[@name='spec_type']").html("<option value='-1'>==����ѡ���豸����==</option>");
				break;
			}
			$.post(url,{
				vendor_id:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='spec_type']"),selectvalue);
			});
			break;
		case "workOpts":
			var url = "<s:url value='/itms/resource/memBook!getWorkOpt.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='workOpts']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='workOpts_add']"),selectvalue);

			});
			break;
		case "reception":
			var url = "<s:url value='/itms/resource/memBook!getReceptions.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='reception']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='reception_add']"),selectvalue);
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}
 
//�����༭�漰
function change_select_add(type,selectvalue){
	switch (type){
	case "specType":
		var url = "<s:url value='/itms/resource/memBook!getSpecType.action'/>";
		var vendorId =  $("select[@name='vendor_add'] option:selected").attr("oname");
		if("-1"==vendorId){
			$("select[@name='spec_type_add']").html("<option value='-1'>==����ѡ���豸����==</option>");
			break;
		}
		$.post(url,{
			vendor_id:vendorId
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='spec_type_add']"),selectvalue);
		});
		break;
	case "specTypeName":
		var url = "<s:url value='/itms/resource/memBook!getSpecType.action'/>";
		var vendorId =  $("select[@name='vendor_add'] option:selected").attr("oname");
		if("-1"==vendorId){
			$("select[@name='spec_type_add']").html("<option value='-1'>==����ѡ���豸����==</option>");
			break;
		}
		$.post(url,{
			vendor_id:vendorId
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='spec_type_add']"),selectvalue);
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
		if(selectvalue==xText){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option oname='"+xValue+"' value='"+xText+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option oname='"+xValue+"' value='"+xText+"'>=="+xText+"==</option>";
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
	for (var i=0; i<iframeids.length; i++)
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
    		tempobj.style.display="block";
		}
	}
}
 
$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

/** ���߷��� **/
/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//ȫ��trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}
 
</SCRIPT>
</html>
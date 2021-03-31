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
	// 初始化厂家,终端类型，接待人员
	change_select("vendor","-1");
	change_select("workOpts","-1");
	change_select("reception","-1");
}

function query(){
	// 普通方式提交
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
				<div align="center" class="title_bigwhite">工作备忘录</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">批量导入导出不包含附件</td>
			</tr>
		</table>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%"
						align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">工作备忘录查询</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">业务编号</td>
							<td align="left" width="35%">
							 <input TYPE="text" NAME="busNo" maxlength=30 class=bk size=20 />
							</td>
							<td align="right" class=column width="15%">终端厂家</td>
							<td width="35%">
							<select name="vendor" onchange="change_select('specType','-1')" class="bk">
							</select>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">终端类型</td>
							<td align="left" width="35%">
							 <select name="spec_type" class="bk">
								<option value="-1">==请选择厂商==</option>
							</select>
							</td>
							<td align="right" class=column width="15%">终端型号</td>
							<td width="35%" nowrap>
							 <INPUT TYPE="text" NAME="spec_model" maxlength=30 class=bk size=20>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">硬件版本</td>
							<td align="left" width="35%">
							 <INPUT TYPE="text" NAME="hardware" maxlength=30 class=bk size=20>
							</td>
							<td align="right" class=column width="15%">软件版本</td>
							<td width="35%">
							 <INPUT TYPE="text" NAME="software" maxlength=30 class=bk size=20>
							</td>
						</TR>
						 <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">工作事项</td>
							<td align="left" width="35%">
							<select name="workOpts" class="bk"></select>
							</td>
							<td align="right" class=column width="15%">联系人员</td>
							<td width="35%">
							 <INPUT TYPE="text" NAME="connPerson" maxlength=30 class=bk size=20>
							</td>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">联系方式</td>
							<td align="left" width="35%">
								 <INPUT TYPE="text" NAME="connPhone" maxlength=30 class=bk size=20>
							</td>
							<td align="right" class=column width="15%">接待人员</td>
							<td align="left" width="35%">
							 <select name="reception" class='bk'></select>
							</td>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">开始时间</td>
							<td align="left" width="35%">
							<%-- <lk:date id="startTime" name="startTime" type="all" /> --%>
							<input type="text" name="startTime" readonly class=bk value='<s:property value="startTime" />'>
							 <img name="shortDateimg" id="startTime"
										onClick="WdatePicker({el:document.mainForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</td>
							<td align="right" class=column width="15%">结束时间</td>
							<td align="left" width="35%">
							<%-- <lk:date id="endTime" name="endTime" type="all" /> --%>
							<input type="text" name="endTime" readonly id="endTime" class=bk value='<s:property value="endTime" />'>
							 <img name="shortDateimg"
										onClick="WdatePicker({el:document.mainForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</td>
						</TR>
	
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button"
									onclick="javascript:query()" class=jianbian
									name="gwShare_queryButton" value=" 查 询 " /> 
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" 新 增 "
									onclick="javascript:add();" />
								<input type="button"
									class=jianbian name="gwShare_reButto" value=" 导 入 "
									onclick="javascript:imp();" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</FORM>
		<!-- 展示结果part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<td bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></td>
			</TR>
		</TABLE>
		<FORM id="addForm" name="addForm" target="" method="post" action="" enctype="multipart/form-data">
		
		<!-- 添加和编辑part -->
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center"
			id="addTable" style="display: none">
			<TR>
				<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"
					id="allDatas">
					<TR>
						<TH colspan="2" align="center"><SPAN id="actLabel">添加</SPAN><SPAN
							id="WorkMemLabel"></SPAN>工作备忘录</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="vendor_idID">
						<td class=column align="right">设备厂商</td>
						<td>
						<select name="vendor_add" class="bk" onchange="change_select_add('specType','-1')">
						</select> <font color="#FF0000">*</font></td>
					</TR>
					<TR bgcolor="#FFFFFF" id="device_ModelID">
						<td class=column align="right">终端类型</td>
						<td>
						<select name="spec_type_add" class="bk">
							<option value="-1">==请选择==</option>
						</select> <font color="#FF0000">*</font></td>
					</TR>

					<TR bgcolor="#FFFFFF">
						<td class=column align="right">终端型号</td>
						<td>
						 <INPUT TYPE="text" NAME="spec_model_add" maxlength=30 class=bk size=20></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">硬件版本</td>
						<td><INPUT TYPE="text" NAME="hardware_add"
							maxlength=30 class=bk size=20></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">软件版本</td>
						<td><INPUT TYPE="text" NAME="software_add"
							maxlength=30 class=bk size=20></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">工作事项</td>
						<td><select name="workOpts_add" class="bk">
							<option value="-2" selected>==请选择==</option>
						</select>&nbsp;<font color="#FF0000">*</font></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">工作状态</td>
						<td>
						<select name="work_status" class="bk" disabled>
							<option oname="0" value="未完成" selected>==未完成==</option>
							<option oname="1" value="进行中" >==进行中==</option>
							<option oname="2" value="已完成" >==已完成==</option>
						</select></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">接待人员</td>
						<td>
						<select name="reception_add" class="bk"></select>&nbsp;<font color="#FF0000">*</font>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column align="right">厂家联系人员</td>
						<td>
						  <input type="text" name="connPerson_add">
						</td>
					</TR>

					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">厂家人员联系方式</td>
						<td><input type="text" width="30%" id="2"
							name="phone_add" value=""></td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">开始时间</td>
						<td>
								<%-- <lk:date id="startOpenDate" name="startOpenDate" type="all" /> --%>
							<input type="text" name="startOpenDate" readonly id="startOpenDate" class=bk>
							 <img name="shortDateimg"
										onClick="WdatePicker({el:document.addForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">&nbsp;<font color="#FF0000">*</font>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">结束时间</td>
						<td>
							<%-- <lk:date id="endOpenDate" name="endOpenDate" type="all" /> --%>
							<input type="text" name="endOpenDate" readonly id="endOpenDate" class=bk>
							 <img name="shortDateimg"
										onClick="WdatePicker({el:document.addForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">	 
						</td>
					</TR>
					 
					<TR bgcolor="#FFFFFF">
						<td class=column width="10%" align="right" id="3">工作内容</td>
						<td width="35%">
						 <textarea rows="3" cols="60" name="content_add" maxlength="500" placeholder="最长可输入500字"></textarea>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
						<td class=column align="right">备注</td>
						<td>
							 <textarea rows="3" cols="60" name="commit_add" maxlength="100" placeholder="最长可输入100字"></textarea>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="importPNG" >
						<td class=column align="right">上传附件</td>
						<td>
						 <div id="importUsername_png">
							<iframe name="gwShare_loadForm" id="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
						</div>
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="importPNG_msgs" style="display:none">
						<td class=column align="right">上传附件</td>
						<td id="importPNG_msgs_th">
						  
						</td>
					</TR>
					<TR bgcolor="#FFFFFF" id="">
					   <td CLASS="green_foot" align="right">注意事项</td>
					   <td CLASS="green_foot">
					   <br>
					     1、仅支持word、pdf、exl、png、jpg格式上传。 <br>
					     2、每张限制文件大小（10M）<br>
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
							onclick="javascript:save()" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
							<input type="hidden" name="operType">
							<input type="hidden" name="busno_edit">
						 </td>
					</TR>
				</TABLE>
				</td>
			</TR>

		</TABLE>  
		</FORM>
		<!-- 新增修改end -->
		<!-- 导入 start-->
		<FORM NAME="impFileForm" id="impFileForm" METHOD="post" ACTION="">
		<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center" id="impFileTable" style="display: none;">
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=0 width="100%"  style="height:20px;line-height: 20px;">
					
						<TR>
							<TH colspan="6" align="center"><SPAN id="impLabel">工作备忘录导入</SPAN></TH>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column width="10%" align="right" id="3">备忘录</TD>
							<TD colspan=5 id="shebeixinghao">
							  <div id="importUsername">
							   <iframe name="gwShare_loadForm_import" id="gwShare_loadForm_import" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="FileUpload.jsp"/>" height="30" width="100%">
							   </iframe>
							   <input type="hidden" name="gwShare_fileName" value=""/>
						     </div>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="">
					        <td CLASS="green_foot" align="right">注意事项</td>
					        <td colspan="5" CLASS="green_foot">
					          1、文件格式仅支持.csv,文件的第一行数据不入库,格式为：终端厂家,终端类型,终端型号,硬件版本,软件版本,工作事项,厂家联系人,厂家联系方式,接待人员,开始时间,结束时间,工作内容,完成状态,备注;<br/>
					          2、终端厂家,终端类型,工作事项,接待人员为必填项;<br>
					          3、开始时间和结束时间的格式为yyyy-MM-dd HH:mm:ss,状态填数字,0:未完成  1:进行中 2:已完成;<br>
					          4、批量导入不包含附件，附件只能单条修改; <br>
					          5、批量导入一次数据量不要太大 <br>
					          <a href="eImportUserTemplate.jsp">点击下载模板</a>
					        </td>
					</TR>
						<TR>
							<td colspan="6" CLASS=green_foot align="right">
								<input type="button" id="impBtn" value="提 交" class="jianbian" onclick="javascript:importWork()">
							</td>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		<!-- 导入end -->
		</td>
	</TR>
	<TR>
		<td HEIGHT=20><IFRAME ID="childFrm" name="childFrm" SRC="" STYLE="display: none"></IFRAME>&nbsp;</td>
	</TR>

</TABLE>
<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
</body>
<%@ include file="/foot.jsp"%>

<!-- 审核部分form的表单提交处理 -->
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

//添加impFileTable
function add() {
	$("#actLabel").html("添加");
	$("input[name='operType']").val(1); // 表示新增
	showAddPart("impFileTable",false);
	showAddPart("addTable",true);
	resetForm("addForm","startOpenDate"); 
	var start = $("input[@name='startOpenDate']").val();
	$("input[@name='endOpenDate']").attr("disabled",true);
	$("select[name='vendor_add']").val("-1");
	$("select[name='spec_type_add']").val("-1");
	$("select[name='workOpts_add']").val("-1");
	$("select[name='work_status']").val("未完成");
	$("select[name='work_status']").attr("disabled",true);
	$("select[name='reception_add']").val("-1");
	reUpload();
}

//清空form,
function resetForm(id,basideId){
	  //document.getElementById(id).reset();
	  $(':input','#'+id).not(':button,:submit,:reset,:hidden,#'+basideId).val('').removeAttr('checked').removeAttr('selected');
}
function imp(){
	showAddPart("impFileTable",true);
	showAddPart("addTable",false);
	$("#gwShare_loadForm_import")[0].contentWindow.initPage("import");//表示当前文件服务器是导入页面引用
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
		alert("请选择设备厂商");
		return false;
	}
	var specType = "";
	var specId = $("select[@name='spec_type_add']").val();
	if(specId != -1){
		specType = $("select[@name='spec_type_add'] option:selected").val();
	}
	if(specType == "" || specType == null || specType == undefined){
		alert("请选择终端类型");
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
		alert("请选择工作事项");
		return false;
	}
	
	var reception = "";
    var reception_id = $("select[@name='reception_add']").val();
    if(reception_id != -1){
    	reception = $("select[@name='reception_add'] option:selected").val();
    }
    if(reception == "" || reception == null || reception == undefined){
		alert("请选择接待人员");
		return false;
	}
	var connPerson = $("input[@name='connPerson_add']").val();
	var phone = $("input[@name='phone_add']").val();
	var start = $("input[@name='startOpenDate']").val();
	if(start == "" || start == null || start == undefined){
		alert("请选开始时间");
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

//导出
function toExport(){
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/memBook!getExcelOut.action'/>";
	form.submit();
}
//查询涉及
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
				$("select[@name='spec_type']").html("<option value='-1'>==请先选择设备厂商==</option>");
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
			alert("未知查询选项！");
			break;
	}	
}
 
//新增编辑涉及
function change_select_add(type,selectvalue){
	switch (type){
	case "specType":
		var url = "<s:url value='/itms/resource/memBook!getSpecType.action'/>";
		var vendorId =  $("select[@name='vendor_add'] option:selected").attr("oname");
		if("-1"==vendorId){
			$("select[@name='spec_type_add']").html("<option value='-1'>==请先选择设备厂商==</option>");
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
			$("select[@name='spec_type_add']").html("<option value='-1'>==请先选择设备厂商==</option>");
			break;
		}
		$.post(url,{
			vendor_id:vendorId
		},function(ajax){
			gwShare_parseMessage(ajax,$("select[@name='spec_type_add']"),selectvalue);
		});
		break;
	
	default:
		alert("未知查询选项！");
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

	option = "<option value='-1' selected>==请选择==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xText){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option oname='"+xValue+"' value='"+xText+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
			option = "<option oname='"+xValue+"' value='"+xText+"'>=="+xText+"==</option>";
		}
		try{
			field.append(option);
		}catch(e){
			alert("检索失败！");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
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

/** 工具方法 **/
/*LTrim(string):去除左边的空格*/
function LTrim(str){
    var whitespace = new String("　 \t\n\r");
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
/*RTrim(string):去除右边的空格*/
function RTrim(str){
    var whitespace = new String("　 \t\n\r");
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
/*Trim(string):去除字符串两边的空格*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//全部trim
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
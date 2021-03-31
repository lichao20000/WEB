<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>


<SCRIPT LANGUAGE="JavaScript">
	//�����ͺ�
	function vendorChange()
	{
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/softVersion!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='deviceModel']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"
												+xValue+"'>"+xText+"  ";
							$("div[@id='deviceModel']").append(checkboxtxt);
						}
					}else{
						$("div[@id='deviceModel']").append("�ó���û�ж�Ӧ�ͺţ�");
					}
				}else{
					$("div[@id='deviceModel']").append("�ó���û�ж�Ӧ�ͺţ�");
				}
			});
		}else{
			$("div[@id='deviceModel']").html("��ѡ����");
		}
	}
	
	//չʾ�༭��
	function addeditVersion(isAdd,id,vendorId,versionName,versionDesc,versionPath,
			deviceModelIds,fileSize,MD5,epg_version,net_type)
	{
		$("table[@id='addedit']").show();
		
		$("input[@name='versionPath']").val(versionPath);
		$("input[@name='isAdd']").val(isAdd);
		$("input[@name='id']").val(id);
		$("input[@name='versionName']").val(versionName);
		$("input[@name='versionDesc']").val(versionDesc);
		$("select[@name='vendorId']").val(vendorId);
		$("input[@name='fileSize']").val(fileSize);
		$("input[@name='MD5']").val(MD5);
		$("input[@name='epg_version']").val(epg_version);
		$("select[@name='net_type']").val(net_type);
		vendorChange();
		setTimeout('deviceModelIdchecked("'+deviceModelIds+'")', 1000);
	}
	
	//ѡ���ͺ�
	function deviceModelIdchecked(deviceModelIds)
	{
		var deviceModelId = deviceModelIds.split(",");
    	for(var i = 0;i<deviceModelId.length;i++){
	        $("input[@name='deviceModelId'][@value='"+deviceModelId[i]+"']").attr("checked",true);
    	}
	}
	
	//�༭�������汾
	function ExecMod()
	{
		var isAdd = $("input[@name='isAdd']").val();
		var id = $("input[@name='id']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath= $("input[@name='versionPath']").val();
		var versionName = $("input[@name='versionName']").val();
		var fileSize = $("input[@name='fileSize']").val();
		var MD5 = $("input[@name='MD5']").val();
		var epg_version = $("input[@name='epg_version']").val();
		var net_type = $("select[@name='net_type']").val();
		
		if(versionName==""){
			alert("������汾����!");
			$("input[@name='versionName']").focus();
			return;
		}
		
		if(vendorId=="-1"){
			alert("��ѡ���̣�");
			return;
		}
		if(versionPath==""){
			alert("������汾�ļ�·��!");
			$("input[@name='versionPath']").focus();
			return;
		}
		if(fileSize==""){
			alert("������汾�ļ���С!");
			$("input[@name='fileSize']").focus();
			return;
		}
		if(MD5==""){
			alert("������MD5����!");
			$("input[@name='MD5']").focus();
			return;
		}
		if(epg_version==""){
			alert("������EPG�汾����!");
			$("input[@name='epg_version']").focus();
			return;
		}
		
		var deviceModelId = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelId = deviceModelId + $(this).val()+",";
	    });
	    if(deviceModelId=="" || deviceModelId==null){
			alert("��ѡ�������ͺ�!");
			return;
		}
	    
	    var url = "<s:url value='/gtms/stb/resource/softVersion!addedit.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionDesc:versionDesc,
			vendorId:vendorId,
			versionPath:versionPath,
			deviceModelId:deviceModelId,
			versionName:versionName,
			id:id,
			fileSize:fileSize,
			MD5:MD5,
			epg_version:epg_version,
			net_type:net_type
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
	    	if(s[0]=="1"){
	    		var showType=$("input[name=showType]").val();
	    		var strpage = "<s:url value='/gtms/stb/resource/softVersion.action'/>?showType="+showType;
	    		window.location.href=strpage;
	    	}
	    });
	}
	
	//ɾ���汾
	function deleteVersion(id)
	{
		if(!confirm("���Ҫɾ��������¼��")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/softVersion!deleteVersion.action'/>";
	    $.post(url,{
			id:id
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
			if(s[0]=="1"){
				var showType=$("input[name=showType]").val();
				var strpage = "<s:url value='/gtms/stb/resource/softVersion.action'/>?showType="+showType;
				window.location.href=strpage;
			}
		});
	}
	
	function query()
	{
		var vendorId = $("select[name=queryVendorId]").val();
		var versionName = $("input[name=queryVersionName]").val();
		var showType=$("input[name=showType]").val();
		window.location.href="<s:url value='/gtms/stb/resource/softVersion.action'/>?"
								+"queryVendorId="+vendorId
								+"&queryVersionName="+versionName
								+"&showType="+showType;
	}
	
	function detailVersion(id)
	{
		window.location.href="<s:url value='/gtms/stb/resource/softVersion!getSoftVersionDetail.action'/>?id="+id;
	}
	
</SCRIPT>

</head>
<body>
<input type="hidden" name="showType" value="<s:property value='showType' />">
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> 
			����ǰ��λ�ã�����汾�ļ�·������
		</TD>
	</TR>
</TABLE>

<br>
<table width="98%" class="querytable" align="center">
	<tr>
		<td class="title_2" align="center">����</td>
		<td width="30%">
			<s:select list="vendorList" name="queryVendorId" headerKey="-1" headerValue="ȫ��" 
				listKey="vendor_id" listValue="vendor_add" value="queryVendorId" cssClass="bk"
				theme="simple">
			</s:select>
		</td>
		<td class="title_2" align="center">�汾����</td>
		<td width="30%">
			<input type="text" name="queryVersionName" maxlength="20" />
		</td>
		<td class="title_2" align="center">
			<button onclick="javascript:query();">��ѯ</button>
		</td>
	</tr>
</table>
<br>

<table  width="98%" class="listtable" align="center">
	<thead>
		<tr bgcolor="#FFFFFF">
			<th align="center" width="8%">����</th>
			<th align="center" width="15%">�����ͺ�</th>
			<th align="center" width="15%">�汾����</th>
			<th align="center" width="20%">�汾����</th>
			<th align="center" width="10%">EPG�汾</th>
			<th align="center" width="10%">������������</th>
			<!-- <th align="center" width="20%">�汾�ļ�·��</th> -->
			<th align="center" width="15%">�汾����ʱ��</th>
			<th align="center" width="8%">����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="versionList!=null && versionList.size()>0">
		<s:iterator value="versionList">
			<TR>
				<td align="center"><s:property value="vendor_add" /></td>
				<td align="center"><s:property value="device_model" /></td>
				<td align="center"><s:property value="version_name" /></td>
				<td align="center"><s:property value="version_desc" /></td>
				<td align="center"><s:property value="epg_version" /></td>
				<td align="center"><s:property value="net_type" /></td>
				<!-- <td align="center"><s:property value="version_path" /></td> -->
				<td align="center"><s:property value="update_time" /></td>
				<td align="center">
					<label onclick="javascript:detailVersion('<s:property value='id'/>');">
						<IMG SRC="<s:url value="/images/view.gif"/>" BORDER='0' ALT='��ϸ��Ϣ' style='cursor:hand'>
					</label>
					
					<s:if test="showType!=1">
						<label onclick="javascript:addeditVersion('0','<s:property value='id'/>',
																	'<s:property value='vendor_id'/>',
																	'<s:property value='version_name'/>',
																	'<s:property value='version_desc'/>',
																	'<s:property value='version_path0'/>',
																	'<s:property value='device_model_id'/>',
																	'<s:property value='file_size'/>',
																	'<s:property value='md5'/>',
																	'<s:property value='epg_version'/>',
																	'<s:property value='net_type'/>');">
							<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭' style='cursor: hand'> 
						</label>
						<s:if test="showType!=2">
							<label onclick="javascript:deleteVersion(<s:property value='id'/>);">
								<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0' ALT='ɾ��' style='cursor: hand'> 
							</label>
						</s:if>
					</s:if>
				</td>
			</TR>
		</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">
					<font color="red">û�а汾�ļ���Ϣ</font>
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="8" align="right">
				<lk:pages url="/gtms/stb/resource/softVersion.action" styleClass="" showType=""
					isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<ms:inArea areaCode="hn_lt" notInMode="true">
		<tr>
			<td colspan="8" align="left">
				<button onclick="javascript:addeditVersion('1','','-1','','','','','','','other','unknown_net');">
					��Ӽ�¼</button>
			</td>
		</tr>
		</ms:inArea>
	</tfoot>
</table>
<br>
<br>

<table class="querytable" width="98%" align="center" style="display: none" id="addedit">
	<tr>
		<td class="title_1" colspan="2">
		<%if("hn_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			�༭��
		<%}else{ %>
			���/�༭��
		<%} %>
			
			<input type="hidden" name="isAdd" value=""> 
			<input type="hidden" name="id" value="">
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%">
			<input type="text" name="versionName" id="versionName" class="bk" value="" size="40" maxlength="40">
			<font color="red">*����С��40</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%">
			<input type="text" name="versionDesc" id="versionDesc" class="bk" value="" size="40" maxlength="80">
			<font color="red">*����С��80</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%">
			<s:select list="vendorList" name="vendorId"
				headerKey="-1" headerValue="��ѡ����" listKey="vendor_id"
				listValue="vendor_add" value="vendorId" cssClass="bk"
				onchange="vendorChange()" theme="simple">
			</s:select> <font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�����ͺ�</TD>
		<TD width="85%">
			<div id="deviceModel">��ѡ����</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾�ļ�·��</TD>
		<TD width="85%">
			<input type="text" name="versionPath" id="versionPath" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�ļ���С</TD>
		<TD width="85%">
			<input type="text" name="fileSize" id="fileSize" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">MD5</TD>
		<TD width="85%">
			<input type="text" name="MD5" id="MD5" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">EPG�汾</TD>
		<TD width="85%">
			<input type="text" name="epg_version" id="epg_version" class="bk" value="" size="16"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">������������</TD>
		<TD width="85%">
			<select name="net_type" class="bk" >
				<option value="unknown_net" selected>δ  ֪</option>
				<option value="public_net">��  ��</option>
				<option value="private_net">ר  ��</option>
			</select>
		</TD>
	</TR>
	<tr>
		<td colspan="2" class="foot" align="right">
			<div class="right">
				<button onclick="javascript:ExecMod();">�ύ</button>
			</div>
		</td>
	</tr>
</table>

</body>
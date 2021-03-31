<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
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
	function ExecMod(){
		var myFile = $("input[@name='upload']").val();
		if(""==myFile){
			alert("��ѡ���ļ�!");
			return false;
		}
		var filePath = $("#filePath").val();
		if(""==filePath){
			$("#notFilePath").html("�������ļ����·��");
			return false;
		}
		//
		var filet = myFile.split(".");
		if(filet.length<2)
		{
			alert("��ѡ���ļ�!");
			return false;
		}
		//�汾�ͺ�
		var deviceModelIds = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelIds = deviceModelIds + $(this).val()+",";
	    });
	    if(deviceModelIds==""){
			alert("��ѡ�������ͺ�!");
			return;
		}
		$("input[@name='deviceModelIds']").attr("value",deviceModelIds);
		//Ŀ��汾
		var pathId = "";
		pathId = $("select[@name=pathId][@checked]").val();
		if(""==pathId){
			alert("��ѡ��Ŀ��汾");
			return false;
		}
		var isVIP = $("input[@name='is_VIP'][@checked]").val();
		var isHotel = $("input[@name='is_Hotel'][@checked]").val();
		var isOther = $("input[@name='is_Other'][@checked]").val();
		if(isVIP=='on'){
			$("input[@name='isVIP']").attr("value",1);
		}else{
			$("input[@name='isVIP']").attr("value",0);
		}
		if(isHotel=='on'){
			$("input[@name='isHotel']").attr("value",1);
		}else{
			$("input[@name='isHotel']").attr("value",0);
		}
		if(isOther=='on'){
			$("input[@name='isOther']").attr("value",1);
		}else{
			$("input[@name='isOther']").attr("value",0);
		}
		if("xls" == filet[filet.length-1])
		{
			var file = myFile.split("\\");
			var fileName = file[file.length-1];
			$("input[@name='uploadFileName']").attr("value",fileName);
			//�������� add by zhangcong@ 2011-09-05
			//����:JSDX_ITV_HTW-REQ-20110824-XMN-003
			$("input[@name='strategyName']").attr("value",$("input[@name='strategyName']").val());
			if($("textarea[@name='taskDetail']").val().length > 127)
			{
				$("textarea[@name='taskDetail']").attr("value",$("textarea[@name='taskDetail']").val().substring(0,127));
			}
			$("form[@name='batchexform']").attr("action","stbSoftUpgrade!batchImportSoftUp.action");
			$("form[@name='batchexform']").submit();
		}
		else
		{
			alert("��֧�ֺ�׺Ϊxls���ļ�");
			return false;
		}
		
		//str = str.substring(0,str.length-1);
	    //var url = "<s:url value='/stb/resource/softUpgrade!execUpload.action'/>?cityId=" + cityId+"&vendorId="+vendorId+"&pathId="+pathId+"&devicetypeId="+str+"&strategyType="+strategyType+"&ipcheck="+ipcheck+"&ipSG="+ipSG;
	    //window.location.href=url;
	}
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	
	function toExport()
	{
		$("form[@name='batchexform']").attr("action","stbSoftUpgrade!downloadTemplate.action");
		$("form[@name='batchexform']").submit();
	}
	
	function queryTask()
	{
		var queryvendorId = $("select[name=queryvendorId]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!initImportSoftUp.action'/>?queryVendorId="+queryvendorId+"&queryVaild="+queryVaild+"&startTime="+startTime+"&endTime="+endTime;
	    window.location.href=url;
	}
	//ʧЧ����
	function cancerTask(status,taskId,version_path,softwareversion){
		var	width=310;    
		var height=150; 
		var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
		if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
			return;
		}
		if(returnVal.substring(0,1)!="1"){
			alert("�û�������֤ʧ��");
			return;
		}
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!cancelBatchTask.action'/>";
	    $.post(url,{
			taskId:taskId,
			status : status
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		queryTask();
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else if(s[0]=="-1"){
	    		$("#cancerButton").attr("disabled","true");
	    		alert(s[1]);
	    	}
	    });
	}
	//ɾ�� ����
	 function delTask(taskId,version_path,softwareversion,strategyName){
        	var queryVendorId = $("select[name=queryvendorId]").val();
            var queryVaild = $("select[name=queryVaild]").val();
            var startTime = $("#start_time").val();
            var endTime = $("#end_time").val();
            var ur = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!deleteSoftUpTask.action'/>";
            var width=310;    
            var height=150; 
			var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>?strategyName="+strategyName;
            var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
            if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
                   return;
             }
            if(returnVal.substring(0,1)!="1"){
				alert("�û�������֤ʧ��");
				return;
		    }
		    $.post(ur,{
				taskId : taskId,
				queryVendorId : queryVendorId,
				queryVaild : queryVaild,
				startTime : startTime,
				endTime : endTime
			},function(ajax){
		    	var s = ajax.split(",");
		    	if(s[0]=="1"){
		    		alert(s[1]);
		    		queryTask();
		    	}else if(s[0]=="0"){
		    		alert(s[1]);
		    	}else{
		    		alert("ɾ������ʧ�ܣ�");
		    	}
	    	});
    }
	function validTask(status,taskId,version_path,softwareversion){
		var	width=310;    
		var height=150; 
		var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
		if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
			return;
		}
		if(returnVal.substring(0,1)!="1"){
				alert("�û�������֤ʧ��");
				return;
		}
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!validExcelBatchTask.action'/>";
	    $.post(url,{
			taskId : taskId,
			status : status
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		queryTask();
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("��������ʧ�ܣ�");
	    	}
	    });
	}
	
	function viewTask(taskId){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("���ڲ�ѯ�����Ե�....");
	    var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getBatchExcelSTS.action'/>"; 
		$.post(url,{
			taskId:taskId
		},function(ajax){	
			$("div[@id='divDetail']").html("");
			$("div[@id='divDetail']").append(ajax);
		});
    }
	
	function viewdetailTask(taskid)
	{
		var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getBatchExcelDetail.action'/>?taskId="+taskid;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    //���������ʺ�
	function exportFailAccount(taskId)
	{
		var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!exportFailAccount.action'/>?taskId="+taskId;
		document.all("childFrm").src = page;
	}
	function initQuery()
	{
		var startT = '<s:property value="startTime"/>';
		var endT = '<s:property value="endTime"/>';
		if('' != startT)
		{
			$("#start_time").val(startT);
			$("#end_time").val(endT);
		}
	}
</SCRIPT>

	</head>
	<body onload="initQuery()">
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã���������ҵ���ʺŰ汾����
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="stbSoftUpgrade!execUpload.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="deviceModelIds" value=""/>
			<input type="hidden" name="isHotel" value=""/>
			<input type="hidden" name="isVIP" value=""/>
			<input type="hidden" name="isOther" value=""/>
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						������������������
					</td>
				</tr>
				<tr>
					<td class="title_2" align="center" width="15%">
						��ѡ���ļ�
					</td>
					<td width="85%" colspan="3">
						<s:file label="�ϴ�" theme="simple" name="upload"></s:file><font color="red">*</font>
						xls��ʽ�ĵ�
						<a href="javascript:void(0);" onclick="toExport();"><font color="red">����ģ��</font></a>
						<input type="hidden" name="uploadFileName" value=""/>
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="15%">
						����
					</TD>
					<TD width="85%" colspan="3">
						<s:select list="vendorList" name="vendorId" headerKey="-1"
							headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
							value="vendorId" cssClass="bk" onchange="vendorChange();getDeviceModel();"
							theme="simple">
						</s:select>
					</TD>
					<TD>
						<font color="red">*</font>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						��Ӧ�ͺ�
					</TD>
					<TD width="85%" colspan="3">
						<div id="adaptModelType">
							 ��ѡ����
						</div>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						Ŀ��汾
					</TD>
					<TD width="85%" colspan="3">
						<div id="targetVersion">
							 ��ѡ����
						</div>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						�Ƶ꼰VIP�û�
					</TD>
					<TD width="85%" colspan="3">
						<input type="checkbox" id="is_Hotel" name="is_Hotel" >�Ƿ�����Ƶ��û�</input>
						<input type="checkbox" id="is_VIP" name="is_VIP" >�Ƿ����VIP�û�</input>
						<input type="checkbox" id="is_Other" name="is_Other" >�Ƿ�������������û�</input>
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						������
					</TD>
					<TD width="85%" colspan="3">
						<input type="text" id="strategyName" name="strategyName" value="" size="26">
					</TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">
						��ע
					</TD>
					<!-- add by zhangcong@ 2011-09-05 ����:JSDX_ITV_HTW-REQ-20110824-XMN-003 -->
					<TD width="85%" colspan="3">
						<textarea id="taskDetail" name="taskDetail" rows="4" cols="30" style="width: 100%"></textarea>
					</TD>
				</TR>
				<tr>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button onclick="ExecMod()">
								����
							</button>
						</div>
					</td>
				</tr>
			</table>
		</s:form>
		<table width="98%" class="querytable" align="center">
			<tr>
				<td class="title_1" width="15%">
						��ʼʱ��
				</td>
				<td width="15%">
					<span id="startTime"><lk:date id="start_time" name="startTime" type="day" defaultDate="" maxDateOffset="0" dateOffset="-15" /></span>
				</td>
				<td class="title_1" width="15%">
					����ʱ��
				</td>
				<td width="15%">
					<span id="endTime"><lk:date id="end_time" name="endTime" dateOffset="0" defaultDate="" type="day" maxDateOffset="0" /></span>
				</td>
				<td class="title_2" align="center">״̬</td>
				<td width="15%">
					<s:select name="queryVaild" value="queryVaild" list="#{'1':'�Ѽ���','0':'��ʧЧ'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="ȫ��" >
						<!--  <option value="-1">All</option>
						<option value="1">��Ч</option>
						<option value="0">ʧЧ</option>-->
					</s:select>
				</td>
				<td class="title_2" align="center"><button onclick="queryTask()">��ѯ</button></td>
			</tr>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="8%">
						������
					</th>
					<th align="center" width="8%">
						������
					</th>
					<th align="center" width="8%">
						����ʱ��
					</th>
					<th align="center" width="8%">
						���Ը���ʱ��
					</th>
					<th align="center" width="8%">
						״̬
					</th>
					<th align="center" width="12%">
						����
					</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null">
                <s:if test="tasklist.size()>0">
                        <tbody>
                                <s:iterator value="tasklist">
                                        <tr>
                                                <td align="center"><s:property value="acc_loginname" /></td>
                                                <td align="center"><s:property value="strategyName" /></td>
                                                <td align="center"><s:property value="add_time" /></td>
                                                <td align="center"><s:property value="mod_time" /></td>
                                                <s:if test='load_status=="1"'>
                                                <s:if test='task_status=="1"'>
	                                                <td align="center">�Ѽ���</td>
	                                                <td align="center">
	                                                   <s:if test='accoid==account_id||areaId=="1"'>
	                                                     <button name="cancerButton" id="cancerButton"
	                                                      		onclick="javascript:cancerTask(0,'<s:property value="task_id"/>',
	                                                      				'<s:property value="version_path" />',
	                                                      				'<s:property value="softwareversion" />')">
	                                                              		  ʧЧ
	                                                      </button>
	                                                   </s:if>
	                                                      <button name="delButton" disabled="disabled"
	                                                             onclick="javascript:delTask('<s:property value="task_id" />',
								                                                             '<s:property value="version_path" />',
								                                                             '<s:property value="softwareversion" />',
								                                                             '<s:property value="strategyName" />')">
	                                                        			ɾ��
	                                                       </button>
	                                                        <button name="viewButton"
	                                                                onclick="javascript:viewTask('<s:property value="task_id"/>')">
	                                                        	�鿴���</button>
	                                                        <button name="viewDetailButton"
	                                                                onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
	                                                    	    �鿴��ϸ</button>
	                                                </td>
                                                </s:if>
                                                <s:else>
                                                        <td align="center">��ʧЧ</td>
                                                        <td align="center">
	                                                        <s:if test='accoid==account_id||areaId=="1"'>
	                                                                <button name="cancerButton"  id="cancerButton"
	                                                                        onclick="javascript:cancerTask(1,'<s:property value="task_id" />',
	                                                                        							  '<s:property value="version_path" />',
	                                                                        							  '<s:property value="softwareversion" />')">
	                                                            		   ����
	                                                            	</button>
	                                                        </s:if>

                                                        <button name="delButton"
                                                                onclick="javascript:delTask('<s:property value="task_id" />',
							                                                                '<s:property value="version_path" />',
							                                                                '<s:property value="softwareversion" />',
							                                                                '<s:property value="strategyName" />')">
                                                        		ɾ��
                                                        </button>
                                                        <button name="viewButton"
                                                                onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                                       			 �鿴���
                                                       	</button>
                                                        <button name="viewDetailButton"
                                                                onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
                                                      		  �鿴��ϸ</button>
                                                        </td>
                                                </s:else>
                                                </s:if>
                                                <s:else>
                                                <td align="center">���������У����Ժ�</td>
                                                        <td align="center">
	                                                     <button name="cancerButton"  disabled="disabled">
	                                                            		   ����
	                                                     </button>
                                                        <button name="delButton" disabled="disabled" >
                                                        		ɾ��
                                                        </button>
                                                        <button name="viewButton" disabled="disabled" >
                                                       			 �鿴���
                                                       	</button>
                                                        <button name="viewDetailButton" disabled="disabled">
                                                      		  �鿴��ϸ</button>
                                                        </td>
                                                </s:else>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="8" align="right"><lk:pages
                                                url="/gtms/stb/resource/stbSoftUpgrade!initImportSoftUp.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="false" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="8">
							<font color="red">û�ж��Ƶ�����</font>
						</td>
					</tr>
				</tbody>
			</s:else>
		<tr STYLE="display: none">
			<td colspan="8">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
</s:if>
		</table>
		<br>
		<br>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 150px; 
			left: 300px; width: 60%; border-width: 1; border-style: ridge;
			 background-color: #eeeeee; padding-top: 10px; display: none"></div>
		<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 250px; width: 55%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table class="querytable" align="center" width="100%">
				<tr>
					<td width="30%" id="ventd" class="title_2" align="center"> 
					</td>
					<td id="softVershow" class="title_2" align="center">	
					</td>
				</tr>
				<tr>
					<td colspan="2" class="title_2" align="center">
						<button name="softdivtbn" onclick="softdivcl()">
						�ر�
						</button>
					</td>
				</tr>
			</table>				
		</div>
	</body>
<script type="text/javascript">
function vendorChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getTargetVersion.action'/>";
		if(vendorId!="-1"){
			$("div[@id='targetVersion']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						$("div[@id='targetVersion']").append("<select name='pathId' class='bk' style='width: 800px'>");
						$("select[@name='pathId']").append("<option value='-1' selected>==��ѡ��==</option>");
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							option = "<option value='"+xValue+"'>=="+xText+"==</option>";
							$("select[@name='pathId']").append(option);
						}
					}else{
						$("div[@id='targetVersion']").append("�ó���û�п������İ汾�ļ���");
					}
				}else{
					$("div[@id='targetVersion']").append("�ó���û�п������İ汾�ļ���");
				}
			});
		}else{
			$("div[@id='targetVersion']").html("��ѡ����");
		}
	}
	/**
	**���ݳ��̻�ȡ�豸�ͺţ����Ը�ѡ�����ʽ���ֳ���
	**/
   function getDeviceModel(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='adaptModelType']").html("");
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
							checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"+xValue+"'>"+xText+"  ";
							$("div[@id='adaptModelType']").append(checkboxtxt);
						}
					}else{
						$("div[@id='adaptModelType']").append("�ó���û�ж�Ӧ�ͺţ�");
					}
				}else{
					$("div[@id='adaptModelType']").append("�ó���û�ж�Ӧ�ͺţ�");
				}
			});
		}else{
			$("div[@id='adaptModelType']").html("��ѡ����");
		}
	}
</script>
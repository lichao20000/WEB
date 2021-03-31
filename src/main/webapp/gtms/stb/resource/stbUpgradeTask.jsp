<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
		<SCRIPT LANGUAGE="JavaScript">
		
		//ϵͳ����
		var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
		
	$(function(){
		if(instAreaName == "jx_dx")
		{
			$("tr[@id='table_vonder']").hide();
		}
		if(instAreaName == "xj_dx")
		{
			var queryvendorId = $("select[name=queryvendorId]").val();
			change_select('deviceModel','-1');
		}
		
	});
	
	function cancerTask(taskId,version_path,softwareversion){
      if( 'sx_lt'!=instAreaName)
      {
        var	width=310;
        var height=150;
        var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
        //url=url+"?versionPath="+version_path;
        //url=url+"&softwareversion="+softwareversion;
        //�޸ı������룬yl
        var obj = new Object();
        obj.versionPath = version_path;
        obj.softwareversion = softwareversion
        var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
        if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
          return;
        }
        if(returnVal.charAt(0)!="1"){
          alert("�û�������֤ʧ��");
          return;
        }
      }
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!cancerTask.action'/>";
	    $.post(url,{
			taskId:taskId,
			isImport : isImport
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!initForUpgradeTask.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("ʧЧ����ʧ�ܣ�");
	    	}
	    });
	}
	
	function validTask(taskId,version_path,softwareversion){
		var	width=310;    
		var height=150; 
		if ("-" == softwareversion ){
			//����Itv������������������������ݴ��ֶ��������Ƿ��ǵ������񣬴˴������أ��ں�̨�ж��Ƿ��ǵ�������
			if("jx_dx"==instAreaName)
			{
			alert("��ע�⣺�����ļ�������ʧЧ���޷�����輤�������¶��ƣ��� ����ἤ��ʧ��");
			}else
			{
				alert("�����ļ�������ʧЧ���޷�����輤�������¶��ƣ���");
				return
			}
		}
		if('sx_lt'!=instAreaName){
          var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
          url=url+"?versionPath="+version_path;
          url=url+"&softwareversion="+softwareversion;
          var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
          if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
            return;
          }
          if(returnVal.charAt(0)!="1"){
            alert("�û�������֤ʧ��");
            return;
          }
		}

		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!validTask.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!initForUpgradeTask.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("��������ʧ�ܣ�");
	    	}
	    });
	}
	
	function deleteTask(taskId,version_path,softwareversion){
		var	width=310;    
		var height=150; 
		var isImport = 0;
		if ("-" == softwareversion){
			isImport = 1;
		}
		if('sx_lt'!=instAreaName){
          var url="<s:url value='/gtms/stb/resource/stbSoftUpgrade!validateCurUser.action'/>";
          url=url+"?versionPath="+version_path;
          url=url+"&softwareversion="+softwareversion;
          var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
          if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
            return;
          }
          if(returnVal.charAt(0)!="1"){
            alert("�û�������֤ʧ��");
            return;
          }
		}
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!deleteTask.action'/>";
	    $.post(url,{
			taskId:taskId,
			isImport : isImport
		},function(ajax){
			var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!initForUpgradeTask.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("ɾ������ʧ�ܣ�");
	    	}
	    });
	}
	function viewTask(taskId,importType,taskType){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("���ڲ�ѯ�����Ե�....");
	    var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!getCountSoftupTaskResult.action'/>";
	    
	    if(importType == undefined || importType == "" || importType == null){
	    	$.post(url,{
				taskId:taskId
			},function(ajax){
				$("div[@id='divDetail']").html("");
				$("div[@id='divDetail']").append(ajax);
			});
	    }else{
	    	$.post(url,{
				taskId:taskId,
				taskImportType:importType,
				taskType:taskType
			},function(ajax){
				$("div[@id='divDetail']").html("");
				$("div[@id='divDetail']").append(ajax);
			});
	    }
		
    }
    
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
   
	function softdivcl()
	{
		$("div[@id='div_css']").hide();
	}
	function viewdetailTask(taskid,taskType)
	{
		var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!queryTaskDetailById.action'/>?taskId="+taskid+"&taskType="+taskType;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	function queryTask()
	{
		var querycityId = $("select[name=querycityId]").val();
		var queryvendorId = $("select[name=queryvendorId]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var queryTask = $("input[name=queryTask]").val();
		var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!initForUpgradeTask.action'/>?queryCityId=" + querycityId+"&queryVendorId="+queryvendorId+"&queryVaild="+queryVaild+"&taskDesc="+queryTask;
		
		if("sx_lt" == instAreaName){
			var taskType =  $("select[name=taskType]").val();
			url = url + "&taskType="+taskType;
		}
		if("xj_dx"==instAreaName){
		var source_devicetypeId = $("select[name=source_devicetypeId]").val();
		var softwareversion = $("input[name=softwareversion]").val();
		if(source_devicetypeId='-1'){
			source_devicetypeId="";
		}
		var startTime = $("input[name=startTime]").val();
		var endTime = $("input[name=endTime]").val();
		if(endTime!=''){
			endTime=endTime+" 23:59:59"
		}
			url=url+"&source_devicetypeId="+source_devicetypeId+"&softwareversion="+softwareversion+"&startTime="+startTime+"&endTime="+endTime;
		}
		window.location.href=url;
	}
	
	function updateTaskDesc(taskid,taskDesc)
	{
		var page = "<s:url value='/gtms/stb/resource/updateTaskDesc.jsp'/>?taskid=" + taskid + "&taskDesc=" + taskDesc ;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}

	//ȥ���ո�
	function trim(str){
      return str.replace(/(^\s*)|(\s*$)/g,"");
	}

	function change_select(type,selectvalue){
	switch (type){
		case "platform":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='queryvendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='queryvendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
			});
			break;	
		case "hardwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			var vendorId = $("select[@name='queryvendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='hardwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='hardwareversion']"),selectvalue);
			});
			break;
		case "softwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetypeByHardVersion.action"/>";
			var hardwareversion = $("select[@name='hardwareversion']").val();
			if("-1"==hardwareversion){
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==����ѡ��Ӳ���汾==</option>");
				break;
			}
			$.post(url,{
				gwShare_hardwareVersion : hardwareversion
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
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
				alert("�豸�ͺż���ʧ�ܣ�");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
</SCRIPT>

	</head>
	<body>
	<TABLE width="98%" align="center" class="querytable">
	   <TR>
		  <TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã�����������������ѯ
		  </TD>
	   </TR>
      </TABLE>	
		<table width="98%" class="querytable" align="center">
			<tr id ="table_vonder">
				<td class="title_2" align="center">����</td>
				<td >
					<s:select list="cityList" name="querycityId" listKey="city_id" listValue="city_name"
						value="queryCityId" cssClass="bk" theme="simple"></s:select>
				</td>
				<td class="title_2" align="center">����</td>
				<td >
					<s:select list="vendorList" name="queryvendorId" headerKey="-1"
						headerValue="ȫ��" listKey="vendor_id" listValue="vendor_add"
						value="queryVendorId" cssClass="bk" theme="simple" onchange="change_select('deviceModel','-1')"></s:select>
				</td>
			</tr>
			
			<%if (LipossGlobals.inArea("xj_dx")) {%>
			
			<tr id ="table_vonder">
				<td class="title_2" align="center">�豸�ͺ�</td>
				<td >
					<select name="deviceModelId" class="bk" onchange="change_select('hardwareversion','-1')">
						<option value="-1">����ѡ����</option>
					</select>
				</td>
				<td class="title_2" align="center">Ӳ���汾</td>
				<td >
					<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
						<option value="-1">����ѡ���豸�ͺ�</option>
					</select>
				</td>
			</tr>
			<tr id ="table_vonder">
				<td class="title_2" align="center">ԭʼ�汾</td>
				<td >
					<select name="source_devicetypeId" class="bk">
						<option value="-1">����ѡ��Ӳ���汾</option>
					</select>
				</td>
				<td class="title_2" align="center">Ŀ��汾</td>
				<td >
					<input type="text" id="softwareversion" name="softwareversion" class="bk" value="">
				</td>
			</tr>
			<tr id ="table_vonder">
				<td class="title_2" align="center">��ʼʱ��</td>
				<td >
					<input type="text" name="startTime" id="startTime" readonly
											value="" class=bk>
					<img name="shortDateimg" onClick="WdatePicker({el:'startTime',dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��">
				</td>
				<td class="title_2" align="center">����ʱ��</td>
				<td >
					<input type="text" name="endTime" id="endTime" readonly
											value="" class=bk>
					<img name="shortDateimg" onClick="WdatePicker({el:'endTime',dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��">
				</td>
			</tr>
			
			<% }%>
			
			<tr>
				<td class="title_2" align="center">״̬</td>
				<td >
					<s:select name="queryVaild" value="queryVaild" list="#{'1':'�Ѽ���','0':'��ʧЧ'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="ȫ��" >
						<!--  <option value="-1">All</option>
						<option value="1">��Ч</option>
						<option value="0">ʧЧ</option>-->
					</s:select>
				</td>
				
				<td class="title_2" align="center">��������</td>
				<td >
					<input type="text" id="queryTask" name="queryTask" class="bk" value="">
				</td>
			</tr>
			<%if (LipossGlobals.inArea("sx_lt")) {%>
			<tr>
				<td class="title_2" align="center">����</td>
				<td >
					<s:select name="taskType" value="taskType" list="#{'0':'����������ѯ','1':'����������ѯ'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="ȫ��" >
					</s:select>
				</td>
				<td class="title_2" align="center" colspan="2"><button onclick="queryTask()">��ѯ</button></td>
			</tr>
			<%}else{ %>
			<tr>
				<td class="title_2" align="center" colspan="4"><button onclick="queryTask()">��ѯ</button></td>
			</tr>
			<%} %>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
				<!-- ����itv��������չʾ���棬��չʾ���ء����̡��汾 -->
					<% if (LipossGlobals.inArea("xj_dx")) {%>
					<th align="center" width="10%">
						TaskID
					</th>
					<% }%>
					<% if (!LipossGlobals.inArea("jx_dx")) {%>
					 <%if(LipossGlobals.inArea("sx_lt")){ %>
				     <!-- ֻ�а�����������ʱ�����Ҫ��ʾ������Ϣ�������̵���������ʱ��û��������Ϣ -->
                      <s:if test="taskType != 1">
					   <th align="center" width="10%">����</th>
					  </s:if>
					<%}else{ %>
					 <th align="center" width="10%">����</th>
					<%}%>
					<th align="center" width="10%">
						����
					</th>
					<th align="center" width="15%">
						Ŀ��汾
					</th>
					<% }%>
					<th align="center" width="10%">
						������
					</th>
					<th align="center" width="10%">
						����ʱ��
					</th>
					<th align="center" width="10%">
						����ʱ��
					</th>
					<%
						if (LipossGlobals.inArea("hb_lt") || LipossGlobals.inArea("jx_dx") || LipossGlobals.inArea("nx_dx")) {
					%>
					<th align="center" width="10%">��������</th>
					<%
						}
					%>
					<th align="center" width="10%">
						״̬
					</th>
					<th align="center" width="15%">
						����
					</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null">
				<s:if test="tasklist.size()>0">
					<tbody>
						<s:iterator value="tasklist">
							<tr>
							<% if (LipossGlobals.inArea("xj_dx")) {%>
								<td align="center">
									<s:property value="task_id" />
								</td>
							<% }%>
							<!-- ����itv��������չʾ���棬��չʾ���س��̰汾 -->
							<% if (!LipossGlobals.inArea("jx_dx")) {%>
							 <%if(LipossGlobals.inArea("sx_lt")){ %>
						     <s:if test="taskType != 1">
						      <td align="center"><s:property value="city_name" /></td>
						     </s:if>
							<%}else{ %>
							  <td align="center"><s:property value="city_name" /></td>
							<%} %>
								<td align="center">
									<s:property value="vendor_add" />
								</td>
								<td align="center">
									<s:property value="softwareversion" />
								</td>
							<%} %>
								<td align="center">
									<s:property value="acc_loginname" />
								</td>
								<td align="center">
									<s:property value="record_time" />
								</td>
								<td align="center">
									<s:property value="update_time" />
								</td>
								<ms:inArea areaCode="hb_lt">
									<td align="center"><s:property value="task_desc" /></td>
								</ms:inArea>
								<ms:inArea areaCode="jx_dx">
									<td align="center"><s:property value="task_desc" /></td>
								</ms:inArea>
								<ms:inArea areaCode="nx_dx">
									<td align="center"><s:property value="task_desc" /></td>
								</ms:inArea>
								<s:if test='valid=="1"'>
									<td align="center">
										�Ѽ���
									</td>
									<td align="center">
										<s:if test='accoid==acc_oid||areaId=="1"'>
										<ms:inArea areaCode="xj_dx" notInMode="true">
											<button name="cancerButton"
												onclick="javascript:cancerTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ʧЧ
											</button>
										</ms:inArea>
											<button name="cancerButton"
												onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ɾ��
											</button>
										</s:if>
										<button name="viewButton"
											onclick="javascript:viewTask('<s:property value="task_id"/>','<s:property value="import_type"/>','<s:property value="type"/>')">
											�鿴���
										</button>
										<s:if test='vendor_id!="-1"'>
											<button name="viewDetailButton"
												onclick="javascript:viewdetailTask('<s:property value="task_id"/>','<s:property value="type"/>')">
												�鿴��ϸ
											</button>
										</s:if>
										<s:else>
											<% if (LipossGlobals.inArea("xj_dx")) {%>
												<button name="viewDetailButton"
												onclick="javascript:viewdetailTask('<s:property value="task_id"/>','')">
												�鿴��ϸ
												</button>
											<% }%>
										</s:else>
										<ms:inArea areaCode="hb_lt">
											<button name="updateTaskDesc"
												onclick="javascript:updateTaskDesc('<s:property value="task_id"/>','<s:property value="task_desc"/>')">
												�༭����
											</button>
										</ms:inArea>
										<ms:inArea areaCode="jx_dx">
											<button name="updateTaskDesc"
												onclick="javascript:updateTaskDesc('<s:property value="task_id"/>','<s:property value="task_desc"/>')">
												�༭����
											</button>
										</ms:inArea>
									</td>
								</s:if>
								<s:else>
									<td align="center">
										��ʧЧ
									</td>
									<td align="center">
										<s:if test='accoid==acc_oid||areaId=="1"'>
										<ms:inArea areaCode="xj_dx" notInMode="true">
											<button name="cancerButton"
												onclick="javascript:validTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												����
											</button>
										</ms:inArea>	
											<button name="cancerButton"
												onclick="javascript:deleteTask('<s:property value="task_id" />','<s:property value="version_path" />','<s:property value="softwareversion" />')">
												ɾ��
											</button>
										</s:if>
										<button name="viewButton"
											onclick="javascript:viewTask('<s:property value="task_id"/>','<s:property value="import_type"/>','<s:property value="type"/>')">
											�鿴���
										</button>
										<s:if test='vendor_id!="-1"'>
										<button name="viewDetailButton"
											onclick="javascript:viewdetailTask('<s:property value="task_id"/>','<s:property value="type"/>')">
											�鿴��ϸ
										</button>
										</s:if>
										<s:else>
											<% if (LipossGlobals.inArea("xj_dx")) {%>
												<button name="viewDetailButton"
												onclick="javascript:viewdetailTask('<s:property value="task_id"/>','')">
												�鿴��ϸ
												</button>
											<% }%>
										</s:else>
										<ms:inArea areaCode="hb_lt">
											<button name="updateTaskDesc"
												onclick="javascript:updateTaskDesc('<s:property value="task_id"/>','<s:property value="task_desc"/>')">
												�༭����
											</button>
										</ms:inArea>
										<ms:inArea areaCode="jx_dx">
											<button name="updateTaskDesc"
												onclick="javascript:updateTaskDesc('<s:property value="task_id"/>','<s:property value="task_desc"/>')">
												�༭����
											</button>
										</ms:inArea>
									</td>
								</s:else>

							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr bgcolor="#FFFFFF">
								<%
									if (LipossGlobals.inArea("hb_lt")||LipossGlobals.inArea("jx_dx")||LipossGlobals.inArea("xj_dx") || LipossGlobals.inArea("nx_dx")) {
								%>
								<td colspan="9" align="right">
									<lk:pages url="/gtms/stb/resource/stbSoftUpgrade!initForUpgradeTask.action"
										styleClass="" showType="" isGoTo="true" changeNum="false" />
								</td>
								<%
									}else{
								%>
								<td colspan="8" align="right">
									<lk:pages url="/gtms/stb/resource/stbSoftUpgrade!initForUpgradeTask.action"
										styleClass="" showType="" isGoTo="true" changeNum="false" />
								</td>
								<%
									}
								%>
						</tr>
					</tfoot>
				</s:if>
				<s:else>
					<tbody>
						<tr>
						<%if (LipossGlobals.inArea("xj_dx") || LipossGlobals.inArea("nx_dx") ) {%>
							<td colspan="9">
								<font color="red">û�ж��Ƶ�����</font>
							</td>
						<%}else{%>
							<td colspan="8">
								<font color="red">û�ж��Ƶ�����</font>
							</td>
						<%}%>
						</tr>
					</tbody>
				</s:else>
			</s:if>
			<s:else>
				<tbody>
					<tr>
						<%if (LipossGlobals.inArea("xj_dx") || LipossGlobals.inArea("nx_dx")) {%>
							<td colspan="9">
								<font color="red">û�ж��Ƶ�����</font>
							</td>
						<%}else{%>
							<td colspan="8">
								<font color="red">û�ж��Ƶ�����</font>
							</td>
						<%}%>
					</tr>
				</tbody>
			</s:else>


		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
		<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table class="querytable" align="center" width="100%">
				<tr>
					<td width="120px" class="title_2" align="center">
						��ѡ������汾�ͺ�
					</td>
					<td id="softVershow" class="title_2" align="center">
					</td>
				</tr>
				<tr>
					<td colspan="2" class="title_2" align="center">
						<button align="center" name="softdivtbn" onclick="softdivcl()">
						�ر�
						</button>
					</td>
				</tr>
			</table>
		</div>
	</body>
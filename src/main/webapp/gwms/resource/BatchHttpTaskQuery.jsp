<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	/* function viewTask(taskId){ 
		var page = "<s:url value='/gwms/resource/batchHttpTestMana!getTestSpeedTaskResult.action'/>?taskId="+taskId;// getShowPictureConfigResult
		var win = window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
		win.location.reload();
		//window.location.href=page;
    } */
	
    $(function() {
    	var type='<s:property value="type" />';
    	$(" select option[value='"+type+"']").attr("selected","selected");
    }); 
    
	function viewTask(taskId){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("���ڲ�ѯ�����Ե�....");
	    var url = "<s:url value='/gwms/resource/batchHttpTestMana!getTaskCount.action'/>";
		$.post(url,{
			taskId:taskId
		},function(ajax){
			$("div[@id='divDetail']").html("");
			$("div[@id='divDetail']").append(ajax);
		});
    }
	
	function viewdetailTask(taskid)
	{
		var page = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getShowPictureConfigDetail.action'/>?taskId="+taskid;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	function disableTask(taskId){
		if(confirm("ȷ��Ҫʹ����ʧЧ��") == true){     //����û�������ʧЧ��ť 
			var url = "<s:url value='/gwms/resource/batchHttpTestMana!doDisable.action'/>";
			$.post(url,{
				taskId : taskId
			},function(ajax){
				 if(ajax=='1'){
					 alert("ʧЧ�����ɹ�");
				 }else{
					 alert("ʧЧ����ʧ��");
				 }
				 queryTask();
			});   
		}
	}
	
	function deleteTask(taskId){
		if(confirm("ȷ��Ҫɾ��ʹ������") == true){     //����û�������ɾ����ť 
			var url = "<s:url value='/gwms/resource/batchHttpTestMana!doDelete.action'/>";
			$.post(url,{
				taskId : taskId
			},function(ajax){
				 if(ajax=='1'){
					 alert("ɾ�������ɹ�");
				 }else{
					 alert("ɾ������ʧ��");
				 }
				 queryTask();
			});   
		}
	}
	
	function showDesc(taskId,task_desc){
		$("div[@id='divDescDetail']").show();
		$("#descInput").val(task_desc);
		$("#taskIdInput").html(taskId);
	}
	
	
	function commitDesc(){
		var task_id = $("#taskIdInput").html();
		var desc = $("#descInput").val();
		var url = "<s:url value='/gwms/resource/batchHttpTestMana!commitDesc.action'/>";
		$.post(url,{
			taskId : task_id,
			task_desc:desc
		},function(ajax){
			 if(ajax=='1'){
				 alert("�����ύ�ɹ�");
			 }else{
				 alert("�����ύʧ��");
			 }
		});
		//�ύ�������޸������Ի���
		closeDescDetail();
		//���²�ѯ
		queryTask();
	}
	
	
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
    function closeDescDetail(){
		$("div[@id='divDescDetail']").hide();
	}
    
    
    
    
    
   	function queryTask()
	{
   		var starttime = $.trim($("input[@name='startTime']").val());// ��ʼʱ��(ע��ʱ��)
	    var endtime = $.trim($("input[@name='endTime']").val());    // ����ʱ��(ע��ʱ��)
	    
	    if(starttime > endtime){
	    	alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��");
	    	return;
	    }
		var taskName = $("input[name=taskName]").val();
		var accName = $("input[name=accName]").val();
		//var queryVaild = $("select[name=queryVaild]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var type = $("select[name=type]").val();
		var url = "<s:url value='/gwms/resource/batchHttpTestMana!init.action'/>?taskName="+taskName+"&accName="+accName+"&startTime="+startTime+"&endTime="+endTime+"&type="+type;
	    window.location.href=url;
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
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã��������������ѯ����
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="batchHttpTestMana!init.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="deviceModelIds" value=""/>
			<table width="98%" class="querytable" align="center">
			<tr>
				<td class="title_1" width="15%">
						��������
				</td>
				<td width="35%">
					<input type="text" id="taskName" name="taskName" value="<s:property value="taskName" />" size="26">
				</td>
				<td class="title_1" width="15%">
						������
				</td>
				<td width="35%">
					<input type="text" id="accName" name="accName" value="<s:property value="accName" />" size="26">
				</td>
			</tr>
			<tr>
			<td class="title_1" width="15%">
						��ʼʱ��
				</td>
				<td>
					<input type="text" name="startTime" id="start_time" class='bk' readonly
						value="<s:property value='startTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
						border="0" alt="ѡ��" />
				</td>
				<!-- 
				<td width="35%" >
					<span id="startTime"><lk:date id="start_time" name="startTime" type="day" defaultDate="" maxDateOffset="0" dateOffset="-15" /></span>
				</td>
				 -->
				<td class="title_1" width="15%">
					����ʱ��
				</td>
				<td>
					<input type="text" name="endTime" id="end_time" class='bk' readonly
						value="<s:property value='endTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
						border="0" alt="ѡ��" />
				</td>
				<!-- 
				<td width="35%">
					<span id="endTime"><lk:date id="end_time" name="endTime" dateOffset="0" defaultDate="" type="day" maxDateOffset="0" /></span>
				</td>
				 -->
			</tr>
			<ms:inArea areaCode="hb_lt" notInMode="false">
			<%if (!("ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))){%>
			<tr>
				<TD colspan="1" class="title_1" >
					��������ģʽ��
				</TD>
				<TD colspan="3" class="" >
					<SELECT name="type">
							<OPTION value=''>==��ѡ��==</OPTION>
							<OPTION value='1'>���в���</OPTION>
							<OPTION value='2' >���в���</OPTION>
					</SELECT>
				</TD>
			</tr>
			<%} %>
			</ms:inArea>
			<tr>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button onclick="queryTask()">
								�� ѯ
							</button>
						</div>
					</td>
				</tr>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="20%">
						��������
					</th>
					<th align="center" width="26%">
						��������
					</th>
					<th align="center" width="7%">
						������
					</th>
					<th align="center" width="20%">
						����ʱ��
					</th>
					<th align="center" width="7%">
						����״̬
					</th>
					<th align="center" width="20%">
						����
					</th>
				</tr>
			</thead>
			<s:if test="tasklist!=null">
                <s:if test="tasklist.size()>0">
                        <tbody>
                                <s:iterator value="tasklist">
                                        <tr>
                                            <td align="center"><s:property value="task_name" /></td>
                                            <td align="center"><s:property value="task_desc_short" /></td>
                                            <td align="center"><s:property value="acc_loginname" /></td>
                                            <td align="center"><s:property value="add_time" /></td>
                                            <td align="center"><s:property value="task_status" /></td>
                                            <td align="center">
                                              <button name="viewButton" 
                                                      onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                              		�鿴���
                                              	</button>&nbsp;&nbsp;
                                          	   <button  onclick="javascript:disableTask('<s:property value="task_id"/>')">
                                          	   ʧЧ</button>
                                          	   <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	  ɾ��</button>
                                          	  <button  onclick="javascript:showDesc('<s:property value="task_id"/>','<s:property value="task_desc"/>')">
                                          	 �༭����</button>
                                             </td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="6" align="right"><lk:pages
                                                url="/gwms/resource/batchHttpTestMana!init.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="false" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="6">
							<font color="red">û�ж��Ƶ�����</font>
						</td>
					</tr>
				</tbody>
			</s:else>

</s:if>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
		<div id="divDescDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 30%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table width="100%" class="listtable">
				<thead>
					<tr>
						<th align="center">
							��������
						</th>
					</tr>
				</thead>
				<tbody>
						<tr>
							<td align="center">
								<textarea id="descInput" style="width:300px;height:80px;"></textarea>
							</td>
							<td id="taskIdInput" style="display: none"></td>
						</tr>
				</tbody>
				<tfoot>
					<tr bgcolor="#FFFFFF">
						<td align="right">
							<button onclick="javascript:commitDesc();">
								�ύ
							</button>
							<button onclick="javascript:closeDescDetail();">
								�ر�
							</button>
						</td>
						
					</tr>
				</tfoot>
			</table>
		</div>
</s:form>
</body>

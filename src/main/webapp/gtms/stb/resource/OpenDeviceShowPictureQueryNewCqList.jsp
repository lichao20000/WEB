<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<style type="text/css">
td {
	white-space: nowrap;
	overflow: hidden;
}
</style>

<script language="JavaScript">
	
$(function(){
	parent.dyniframesize();
});	
function viewTask(taskId){ 
	var page = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!getShowPictureConfigResult.action'/>?taskId="+taskId;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
function viewdetailTask(taskid)
{
	var page = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!getTaskDetail.action'/>?taskId="+taskid;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

function deleteTask(taskId){
	if(confirm("ɾ���ǲ��ɻָ��ģ�ȷ��Ҫɾ����") == true){     //����û�������ȷ����ť 
		var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!doDelete.action'/>";
		$.post(url,{
			taskId : taskId
		},function(ajax){
			 if(ajax=='1'){
				 alert("ɾ���ɹ�");
			 }else{
				 alert("ɾ��ʧ��");
			 }
			 parent.query();
		});   
	}
}
function takeeffect(taskId,status){
	/* var	width=310;    
	var height=150; 
	var url="<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!validateCurUser.action'/>";
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
		return;
	}
	if(returnVal.charAt(0)!="1"){
		alert("�û�������֤ʧ��");
		return;
	} */
		var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!updatestatus.action'/>";
		$.post(url,{
			taskId : taskId,
			status : status
		},function(ajax){
			alert(ajax);
			parent.query();
		});   
}
function loseefficacy(taskId,status){
	/* var	width=310;    
	var height=150; 
	var url="<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!validateCurUser.action'/>";
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
		return;
	}
	if(returnVal.charAt(0)!="1"){
		alert("�û�������֤ʧ��");
		return;
	} */
		var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!updatestatus.action'/>";
		$.post(url,{
			taskId : taskId,
			status : status
		},function(ajax){
			 alert(ajax);
			 parent.query();
		});   
}
function CloseDetail(){
	$("div[@id='divDetail']").hide();
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
</script>
</head>

<body>
	<table class="listtable" width="100%" align="center">
		<thead>
			<tr>
				<th align="center" width="20%">
						��������
					</th>
					<th align="center" width="20%">
						������
					</th>
					<th align="center" width="20%">
						����ʱ��
					</th>
					<th align="center" width="20%">
						���״̬
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
                                            <td align="center"><s:property value="acc_loginname" /></td>
                                            <td align="center"><s:property value="add_time" /></td>
                                            	<s:if test="status==1">
                                            <td align="center">�����</td> </s:if><s:else > <td align="center">δ���</td></s:else>
                                            <td>&nbsp;&nbsp;
                                              <button name="viewButton" 
                                                      onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                              		�鿴���
                                              	</button>&nbsp;&nbsp;
                                              <button name="viewDetailButton"
                                                      onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
                                          			    �鿴��ϸ
                                          	   </button>&nbsp;&nbsp;
                                          	  <s:if test="statushow!=null&&statushow.size()>0">
                                          	   <s:if test="status==1">
                                          	     <button name="viewDetailButton"
                                                      onclick="javascript:takeeffect('<s:property value="task_id"/>','<s:property value="status"/>')">
                                          			ȡ��
                                          	   </button>&nbsp;&nbsp;
                                          	   </s:if><s:elseif test="status==2">
                                          	     <button name="viewDetailButton"
                                                      onclick="javascript:loseefficacy('<s:property value="task_id"/>','<s:property value="status"/>')">
                                          			    ���
                                          	   </button>&nbsp;&nbsp;
                                          	   </s:elseif></s:if>
                                          	   
                                            	 <s:if test="statushow!=null&&statushow.size()>0">
                                            	 <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	   ɾ��</button>
                                            	</s:if><s:else>
                                            	<s:if test="status==2">
                                          	   <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	   ɾ��</button></s:if>
                                          	   </s:else> 
                                             </td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="5" align="right">
                                        <lk:pages url="/gtms/stb/resource/OpenDeviceShowPictureNew!init.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="true" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="5">
							<font color="red">û�ж��Ƶ�����</font>
						</td>
					</tr>
				</tbody>
			</s:else>

</s:if>
	</table>
</body>

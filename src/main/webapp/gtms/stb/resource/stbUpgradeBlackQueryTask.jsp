<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
    
	//������ֶ���ҳ��
	function getdetailList(task_id){
		var page = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!getdetailList.action'/>?task_id="+task_id;
		window.open(page,"","left=20,top=20,width=700,height=600,resizable=yes,scrollbars=yes");
	}
	
	function disableTask(task_id){
		if(confirm("ȷ��Ҫʹ����ʧЧ��") == true){     //����û�������ʧЧ��ť 
			var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!doDisable.action'/>";
			$.post(url,{
				task_id : task_id
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
	
	function ableTask(task_id){
		if(confirm("ȷ��Ҫ������Ч��") == true){     //����û�������ʧЧ��ť 
			var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!doAble.action'/>";
			$.post(url,{
				task_id : task_id
			},function(ajax){
				 if(ajax=='1'){
					 alert("��Ч�����ɹ�");
				 }else{
					 alert("��Ч����ʧ��");
				 }
				 queryTask();
			});   
		}
	}
    
   	function queryTask()
	{
   		var starttime = $.trim($("input[@name='startTime']").val());// ��ʼʱ��(ע��ʱ��)
	    var endtime = $.trim($("input[@name='endTime']").val());    // ����ʱ��(ע��ʱ��)
	    
		var taskName = $("input[name=taskName]").val();
		var accName = $("input[name=accName]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!queryBlackDeviceTask.action'/>?taskName="+taskName+"&accName="+accName+"&startTime="+startTime+"&endTime="+endTime;
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
					����ǰ��λ�ã������������������
				</TD>
			</TR>
		</TABLE>
		<br>
		<form name="batchexform" method="post" action="">
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
			</tr>
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
	                                            <td align="center"><s:property value="acc_loginname" /></td>
	                                            <td align="center"><s:property value="add_time" /></td>
	                                            <td align="center"><s:property value="status" /></td>
	                                            <td align="center">
	                                              <button onclick="javascript:getdetailList('<s:property value="task_id"/>')">
	                                              		�鿴����
	                                              </button>&nbsp;&nbsp;
	                                          	   <s:if test="status!='����ִ����'&&status!='ʧЧ'">
		                                          	   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                                          	   <button  onclick="javascript:disableTask('<s:property value="task_id"/>')">
		                                          	   		ʧЧ
		                                          	   </button>
	                                          	   </s:if>
	                                          	   <s:if test="status=='ʧЧ'">
		                                          	   <button  onclick="javascript:ableTask('<s:property value="task_id"/>')">
		                                          	   		������Ч
		                                          	   </button>&nbsp;&nbsp;
	                                          	   </s:if>
	                                             </td>
	                                        </tr>
	                                </s:iterator>
	                        </tbody>
	                        <tfoot>
	                                <tr bgcolor="#FFFFFF">
	                                        <td colspan="6" align="right"><lk:pages
	                                                url="/gtms/stb/resource/stbUpgradeBlackList!queryBlackDeviceTask.action" styleClass=""
	                                                showType="" isGoTo="true" /></td>
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
		</form>
</body>

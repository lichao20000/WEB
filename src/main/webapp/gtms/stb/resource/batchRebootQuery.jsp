<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">		
function updateTask(taskId,status)
{
	//���񼤻��Ҫ����֤
	if(status=='1')
	{
		var url = "<s:url value='/gtms/stb/resource/batchReboot!getErrData.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
			var s=ajax.split(",");
			var flag=true;
	    	if(s[0]!='0')
	    	{
	    		var mess="�����˺�����"+s[1]+", �쳣�˺�����"+s[0]+",�Ƿ�������";
	    		flag=confirm(mess);
	    	}
	    	
	    	if(!flag){
	    		return;
	    	}
	    	
	    	var url = "<s:url value='/gtms/stb/resource/batchReboot!updateTask.action'/>";
		    $.post(url,{
				taskId:taskId,
				status:status
			},function(ajax){
		    	var s = ajax.split(",");
		    	alert(s[1]);
		    	if(s[0]=="1"){
		    		var cityId = $("select[name=cityId]").val();
		    		var vendorId = $("select[name=vendorId]").val();
		    		var status = $("select[name=status]").val();
		    		var showType=$("input[name=showType]").val();
		    		var url = "<s:url value='/gtms/stb/resource/batchReboot!queryInit.action'/>?"
		    					+ "cityId="+cityId
		    					+ "&vendorId="+vendorId
		    					+ "&status="+status;
		    					+ "&showType="+showType;
		    		location.reload();
		    	}
		    });
	    	
	    });
	}
	else
	{
		var url = "<s:url value='/gtms/stb/resource/batchReboot!updateTask.action'/>";
	    $.post(url,{
			taskId:taskId,
			status:status
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
	    	if(s[0]=="1"){
	    		var cityId = $("select[name=cityId]").val();
	    		var vendorId = $("select[name=vendorId]").val();
	    		var status = $("select[name=status]").val();
	    		var showType=$("input[name=showType]").val();
	    		var url = "<s:url value='/gtms/stb/resource/batchReboot!queryInit.action'/>?"
	    					+ "cityId="+cityId
	    					+ "&vendorId="+vendorId
	    					+ "&status="+status;
	    					+ "&showType="+showType;
	    		location.reload();
	    	}
	    });
	}
}

    
function viewdetailTask(taskid)
{
	var page = "<s:url value='/gtms/stb/resource/batchReboot!getTaskInfo.action'/>?taskId="+taskid;
	window.open(page,"","left=20,top=20,width=800,height=400,resizable=no,scrollbars=yes");
}

function count(taskid)
{
	var page = "<s:url value='/gtms/stb/resource/batchReboot!count.action'/>?taskId="+taskid;
	window.open(page,"","left=20,top=20,width=800,height=400,resizable=no,scrollbars=yes");
}
	
function queryTask()
{
	var cityId = $("select[name=cityId]").val();
	var vendorId = $("select[name=vendorId]").val();
	var status = $("select[name=status]").val();
	var showType=$("input[name=showType]").val();
	
	var url = "<s:url value='/gtms/stb/resource/batchReboot!queryInit.action'/>?"
				+ "cityId="+cityId
				+ "&vendorId="+vendorId
				+ "&status="+status
				+ "&showType="+showType;
    window.location.href=url;
}
</SCRIPT>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã����������������������
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" class="querytable" align="center">
	<tr>
		<TD class="title_2" align="center" width="10%">����</TD>
		<TD width="15%">
			<s:select list="cityList" name="cityId" headerKey="00"
				headerValue="ʡ����" listKey="city_id" listValue="city_name"
				value="cityId" cssClass="bk" theme="simple">
			</s:select>
		</TD>
		<TD class="title_2" align="center" width="10%">����</TD>
		<TD width="25%">
			<s:select list="vendorList" name="vendorId" headerKey="-1"
				headerValue="ȫ��" listKey="vendor_id" listValue="vendor_add"
				value="vendorId" cssClass="bk" theme="simple">
			</s:select>
		</TD>
		<td class="title_2" align="center">״̬</td>
		<td width="15%">
			<s:select name="status" value="status" list="#{'1':'�Ѽ���','-1':'��ʧЧ'}" 
				cssClass="bk" theme="simple" headerKey="" headerValue="ȫ��" >
			</s:select>
		</td>
		<td class="title_2" align="center">
			<button onclick="queryTask()"> �� ѯ </button>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>

<br>
<br>
<input type="hidden" name="showType" value="<s:property value='showType' />">
<table width="98%" class="listtable" align="center">
	<thead>
		<tr>
		    <th align="center" width="15%">�����Ҫ����</th>
		    <th align="center" width="5%">��������</th>
			<th align="center" width="5%">����</th>
			<th align="center" width="5%">����</th>
			<th align="center" width="5%">������</th>
            <th align="center" width="8%">����ʱ��</th>
            <th align="center" width="8%">����ʱ��</th>
			<th align="center" width="5%">״̬</th>
			<th align="center" width="20%">����</th>
		</tr>
	</thead>
	<s:if test="data!=null && data.size()>0">
		<tbody>
			<s:iterator value="data">
				<tr>
					<td align="center"><s:property value="task_desc" /></td>
					<td align="center"><s:property value="data_desc" /></td>
					<td align="center"><s:property value="city_name" /></td>
					<td align="center"><s:property value="vendor_name" /></td>
					<td align="center"><s:property value="acc_loginname" /></td>
					<td align="center"><s:property value="add_time" /></td>
					<td align="center"><s:property value="update_time" /></td>
					<s:if test='status=="1" || status=="4"'>
						<td align="center">�Ѽ���</td>
						<td align="center">
							<s:if test='showType!=1'>
								<button onclick="javascript:updateTask('<s:property value="task_id" />','-2')">
									ʧЧ
								</button>
								<s:if test="showType!=2">
									<button onclick="javascript:updateTask('<s:property value="task_id" />','2')">
										ɾ��
									</button>
								</s:if>
							</s:if>
                            <button onclick="javascript:count('<s:property value="task_id"/>')">
 								���ͳ��
                            </button>
							<button onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
								�鿴��ϸ
							</button>
						</td>
					</s:if>
					<s:elseif test='status=="-1"' >
						<td align="center">��ʧЧ</td>
						<td align="center">
							<s:if test='showType!=1'>
								<button onclick="javascript:updateTask('<s:property value="task_id" />','1')">
									����
								</button>
								<s:if test="showType!=2">
									<button onclick="javascript:updateTask('<s:property value="task_id" />','2')">
										ɾ��
									</button>
								</s:if>
							</s:if>
							<button onclick="javascript:count('<s:property value="task_id"/>')">
								���ͳ��
							</button>
							<button onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
								�鿴��ϸ
							</button>
						</td>
					</s:elseif>
					<s:else>
						<td align="center">���ݴ�����</td>
						<td align="center">
							<s:if test='showType!=1'>
								<button disabled >����</button>
								<s:if test="showType!=2">
									<button disabled >ɾ��</button>
								</s:if>
							</s:if>
							<button disabled >���ͳ��</button>
							<button disabled >�鿴��ϸ</button>
						</td>
					</s:else>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="right">
					<lk:pages url="/gtms/stb/resource/batchReboot!queryInit.action"
						styleClass="" showType="" isGoTo="true" changeNum="false" />
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tbody>
			<tr>
				<td colspan="9">
					<font color="red">û�ж��Ƶ�����</font>
				</td>
			</tr>
		</tbody>
	</s:else>
</table>
</body>

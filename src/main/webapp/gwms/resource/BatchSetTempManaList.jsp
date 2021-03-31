<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<%--
	/**
	 * �淶�汾��ѯ�б�ҳ��
	 *
	 * @author ����(����) Tel:�绰
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������б�</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

	function deviceDetail(type,countNum,task_id){
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!queryDevList.action?countNum='/>"+countNum+"&&type="+type+"&&task_id="+task_id;
		window.open(url ,"","left=140,top=80,width=1000,height=600,resizable=yes,scrollbars=yes");
	}
	
	
	function updateStatus(task_id,type){
		
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!updateCount.action'/>";
		$.post(url,{
			task_id:task_id,
			type:type
		},function(ajax){
			if("ok" == ajax){
				doUpdate(task_id,type);
			}else if("tooMuch" == ajax){
				var  bln   =   window.confirm("��Ҫ���µ���������40W,��������ִ��!(����Ǽ�������)");  
			    if(bln == true) {
			    	doUpdate(task_id,type);
			    }else{
			    	return false;
			    }
			}
		});
	}
	
	
	function doUpdate(task_id,type){
		
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!update.action'/>";
		$.post(url,{
			task_id:task_id,
			type:type
		},function(ajax){
			if("1" == ajax){
				if('1'==type){
				    alert("��ͣ�ɹ�");
				}
				else{
					alert("�����ɹ�");
				}
				
				window.parent.query();
			}else if("0" == ajax){
				alert("�޸�ʧ��");
			}
		});
	}
	
	
	
	function doDel(task_id){
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!del.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("1" == ajax){
				alert("ɾ���ɹ�");
				window.location.reload();
			}else if("0" == ajax){
				alert("ɾ��ʧ��");
			}
		});
	}
	
	
	function del(task_id){
		var  bln1   =   window.confirm("ȷ��Ҫɾ��������(����Ǽ���)");  
	    if(bln1 != true) {
	    	return;
	    }
	    
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!delCount.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("ok" == ajax){
				doDel(task_id);
			}else if("tooMuch" == ajax){
				var  bln   =   window.confirm("��Ҫɾ������������40W,��������ִ��!(����Ǽ���ɾ��)");  
			    if(bln == true) {
			    	doDel(task_id);
			    }else{
			    	return false;
			    }
			}
		});
	}
	
	
	function detail(task_id){
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!queryTaskDetailById.action?task_id='/>"+task_id;
		window.open(url ,"","left=80,top=80,width=800,height=600,resizable=yes,scrollbars=yes");
	}
	

</script>

</head>

<body >
<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
<INPUT TYPE="hidden" NAME="task_id" id="task_id" maxlength=60 class=bk size=20>
<INPUT TYPE="hidden" NAME="task_name" id="task_name" maxlength=60 class=bk size=20>
</FORM>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>��������</th>
			<th>ģ��</th>
			<th>����״̬</th>
			<th>����ʱ��</th>
			<!-- <th>������</th> -->
			<th>����</th>
			<th>�ɹ���</th>
			<th>ʧ����</th>
			<th>δִ��</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<td align="center"><s:property value="template_name" /></td>
						<s:if test="status==1">
							<td align="center">����</td>
						</s:if>
						<s:else>
							<td align="center">��ͣ</td>
						</s:else>
						
						<td align="center"><s:property value="add_time" /></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('total','<s:property value="totalNum" />','<s:property value="task_id" />')"><s:property value="totalNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('succ','<s:property value="succNum" />','<s:property value="task_id" />')"><s:property value="succNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('fail','<s:property value="failNum" />','<s:property value="task_id" />')"><s:property value="failNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('unDone','<s:property value="unDoneNum" />','<s:property value="task_id" />')"><s:property value="unDoneNum" /></a></td>
						<td align="center">
						<s:if test="status==1"><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">��ͣ</a></s:if>
						<s:else><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">����</a></s:else>
						<a href="javascript:del('<s:property value="task_id" />')">ɾ��</a>
						<a href="javascript:detail('<s:property value="task_id" />')">����</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>û�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="9" align="right">
					<lk:pages url="/gwms/resource/batchSetTempManaACT!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>
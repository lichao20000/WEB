<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<ms:inArea areaCode="cq_dx" notInMode="false">

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

	
	function updateStatus(task_id,type){
		
		var url = "<s:url value='/gwms/resource/batchConfig!updateCount.action'/>";
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
		
		var url = "<s:url value='/gwms/resource/batchConfig!update.action'/>";
		$.post(url,{
			task_id:task_id,
			type:type
		},function(ajax){
			if("1" == ajax){
				alert("�޸ĳɹ�");
				window.parent.query();
			}else if("0" == ajax){
				alert("�޸�ʧ��");
			}
		});
	}
	
	
	
	function doDel(task_id){
		var url = "<s:url value='/gwms/resource/batchConfig!del.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("1" == ajax){
				alert("ɾ���ɹ�");
				window.parent.query();
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
	    
		var url = "<s:url value='/gwms/resource/batchConfig!delCount.action'/>";
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
	
	
	function detail(task_id,task_name){
		/* var url = "<s:url value='/gwms/resource/batchSoftUpACT!queryTaskGyCity.action'/>";
		$.post(url,{
			task_id:task_id,
			task_name:task_name
		},function(ajax){
			if("1" == ajax){
				alert("�޸ĳɹ�");
				window.parent.query();
			}else if("0" == ajax){
				alert("�޸�ʧ��");
			}
		}); */
		
		
		var url = "<s:url value='/gwms/resource/batchConfig!queryTaskGyCity.action?task_id='/>"+task_id+"&&task_name="+task_name;
		window.open(url ,"","left=80,top=80,width=800,height=600,resizable=yes,scrollbars=yes");
		
		/* var form = document.getElementById("mainForm");
		form.action = "<s:url value='/gwms/resource/batchSoftUpACT!queryList.action'/>";
		form.submit(); */
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
			<ms:inArea areaCode="cq_dx" notInMode="false">
			<th>״̬</th>
			</ms:inArea>
			<th>����ʱ��</th>
			<th>�����ڵ�·��</th>
			<th>����ֵ</th>
			<th>��������</th>
			<th>����</th>
			<th>�ɹ���</th>
			<th>ʧ����</th>
			<th>δ����</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<ms:inArea areaCode="cq_dx" notInMode="false">
						<s:if test="status==1">
							<td align="center">����</td>
						</s:if>
						<s:else>
							<td align="center">��ͣ</td>
						</s:else>
						</ms:inArea>
						
						<td align="center"><s:property value="settime" /></td>
						<td align="center" width="300px" title='<s:property value="parampath" />'><s:property value="parampaths" /></td>
						<td align="center" width="100px" title='<s:property value="paramvalue" />'><s:property value="paramvalues" /></td>
						<td align="center" width="100px" title='<s:property value="paramtype" />'><s:property value="paramtypes" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="succNum" /></td>
						<td align="center"><s:property value="failNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center">
						<ms:inArea areaCode="cq_dx" notInMode="false">
						<s:if test="status==1"><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">��ͣ</a></s:if>
						<s:else><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">����</a></s:else>
						</ms:inArea>
						<a href="javascript:del('<s:property value="task_id" />')">ɾ��</a>
						<a href="javascript:detail('<s:property value="task_id" />','<s:property value="task_name" />')">����</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11>û�в�ѯ�������Ϣ��</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11>û�в�ѯ�������Ϣ��</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/gwms/resource/batchConfig!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>
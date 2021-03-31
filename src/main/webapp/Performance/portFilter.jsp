<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
	<head>
		<title>���˹�������</title>
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
		type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
		type="text/css">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
		type="text/css">
		<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet"
		type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
		<style type="text/css">
		</style>
		<script type="text/javascript">

		var msg = '<s:property value="messageStr"/>'
		if(msg!=''&&msg!=null)
		alert(msg)//��ʾ�������
		//ѡ���豸���̣������豸���̻���豸����
		function chooseCompany(){
			var form1 = document.forms['form1'];
			form1.action = "<s:url value="/performance/portFilterAction!chooseCompany.action"/>";
			form1.submit();

		}

		//�������˹���
		function submit1(){
			var form1 = document.forms['form1'];
			var companyName = document.getElementById("companyName").value;
			var deviceModel = document.getElementById("deviceModel").value;
			var filterType = document.getElementById("filterType").value;
			var filterValue = document.getElementById("filterValue").value;
			if(Trim(companyName)=="None"){//�жϳ������Ƿ�Ϊ��
				alert("��ѡ���豸���̣�");
				return;
			}
			if(Trim(deviceModel)=="None"){//�ж��豸�ͺ��Ƿ�Ϊ��
				alert("��ѡ���豸�ͺţ�");
				return;
			}
			if(Trim(filterType)=="None"){//�жϹ��������Ƿ�Ϊ��
				alert("��ѡ��������ͣ�");
				return;
			}
			if(Trim(filterValue)==""){//�жϹ�����Ϣ�Ƿ�Ϊ��
				alert("�����������Ϣ��");
				return;
			}
			if(Trim(filterType)=="4"){//�ж�����Ķ˿�����������Ϣ�Ƿ�Ϸ�
				if(!isIndex(filterValue)){
					alert("�˿�����������Ϣ���Ϸ����������ֲ������� 123��456��");
					return ;
				}
			}
			if(Trim(filterType)=="5"){//�ж�����Ķ˿����͹�����Ϣ�Ƿ�Ϸ�
				if(!IsNum(filterValue)){//�ж��Ƿ�������
					alert("�˿����ͱ��������֣�")
					return;
				}
			}

			function isIndex(str){//�ж��Ƿ�����Ч��Index����
				var bz=true;
				var pos = str.indexOf(",");
				if(pos==-1){
					bz=false;
				}else{
					var arr = str.split(",");
					if(arr.length>2){
						bz=false;
					}else{
						if(!IsNum(arr[0])){
							bz=false;
						}
						if(!IsNum(arr[1])){
							bz=false;
						}
					}
				}
				return bz;
			}

			form1.action = "<s:url value="/performance/portFilterAction!savePortFilterRule.action"/>";
			form1.submit();

		}
		//��ѯ����
		function search(){
			var form1 = document.forms['form1'];
			var companyName = document.getElementById("companyName").value;
			var deviceModel = document.getElementById("deviceModel").value;
			if(Trim(companyName)=="None"){//�жϳ������Ƿ�Ϊ��
				alert("��ѡ���豸���̣�");
				return;
			}
			if(Trim(deviceModel)=="None"){//�ж��豸�ͺ��Ƿ�Ϊ��
				alert("��ѡ���豸�ͺţ�");
				return;
			}
			form1.action = "<s:url value="/performance/portFilterAction!searchPortFilterRule.action"/>";
			form1.submit();
		}
		//ɾ������
		function delRules(deviceModel,filterType){
			if(confirm("ȷ��Ҫɾ������?")==false){
	  			return;
	  		}else{
				document.getElementById("delDeviceModel").value=deviceModel
				document.getElementById("filterType").value=filterType
				var form11 = document.forms['form1'];
				form11.action = "<s:url value="/performance/portFilterAction!removePortFilterRule.action"/>";
				form11.submit();
			}
		}
		</script>
	</head>
	<body>
	<form name="form1" id="form1" align="center">
	<input type="hidden" id="delDeviceModel" name="delDeviceModel"/>
		<br/>
		<br/>
			<table width="95%" class="tab" align="center">
			  <tr>
			    <td class="mouseon"
			                      onClick="location.href='#'"
			                      onMouseOver="this.className='mouseon';"
			                      onMouseOut="this.className='mouseon';">��������</td>
			    <td>&nbsp;</td>
			  </tr>
			  <tr>
			    <td colspan="2" class="tag_line"></td>
			  </tr>
			</table>
			<table width="95%" class="querytable" align="center">
			<tr>
					<td class="title_2">ѡ���豸���̣�</td>
					<td>
					<select id="companyName" name="companyID" onchange="chooseCompany()">
						<option value="None" selected="selected">--��ѡ���豸����--</option>
						<s:iterator var="company" value="companyList" status="row">
							<s:if test="companyID==#company.vendor_id">
								<option value="<s:property value="vendor_id"/>" selected="selected"><s:property value="vendor_name"/></option>
							</s:if>
							<s:else>
								<option value="<s:property value="vendor_id"/>"><s:property value="vendor_name"/></option>
							</s:else>
						</s:iterator>
					</select>
					</td>

			</tr>

			<tr>
					<td class="title_2">ѡ���豸�ͺţ�</td>
					<td>
					<select name="deviceModel" id="deviceModel">
						<option value="None" selected="selected">--��ѡ���豸�ͺ�--</option>
						<s:iterator var="modelMap" value="deviceModelList" status="row">
							<s:if test="deviceModel==#modelMap.device_name">
								<option value="<s:property value="device_model_id"/>" selected="selected"><s:property value="device_model"/></option>
							</s:if>
							<s:else>
								<option value="<s:property value="device_model_id"/>"><s:property value="device_model"/></option>
							</s:else>
						</s:iterator>
					</select>
					</td>

			</tr>
			<tr>
					<td class="title_2">ѡ��������ͣ�</td>
					<td >
					<select name="filterType" id="filterType">
						<option value="None" selected="selected">--��ѡ���������--</option>
						<option value="1" >�˿�����</option>
						<option value="2" >�˿�����</option>
						<option value="3" >�˿ڱ���</option>
						<option value="4" >�˿�����</option>
						<option value="5" >�˿�����</option>
					</select>
					</td>
			</tr>
			<tr>
					<td class="title_2">���������Ϣ��</td>
					<td ><input name="filterValue" id ="filterValue" type="text" /></td>
			</tr>
			<tr>
				<td colspan="2" class="foot"><div align="right"><button onclick="submit1()"/>��  ��</button><button onclick="search()"/>��  ѯ</button></div></td>
			</tr>
			</table>


		<br/>
		<br/>
		<table width="95%" class="tab" align="center">
		  <tr>
		    <td class="mouseon"
		                      onClick="location.href='#'"
		                      onMouseOver="this.className='mouseon';"
		                      onMouseOut="this.className='mouseon';">�����б�</td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td colspan="2" class="tag_line"></td>
		  </tr>
		</table>
		<table width="95%" align="center" class="listtable">
		<thead>
	    <tr align="center">
        <th>�豸�ͺ�</th>
        <th>��������</th>
        <th> ������Ϣ</th>
        <th>����</th>

	    </tr>
	    <tbody>
	    <s:if test="filterRulesList.size!=0">
	    <s:iterator var="filter"value="filterRulesList" status="row">
	    	<tr>
	    	<td><s:property value="device_model"/></td>
	    	<td><s:if test="#filter.type==1">�˿�����</s:if>
	    		<s:elseif test="#filter.type==2">�˿�����</s:elseif>
	    		<s:elseif test="#filter.type==3">�˿ڱ���</s:elseif>
	    		<s:elseif test="#filter.type==4">�˿�����</s:elseif>
	    		<s:elseif test="#filter.type==5">�˿�����</s:elseif>
	    	</td>
	    	<td><s:property value="value"/></td>
	    	<td>
		    	<a href="javascript:delRules('<s:property value="device_model_id"/>','<s:property value="type"/>');" title="���ɾ������">ɾ��</a>
	    	</td>
	    	</tr>
	    </s:iterator>
	    <tr><td colspan="4"><div align="right"><lk:pages url="/performance/portFilterAction!toChooseCompany.action" isGoTo="true"/></div></td></tr>
		</s:if>
		<s:else>
		<td colspan="4">�����ݣ�</td>
		</s:else>

		</table>
	</form>
	</body>
</html>

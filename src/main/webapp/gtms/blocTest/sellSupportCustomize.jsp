<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function selectCust(_name){
	
	var strcustIDs = getDeviceIDByCheck(_name);
	var cust_id = strcustIDs.split(",");
	
	if(strcustIDs == ""){
		alert("��ѡ��Ҫȷ�ϵĿͻ�");
		return false;
	}
	document.addForm.customerId.value=strcustIDs;
	return true;
}
//���ݸ�ѡ��ѡ��״̬����ȡ�豸id
function getDeviceIDByCheck(_name){
	var arrObj = document.all(_name);
	var strDeviceIDs = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = arrObj.value + ",";
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				strDeviceIDs += arrObj[i].value + ",";
			}
		}
	}
	strDeviceIDs = strDeviceIDs.substring(0, strDeviceIDs.length-1);
	return strDeviceIDs;
}
	//��ѯ
	function queryCustomer() {
		document.addForm.submit();
	}
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids=["dataForm"]

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide="yes"

	function dyniframesize() 
	{
		var dyniframe=new Array()
		for (i=0; i<iframeids.length; i++)
		{
			if (document.getElementById)
			{
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera)
				{
	     			dyniframe[i].style.display="block"
	     			//����û����������NetScape
	     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	      			//����û����������IE
	     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			 }
	   		}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide=="no")
			{
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
	    		tempobj.style.display="block"
			}
		}
	}

	$(function(){
		//setValue();
		dyniframesize();
	});

	$(window).resize(function(){
		dyniframesize();
	}); 
</SCRIPT>
<%@ include file="/toolbar.jsp"%>
<body onload="init();">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM id="addForm" name="addForm" method="post"
			action="./gtms/blocTest/sellSupportCustomize!queryData.action" target="dataForm">
			<input type="hidden" name="customerId" value="">
						
		<!-- ��Ӻͱ༭part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center"
			id="addTable">
<table  class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">Ӫ��֧�Ŷ���</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12" /> ����Ӫ��֧�ű���</td>
			</tr>
		</table>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

					<tr>
						<th colspan="4" id="gwShare_thTitle">Ӫ��֧�Ŷ���</th>
					</tr>
					<!-- id="gwShare_tr21" -->
					<tr bgcolor="#FFFFFF" id="gwShare_tr21">
						<td align="right" class=column width="15%">��������</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="flow_max" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
						<td align="right" class=column width="15%">��������</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="flow_min" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">ʱ������</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="time_max" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
						<td align="right" class=column width="15%">ʱ������</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="time_min" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>

					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">�ͻ�����</td>
						<td align="left" width="35%">
									<s:select list="custManagerList" name="custManagerId" headerKey="-1"
										headerValue="==��ѡ��==" listKey="cust_manager_id" listValue="cust_manager_name"
										 cssClass="bk"></s:select>
						</td>

						<td align="right" class=column width="15%"></td>
						<td align="left" width="35%"><!-- input type="button"
							value="��ѯ�ͻ�" onclick="queryCustomer()"--></td>

					</tr>
					<tr>
						<th colspan="4" id="gwShare_thTitle">ѡ��ͻ�</th>
					</tr>
					<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
					<th></th>
						<th>�ͻ�ID</th>
						<th>�ͻ���</th>
						<th>��ϵ��</th>
						<th>��ϵ�绰</th>
						<!--th>����</th-->
					</tr>
					<s:if test="customerList.size()>0">
						<s:iterator value="customerList">
							<tr bgcolor="#FFFFFF">
								<td class=column1><input type=checkbox name=chkCheck onclick="selectCust('chkCheck')"
									value='<s:property value="customer_id"/>'></td>
								<td class=column1><s:property value="customer_id" /></td>
								<!--  <td class=column1><s:property value="customer_account"/></td>-->
								<td class=column1><s:property value="customer_name" /></td>
								<td class=column1><s:property value="linkman" /></td>
								<td class=column1><s:property value="linkphone" /></td>
								<!--td class=column1><s:property value="city_id" /></td-->
							</tr>
						</s:iterator>
					</s:if>
					</table>
					<tr>
						<td colspan="4" align="right" CLASS=green_foot><INPUT
							TYPE="button" onclick="javascript:abc();" class="jianbian"
							value="�� ��"> <INPUT TYPE="hidden" name="action"
							value="add"> <input type="hidden" name="devicetype_id"
							value="<s:property value="devicetype_id"/>"></td>
					</tr>
					

				</TABLE>
				</TD>
				
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=15><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>
</body>
<SCRIPT LANGUAGE="JavaScript">

	function abc() {

		//trimAll();
		var url = "<s:url value='/gtms/blocTest/sellSupportCustomize!add.action'/>?customerId="+document.addForm.customerId.value;
		var cust_manager_id = $("select[@name='custManagerId']").val();
		var flow_min = $("input[@name='flow_min']").val();
		var flow_max = $("input[@name='flow_max']").val();
		var time_min = $("input[@name='time_min']").val();
		var time_max = $("input[@name='time_max']").val();
		if (!/^[\d]+$/gi.test(flow_min)) {
			alert("����������");
			$("input[@name='flow_min']").focus();
			$("input[@name='flow_min']").select();
			return false;
		}
		if (!/^[\d]+$/gi.test(flow_max)) {
			alert("����������");
			$("input[@name='flow_max']").focus();
			$("input[@name='flow_max']").select();
			return false;
		}
		if (!/^[\d]+$/gi.test(time_min)) {
			alert("����������");
			$("input[@name='time_min']").focus();
			$("input[@name='time_min']").select();
			return false;
		}
		if (!/^[\d]+$/gi.test(time_max)) {
			alert("����������");
			$("input[@name='time_max']").focus();
			$("input[@name='time_max']").select();
			return false;
		}
		$.post(url, {
			custManagerId : cust_manager_id,
			flowMin : flow_min,
			flowMax : flow_max,
			timeMin : time_min,
			timeMax : time_max
		}, function(ajax) {

				alert(ajax);

		});
	}

</SCRIPT>
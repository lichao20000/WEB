<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�ϱ��ʺ�</title>
<%
	/**
	 * 
	 * 
	 * @author 
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
	 //String gw_type = request.getParameter("gw_type");
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
function DetailUser(user_id){
	///if(gw_type=="2"){
      //   var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
     //}else{
        var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
     // }
	//var strpage="../../Resource/HGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
$(function(){
	parent.dyniframesize();
});
</script>

</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>�豸���к�</th>
			<th>�ϱ��ʺ�</th>

		</tr>
	</thead>
	<tbody>
		<s:if test="userNameList.size()>0">
			<s:iterator value="userNameList">
				<tr>
					<td width="50%"><a
						href="javascript:DetailDevice('<s:property value="device_id" />')">
					<s:property value="device_serialnumber" /></a></td>
			<!--<td><a
						href="javascript:DetailUser('<s:property value="user_id" />')"><s:property
						value="username" /></a></td>-->	
                   <td width="50%"> <s:property value="username" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>

			<tr>
				<td colspan=2>û�в�ѯ�������Ϣ��</td>
			</tr>


		</s:else>



	</tbody>

	

</table>
</body>
<script>
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


</script>
</html>
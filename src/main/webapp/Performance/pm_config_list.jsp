<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%//���������ܱ��ʽ�б�
	String device_id=request.getParameter("device_id");
	if(!device_id.contains("'")) device_id="'"+device_id+"'";
	String sql="select a.expressionid,a.interval,a.isok,a.remark,b.name,b.descr,b.class1,c.device_name,c.loopback_ip,c.device_id from pm_map a,pm_expression b,tab_gw_device c";
	sql+=" where a.expressionid=b.expressionid and c.device_id=a.device_id and a.device_id in ("+device_id+") order by a.expressionid";
	Cursor cursor=DataSetBean.getCursor(sql);
	String data="";
	if(cursor.getRecordSize()==0){
		data="<tr bgcolor='#FFFFFF'><td colspan=8>���豸��ʱû����������</td></tr>";
	}else{
		Map fields=cursor.getNext();
		HashMap class_map=new HashMap();
		class_map.put("0","����");
		class_map.put("1","CPU������");
		class_map.put("2","�ڴ�������");
		class_map.put("3","��ַ��������");
		class_map.put("4","�¶�");
		class_map.put("5","��Դ");
		class_map.put("6","����");
		HashMap isok_map=new HashMap();
		isok_map.put("-1","û�г�ʼ��");
		isok_map.put("0","��ʼ��ʧ��");
		isok_map.put("1","��ʼ���ɹ�");
		HashMap remark_map =new HashMap();
		remark_map.put("-1","��ʱ");
		remark_map.put("-2","��֧��");
		remark_map.put("-21","����һ��oid�ɲ�������");
		remark_map.put("-3","�޷��ɼ�������Ϣ");
		remark_map.put("-4","oid�ɼ�����������һ��");
		remark_map.put("-41","���ܺ������ɼ�����������һ��");
		remark_map.put("-6","���ʽID������999");
		String tmp="";
		while(fields!=null){
			tmp=(String)remark_map.get(fields.get("remark"));
			if(tmp==null || tmp.equals("null") || tmp.equals("NULL")){
				tmp="";
			}
			if(fields.get("isok")!=null && ((String)fields.get("isok")).equals("1")){
				data+="<tr bgcolor='#FFFFFF'>";
			}else{
				data+="<tr bgcolor='red'>";
			}
			data+="<td>"+fields.get("device_name")+"</td>";
			data+="<td>"+fields.get("loopback_ip")+"</td>";
			data+="<td>"+fields.get("name")+"</td>";
			data+="<td>"+class_map.get(fields.get("class1"))+"</td>";
			data+="<td>"+fields.get("interval")+"</td>";
			data+="<td>"+isok_map.get(fields.get("isok"))+"</td>";
			data+="<td>"+tmp+"</td>";
			data+="<td><a href='javascript://' onclick=Del('"+fields.get("device_id")+"','"+fields.get("expressionid")+"',$(this))>ɾ��</a></td>";
			data+="</tr>";
			fields=cursor.getNext();
		}
		class_map=null;
		isok_map=null;
		remark_map=null;
	}
	cursor=null;
	
%>
<html>
<head>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript"  src="../Js/jquery.js"></script>
</head>
<body>
	<form>
		<table width="98%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
			<tr>
				<th colspan=8>�豸�������ý��</th>
			</tr>
			<tr>
				<th>�豸����</th>
				<th>�豸IP</th>
				<th>���ʽ����</th>
				<th>����</th>
				<th>�������</th>
				<th>���ý��</th>
				<th>ʧ��ԭ������</th>
				<th>����</th>
			</tr>
			<%=data%>
		</table>
	</form>
</body>
</html>
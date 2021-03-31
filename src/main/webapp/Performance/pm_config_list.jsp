<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%//已配置性能表达式列表
	String device_id=request.getParameter("device_id");
	if(!device_id.contains("'")) device_id="'"+device_id+"'";
	String sql="select a.expressionid,a.interval,a.isok,a.remark,b.name,b.descr,b.class1,c.device_name,c.loopback_ip,c.device_id from pm_map a,pm_expression b,tab_gw_device c";
	sql+=" where a.expressionid=b.expressionid and c.device_id=a.device_id and a.device_id in ("+device_id+") order by a.expressionid";
	Cursor cursor=DataSetBean.getCursor(sql);
	String data="";
	if(cursor.getRecordSize()==0){
		data="<tr bgcolor='#FFFFFF'><td colspan=8>该设备暂时没有配置性能</td></tr>";
	}else{
		Map fields=cursor.getNext();
		HashMap class_map=new HashMap();
		class_map.put("0","其他");
		class_map.put("1","CPU利用率");
		class_map.put("2","内存利用率");
		class_map.put("3","地址池利用率");
		class_map.put("4","温度");
		class_map.put("5","电源");
		class_map.put("6","风扇");
		HashMap isok_map=new HashMap();
		isok_map.put("-1","没有初始化");
		isok_map.put("0","初始化失败");
		isok_map.put("1","初始化成功");
		HashMap remark_map =new HashMap();
		remark_map.put("-1","超时");
		remark_map.put("-2","不支持");
		remark_map.put("-21","其中一个oid采不到数据");
		remark_map.put("-3","无法采集描述信息");
		remark_map.put("-4","oid采集的索引数不一致");
		remark_map.put("-41","性能和索引采集到的索引不一致");
		remark_map.put("-6","表达式ID超过了999");
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
			data+="<td><a href='javascript://' onclick=Del('"+fields.get("device_id")+"','"+fields.get("expressionid")+"',$(this))>删除</a></td>";
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
				<th colspan=8>设备性能配置结果</th>
			</tr>
			<tr>
				<th>设备名称</th>
				<th>设备IP</th>
				<th>表达式名称</th>
				<th>分类</th>
				<th>采样间隔</th>
				<th>配置结果</th>
				<th>失败原因描述</th>
				<th>操作</th>
			</tr>
			<%=data%>
		</table>
	</form>
</body>
</html>
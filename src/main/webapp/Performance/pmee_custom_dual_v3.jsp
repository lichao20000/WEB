<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
	String sql;
	String type=request.getParameter("type");
	String vendor_id=request.getParameter("vendor");
	long acc_oid=curUser.getUser().getId();
	if(type!=null && "1".equals(type)){//获取性能类型
		sql="select b.expressionid as expid,a.expressionid as expressionid,a.name,a.remark from pm_expression a left join gw_pmee_custom b on a.expressionid=b.expressionid and b.acc_oid="+acc_oid+" where a.company='"+vendor_id+"'";
		Cursor cursor=DataSetBean.getCursor(sql);
		Map field=cursor.getNext();
		String data="";
		data="<table width='100%'><tr>";
		int n=1;
		while(field!=null){
			data+="<td><input type='checkbox' name='chk' value='"+field.get("expressionid")+"-/-"+field.get("name")+"' ";
			if(field.get("remark").equals("0")){//默认显示
				data+=" disabled checked";
			}else if(field.get("expressionid").equals(field.get("expid"))){
				data+=" checked";
			}
			data+=">"
				+field.get("name")
				+"</td>";;
			if(n%3==0){
				data+="</tr><tr>";
			}
			
			field=cursor.getNext();
			n++;
		}
		data+="</tr></table>";
		out.println(data);
	}else{//保存定制
		String chk;
		// 通过ajax传递中文，需要将字符集合转码的。
		try
			{
				chk=java.net.URLDecoder.decode(request.getParameter("chk"),"UTF-8");
			} catch (Exception e)
			{
				chk=request.getParameter("chk");
			}
		String[] expid=chk.split(",");
		String[] tmp;
		int n=expid.length;
		sql="delete gw_pmee_custom where acc_oid="+acc_oid+" and oui='"+vendor_id+"'";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql="delete from gw_pmee_custom where acc_oid="+acc_oid+" and oui='"+vendor_id+"'";
		}
		for(int i=0;i<n;i++){
			tmp=expid[i].split("-/-");
			sql+=" insert into gw_pmee_custom(acc_oid,oui,expressionid,custom_desc) values("+acc_oid+",'"+vendor_id+"',"+tmp[0]+",'"+tmp[1]+"')";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		int result=DataSetBean.executeUpdate(sql);
		if(result==1){
			out.println(true);
		}else{
			out.println(false);
		}
		
	}
%>


<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.util.*"%>
<%@page import="com.linkage.commons.db.PrepareSQL"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%
request.setCharacterEncoding("GBK");

String strSQL;
String strMsg = "";
PrepareSQL pSQL = new PrepareSQL();
String strAction = request.getParameter("action");
String allHost = request.getParameter("allHost");

if (strAction.equals("delete")) { // ɾ������
    String str_hostip = request.getParameter("hostip");
    String str_gather_id = request.getParameter("gather_id");
    strSQL = "delete from tab_attrwarconf where gather_id=? and hostip=?";
	pSQL.setSQL(strSQL);
    pSQL.setString(1, str_gather_id);
    pSQL.setString(2, str_hostip);
    strSQL = pSQL.getSQL();
} else {
	//����ʱ
    String str_hostip = request.getParameter("hostip");
    String str_gather_id = request.getParameter("add_gather_id");
    //�޸�ʱ
    String str_hid_hostip = request.getParameter("hid_hostip");    
    String str_hid_gather_id = request.getParameter("hid_gather_id");
    
    String str_attr_id1 = request.getParameter("attr_id1");
    String str_attr_id2 = request.getParameter("attr_id2");
    String str_attr_id3 = request.getParameter("attr_id3");
    String str_attr_id1_s = request.getParameter("attr_id1_s");
    String str_attr_id2_s = request.getParameter("attr_id2_s");
    String str_attr_id3_s = request.getParameter("attr_id3_s");

    if (strAction.equals("add")) { // ���Ӳ���
    	String tem_Sql = "";
    	strSQL = "delete from tab_attrwarconf where gather_id=? and hostip=?";
    	pSQL.setSQL(strSQL);
        pSQL.setString(1, str_gather_id);
        pSQL.setString(2, str_hostip);
        DataSetBean.executeUpdate(pSQL.getSQL());
    	
        strSQL = "insert into tab_attrwarconf(gather_id,hostip,attr_id,warmvalue,s_warmvalue) values(?,?,?,?,?)";
    	for(int i=1;i<4;i++){
        	pSQL.setSQL(strSQL);
            pSQL.setString(1, str_gather_id);
            pSQL.setString(2, str_hostip);
            pSQL.setStringExt(3, ""+i,false);
            
        	if(i==1){
        		pSQL.setStringExt(4, str_attr_id1,false);
        		pSQL.setStringExt(5, str_attr_id1_s,false);
        	}
        	else if(i==2){
        		pSQL.setStringExt(4, str_attr_id2,false);
        		pSQL.setStringExt(5, str_attr_id2_s,false);
        	}
        	else if(i==3){
        		pSQL.setStringExt(4, str_attr_id3,false);
        		pSQL.setStringExt(5, str_attr_id3_s,false);
        	}
            
            strSQL = StringUtils.replace(strSQL, ",,", ",null,");
            strSQL = StringUtils.replace(strSQL, ",,", ",null,");
            strSQL = StringUtils.replace(strSQL, ",)", ",null)");
            
            if(tem_Sql.equals(""))
            	tem_Sql = pSQL.getSQL();
            else
            	tem_Sql += ";"+pSQL.getSQL();
    	}
    	
    	strSQL = tem_Sql;
    } else { // �޸Ĳ���
    	strSQL = "update tab_attrwarconf set warmvalue=?,s_warmvalue=? where gather_id=? and hostip=? and attr_id=?";
    	String tem_Sql = "";
    	for(int i=1;i<4;i++){    		
        	pSQL.setSQL(strSQL);
        	
        	if(i==1){
        		pSQL.setStringExt(1, str_attr_id1,false);
        		pSQL.setStringExt(2, str_attr_id1_s,false);
        	}
        	else if(i==2){
        		pSQL.setStringExt(1, str_attr_id2,false);
        		pSQL.setStringExt(2, str_attr_id2_s,false);
        	}
        	else if(i==3){
        		pSQL.setStringExt(1, str_attr_id3,false);
        		pSQL.setStringExt(2, str_attr_id3_s,false);
        	}

            pSQL.setString(3, str_hid_gather_id);
            pSQL.setString(4, str_hid_hostip);
            pSQL.setStringExt(5, ""+i,false);
            
            strSQL = StringUtils.replace(strSQL, ",,", ",null,");
            strSQL = StringUtils.replace(strSQL, ",,", ",null,");
            strSQL = StringUtils.replace(strSQL, ",)", ",null)");
            
            if(tem_Sql.equals(""))
            	tem_Sql = pSQL.getSQL();
            else
            	tem_Sql += ";"+pSQL.getSQL();
    	}
    	
    	strSQL = tem_Sql;
    }
}

if (!strSQL.equals("")) {
	
   int[] iCode = DataSetBean.doBatch(strSQL);
   if (iCode[0] > 0) {
        strMsg = "��ֵ���ò����ɹ���";
    } else {
        strMsg = "��ֵ���ò���ʧ�ܣ������Ի��Ժ����ԣ�";
    }
}

pSQL = null;
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	window.alert('<%=strMsg%>');
	parent.location.reload();
//-->
</SCRIPT>

<%@ page contentType="text/html; charset=GBK" language="java" import="com.linkage.liposs.manaconf.*" errorPage="" %>
<%@ page import="com.linkage.litms.common.database.Cursor,java.util.*,com.linkage.litms.common.util.Encoder"%>
<%@ page import="com.linkage.liposs.common.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%
request.setCharacterEncoding("GBK");
//调用接口返回添加设备处理后的信息
ManaConfig mc = new ManaConfig(request);
Cursor cursor = mc.getDeviceByID();
Map fields = cursor.getNext();
Date date = new Date();

if (fields == null)
{
   out.println("您要立即备份的设备不存在，请先进行配置！");
}
else
{
    String device_id = URLEncoder.encode((String)fields.get("device_id"));
    String device_model = URLEncoder.encode((String)fields.get("device_model"));
    String mib = URLEncoder.encode((String)fields.get("os_version"));
    String backup_type = URLEncoder.encode((String)fields.get("backup_type"));
    String ftp_filename = URLEncoder.encode((String)fields.get("ftp_filename"));
    
    String url = "immediatelyBackup.jsp"
               + "?devicelist="+device_id+"&device_model="+device_model
               +"&os_version="+mib+"&backup_type="+backup_type+"&ftp_filename="+ftp_filename+"&flag=0"+"&now="+date.getTime();
               
    if (backup_type.equals("1"))
    {
       String tel_user = URLEncoder.encode((String)fields.get("tel_user"));
       String tel_pwd = (String)fields.get("tel_pwd");
       String tel_enpwd =(String)fields.get("tel_enpwd");
       String tel_encmd = URLEncoder.encode((String)fields.get("tel_encmd"));
       //-------------add by maym--------------------------------------
       tel_enpwd = URLEncoder.encode(Encoder.getFromBase64(tel_enpwd));
       tel_pwd = URLEncoder.encode(Encoder.getFromBase64(tel_pwd));
       //--------------------------------------------------------------
       String hostprompt = URLEncoder.encode((String)fields.get("hostprompt"));
       String tel_logonmod = URLEncoder.encode((String)fields.get("tel_logonmod"));
      
       url += "&tel_user="+tel_user+"&tel_logonmod="+tel_logonmod
           +"&tel_enpwd="+tel_enpwd+"&tel_encmd="+tel_encmd
           +"&tel_pwd="+tel_pwd+"&hostprompt="+hostprompt;
    }
    else  if (backup_type.equals("4"))
    {
        String tel_user = URLEncoder.encode((String)fields.get("tel_user"));
        String tel_pwd = (String)fields.get("tel_pwd");
        String tel_enpwd =(String)fields.get("tel_enpwd");
        String tel_encmd = URLEncoder.encode((String)fields.get("tel_encmd"));
        //-------------add by maym--------------------------------------
        tel_enpwd = URLEncoder.encode(Encoder.getFromBase64(tel_enpwd));
        tel_pwd = URLEncoder.encode(Encoder.getFromBase64(tel_pwd));
        //--------------------------------------------------------------
        String hostprompt = URLEncoder.encode((String)fields.get("hostprompt"));
        String tel_logonmod = URLEncoder.encode((String)fields.get("tel_logonmod"));
       
        url += "&tel_user="+tel_user+"&tel_logonmod="+tel_logonmod
            +"&tel_enpwd="+tel_enpwd+"&tel_encmd="+tel_encmd
            +"&tel_pwd="+tel_pwd+"&hostprompt="+hostprompt;
     }
    else if (backup_type.equals("2"))
    {
       String ftp_user = URLEncoder.encode((String)fields.get("ftp_user"));
       String ftp_pwd = (String)fields.get("ftp_pwd");
       String ftp_dir = URLEncoder.encode((String)fields.get("ftp_dir"));
       //-----------add by maym--------------------------------
       ftp_pwd = URLEncoder.encode(Encoder.getFromBase64(ftp_pwd));
       //------------------------------------------------------
       
       url += "&ftp_user="+ftp_user+"&ftp_pwd="+ftp_pwd
           +"&ftp_dir="+ftp_dir;
    }
    else if (backup_type.equals("3"))
    {
       String read_comm = URLEncoder.encode((String)fields.get("read_comm"));
       String write_comm = URLEncoder.encode((String)fields.get("write_comm"));
       
       url += "&read_comm="+read_comm+"&write_comm="+write_comm;
    }
    //response.setCharacterEncoding("GBK");
    response.sendRedirect(url);
}
%>
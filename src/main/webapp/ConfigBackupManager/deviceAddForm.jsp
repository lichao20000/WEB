<%--
Note		: ���ù���-�����豸������Ϣ
Date		: 2007-04-26
Author		: maym
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import ="com.linkage.liposs.manaconf.*,com.linkage.litms.common.util.*"%>
<%
	request.setCharacterEncoding("GBK");
	ManaConfig mc = new ManaConfig(request);
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	DateTimeUtil dateUtil = new DateTimeUtil();
	String time = dateUtil.getTime();
//	Cursor cursor = null;
//	Map fields = null;
	String strWeekList = ManaConfTools.getWeekSelect(null);
	String strMonthList = ManaConfTools.getMonthSelect(null);
	String strHourList = ManaConfTools.getHourList(null);
	  String city = curUser.getCityId(); //�û�������
      long areaid =curUser.getAreaId();//�û�������û���admin������ʾȫ�������أ�����ֻ��ʾ���������Լ���

      //----------------------modify by maym  070703--------------------------------------
	    String getarea= "select city_name,city_id from tab_city "+((areaid==1)?"":" where city_id='"+city+"' or parent_id='" + city + "'");
	    //----------------------------------------------------------------------------------
      Cursor areacur = DataSetBean.getCursor(getarea);
      String strAreaList = "<select name=\"city_id\" onchange=\"showChild('city_id')\">";
      strAreaList+="<option value=\"-1\">==��ѡ��==</option>";
      Map ar = new HashMap();
      while((ar = areacur.getNext())!=null)
      	{
    		strAreaList+="<option value=\""+ar.get("city_id")+"\">=="+ar.get("city_name")+"==</option>";
      	}
      strAreaList+="</select>";
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
var city_id = "-1";
function showChild(parname){
       
	if(parname=='city_id')
	{
	    var o1 = document.all("city_id");
		city_id = o1.options[o1.selectedIndex].value;
		
		if (typeof(city_id) == "undefined")
		{
		   city_id = "-1";
		}
		document.all("vendor_id").value="-1";
		document.all("device_model").value="-1";
	}
	else if(parname=="device_model")
	{
	
		var o2 = document.all("device_model");
		var dm = o2.options[o2.selectedIndex].value;
		document.all("childFrm").src = "getDeviceList.jsp?model="+dm+"&city_id="+city_id;
		
	}else if(parname=="vendor_id"){
		var o = document.all("vendor_id");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getVendorDeviceModel.jsp?vendor_id="+ id;		
	}else if(parname=="group_oid"){
		var o = document.all("group_oid");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getGroup_oid.jsp?group_oid="+ id;
	}
}
//����ָ������
function   hiddenOBJ(obj)
{   
  obj.style.display="none";   
} 
//��ʾָ������
function displayOBJ(obj)
{
   obj.style.display="";   
}

function changeTimeMod(mod)
{
   if (mod == "2")
   {
       hiddenOBJ(document.getElementById("month"));
       displayOBJ(document.getElementById("week"));
   }
   else if (mod == "3")
   {
       hiddenOBJ(document.getElementById("week"));
       displayOBJ(document.getElementById("month"));
   }
   else if (mod == "1")
   {
       hiddenOBJ(document.getElementById("month"));
       hiddenOBJ(document.getElementById("week"));
   }
   else
   {
       alert("������ѡ�񱸷�ʱ��ģʽ!");
       return false;
   }
}
function changeBKTime()
{
   var o = document.all("time_type");
   var type = o.options[o.selectedIndex].value;
   
   if (type == "2")
   {
      hiddenOBJ(document.getElementById("time1")); 
      displayOBJ(document.getElementById("time2")); 
   }
   else if (type == "1")
   {
      hiddenOBJ(document.getElementById("time2"));
      displayOBJ(document.getElementById("time1"));
   }
   else
   {
       alert("������ѡ�񱸷�ʱ��!");
       return false;
   }
}
//���ݵ�½ģʽѡ����ʾenable��Ϣ
function changeLogonMod()
{
    var o = document.all("tel_logonmod");
	var mod = o.options[o.selectedIndex].value;
	
	if (mod == "0" || mod == "1" || mod == "4")
	{
	    displayOBJ(document.getElementById("telnet3"));
	}
	else if (mod == "2" || mod == "3")
	{
	    hiddenOBJ(document.getElementById("telnet3")); 
	}
}
//���ݱ��ַ�ʽ��ʾ����
function changeBKType()
{
    var o = document.all("backup_type");
	var type = o.options[o.selectedIndex].value;
    
    if (type == "2")
    {
           hiddenOBJ(document.getElementById("telnet1")); 
           hiddenOBJ(document.getElementById("telnet2")); 
           hiddenOBJ(document.getElementById("telnet3")); 
           displayOBJ(document.getElementById("ftp1"));  
           displayOBJ(document.getElementById("ftp2"));      
           hiddenOBJ(document.getElementById("snmp"));
    }   
    else if (type == "1")
    {
           displayOBJ(document.getElementById("telnet1")); 
           displayOBJ(document.getElementById("telnet2")); 
           displayOBJ(document.getElementById("telnet3")); 
           hiddenOBJ(document.getElementById("ftp1"));  
           hiddenOBJ(document.getElementById("ftp2"));      
           hiddenOBJ(document.getElementById("snmp"));   
    }     
    else if (type == "3")
    {
           hiddenOBJ(document.getElementById("telnet1")); 
           hiddenOBJ(document.getElementById("telnet2")); 
           hiddenOBJ(document.getElementById("telnet3")); 
           hiddenOBJ(document.getElementById("ftp1"));  
           hiddenOBJ(document.getElementById("ftp2"));      
           displayOBJ(document.getElementById("snmp")); 
    }
    else 
    {
       alert("������ѡ�񱸷ݷ�ʽ!");
       return false;
    }
     
}

//����
function checkForm()
{
    if (document.form1.city_id.value == -1)
    {
            alert("����ѡ��ɼ����أ�");
			document.form1.city_id.focus();
            return false;
    }
    
    if (document.form1.vendor_id.value == -1)
    {
            alert("����ѡ���豸���̣�");
			document.form1.vendor_id.focus();
            return false;
    }
    
    if (document.form1.device_model.value == -1)
    {
            alert("����ѡ���豸�ͺţ�");
			document.form1.device_model.focus();
            return false;
    }
    
    if (document.form1.devicelist.value == -1)
    {
            alert("����ѡ���豸IP��");
			document.form1.devicelist.focus();
            return false;
    }
    
   if (document.form1.ftp_filename.value == '')
   {
               
        alert("���������ر����ļ�����!");
	    document.form1.ftp_filename.focus();
        return false;
   } 
   else if (document.form1.ftp_filename.value.length > 50)
   {
        alert("���ر����ļ�����������볤�Ȳ��ܴ���50�ַ�!");
	    document.form1.ftp_filename.focus();
        return false;
   }
   
        
    var o = document.all("backup_type");
    var type = o.options[o.selectedIndex].value;
	o = document.all("tel_logonmod");
	mod = o.options[o.selectedIndex].value;
	mod = parseInt(mod.trim());
	
    if (type == "1")
    {           
           switch(mod)
           {
               //�û����롢enable��enable����
               case 0:
                     if (document.form1.tel_pwd.value == '')
                     {
                          alert("������TELNET���룡");
			              document.form1.tel_pwd.focus();
                          return false;
                     }
                     else if (document.form1.tel_pwd.value.length > 40)
                     {
                          alert("TELNET����������볤�Ȳ��ܴ���40�ַ�!");
	                      document.form1.tel_pwd.focus();
                          return false;
                     }
                     if (document.form1.tel_encmd.value == '')
                     {
                         alert("������enable���");
			             document.form1.tel_encmd.focus();
                         return false;
                     }
                     else if (document.form1.tel_encmd.value.length > 50)
                     {
                           alert("enbale����������볤�Ȳ��ܴ���50�ַ�!");
	                       document.form1.tel_encmd.focus();
                           return false;
                     }
                     if (document.form1.tel_enpwd.value == '')
                     {
                         alert("������enable���룡");
			             document.form1.tel_enpwd.focus();
                         return false;
                     }
                     else if (document.form1.tel_enpwd.value.length > 40)
                     {
                         alert("enable����������볤�Ȳ��ܴ���40�ַ�!");
	                     document.form1.tel_enpwd.focus();
                         return false;
                     }
                     break;
               //�û������û����롢enable��enable����
               case 1:
                     if (document.form1.tel_user.value == '')
                     {
                         alert("������TELNET�û�����");
			             document.form1.tel_user.focus();
                         return false;
                     }
                     else if (document.form1.tel_user.value.length > 50)
                     {
                         alert("TELNET�û���������볤�Ȳ��ܴ���50�ַ�!");
	                     document.form1.tel_user.focus();
                         return false;
                     }
                     if (document.form1.tel_pwd.value == '')
                     {
                          alert("������TELNET���룡");
			              document.form1.tel_pwd.focus();
                          return false;
                     }
                     else if (document.form1.tel_pwd.value.length > 40)
                     {
                          alert("TELNET����������볤�Ȳ��ܴ���40�ַ�!");
	                      document.form1.tel_pwd.focus();
                          return false;
                     }
                     if (document.form1.tel_encmd.value == '')
                     {
                         alert("������enable���");
			             document.form1.tel_encmd.focus();
                         return false;
                     }
                     else if (document.form1.tel_encmd.value.length > 50)
                     {
                           alert("enbale����������볤�Ȳ��ܴ���50�ַ�!");
	                       document.form1.tel_encmd.focus();
                           return false;
                     }
                     if (document.form1.tel_enpwd.value == '')
                     {
                         alert("������enable���룡");
			             document.form1.tel_enpwd.focus();
                         return false;
                     }
                     else if (document.form1.tel_enpwd.value.length > 40)
                     {
                         alert("enable����������볤�Ȳ��ܴ���40�ַ�!");
	                     document.form1.tel_enpwd.focus();
                         return false;
                     }
                     break;
               //�û�����
               case 2:
                     if (document.form1.tel_pwd.value == '')
                     {
                          alert("������TELNET���룡");
			              document.form1.tel_pwd.focus();
                          return false;
                     }
                     else if (document.form1.tel_pwd.value.length > 40)
                     {
                          alert("TELNET����������볤�Ȳ��ܴ���40�ַ�!");
	                      document.form1.tel_pwd.focus();
                          return false;
                     }
                     break;
               //�û������û�����
               case 3:
                     if (document.form1.tel_user.value == '')
                     {
                         alert("������TELNET�û�����");
			             document.form1.tel_user.focus();
                         return false;
                     }
                     else if (document.form1.tel_user.value.length > 50)
                     {
                         alert("TELNET�û���������볤�Ȳ��ܴ���50�ַ�!");
	                     document.form1.tel_user.focus();
                         return false;
                     }
                     if (document.form1.tel_pwd.value == '')
                     {
                          alert("������TELNET���룡");
			              document.form1.tel_pwd.focus();
                          return false;
                     }
                     else if (document.form1.tel_pwd.value.length > 40)
                     {
                          alert("TELNET����������볤�Ȳ��ܴ���40�ַ�!");
	                      document.form1.tel_pwd.focus();
                          return false;
                     }
                     enable
               //�û������û����롢enable
               case 4:
                     if (document.form1.tel_user.value == '')
                     {
                         alert("������TELNET�û�����");
			             document.form1.tel_user.focus();
                         return false;
                     }
                     else if (document.form1.tel_user.value.length > 50)
                     {
                         alert("TELNET�û���������볤�Ȳ��ܴ���50�ַ�!");
	                     document.form1.tel_user.focus();
                         return false;
                     }
                     if (document.form1.tel_pwd.value == '')
                     {
                          alert("������TELNET���룡");
			              document.form1.tel_pwd.focus();
                          return false;
                     }
                     else if (document.form1.tel_pwd.value.length > 40)
                     {
                          alert("TELNET����������볤�Ȳ��ܴ���40�ַ�!");
	                      document.form1.tel_pwd.focus();
                          return false;
                     }
                     if (document.form1.tel_encmd.value == '')
                     {
                         alert("������enable���");
			             document.form1.tel_encmd.focus();
                         return false;
                     }
                     else if (document.form1.tel_encmd.value.length > 50)
                     {
                           alert("enbale����������볤�Ȳ��ܴ���50�ַ�!");
	                       document.form1.tel_encmd.focus();
                           return false;
                     }
                     break;
               default: alert("����ȷѡ���¼ģʽ��");
                        return false;
           }
            if (document.form1.hostprompt.value == '')
            {
                alert("������������ʾ����");
			    document.form1.hostprompt.focus();
                return false;
            }
            else if (document.form1.hostprompt.value.length > 50)
            {
                 alert("������ʾ��������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.hostprompt.focus();
                 return false;
            }
            /*
            if (document.form1.tel_user.value == '')
            {
                alert("������TELNET�û�����");
			    document.form1.tel_user.focus();
                return false;
            }
            else if (document.form1.tel_user.value.length > 50)
            {
                 alert("TELNET�û���������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.tel_user.focus();
                 return false;
            }
            
            if (document.form1.tel_pwd.value == '')
            {
                alert("������TELNET���룡");
			    document.form1.tel_pwd.focus();
                return false;
            }
            else if (document.form1.tel_pwd.value.length > 40)
            {
                 alert("TELNET����������볤�Ȳ��ܴ���40�ַ�!");
	             document.form1.tel_pwd.focus();
                 return false;
            }
            
            if (document.form1.hostprompt.value == '')
            {
                alert("������������ʾ����");
			    document.form1.hostprompt.focus();
                return false;
            }
            else if (document.form1.hostprompt.value.length > 50)
            {
                 alert("������ʾ��������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.hostprompt.focus();
                 return false;
            }
            
            
             o = document.all("tel_logonmod");
	         mod = o.options[o.selectedIndex].value;
     
             
             if (mod == "0" || mod == "1" || mod == "4" || mod == "5")
             {
                     if (document.form1.tel_encmd.value == '')
                     {
                         alert("������enable���");
			             document.form1.tel_encmd.focus();
                         return false;
                     }
                     else if (document.form1.tel_encmd.value.length > 50)
                     {
                           alert("enbale����������볤�Ȳ��ܴ���50�ַ�!");
	                       document.form1.tel_encmd.focus();
                           return false;
                     }
                     if (document.form1.tel_enpwd.value == '')
                     {
                         alert("������enable���룡");
			             document.form1.tel_enpwd.focus();
                         return false;
                     }
                     else if (document.form1.tel_enpwd.value.length > 40)
                     {
                         alert("enable����������볤�Ȳ��ܴ���40�ַ�!");
	                     document.form1.tel_enpwd.focus();
                         return false;
                     }
            }*/
    }
    else if (type == "2")
    {
            if (document.form1.ftp_user.value == '')
            {
                alert("������FTP�û�����");
			    document.form1.ftp_user.focus();
                return false;
            }
            else if (document.form1.ftp_user.value.length > 50)
            {
                 alert("FTP�û���������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.ftp_user.focus();
                 return false;
            }
            
            if (document.form1.ftp_pwd.value == '')
            {
                alert("������FTP���룡");
			    document.form1.ftp_pwd.focus();
                return false;
            }
            else if (document.form1.ftp_pwd.value.length > 40)
            {
                 alert("FTP����������볤�Ȳ��ܴ���40�ַ�!");
	             document.form1.ftp_pwd.focus();
                 return false;
            }
            
            if (document.form1.ftp_dir.value == '')
            {
                alert("���������ر����ļ�·����");
			    document.form1.ftp_dir.focus();
                return false;
            }
            else if (document.form1.ftp_dir.value.length > 50)
            {
                 alert("���ر����ļ�·��������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.ftp_dir.focus();
                 return false;
            }      
    }
    else if (type == "3")
    {
            if (document.form1.read_comm.value == '')
            {
                alert("������read community��");
			    document.form1.read_comm.focus();
                return false;
            } 
            else if (document.form1.read_comm.value.length > 50)
            {
                 alert("read community������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.read_comm.focus();
                 return false;
            }        
            
            if (document.form1.write_comm.value == '')
            {
                alert("������write community��");
			    document.form1.write_comm.focus();
                return false;
            }
            else if (document.form1.read_comm.value.length > 50)
            {
                 alert("write community������볤�Ȳ��ܴ���50�ַ�!");
	             document.form1.read_comm.focus();
                 return false;
            }     
    }
    
   o = document.all("time_type");
   type = o.options[o.selectedIndex].value;
   
   if (type == "1")
   {
            if (document.form1.backup_time1.value == '')
            {
                alert("��ѡ�񱸷����ڣ�");
			    document.form1.backup_time1.focus();
                return false;
            }   
            else
            {
                           
               var result = checkDateFormat(document.form1.backup_time1.value)
               if (result != "1")
               {
                  alert(result);
                  document.form1.backup_time1.focus();
                  return false;
                 
               }
            }
    
            if (document.form1.backup_time2.value == '')
            {
                alert("�����뱸��ʱ�䣡");
			    document.form1.backup_time2.focus();
                return false;
            }   
            else
            {
                result = checkTimeFormat(document.form1.backup_time2.value)
                if (result != "1")
                {
                   alert(result);
                   document.form1.backup_time2.focus();
                   return false;
                }
            }

   }
   
    if (backup_type == "1")
    { 
       document.form1.tel_user.value = escape(document.form1.tel_user.value);
       document.form1.tel_pwd.value = escape(document.form1.tel_pwd.value);
       document.form1.tel_enpwd.value = escape(document.form1.tel_enpwd.value);
       document.form1.hostprompt.value = escape(document.form1.hostprompt.value);
    } 
    else if (backup_type == "2")
    {
       document.form1.ftp_user.value = escape(document.form1.ftp_user.value);
       document.form1.ftp_pwd.value = escape(document.form1.ftp_pwd.value);  
    }
    else if (backup_type == "3")
    {
       document.form1.read_comm.value = escape(document.form1.read_comm.value);
       document.form1.write_comm.value = escape(document.form1.write_comm.value);
    }
  
}

//�����Ƿ�ƥ���ʽ YYYY-MM-DD��YYYY-M-D
function checkDateFormat(date)
{
   var result = null;
   var re = /((19|20)\d{2})[\-]\b(1[0-2]|0?[1-9])[\-]\b(0?[1-9]|[12][0-9]|3[01])\b/;
   var matchArray = re.exec(date);
   if (matchArray)
   {
       result = "1";
   } 
   else
   {
       result = "�밴��YYYY-MM-DD����YYYY-M-D�������ڣ�";
   }
   return result;
}

//ʱ���ʽƥ�亯��    HH:MM:SS
function checkTimeFormat(time)
{
   var result = null;
   var re = /([012]\d{1})[\:]\b[012345]\d{1}[\:]\b[012345]\d{1}/;
   var matchArray = re.exec(time);
   if (matchArray)
   {
       result = "1";
   } 
   else
   {
       result = "�밴��HH:MM:SS����ʱ�䣡";
   }
   return result;
}

function initTime()
{
	var vNow = new Date();
	var y0  = vNow.getFullYear();
	var m0  = vNow.getMonth()+1;
	var d0  = vNow.getDate();
	document.form1.backup_time1.value = y0 + "-" + m0 + "-" + d0;
}	
</script>

<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=14>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="form1" action="deviceSave.jsp?" methed="POST" onsubmit="return checkForm()">
				<TABLE width="96%" border=0 align="center" cellpadding=0
					cellspacing=0>
					<TR>
						<TD bgcolor=#999999>
							<TABLE width="100%" border=0 align="center" cellpadding=2
								cellspacing=1>
								<TR class="blue_title">
									<td colspan=8>
										<div align="center">
											���ù����豸����
										</div>
									</td>
								</TR>

								<TR>
									<TD bgcolor="#FFFFFF">
										<div align=right>
											���أ�
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
                                       <%=strAreaList %>
									</TD>
									<TD bgcolor="#FFFFFF">
										<div align="right">
											�豸���̣�
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
										<%=strVendorList%>
									</TD>
								</TR>
								<TR>
									<TD bgcolor="#FFFFFF">
										<div align="right">
											�豸�ͺţ�
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
										<span id=strModelList></span>
									</TD>
									<TD bgcolor="#FFFFFF">
										<div align="right">
											�豸IP��
										</div>
									</TD>
									<TD bgcolor="#FFFFFF"><span id=strDeviceList> <select
							id="devicelist" name="devicelist"
							style="position:absolute;top:0px;left:0px;width:145px;"
							onchange="getMib(this.options[this.selectedIndex].text);"><option value="-1">==��ѡ��==</option></select>
							</span></TD>
								</TR>
								<TR>
									<TD bgcolor="#FFFFFF">
										<div align="right">
											�Ƿ񱸷ݣ�
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
										<select name=if_backup class=bk>
											<option value=1>
												��
											</option>
											<option value=0>
												��
											</option>
										</select>
									</TD>
									<TD bgcolor="#FFFFFF">
										<div align="right">
											���ݷ�ʽ��
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
										<select name=backup_type class=bk onchange=changeBKType()>
											<option value=1>
												TELNET
											</option>
											<option value=2>
												FTP
											</option>
											<option value=3>
												SNMP
											</option>
											<option value=3>
												SHOW
											</option>
										</select>
									</TD>
								</TR>

								<TR id=telnet1 style="display:block">
									<TD align=right bgcolor="#FFFFFF">
										telnet�û�����
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="text" name="tel_user">
									</TD>
									<TD align=right bgcolor="#FFFFFF">
										telnet���룺
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="password" name="tel_pwd">
									</TD>
								</TR>
								<TR id=telnet2 style="display:block">
									<TD align=right bgcolor="#FFFFFF">
										������ʾ����
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="text" name="hostprompt" value="#">
									</TD>
									<TD align=right bgcolor="#FFFFFF">
										��½ģʽ��
									</TD>
									<TD bgcolor="#FFFFFF">
										<select name="tel_logonmod" onchange="changeLogonMod()">
											<option value="0">
												�û����롢enable��enable����
											</option>
											<option value="1">
												�û������û����롢enable��enable����
											</option>
											<option value="2">
												�û�����
											</option>
											<option value="3" selected>
												�û������û�����
											</option>
											<option value="4">
												�û������û����롢enable
											</option>
										</select>
									</TD>
								</TR>
								<TR id=telnet3 style="display:block">
													
									<TD align=right bgcolor="#FFFFFF">
										enable���
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="text" name="tel_encmd">
									</TD>
								
									<TD align=right bgcolor="#FFFFFF">
										enable���룺
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="password" name="tel_enpwd">
									</TD>
								</TR>

								<TR id=ftp1 style="display:none">
									<TD align=right bgcolor="#FFFFFF">
										FTP�û�����
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="text" name="ftp_user">
									</TD>
									<TD align=right bgcolor="#FFFFFF">
										FTP���룺
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="password" name="ftp_pwd">
									</TD>
								</TR>
								<TR id=ftp2 style="display:none">
									<TD align=right bgcolor="#FFFFFF">
										���ر����ļ�·����
									</TD>
									<TD bgcolor="#FFFFFF" colspan=3>
										<input type="text" name="ftp_dir" size="48">
									</TD>
								</TR>
								<TR id=snmp style="display:none">
									<TD bgcolor="#FFFFFF">
										<div align=right>
											write community��
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="password" name="write_comm">
									</TD>
									<TD bgcolor="#FFFFFF">
										<div align=right>
											read community��
										</div>
									</TD>
									<TD bgcolor="#FFFFFF">
										<input type="password" name="read_comm">
									</TD>
								</TR>
								
								<TR style="display:block">
								    <TD align=right bgcolor="#FFFFFF">
										���ر����ļ����ƣ�
									</TD>
									<TD bgcolor="#FFFFFF" COLSPAN=3>
										<input type="text" name="ftp_filename" size="48" value="temp.cfg">
									</TD>
                                </TR>

								<TR>
									<TD align=right bgcolor="#FFFFFF">
										����ʱ�䣺
									</TD>
									<TD bgcolor="#FFFFFF">
										<select name=time_type onchange=changeBKTime()>
											<option value=1>
												��ʱ����
											</option>
											<option value=2>
												���ڱ���
											</option>
										</select>
									</TD>
									<TD align=right bgcolor="#FFFFFF">
										ѡ��ʱ�䣺
									</TD>
									<TD bgcolor="#FFFFFF">
										<table>
											<TR id=time1 style="display:block">
												<TD bgcolor="#FFFFFF">
													<input type="text" name="backup_time1">
													<input type="button" value="��" class=btn
														onClick="showCalendar('day',event)" name="button">
													<input type="text" name="backup_time2" class=bk
														value="<%=time%>">
												<TD>
											</TR>
											<TR  id=time2 style="display:none">
												<TD bgcolor="#FFFFFF">
													ÿ��
													<input type="radio" name="time_model" checked="checked"
														value="1" onclick="changeTimeMod('1')"/>
													ÿ��
													<input type="radio" name="time_model" value="2" onclick="changeTimeMod('2')"/>
													ÿ��
													<input type="radio" name="time_model" value="3" onclick="changeTimeMod('3')"/>
                                                    <%=strWeekList%>
                                                    <%=strMonthList%>
                                                    <%=strHourList%>

												<TD>
											</TR>
											<TR>
											  <TD COLSPAN=4>
											     <INPUT TYPE="hidden" name="act" value="add">
											  </TD>
											</TR>
										</table>
									</TD>
								</TR>
								<TR class="blue_foot">
									<TD colspan=2 align=RIGHT>
										<input type="submit" name="b1" value=" ��  �� " class=jianbian>
								    </TD>
								    <TD colspan=2 align=LEFT>		
										<INPUT TYPE="reset" name="b2" value=" ��  �� " class=jianbian>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD HEIGHT=20>
							<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
							<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
							&nbsp;
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
initTime();
//-->
</SCRIPT>


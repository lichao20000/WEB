<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%
String ifindex="";
String ifdescr="";
String ifname="";
String ifnamedefined="";
String iftype="-1";
String ifspeed="-1";
String ifmtu="-1";
String ifhighspeed="-1";
String intodb="1";
//begin modifided by w5221 ���������ʡ���̬��ֵ�Ǳ�������ֵ����
String ifinoct_maxtype="2";
String ifinoctetsbps_max="80";
String ifoutoct_maxtype="2";
String ifoutoctetsbps_max="80";
String ifindiscardspps_max="-1";
String ifoutdiscardspps_max="-1";
String ifinerrorspps_max="-1";
String ifouterrorspps_max="-1";
String warningnum="3";
String warninglevel="3";
String reinstatelevel="1";
//��ֵ��
String ifinoct_mintype="0";
String ifinoctetsbps_min="-1";
String ifoutoct_mintype="0";
String ifoutoctetsbps_min="0";
String warningnum_min="3";
String warninglevel_min="3";
String reinlevel_min="1";

//��̬��ֵ
String overmax="0";
String overper="-1";
String overnum="3";
String overlevel="3";
String reinoverlevel="1";
//��̬��ֵ��
String overmin="0";
String overper_min="-1";
String overnum_min="3";
String overlevel_min="3";
String reinoverlevel_min="1";
String com_day="3";
//ͻ����ֵ
String intbflag="0";
String ifinoctets="-1";
String inoperation="1";
String inwarninglevel="3";
String inreinstatelevel="1";
String outtbflag="0";
String ifoutoctets="-1";
String outwarninglevel="3";
String outreinstatelevel="1";
String outoperation="1";
String gatherflag="1";
//end modifided by w5221 ���������ʡ���̬��ֵ�Ǳ�������ֵ����

String device_ip = request.getParameter("device_ip");
String device_id = request.getParameter("device_id");
String[] ports = request.getParameterValues("port");

String port_info = null;
//ֻ��ѡ��һ���豸��ʱ����Ҫ�������ֵ������
if(ports!=null&&ports.length==1)
{
	port_info=ports[0];	
	ManagerFluxConfig fluxConfig = new ManagerFluxConfig(request);
	HashMap portInfo =fluxConfig.getDevicePortInfo();
	if(0!=portInfo.size())
	{
		ifindex=(String)portInfo.get("ifindex");
		ifdescr=(String)portInfo.get("ifdescr");
		ifname=(String)portInfo.get("ifname");
		ifnamedefined=(String)portInfo.get("ifnamedefined");
		iftype=(String)portInfo.get("iftype");
		ifspeed=(String)portInfo.get("ifspeed");
		ifmtu=(String)portInfo.get("ifmtu");
		ifhighspeed=(String)portInfo.get("ifhighspeed");
		intodb=(String)portInfo.get("intodb");	
		//�̶���ֵһ
		ifinoct_maxtype=(String)portInfo.get("ifinoct_maxtype");
		ifinoctetsbps_max=(String)portInfo.get("ifinoctetsbps_max");
        ifoutoct_maxtype=(String)portInfo.get("ifoutoct_maxtype");
		ifoutoctetsbps_max=(String)portInfo.get("ifoutoctetsbps_max");
		ifindiscardspps_max=(String)portInfo.get("ifindiscardspps_max");
		ifoutdiscardspps_max=(String)portInfo.get("ifoutdiscardspps_max");
		ifinerrorspps_max=(String)portInfo.get("ifinerrorspps_max");
		ifouterrorspps_max=(String)portInfo.get("ifouterrorspps_max");
		warningnum=(String)portInfo.get("warningnum");
		warninglevel=(String)portInfo.get("warninglevel");
		reinstatelevel=(String)portInfo.get("reinstatelevel");
        //�̶���ֵ��
        ifinoct_mintype=(String)portInfo.get("ifinoct_mintype");
        ifinoctetsbps_min=(String)portInfo.get("ifinoctetsbps_min");
        ifoutoct_mintype=(String)portInfo.get("ifoutoct_mintype");
        ifoutoctetsbps_min=(String)portInfo.get("ifoutoctetsbps_min");
        warningnum_min=(String)portInfo.get("warningnum_min");
		warninglevel_min=(String)portInfo.get("warninglevel_min");
		reinlevel_min=(String)portInfo.get("reinlevel_min");
        //��̬��ֵһ
		overmax=(String)portInfo.get("overmax");
		overper=(String)portInfo.get("overper");
		overnum=(String)portInfo.get("overnum");
		overlevel=(String)portInfo.get("overlevel");
		reinoverlevel=(String)portInfo.get("reinoverlevel");
		//��̬��ֵ��
		overmin=(String)portInfo.get("overmin");
		overper_min=(String)portInfo.get("overper_min");
		overnum_min=(String)portInfo.get("overnum_min");
		overlevel_min=(String)portInfo.get("overlevel_min");
		reinoverlevel_min=(String)portInfo.get("reinoverlevel_min");
		com_day=(String)portInfo.get("com_day");		
		//ͻ����ֵ
		intbflag=(String)portInfo.get("intbflag");
		ifinoctets=(String)portInfo.get("ifinoctets");
		inoperation=(String)portInfo.get("inoperation");
		inwarninglevel=(String)portInfo.get("inwarninglevel");
		inreinstatelevel=(String)portInfo.get("inreinstatelevel");
		outtbflag=(String)portInfo.get("outtbflag");
		ifoutoctets=(String)portInfo.get("ifoutoctets");
		outwarninglevel=(String)portInfo.get("outwarninglevel");
		outreinstatelevel=(String)portInfo.get("outreinstatelevel");
		outoperation=(String)portInfo.get("outoperation");
		gatherflag =(String)portInfo.get("gatherflag");
	}
	
	//clear
	portInfo = null;	
}
else if(ports!=null)
{
	for(int i=0;i<ports.length;i++)
	{
		if(null==port_info)
		{
			port_info=ports[i];
		}
		else
		{
			port_info+="$$$"+ports[i];
		}
	}
	
}

%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//�������仯js
function showDisplay(name)
{
	//�ı����������������ֵһ������
	if(name=="ifinoct_maxtype")
	{
       var ifinoct_maxtype = document.all("ifinoct_maxtype").value;
	   if(ifinoct_maxtype==0)
		{
		   document.all("ifinoctetsbps_max").disabled=true;
		}
		else
		{
		   document.all("ifinoctetsbps_max").disabled=false;
		}
	}
	//�ı�����������������ֵһ������
	else if(name=="ifoutoct_maxtype")
	{
	   var ifoutoct_maxtype = document.all("ifoutoct_maxtype").value;
	   if(ifoutoct_maxtype==0)
		{
		   document.all("ifoutoctetsbps_max").disabled=true;
		}
		else
		{
		   document.all("ifoutoctetsbps_max").disabled=false;
		}
	}
	//�ı����������������ֵ��������
	else if(name=="ifinoct_mintype")
	{
		var ifinoct_mintype = document.all("ifinoct_mintype").value;
	   if(ifinoct_mintype==0)
		{
		   document.all("ifinoctetsbps_min").disabled=true;
		}
		else
		{
		   document.all("ifinoctetsbps_min").disabled=false;
		}
	}
	//�ı�����������������ֵ��������
	else if(name=="ifoutoct_mintype")
	{
		var ifoutoct_mintype = document.all("ifoutoct_mintype").value;
	   if(ifoutoct_mintype==0)
		{
		   document.all("ifoutoctetsbps_min").disabled=true;
		}
		else
		{
		   document.all("ifoutoctetsbps_min").disabled=false;
		}
	}
	//��̬��ֵһ������
	else if(name=="overmax")
	{
		var overmax = document.all("overmax").value;
	   if(overmax==0)
		{
		   document.all("overper").disabled=true;
		   document.all("overnum").disabled=true;
		   document.all("overlevel").disabled=true;
		   document.all("reinoverlevel").disabled=true;	   
		}
		else
		{
		   document.all("overper").disabled=false;
		   document.all("overnum").disabled=false;
		   document.all("overlevel").disabled=false;
		   document.all("reinoverlevel").disabled=false;	 
		}
	}
	//��̬��ֵ��������
	else if(name=="overmin")
	{
		var overmin = document.all("overmin").value;
	   if(overmin==0)
		{
		   document.all("overper_min").disabled=true;
		   document.all("overnum_min").disabled=true;
		   document.all("overlevel_min").disabled=true;
		   document.all("reinoverlevel_min").disabled=true;	 
		}
		else
		{
		   document.all("overper_min").disabled=false;
		   document.all("overnum_min").disabled=false;
		   document.all("overlevel_min").disabled=false;
		   document.all("reinoverlevel_min").disabled=false;
		}
	}
}
//���ҳ�������Ƿ���ȷ
function CheckForm()
{
  //���������������������ֵһ
  var ifinoct_maxtype = document.all("ifinoct_maxtype").value;
  if(ifinoct_maxtype>0)
  {
    if(!IsNull(document.all("ifinoctetsbps_max").value,"�˿����������������ֵһ��")
         ||!isFloatNumber(document.all("ifinoctetsbps_max").value,"�˿����������������ֵһ��������"))
     {       
        return false;
     }
     else if(document.all("ifinoctetsbps_max").value>100)
     {
        alert("�˿����������������ֵһ����100");
        return false;
     }     
  }

  //����������������������ֵ��
  var ifoutoct_maxtype = document.all("ifoutoct_maxtype").value;
  if(ifoutoct_maxtype>0)
  {
	  if(!IsNull(document.all("ifoutoctetsbps_max").value,"�˿�����������������ֵһ��")
         ||!isFloatNumber(document.all("ifoutoctetsbps_max").value,"�˿�����������������ֵһ��������"))
     {
        return false;
     }
     else if(document.all("ifoutoctetsbps_max").value>100)
     {
        alert("�˿�����������������ֵһ����100");
        return false;
     }     
  }  
  
  //ѡ�ж˿����붪������ֵ
  if(document.all("ifindiscardspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifindiscardspps_max").value,"�˿����붪������ֵ��")
         ||!isFloatNumber(document.all("ifindiscardspps_max").value,"�˿����붪������ֵ��������"))
     {
        return false;
     }
     else if(document.all("ifindiscardspps_max").value>100)
     {
        alert("�˿����붪������ֵ����100");
        return false;
     }
  }
  
  
   //ѡ�ж˿�������������ֵ
  if(document.all("ifoutdiscardspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifoutdiscardspps_max").value,"�˿�������������ֵ��")
         ||!isFloatNumber(document.all("ifoutdiscardspps_max").value,"�˿�������������ֵ��������"))
     {
        return false;
     }
     else if(document.all("ifoutdiscardspps_max").value>100)
     {
        alert("�˿�������������ֵ����100");
        return false;
     }
  }
  
  
   //ѡ�ж˿�����������ֵ
  if(document.all("ifinerrorspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifinerrorspps_max").value,"�˿�����������ֵ��")
         ||!isFloatNumber(document.all("ifinerrorspps_max").value,"�˿�����������ֵ��������"))
     {
        return false;
     }
     else if(document.all("ifinerrorspps_max").value>100)
     {
        alert("�˿�����������ֵ����100");
        return false;
     }
  }
  
   //ѡ�ж˿������������ֵ
  if(document.all("ifouterrorspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifouterrorspps_max").value,"�˿�����������ֵ��")
         ||!isFloatNumber(document.all("ifouterrorspps_max").value,"�˿�����������ֵ��������"))
     {
        return false;
     }
     else if(document.all("ifouterrorspps_max").value>100)
     {
        alert("�˿������������ֵ����100");
        return false;
     }
  }
  
  
  if(!IsNull(document.all("warningnum").value,"������ֵһ�Ĵ���Ϊ��")
        ||!IsNumber(document.all("warningnum").value,"������ֵһ�Ĵ�����������"))
   {
       return false;
   }

   //�������������������ֵ��
   var ifinoct_mintype = document.all("ifinoct_mintype").value;
   if(ifinoct_mintype>0)
   {
	  if(!IsNull(document.all("ifinoctetsbps_min").value,"�˿����������������ֵ����")
         ||!isFloatNumber(document.all("ifinoctetsbps_min").value,"�˿����������������ֵ����������"))
     {       
        return false;
     }
     else if(document.all("ifinoctetsbps_min").value>100)
     {
        alert("�˿����������������ֵ������100");
        return false;
     }
   }

   //��������������������ֵ��
   var ifoutoct_mintype = document.all("ifoutoct_mintype").value;
   if(ifoutoct_mintype>0)
   {
	  if(!IsNull(document.all("ifoutoctetsbps_min").value,"�˿�����������������ֵ����")
         ||!isFloatNumber(document.all("ifoutoctetsbps_min").value,"�˿�����������������ֵ����������"))
     {       
        return false;
     }
     else if(document.all("ifoutoctetsbps_min").value>100)
     {
        alert("�˿�����������������ֵ������100");
        return false;
     }
   }

    if(!IsNull(document.all("warningnum_min").value,"������ֵ���Ĵ���Ϊ��")
        ||!IsNumber(document.all("warningnum_min").value,"������ֵ���Ĵ�����������"))
   {
       return false;
   }

   //��̬��ֵһ����
   var overmax = document.all("overmax").value;
   if(overmax>0)
   {
	 if(!IsNull(document.all("overper").value,"��̬��ֵһ�ٷֱ�Ϊ��")
          ||!IsNumber(document.all("overper").value,"��̬��ֵһ�ٷֱȲ�������"))
     {
        return false;
     }
     else
     {
       if(document.all("overper").value>100)
       {
          alert("��̬��ֵһ�ٷֱȲ��ܳ���100");
          return false;
       }
     }
     
     if(!IsNull(document.all("overnum").value,"������̬��ֵһ�Ĵ���Ϊ��")
          ||!IsNumber(document.all("overnum").value,"������̬��ֵһ�Ĵ�����������"))
     {
        return false;
     }

	 if(!IsNull(document.all("com_day").value,"���ɶ�̬��ֵ����Ϊ��")
          ||!IsNumber(document.all("com_day").value,"���ɶ�̬��ֵ������������"))
     {
        return false;
     }
     else
     {
       if(document.all("com_day").value>3)
       {
          alert("���ɶ�̬��ֵ�������ܳ���3��");
          return false;
       }
     }
   }
   
   //��̬��ֵ������
   var overmin = document.all("overmin").value;
   if(overmin>0)
   {
	    if(!IsNull(document.all("overper_min").value,"��̬��ֵ���ٷֱ�Ϊ��")
          ||!IsNumber(document.all("overper_min").value,"��̬��ֵ���ٷֱȲ�������"))
     {
        return false;
     }
     else
     {
       if(document.all("overper_min").value>100)
       {
          alert("��̬��ֵ���ٷֱȲ��ܳ���100");
          return false;
       }
     }
     
     if(!IsNull(document.all("overnum_min").value,"������̬��ֵ���Ĵ���Ϊ��")
          ||!IsNumber(document.all("overnum_min").value,"������̬��ֵ���Ĵ�����������"))
     {
        return false;
     }

	 if(!IsNull(document.all("com_day").value,"���ɶ�̬��ֵ����Ϊ��")
          ||!IsNumber(document.all("com_day").value,"���ɶ�̬��ֵ������������"))
     {
        return false;
     }
     else
     {
       if(document.all("com_day").value>3)
       {
          alert("���ɶ�̬��ֵ�������ܳ���3��");
          return false;
       }
     }
   }
   
   //ͻ����ֵ
   if(document.all("intbflag").checked)
   {
      if(!IsNull(document.all("ifinoctets").value,"�������ʱ仯����ֵΪ��")
          ||!isFloatNumber(document.all("ifinoctets").value,"�������ʱ仯����ֵ��������"))
      {
         return false;
      }
      else if(document.all("ifinoctets").value>100||document.all("ifinoctets").value<0)
      {
        alert("�������ʱ仯����ֵ�����Ǵ���0��С��100������");
        return false;
      }    
   }
   
   if(document.all("outtbflag").checked)
   {
      if(!IsNull(document.all("ifoutoctets").value,"�������ʱ仯����ֵΪ��")
          ||!isFloatNumber(document.all("ifoutoctets").value,"�������ʱ仯����ֵ��������"))
      {
         return false;
      }
      else if(document.all("ifoutoctets").value>100||document.all("ifoutoctets").value<0)
      {
        alert("�������ʱ仯����ֵ�����Ǵ���0��С��100������");
        return false;
      }   
   }
   
   frm.submit();    
}

function showpage(uniformid)
{       
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="button_onblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="";
			document.all("test2").style.display="none";
			document.all("test3").style.display="none";

			break;
		} 
		case 2:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_onblue";
			document.all("td3").className="button_outblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="";
			document.all("test3").style.display="none";

			break;
		}	
		case 3:
		{
			document.all("td1").className="button_outblue";
			document.all("td2").className="button_outblue";
			document.all("td3").className="button_onblue";
			document.all("test1").style.display="none";
			document.all("test2").style.display="none";
			document.all("test3").style.display="";

			break;
		}	
	}
}

function checked1(name)
{
   //alert("checked1");
   var Obj = document.all(name);
   if(Obj.checked)
   {
      if("ifindiscardspps_max_checkbox"==name)
      {
         document.all("ifindiscardspps_max").disabled= false;
      }      
      else if("ifoutdiscardspps_max_checkbox"==name)
      {
         document.all("ifoutdiscardspps_max").disabled= false;
      }
      else if("ifinerrorspps_max_checkbox"==name)
      {
         document.all("ifinerrorspps_max").disabled= false;
      }
      else if("ifouterrorspps_max_checkbox"==name)
      {
         document.all("ifinerrorspps_max").disabled= false;
      }
      else if("intbflag"==name)
      {
         document.all("intbflag").value="1";         
         document.all("ifinoctets").disabled= false;
         document.all("inoperation").disabled= false;
         document.all("inwarninglevel").disabled= false;
         document.all("inreinstatelevel").disabled= false;
      }
      else if("outtbflag"==name)
      {  
         document.all("outtbflag").value="1";       
         document.all("ifoutoctets").disabled= false;
         document.all("outoperation").disabled= false;
         document.all("outwarninglevel").disabled= false;
         document.all("outreinstatelevel").disabled= false;
      }      
   }
   else
   {
     if("ifindiscardspps_max_checkbox"==name)
      {
         document.all("ifindiscardspps_max").disabled= true;
      }      
      else if("ifoutdiscardspps_max_checkbox"==name)
      {
         document.all("ifoutdiscardspps_max").disabled= true;
      }
      else if("ifinerrorspps_max_checkbox"==name)
      {
         document.all("ifinerrorspps_max").disabled= true;
      }
      else if("ifouterrorspps_max_checkbox"==name)
      {
         document.all("ifinerrorspps_max").disabled= true;
      }     
      else if("intbflag"==name)
      { 
         document.all("intbflag").value="0";         
         document.all("ifinoctets").disabled= true;
         document.all("inoperation").disabled= true;
         document.all("inwarninglevel").disabled= true;
         document.all("inreinstatelevel").disabled= true;
      }
      else if("outtbflag"==name)
      { 
         document.all("outtbflag").value="0";        
         document.all("ifoutoctets").disabled= true;
         document.all("outoperation").disabled= true;
         document.all("outwarninglevel").disabled= true;
         document.all("outreinstatelevel").disabled= true;
      }
     
   }
}

function goback()
{
 var page="webtop_devicePortsList.jsp?device_id=<%=device_id%>";
 this.location.href=page;
}
</SCRIPT>
<FORM NAME="frm" METHOD="post" action="webtop_devicePortModify_submit.jsp" target="childFrm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD bgcolor=#999999>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" align=center>
		  <TR>
		    <TH colspan=4  align=center>�˿���Ϣ</TH>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>�豸IP</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="device_ip" value="<%=device_ip%>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>�˿�����</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifindex" value="<%=ifindex %>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>�˿�����</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifdescr" value="<%="null".equals(ifdescr)?"":ifdescr %>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>�˿�����</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifname" value="<%= "null".equals(ifname)?"":ifname%>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>�˿ڱ���</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifnamedefined" value="<%= "null".equals(ifnamedefined)?"":ifnamedefined%>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>�˿�����</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="iftype" value="<%=iftype %>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>�˿�����(bps)</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifspeed" value="<%= ifspeed%>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>�˿�����䵥Ԫ</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifmtu" value="<%= ifmtu%>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>���ٶ˿�����(bps)</TD>
		     <TD class=column2 width=35% align=left colspan=3><input type="text" name="ifhighspeed" value="<%=ifhighspeed %>" style="size:20;maxlength:200" disabled></TD>		     
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>�Ƿ�ɼ��˿�������Ϣ</TD>
		     <TD class=column2 width=35% align=left><select name="gatherflag"><option value="1" <%="1".equals(gatherflag)?"selected":"" %>>�ɼ�</option><option value="0" <%="0".equals(gatherflag)?"selected":"" %>>���ɼ�</option></select></TD>
		     <TD class=column1 width=15% align=center>ԭʼ�����Ƿ����</TD>
		     <TD class=column2 width=35% align=left><select name="intodb"><option value="1" <%="1".equals(intodb)?"selected":"" %>>���</option><option value="0" <%="0".equals(intodb)?"selected":"" %>>�����</option></select></TD>
		  </TR>
		  </TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align=center>
			<tr>
				<TD class="column1">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" valign="middle">
					<tr>
						<TH width="159" height="25" class="green_gargtd" id="td1" onClick="location.href='javascript:showpage(1);'">�̶���ֵ�澯����</TH>
						<TH width="159" height="25" class="green_gargtd" id="td2" onClick="location.href='javascript:showpage(2);'">��̬��ֵ�澯����</TH>
						<TH width="159" height="25" class="green_gargtd" id="td3" onClick="location.href='javascript:showpage(3);'">ͻ�䷧ֵ�澯����</TH>
						<td align="left"></td>
					</tr>
					<tr>
						<td height="3" colspan="4" align="center" class="blue_tag_line"></td>
					</tr>
				</table>
				</TD>
			</tr>

			<TR>
				<TD id="test1" style="display:" bgcolor=#000000>
				<table width="100%" border="0" align="center" cellpadding="2" cellspacing="1" valign="middle">
				    <tr>
				        <TH colspan=4>������������ֵһ����</TH>
				    </tr>
				    <tr>
				        <TD class=column1 width=30% align=center>�˿�������������ֵһ�Ƚϲ�����</TD>
				        <TD class=column2 width=20% align=left><select name="ifinoct_maxtype" onchange="showDisplay('ifinoct_maxtype')">
				        <option value="0" <%=("0".equals(ifinoct_maxtype)?"selected":"") %>>��ʹ��</option>
                        <option value="1" <%=("1".equals(ifinoct_maxtype)?"selected":"") %>>����</option>
                        <option value="2" <%=("2".equals(ifinoct_maxtype)?"selected":"") %>>���ڵ���</option>
                        <option value="3" <%=("3".equals(ifinoct_maxtype)?"selected":"") %>>С��</option>
                        <option value="4" <%=("4".equals(ifinoct_maxtype)?"selected":"") %>>С�ڵ���</option>
                        <option value="5" <%=("5".equals(ifinoct_maxtype)?"selected":"") %>>����</option>
                        <option value="6" <%=("6".equals(ifinoct_maxtype)?"selected":"") %>>������</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>�˿�������������ֵһ(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifinoctetsbps_max" value="<%=ifinoctetsbps_max%>" <%=Integer.parseInt(ifinoct_maxtype)>0?"":"disabled"%>></TD>
				    </tr>	
				     <tr>
				        <TD class=column1 width=30% align=center>�˿�������������ֵһ�Ƚϲ�����</TD>
				        <TD class=column2 width=20% align=left><select name="ifoutoct_maxtype" onchange="showDisplay('ifoutoct_maxtype')">
				        <option value="0" <%=("0".equals(ifoutoct_maxtype)?"selected":"") %>>��ʹ��</option>
                        <option value="1" <%=("1".equals(ifoutoct_maxtype)?"selected":"") %>>����</option>
                        <option value="2" <%=("2".equals(ifoutoct_maxtype)?"selected":"") %>>���ڵ���</option>
                        <option value="3" <%=("3".equals(ifoutoct_maxtype)?"selected":"") %>>С��</option>
                        <option value="4" <%=("4".equals(ifoutoct_maxtype)?"selected":"") %>>С�ڵ���</option>
                        <option value="5" <%=("5".equals(ifoutoct_maxtype)?"selected":"") %>>����</option>
                        <option value="6" <%=("6".equals(ifoutoct_maxtype)?"selected":"") %>>������</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>�˿�������������ֵһ(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifoutoctetsbps_max" value="<%=ifoutoctetsbps_max%>" <%=Integer.parseInt(ifoutoct_maxtype)>0?"":"disabled"%>></TD>
				    </tr>				
					<tr>
						<TD class=column1 width=30% align=center><input type="checkbox" name="ifindiscardspps_max_checkbox" onclick="checked1('ifindiscardspps_max_checkbox')" <%=Double.parseDouble(ifindiscardspps_max)>=0?"checked":"" %>>�˿����붪������ֵ(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifindiscardspps_max" value="<%=ifindiscardspps_max%>" <%=Double.parseDouble(ifindiscardspps_max)>=0?"":"disabled" %>></TD>
		                <TD class=column1 width=30% align=center><input type="checkbox" name="ifoutdiscardspps_max_checkbox" onclick="checked1('ifoutdiscardspps_max_checkbox')" <%=Double.parseDouble(ifoutdiscardspps_max)>=0?"checked":"" %>>�˿�������������ֵ(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifoutdiscardspps_max" value="<%=ifoutdiscardspps_max %>" <%=Double.parseDouble(ifoutdiscardspps_max)>=0?"":"disabled" %>></TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center><input type="checkbox" name="ifinerrorspps_max_checkbox" onclick="checked1('ifinerrorspps_max_checkbox')" <%=Double.parseDouble(ifinerrorspps_max)>=0?"checked":"" %>>�˿�����������ֵ(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifinerrorspps_max" value="<%=ifinerrorspps_max%>" <%=Double.parseDouble(ifinerrorspps_max)>=0?"":"disabled" %>></TD>
		                <TD class=column1 width=30% align=center><input type="checkbox" name="ifouterrorspps_max_checkbox" onclick="checked1('ifouterrorspps_max_checkbox')" <%=Double.parseDouble(ifouterrorspps_max)>=0?"checked":"" %>>�˿������������ֵ(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifouterrorspps_max" value="<%=ifouterrorspps_max %>" <%=Double.parseDouble(ifouterrorspps_max)>=0?"":"disabled" %>></TD>
					</tr>
					<tr>
					   <TD class=column1 width=30% align=center>������ֵ�Ĵ��������澯��</TD>
					   <TD class=column2 width=20% align=left colspan=3><input type="text" name="warningnum" value="<%=warningnum%>"></TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>������ֵ�澯ʱ�ĸ澯����</td>
						<td class=column2 align=left><select name="warninglevel">	
							<option value="0" <%=("0".equals(warninglevel)?"selected":"") %>>����澯</option>				
							<option value="1" <%=("1".equals(warninglevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(warninglevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(warninglevel)?"selected":"")%>>һ��澯</option>
							<option value="4" <%=("4".equals(warninglevel)?"selected":"")%>>���ظ澯</option>
							<option value="5" <%=("5".equals(warninglevel)?"selected":"")%>>�����澯</option>
						</select></td>
						<td class=column1 align=center>�ָ��澯����</td>
						<td class=column2 align=left><select name="reinstatelevel">	
						    <option value="0" <%=("0".equals(reinstatelevel)?"selected":"") %>>����澯</option>	
							<option value="1" <%=("1".equals(reinstatelevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(reinstatelevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(reinstatelevel)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(reinstatelevel)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(reinstatelevel)?"selected":"") %>>�����澯</option>
						</select></td>
					</tr>
					<tr>
				        <TH colspan=4>������������ֵ������</TH>
				    </tr>
				    <tr>
				        <TD class=column1 width=30% align=center>�˿�������������ֵ���Ƚϲ�����</TD>
				        <TD class=column2 width=20% align=left><select name="ifinoct_mintype" onchange="showDisplay('ifinoct_mintype')">
				        <option value="0" <%=("0".equals(ifinoct_mintype)?"selected":"") %>>��ʹ��</option>
                        <option value="1" <%=("1".equals(ifinoct_mintype)?"selected":"") %>>����</option>
                        <option value="2" <%=("2".equals(ifinoct_mintype)?"selected":"") %>>���ڵ���</option>
                        <option value="3" <%=("3".equals(ifinoct_mintype)?"selected":"") %>>С��</option>
                        <option value="4" <%=("4".equals(ifinoct_mintype)?"selected":"") %>>С�ڵ���</option>
                        <option value="5" <%=("5".equals(ifinoct_mintype)?"selected":"") %>>����</option>
                        <option value="6" <%=("6".equals(ifinoct_mintype)?"selected":"") %>>������</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>�˿�������������ֵ��(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifinoctetsbps_min" value="<%=ifinoctetsbps_min%>" <%=Integer.parseInt(ifinoct_mintype)>0?"":"disabled"%>></TD>
				    </tr>	
				     <tr>
				        <TD class=column1 width=30% align=center>�˿�������������ֵ���Ƚϲ�����</TD>
				        <TD class=column2 width=20% align=left><select name="ifoutoct_mintype" onchange="showDisplay('ifoutoct_mintype')">
				        <option value="0" <%=("0".equals(ifoutoct_mintype)?"selected":"") %>>��ʹ��</option>
                        <option value="1" <%=("1".equals(ifoutoct_mintype)?"selected":"") %>>����</option>
                        <option value="2" <%=("2".equals(ifoutoct_mintype)?"selected":"") %>>���ڵ���</option>
                        <option value="3" <%=("3".equals(ifoutoct_mintype)?"selected":"") %>>С��</option>
                        <option value="4" <%=("4".equals(ifoutoct_mintype)?"selected":"") %>>С�ڵ���</option>
                        <option value="5" <%=("5".equals(ifoutoct_mintype)?"selected":"") %>>����</option>
                        <option value="6" <%=("6".equals(ifoutoct_mintype)?"selected":"") %>>������</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>�˿�������������ֵ��(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifoutoctetsbps_min" value="<%=ifoutoctetsbps_min%>" <%=Integer.parseInt(ifoutoct_mintype)>0?"":"disabled"%>></TD>
				    </tr>				
					<tr>
					   <TD class=column1 width=30% align=center>������ֵ���Ĵ��������澯��</TD>
					   <TD class=column2 width=20% align=left colspan=3><input type="text" name="warningnum_min" value="<%=warningnum_min%>"></TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>������ֵ���澯ʱ�ĸ澯����</td>
						<td class=column2 align=left><select name="warninglevel_min">	
						    <option value="0" <%=("0".equals(warninglevel_min)?"selected":"") %>>����澯</option>
							<option value="1" <%=("1".equals(warninglevel_min)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(warninglevel_min)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(warninglevel_min)?"selected":"")%>>һ��澯</option>
							<option value="4" <%=("4".equals(warninglevel_min)?"selected":"")%>>���ظ澯</option>
							<option value="5" <%=("5".equals(warninglevel_min)?"selected":"")%>>�����澯</option>
						</select></td>
						<td class=column1 align=center>��ֵ���ָ��澯����</td>
						<td class=column2 align=left><select name="reinlevel_min">	
						    <option value="0" <%=("0".equals(reinlevel_min)?"selected":"") %>>����澯</option>
							<option value="1" <%=("1".equals(reinlevel_min)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(reinlevel_min)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(reinlevel_min)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(reinlevel_min)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(reinlevel_min)?"selected":"") %>>�����澯</option>
						</select></td>
					</tr>
									
				</table>
				</TD>
			</TR>
			<TR>
				<TD id="test2" style="display:none" bgcolor=#000000>
				<table width="100%" border="0" cellspacing="1" cellpadding="2"
					valign="middle" align="center">
					<tr>
						<TD class=column1 align=center colspan=4>��̬��ֵ������</TD>
					</tr>
					<tr>
					   <TD class=column1 width=30% align=center>��̬��ֵһ������</TD>
					   <TD class=column2 width=20% align=left><select name="overmax" onchange="showDisplay('overmax')">
				        <option value="0" <%=("0".equals(overmax)?"selected":"") %>>��ʹ��</option>
                        <option value="1" <%=("1".equals(overmax)?"selected":"") %>>����</option>
                        <option value="2" <%=("2".equals(overmax)?"selected":"") %>>���ڵ���</option>
                        <option value="3" <%=("3".equals(overmax)?"selected":"") %>>С��</option>
                        <option value="4" <%=("4".equals(overmax)?"selected":"") %>>С�ڵ���</option>
                        <option value="5" <%=("5".equals(overmax)?"selected":"") %>>����</option>
                        <option value="6" <%=("6".equals(overmax)?"selected":"") %>>������</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>��̬��ֵһ(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="overper" value="<%=overper %>" <%=Integer.parseInt(overmax)>0?"":"disabled" %>><font color="#FF0000">(0-100%)</font></TD>
					</tr>					
					<tr>
					    <TD class=column1 width=30% align=center>������̬��ֵһ�Ĵ���(���澯)</TD>
						<TD class=column2 width=20% align=left colspan=3><input type="text" name="overnum" value="<%=overnum %>" <%=Integer.parseInt(overmax)>0?"":"disabled" %>></TD>												
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>������̬��ֵһ�澯ʱ�ĸ澯����</td>
						<td class=column2 align=left><select name="overlevel" <%=Integer.parseInt(overmax)>0?"":"disabled" %>>				<option value="0" <%=("0".equals(overlevel)?"selected":"") %>>����澯</option>			
							<option value="1" <%=("1".equals(overlevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(overlevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(overlevel)?"selected":"")%>>һ��澯</option>
							<option value="4" <%=("4".equals(overlevel)?"selected":"")%>>���ظ澯</option>
							<option value="5" <%=("5".equals(overlevel)?"selected":"")%>>�����澯</option>
						</select></td>
						<td class=column1 align=center>�����ָ��澯ʱ�ļ���</td>
						<td class=column2 align=left><select name="reinoverlevel" <%=Integer.parseInt(overmax)>0?"":"disabled" %>>			<option value="0" <%=("0".equals(reinoverlevel)?"selected":"") %>>����澯</option>					
							<option value="1" <%=("1".equals(reinoverlevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(reinoverlevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(reinoverlevel)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(reinoverlevel)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(reinoverlevel)?"selected":"") %>>�����澯</option>
						</select></td>
					</tr>	
					<tr>
						<TD class=column1 align=center colspan=4>��̬��ֵ������</TD>
					</tr>
					<tr>
					   <TD class=column1 width=30% align=center>��̬��ֵ��������</TD>
					   <TD class=column2 width=20% align=left><select name="overmin" onchange="showDisplay('overmin')">
				        <option value="0" <%=("0".equals(overmin)?"selected":"") %>>��ʹ��</option>
                        <option value="1" <%=("1".equals(overmin)?"selected":"") %>>����</option>
                        <option value="2" <%=("2".equals(overmin)?"selected":"") %>>���ڵ���</option>
                        <option value="3" <%=("3".equals(overmin)?"selected":"") %>>С��</option>
                        <option value="4" <%=("4".equals(overmin)?"selected":"") %>>С�ڵ���</option>
                        <option value="5" <%=("5".equals(overmin)?"selected":"") %>>����</option>
                        <option value="6" <%=("6".equals(overmin)?"selected":"") %>>������</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>��̬��ֵ��(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="overper_min" value="<%=overper_min %>" <%=Integer.parseInt(overmin)>0?"":"disabled" %>><font color="#FF0000">(0-100%)</font></TD>
					</tr>					
					<tr>
					    <TD class=column1 width=30% align=center>������̬��ֵ������(���澯)</TD>
						<TD class=column2 width=20% align=left colspan=3><input type="text" name="overnum_min" value="<%=overnum_min %>" <%=Integer.parseInt(overmin)>0?"":"disabled" %>></TD>						
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>������̬��ֵ���澯ʱ�ĸ澯����</td>
						<td class=column2 align=left><select name="overlevel_min" <%=Integer.parseInt(overmin)>0?"":"disabled" %>>			<option value="0" <%=("0".equals(overlevel_min)?"selected":"") %>>����澯</option>					
							<option value="1" <%=("1".equals(overlevel_min)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(overlevel_min)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(overlevel_min)?"selected":"")%>>һ��澯</option>
							<option value="4" <%=("4".equals(overlevel_min)?"selected":"")%>>���ظ澯</option>
							<option value="5" <%=("5".equals(overlevel_min)?"selected":"")%>>�����澯</option>
						</select></td>
						<td class=column1 align=center>�����ָ��澯ʱ�ļ���</td>
						<td class=column2 align=left><select name="reinoverlevel_min" <%=Integer.parseInt(overmin)>0?"":"disabled" %>>		<option value="0" <%=("0".equals(reinoverlevel_min)?"selected":"") %>>����澯</option>					
							<option value="1" <%=("1".equals(reinoverlevel_min)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(reinoverlevel_min)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(reinoverlevel_min)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(reinoverlevel_min)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(reinoverlevel_min)?"selected":"") %>>�����澯</option>
						</select></td>
					</tr>
					<tr>
					    <TD class=column1 width=30% align=center>���ɶ�̬��ֵһ������(��)</TD>
						<TD class=column2 width=20% align=left colspan=3><input type="text" name="com_day" value="<%= com_day%>"><font color="#FF0000">(������Ϊ�Ƚϱ�׼��ֵһ����ֵ�������������Ϊ3��)</font></TD>
					</tr>				
				</table>
				</TD>
			</TR>
			<TR>
				<TD id="test3" style="display:none" bgcolor=#000000>
				<table width="100%" border="0" cellspacing="1" cellpadding="2"
					valign="middle" align="center">
					<tr>
						<TD class=column1 width=30% align=center colspan=4><input type="checkbox" name="intbflag" <%="1".equals(intbflag)?"checked":"" %>  value= <%="1".equals(intbflag)?"1":"0" %> onclick="checked1('intbflag')">������������ͻ��澯����</TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>�������ʱ仯����ֵ(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="ifinoctets" value="<%=ifinoctets %>" <%="1".equals(intbflag)?"":"disabled"%>></TD>
						<TD class=column1 width=30% align=center>��������ͻ��澯������</TD>
						<TD class=column2 width=20% align=left><select name="inoperation" <%="1".equals(intbflag)?"":"disabled"%>>
						     <option value="1" <%="1".equals(inoperation)?"selected":"" %>>></option>
						     <option value="2" <%="2".equals(inoperation)?"selected":"" %>><</option>
						     <option value="3" <%="3".equals(inoperation)?"selected":"" %>>=</option>
						</select></TD>						 
					</tr>					
					<tr>
						<TD class=column1 width=30% align=center>��������ͻ��澯����</td>
						<TD class=column2 width=20% align=left><select name="inwarninglevel" <%="1".equals(intbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(inwarninglevel)?"selected":"") %>>����澯</option>
							<option value="1" <%=("1".equals(inwarninglevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(inwarninglevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(inwarninglevel)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(inwarninglevel)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(inwarninglevel)?"selected":"") %>>�����澯</option>
						</select></td>
						<TD class=column1 width=30% align=center>�������ʻָ�ͻ��澯����</TD>
						<TD class=column2 width=20% align=left><select name="inreinstatelevel" <%="1".equals(intbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(inreinstatelevel)?"selected":"") %>>����澯</option>
						    <option value="1" <%=("1".equals(inreinstatelevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(inreinstatelevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(inreinstatelevel)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(inreinstatelevel)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(inreinstatelevel)?"selected":"") %>>�����澯</option>
						</select></td>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center colspan=4><input type="checkbox" name="outtbflag" <%="1".equals(outtbflag)?"checked":"" %> value= <%="1".equals(outtbflag)?"1":"0" %>  onclick="checked1('outtbflag')">������������ͻ��澯����</TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>�������ʱ仯����ֵ(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="ifoutoctets" value="<%=ifinoctets %>" <%="1".equals(outtbflag)?"":"disabled"%>></TD>
						<TD class=column1 width=30% align=center>��������ͻ��澯������</TD>
						<TD class=column2 width=20% align=left><select name="outoperation" <%="1".equals(outtbflag)?"":"disabled"%>>
						     <option value="1" <%="1".equals(outoperation)?"selected":"" %>>></option>
						     <option value="2" <%="2".equals(outoperation)?"selected":"" %>><</option>
						     <option value="3" <%="3".equals(outoperation)?"selected":"" %>>=</option>
						</select></TD>						 
					</tr>					
					<tr>
						<TD class=column1 width=30% align=center>��������ͻ��澯����</td>
						<TD class=column2 width=20% align=left><select name="outwarninglevel" <%="1".equals(outtbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(outwarninglevel)?"selected":"") %>>����澯</option>
							<option value="1" <%=("1".equals(outwarninglevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(outwarninglevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(outwarninglevel)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(outwarninglevel)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(outwarninglevel)?"selected":"") %>>�����澯</option>
						</select></td>
						<TD class=column1 width=30% align=center>�������ʻָ�ͻ��澯����</TD>
						<TD class=column2 width=20% align=left><select name="outreinstatelevel" <%="1".equals(outtbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(outreinstatelevel)?"selected":"") %>>����澯</option>
						    <option value="1" <%=("1".equals(outreinstatelevel)?"selected":"") %>>������־</option>
							<option value="2" <%=("2".equals(outreinstatelevel)?"selected":"") %>>��ʾ�澯</option>
							<option value="3" <%=("3".equals(outreinstatelevel)?"selected":"") %>>һ��澯</option>
							<option value="4" <%=("4".equals(outreinstatelevel)?"selected":"") %>>���ظ澯</option>
							<option value="5" <%=("5".equals(outreinstatelevel)?"selected":"") %>>�����澯</option>
						</select></td>
					</tr>					
				</table>

				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
	 <td align=right>
	 <input type="hidden" name="device_id" value="<%=device_id %>">
	 <input type="hidden" name="port_info" value="<%=port_info%>">	
	 <input type="button" value=" �� �� " class="jianbian" name="save" onclick="javascript:CheckForm();">
	 <input type="button" value="���ض˿��б� " class="jianbian" name="close" onclick="javascript:goback();">
	 <input type="button" value=" �� �� " class="jianbian" name="close" onclick="javascript:window.close();">
	 </td>
	</tr>
</TABLE>
</form>
<IFRAME id="childFrm" name="childFrm" SRC="" STYLE="display:none;width:500;height:500"></IFRAME>



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
//begin modifided by w5221 带宽利用率、动态阈值那边增加阈值下限
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
//阈值二
String ifinoct_mintype="0";
String ifinoctetsbps_min="-1";
String ifoutoct_mintype="0";
String ifoutoctetsbps_min="0";
String warningnum_min="3";
String warninglevel_min="3";
String reinlevel_min="1";

//动态阈值
String overmax="0";
String overper="-1";
String overnum="3";
String overlevel="3";
String reinoverlevel="1";
//动态阈值二
String overmin="0";
String overper_min="-1";
String overnum_min="3";
String overlevel_min="3";
String reinoverlevel_min="1";
String com_day="3";
//突变阈值
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
//end modifided by w5221 带宽利用率、动态阈值那边增加阈值下限

String device_ip = request.getParameter("device_ip");
String device_id = request.getParameter("device_id");
String[] ports = request.getParameterValues("port");

String port_info = null;
//只有选中一个设备的时候需要把下面的值给填入
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
		//固定阈值一
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
        //固定阈值二
        ifinoct_mintype=(String)portInfo.get("ifinoct_mintype");
        ifinoctetsbps_min=(String)portInfo.get("ifinoctetsbps_min");
        ifoutoct_mintype=(String)portInfo.get("ifoutoct_mintype");
        ifoutoctetsbps_min=(String)portInfo.get("ifoutoctetsbps_min");
        warningnum_min=(String)portInfo.get("warningnum_min");
		warninglevel_min=(String)portInfo.get("warninglevel_min");
		reinlevel_min=(String)portInfo.get("reinlevel_min");
        //动态阈值一
		overmax=(String)portInfo.get("overmax");
		overper=(String)portInfo.get("overper");
		overnum=(String)portInfo.get("overnum");
		overlevel=(String)portInfo.get("overlevel");
		reinoverlevel=(String)portInfo.get("reinoverlevel");
		//动态阈值二
		overmin=(String)portInfo.get("overmin");
		overper_min=(String)portInfo.get("overper_min");
		overnum_min=(String)portInfo.get("overnum_min");
		overlevel_min=(String)portInfo.get("overlevel_min");
		reinoverlevel_min=(String)portInfo.get("reinoverlevel_min");
		com_day=(String)portInfo.get("com_day");		
		//突变阈值
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
//操作符变化js
function showDisplay(name)
{
	//改变流入带宽利用率阈值一操作符
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
	//改变流出带宽利用率阈值一操作符
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
	//改变流入带宽利用率阈值二操作符
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
	//改变流出带宽利用率阈值二操作符
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
	//动态阈值一操作符
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
	//动态阈值二操作符
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
//检查页面数据是否正确
function CheckForm()
{
  //设置了流入带宽利用率阈值一
  var ifinoct_maxtype = document.all("ifinoct_maxtype").value;
  if(ifinoct_maxtype>0)
  {
    if(!IsNull(document.all("ifinoctetsbps_max").value,"端口流入带宽利用率阈值一空")
         ||!isFloatNumber(document.all("ifinoctetsbps_max").value,"端口流入带宽利用率阈值一不是数字"))
     {       
        return false;
     }
     else if(document.all("ifinoctetsbps_max").value>100)
     {
        alert("端口流入带宽利用率阈值一超过100");
        return false;
     }     
  }

  //设置了流出带宽利用率阈值二
  var ifoutoct_maxtype = document.all("ifoutoct_maxtype").value;
  if(ifoutoct_maxtype>0)
  {
	  if(!IsNull(document.all("ifoutoctetsbps_max").value,"端口流出带宽利用率阈值一空")
         ||!isFloatNumber(document.all("ifoutoctetsbps_max").value,"端口流出带宽利用率阈值一不是数字"))
     {
        return false;
     }
     else if(document.all("ifoutoctetsbps_max").value>100)
     {
        alert("端口流出带宽利用率阈值一超过100");
        return false;
     }     
  }  
  
  //选中端口流入丢包率阈值
  if(document.all("ifindiscardspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifindiscardspps_max").value,"端口流入丢包率阈值空")
         ||!isFloatNumber(document.all("ifindiscardspps_max").value,"端口流入丢包率阈值不是数字"))
     {
        return false;
     }
     else if(document.all("ifindiscardspps_max").value>100)
     {
        alert("端口流入丢包率阈值超过100");
        return false;
     }
  }
  
  
   //选中端口流出丢包率阈值
  if(document.all("ifoutdiscardspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifoutdiscardspps_max").value,"端口流出丢包率阈值空")
         ||!isFloatNumber(document.all("ifoutdiscardspps_max").value,"端口流出丢包率阈值不是数字"))
     {
        return false;
     }
     else if(document.all("ifoutdiscardspps_max").value>100)
     {
        alert("端口流出丢包率阈值超过100");
        return false;
     }
  }
  
  
   //选中端口流入错包率阈值
  if(document.all("ifinerrorspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifinerrorspps_max").value,"端口流入错包率阈值空")
         ||!isFloatNumber(document.all("ifinerrorspps_max").value,"端口流入错包率阈值不是数字"))
     {
        return false;
     }
     else if(document.all("ifinerrorspps_max").value>100)
     {
        alert("端口流入错包率阈值超过100");
        return false;
     }
  }
  
   //选中端口流出错包率阈值
  if(document.all("ifouterrorspps_max_checkbox").checked)
  {
     if(!IsNull(document.all("ifouterrorspps_max").value,"端口流入错包率阈值空")
         ||!isFloatNumber(document.all("ifouterrorspps_max").value,"端口流入错包率阈值不是数字"))
     {
        return false;
     }
     else if(document.all("ifouterrorspps_max").value>100)
     {
        alert("端口流出错包率阈值超过100");
        return false;
     }
  }
  
  
  if(!IsNull(document.all("warningnum").value,"超出阈值一的次数为空")
        ||!IsNumber(document.all("warningnum").value,"超出阈值一的次数不是数字"))
   {
       return false;
   }

   //设置流入带宽利用率阈值二
   var ifinoct_mintype = document.all("ifinoct_mintype").value;
   if(ifinoct_mintype>0)
   {
	  if(!IsNull(document.all("ifinoctetsbps_min").value,"端口流入带宽利用率阈值二空")
         ||!isFloatNumber(document.all("ifinoctetsbps_min").value,"端口流入带宽利用率阈值二不是数字"))
     {       
        return false;
     }
     else if(document.all("ifinoctetsbps_min").value>100)
     {
        alert("端口流入带宽利用率阈值二超过100");
        return false;
     }
   }

   //设置流出带宽利用率阈值二
   var ifoutoct_mintype = document.all("ifoutoct_mintype").value;
   if(ifoutoct_mintype>0)
   {
	  if(!IsNull(document.all("ifoutoctetsbps_min").value,"端口流出带宽利用率阈值二空")
         ||!isFloatNumber(document.all("ifoutoctetsbps_min").value,"端口流出带宽利用率阈值二不是数字"))
     {       
        return false;
     }
     else if(document.all("ifoutoctetsbps_min").value>100)
     {
        alert("端口流出带宽利用率阈值二超过100");
        return false;
     }
   }

    if(!IsNull(document.all("warningnum_min").value,"超出阈值二的次数为空")
        ||!IsNumber(document.all("warningnum_min").value,"超出阈值二的次数不是数字"))
   {
       return false;
   }

   //动态阈值一设置
   var overmax = document.all("overmax").value;
   if(overmax>0)
   {
	 if(!IsNull(document.all("overper").value,"动态阈值一百分比为空")
          ||!IsNumber(document.all("overper").value,"动态阈值一百分比不是数字"))
     {
        return false;
     }
     else
     {
       if(document.all("overper").value>100)
       {
          alert("动态阈值一百分比不能超过100");
          return false;
       }
     }
     
     if(!IsNull(document.all("overnum").value,"超出动态阈值一的次数为空")
          ||!IsNumber(document.all("overnum").value,"超出动态阈值一的次数不是数字"))
     {
        return false;
     }

	 if(!IsNull(document.all("com_day").value,"生成动态阈值天数为空")
          ||!IsNumber(document.all("com_day").value,"生成动态阈值天数不是数字"))
     {
        return false;
     }
     else
     {
       if(document.all("com_day").value>3)
       {
          alert("生成动态阈值天数不能超过3天");
          return false;
       }
     }
   }
   
   //动态阈值二设置
   var overmin = document.all("overmin").value;
   if(overmin>0)
   {
	    if(!IsNull(document.all("overper_min").value,"动态阈值二百分比为空")
          ||!IsNumber(document.all("overper_min").value,"动态阈值二百分比不是数字"))
     {
        return false;
     }
     else
     {
       if(document.all("overper_min").value>100)
       {
          alert("动态阈值二百分比不能超过100");
          return false;
       }
     }
     
     if(!IsNull(document.all("overnum_min").value,"超出动态阈值二的次数为空")
          ||!IsNumber(document.all("overnum_min").value,"超出动态阈值二的次数不是数字"))
     {
        return false;
     }

	 if(!IsNull(document.all("com_day").value,"生成动态阈值天数为空")
          ||!IsNumber(document.all("com_day").value,"生成动态阈值天数不是数字"))
     {
        return false;
     }
     else
     {
       if(document.all("com_day").value>3)
       {
          alert("生成动态阈值天数不能超过3天");
          return false;
       }
     }
   }
   
   //突变阈值
   if(document.all("intbflag").checked)
   {
      if(!IsNull(document.all("ifinoctets").value,"流入速率变化率阈值为空")
          ||!isFloatNumber(document.all("ifinoctets").value,"流入速率变化率阈值不是数字"))
      {
         return false;
      }
      else if(document.all("ifinoctets").value>100||document.all("ifinoctets").value<0)
      {
        alert("流入速率变化率阈值必须是大于0，小于100的数字");
        return false;
      }    
   }
   
   if(document.all("outtbflag").checked)
   {
      if(!IsNull(document.all("ifoutoctets").value,"流出速率变化率阈值为空")
          ||!isFloatNumber(document.all("ifoutoctets").value,"流出速率变化率阈值不是数字"))
      {
         return false;
      }
      else if(document.all("ifoutoctets").value>100||document.all("ifoutoctets").value<0)
      {
        alert("流出速率变化率阈值必须是大于0，小于100的数字");
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
		    <TH colspan=4  align=center>端口信息</TH>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>设备IP</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="device_ip" value="<%=device_ip%>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>端口索引</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifindex" value="<%=ifindex %>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>端口描述</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifdescr" value="<%="null".equals(ifdescr)?"":ifdescr %>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>端口名字</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifname" value="<%= "null".equals(ifname)?"":ifname%>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>端口别名</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifnamedefined" value="<%= "null".equals(ifnamedefined)?"":ifnamedefined%>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>端口类型</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="iftype" value="<%=iftype %>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>端口速率(bps)</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifspeed" value="<%= ifspeed%>" style="size:20;maxlength:200" disabled></TD>
		     <TD class=column1 width=15% align=center>端口最大传输单元</TD>
		     <TD class=column2 width=35% align=left><input type="text" name="ifmtu" value="<%= ifmtu%>"  style="size:20;maxlength:200" disabled></TD>
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>高速端口速率(bps)</TD>
		     <TD class=column2 width=35% align=left colspan=3><input type="text" name="ifhighspeed" value="<%=ifhighspeed %>" style="size:20;maxlength:200" disabled></TD>		     
		  </TR>
		  <TR>
		     <TD class=column1 width=15% align=center>是否采集端口流量信息</TD>
		     <TD class=column2 width=35% align=left><select name="gatherflag"><option value="1" <%="1".equals(gatherflag)?"selected":"" %>>采集</option><option value="0" <%="0".equals(gatherflag)?"selected":"" %>>不采集</option></select></TD>
		     <TD class=column1 width=15% align=center>原始数据是否入库</TD>
		     <TD class=column2 width=35% align=left><select name="intodb"><option value="1" <%="1".equals(intodb)?"selected":"" %>>入库</option><option value="0" <%="0".equals(intodb)?"selected":"" %>>不入库</option></select></TD>
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
						<TH width="159" height="25" class="green_gargtd" id="td1" onClick="location.href='javascript:showpage(1);'">固定阀值告警配置</TH>
						<TH width="159" height="25" class="green_gargtd" id="td2" onClick="location.href='javascript:showpage(2);'">动态阀值告警配置</TH>
						<TH width="159" height="25" class="green_gargtd" id="td3" onClick="location.href='javascript:showpage(3);'">突变阀值告警配置</TH>
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
				        <TH colspan=4>带宽利用率阈值一设置</TH>
				    </tr>
				    <tr>
				        <TD class=column1 width=30% align=center>端口流入利用率阈值一比较操作符</TD>
				        <TD class=column2 width=20% align=left><select name="ifinoct_maxtype" onchange="showDisplay('ifinoct_maxtype')">
				        <option value="0" <%=("0".equals(ifinoct_maxtype)?"selected":"") %>>不使用</option>
                        <option value="1" <%=("1".equals(ifinoct_maxtype)?"selected":"") %>>大于</option>
                        <option value="2" <%=("2".equals(ifinoct_maxtype)?"selected":"") %>>大于等于</option>
                        <option value="3" <%=("3".equals(ifinoct_maxtype)?"selected":"") %>>小于</option>
                        <option value="4" <%=("4".equals(ifinoct_maxtype)?"selected":"") %>>小于等于</option>
                        <option value="5" <%=("5".equals(ifinoct_maxtype)?"selected":"") %>>等于</option>
                        <option value="6" <%=("6".equals(ifinoct_maxtype)?"selected":"") %>>不等于</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>端口流入利用率阈值一(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifinoctetsbps_max" value="<%=ifinoctetsbps_max%>" <%=Integer.parseInt(ifinoct_maxtype)>0?"":"disabled"%>></TD>
				    </tr>	
				     <tr>
				        <TD class=column1 width=30% align=center>端口流出利用率阈值一比较操作符</TD>
				        <TD class=column2 width=20% align=left><select name="ifoutoct_maxtype" onchange="showDisplay('ifoutoct_maxtype')">
				        <option value="0" <%=("0".equals(ifoutoct_maxtype)?"selected":"") %>>不使用</option>
                        <option value="1" <%=("1".equals(ifoutoct_maxtype)?"selected":"") %>>大于</option>
                        <option value="2" <%=("2".equals(ifoutoct_maxtype)?"selected":"") %>>大于等于</option>
                        <option value="3" <%=("3".equals(ifoutoct_maxtype)?"selected":"") %>>小于</option>
                        <option value="4" <%=("4".equals(ifoutoct_maxtype)?"selected":"") %>>小于等于</option>
                        <option value="5" <%=("5".equals(ifoutoct_maxtype)?"selected":"") %>>等于</option>
                        <option value="6" <%=("6".equals(ifoutoct_maxtype)?"selected":"") %>>不等于</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>端口流出利用率阈值一(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifoutoctetsbps_max" value="<%=ifoutoctetsbps_max%>" <%=Integer.parseInt(ifoutoct_maxtype)>0?"":"disabled"%>></TD>
				    </tr>				
					<tr>
						<TD class=column1 width=30% align=center><input type="checkbox" name="ifindiscardspps_max_checkbox" onclick="checked1('ifindiscardspps_max_checkbox')" <%=Double.parseDouble(ifindiscardspps_max)>=0?"checked":"" %>>端口流入丢包率阈值(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifindiscardspps_max" value="<%=ifindiscardspps_max%>" <%=Double.parseDouble(ifindiscardspps_max)>=0?"":"disabled" %>></TD>
		                <TD class=column1 width=30% align=center><input type="checkbox" name="ifoutdiscardspps_max_checkbox" onclick="checked1('ifoutdiscardspps_max_checkbox')" <%=Double.parseDouble(ifoutdiscardspps_max)>=0?"checked":"" %>>端口流出丢包率阈值(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifoutdiscardspps_max" value="<%=ifoutdiscardspps_max %>" <%=Double.parseDouble(ifoutdiscardspps_max)>=0?"":"disabled" %>></TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center><input type="checkbox" name="ifinerrorspps_max_checkbox" onclick="checked1('ifinerrorspps_max_checkbox')" <%=Double.parseDouble(ifinerrorspps_max)>=0?"checked":"" %>>端口流入错包率阈值(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifinerrorspps_max" value="<%=ifinerrorspps_max%>" <%=Double.parseDouble(ifinerrorspps_max)>=0?"":"disabled" %>></TD>
		                <TD class=column1 width=30% align=center><input type="checkbox" name="ifouterrorspps_max_checkbox" onclick="checked1('ifouterrorspps_max_checkbox')" <%=Double.parseDouble(ifouterrorspps_max)>=0?"checked":"" %>>端口流出错包率阈值(%)</TD>
		                <TD class=column2 width=20% align=left><input type="text" name="ifouterrorspps_max" value="<%=ifouterrorspps_max %>" <%=Double.parseDouble(ifouterrorspps_max)>=0?"":"disabled" %>></TD>
					</tr>
					<tr>
					   <TD class=column1 width=30% align=center>超出阈值的次数（发告警）</TD>
					   <TD class=column2 width=20% align=left colspan=3><input type="text" name="warningnum" value="<%=warningnum%>"></TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>发出阈值告警时的告警级别</td>
						<td class=column2 align=left><select name="warninglevel">	
							<option value="0" <%=("0".equals(warninglevel)?"selected":"") %>>清除告警</option>				
							<option value="1" <%=("1".equals(warninglevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(warninglevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(warninglevel)?"selected":"")%>>一般告警</option>
							<option value="4" <%=("4".equals(warninglevel)?"selected":"")%>>严重告警</option>
							<option value="5" <%=("5".equals(warninglevel)?"selected":"")%>>紧急告警</option>
						</select></td>
						<td class=column1 align=center>恢复告警级别</td>
						<td class=column2 align=left><select name="reinstatelevel">	
						    <option value="0" <%=("0".equals(reinstatelevel)?"selected":"") %>>清除告警</option>	
							<option value="1" <%=("1".equals(reinstatelevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(reinstatelevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(reinstatelevel)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(reinstatelevel)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(reinstatelevel)?"selected":"") %>>紧急告警</option>
						</select></td>
					</tr>
					<tr>
				        <TH colspan=4>带宽利用率阈值二设置</TH>
				    </tr>
				    <tr>
				        <TD class=column1 width=30% align=center>端口流入利用率阈值二比较操作符</TD>
				        <TD class=column2 width=20% align=left><select name="ifinoct_mintype" onchange="showDisplay('ifinoct_mintype')">
				        <option value="0" <%=("0".equals(ifinoct_mintype)?"selected":"") %>>不使用</option>
                        <option value="1" <%=("1".equals(ifinoct_mintype)?"selected":"") %>>大于</option>
                        <option value="2" <%=("2".equals(ifinoct_mintype)?"selected":"") %>>大于等于</option>
                        <option value="3" <%=("3".equals(ifinoct_mintype)?"selected":"") %>>小于</option>
                        <option value="4" <%=("4".equals(ifinoct_mintype)?"selected":"") %>>小于等于</option>
                        <option value="5" <%=("5".equals(ifinoct_mintype)?"selected":"") %>>等于</option>
                        <option value="6" <%=("6".equals(ifinoct_mintype)?"selected":"") %>>不等于</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>端口流入利用率阈值二(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifinoctetsbps_min" value="<%=ifinoctetsbps_min%>" <%=Integer.parseInt(ifinoct_mintype)>0?"":"disabled"%>></TD>
				    </tr>	
				     <tr>
				        <TD class=column1 width=30% align=center>端口流出利用率阈值二比较操作符</TD>
				        <TD class=column2 width=20% align=left><select name="ifoutoct_mintype" onchange="showDisplay('ifoutoct_mintype')">
				        <option value="0" <%=("0".equals(ifoutoct_mintype)?"selected":"") %>>不使用</option>
                        <option value="1" <%=("1".equals(ifoutoct_mintype)?"selected":"") %>>大于</option>
                        <option value="2" <%=("2".equals(ifoutoct_mintype)?"selected":"") %>>大于等于</option>
                        <option value="3" <%=("3".equals(ifoutoct_mintype)?"selected":"") %>>小于</option>
                        <option value="4" <%=("4".equals(ifoutoct_mintype)?"selected":"") %>>小于等于</option>
                        <option value="5" <%=("5".equals(ifoutoct_mintype)?"selected":"") %>>等于</option>
                        <option value="6" <%=("6".equals(ifoutoct_mintype)?"selected":"") %>>不等于</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>端口流出利用率阈值二(%)</TD>
				        <TD class=column2 width=20% align=left><input type="text" name="ifoutoctetsbps_min" value="<%=ifoutoctetsbps_min%>" <%=Integer.parseInt(ifoutoct_mintype)>0?"":"disabled"%>></TD>
				    </tr>				
					<tr>
					   <TD class=column1 width=30% align=center>超出阈值二的次数（发告警）</TD>
					   <TD class=column2 width=20% align=left colspan=3><input type="text" name="warningnum_min" value="<%=warningnum_min%>"></TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>发出阈值二告警时的告警级别</td>
						<td class=column2 align=left><select name="warninglevel_min">	
						    <option value="0" <%=("0".equals(warninglevel_min)?"selected":"") %>>清除告警</option>
							<option value="1" <%=("1".equals(warninglevel_min)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(warninglevel_min)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(warninglevel_min)?"selected":"")%>>一般告警</option>
							<option value="4" <%=("4".equals(warninglevel_min)?"selected":"")%>>严重告警</option>
							<option value="5" <%=("5".equals(warninglevel_min)?"selected":"")%>>紧急告警</option>
						</select></td>
						<td class=column1 align=center>阈值二恢复告警级别</td>
						<td class=column2 align=left><select name="reinlevel_min">	
						    <option value="0" <%=("0".equals(reinlevel_min)?"selected":"") %>>清除告警</option>
							<option value="1" <%=("1".equals(reinlevel_min)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(reinlevel_min)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(reinlevel_min)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(reinlevel_min)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(reinlevel_min)?"selected":"") %>>紧急告警</option>
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
						<TD class=column1 align=center colspan=4>动态阈值－设置</TD>
					</tr>
					<tr>
					   <TD class=column1 width=30% align=center>动态阈值一操作符</TD>
					   <TD class=column2 width=20% align=left><select name="overmax" onchange="showDisplay('overmax')">
				        <option value="0" <%=("0".equals(overmax)?"selected":"") %>>不使用</option>
                        <option value="1" <%=("1".equals(overmax)?"selected":"") %>>大于</option>
                        <option value="2" <%=("2".equals(overmax)?"selected":"") %>>大于等于</option>
                        <option value="3" <%=("3".equals(overmax)?"selected":"") %>>小于</option>
                        <option value="4" <%=("4".equals(overmax)?"selected":"") %>>小于等于</option>
                        <option value="5" <%=("5".equals(overmax)?"selected":"") %>>等于</option>
                        <option value="6" <%=("6".equals(overmax)?"selected":"") %>>不等于</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>动态阈值一(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="overper" value="<%=overper %>" <%=Integer.parseInt(overmax)>0?"":"disabled" %>><font color="#FF0000">(0-100%)</font></TD>
					</tr>					
					<tr>
					    <TD class=column1 width=30% align=center>超出动态阈值一的次数(发告警)</TD>
						<TD class=column2 width=20% align=left colspan=3><input type="text" name="overnum" value="<%=overnum %>" <%=Integer.parseInt(overmax)>0?"":"disabled" %>></TD>												
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>发出动态阈值一告警时的告警级别</td>
						<td class=column2 align=left><select name="overlevel" <%=Integer.parseInt(overmax)>0?"":"disabled" %>>				<option value="0" <%=("0".equals(overlevel)?"selected":"") %>>清除告警</option>			
							<option value="1" <%=("1".equals(overlevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(overlevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(overlevel)?"selected":"")%>>一般告警</option>
							<option value="4" <%=("4".equals(overlevel)?"selected":"")%>>严重告警</option>
							<option value="5" <%=("5".equals(overlevel)?"selected":"")%>>紧急告警</option>
						</select></td>
						<td class=column1 align=center>发出恢复告警时的级别</td>
						<td class=column2 align=left><select name="reinoverlevel" <%=Integer.parseInt(overmax)>0?"":"disabled" %>>			<option value="0" <%=("0".equals(reinoverlevel)?"selected":"") %>>清除告警</option>					
							<option value="1" <%=("1".equals(reinoverlevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(reinoverlevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(reinoverlevel)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(reinoverlevel)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(reinoverlevel)?"selected":"") %>>紧急告警</option>
						</select></td>
					</tr>	
					<tr>
						<TD class=column1 align=center colspan=4>动态阈值二设置</TD>
					</tr>
					<tr>
					   <TD class=column1 width=30% align=center>动态阈值二操作符</TD>
					   <TD class=column2 width=20% align=left><select name="overmin" onchange="showDisplay('overmin')">
				        <option value="0" <%=("0".equals(overmin)?"selected":"") %>>不使用</option>
                        <option value="1" <%=("1".equals(overmin)?"selected":"") %>>大于</option>
                        <option value="2" <%=("2".equals(overmin)?"selected":"") %>>大于等于</option>
                        <option value="3" <%=("3".equals(overmin)?"selected":"") %>>小于</option>
                        <option value="4" <%=("4".equals(overmin)?"selected":"") %>>小于等于</option>
                        <option value="5" <%=("5".equals(overmin)?"selected":"") %>>等于</option>
                        <option value="6" <%=("6".equals(overmin)?"selected":"") %>>不等于</option>				        
				        </select></TD>
				        <TD class=column1 width=30% align=center>动态阈值二(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="overper_min" value="<%=overper_min %>" <%=Integer.parseInt(overmin)>0?"":"disabled" %>><font color="#FF0000">(0-100%)</font></TD>
					</tr>					
					<tr>
					    <TD class=column1 width=30% align=center>超出动态阈值二次数(发告警)</TD>
						<TD class=column2 width=20% align=left colspan=3><input type="text" name="overnum_min" value="<%=overnum_min %>" <%=Integer.parseInt(overmin)>0?"":"disabled" %>></TD>						
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>发出动态阈值二告警时的告警级别</td>
						<td class=column2 align=left><select name="overlevel_min" <%=Integer.parseInt(overmin)>0?"":"disabled" %>>			<option value="0" <%=("0".equals(overlevel_min)?"selected":"") %>>清除告警</option>					
							<option value="1" <%=("1".equals(overlevel_min)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(overlevel_min)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(overlevel_min)?"selected":"")%>>一般告警</option>
							<option value="4" <%=("4".equals(overlevel_min)?"selected":"")%>>严重告警</option>
							<option value="5" <%=("5".equals(overlevel_min)?"selected":"")%>>紧急告警</option>
						</select></td>
						<td class=column1 align=center>发出恢复告警时的级别</td>
						<td class=column2 align=left><select name="reinoverlevel_min" <%=Integer.parseInt(overmin)>0?"":"disabled" %>>		<option value="0" <%=("0".equals(reinoverlevel_min)?"selected":"") %>>清除告警</option>					
							<option value="1" <%=("1".equals(reinoverlevel_min)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(reinoverlevel_min)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(reinoverlevel_min)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(reinoverlevel_min)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(reinoverlevel_min)?"selected":"") %>>紧急告警</option>
						</select></td>
					</tr>
					<tr>
					    <TD class=column1 width=30% align=center>生成动态阈值一的天数(天)</TD>
						<TD class=column2 width=20% align=left colspan=3><input type="text" name="com_day" value="<%= com_day%>"><font color="#FF0000">(用来做为比较标准阈值一、阈值二的天数，最大为3天)</font></TD>
					</tr>				
				</table>
				</TD>
			</TR>
			<TR>
				<TD id="test3" style="display:none" bgcolor=#000000>
				<table width="100%" border="0" cellspacing="1" cellpadding="2"
					valign="middle" align="center">
					<tr>
						<TD class=column1 width=30% align=center colspan=4><input type="checkbox" name="intbflag" <%="1".equals(intbflag)?"checked":"" %>  value= <%="1".equals(intbflag)?"1":"0" %> onclick="checked1('intbflag')">启用流入流量突变告警配置</TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>流入速率变化率阈值(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="ifinoctets" value="<%=ifinoctets %>" <%="1".equals(intbflag)?"":"disabled"%>></TD>
						<TD class=column1 width=30% align=center>流入速率突变告警操作符</TD>
						<TD class=column2 width=20% align=left><select name="inoperation" <%="1".equals(intbflag)?"":"disabled"%>>
						     <option value="1" <%="1".equals(inoperation)?"selected":"" %>>></option>
						     <option value="2" <%="2".equals(inoperation)?"selected":"" %>><</option>
						     <option value="3" <%="3".equals(inoperation)?"selected":"" %>>=</option>
						</select></TD>						 
					</tr>					
					<tr>
						<TD class=column1 width=30% align=center>流入速率突变告警级别</td>
						<TD class=column2 width=20% align=left><select name="inwarninglevel" <%="1".equals(intbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(inwarninglevel)?"selected":"") %>>清除告警</option>
							<option value="1" <%=("1".equals(inwarninglevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(inwarninglevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(inwarninglevel)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(inwarninglevel)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(inwarninglevel)?"selected":"") %>>紧急告警</option>
						</select></td>
						<TD class=column1 width=30% align=center>流入速率恢复突变告警级别</TD>
						<TD class=column2 width=20% align=left><select name="inreinstatelevel" <%="1".equals(intbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(inreinstatelevel)?"selected":"") %>>清除告警</option>
						    <option value="1" <%=("1".equals(inreinstatelevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(inreinstatelevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(inreinstatelevel)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(inreinstatelevel)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(inreinstatelevel)?"selected":"") %>>紧急告警</option>
						</select></td>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center colspan=4><input type="checkbox" name="outtbflag" <%="1".equals(outtbflag)?"checked":"" %> value= <%="1".equals(outtbflag)?"1":"0" %>  onclick="checked1('outtbflag')">启用流出流量突变告警配置</TD>
					</tr>
					<tr>
						<TD class=column1 width=30% align=center>流出速率变化率阈值(%)</TD>
						<TD class=column2 width=20% align=left><input type="text" name="ifoutoctets" value="<%=ifinoctets %>" <%="1".equals(outtbflag)?"":"disabled"%>></TD>
						<TD class=column1 width=30% align=center>流出速率突变告警操作符</TD>
						<TD class=column2 width=20% align=left><select name="outoperation" <%="1".equals(outtbflag)?"":"disabled"%>>
						     <option value="1" <%="1".equals(outoperation)?"selected":"" %>>></option>
						     <option value="2" <%="2".equals(outoperation)?"selected":"" %>><</option>
						     <option value="3" <%="3".equals(outoperation)?"selected":"" %>>=</option>
						</select></TD>						 
					</tr>					
					<tr>
						<TD class=column1 width=30% align=center>流出速率突变告警级别</td>
						<TD class=column2 width=20% align=left><select name="outwarninglevel" <%="1".equals(outtbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(outwarninglevel)?"selected":"") %>>清除告警</option>
							<option value="1" <%=("1".equals(outwarninglevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(outwarninglevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(outwarninglevel)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(outwarninglevel)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(outwarninglevel)?"selected":"") %>>紧急告警</option>
						</select></td>
						<TD class=column1 width=30% align=center>流出速率恢复突变告警级别</TD>
						<TD class=column2 width=20% align=left><select name="outreinstatelevel" <%="1".equals(outtbflag)?"":"disabled"%>>
						    <option value="0" <%=("0".equals(outreinstatelevel)?"selected":"") %>>清除告警</option>
						    <option value="1" <%=("1".equals(outreinstatelevel)?"selected":"") %>>正常日志</option>
							<option value="2" <%=("2".equals(outreinstatelevel)?"selected":"") %>>提示告警</option>
							<option value="3" <%=("3".equals(outreinstatelevel)?"selected":"") %>>一般告警</option>
							<option value="4" <%=("4".equals(outreinstatelevel)?"selected":"") %>>严重告警</option>
							<option value="5" <%=("5".equals(outreinstatelevel)?"selected":"") %>>紧急告警</option>
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
	 <input type="button" value=" 保 存 " class="jianbian" name="save" onclick="javascript:CheckForm();">
	 <input type="button" value="返回端口列表 " class="jianbian" name="close" onclick="javascript:goback();">
	 <input type="button" value=" 关 闭 " class="jianbian" name="close" onclick="javascript:window.close();">
	 </td>
	</tr>
</TABLE>
</form>
<IFRAME id="childFrm" name="childFrm" SRC="" STYLE="display:none;width:500;height:500"></IFRAME>



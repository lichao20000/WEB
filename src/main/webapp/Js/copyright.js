var CopyStr;
if(strAreaName == '山西联通'){
	CopyStr  = "<TABLE border=0 cellspacing=0 cellpadding=0 width='100%' " +
			"style='position: fixed;bottom: 20px;'>";
	CopyStr += "<TR><TD align=center><HR color=black size=1 width='80%'></TD></TR>";
	CopyStr += "<TR><TD align=center><SPAN class=90v><BR>&copy; 2019 "+"版权所有;Powered By：亚信科技";
	CopyStr += "</TD></TABLE>";
}else if(strAreaName == '宁夏联通'){
	CopyStr  = "<TABLE border=0 cellspacing=0 cellpadding=0 width='100%'>";
	CopyStr += "<TR><TD align=center><HR color=black size=1 width='80%'></TD></TR>";
	CopyStr += "<TR><TD align=center><SPAN class=90v>请以 <FONT face=Arial>IE5.5 </FONT>以上版本 <FONT face=Arial>1024 * 768</FONT> 浏览<BR>&copy; 2020 "+ strAreaName +".版权所有;Powered By：<a href='http://www.lianchuang.com/'>亚信科技</a>";
	CopyStr += "</TD></TABLE>";
}else{
	CopyStr  = "<TABLE border=0 cellspacing=0 cellpadding=0 width='100%'>";
	CopyStr += "<TR><TD align=center><HR color=black size=1 width='80%'></TD></TR>";
	CopyStr += "<TR><TD align=center><SPAN class=90v>请以 <FONT face=Arial>IE5.5 </FONT>以上版本 <FONT face=Arial>1024 * 768</FONT> 浏览<BR>&copy; 2008 "+ strAreaName +".版权所有;Powered By：<a href='http://www.lianchuang.com/'>联创科技</a>";
	CopyStr += "</TD></TABLE>";
}

document.write(CopyStr);




var str1 = "<TABLE border=0 cellspacing=0 cellpadding=0 width=\"100%\">"+
	       "<TR><TD HEIGHT=20>&nbsp;</TD></TR><TR><TD><p>&nbsp;</p>"+
		"<TABLE width=\"50%\" border=0 cellspacing=0 cellpadding=0 align=\"center\">"+
			"<TR>"+
				"<TD bgcolor=#000000>"+
				"<TABLE border=0 cellspacing=1 cellpadding=2 width=\"100%\">"+
					"<TR>"+
						"<TH align=\"center\">������Ϣ��ʾ</TH>"+
					"</TR>"+
					"<TR bgcolor=\"#FFFFFF\">"+
						"<TD class=column height=50 align=center>";

var str2 = "<div id=text>��ʹ�õ������Ѿ����ڣ���ҳ����<strong id=tt></strong>����ת��<span id=link></span></div>";

var str3 = "</TD></TR><TR><TD class=foot align=\"right\"></TD></TR></TABLE></TD>"+
		   "</TR></TABLE></TD></TR><TR><TD>&nbsp;</TD></TR><TR><TD HEIGHT=20>&nbsp;</TD></TR></TABLE>";


function go(t,url){
	//t������תʱ�䣺��
	//url������ת��ַ
	document.write(str1+str2+str3);

	document.getElementById("link").innerHTML="<a href="+url+">"+"�����޸�ҳ��"+"</a>";
	$(t,url);
}

//��ת��ʾ
function goTips(t,url,msg){
	//t������תʱ�䣺��
	//url������ת��ַ
	str2 = "<div id=text>" + msg + "����ҳ����<strong id=tt></strong>����ת��<span id=link></span></div>";
	document.write(str1+str2+str3);

	document.getElementById("link").innerHTML="<a href="+url+">"+"�����޸�ҳ��"+"</a>";
	$(t,url);
}
     
function $(t,url){
	ta = t-1;
	tb = t + "000";
	d = document.getElementById("tt");
	d.innerHTML = t;

	window.setInterval(function() {
		go_to(url);
	},1000);
}
     
function go_to(url){
	d.innerHTML = ta--;
	if(ta <= 0){
		//document.write("<LINK REL=\"stylesheet\" HREF=\".\/css\/css_green.css\" TYPE=\"text\/css\">"+str1+"������ת����<a href="+url+">"+url+"</a>"+str3);
		location.href=url;
	} else{
		return;
	}
}

/************************************************

 * �Ա�����ѡ���к��е����
 *
 * Create 2005-5-27 By yuht

 ************************************************/



var g_tbl_data = null;	//��ű����Ч����

var g_tbl_obj  = null;	//�ȴ�����ı�����

var g_tbl_head = null;	//��ű���ͷ����	��ʱû����

var g_realpath = "../";

var g_maxcell = 0;

var DEBUG = false;		//�Ƿ��ǲ���ģʽ



function initialize(str_obj,headIndex,e_row){		
	g_tbl_obj = eval(str_obj);

	iRows = g_tbl_obj.rows.length;

	//if(DEBUG) alert("���������: "+iRows);

	//�������ͷ��������

	//g_tbl_head = new Array(1);

	//row = g_tbl_obj.rows[headIndex];

	//iCells = row.cells.length;

	//for(var j=0;j<iCells;j++){

	//	cell = row.cells[j];

	//	g_tbl_head[0][j] = cell.innerText;

	//}

	//���������ݵ�������

	g_tbl_data = new Array(iRows-headIndex-e_row);

	var ln = 0;

	for(var i=headIndex;i<iRows-e_row;i++){

		row = g_tbl_obj.rows[i];				//�õ��ж���

		iCells = row.cells.length;				//�õ��е��е�����

		if(g_maxcell<iCells) g_maxcell=iCells;

		g_tbl_data[i-headIndex] = new Array(iCells);	

		//if(DEBUG && i==headIndex) alert(iCells);

		for(var j=0;j<iCells;j++){

			cell = row.cells[j];

			g_tbl_data[i-headIndex][j] = cell.innerText;

			//if(DEBUG && i==headIndex) alert(cell.innerText);

			//alert(cell.innerText);

		}

	}

	

	l = (document.body.clientWidth-800)/2;

	//t = (document.body.clientHeight-540)/2; 
	t = (document.body.clientHeight-400)/2; 



	if(typeof(idDivWin) != "object"){

		document.body.insertAdjacentHTML("afterBegin",
		"<div id=idDivWin style=\"position:absolute;z-index:1000;top:"
		+t+"px;left:"+l
		+"px;width:800px;border:solid #999999 1.5px;background-color:#E0E0E0\"><form action='"
		+g_realpath
		+"OutExcel.jsp' method='post' name=outfrm onsubmit='return OutExcel();' target=childFrm>"
		+"<table border=0 cellspacing=0 cellpadding=0 width='100%'><tr><th>�����������</th></tr>"
		+"<tr height=15><td></td></tr><tr><td align=center>"
		+"<div id=idpreview style=\"width:768px;height:445px;overflow:auto;\"></div></td></tr><tr height=15><td></td></tr><tr><td align=right>"
		+"<input type=submit value=' �� �� ' class=btn>&nbsp;&nbsp;<input type=button value=' �� �� ' onclick='closeDivWin()' class=btn>"
		+"&nbsp;&nbsp;</td></tr><tr height=15><td></td></tr></table><TEXTAREA NAME=rptdata ROWS=5 COLS=80 style='display:none'></TEXTAREA></form></div>");


        
		preview();
		//showElement("SELECT");

	}

	else{

		idDivWin.style.display = "";

	}
	//���������Ƶ�����
	window.scroll(l,t);	

}


function initializeTxt(str_obj,headIndex,e_row){	

	g_tbl_obj = eval(str_obj);

	iRows = g_tbl_obj.rows.length;

	//if(DEBUG) alert("���������: "+iRows);

	//�������ͷ��������

	//g_tbl_head = new Array(1);

	//row = g_tbl_obj.rows[headIndex];

	//iCells = row.cells.length;

	//for(var j=0;j<iCells;j++){

	//	cell = row.cells[j];

	//	g_tbl_head[0][j] = cell.innerText;

	//}

	//���������ݵ�������

	g_tbl_data = new Array(iRows-headIndex-e_row);

	var ln = 0;

	for(var i=headIndex;i<iRows-e_row;i++){

		row = g_tbl_obj.rows[i];				//�õ��ж���

		iCells = row.cells.length;				//�õ��е��е�����

		if(g_maxcell<iCells) g_maxcell=iCells;

		g_tbl_data[i-headIndex] = new Array(iCells);	

		//if(DEBUG && i==headIndex) alert(iCells);

		for(var j=0;j<iCells;j++){

			cell = row.cells[j];

			g_tbl_data[i-headIndex][j] = cell.innerText;

			//if(DEBUG && i==headIndex) alert(cell.innerText);

			//alert(cell.innerText);

		}

	}

	

	l = (document.body.clientWidth-800)/2;

	//t = (document.body.clientHeight-540)/2; 
	t = (document.body.clientHeight-400)/2; 



	if(typeof(idDivWin) != "object"){

		document.body.insertAdjacentHTML("afterBegin","<div id=idDivWin style=\"position:absolute;z-index:1000;top:"+t+"px;left:"+l+"px;width:800px;border:solid #999999 1.5px;background-color:#E0E0E0\"><form action='"+g_realpath+"OutTxt.jsp' method='post' name=outfrm onsubmit='return OutTxt();' target=childFrm><table border=0 cellspacing=0 cellpadding=0 width='100%'><tr><th>�����������</th></tr><tr height=15><td></td></tr><tr><td align=center><div id=idpreview style=\"width:768px;height:445px;overflow:auto;z-index:255;\"></div></td></tr><tr height=15><td></td></tr><tr><td align=right><input type=submit value=' �� �� ' class=btn>&nbsp;&nbsp;<input type=button value=' �� �� ' onclick='closeDivWin()' class=btn>&nbsp;&nbsp;</td></tr><tr height=15><td></td></tr></table><TEXTAREA NAME=rptdata ROWS=5 COLS=80 style='display:none'></TEXTAREA></form></div>");



		preview();

	}

	else{

		idDivWin.style.display = "";

	}
	//���������Ƶ�����
	window.scroll(l,t);	

}



function closeDivWin(){

	g_tbl_obj=null;
    g_tbl_data = null;
    g_tbl_head = null;
	idDivWin.parentNode.removeChild(idDivWin);
	showAllElement("select");
	//document.getElementById("idDivWin")
	//idDivWin.style.display = "none";

}

function hiddenAllElement(elmID){
	// 2020/05/07 �� document.all.tags ��Ϊ document.getElementsByTagName ��֧�ָ��������
	var elements = document.getElementsByTagName(elmID);
	for (i = 0; i < elements.length; i++){
		obj = elements[i];
		if (! obj || ! obj.offsetParent)
			continue;
		obj.style.visibility = "hidden";
	}
	document.onmouseup = null;
}

function showAllElement(elmID){
	for (i = 0; i < document.all.tags(elmID).length; i++){
		obj = document.all.tags(elmID)[i];
		if (! obj || ! obj.offsetParent)
			continue;
		obj.style.visibility = "";
	}
	 if(C_MouseUpft != null)
        document.onmouseup = C_MouseUpft;
}
function C_MouseUpft(){
    if(idCalendar == null)
        return;
    var top  = parseInt(idCalendar.style.top);
    var left = parseInt(idCalendar.style.left);
    var wi   = idCalendar.offsetWidth;
    var he   = idCalendar.offsetHeight;
    var x    = event.clientX;
    var y    = event.clientY + document.body.scrollTop;
    var x1 = left + wi;
    var y1 = top + he;

    if(x>x1||x<left||y>y1||y<top){
        hideCalendar();
        showElement("SELECT");
    }
}
function preview(){	

	sHtml = '<table width="100%" border=0 cellspacing=0 cellpadding=0 ><tr><td bgcolor="#000000"><table border="0" cellspacing="1" cellpadding="2"  width="100%">';

	sHtml += '<tr><td class=head nowrap><input type=checkbox name="allcol" onclick=selectall(0) checked> ��ȫѡ<br><input type=checkbox name="allrow" onclick=selectall(1) checked> ��ȫѡ</td>';

	

	for(var i=0;i<g_maxcell;i++){

		sHtml += '<td align=center class=head nowrap><input type=checkbox name="col" checked></td>';

	}

	sHtml += '</tr>';

	var strSpan="";

	for(var i=0;i<g_tbl_data.length;i++){

		sHtml += '<tr><td align=center class=head nowrap><input type=checkbox name="row" checked></td>';

		

		if(g_tbl_data[i].length<g_maxcell)

			strSpan=" colspan="+g_maxcell+" align=center "

		for(var j=0;j<g_tbl_data[i].length;j++){

			sHtml += '<td class=column nowrap '+ strSpan +'>'+ g_tbl_data[i][j] +'</td>';

		}

		sHtml += '</tr>';

	}



	sHtml += '</table></td></tr></table>';


    hiddenAllElement("select");
	document.all("idpreview").innerHTML = sHtml;

	sHtml = "";

}



function selectall(){

	o = document.outfrm;

	if(arguments[0] == 0){

		if(typeof(o.col.length)=="number"){

			for(var i=0;i<o.col.length;i++){

				o.col[i].checked = o.allcol.checked;

			}

		}

		else

			o.col.checked = o.allcol.checked;

	}

	else{

		if(typeof(o.row.length)=="number"){

			for(var i=0;i<o.row.length;i++){

				o.row[i].checked = o.allrow.checked;

			}

		}

		else

			o.row.checked = o.allrow.checked;	

	}

}



function OutExcel(){

	if(typeof(childFrm) != "object"){

		document.body.insertAdjacentHTML("afterBegin","<IFRAME name=childFrm ID=childFrm SRC=\"about:blank\" STYLE=\"display:none\"></IFRAME>");

	}

	printdata();

	if(document.outfrm.rptdata.value==""){

		alert("û���κ����ݵ���");
//showElement("select");
		return false;

	}

	else{
//showElement("select");
		return true;

	}

}

function OutTxt(){

	if(typeof(childFrm) != "object"){

		document.body.insertAdjacentHTML("afterBegin","<IFRAME name=childFrm ID=childFrm SRC=\"about:blank\" STYLE=\"display:none\"></IFRAME>");

	}

	printdataTxt();

	if(document.outfrm.rptdata.value==""){

		alert("û���κ����ݵ���");
       // showElement("select");
		return false;

	}

	else{
       // showElement("select");
		return true;

	}

}

function printdataTxt()
{
	o = document.outfrm;
	for(var i=0;i<g_tbl_data.length;i++){
	if(typeof(o.row.length)=="number"){

			if(!o.row[i].checked) continue;

		}

		else{

			if(!o.row.checked) continue;

		}
		for(var j=0;j<g_tbl_data[i].length;j++){
			if(typeof(o.col.length)=="number"){

				if(!o.col[j].checked) continue;

			}

			else{

				if(!o.col.checked) continue;

			}
			sHtml +=g_tbl_data[i][j]+'|';
	 }
	sHtml +='\n';
}
document.outfrm.rptdata.value = sHtml;
}



function printdata(){

	o = document.outfrm;

	sHtml = '<TABLE border=1 cellspacing=0 cellpadding=2 borderColorLight="#000000" borderColorDark="#FFFFFF" width="96%">';

	for(var i=0;i<g_tbl_data.length;i++){

		if(typeof(o.row.length)=="number"){

			if(!o.row[i].checked) continue;

		}

		else{

			if(!o.row.checked) continue;

		}

		sHtml += '<tr>';

		for(var j=0;j<g_tbl_data[i].length;j++){

			if(typeof(o.col.length)=="number"){

				if(!o.col[j].checked) continue;

			}

			else{

				if(!o.col.checked) continue;

			}

			sHtml += '<td class=column nowrap>'+ g_tbl_data[i][j] +'</td>';

		}

		sHtml += '</tr>';

	}

	sHtml += "</table>";

	document.outfrm.rptdata.value = sHtml;

}



function setRealPath(path){

	g_realpath = path;

}

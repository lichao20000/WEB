<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
</head>

<body onload="init()">
<div id="dataArea">

</div>
</body>

<script>
function reset()
{
	var dataArr = new Array();
	
	// ȡ��ÿһ�������ص�����
	for(var j=1;j<rows.length;j++)
	{
		var box = rows[j].cells[0].firstChild;
		if(box.checked)
		{
			var input = rows[j].cells[7].lastChild;
			dataArr.push(input.value);
		}
	}	
	
	if(dataArr.length == 0)
	{
		alert("����ѡ��һ�����ݣ�");
		return;
	}
	//alert(dataArr.join("---"))
	// test document.getElementsByName('resetParam')
	
	var jsonArr = new Array();
	for(var j=0;j<dataArr.length;j++)
	{
		var user_id =  trim(dataArr[j].split("|")[0]);
		var device_id = trim(dataArr[j].split("|")[1]);
		var oui = trim(dataArr[j].split("|")[2]);
		var device_serialnumber = trim(dataArr[j].split("|")[3]);
		var serv_type_id = trim(dataArr[j].split("|")[4]);
		var serv_status = trim(dataArr[j].split("|")[5]);
		
		if(device_id=="")
		{
			alert("�û�δ���豸�����Ȱ��豸���ټ��");
			return;
		}
	
		var json = {user_id:user_id, device_id:device_id, oui:oui, device_serialnumber:device_serialnumber, serv_type_id:serv_type_id, serv_status:serv_status};
		jsonArr.push(json);			
	}

	// ����json���� datas:[a,b,c]��ʽ
	var jsonData = {datas:jsonArr};
	
	// ��װjson����
	if(!confirm('���¼����ǽ���ҵ����Ϊδ��״̬��ȷʵҪ������?'))
		return;
		
	var url = "<s:url value='/itms/service/bssSheetServ!callPreProcessBatch.action'/>";
	$.post(url,{
			batchReset:$.toJSON(jsonData)
	    },function(ajax){
	    	if(ajax=="1"){
		    		alert("��Ԥ���ɹ���");
		    		//query();
		    	}else if(ajax=="-1"){
		    		alert("����Ϊ�գ�");
		    	}else if(ajax=="-2"){
		    		alert("��Ԥ��ʧ�ܣ�");
		    	}
	    });
}

function init()
{
	dataArea.innerHTML = window.opener.document.getElementById("listTable").outerHTML;
	// Ŀ��table
	var table = document.getElementById("listTable");
	// ����Ҫ��ҳ��
	table.deleteRow(table.rows.length - 1);
	
	rows = table.rows;
	
	// ��һ��,���ȫѡ��checkbox
	var box_all = document.createElement("<input type='checkbox' id='all_box' >");
	box_all.attachEvent("onclick",selectAll);
	var cell1 = document.createElement("<TH>");
	table.getElementsByTagName("thead")[0].firstChild.insertAdjacentElement("afterBegin",cell1);
	// ȥ��������
	table.getElementsByTagName("thead")[0].firstChild.lastChild.removeNode(true);
	cell1.appendChild(box_all);
	cell1.align = "center";

	// ȥ����ͨ�ļ�¼,ֻ��ʧ�ܵ�voipչʾ
	for(var i=1; i < rows.length; i++)
	{
		// ҵ������
		cell_servtype = trim(rows[i].cells[3].firstChild.innerHTML);
		
		// ��ͨ״̬
		cell_openstatus = trim(rows[i].cells[5].firstChild.innerHTML);
		var yes = cell_servtype.indexOf("VOIP")!=-1 && cell_openstatus.indexOf("ʧ��")!=-1;

		// ���checkbox
		var box = document.createElement("<input type='checkbox' name='sel_box'>");
		var cell_1 = rows[i].insertCell(0);
		cell_1.appendChild(box);
		cell_1.align = "center";
		
		// ȥ�����Ĳ���һ��
		rows[i].cells[rows[i].cells.length - 1].removeNode(true);
		
		// ȥ��A���
		rows[i].cells[1].firstChild.removeNode();
		rows[i].cells[5].firstChild.removeNode();
		
		// ����Ҫ��������ǣ�����ɾ��
		if(!yes)
		{
			rows[i].style.display = "none";
		}
	}
	
	// ɾ������Ҫ����
	for(var i = rows.length - 1 ; i>=1 ; i--) //����ɾ��
	{
		if(rows[i].style.display == "none")
		{
			rows[i].removeNode(true);
		}
	}
	
	// table�������ϼ���İ�ť
	var tr_last = table.insertRow();
	var cell_last = tr_last.insertCell();
	cell_last.colSpan = "8";
	cell_last.className = "foot";
	cell_last.style.height = "30px";
	var button = document.createElement("<button>");
	cell_last.appendChild(button);
	cell_last.align = "center";
	button.innerHTML = "�� ��";
	button.attachEvent("onclick",reset);
	
	// ���ҳ���в���Ҫa���ӣ�ֻ���ı��Ϳ�����
	/*
	var atags = document.getElementsByTagName("a");
	alert(atags.length);
	for(var i in atags)
	{
		alert(atags[i].innerHTML);
		//var atag = atags[i];
		atags[i].removeNode(false);		
	}
	*/
}

// ȫѡ��ѡ
function selectAll()
{
	var all_box = document.getElementById("all_box");
	var boxes = document.getElementsByName("sel_box");
	for(var k=0; k<boxes.length; k++)
	{
		if(all_box.checked)
		{
			boxes[k].checked = true;
		}
		else
		{
			boxes[k].checked = false;
		}
	}
}

/** ���߷��� **/
/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}
</script>

 <script>
   jQuery.extend(
 {
  /**
   * @see  ��json�ַ���ת��Ϊ����
   * @param   json�ַ���
   * @return ����object,array,string�ȶ���
   */
  evalJSON : function (strJson)
  {
   return eval( "(" + strJson + ")");
  }
 });
 jQuery.extend(
 {
  /**
   * @see  ��javascript��������ת��Ϊjson�ַ���
   * @param ��ת������,֧��object,array,string,function,number,boolean,regexp
   * @return ����json�ַ���
   */
  toJSON : function (object)
  {
   var type = typeof object;
   if ('object' == type)
   {
    if (Array == object.constructor)
     type = 'array';
    else if (RegExp == object.constructor)
     type = 'regexp';
    else
     type = 'object';
   }
      switch(type)
   {
         case 'undefined':
       case 'unknown': 
     return;
     break;
    case 'function':
       case 'boolean':
    case 'regexp':
     return object.toString();
     break;
    case 'number':
     return isFinite(object) ? object.toString() : 'null';
       break;
    case 'string':
     return '"' + object.replace(/(\\|\")/g,"\\$1").replace(/\n|\r|\t/g,
       function(){   
                 var a = arguments[0];                   
        return  (a == '\n') ? '\\n':   
                       (a == '\r') ? '\\r':   
                       (a == '\t') ? '\\t': ""  
             }) + '"';
     break;
    case 'object':
     if (object === null) return 'null';
        var results = [];
        for (var property in object) {
          var value = jQuery.toJSON(object[property]);
          if (value !== undefined)
            results.push(jQuery.toJSON(property) + ':' + value);
        }
        return '{' + results.join(',') + '}';
     break;
    case 'array':
     var results = [];
        for(var i = 0; i < object.length; i++)
     {
      var value = jQuery.toJSON(object[i]);
           if (value !== undefined) results.push(value);
     }
        return '[' + results.join(',') + ']';
     break;
      }
  }
 });
  </script>

</html>
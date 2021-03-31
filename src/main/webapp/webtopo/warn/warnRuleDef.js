function printError(_obj){
	alert(_obj.description);
}
//����е���¼�
function clkRow(){
	var obj = event.srcElement.parentElement;
	tabRowIndex = obj.rowIndex;
	chColor(tabRowIndex);
	event.cancelBubble = true;
}
//�ı��е���ɫ
function chColor(rowIndex){
	if(rowIndex == null)
		return ;
	try{
		var oTab = document.all("myTable");
		clearColor();
		oTab.rows[rowIndex].bgColor="LightSkyBlue";
	}catch(e){
		e.print();
	}finally{
		oTab = null;	
	}
}
//��������ɫ
function clearColor(){
	var oTab = document.all("myTable");
	for(var i=2;i<oTab.rows.length;i++){
		oTab.rows[i].bgColor="#ffffff";
	}
}
//body����¼�
function bodyClk(){ 
	clearColor();
	tabRowIndex = -1;
}
//�õ����ĵ�Ԫ��value
function getCellValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].innerText;
	}catch(e){
		e.print();
	}
}
//�õ������ӵ�Ԫ��value
function getCellChildValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].firstChild.value;
	}catch(e){
		e.print();
	}
}
//�õ������ӵ�Ԫ���״̬(�Ƿ�ѡ��checkbox)
function getCellChildState(oTab,rowIndex,cellIndex){
	try{
		var obj = oTab.rows[rowIndex].cells[cellIndex].firstChild;
		if(obj.checked){
			return 1;
		}else{
			return 0;
		}
	}catch(e){
		e.print();
	}
}
//���õ�Ԫ���ֵ
function setCellValue(oTab,rowIndex,cellIndex,_value){
	try{
		oTab.rows[rowIndex].cells[cellIndex].innerHTML = _value;
	}catch(e){
		e.print();
	}
}
//������ť���µ���
//count:�����ƶ���ƫ����
//begin:��ʼ�ӵڼ�������
//end:����в�Ҫ�ƶ�
function upBtn(count,begin,end){
	try{
		if(typeof(tabRowIndex) == 'undefined' ||tabRowIndex == -1){
			alert("��ѡ������");
			return ;
		}
		var oTab = document.all("myTable");
		var sourceRowIndex = tabRowIndex;
		var detinstRowIndex = sourceRowIndex - count;
		if(detinstRowIndex == 1 || detinstRowIndex == oTab.rows.length){
			this.event.cancelBubble = true;
			return;
		}
		convert(oTab.rows,sourceRowIndex,detinstRowIndex,begin,end);
		tabRowIndex = sourceRowIndex - count;
		chColor(tabRowIndex);
		this.event.cancelBubble = true;
	}catch(e){
		e.print();
	} finally {
		oTab = null;
	}
}
//��������еĵ�Ԫֵ
function convert(rows,indexSource,indexDetinst,begin,end){
	var tmpValue = "";
	var sRow = rows[indexSource];
	var dRow = rows[indexDetinst];
	var len = sRow.cells.length-end;
	for(var i=begin;i<len;i++){
		tmpValue = sRow.cells[i].innerHTML;
		sRow.cells[i].innerHTML = dRow.cells[i].innerHTML;
		dRow.cells[i].innerHTML = tmpValue;
	}
}
//������
function createRow(oTab,text,index)
{   
	  var oRow = oTab.insertRow();
	  oRow.bgColor = "#FFFFFF";
	  oRow.align = "center";
	  //�����е���¼�
	  oRow.onclick=clkRow;
	  var tmpCell = "<a href=\"javascript://\" title='�༭��������' name='edit' onclick='edit()'>�༭</a>&nbsp;";
	  tmpCell += "<a href=\"javascript://\" title='ɾ����������' name='del' onclick='delRow()'>ɾ��</a>";
	  var oCell = oRow.insertCell();
	  oCell.innerHTML = index;
	  oCell = oRow.insertCell();
	  oCell.align="left";
	  oCell.innerText = text;
	  oCell = oRow.insertCell();
	  oCell.innerHTML = "<select id='invocation'><option value='1' selected>����</option><option value='0' >����</option></select>";
	  oCell = oRow.insertCell();
	  oCell.innerHTML = tmpCell;
}
//ɾ����
function delRow(){
	 if(!confirm("ȷ��ɾ��?"))
	 	return;
	 var intRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
	 var oTab = document.all("myTable");
	 for(var i=intRowIndex;i<oTab.rows.length;i++){
		oTab.rows[i].cells[0].innerHTML --;
	 }
	 oTab.deleteRow(intRowIndex);
}
//������
function addRow(text){
	var oTab = document.all("myTable");
	var tbIndex=oTab.rows.length;
	createRow(oTab,text,tbIndex-2);
}
//�༭��
function editRow(rowIndex,text){
	//����"/"����ֹ��̨����
	if(text.indexOf("�豸����")!= -1 && text.lastIndexOf('/') == -1){
		text += "/";
	}
	var oTab = document.all("myTable");
	setCellValue(oTab,rowIndex,1,text);
}
function printError(_obj){
	alert(_obj.description);
}
//表格行点击事件
function clkRow(){
	var obj = event.srcElement.parentElement;
	tabRowIndex = obj.rowIndex;
	chColor(tabRowIndex);
	event.cancelBubble = true;
}
//改变行的颜色
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
//清除表格颜色
function clearColor(){
	var oTab = document.all("myTable");
	for(var i=2;i<oTab.rows.length;i++){
		oTab.rows[i].bgColor="#ffffff";
	}
}
//body点击事件
function bodyClk(){ 
	clearColor();
	tabRowIndex = -1;
}
//得到表格的单元格value
function getCellValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].innerText;
	}catch(e){
		e.print();
	}
}
//得到表格的子单元格value
function getCellChildValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].firstChild.value;
	}catch(e){
		e.print();
	}
}
//得到表格的子单元格的状态(是否被选中checkbox)
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
//设置单元格的值
function setCellValue(oTab,rowIndex,cellIndex,_value){
	try{
		oTab.rows[rowIndex].cells[cellIndex].innerHTML = _value;
	}catch(e){
		e.print();
	}
}
//单独按钮上下调整
//count:上下移动的偏移量
//begin:开始从第几列移起
//end:最后几列不要移动
function upBtn(count,begin,end){
	try{
		if(typeof(tabRowIndex) == 'undefined' ||tabRowIndex == -1){
			alert("请选择表格行");
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
//交换表格行的单元值
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
//创建行
function createRow(oTab,text,index)
{   
	  var oRow = oTab.insertRow();
	  oRow.bgColor = "#FFFFFF";
	  oRow.align = "center";
	  //增加行点击事件
	  oRow.onclick=clkRow;
	  var tmpCell = "<a href=\"javascript://\" title='编辑规则详情' name='edit' onclick='edit()'>编辑</a>&nbsp;";
	  tmpCell += "<a href=\"javascript://\" title='删除规则详情' name='del' onclick='delRow()'>删除</a>";
	  var oCell = oRow.insertCell();
	  oCell.innerHTML = index;
	  oCell = oRow.insertCell();
	  oCell.align="left";
	  oCell.innerText = text;
	  oCell = oRow.insertCell();
	  oCell.innerHTML = "<select id='invocation'><option value='1' selected>启用</option><option value='0' >禁用</option></select>";
	  oCell = oRow.insertCell();
	  oCell.innerHTML = tmpCell;
}
//删除行
function delRow(){
	 if(!confirm("确认删除?"))
	 	return;
	 var intRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
	 var oTab = document.all("myTable");
	 for(var i=intRowIndex;i<oTab.rows.length;i++){
		oTab.rows[i].cells[0].innerHTML --;
	 }
	 oTab.deleteRow(intRowIndex);
}
//新增行
function addRow(text){
	var oTab = document.all("myTable");
	var tbIndex=oTab.rows.length;
	createRow(oTab,text,tbIndex-2);
}
//编辑行
function editRow(rowIndex,text){
	//加上"/"，防止后台出错
	if(text.indexOf("设备类型")!= -1 && text.lastIndexOf('/') == -1){
		text += "/";
	}
	var oTab = document.all("myTable");
	setCellValue(oTab,rowIndex,1,text);
}
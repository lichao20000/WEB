//定义变量
var refnum=5;//设置按钮刷新时间为10秒
var refnum_total=0;//保存刷新时间
var time_out=null;//定时器
var ref_interval=null;//恢复告警定时器
var rid;//规则ID
var RM_Menu = new CMenu("RM_Menu");
var timeout=0;//超时的次数
//**********************For Ctrl & Shift Click*************
var startindex,endindex;//起始位置、结束位置
var sel_json={};//选中的数据组成的json，结构：<id>:{id:<id>,css:<css>}   <id>:为记录的id
var sel_id="";//选中的tr的id，用-/-分隔
var sel_devid="";//删除、确认告警时使用的参数Numer+"-"+gatherid+"-"+deviceid
//*************************End Right Menu******************

//切换手动和自动刷新
function ChangeAutoOrHandRef(val,intevel){
	$.showRef(val);
	if(time_out!=null)
		window.clearTimeout(time_out);
		time_out=null;
	if(val==0){//自动刷新
		SetRefTime(intevel);
		ShowTimeRef();
	}else{//手动刷新
		SetRefTime(5);
		RefBtn();
	}
}
//设置刷新时间
 function SetRefTime(t){
	if(time_out!=null)
		window.clearTimeout(time_out);
	time_out=null;
	refnum=eval(t);
	refnum_total=refnum;
 }
 //重新设置刷新时间
 function ReSetRefTime(){
	if(time_out!=null)
		window.clearTimeout(time_out);
	time_out=null;
	refnum=refnum_total;
 }
//显示还有多少时间刷新
function ShowTimeRef(){
	document.getElementById("next_reftime").innerHTML="距离自动刷新时间还有"+refnum+"秒";
	if(refnum==0){
		window.clearTimeout(time_out);
		time_out=null;
		$.RefData();
	}else{
		refnum--;
        time_out=window.setTimeout("ShowTimeRef()",1000);
	}	
}
//按钮刷新
function RefBtn(){
	document.getElementById("next_reftime").innerHTML="距离手动刷新时间还有"+refnum+"秒";
    if(refnum==0){
        window.clearTimeout(time_out);
		time_out=null;
        document.getElementById("refbtn").value="刷新";
        document.getElementById("refbtn").disabled=false;
    }else{
        document.getElementById("refbtn").value="刷新("+refnum+")";
		document.getElementById("refbtn").disabled="true";
        refnum--;
        time_out=window.setTimeout("RefBtn()",1000);
    }
}
//定时获取恢复告警
function getRefWarn(){
	//BBMS暂时没有提供清除告警的功能,故此功能暂时屏蔽
	/**
	if(ref_interval!=null){
		window.clearInterval(ref_interval);
		ref_interval=null;
	}
	ref_interval=window.setInterval("$.getRecWarn()",(1000*10+10));
	*/
}
//关闭恢复告警定时器
function ClearWarnInterval(){
	if(ref_interval!=null){
		window.clearInterval(ref_interval);
		ref_interval=null;
	}
}

//创建右健菜单
function createRMenu(type,pid){
	if(RM_Menu.menu.length>0){
		return;
	}
	var n=0;
	RM_Menu.menu.length=0;
	for(var i=0;i<arrRMenu.length;i++){
		if(arrRMenu[i] == null) continue;
				if(typeof(arrRMenu[i]._submenu) == "string")
				{
					RM_Menu.menu[n] = new menu("RM_Menu"+n,
						arrRMenu[i]._name,
						"","",
						arrRMenu[i]._target,
						"",0,
						arrRMenu[i]._submenu);
					createRSMenu(arrRMenu[i]._submenu,type,pid);
				}
				else
					RM_Menu.menu[n] = new menu("RM_Menu"+n,
						arrRMenu[i]._name,
						"","",
						arrRMenu[i]._target,
						"",1,
						arrRMenu[i]._link);
				n++;
			}
}

/**
 * 菜单数据结构
 * keyword：菜单所属关键字
 * name：菜单名称
 * link：菜单连接
 * desc：菜单描述
*/
function RMenuRes(type,keyword, name, link,target, icon,desc, oper,smenu){
	this._type = type;
	this._keyword = keyword;
	this._name    = name;
	this._link	  = link;
	this._target = target;
	this._icon = icon;
	this._desc	  = desc;
	this._oper   = oper;
	if(smenu != null){
		this._submenu = smenu;
	}
}
var arrRMenu=[];
arrRMenu[arrRMenu.length] = new RMenuRes("-3","CHECKSTOREALARM","确认告警","$.ConfigAlarm()","javascript","","确认告警并入库","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","DELALARM","清除告警","$.DelAlarm()","javascript","","清除告警","");

arrRMenu[arrRMenu.length] = new RMenuRes("*","","-","","","","","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","DELALARM","移除告警","$.RemoveWarn(-2)","javascript","","移除告警","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","EXPORTEXCEL","导出EXCEL","$.Export()","javascript","","导出Excel","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","FINDDEVICEOBJ","定位设备对象","$.findDevObj()","javascript","","定位设备对象","");



function showRightMenu(){
	//显示之前，首先将原有的层删除掉
	document_click();
	//画图
	drawSubMenu(RM_Menu);
	//alert(RM_Menu.menu.length);
	//显示菜单
	showRMenu("RM_Menu");
	g_blnRMShow = true;
	return;
}

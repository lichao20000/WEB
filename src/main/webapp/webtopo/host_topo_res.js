//------------------------------------------------------------
// Copyright (c) 2004-2005 linkage. All rights reserved.
// Version 1.0.03
// Ahthor Haiteng.Yu
//------------------------------------------------------------
/**
 * 菜单数据结构
 * keyword：菜单所属关键字
 * name：菜单名称
 * link：菜单连接
 * desc：菜单描述
*/
function RMenuRes(type,keyword, name, link,target, icon,desc, oper){
	this._type = type;
	this._keyword = keyword;
	this._name    = name;
	this._link	  = link;
	this._target = target;
	this._icon = icon;
	this._desc	  = desc;
	this._oper   = oper;
	if(arguments.length>8){
		this._submenu = arguments[8];
	}
}



//网络视图的快捷菜单
var netMenu=new Array();
netMenu[0]=new String("HOST_ADDOBJECT");
netMenu[1]=new String("HOST_SAVETOPO");
netMenu[2]=new String("HOST_GOUP");



//右健菜单资源
//右健菜单资源
/*
	针对菜单情况的说明:
	如果该菜单是在任何地方可以点击显示，则添入"";
	如果该菜单是针对所有的设备、网段、链路,则用"*",针对单个的对象
	如果该菜单是针对所有的设备、网段、链路,则用"***",针对多个的对象
	如果该菜单针对网段,则填入"1"
	如果该菜单针对所有的设备,则填入"node";
	如果该菜单针对cpe设备，则填入"device_cpe"
	如果该菜单针对特定的设备类型，则填入该设备类型的序列号;
	如果该菜单针对链路,则填入"link";	
	如果该菜单针对告警,则填入"-3";
	如果该菜单针对网段和网元，则填入"nodes";
	特殊处理：
	针对网元管理的特殊处理

*/
var arrRMenu = new Array();
arrRMenu[0] = new RMenuRes("","HOST_GOUP","返回上一层","getParentTopo.jsp","childFrm","","返回上一层Topo","");
arrRMenu[1] = new RMenuRes("1","HOST_GODOWN","进入下一层","getChildTopo.jsp","childFrm","","进入下一层Topo","");
arrRMenu[2] = new RMenuRes("*","","-","","","","","");
arrRMenu[3] = new RMenuRes("node","HOST_OBJECTATTRIBUTE","对象属性","HOST_ShowObjectAttr","javascript","","查看当前对象属性","");

//告警列表右健菜单

arrRMenu[4] = new RMenuRes("-3","CHECKALARM","确认告警","ack_Warn","javascript","","确认告警","");
arrRMenu[5] = new RMenuRes("-3","DELETEALARM","删除告警","remove_Warn","javascript","","删除告警","");
//arrRMenu[6] = new RMenuRes("-3","ALARMQUERY","告警详情","query_Warn","javascript","","告警详情","");
arrRMenu[6] = new RMenuRes("-3","CHECKSTOREALARM","确认并入库","ack_Store_Warn","javascript","","确认告警并入库","");
arrRMenu[7] = new RMenuRes("node","HOST_EDITATTRIBUTE","修改对象","HOST_EditObjectAttr","javascript","","修改当前对象属性","");
arrRMenu[8] = new RMenuRes("nodes","HOST_DELETEOBJECT","删除对象","HOST_DelObject","javascript","","删除选择的对象","");
arrRMenu[9] = new RMenuRes("node","HOST_PERFCONFIG","性能配置","HOST_perfConfig","javascript","","进行性能配置","");
arrRMenu[10] = new RMenuRes("node","HOST_WARNCONFIG","告警配置","HOST_warnConfig","javascript","","进行告警配置","");
arrRMenu[11] = new RMenuRes("node","HOST_PERFVIEW","性能查看","HOST_perfView","javascript","","性能查看","");
arrRMenu[12] = new RMenuRes("node","HOST_WARN","对象告警","showDeviceWarn","javascript","","查看当前设备的告警","");
arrRMenu[13] = new RMenuRes("-3","FINDDEVICEOBJ","定位设备对象","findDevObj","javascript","","定位设备对象","");

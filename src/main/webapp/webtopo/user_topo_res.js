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



//主机视图的快捷菜单
var netMenu=new Array();
netMenu[0]=new String("ADDOBJECT");
netMenu[1]=new String("ADDLINK");
netMenu[2]=new String("LOADTOPO");
netMenu[3]=new String("REDISCOVERTOPO");
netMenu[4]=new String("INCREMENTFINDTOPO");
netMenu[5]=new String("SAVETOPO");
netMenu[6]=new String("GOUP");
netMenu[7]=new String("VIEW_ALL_DEVICE");
netMenu[8]=new String("VIEW_MANAGED_DEVICE");
//手动刷新、自动刷新 权限控制
//netMenu[9]=new String("WARN_ATUO_FRESH");
//netMenu[10]=new String("WARN_FRESH_BY_HAND");


//右健菜单资源
/*
	针对菜单情况的说明:
	如果该菜单是在任何地方可以点击显示，则添入"";
	如果该菜单是针对所有的设备、网段、链路,则用"*",针对单个的对象
	如果该菜单是针对所有的设备、网段、链路,则用"***",针对多个的对象
	如果该菜单针对网段,则填入"0"
	如果该菜单针对所有的设备,则填入"device";
	如果该菜单针对所有的网络设备,则填入"device_net";
	如果该菜单针对所有的网络主机,则填入"device_host";
	如果该菜单针对cpe设备，则填入"device_cpe"
	如果该菜单针对特定的设备类型，则填入该设备类型的序列号;
	如果该菜单针对链路,则填入"link";	
	如果该菜单针对告警,则填入"-3";
	如果该菜单针对多个网段和网元，则填入"net";
	特殊处理：
	针对网元管理的特殊处理；
	如果该菜单针对单个网段或者网元，则填入"singlenet"

*/
var arrRMenu = new Array();
arrRMenu[0] = new RMenuRes("","GOUP","返回上一层","getParentTopo.jsp","childFrm","","返回上一层Topo","");
arrRMenu[1] = new RMenuRes("0","GODOWN","进入下一层","getChildTopo.jsp","childFrm","","进入下一层Topo","");
arrRMenu[2] = new RMenuRes("*","","-","","","","","");
arrRMenu[3] = new RMenuRes("device_net","OBJECTATTRIBUTE","属性","showDevDetail","javascript","","查看当前对象属性","");

arrRMenu[4] = new RMenuRes("device_net","OBJECTATTRIBUTE","端口信息","showPortInfo","javascript","","查看当前对象端口信息","");
arrRMenu[5] = new RMenuRes("device_net","OBJECTATTRIBUTE","设备流量","showFlux","javascript","","查看当前对象流量","");
arrRMenu[6] = new RMenuRes("device_net","OBJECTATTRIBUTE","显示设备性能","showPmee","javascript","","查看当前设备性能","");
//告警列表右健菜单
arrRMenu[7] = new RMenuRes("-3","CHECKALARM","确认告警","ack_Warn","javascript","","确认告警","");
arrRMenu[8] = new RMenuRes("-3","DELETEALARM","删除告警","remove_Warn","javascript","","删除告警","");
arrRMenu[9] = new RMenuRes("-3","CHECKSTOREALARM","确认并入库","ack_Store_Warn","javascript","","确认告警并入库","");
arrRMenu[10] = new RMenuRes("device_cpe","CIRCUITDIAGRAM","电路图","showCircuit","javascript","","查看当前设备的电路图","");
arrRMenu[11] = new RMenuRes("device","DEVICEALARM","设备告警","showDeviceWarn","javascript","","查看当前设备的告警","");
arrRMenu[12] = new RMenuRes("device_net","GODOWN","实用工具","","","","","","RSMenu1");
arrRMenu[13] = new RMenuRes("***","OPERATION#","操作","","","","对设备进行管理","","RSMenu2");
arrRMenu[14] = new RMenuRes("link","LINKOPERATION","链路流量","showLinkInfo","javascript","","对链路进行操作","");
arrRMenu[15] = new RMenuRes("link","LINKOPERATION","链路配置","editLinkInfo","javascript","","对链路进行操作","");
arrRMenu[16] = new RMenuRes("-3","FINDDEVICEOBJ","定位设备对象","findDevObj","javascript","","定位设备对象","");
arrRMenu[17] = new RMenuRes("0","GODOWN","显示用户信息","ViewUserInfo","javascript","","显示用户信息","");
arrRMenu[18] = new RMenuRes("0","GODOWN","修改用户信息","UpdateUserInfo","javascript","","修改用户信息","");
arrRMenu[19] = new RMenuRes("0","GODOWN","修改用户图标","UpdateUserIcon","javascript","","修改用户图标","");
arrRMenu[20] = new RMenuRes("0","GODOWN","删除用户图标","DeleteUserIcon","javascript","","删除用户图标","");
arrRMenu[21] = new RMenuRes("0","GODOWN","添加用户图标","AddUserIcon","javascript","","添加用户图标","");
arrRMenu[22] = new RMenuRes("0","GODOWN","添加设备图标","AddDeviceIcon","javascript","","添加设备图标","");
arrRMenu[23] = new RMenuRes("device_host","GOUP","显示对象定义","HOST_ShowObjectAttr","javascript","","显示当前对象属性","");
arrRMenu[24] = new RMenuRes("device_host","GOUP","性能查看","HOST_perfView","javascript","","性能查看","");
//arrRMenu[25] = new RMenuRes("device_host","GOUP","性能配置","HOST_perfConfig","javascript","","进行性能配置","");

var RSMenu1= new Array();
RSMenu1[0]=new RMenuRes("device_net","GODOWN","Ping","pingIp","javascript","","Ping","");
RSMenu1[1]=new RMenuRes("device_net","GODOWN","Telnet","telnetLink","javascript","","Telnet","");

var RSMenu2=new Array();
RSMenu2[0] = new RMenuRes("netmanager1","MANAGEDEVICEWIN#","管理网元","ManagerDeviceWin","javascript","","管理网元","");
RSMenu2[1] = new RMenuRes("netmanager2","UNMANAGEDEVICEWIN#","取消管理","UnManagerDeviceWin","javascript","","取消管理网元","");
RSMenu2[2] = new RMenuRes("***","","-","","","","","");
RSMenu2[3] = new RMenuRes("net","CUT"," 剪 切 ","CutNet","javascript","","剪切网元","");
RSMenu2[4] = new RMenuRes("net1","PASTE"," 粘 贴 ","PasteNet","javascript","","剪切网元","");
RSMenu2[5] = new RMenuRes("net1","CANCELCUT","取消剪切","CancelCut","javascript","","剪切网元","");
RSMenu2[6] = new RMenuRes("net","DELETEOBJECT","删除对象","DeleteObj","javascript","","删除选择的对象","");
RSMenu2[7] = new RMenuRes("link","DELETELINK","删除链路","DeleteLinkObj","javascript","","删除选择的设备","");
RSMenu2[8] = new RMenuRes("***","","-","","","","","");
RSMenu2[9] = new RMenuRes("device_net","MODIFYTYPE","修改设备类型","ModifyDeviceType","javascript","","修改设备类型","");
RSMenu2[10] = new RMenuRes("singlenet","MODIFYATTRIBUTE","修改属性","ModifyDeviceAttribute","javascript","","修改设备属性","");
RSMenu2[11] = new RMenuRes("singlenet","SETCUSTOMINOF","关联客户","SetCustomInfo","javascript","","关联客户信息","");
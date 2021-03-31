package com.linkage.module.liposs.performance.bio;

import java.util.HashMap;

/**
 * 性能配置定义常量
 * @author Administrator
 *
 */
public class PMEEGlobal {
	/**
	 * 配置结果
	 * @return
	 * <ul>
	 * 		<li><font color='blue'>key=-1:</font>value=超时</li>
	 *      <li><font color='blue'>key=-2:</font>value=oid不支持</li>
	 *      <li><font color='blue'>key=-21:</font>value=有一个oid采集不到数据</li>
	 *      <li><font color='blue'>key=-3:</font>value=没有采集到描述信息</li>
	 *      <li><font color='blue'>key=-4:</font>value=oid采集到的索引数不一致</li>
	 *      <li><font color='blue'>key=-41:</font>value=性能和描述采集到的索引不一致</li>
	 *      <li><font color='blue'>key=-6:</font>value=表达式ID超过了999</li>
	 * </ul>
	 * 
	 */
	public final static HashMap<Integer,String> ConfigResultMap= new HashMap<Integer,String>(3);
	/**
	 * 失败原因
	 * @return
	 * <table width="150" border=1>
	 * 		<tr><th>Key</th><th>Value</th></tr>
	 * 		<tr><td>-1</td><td>没有初始化</td></tr>
	 * 		<tr><td>0</td><td>初始化失败</td></tr>
	 * 		<tr><td>1</td><td>初始化成功</td></tr>
	 * </table>
	 */
	public final static HashMap<Integer,String> ConfigFialMap=new HashMap<Integer,String>(7);
	/**
	 * 端口标识方式
	 * @return
	 * <ul>
	 * 		<li><font color='blue'>key=1:</font>value=索引</li>
	 *      <li><font color='blue'>key=2:</font>value=描述</li>
	 *      <li><font color='blue'>key=3:</font>value=名字</li>
	 *      <li><font color='blue'>key=4:</font>value=别名</li>
	 *      <li><font color='blue'>key=5:</font>value=端口IP</li>
	 * </ul>
	 */
	public final static HashMap<Integer,String> GetWayMap=new HashMap<Integer,String>(5);
	/**
	 * 是否入库
	 * @return
	 * <ul>
	 * 		<li><font color='blue'>key=0:</font>value=不入库</li>
	 *      <li><font color='blue'>key=1:</font>value=入库</li>
	 * </ul>
	 */
	public final static HashMap<Integer,String> IntoDBMap=new HashMap<Integer,String>(2);
	/**
	 * 是否采集
	 * @return
	 * <ul>
	 * 		<li><font color='blue'>key=0:</font>value=不采集</li>
	 *      <li><font color='blue'>key=1:</font>value=采集</li>
	 * </ul>
	 */
	public final static HashMap<Integer,String> GatherFlgMap=new HashMap<Integer,String>(2);
	/**
	 * 是否已经配置
	 * @return
	 *    <ul>
	 *    	<li><font color='blue'>key=0:</font>value=未配置</li>
	 *      <li><font color='blue'>key=1:</font>value=已配置</li>
	 *    </ul>
	 */
	public final static HashMap<Integer,String> IsConfigMap=new HashMap<Integer,String>(2);
	/**
	 * 操作成功
	 * @return 0
	 */
	public final static int Success=0;
	/**
	 * 操作数据库失败
	 * @return -1
	 */
	public final static int Error_DataBase=-1;
	/**
	 * 通知后台失败
	 * @return -2
	 */
	public final static int Error_Process=-2;
	static{
		IsConfigMap.put(0,"未配置");
		IsConfigMap.put(1,"已配置");
		
		GatherFlgMap.put(0,"不采集");
		GatherFlgMap.put(1,"采集");
		
		IntoDBMap.put(0,"不入库");
		IntoDBMap.put(1,"入库");
		
		GetWayMap.put(1,"索引");
		GetWayMap.put(2,"描述");
		GetWayMap.put(3,"名字");
		GetWayMap.put(4,"别名");
		GetWayMap.put(5,"端口IP");
		
		ConfigResultMap.put(-1, "没有初始化");
		ConfigResultMap.put(0, "初始化失败");
		ConfigResultMap.put(1, "初始化成功");
		
		ConfigFialMap.put(-1,"超时");
		ConfigFialMap.put(-2,"oid不支持");
		ConfigFialMap.put(-21,"有一个oid采集不到数据");
		ConfigFialMap.put(-3,"没有采集到描述信息");
		ConfigFialMap.put(-4,"oid采集到的索引数不一致");
		ConfigFialMap.put(-41,"性能和描述采集到的索引不一致");
		ConfigFialMap.put(-6,"表达式ID超过了999");
		
	}
}

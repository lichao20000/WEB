package com.linkage.module.liposs.performance.bio;


/**
 * 性能配置结构体【已经提供默认值】
 * @author Administrator
 *
 */
public class Pm_Map_Instance {
	private int interval=300;//采集时间间隔
	private int intodb=0;//是否入库
	private int collect=1;//采集
	private boolean iskeep=false;//是否保留原有配置 true保留原有配置 false 不保留原有配置
	//*********************固定阀值******************、、
	private int mintype=0;//固定阈值一类型【比较操作符一】
	private String mindesc="";//固定阈值一描述
	private float minthres=0L;//固定阈值一
	private int mincount=1;//连续超出阈值一次数
	private int minwarninglevel=0;//固定阈值一告警级别
	private int minreinstatelevel=0;//固定阈值一恢复告警级别
	private int maxtype=0;//固定阈值二类型
	private String maxdesc="";//固定阈值二描述
	private float maxthres=0L;//固定阈值二
	private int maxcount=1;//连续超出阈值二次数
	private int maxwarninglevel=0;//固定阈值二告警级别
	private int maxreinstateleve=0;//固定阈值二恢复告警级别
	//***************动态阀值**********************
	private int dynatype=0;//是否启动动态阈值类型
	private String dynadesc="";//	动态阈值描述
	private int dynacount=1;//连续超出动态阈值次数
	private int beforeday=1;//几天前的数据为基准值
	private float dynathres=0L;//动态阈值
	private int dynawarninglevel=0;//动态阈值告警级别
	private int dynareinstatelevel=0;//动态阈值告警恢复告警级别
	//*****************突变阀值********************
	private int mutationtype=0;//是否启动突变阈值类型
	private float mutationthres=0L;//突变阈值
	private int mutationwarninglevel=0;//突变阈值告警级别
	private int mutationreinstatelevel=0;//突变阈值告警恢复告警级别
	private String mutationdesc="";//突变阈值描述
	private int mutationcount=1;//连续超出突变阈值次数
	private String remark1="";//保留字段1
	private String remark2="";//保留字段2
	
	/**
	 * Pm_Map_Instance默认构造函数
	 */
	public Pm_Map_Instance(){
		
	}
	/**
	 * PM_MAP_INSTANCE默认构造方法【直接将所有字段赋值】【批量】
	 * @param ExpressionList:【性能表达式列表】存放性能表达式
	 * @param DeviceList:【设备列别表】存放设备ID
	 * @param interval:采集时间间隔
	 * @param intodb:是否入库
	 * @param mintype:固定阈值一类型【比较操作符一】
	 * @param mindesc:固定阈值一描述
	 * @param minthres:固定阈值一
	 * @param mincount:连续超出阈值一次数
	 * @param minwarninglevel:固定阈值一告警级别
	 * @param minreinstatelevel:固定阈值一恢复告警级别
	 * @param maxtype:固定阈值二类型
	 * @param maxdesc:固定阈值二描述
	 * @param maxthres:固定阈值二
	 * @param maxcount:连续超出阈值二次数
	 * @param maxwarninglevel:固定阈值二告警级别
	 * @param maxreinstateleve:固定阈值二恢复告警级别
	 * @param dynatype:是否启动动态阈值类型
	 * @param dynadesc:动态阈值描述
	 * @param dynacount:连续超出动态阈值次数
	 * @param beforeday:几天前的数据为基准值
	 * @param dynathres:动态阈值
	 * @param dynawarninglevel:动态阈值告警级别
	 * @param dynareinstatelevel:动态阈值告警恢复告警级别
	 * @param mutationtype:是否启动突变阈值类型
	 * @param mutationthres:突变阈值
	 * @param mutationwarninglevel:突变阈值告警级别
	 * @param mutationreinstatelevel:突变阈值告警恢复告警级别
	 */
	public Pm_Map_Instance(int interval,int intodb,int mintype,String mindesc,
						  float minthres,int mincount,int minwarninglevel,
						  int minreinstatelevel,int maxtype,String maxdesc,
						  float maxthres,int maxcount,int maxwarninglevel,
						  int maxreinstateleve,int dynatype,String dynadesc,
						  int dynacount,int beforeday,float dynathres,
						  int dynawarninglevel,int dynareinstatelevel,
						  int mutationtype, float mutationthres,String mutationdesc,int mutationcount,int mutationwarninglevel,
						  int mutationreinstatelevel){
		setInterval(interval);
		setIntodb(intodb);
		//配置固定阈值
		setFixThres(mintype, mindesc, minthres, mincount, minwarninglevel, minreinstatelevel, 
				maxtype, maxdesc, maxthres, maxcount, maxwarninglevel, maxreinstateleve);
		//配置动态阈值
		setDynamicThres(dynatype, dynadesc, dynacount, beforeday, 
				dynathres, dynawarninglevel, dynareinstatelevel);
		//配置突变阈值
		setSuddenThres(mutationtype, mutationthres,mutationdesc, mutationcount,mutationwarninglevel, mutationreinstatelevel);
		setRemark1(remark1);
		setRemark2(remark2);
	}
	/**
	 * 配置动态阈值
	 * @param dynatype:是否启动动态阈值类型
	 * @param dynadesc:动态阈值描述
	 * @param dynacount:连续超出动态阈值次数
	 * @param beforeday:几天前的数据为基准值
	 * @param dynathres:动态阈值
	 * @param dynawarninglevel:动态阈值告警级别
	 * @param dynareinstatelevel:动态阈值告警恢复告警级别
	 */
	public void setDynamicThres(int dynatype,String dynadesc,int dynacount,int beforeday,
			float dynathres,int dynawarninglevel,int dynareinstatelevel){
		setDynatype(dynatype);
		setDynadesc(dynadesc);
		setDynacount(dynacount);
		setBeforeday(beforeday);
		setDynathres(dynathres);
		setDynawarninglevel(dynawarninglevel);
		setDynareinstatelevel(dynareinstatelevel);
	}
	/**
	 * 配置突变阈值
	 * @param mutationtype:是否启动突变阈值类型
	 * @param mutationthres:突变阈值
	 * @param mutationwarninglevel:突变阈值告警级别
	 * @param mutationreinstatelevel:突变阈值告警恢复告警级别
	 */
	public void setSuddenThres(int mutationtype,float mutationthres,String mutationdesc,int mutationcount,int mutationwarninglevel,int mutationreinstatelevel){
		setMutationtype(mutationtype);
		setMutationthres(mutationthres);
		setMutationdesc(mutationdesc);
		setMutationcount(mutationcount);
		setMutationwarninglevel(mutationwarninglevel);
		setMutationreinstatelevel(mutationreinstatelevel);
	}
	/**
	 * 配置固定阈值
	 * @param mintype:固定阈值一类型【比较操作符一】
	 * @param mindesc:固定阈值一描述
	 * @param minthres:固定阈值一
	 * @param mincount:连续超出阈值一次数
	 * @param minwarninglevel:固定阈值一告警级别
	 * @param minreinstatelevel:固定阈值一恢复告警级别
	 * @param maxtype:固定阈值二类型
	 * @param maxdesc:固定阈值二描述
	 * @param maxthres:固定阈值二
	 * @param maxcount:连续超出阈值二次数
	 * @param maxwarninglevel:固定阈值二告警级别
	 * @param maxreinstateleve:固定阈值二恢复告警级别
	 */
	public void setFixThres(int mintype,String mindesc,
							float minthres,int mincount,
							int minwarninglevel,int minreinstatelevel,
							int maxtype,String maxdesc,
							float maxthres,int maxcount,
							int maxwarninglevel,int maxreinstateleve){
		setMintype(mintype);
		setMindesc(mindesc);
		setMinthres(minthres);
		setMincount(mincount);
		setMinwarninglevel(minwarninglevel);
		setMinreinstatelevel(minreinstatelevel);
		setMaxtype(maxtype);
		setMaxdesc(maxdesc);
		setMaxthres(maxthres);
		setMaxcount(maxcount);
		setMaxwarninglevel(maxwarninglevel);
		setMaxreinstateleve(maxreinstateleve);
	}
	
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getIntodb() {
		return intodb;
	}
	public void setIntodb(int intodb) {
		this.intodb = intodb;
	}
	public int getMintype() {
		return mintype;
	}
	public void setMintype(int mintype) {
		this.mintype = mintype;
	}
	public String getMindesc() {
		return mindesc;
	}
	public void setMindesc(String mindesc) {
		this.mindesc = mindesc;
	}
	public float getMinthres() {
		return minthres;
	}
	public void setMinthres(float minthres) {
		this.minthres = minthres;
	}
	public int getMincount() {
		return mincount;
	}
	public void setMincount(int mincount) {
		this.mincount = mincount;
	}
	public int getMinwarninglevel() {
		return minwarninglevel;
	}
	public void setMinwarninglevel(int minwarninglevel) {
		this.minwarninglevel = minwarninglevel;
	}
	public int getMinreinstatelevel() {
		return minreinstatelevel;
	}
	public void setMinreinstatelevel(int minreinstatelevel) {
		this.minreinstatelevel = minreinstatelevel;
	}
	public int getMaxtype() {
		return maxtype;
	}
	public void setMaxtype(int maxtype) {
		this.maxtype = maxtype;
	}
	public String getMaxdesc() {
		return maxdesc;
	}
	public void setMaxdesc(String maxdesc) {
		this.maxdesc = maxdesc;
	}
	public float getMaxthres() {
		return maxthres;
	}
	public void setMaxthres(float maxthres) {
		this.maxthres = maxthres;
	}
	public int getMaxcount() {
		return maxcount;
	}
	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}
	public int getMaxwarninglevel() {
		return maxwarninglevel;
	}
	public void setMaxwarninglevel(int maxwarninglevel) {
		this.maxwarninglevel = maxwarninglevel;
	}
	public int getMaxreinstateleve() {
		return maxreinstateleve;
	}
	public void setMaxreinstateleve(int maxreinstateleve) {
		this.maxreinstateleve = maxreinstateleve;
	}
	public int getDynatype() {
		return dynatype;
	}
	public void setDynatype(int dynatype) {
		this.dynatype = dynatype;
	}
	public String getDynadesc() {
		return dynadesc;
	}
	public void setDynadesc(String dynadesc) {
		this.dynadesc = dynadesc;
	}
	public int getDynacount() {
		return dynacount;
	}
	public void setDynacount(int dynacount) {
		this.dynacount = dynacount;
	}
	public int getBeforeday() {
		return beforeday;
	}
	public void setBeforeday(int beforeday) {
		this.beforeday = beforeday;
	}
	public float getDynathres() {
		return dynathres;
	}
	public void setDynathres(float dynathres) {
		this.dynathres = dynathres;
	}
	public int getDynawarninglevel() {
		return dynawarninglevel;
	}
	public void setDynawarninglevel(int dynawarninglevel) {
		this.dynawarninglevel = dynawarninglevel;
	}
	public int getDynareinstatelevel() {
		return dynareinstatelevel;
	}
	public void setDynareinstatelevel(int dynareinstatelevel) {
		this.dynareinstatelevel = dynareinstatelevel;
	}
	public int getMutationtype() {
		return mutationtype;
	}
	public void setMutationtype(int mutationtype) {
		this.mutationtype = mutationtype;
	}
	public float getMutationthres() {
		return mutationthres;
	}
	public void setMutationthres(float mutationthres) {
		this.mutationthres = mutationthres;
	}
	public int getMutationwarninglevel() {
		return mutationwarninglevel;
	}
	public void setMutationwarninglevel(int mutationwarninglevel) {
		this.mutationwarninglevel = mutationwarninglevel;
	}
	public int getMutationreinstatelevel() {
		return mutationreinstatelevel;
	}
	public void setMutationreinstatelevel(int mutationreinstatelevel) {
		this.mutationreinstatelevel = mutationreinstatelevel;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getMutationdesc() {
		return mutationdesc;
	}
	public void setMutationdesc(String mutationdesc) {
		this.mutationdesc = mutationdesc;
	}
	public int getMutationcount() {
		return mutationcount;
	}
	public void setMutationcount(int mutationcount) {
		this.mutationcount = mutationcount;
	}
	public int getCollect()
	{
		return collect;
	}
	public void setCollect(int collect)
	{
		this.collect = collect;
	}
	public boolean isIskeep() {
		return iskeep;
	}
	public void setIskeep(boolean iskeep) {
		this.iskeep = iskeep;
	}
}

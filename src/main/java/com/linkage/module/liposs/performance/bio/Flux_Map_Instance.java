package com.linkage.module.liposs.performance.bio;

public class Flux_Map_Instance {
	private int ifinoct_maxtype=0;//端口流入利用率阈值一比较操作符
	private float ifinoctetsbps_max=-1L;//端口流入利用率阈值一(%)
	private int ifoutoct_maxtype=0;//端口流出利用率阈值一比较操作符
	private float ifoutoctetsbps_max=-1L;//端口流出利用率阈值一(%)
	private float ifindiscardspps_max=-1L;//端口流入丢包率阈值(%)
	private float ifoutdiscardspps_max=-1L;//端口流出丢包率阈值(%)
	private float ifinerrorspps_max=-1L;//端口流入错包率阈值(%)
	private float ifouterrorspps_max=-1L;//端口流出错包率阈值(%)
	private int warningnum=3;//超出阈值的次数（发告警）
	private int warninglevel=0;//发出阈值告警时的告警级别
	private int reinstatelevel=0;//恢复告警级别
	//*****************固定阈值二***************************//
	private int ifinoct_mintype=0;//端口流入利用率阈值二比较操作符
	private float ifinoctetsbps_min=-1L;//端口流入利用率阈值二(%)
	private int ifoutoct_mintype=0;//端口流出利用率阈值二比较操作符
	private float ifoutoctetsbps_min=-1L;//端口流出利用率阈值二(%)
	private int warningnum_min=3;//超出阈值二的次数（发告警）
	private int warninglevel_min=0;//发出阈值二告警时的告警级别
	private int reinlevel_min=0;//阈值二恢复告警级别
	//*****************动态阈值一************************************************************//
	private int overmax=0;//动态阈值一操作符
	private float overper=-1L;//动态阈值一(%)
	private int overnum=3;//超出动态阈值一的次数(发告警)
	private int overlevel=0;//发出动态阈值一告警时的告警级别
	private int reinoverlevel=0;//发出恢复告警时的级别
	//*****************动态阈值二*************************//
	private int overmin=0;//动态阈值二操作符
	private float overper_min=-1L;//动态阈值二(%)
	private int overnum_min=3;//超出动态阈值二次数(发告警)
	private int overlevel_min=0;//发出动态阈值二告警时的告警级别
	private int reinoverlevel_min=0;//发出恢复告警时的级别
	private int com_day=3;//生成动态阈值一的天数(天)
	//*****************突变阈值************************************************************//
	private int intbflag=0;//判断是否配置流入突变告警操作
	private float ifinoctets=-1L;//流入速率变化率阈值(%)
	private int inoperation=0;//流入速率突变告警操作符
	private int inwarninglevel=0;//流入速率突变告警级别
	private int inreinstatelevel=0;//流入速率恢复突变告警级别
	private int outtbflag=0;//是否配置流出突变告警操作
	private float ifoutoctets=-1L;//流出速率变化率阈值(%)
	private int outoperation=0;//流出速率突变告警操作符
	private int outwarninglevel=0;//流出速率突变告警级别
	private int outreinstatelevel=0;//流出速率恢复突变告警级别
	private int confignum=0;//已配阀值数量【江苏使用】
	
	private int countConfigNum(){
		int countNum = 0;
		// 端口流入带宽利用率阈值一操作符
		if(ifinoct_maxtype != 0)
		{
			countNum ++;
		}
		// 端口流出带宽利用率阈值一操作符
		if(ifoutoct_maxtype != 0 )
		{
			countNum ++;
		}
		// 端口流入丢包率阈值
		if(ifindiscardspps_max >= 0)
		{
			countNum ++;
		}
		// 端口流出丢包率阈值
		if(ifoutdiscardspps_max >= 0)
		{
			countNum ++;
		}
		// 端口流入错包率阈值
		if(ifinerrorspps_max >= 0)
		{
			countNum ++;
		}
		// 端口流出错包率阈值
		if(ifouterrorspps_max >= 0)
		{
			countNum ++;
		}
		// 端口流入利用率阈值二操作符
		if(ifinoct_mintype != 0)
		{
			countNum ++;
		}
		// 端口流出利用率阈值二操作符
		if(ifoutoct_mintype != 0)
		{
			countNum ++;
		}
		// 动态阈值一操作符
		if(overmax != 0)
		{
			countNum ++;
		}
		// 动态阈值二操作符
		if(overmin != 0)
		{
			countNum ++;
		}
		// 判断是否配置流入突变告警操作
		if(intbflag != 0)
		{
			countNum ++;
		}
		// 是否配置流出突变告警操作
		if(outtbflag != 0)
		{
			countNum ++;
		}
		return countNum;
	}
	/**
	 * 默认构造函数，使用默认告警配置
	 */
	public Flux_Map_Instance(){
		
	}
	/**
	 * 构造函数，初始化告警
	 * @param ifinoct_maxtype
	 * @param ifinoctetsbps_max
	 * @param ifoutoct_maxtype
	 * @param ifoutoctetsbps_max
	 * @param ifindiscardspps_max
	 * @param ifoutdiscardspps_max
	 * @param ifinerrorspps_max
	 * @param ifouterrorspps_max
	 * @param warningnum
	 * @param warninglevel
	 * @param reinstatelevel
	 * @param ifinoct_mintype
	 * @param ifinoctetsbps_min
	 * @param ifoutoct_mintype
	 * @param ifoutoctetsbps_min
	 * @param warningnum_min
	 * @param warninglevel_min
	 * @param reinlevel_min
	 * @param overmax
	 * @param overper
	 * @param overnum
	 * @param overlevel
	 * @param reinoverlevel
	 * @param overmin
	 * @param overper_min
	 * @param overnum_min
	 * @param overlevel_min
	 * @param reinoverlevel_min
	 * @param com_day
	 * @param intbflag
	 * @param ifinoctets
	 * @param inoperation
	 * @param inwarninglevel
	 * @param inreinstatelevel
	 * @param outtbflag
	 * @param ifoutoctets
	 * @param outoperation
	 * @param outwarninglevel
	 * @param outreinstatelevel
	 */
	public Flux_Map_Instance(int ifinoct_maxtype,float ifinoctetsbps_max,int ifoutoct_maxtype,float ifoutoctetsbps_max,
							 float ifindiscardspps_max,float ifoutdiscardspps_max,float ifinerrorspps_max,float ifouterrorspps_max,
							 int warningnum,int warninglevel,int reinstatelevel,int ifinoct_mintype,float ifinoctetsbps_min,
							 int ifoutoct_mintype,float ifoutoctetsbps_min,int warningnum_min,int warninglevel_min,int reinlevel_min,
							 int overmax,float overper,int overnum,int overlevel,int reinoverlevel,int overmin,float overper_min,
							 int overnum_min,int overlevel_min,int reinoverlevel_min,int com_day,int intbflag,float ifinoctets,
							 int inoperation,int inwarninglevel,int inreinstatelevel,int outtbflag,float ifoutoctets,
							 int outoperation,int outwarninglevel,int outreinstatelevel
							 ){
		setIfinoct_maxtype(ifinoct_maxtype);//端口流入利用率阈值一比较操作符
		setIfinoctetsbps_max(ifinoctetsbps_max);//端口流入利用率阈值一(%)
		setIfoutoct_maxtype(ifoutoct_maxtype);//端口流出利用率阈值一比较操作符
		setIfoutoctetsbps_max(ifoutoctetsbps_max);//端口流出利用率阈值一(%)
		setIfindiscardspps_max(ifindiscardspps_max);//端口流入丢包率阈值(%)
		setIfoutdiscardspps_max(ifoutdiscardspps_max);//端口流出丢包率阈值(%)
		setIfinerrorspps_max(ifinerrorspps_max);//端口流入错包率阈值(%)
		setIfouterrorspps_max(ifouterrorspps_max);//端口流出错包率阈值(%)
		setWarningnum(warningnum);//超出阈值的次数（发告警）
		setWarninglevel(warninglevel);//发出阈值告警时的告警级别
		setReinstatelevel(reinstatelevel);//恢复告警级别
		//*****************固定阈值二***************************
		setIfinoct_mintype(ifinoct_mintype);//端口流入利用率阈值二比较操作符
		setIfinoctetsbps_min(ifinoctetsbps_min);//端口流入利用率阈值二
		setIfoutoct_mintype(ifoutoct_mintype);//端口流出利用率阈值二比较操作符
		setIfoutoctetsbps_min(ifoutoctetsbps_min);//端口流出利用率阈值二
		setWarningnum_min(warningnum_min);//超出阈值二的次数（发告警）
		setWarninglevel_min(warninglevel_min);//发出阈值二告警时的告警级别
		setReinlevel_min(reinlevel_min);//阈值二恢复告警级别
		//*****************动态阈值一************************************************************
		setOvermax(overmax);//动态阈值一操作符
		setOverper(overper);//动态阈值一
		setOvernum(overnum);//超出动态阈值一的次数
		setOverlevel(overlevel);//发出动态阈值一告警时的告警级别
		setReinoverlevel(reinoverlevel);//发出恢复告警时的级别
        //*****************动态阈值二*************************
		setOvermin(overmin);//动态阈值二操作符
		setOverper_min(overper_min);//动态阈值二
		setOvernum_min(overnum_min);//超出动态阈值二次数
		setOverlevel_min(overlevel_min);//发出动态阈值二告警时的告警级别
		setReinoverlevel_min(reinoverlevel_min);//发出恢复告警时的级别
		setCom_day(com_day);//生成动态阈值一的天数
		//*****************突变阈值************************************************************
		setIntbflag(intbflag);//判断是否配置流入突变告警操作
		setIfinoctets(ifinoctets);//流入速率变化率阈值
		setInoperation(inoperation);//流入速率突变告警操作符
		setInwarninglevel(inwarninglevel);//流入速率突变告警级别
		setInreinstatelevel(inreinstatelevel);//流入速率恢复突变告警级别
		setOuttbflag(outtbflag);//是否配置流出突变告警操作
		setIfoutoctets(ifoutoctets);//流出速率变化率阈值
		setOutoperation(outoperation);//流出速率突变告警操作符
		setOutwarninglevel(outwarninglevel);//流出速率突变告警级别
		setOutreinstatelevel(outreinstatelevel);//流出速率恢复突变告警级别
		//计算阀值数量
		setConfignum(countConfigNum());
	}
	
	public int getIfinoct_maxtype() {
		return ifinoct_maxtype;
	}
	public void setIfinoct_maxtype(int ifinoct_maxtype) {
		this.ifinoct_maxtype = ifinoct_maxtype;
	}
	public float getIfinoctetsbps_max() {
		return ifinoctetsbps_max;
	}
	public void setIfinoctetsbps_max(float ifinoctetsbps_max) {
		this.ifinoctetsbps_max = ifinoctetsbps_max;
	}
	public int getIfoutoct_maxtype() {
		return ifoutoct_maxtype;
	}
	public void setIfoutoct_maxtype(int ifoutoct_maxtype) {
		this.ifoutoct_maxtype = ifoutoct_maxtype;
	}
	public float getIfoutoctetsbps_max() {
		return ifoutoctetsbps_max;
	}
	public void setIfoutoctetsbps_max(float ifoutoctetsbps_max) {
		this.ifoutoctetsbps_max = ifoutoctetsbps_max;
	}
	public float getIfindiscardspps_max() {
		return ifindiscardspps_max;
	}
	public void setIfindiscardspps_max(float ifindiscardspps_max) {
		this.ifindiscardspps_max = ifindiscardspps_max;
	}
	public float getIfoutdiscardspps_max() {
		return ifoutdiscardspps_max;
	}
	public void setIfoutdiscardspps_max(float ifoutdiscardspps_max) {
		this.ifoutdiscardspps_max = ifoutdiscardspps_max;
	}
	public float getIfinerrorspps_max() {
		return ifinerrorspps_max;
	}
	public void setIfinerrorspps_max(float ifinerrorspps_max) {
		this.ifinerrorspps_max = ifinerrorspps_max;
	}
	public float getIfouterrorspps_max() {
		return ifouterrorspps_max;
	}
	public void setIfouterrorspps_max(float ifouterrorspps_max) {
		this.ifouterrorspps_max = ifouterrorspps_max;
	}
	public int getWarningnum() {
		return warningnum;
	}
	public void setWarningnum(int warningnum) {
		this.warningnum = warningnum;
	}
	public int getWarninglevel() {
		return warninglevel;
	}
	public void setWarninglevel(int warninglevel) {
		this.warninglevel = warninglevel;
	}
	public int getReinstatelevel() {
		return reinstatelevel;
	}
	public void setReinstatelevel(int reinstatelevel) {
		this.reinstatelevel = reinstatelevel;
	}
	public int getIfinoct_mintype() {
		return ifinoct_mintype;
	}
	public void setIfinoct_mintype(int ifinoct_mintype) {
		this.ifinoct_mintype = ifinoct_mintype;
	}
	public float getIfinoctetsbps_min() {
		return ifinoctetsbps_min;
	}
	public void setIfinoctetsbps_min(float ifinoctetsbps_min) {
		this.ifinoctetsbps_min = ifinoctetsbps_min;
	}
	public int getIfoutoct_mintype() {
		return ifoutoct_mintype;
	}
	public void setIfoutoct_mintype(int ifoutoct_mintype) {
		this.ifoutoct_mintype = ifoutoct_mintype;
	}
	public float getIfoutoctetsbps_min() {
		return ifoutoctetsbps_min;
	}
	public void setIfoutoctetsbps_min(float ifoutoctetsbps_min) {
		this.ifoutoctetsbps_min = ifoutoctetsbps_min;
	}
	public int getWarningnum_min() {
		return warningnum_min;
	}
	public void setWarningnum_min(int warningnum_min) {
		this.warningnum_min = warningnum_min;
	}
	public int getWarninglevel_min() {
		return warninglevel_min;
	}
	public void setWarninglevel_min(int warninglevel_min) {
		this.warninglevel_min = warninglevel_min;
	}
	public int getReinlevel_min() {
		return reinlevel_min;
	}
	public void setReinlevel_min(int reinlevel_min) {
		this.reinlevel_min = reinlevel_min;
	}
	public int getOvermax() {
		return overmax;
	}
	public void setOvermax(int overmax) {
		this.overmax = overmax;
	}
	public float getOverper() {
		return overper;
	}
	public void setOverper(float overper) {
		this.overper = overper;
	}
	public int getOvernum() {
		return overnum;
	}
	public void setOvernum(int overnum) {
		this.overnum = overnum;
	}
	public int getOverlevel() {
		return overlevel;
	}
	public void setOverlevel(int overlevel) {
		this.overlevel = overlevel;
	}
	public int getReinoverlevel() {
		return reinoverlevel;
	}
	public void setReinoverlevel(int reinoverlevel) {
		this.reinoverlevel = reinoverlevel;
	}
	public int getOvermin() {
		return overmin;
	}
	public void setOvermin(int overmin) {
		this.overmin = overmin;
	}
	public float getOverper_min() {
		return overper_min;
	}
	public void setOverper_min(float overper_min) {
		this.overper_min = overper_min;
	}
	public int getOvernum_min() {
		return overnum_min;
	}
	public void setOvernum_min(int overnum_min) {
		this.overnum_min = overnum_min;
	}
	public int getOverlevel_min() {
		return overlevel_min;
	}
	public void setOverlevel_min(int overlevel_min) {
		this.overlevel_min = overlevel_min;
	}
	public int getReinoverlevel_min() {
		return reinoverlevel_min;
	}
	public void setReinoverlevel_min(int reinoverlevel_min) {
		this.reinoverlevel_min = reinoverlevel_min;
	}
	public int getCom_day() {
		return com_day;
	}
	public void setCom_day(int com_day) {
		this.com_day = com_day;
	}
	public int getIntbflag() {
		return intbflag;
	}
	public void setIntbflag(int intbflag) {
		this.intbflag = intbflag;
	}
	public float getIfinoctets() {
		return ifinoctets;
	}
	public void setIfinoctets(float ifinoctets) {
		this.ifinoctets = ifinoctets;
	}
	public int getInoperation() {
		return inoperation;
	}
	public void setInoperation(int inoperation) {
		this.inoperation = inoperation;
	}
	public int getInwarninglevel() {
		return inwarninglevel;
	}
	public void setInwarninglevel(int inwarninglevel) {
		this.inwarninglevel = inwarninglevel;
	}
	public int getInreinstatelevel() {
		return inreinstatelevel;
	}
	public void setInreinstatelevel(int inreinstatelevel) {
		this.inreinstatelevel = inreinstatelevel;
	}
	public int getOuttbflag() {
		return outtbflag;
	}
	public void setOuttbflag(int outtbflag) {
		this.outtbflag = outtbflag;
	}
	public float getIfoutoctets() {
		return ifoutoctets;
	}
	public void setIfoutoctets(float ifoutoctets) {
		this.ifoutoctets = ifoutoctets;
	}
	public int getOutoperation() {
		return outoperation;
	}
	public void setOutoperation(int outoperation) {
		this.outoperation = outoperation;
	}
	public int getOutwarninglevel() {
		return outwarninglevel;
	}
	public void setOutwarninglevel(int outwarninglevel) {
		this.outwarninglevel = outwarninglevel;
	}
	public int getOutreinstatelevel() {
		return outreinstatelevel;
	}
	public void setOutreinstatelevel(int outreinstatelevel) {
		this.outreinstatelevel = outreinstatelevel;
	}
	public int getConfignum() {
		return confignum;
	}
	public void setConfignum(int confignum) {
		this.confignum = confignum;
	}
}

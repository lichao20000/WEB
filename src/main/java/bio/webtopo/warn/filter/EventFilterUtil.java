package bio.webtopo.warn.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.AlarmEvent;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-9
 * @category com.linkage.liposs.bio.webtopo.warn 版权：南京联创科技 网管科技部
 * 
 */
public class EventFilterUtil {
    /**
     * 告警明细规则id
     */
    private int rule_id = 0;
    /**
     * 过滤类型 符合规则 1：显示告警(默认值) 0：不显示告警（过滤掉）
     */
    private boolean filterType = true;

    /**
     * 加载规则sql
     */
    private final static String sqlRuleDetail = "select a.rule_content,a.rule_priority,b.visible from tab_warn_filterdetail a,tab_warn_filterrule b where a.rule_id=b.rule_id and a.rule_id=? and rule_invocation=1 order by rule_priority";

    /**
     * 原始告警过滤器
     */
    private BaseFilter eventFilter = ConstantEventEnv.DefaultLevelFilter;
    /**
     * 清除告警过滤器
     */
    private BaseFilter clearEventFilter = ConstantEventEnv.TRUEFILTER;

    private static Logger log = LoggerFactory.getLogger(EventFilterUtil.class);

    /**
     * 
     * @param rule_id
     */
    public EventFilterUtil(int rule_id) {
	this.rule_id = rule_id;
    }
    /**
     * 加载过滤过滤规则明细,不包括规则模版中其他数据（即：过滤类型）
     * 
     * @return 加载成功与否
     */
    public boolean loadRule() {
	Cursor cursor = null;//new Cursor();
	String tempSql = null;
	Map fields = null;
	
	// rule_id = -2 说明是WEBTOPO加载的默认过滤规则,只展示4,5两个等级的告警
	if (rule_id == -2)
	{
		setEventFilter(ConstantEventEnv.WebTopoFilter);
		setClearEventFilter(ConstantEventEnv.TRUEFILTER);
		filterType = true;
		log.debug("加载WEBTOPO默认过滤规则,只展示4,5两个等级的告警  rule_id=" + rule_id);
		return true;
	}
	
	tempSql = sqlRuleDetail.replaceAll("\\?", "" + (rule_id));
	log.debug("加载过滤规则:" + tempSql);
	PrepareSQL psql = new PrepareSQL(tempSql);
	cursor = DataSetBean.getCursor(psql.getSQL());
	// 如果数据中无规则明细，则加载失败
	if (cursor == null || cursor.getRecordSize() == 0) {
	    log.debug("数据库中无过滤规则明细rule_id=" + rule_id);
	    
	    setEventFilter(ConstantEventEnv.DefaultLevelFilter);
	    setClearEventFilter(ConstantEventEnv.TRUEFILTER);
	    filterType = true;
	    
	    log.debug(toString());
	    return true;
	}
	fields = cursor.getNext();

	// 规则中最小定义单元 设备名称=思科设备
	RuleDetail rule = null;
	// 规则明细中规则内容
	String rule_content = null;
	// 通过or 分割的最小定义单元
	String[] result = null;
	//对应规则模版中 visible字段
	String visible = null;
	
	// 每行规则 链表
	List<BaseFilter> andList = new ArrayList<BaseFilter>(cursor.getRecordSize());
	List<BaseFilter> andListForClearEvent = new ArrayList<BaseFilter>(cursor.getRecordSize());
	
	BaseFilter baseFilter = null;
	try {
	    // 遍历每行的规则 and
	    while (fields != null) {
		rule_content = (String) fields.get("rule_content");
		rule_content = rule_content.trim();
		visible = (String) fields.get("visible");
		
		result = rule_content.split("(\\s*or\\s*)");
		log.debug(rule_content + "==>>split or 后" + Arrays.asList(result));
		if (result != null && result.length > 0) {
		    // 规则中没有
		    List<BaseFilter> orList = new ArrayList<BaseFilter>(result.length);
		    List<BaseFilter> orListForClearEvent = new ArrayList<BaseFilter>(result.length);
		    String[] ruleObj = null;
		    // 遍历规则内容中每组规则 or
		    for (int i = 0; i < result.length; i++) {
			// 格式化规则 分解
			ruleObj = ConstantEventEnv.formatCotont(result[i]);
			if (ruleObj != null && ruleObj.length == 3) {
			    rule = new RuleDetail();
			    rule.setKey(ruleObj[0]);
			    rule.setOperation(ruleObj[1]);
			    rule.setValue(ruleObj[2]);
			    baseFilter = FilterFactory.buildFilter(rule);
			    
			    orList.add(baseFilter);
			    if (baseFilter instanceof LevelFilter) {
			    }else{
				orListForClearEvent.add(baseFilter);
			    }
			}
		    }
		    ///////////////////////构建原始告警过滤器/////////////////
		    //orList不为空才能加入到andList列表中
		    if(orList.size() != 0){
			baseFilter = getOrLogicTree(orList, orList.size() - 1, ConstantEventEnv.FALSEFILTER);
			if(baseFilter != null)
			// 将content中组装成一个BaseFilter对象
			    andList.add(baseFilter);
			else
			    log.debug("andList.add null" );
		    }
		    /////////////////////////构建清除告警过滤器/////////////////
		    if(orListForClearEvent.size() != 0){
			baseFilter = getOrLogicTree(orListForClearEvent, orListForClearEvent.size() - 1, ConstantEventEnv.FALSEFILTER);
			if(baseFilter != null)
			    andListForClearEvent.add(baseFilter);
			else
			    log.debug("andListForClearEvent.add null" );
		    }
		    
		    if (orList != null)
			orList.clear();
		    if (orListForClearEvent != null)
			orListForClearEvent.clear();
		    orList = null;
		}
		fields = cursor.getNext();
	    }
	    
	    // 将每行的规则组装成一个BaseFilter对象
	    baseFilter = getAndLogicTree(andList, andList.size() - 1, ConstantEventEnv.TRUEFILTER);
	    if(baseFilter != null)
		setEventFilter(baseFilter);
	    else//如果构建的原始告警过滤器对象为NULL，则使用一般告警过滤器
		setEventFilter(ConstantEventEnv.DefaultLevelFilter);
	    baseFilter = getAndLogicTree(andListForClearEvent, andListForClearEvent.size() - 1, ConstantEventEnv.TRUEFILTER);
	    if(baseFilter != null)
		setClearEventFilter(baseFilter);
	    else//如果构建的清除告警过滤器对象为NULL，则使用TRUEFILTER过滤器
		setClearEventFilter(ConstantEventEnv.TRUEFILTER);
	    //从数据库读取visible字段 设置过滤器类型
	    setFilterType("1".equals(visible));
	} catch (Exception e) {
	    log.debug("加载过滤规则失败：" + e.getMessage());
	    return false;
	} finally {
	    cursor = null;
	    if (fields != null)
		fields.clear();
	    andList.clear();
	    andList = null;
	    andListForClearEvent.clear();
	    andListForClearEvent = null;
	}
	log.debug(toString());
	
	return true;
    }
    /**
     * 获取原始告警过滤器对象
     * 
     * @return
     */
    public BaseFilter getEventFilter() {
	return eventFilter;
    }

    /**
     * 设置原始告警过滤器对象
     * 
     * @param eventFilter
     */
    public void setEventFilter(BaseFilter eventFilter) {
	log.debug("注入eventFilter=" + eventFilter);
	this.eventFilter = eventFilter;
    }

    public BaseFilter getClearEventFilter() {
	return clearEventFilter;
    }

    public void setClearEventFilter(BaseFilter clearEventFilter) {
	log.debug("注入clearEventFilter=" + clearEventFilter);
	this.clearEventFilter = clearEventFilter;
    }

    /**
     * 将链表中顺序存放BaseFilter类，构建成迭代二叉树（使用OrLogic装饰BaseFilter）
     * @param list  BaseFilter对象链表
     * @param index 链表索引，调用时index为list的最后一个元素索引
     * @param right 二叉树右边的过滤器对象
     * @return 
     * 		返回装饰过的过滤器对象,如果list列表为空则返回NULL过滤器对象
     * @throws
     * 		如果传入的列表中NULL或者 index >= list.size 时，抛出越界异常
     */
    private BaseFilter getOrLogicTree(List<BaseFilter> list, int index,
	    BaseFilter right) {
	
	if (list == null)
	    throw new NullPointerException("传入的过滤链表为空！！");
	if (index < 0 || index >= list.size()){
	    throw new ArrayIndexOutOfBoundsException("index值超出list下标范围！");
	}
	if (list.size() == 0)
	    return null;

	if (index == 0) {
	    return new OrLogic(list.get(0), right);
	}

	BaseFilter left = list.get(index);
	BaseFilter logic = new OrLogic(left, right);

	return getOrLogicTree(list, --index, logic);
    }

    /**
     * 将链表中顺序存放BaseFilter类，构建成迭代二叉树（使用AndLogic装饰BaseFilter）
     * @param list  BaseFilter对象链表
     * @param index 链表索引，调用时index为list的最后一个元素索引
     * @param right 二叉树右边的过滤器对象
     * @return 返回装饰过的过滤器对象,如果list列表为空则返回NULL过滤器对象
     * @throws
     * 		如果传入的列表中NULL或者 index >= list.size 时，抛出越界异常
     */
    private BaseFilter getAndLogicTree(List<BaseFilter> list, int index,
	    BaseFilter right) {
	if (list == null)
	    throw new NullPointerException("传入的过滤链表为空！！");
	if (index >= list.size())
	    throw new ArrayIndexOutOfBoundsException("index值超出list下标范围！");
	if (list.size() == 0)
	    return null;

	if (index == 0) {
	    return new AndLogic(list.get(0), right);
	}

	BaseFilter left = list.get(index);
	BaseFilter logic = new AndLogic(left, right);

	return getAndLogicTree(list, --index, logic);
    }

    /**
     * 设置过滤类型 如果过滤类型为false，则需要反转过滤器
     * 
     * @param filterType
     */
    private void setFilterType(boolean filterType) {
	log.debug("设置过滤器类型：visible=" + filterType);
	// 如果设置false需要相反的过滤器装饰
	if (filterType == false) {
	    //如果过滤器对象是DefaultLevelFilter，则不需要反转
	    if(eventFilter != ConstantEventEnv.DefaultLevelFilter){
		this.setEventFilter(new InverseBaseFilter(eventFilter));
	    }
	    else
		log.debug("原始告警过滤器为TRUEFILTER，不需要反转");
	    //如果过滤器对象是TRUEFILTER，则不需要反转
	    if(clearEventFilter != ConstantEventEnv.TRUEFILTER)
		this.setClearEventFilter(new InverseBaseFilter(clearEventFilter));
	    else
		log.debug("清除告警过滤器为TRUEFILTER，不需要反转");
	}
	
	 this.filterType = filterType;
    }
    /**
     * 相反过滤类
     * 
     * @author 何茂才
     * 
     */
    private class InverseBaseFilter implements BaseFilter {
	private BaseFilter filter = null;

	public InverseBaseFilter(BaseFilter f) {
	    if (f == null)
		throw new NullPointerException("Filter对象不能空!!!");

	    filter = f;
	    
	    log.debug("反转装饰:" + f);
	}

	public boolean accept(AlarmEvent event) {
	    return !filter.accept(event);
	}
	
	public String toString(){
	    return "!" + filter.toString();
	}
    }

    public static void main(String[] args) {
	EventFilterUtil filter = new EventFilterUtil(1);
	filter.loadRule();
	log.debug(filter.getEventFilter().toString());
	log.debug(filter.getClearEventFilter().toString());
    }

    public int getRule_id() {
	return rule_id;
    }
    
    public String toString(){
	try{
	    return "原始告警过滤器：" + this.getEventFilter().toString() + "\n清除告警过滤器：" + this.getClearEventFilter().toString() + " 过滤类型visible=" + filterType;
	}catch(Exception e){
	    log.debug(e.getMessage());
	}
	return "异常：event=" + this.getEventFilter() + " clear=" + this.getClearEventFilter();
    }
}

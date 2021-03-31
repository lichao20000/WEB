package bio.webtopo.warn.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-10
 * @category com.linkage.liposs.bio.webtopo.warn.filter 版权：南京联创科技 网管科技部
 * 
 */
public class CacheBaseFilter {
    private static CacheBaseFilter instance = new CacheBaseFilter();
    private static Logger log = LoggerFactory.getLogger(CacheBaseFilter.class);
    /**
     * <li>告警过滤器缓存容器
     * <li>主键key:acc_loginname_ruleid value值：BaseFilter对象实例
     */
    private final static Map<Integer, EventFilterUtil> cacheFilter = new HashMap<Integer, EventFilterUtil>();
    
    private ReadWriteLock CacheLock = new ReentrantReadWriteLock();

    private CacheBaseFilter() {
    }

    /**
     * 单件模式获取缓存对象
     * 
     * @return
     */
    public static CacheBaseFilter getInstance() {
	return instance;
    }

    /**
     * 将告警过滤对象加入到缓存中
     * 
     * @param filteUtil
     */
    private void putFilter(EventFilterUtil filteUtil) {
	try{
	    CacheLock.writeLock().lock();
	    cacheFilter.put(filteUtil.getRule_id(), filteUtil);
	}finally{
	    CacheLock.writeLock().unlock();
	}
    }

    /**
     * 将告警过滤对象加入到缓存中
     * 
     * @param filteUtil
     */
    private EventFilterUtil getFilter(int rule_id) {
	try{
	    CacheLock.readLock().lock();
	    return cacheFilter.get(rule_id);
	}finally{
	    CacheLock.readLock().unlock();
	}
	
    }
    /**
     * 将告警过滤对象从缓存中移出
     * @param rule_id
     * @return
     */
    public EventFilterUtil delFilter(int rule_id){
	try{
	    CacheLock.writeLock().lock();
	    return cacheFilter.remove(rule_id);
	}finally{
	    CacheLock.writeLock().unlock();
	}
    }
    /**
     * 根据rule_id获取对应的告警过滤器对象，如果缓存中没有则读取数据库规则明细加载过滤规则，并加入到缓冲中 <b>需要同步，方式多次加载规则</b>
     * 
     * @param rule_id
     *                规则过滤对象rule_id
     * @param reload
     *                是否需要重新加载
     */
    private synchronized EventFilterUtil reloadFilter(int rule_id) {
	EventFilterUtil filterUtil = null;
	// 从缓冲区中获取过滤器
	filterUtil = getFilter(rule_id);
	// 如果缓存区中不存在
	if (filterUtil == null) {
	    filterUtil = new EventFilterUtil(rule_id);
	    // 加入到缓存中
	    putFilter(filterUtil);
	}
	 // 如果不为空，则调用重新加载过则规则，如果加载失败
	if (!filterUtil.loadRule()) {
	    log.debug("规则明细加载失败");
	}
	return filterUtil;
    }
    /**
     * 加载规则
     * @param rule_id
     * @return 成功 或者 失败
     */
    public boolean reload(int rule_id){
	return reloadFilter(rule_id) != null;
    }
    /**
     * 前台界面获取规则过滤器对象
     * @param rule_id
     * @return
     */
    public EventFilterUtil getFilterUtil(int rule_id){
	EventFilterUtil filterUtil = getFilter(rule_id);
	//如果map中有此对象，则直接返回
	if(filterUtil != null)
	    return filterUtil;
	else{
	    log.debug("重新加载,因缓存中没有获取到过滤器rule_id=" + rule_id);
	    //则加载规则，返回过滤器对象
	    return reloadFilter(rule_id);
	}
    }
    /**
     * 过滤规则配置时改变模板过滤类型需要通知 此对象 设置过滤类型(屏蔽了用户能随时设置过滤器类型)
     * 
     * @param rule_id
     *                规则id
     * @param show
     *                true：显示告警 false：过滤告警
     */
    private void setFilterType(int rule_id, boolean visible) {
	// 从缓冲区中获取过滤器
	EventFilterUtil filterUtil = getFilterUtil(rule_id);
	if (filterUtil != null) {
	    //filterUtil.setFilterType(visible);
	} else
	    log.debug("找不到过滤规则对象rule_id=" + rule_id);
    }
    /**
     * 删除规则模版
     * @param rule_id
     * @return
     */
    public boolean delFilterUtil(int rule_id){
	if(delFilter(rule_id) == null)
	    log.debug("缓存中没缓存.rule_id=" + rule_id);
	return true;
    }
}

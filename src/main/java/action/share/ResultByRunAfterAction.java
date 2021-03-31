/**
 * 
 */
package action.share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-3-30
 * @category action.share
 * 
 */
public class ResultByRunAfterAction {
	
	/** log */
	private Logger logger = LoggerFactory.getLogger(ResultByRunAfterAction.class);
	
	/*
	 * 需要返回的页面
	 */
	private String goback = null;
	
	/*
	 * 查看结果的页面
	 */
	private String gobefore = null;
	
	/**
	 * @category 默认的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		
		logger.debug("execute()");
		
		logger.debug("goback:{}",goback);
		
		logger.debug("gobefore:{}",gobefore);
		
		return "result";
	}

	/**
	 * @return the goback
	 */
	public String getGoback() {
		return goback;
	}

	/**
	 * @param goback the goback to set
	 */
	public void setGoback(String goback) {
		this.goback = goback;
	}

	/**
	 * @return the gobefore
	 */
	public String getGobefore() {
		return gobefore;
	}

	/**
	 * @param gobefore the gobefore to set
	 */
	public void setGobefore(String gobefore) {
		this.gobefore = gobefore;
	}
	
}

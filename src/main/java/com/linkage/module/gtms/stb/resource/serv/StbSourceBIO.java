/**
 * 
 */
package com.linkage.module.gtms.stb.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.resource.dao.StbBidChageDAO;
import com.linkage.module.gtms.stb.resource.dao.StbSourceDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.share.dao.GwVendorModelVersionDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.bio
 * 
 */

@SuppressWarnings("rawtypes")
public class StbSourceBIO {
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StbSourceBIO.class);
	private StbSourceDAO dao = null;
	
	public List qryStbSource(String loid, String devSn, String mac,String netUsername,String servAccount,String stbDevSn,String stbMac,
			int currPage,int numPage){
		
		logger.warn("StbSourceBIO ==> qryStbSource");
		logger.warn("param:{},{},{},{},{},{},{}",new Object[]{loid,devSn,mac,netUsername,servAccount,stbDevSn,stbMac});
		return dao.qryStbSource(loid, devSn, mac, netUsername, servAccount, stbDevSn, stbMac,currPage,numPage);
	}
	
	public int qryStbResCount(String loid, String devSn, String mac,String netUsername,String servAccount,String stbDevSn,String stbMac){
		return dao.qryStbResCount(loid, devSn, mac, netUsername, servAccount, stbDevSn, stbMac);
	}
	
	public StbSourceDAO getDao()
	{
		return dao;
	}
	public void setDao(StbSourceDAO dao)
	{
		this.dao = dao;
	}
}
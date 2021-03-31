
package com.linkage.module.gwms.blocTest.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.module.gwms.blocTest.bio.EgwExpertBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-8-10 下午02:12:31
 * @category com.linkage.module.gwms.blocTest.act
 * @copyright 南京联创科技 网管科技部
 */
public class EgwExpertACT implements SessionAware
{

	private Map session;
	private EgwExpertBIO bio;
	// 查处的记录集合
	private List egwExperList;
	// TR069诊断_BBMS_标准值表id
	private int ex_id;
	private String ex_regular;
	private String ex_name;
	private String ex_bias;
	private String ex_succ_desc;
	private String ex_fault_desc;
	private String ex_suggest;
	private String ex_desc;
	private String ajax;
	// 查处的一条记录Map
	private Map egwExpertMap;

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public Map getEgwExpertMap()
	{
		return egwExpertMap;
	}

	public void setEgwExpertMap(Map egwExpertMap)
	{
		this.egwExpertMap = egwExpertMap;
	}



	
	public int getEx_id()
	{
		return ex_id;
	}

	
	public void setEx_id(int exId)
	{
		ex_id = exId;
	}

	public String getEx_regular()
	{
		return ex_regular;
	}

	public void setEx_regular(String exRegular)
	{
		ex_regular = exRegular;
	}

	public String getEx_name()
	{
		return ex_name;
	}

	public void setEx_name(String exName)
	{
		try
		{
			this.ex_name = java.net.URLDecoder.decode(exName, "UTF-8");
		}
		catch (Exception e)
		{
			this.ex_name = exName;
		}
	}

	public String getEx_bias()
	{
		return ex_bias;
	}

	public void setEx_bias(String exBias)
	{
		try
		{
			this.ex_bias = java.net.URLDecoder.decode(exBias, "UTF-8");
		}
		catch (Exception e)
		{
			this.ex_bias = exBias;
		}
	}

	public String getEx_succ_desc()
	{
		return ex_succ_desc;
	}

	public void setEx_succ_desc(String exSuccDesc)
	{
		try
		{
			this.ex_succ_desc = java.net.URLDecoder.decode(exSuccDesc, "UTF-8");
		}
		catch (Exception e)
		{
			this.ex_succ_desc = exSuccDesc;
		}
	}

	public String getEx_fault_desc()
	{
		return ex_fault_desc;
	}

	public void setEx_fault_desc(String exFaultDesc)
	{
		try
		{
			this.ex_fault_desc = java.net.URLDecoder.decode(exFaultDesc, "UTF-8");
		}
		catch (Exception e)
		{
			this.ex_fault_desc = exFaultDesc;
		}
	}

	public String getEx_suggest()
	{
		return ex_suggest;
	}

	public void setEx_suggest(String exSuggest)
	{
		try
		{
			this.ex_suggest = java.net.URLDecoder.decode(exSuggest, "UTF-8");
		}
		catch (Exception e)
		{
			this.ex_suggest = exSuggest;
		}
	}

	public String getEx_desc()
	{
		return ex_desc;
	}

	public void setEx_desc(String exDesc)
	{
		try
		{
			this.ex_desc = java.net.URLDecoder.decode(exDesc, "UTF-8");
		}
		catch (Exception e)
		{
			this.ex_desc = exDesc;
		}
	}

	public String execute()
	{
		return "success";
	}

	public String queryAll()
	{  
		egwExperList = bio.queryAll(getEx_id(), getEx_name());
		return "init";
	}

	public String query()
	{
		egwExpertMap = bio.queryOne(getEx_id());
		/**
		 * logger.warn("aaaaaaaaaa" + egwExpertMap.get("id").toString());
		 * logger.warn("ppppppppppp" + egwExpertMap.get("ex_name").toString());
		 * logger.warn("ppppppppppp" + egwExpertMap.get("ex_regular").toString());
		 * logger.warn("ppppppppppp" + egwExpertMap.get("ex_bias").toString());
		 * logger.warn("ppppppppppp" + egwExpertMap.get("ex_succ_desc").toString());
		 * logger.warn("ppppppppppp" + egwExpertMap.get("ex_fault_desc").toString());
		 * logger.warn("ppppppppppp" + egwExpertMap.get("ex_suggest").toString());
		 * logger.warn("aaaaaaaaaaa" + egwExpertMap.get("ex_desc").toString());
		 */
		return "edit";
	}

	public String edit()
	{
		int i = bio.update(ex_id, ex_regular, ex_name, ex_bias, ex_succ_desc, ex_fault_desc,
				ex_suggest, ex_desc);
		if (i > 0)
		{
			ajax = "1";
		}
		else
		{
			ajax = "0";
		}
		return "ajax";
	}

	public List getEgwExperList()
	{
		return egwExperList;
	}

	public void setEgwExperList(List egwExperList)
	{
		this.egwExperList = egwExperList;
	}

	public EgwExpertBIO getBio()
	{
		return bio;
	}

	public void setBio(EgwExpertBIO bio)
	{
		this.bio = bio;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}
}

package com.linkage.liposs.manaconf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hywhy
 * @功能：读写properties文件
 */
public class PropertiesFileManager
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PropertiesFileManager.class);
	private Properties properties;
	private InputStream in;
	private OutputStream out;
	private String filename;
	
	
	public PropertiesFileManager(String filename) 
	{
        this.filename = filename;
        properties = new Properties();
        in = getClass().getResourceAsStream(filename);
        load();
	}

	/* (non-Javadoc)
	 * @获得指定key的配置项value值
	 */
	public String getConfigItem(String key)
	{	
		return properties.getProperty(key);
	}
	

	/* (non-Javadoc)
	 * @设置配置项为value的值为value
	 */
	public void setConfigItem(String key, String value)
	{
          properties.setProperty(key, value);	
	}

	/* (non-Javadoc)
	 * @载入配置文件
	 */
	public void load() 
	{
		try
		{
		   properties.load(in);
		   in.close();
		}
		catch(IOException e)
		{
			logger.warn("the config file load faild!");
		}		
	}
	/* (non-Javadoc)
	 * @see linkagemanager.ConfigFileManager#store()
	 */
	public void store() 
	{
		String path =getClass().getResource(filename).getPath();
		try 
		{
			out = new FileOutputStream(path);
			properties.store(out,null);
		    out.close();
		}
		catch(FileNotFoundException e)
		{
			logger.warn("config.properties was not found!");
		}
		catch(IOException e)
		{
			logger.warn("the config file store faild!");
		}
	}
	public Properties getProperties()
	{
		return properties;
	}

	
	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// 测试
		PropertiesFileManager pf = new PropertiesFileManager("config.properties");
		String str = pf.getConfigItem("path");

		logger.debug(str);
	}

}


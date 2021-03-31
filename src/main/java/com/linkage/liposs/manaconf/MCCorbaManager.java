package com.linkage.liposs.manaconf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.corba.WebCorba;
import com.linkage.litms.common.util.Encoder;

/*
 * 配置管理CORBA接口管理
 * 
 */

public class MCCorbaManager
{
	//public static I_Mana_Conf.I_ImpConfIdl backupManager = null;
	public I_Mana_Conf.I_ImpConfIdl backupManager = null;
	//public  static WebCorba corba = null;
	public  WebCorba corba = null;
	private static Logger m_logger = LoggerFactory.getLogger(MCCorbaManager.class);
	//private  static boolean isBind = false;
	private  boolean isBind = false;
	private String gather_id = null;
	
	public MCCorbaManager(String gather_id)
	{
	    this.gather_id = gather_id;
	    if (backupManager == null)
	    {
	    	BindManaConf();
	    }
	}
	
	/**
	 * 与ManaConf绑定
	 *
	 */
	public boolean BindManaConf()
	{
		try 
		{
			if (corba == null)
			{
				corba = new WebCorba("ManaConf", gather_id, "0");
		    }
			backupManager = (I_Mana_Conf.I_ImpConfIdl) corba
					.getIDLCorba("ManaConf");

			m_logger.debug(backupManager.toString());
			isBind = true;
		} 
		catch (Exception ex) 
		{
			backupManager = null;
			m_logger.debug("ManaConf bind fail!");
			isBind = false;
			ex.printStackTrace();
		}

		return isBind;
	}
	/**
	 * 重新绑定corba
	 * 
	 * @return boolean
	 */
	public boolean Rebind() 
	{
		if (corba == null) 
		{
			corba = new WebCorba("ManaConf", gather_id, "0");
		}
		if (backupManager == null)
		{
			corba.refreshCorba();
			if (!BindManaConf())
				return false;
			return true;
		} 
		return true;
	}
	
	/**
	 * 通知后台程序配置改变
	 */
	public void chnageConf()
	{
		if (!Rebind())
		{
			m_logger.warn("rebind failed!");
			return;
		}
		
		m_logger.debug("chnageConf()");
		
		try
		{
            backupManager.I_ChangeConf();
		}
		catch(Exception e)
		{
			backupManager = null;
			
			if (!Rebind())
			{
				e.printStackTrace();
				return;
			}
			
			try
			{
				backupManager.I_ChangeConf();
				return;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}		
	}
	/**
	 * 及时备份
	 */
	
	public String promptBackup(int backup_type,I_Mana_Conf.Telnet_Info tel_info,I_Mana_Conf.Ftp_Info ftp_info)
	{
		String result = "您所选择的备份方式暂不支持，或者备份过程出现异常，请重新备份！";
		
		if (!Rebind())
		{
			m_logger.warn("rebind failed!");
			return result;
		}
		
		m_logger.debug("promptBackup()");
		
        //TELNET方式备份
        tel_info.m_OsVersion = Encoder.ChineseStringToAscii(tel_info.m_OsVersion);
	    tel_info.m_strCommand = Encoder.ChineseStringToAscii(tel_info.m_strCommand);
		tel_info.m_strDeviceModel = Encoder.ChineseStringToAscii(tel_info.m_strDeviceModel);
		tel_info.m_strEnableCmd = Encoder.ChineseStringToAscii(tel_info.m_strEnableCmd);
		tel_info.m_strEnablePwd = Encoder.ChineseStringToAscii(tel_info.m_strEnablePwd);
		tel_info.m_strHostPrompt = Encoder.ChineseStringToAscii(tel_info.m_strHostPrompt);
		tel_info.m_strIp = Encoder.ChineseStringToAscii(tel_info.m_strIp);
		tel_info.m_strLogOnMode = Encoder.ChineseStringToAscii(tel_info.m_strLogOnMode);
		tel_info.m_strLogOnPwd = Encoder.ChineseStringToAscii(tel_info.m_strLogOnPwd);
		tel_info.m_strLogOnUser = Encoder.ChineseStringToAscii(tel_info.m_strLogOnUser);

        //FTP方式备份
	    ftp_info.m_device_ip = Encoder.ChineseStringToAscii(ftp_info.m_device_ip);
		ftp_info.m_device_model = Encoder.ChineseStringToAscii(ftp_info.m_device_model);
		ftp_info.m_file_dir = Encoder.ChineseStringToAscii(ftp_info.m_file_dir);
		ftp_info.m_os_version = Encoder.ChineseStringToAscii(ftp_info.m_os_version);
		ftp_info.m_ftp_dir = Encoder.ChineseStringToAscii(ftp_info.m_ftp_dir);
		ftp_info.m_ftp_pwd = Encoder.ChineseStringToAscii(ftp_info.m_ftp_pwd);
		ftp_info.m_ftp_user = Encoder.ChineseStringToAscii(ftp_info.m_ftp_user);
		ftp_info.m_ftp_filename = Encoder.ChineseStringToAscii(ftp_info.m_ftp_filename);

        
		try
		{
           result = backupManager.I_PromptBackup(backup_type, tel_info, ftp_info);
		}
		catch(Exception e)
		{
			backupManager = null;
			
			if (!Rebind())
			{
				e.printStackTrace();
				return result;
			}
			
			try
			{
	           result = backupManager.I_PromptBackup(backup_type, tel_info, ftp_info);
			   return result;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				return result;
			}
		}
		
		return result;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

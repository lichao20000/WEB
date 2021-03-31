package com.linkage.litms.webtopo.warn;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MyFile
{
  //记录MasterControl重起时间的日志
  public static String RebootLogFile = "logs/reboot.log";
  public static String LOG = "logs/current.log";
  public MyFile()
  {
  }

  public static void createFolder(String FolderName)
   {
     File FileDs=new File(FolderName);
     try
     {
       if(!FileDs.exists())
       {
         FileDs.mkdir();
       }
     }
     catch(Exception e)
     {
       e.printStackTrace();
     }

   }

  /**
     * 写日志
     * @param str
     */
    public static void writeStr(BufferedWriter Writer, String str) {
      SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date now = new Date();
      String nowTime = df1.format(now);
      try {
        Writer.write(nowTime + " " + str);
        Writer.newLine();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    public static void WriteXml(BufferedWriter Writer,String str)
    {
      try
      {
        Writer.write(str);
        Writer.newLine();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }

    }

  //如果文件存在，则删除后并重新创建；如果不存在，则创建。
  public static void createOrClearFile(String FileName)
  {
    java.io.File FileDS = new java.io.File(FileName);
    try
    {
      if (FileDS.exists() == true)
      {
        //如果文件存在并且文件不为空，则给文件重命名
        if(FileDS.length() > 0)
        {
          for(int i=0; i<i+1; i++)
          {
            String FileNameTemp = FileName.trim()+String.valueOf(i);
            java.io.File FileTemp = new java.io.File(FileNameTemp);
            if(FileTemp.exists() == false)
            {
              FileDS.renameTo(FileTemp);
              break;
            }
          }
        }
        FileDS = new java.io.File(FileName);
        FileDS.delete();
      }
      FileDS.createNewFile();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  //如果文件存在，则删除后并重新创建；如果不存在，则创建。
  public static void clearFile(String FileName)
  {
    java.io.File FileDS = new java.io.File(FileName);
    try
    {
    	File file = new File(FileName.substring(0,FileName.lastIndexOf(File.separator)));		
		if(!file.exists())
		{
			file.mkdirs();			 
		}
		else
		{
			if (FileDS.exists() == true)
		      {
		        FileDS = new java.io.File(FileName);
		        FileDS.delete();	       
		      }		      
		}
		FileDS.createNewFile();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }


  //如果文件存在，则没有动作，如果不存在，则创建之。
  public static void createFile(String FileName)
  {
    java.io.File FileDS = new java.io.File(FileName);
    try
    {
      if (FileDS.exists() == true)
      {
      }
      else FileDS.createNewFile();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public static java.io.BufferedWriter openAppendWriter(String FileName)
  {
    try
    {
      java.io.BufferedWriter writer = new java.io.BufferedWriter(new
          java.io.
          FileWriter(FileName, true));
      return writer;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
  public static java.io.BufferedReader openReader(String FileName)
  {
    try
    {
      java.io.BufferedReader reader = new java.io.BufferedReader(new
          java.io.
          FileReader(FileName));
      return reader;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }

  }
  public static void closeWriter(java.io.BufferedWriter writer)
  {
    try
    {
      writer.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  public static void closeReader(java.io.BufferedReader reader)
  {
    try
    {
      reader.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  public static void appendRebootLog(String log)
  {
    try
    {
      MyFile.createFile(MyFile.RebootLogFile);
      java.io.BufferedWriter writer = MyFile.openAppendWriter(MyFile.
          RebootLogFile);
      if (log != null)
      {
        writer.write(new java.util.Date().toString()+" ");
        writer.write(log);
        writer.newLine();
      }
      writer.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
  public static void appendCurrentLog(String log)
 {
   try
   {
     MyFile.createFile(MyFile.LOG);
     java.io.BufferedWriter writer = MyFile.openAppendWriter(MyFile.LOG);
     if (log != null)
     {
       writer.write(new java.util.Date().toString()+" ");
       writer.write(log);
       writer.newLine();
     }
     writer.close();
   }
   catch(Exception e)
   {
     e.printStackTrace();
   }

 }

}
package com.linkage.litms.uss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UssLog {

	public static void log(Object object) {

		
//		String path = request.getContextPath();
//
//		String path1 = request.getSession().getServletContext().getRealPath("");

		String path = "/export/home/bbms/WEB/"; 
		
		if (path.lastIndexOf("\\") != -1) {
			path = path.substring(0, path.lastIndexOf("\\"));
		} else {
			path = path.substring(0, path.lastIndexOf("/"));
		}

		File directory = new File(path + "/UserSelfServ/logs/");

		File file = new File(path + "/UserSelfServ/logs/ussLog_"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()).toString());

		try {
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			writer.write(">>>");
			writer.write(object.toString());
			writer.write("\n");
			writer.flush();
			writer.close();
			writer = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


























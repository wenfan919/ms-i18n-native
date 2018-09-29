package com.yonyou.cloud.ms.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CustomServletContextListener implements ServletContextListener {
	
	public static String CTX_REAL_PATH = null;

    public void contextDestroyed(ServletContextEvent sce) {
    }

	public void contextInitialized(ServletContextEvent event) {
		String realContextPath = event.getServletContext().getRealPath("/");
		CTX_REAL_PATH = realContextPath;
	}

}
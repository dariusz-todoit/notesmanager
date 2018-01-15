package com.todoit.training.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;



public class Config extends RemoteServiceServlet {

  private static final long serialVersionUID = 1L;

  Properties prop = new Properties();

  public Config() {
  }

  public String getProperty(String name){
      return prop.getProperty(name);
  }

  @Override
  public void init(ServletConfig config) throws ServletException
  {
      super.init(config);
      InputStream input = null;
      try {
        input = ServletContext.class.getResourceAsStream("/config.properties");
        // ServletContext sc = config.getServletContext();
        prop.load(input);        
      } catch (MalformedURLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }
}

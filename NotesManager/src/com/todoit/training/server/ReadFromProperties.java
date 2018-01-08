package com.todoit.training.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadFromProperties {
  public Properties prop = new Properties();
  
  public ReadFromProperties () {    
    InputStream input = null;
    try {
      input = new FileInputStream ("config.properties");
      // load a properties file
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
  }  

}

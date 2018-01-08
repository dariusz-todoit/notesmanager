package com.todoit.training.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class WtrieToProperties {

  public static void main(String[] args) {
    Properties prop = new Properties();
    OutputStream output = null;

    try {

      output = new FileOutputStream("config.properties");

      // set the properties value
      prop.setProperty("url", "jdbc:jtds:sqlserver://127.0.0.1;instance=SQLEXPRESS;DatabaseName=notes");
      prop.setProperty("userName", "sa");
      prop.setProperty("password", "Dominik");

      // save properties to project root folder
      prop.store(output, null);

    } catch (IOException io) {
      io.printStackTrace();
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }

  }

}

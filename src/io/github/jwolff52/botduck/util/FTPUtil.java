package io.github.jwolff52.botduck.util;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPUtil {
  public static FTPClient ftp;
  private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);
  private static String password;
  
  public static boolean initFTP(String pass) {
    ftp = new FTPClient();
    password = "duckbot";
    if (connect())
    {
      disconnect();
      return true;
    }
    return false;
  }
  
  private static boolean connect() {
    try
    {
      ftp.connect("ftp.duckbot.x10host.com");
      ftp.login("duckbot@duckbot.x10host.com", password);
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException e)
    {
      logger.error("An error occurred connecting to the FTP\n" + e.toString());
      return false;
    }
    try
    {
      ftp.changeDirectory("currentSong");
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException e)
    {
      logger.error("An error occurred changing directories\n" + e.toString());
    }
    return true;
  }
  
  public static void disconnect() {
    try
    {
      ftp.disconnect(true);
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException e)
    {
      logger.error("An error occurred disconnecting from the FTP\n" + e.toString());
    }
  }
  
  public static void changeDir(String dir) {
    connect();
    try
    {
      ftp.changeDirectory(dir);
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException e)
    {
      logger.error("An error occurred changing directories\n" + e.toString());
    }
    disconnect();
  }
  
  public static void moveUp() {
    connect();
    try
    {
      ftp.changeDirectoryUp();
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException e)
    {
      logger.error("An error occurred moving up\n" + e.toString());
    }
    disconnect();
  }
  
  public static void upload(File f) {
    connect();
    try
    {
      ftp.upload(f);
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException|FTPDataTransferException|FTPAbortedException e)
    {
      logger.error("An error occurred uploading the file " + f.getName() + "!\n" + e.toString());
    }
    disconnect();
  }
  
  public static void download(String name, File f) {
    connect();
    try
    {
      ftp.download(name, f);
    }
    catch (IllegalStateException|IOException|FTPIllegalReplyException|FTPException|FTPDataTransferException|FTPAbortedException e)
    {
      logger.error("An error occurred downloading the file " + f.getName() + "!\n" + e.toString());
    }
    disconnect();
  }
}
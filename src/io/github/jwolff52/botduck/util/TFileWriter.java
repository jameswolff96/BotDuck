package io.github.jwolff52.botduck.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TFileWriter {
  static final Logger logger = LoggerFactory.getLogger(TFileWriter.class);
  
  public static void writeFile(File f, ArrayList<String> strings) {
    if (!f.exists()) {
      try
      {
        f.createNewFile();
      }
      catch (IOException e)
      {
        logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
      }
    } else {
      strings.addAll(0, TFileReader.readFile(f));
    }
    try
    {
      BufferedWriter writer = Files.newBufferedWriter(f.toPath(), StandardCharsets.UTF_8, new OpenOption[0]);
      for (String s : strings)
      {
        writer.write(s);
        writer.newLine();
      }
      writer.close();
    }
    catch (IOException e)
    {
      logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
    }
  }
  
  public static void writeFile(File f, String output) {
    ArrayList<String> strings = new ArrayList<>();
    if (!f.exists()) {
      try
      {
        f.createNewFile();
      }
      catch (IOException e)
      {
        logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
      }
    } else {
      strings = TFileReader.readFile(f);
    }
    strings.add(output);
    try
    {
      BufferedWriter writer = Files.newBufferedWriter(f.toPath(), StandardCharsets.UTF_8, new OpenOption[0]);
      for (String s : strings)
      {
        writer.write(s);
        writer.newLine();
      }
      writer.close();
    }
    catch (IOException e)
    {
      logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
    }
  }
  
  public static void overWriteFile(File f, ArrayList<String> strings) {
    if (f.exists()) {
      f.delete();
    }
    try
    {
      f.createNewFile();
    }
    catch (IOException e)
    {
      logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
    }
    try
    {
      BufferedWriter writer = Files.newBufferedWriter(f.toPath(), StandardCharsets.UTF_8, new OpenOption[0]);
      for (String s : strings)
      {
        writer.write(s);
        writer.newLine();
      }
      writer.close();
    }
    catch (IOException e)
    {
      logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
    }
  }
  
  public static void overWriteFile(File f, String output) {
    if (f.exists()) {
      f.delete();
    }
    try
    {
      f.createNewFile();
    }
    catch (IOException e)
    {
      logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
    }
    try
    {
      BufferedWriter writer = Files.newBufferedWriter(f.toPath(), StandardCharsets.UTF_8, new OpenOption[0]);
      writer.write(output);
      writer.newLine();
      writer.close();
    }
    catch (IOException e)
    {
      logger.error("Error writing the file at location: " + f.getName() + "\n" + e.toString());
    }
  }
}
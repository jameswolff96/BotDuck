package io.github.jwolff52.botduck.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TFileReader {
  static final Logger logger = LoggerFactory.getLogger(TFileReader.class);
  
  @SuppressWarnings("resource")
  public static ArrayList<String> readFile(File f) {
    FileInputStream fis = null;
    try
    {
      fis = new FileInputStream(f);
    }
    catch (FileNotFoundException e)
    {
      logger.error("Error reading the file at location: " + f.getName() + "\n" + e.toString());
    }
    ArrayList<String> buffer = new ArrayList<>();
    Scanner scanner = new Scanner(fis);
    while (scanner.hasNext()) {
      buffer.add(scanner.nextLine());
    }
    return buffer;
  }
}
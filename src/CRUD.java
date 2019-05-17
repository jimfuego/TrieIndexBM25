import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public final class CRUD {

  private static final String outputFilePath = System.getProperty("user.dir") + File.separator + "Index_Output.txt";

  /**
   * Initilaizes a directory with parameterized filepath.
   *
   * @Directory path to initialize
   * @return true if write was successful, false otherwise
   */
  public boolean initDirectory(String dirPath) {
    if (!new File(dirPath).exists()) {
      new File(dirPath).mkdirs();
      return true;
    }
    return false;
  }

  /**
   * Gets and returns a list of file names in the article directory.
   *
   * @param articlePath the source directory
   * @return a list of file names contained in the desired directory
   */
  static LinkedList<String> getFilesInDirectory(String articlePath) {
    File folder = new File(articlePath);
    File[] directoryContents = folder.listFiles();
    LinkedList<String> filesInDirectory = new LinkedList<>();
    assert directoryContents != null;
    for (File directoryContent : directoryContents) {
      if (directoryContent.isFile()) {
        filesInDirectory.add(directoryContent.getName());
      }
    }
    return filesInDirectory;
  }

  /**
   * Appends the given line to inverted index output file.
   *
   * @param path filepath to write-to
   * @param line contents to write
   * @return true if successfully written
   */
  static boolean appendToFileBuffered(String path, String line) {
    boolean exists = new File(path).exists();
    try (BufferedWriter bw = new BufferedWriter(
            new FileWriter(path, true))) {
      if (exists) { bw.newLine(); }
      bw.append(line);
      bw.flush();
      bw.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Appends the given line to inverted index output file.
   *
   * @param path filepath to write-to
   * @param line contents to write
   * @return true if successfully written
   */
  static boolean appendToFileRaw(String path, String line) {
    try (FileWriter fw = new FileWriter(path, true)) {
      fw.append(line);
      fw.flush();
      fw.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Reads file from AbsolutePath.
   *
   * @param path the path
   * @return the list
   */
  static List<String> readFile(String path) {
    try {
      //create list to hold lines
      List<String> list = Files.readAllLines(Paths.get(path));
      return list;
    } catch (IOException e) {
      e.getMessage();
    }
    return null;
  }

  /**
   * Takes a document ID tag and retrieves the English headline title.
   *
   * @param docID the document ID tag
   * @return the English headline
   */
  public static String getDocTitle(String docID){
    String[] words = docID.replaceAll("[^a-zA-Z ^0-9]", " ").toLowerCase().split("\\s+");
    String fileName = System.getProperty("user.dir") +
            File.separator + "Program_Data" + File.separator + "AP_Articles" + File.separator + words[0];
    try {
      //open reader to file containing the desired headline
      BufferedReader bf = new BufferedReader(new FileReader(fileName));
      String line;
      String docNo = "";
      //while there are lines to read
      while ((line = bf.readLine()) != null) {
        if(line.matches("<DOC>")){
          line = bf.readLine();
          String[] docNumLine = line.split("\\s+");
          docNo = docNumLine[1];
        }
        //if the document has been found, get title
        if(docNo.matches(docID)) {
          while((line = bf.readLine()) != null) {
            if (line.length() > 6) {
              if (line.substring(0, 6).matches("<HEAD>")) {
                return line.substring(6, line.length() - 7);
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}

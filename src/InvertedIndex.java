import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InvertedIndex implements TrieIndex {
  // path to project directory
  private static final String projectFolderPath = System.getProperty("user.dir");

  // words to be omitted from search
  private ArrayList<String> stopWords = new ArrayList<>();

  // root node
  private BranchNode root = new BranchNode();

  // document collection (docid:docsizesize)
  private HashMap<String, Integer> docCollection = new HashMap<>();

  /**
   * Constructor loads stop-words to class.
   */
  InvertedIndex() {
    try {
      this.stopWords.addAll(Files.readAllLines(Paths.get(projectFolderPath + File.separator + "Program_Data" + File.separator +
              "Stop_Words" + File.separator + "stopwords.txt")));
    } catch (IOException e) {
      System.err.println("Error loading stop words: FileNot found \'stopwords.txt\'");
      e.printStackTrace();
    }
  }

  @Override
  public HashMap<String, ArrayList<Integer>> getPositions(String term) {
    return null;
  }

  @Override
  public ArrayList<Match> getMatches(String term) {
    return this.root.getMatches(Integer.toString(term.hashCode()), 0);
  }

  @Override
  public HashMap<String, Integer> getDocCollection(){
    return this.docCollection;
  }

  @Override
  public void createIndex(String articleDir) {
    // get file paths of all articles contained in directory
    for (String fileName : CRUD.getFilesInDirectory(articleDir)) {
      String docNo = "";
      // open reader
      try (BufferedReader bf =
                   new BufferedReader(new FileReader(articleDir + File.separator + fileName))) {
        String line;
        int position = 1;
        // read file
        while ((line = bf.readLine()) != null) {
          // reached new doc
          if (line.matches("<DOC>")) {
            line = bf.readLine();
            String[] words = line.split("\\s+");
            docNo = words[1];
            position = 1;
          }
          //end of doc
          else if(line.matches("</DOC>")){
            this.docCollection.put(docNo, position);
          }
          // if main text, add contents and increment position for each term
          else if (line.matches("<TEXT>")) {
            while (!(line = bf.readLine()).matches("</TEXT>")) {
              ArrayList<String> textArray = new ArrayList<>(
                      Arrays.asList(line.replaceAll("[^a-zA-Z ^0-9]", " ")
                              .toLowerCase()
                              .split("\\s+")));
              // add terms to index
              for(String term : textArray){
                this.addTerm(term, docNo, position);
                position++;
              }
            }
          }
          // check length to avoid null index in line.substring
          else if (line.length() > 6) {
            // if heading, add contents and increment position for each term
            if (line.substring(0, 6).matches("<HEAD>")) {
              ArrayList<String> headlineArray = new ArrayList<>(
                      Arrays.asList(line.substring(6, line.length() - 7)
                              .replaceAll("[^a-zA-Z ^0-9]", " ")
                              .toLowerCase()
                              .split("\\s+")));
              // add terms to index
              for(String term : headlineArray){
                this.addTerm(term, docNo, position);
                position++;
              }
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   *
   * @param term
   * @param docID
   * @param position
   * @return
   */
  private boolean addTerm(String term, String docID, int position) {
    if(term.matches("")) { return false; }
    return this.root.addTerm(Integer.toString(term.hashCode()), 0, docID, position);
  }
}
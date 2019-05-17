import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 *
 */
public class Main {
  // this project folder
  private static final String projectFolderPath = System.getProperty("user.dir");

  // all read and written files
  private static final String programDataDirectory =
          projectFolderPath + File.separator + "Program_Data";

  // input dir containing a .txt file "hw1_test_queries" to be used as search terms for this indexing
  private static final String searchTermDirectoryPath =
          programDataDirectory + File.separator +
                  "Queries" + File.separator + "hw1_test_queries.txt";

  // output dir for query results
  private static final String queryResultDir = projectFolderPath + File.separator + "Program_Data"
          + File.separator + "Query_Results";

  // output dir for BM25 results
  private static final String bm25QueryResults = queryResultDir + File.separator + "BM25_query_results.txt";

  // output directory for cosine similarity query results (incomplete)
  private static final String cosQueryResults = queryResultDir + File.separator + "cosine_query_results.txt";

  // path to articles
  private static final String articlePath =
          programDataDirectory + File.separator + "AP_Articles";

  /**
   * Helper function used in recursive delete functions.
   *
   * @param filePath the file path
   * @return boolean boolean
   */
  boolean deleteMisc(Path filePath) {
    try (Stream<Path> stream = Files.walk(filePath)) {
      stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  /**
   * Driver method.
   *
   * @param args
   */
  public static void main(String[] args) {
    //get query terms
    ArrayList<String> queries = (ArrayList<String>) CRUD.readFile(searchTermDirectoryPath);

    //me
    System.out.println("James Borzillieri");
    System.out.println("Assignment 2 - CS6200");

    //create index
    InvertedIndex index = new InvertedIndex();
    index.createIndex(articlePath);
    System.out.println("index created");

    //BM25 ranking
    BM25 bm25 = new BM25();
    for(String query : queries){
      bm25.calculateBM25(query, index, bm25QueryResults);
    }

//    //cosine ranking fixme: I'm not done
//    CosineSimilarityQuery csQuery = new CosineSimilarityQuery();
//    System.out.println("Running queries...");
//    queries.forEach(query -> csQuery.cosineRankings(query, index, cosQueryResults));
  }
}

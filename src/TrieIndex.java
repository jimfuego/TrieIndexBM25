import java.util.ArrayList;
import java.util.HashMap;

public interface TrieIndex {

  /**
   *
   * @param term desired term to locate
   * @return a list of positions matched within the associated document
   */
  HashMap<String, ArrayList<Integer>> getPositions(String term);

  /**
   * Takes a term and returns a HashMap
   *
   * @param term
   * @return
   */
  ArrayList<Match> getMatches(String term);

  /**
   * Returns the collection of documents with document size.
   *
   * @return
   */
  HashMap<String, Integer> getDocCollection();

  /**
   * Reads from directory containing TREC-formatted news articles and returns a list of tokens with
   * information pertaining to headline terms, positioning of those terms, as well as a document ID
   * for which the term appears.
   *
   * @param articleDir the directory containing the article files
   */
  void createIndex(String articleDir);
}

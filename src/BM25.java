import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BM25 {

  /**
   * Subclass used to store the document ID and its BM25 score.
   */
  private class Tuple{
    String docID;
    double score;

    /**
     *
     * @param docID
     * @param score
     */
    Tuple(String docID, double score){
      this.docID = docID;
      this.score = score;
    }

    /**
     * Returns the docID.
     * @return
     */
    String getDocID(){
      return this.docID;
    }

    /**
     * Returns the BM25 score.
     * @return
     */
    double getScore(){
      return this.score;
    }
  }

  /**
   *
   * @param query
   * @param index
   * @param outputPath
   */
  void calculateBM25(String query, InvertedIndex index, String outputPath){
    //makes a set of query terms to retrieve matches for (prevents duplicate term search)
    HashMap<String, Integer> queryMap = this.getQueryTerms(query);
    //get avg doc size
    int n = 0;
    int allWords = 0;
    for(String key : index.getDocCollection().keySet()){
      n++;
      allWords += index.getDocCollection().get(key);
    }
    double avgDocSize = (double) allWords / (double) n;
    //BM25
    HashMap<String, Double> articleScores = new HashMap<>();
    for(String queryTerm : queryMap.keySet()){
      ArrayList<Match> matches = index.getMatches(queryTerm);
      int dft = matches.size();
      double x = Math.log10(index.getDocCollection().size() / (double) dft);
      for(Match m : matches){
        double y = (2 + m.getTF()) / 1.2 * ((1 - .75) + .75 * (index.getDocCollection().get(m.getDocID()) / avgDocSize) + m.getTF());
        double z = x * y;
        articleScores.merge(m.getDocID(), z, (a, b) -> a + b);
      }
    }
    articleScores = this.getTopMatches(articleScores);
    ArrayList<Tuple> finalMatches = new ArrayList<>();
    for(String key : articleScores.keySet()){
      finalMatches.add(new Tuple(key, articleScores.get(key)));
    }
    Collections.sort(finalMatches, Comparator.comparing(Tuple::getScore));
    this.printResults(query,finalMatches, outputPath);
  }

  /**
   *
   * @param query
   * @param outputPath
   */
  private void printResults(String query, ArrayList<Tuple> finalResults, String outputPath){
    CRUD.appendToFileBuffered(outputPath, "------------------------------------------------------");
    CRUD.appendToFileBuffered(outputPath, "Query: " + query);
    for (int i = 9; i >= 0; i--) {
      CRUD.appendToFileBuffered(
              outputPath, "\"" + CRUD.getDocTitle(finalResults.get(i).docID) + "\"" +
              " (" + finalResults.get(i).score + ")");
    }
    CRUD.appendToFileBuffered(outputPath, "------------------------------------------------------");
  }

  /**
   *
   * @param articleScores
   */
  private HashMap<String, Double> getTopMatches(HashMap<String, Double> articleScores){
    double lowScore = 0;
    HashMap<String, Double> finalResults= new HashMap<>();
    //for each result
    for(String key : articleScores.keySet()){
      //if there are less than 10 results, add and update lowest
      if(finalResults.size() < 10){
        if(articleScores.get(key) < lowScore || lowScore == 0){
          lowScore = articleScores.get(key);
        }
        finalResults.put(key, articleScores.get(key));
      //if there are 10 results
      } else {
        //check that this entry is greater than the lowest rank
        if(articleScores.get(key) > lowScore){
          String lowKey = this.getLowestScore(finalResults);
          finalResults.remove(lowKey);
          finalResults.put(key, articleScores.get(key));
          lowScore = finalResults.get(this.getLowestScore(finalResults));
        }
      }
    }
    return finalResults;
  }

  /**
   * Gets the key associated with the lowest stored value in HashMap.
   *
   * @param map
   * @return
   */
  private String getLowestScore(HashMap<String, Double> map){
    String lowKey = "";
    double lowest = 10000;
    for(String key : map.keySet()){
      if(map.get(key) < lowest){
        lowKey = key;
        lowest = map.get(key);
      }
    }
    return lowKey;
  }

  /**
   * Parses words from search phrase and returns a set of unique words.
   *
   * @param line the phrase pertaining to the desired information to be retrieved
   * @return a set of String tokens to be used in the info retrieval process
   */
  private HashMap<String, Integer> getQueryTerms(String line) {
    String[] words = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
    HashMap<String, Integer> returnMap = new HashMap<>();
    for(String key : words){
      if ((returnMap.keySet().contains(key))) {
        returnMap.replace(key, returnMap.get(key) + 1);
      } else {
        returnMap.put(key, 1);
      }
    }
    return returnMap;
  }
}

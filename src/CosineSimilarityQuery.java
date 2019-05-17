import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public final class CosineSimilarityQuery {



  /**
   * Private class used to pair docs with TF*IDF vals.
   */
  private class Tuple{
    private String doc;
    private double TFIDF;

    /**
     * Constructor.
     *
     * @param doc
     * @param TFIDF
     */
    Tuple(String doc, double TFIDF){
      this.doc = doc;
      this.TFIDF = TFIDF;
    }

    /**
     * Getter for docID.
     *
     * @return
     */
    String getDocID(){
      return this.doc;
    }

    /**
     * Getter for TF*IDF value.
     *
     * @return
     */
    double getTFIDF(){
      return this.TFIDF;
    }
  }

  /**
   * Determines cosine rankings and similarities to the parameterized query.
   *
   * @param query search term
   * @param index inverted index
   * @param outputDir filepath to write results
   */
  void cosineRankings(String query, InvertedIndex index, String outputDir){
    //map to hold matches to set terms (queryTerm : docMatches)
    HashMap<String, ArrayList<Match>> matches = new HashMap<>();
    //map to hold idf
    HashMap<String, Double> termIDFs = new HashMap<>();
    //makes a set of query terms to retrieve matches for (prevents duplicate term search)
    HashMap<String, Integer> queryMap = this.getQueryTerms(query);
    //HashMap to store TF*IDF vals
    HashMap<String, ArrayList<Tuple>> tfIdfs = new HashMap<>();
    //get number of terms in query
    int querySize = queryMap.keySet().stream().mapToInt(queryMap::get).sum();
    //search index for each term and get matches
    queryMap.keySet().forEach(key -> matches.put(key, index.getMatches(key)));
    //calculate idf for matched terms
    queryMap.keySet().forEach(key -> termIDFs.put(key,
                              1 + Math.log(
                              (double) index.getDocCollection().keySet().size() /
                              (double) matches.get(key).size())));
    //compute TF*IDF for each term to each doc match
    for(String key : matches.keySet()){
      //add TFIDF weights to mapping
      tfIdfs.put(key, new ArrayList<>());
      matches.get(key).forEach(match -> tfIdfs.get(key).add(new Tuple(match.getDocID(),
              termIDFs.get(key) * match.getNormalizedTF(index.getDocCollection().size()))));
    }

    //get dot product
    double dotProduct = 0;
    double queryVector = 0;
    for(String key : matches.keySet()){
      double qtTFIDF = (double) queryMap.get(key) / (double) querySize;

      dotProduct += qtTFIDF * 1;
    }

    //sort matches by docid
//    for(String key : matches.keySet()){
//      matches.get(key).sort(Comparator.comparing(Match::getDocID));
//    }

    //merge tuples into single ArrayList
//    ArrayList<Tuple> allTFIDFs = new ArrayList<>();
//    tfIdfs.keySet().forEach(key -> allTFIDFs.addAll(tfIdfs.get(key)));
//    Collections.sort(allTFIDFs, Comparator.comparing(Tuple::getDocID));


    //will store the ranked articles
//    Rankings rankings = new Rankings();
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

  /**
   * Computes normalized term frequency for queries and query-document matches.
   *
   * @param frequency of the term in the collection
   * @param n size of the term collection (doc or query length)
   * @return
   */
  private float getNormalizedTF(int frequency, int n){
    return (float) frequency / (float) n;
  }
}
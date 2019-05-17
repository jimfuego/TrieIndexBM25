import java.util.HashMap;

/**
 * Private class will hold top rankings for query.
 * These ranked docs are written to the output file.
 */
public class Rankings{
  private float lowestRanking;
  private String lowestDocRanked;
  private HashMap<String, Float> docRankings;

  /**
   * Constructor
   */
  Rankings(double queryScore){
    this.lowestRanking = 0;
    this.docRankings = new HashMap<>();
  }

  /**
   *
   */
  void evaluate(String docID, double score){
    //if docrankings < 10, add
    //else if greatestDiff > new diff replace
  }

  /**
   *
   */
  void appendRankingsToOutput(String outputFile){

  }

  /**
   *
   */
  private void setNewLow(String newDoc, float newDiff){

  }
}
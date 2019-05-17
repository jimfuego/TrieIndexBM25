import java.util.ArrayList;

public class Match {

  String docID;
  private ArrayList<Integer> matchPositions;

  /**
   * Constructor.
   *
   * @param positions
   */
  public Match(String docID, ArrayList<Integer> positions){
    this.docID = docID;
    this.matchPositions = positions;
  }

  /**
   *
   * @return
   */
  String getDocID(){
    return this.docID;
  }

  /**
   * Gets the term frequency in this document.
   *
   * @return
   */
  int getTF(){
    return this.matchPositions.size();
  }

  /**
   * Gets normalized term frequnecy given the parameterized document size.
   *
   * @param docSize
   * @return
   */
  double getNormalizedTF(int docSize){
    return (double) this.matchPositions.size() / (double) docSize;
  }

}

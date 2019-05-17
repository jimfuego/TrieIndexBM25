import java.util.ArrayList;
import java.util.HashMap;

public class BranchNode{

  // links to child branch nodes
  private HashMap<Character, BranchNode> branches;
  // links to child document nodes
  private HashMap<String, DocNode> docLeaves;
  // indicates whether this node represents the final node for a term (aka a complete term)
  private boolean completeWord;

  /**
   * Constructor.
   */
  BranchNode(){
    this.branches = new HashMap<>();
    this.completeWord = false;
  }

  /**
   * Adds a term to the trie.
   *
   * @param termID
   * @param index
   * @param docNo
   * @param position
   * @return
   */
  public boolean addTerm(String termID, int index, String docNo, int position) {
    // get key
    char c = termID.charAt(index);
    //check that index-1 != term.length()
    if(index == termID.length() - 1){
      // ID as complete word node
      this.completeWord = true;
      // init doc leaves if necessary
      if(this.docLeaves == null){
        this.docLeaves = new HashMap<>();
      }
      // add doc||position
      if (this.docLeaves.get(docNo) != null) {
        return this.docLeaves.get(docNo).addPosition(position);
      } else {
        this.docLeaves.putIfAbsent(docNo, new DocNode());
        return this.docLeaves.get(docNo).addPosition(position);
      }
    } else if (this.branches.get(c) == null){
      // create new mapping, recurse in
      this.branches.putIfAbsent(c, new BranchNode());
      return this.branches.get(c).addTerm(termID, index + 1, docNo, position);
    } else if (this.branches.get(c) != null){
      // recurse in
      return this.branches.get(c).addTerm(termID, index + 1, docNo, position);
    }
    return false;
  }

  /**
   * Retrieves a list of Match(es) that contains the document, and positions of the term occurrence.
   *
   * @param termID
   * @return
   */
  ArrayList<Match> getMatches(String termID, int index){
    char c = termID.charAt(index);
    if(index == termID.length() - 1 && this.completeWord){
      //init return map
      ArrayList<Match> docMatches = new ArrayList<>();
      //for each matching document, add to map and chart frequency
      for(String key : this.docLeaves.keySet()){// FIXME: 2/19/19 return an ArrayList<Match>
        docMatches.add(new Match(key, this.docLeaves.get(key).getPositions()));
      }
      return docMatches;
    } else if (index < termID.length() - 1 ){
      // recurse
      return this.branches.get(c).getMatches(termID, index + 1);
    } else {
      //term is not present in index
      return null;
    }
  }
}
import java.util.ArrayList;

public class DocNode {
  // list of positions within document
  ArrayList<Integer> positions;

  /**
   *
   */
  DocNode(){
    this.positions = new ArrayList<>();
  }

  /**
   *
   * @param position
   * @return
   */
  boolean addPosition(int position){
    if (!this.positions.contains(position)){
      this.positions.add(position);
      return true;
    }
    return false;
  }

  /**
   *
   * @return clone copy
   */
  ArrayList<Integer> getPositions() {
    return new ArrayList<>(this.positions);
  }

  /**
   *
   * @return
   */
  int getFrequency(){
    return this.positions.size();
  }
}
package java.util;

public class HashSet implements Set {
    /*@ public normal_behavior
      @ ensures seq.length == 0;
      @ ensures \fresh(this) && \fresh(this.*);
      @*/
    public /*@pure@*/ HashSet();
}


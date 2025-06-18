package java.util;

public final class HashMap implements Map {
    /*@ public normal_behavior
      @ ensures \fresh(this) && \fresh(this.*) && \fresh(this.footprint) && \invariant_for(this);
      @ ensures key_seq.length == 0;
      @ ensures value_seq.length == 0;
      @ assignable \nothing;
      @*/
    public HashMap();
}
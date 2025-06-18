package java.util;

public final class HashMap implements Map {
    /*@ public normal_behavior
      @ ensures \fresh(this) && \fresh(this.*) && \invariant_for(this);
      @ ensures key_seq.length == 0;
      @ ensures value_seq.length == 0;
      @ assignable \nothing;
      @*/
    public HashMap();
}
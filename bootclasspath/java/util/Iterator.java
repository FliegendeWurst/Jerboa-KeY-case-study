package java.util;

public interface Iterator
{
   //@ public instance ghost \TYPE elementType;
   //@ public instance ghost \seq seq;
   //@ public instance ghost \bigint index;

   //@ public instance invariant (\forall int i; 0 <= i && i < seq.length; seq[i] instanceof elementType);
   //@ public instance invariant 0 <= index && index <= seq.length;

   /*@ public normal_behavior
     @ ensures \result <==> index < seq.length;
     @ assignable \strictly_nothing;
     @*/
   public boolean hasNext();
   /*@ public normal_behavior
     @ requires 0 <= index && index < seq.length;
     @ ensures index == \old(index) + 1;
     @ ensures \result == seq[\old(index)];
     @ assignable index;
     @*/
   public /*@ nullable @*/ Object next();
   public void remove();
}

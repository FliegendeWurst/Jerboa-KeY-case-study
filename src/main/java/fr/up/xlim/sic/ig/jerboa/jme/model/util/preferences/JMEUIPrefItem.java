package fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


// Attention certaines annotations disparaisse apres la compile il faut la ligne suivante
// pour les preserver a l'execution
@Retention(RetentionPolicy.RUNTIME)
public @interface JMEUIPrefItem {
	String name();
	String desc() default "";
	String group() default "Common";
	String subGroup() default "";
	boolean noModification() default false;
	int min() default 0;
	int max()  default 100;
	int size() default 30;
	double dmin() default 0;
	double dmax() default 1000.0;
	double dstep() default 0.1;
}

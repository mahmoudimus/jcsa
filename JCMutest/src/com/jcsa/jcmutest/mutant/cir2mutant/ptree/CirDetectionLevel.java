package com.jcsa.jcmutest.mutant.cir2mutant.ptree;

/**
 * The level of detecting a cir-mutation
 * 
 * @author yukimula
 *
 */
public enum CirDetectionLevel {
	not_reached,
	not_satisfied,
	
	satisfiable_not_infected,
	satisfiable_infectable,
	
	satisfied_not_infected,
	satisfied_infectable,
	satisfied_infected,
	
}
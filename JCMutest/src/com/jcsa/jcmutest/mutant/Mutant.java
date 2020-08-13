package com.jcsa.jcmutest.mutant;

import com.jcsa.jcmutest.mutant.mutation.AstMutation;

/**
 * It provides the interface to manage the access to the mutation data
 * in the space.
 * @author yukimula
 *
 */
public class Mutant {
	
	/* definitions */
	/** the space in which the mutant is managed **/
	private MutantSpace space;
	/** the unique ID that tags the mutant in space **/
	private int id;
	/** the mutation that the mutant represents **/
	private AstMutation mutation;
	/** coverage, weak and strong version of mutant **/
	protected Mutant[] versions;
	/**
	 * create an isolated mutant in the space w.r.t the given mutation
	 * @param space
	 * @param id
	 * @param mutation
	 * @throws Exception
	 */
	protected Mutant(MutantSpace space, int id, AstMutation mutation) throws Exception {
		if(space == null)
			throw new IllegalArgumentException("Invalid space: null");
		else if(mutation == null)
			throw new IllegalArgumentException("Invalid mutation: null");
		else {
			this.space = space;
			this.id = id;
			this.mutation = mutation;
			this.versions = new Mutant[] { null, null, null };
		}
	}
	
	/* getters */
	/**
	 * @return the space in which the mutant is managed
	 */
	public MutantSpace get_space() { return this.space; }
	/**
	 * @return the unique ID that tags the mutant in space
	 */
	public int get_id() { return this.id; }
	/**
	 * @return the mutation that the mutant represents
	 */
	public AstMutation get_mutation() { return this.mutation; }
	/**
	 * @return the coverage version of mutation for this mutant
	 */
	public Mutant get_coverage_mutant() { return this.versions[0]; }
	/**
	 * @return the weak version of mutation for this mutant
	 */
	public Mutant get_weak_mutant() { return this.versions[1]; }
	/**
	 * @return the strong version of mutation for this mutant
	 */
	public Mutant get_strong_mutant() { return this.versions[2]; }
	/**
	 * remove the mutant from its space
	 */
	protected void delete() {
		this.space = null;
		this.id = -1;
		this.mutation = null;
		this.versions = null;
	}
	
}

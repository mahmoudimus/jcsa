package com.jcsa.jcmutest.mutant.cir2mutant.model;

import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.expr.CirReferExpression;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;
import com.jcsa.jcparse.lang.sym.SymEvaluator;
import com.jcsa.jcparse.lang.sym.SymExpression;
import com.jcsa.jcparse.test.state.CStateContexts;

/**
 * The mutation in C-intermediate representation code defines a constraint-error pair
 * in which the constraint is required to be satisfied during testing such that the
 * error will be caused for the purpose of killing the mutation.
 * 
 * @author yukimula
 *
 */
public class CirMutation {
	
	/* definitions */
	/** the constraint that needs to be satisfied for killing mutation **/
	private CirConstraint constraint;
	/** the state error that is expected to occur for killing mutation **/
	private CirStateError state_error;
	/**
	 * @param constraint that needs to be satisfied for killing mutation
	 * @param state_error that is expected to occur for killing mutation
	 * @throws Exception
	 */
	protected CirMutation(CirConstraint constraint, 
			CirStateError state_error) throws Exception {
		if(constraint == null)
			throw new IllegalArgumentException("Invalid constraint: null");
		else if(state_error == null)
			throw new IllegalArgumentException("Invalid state_error: null");
		else {
			this.constraint = constraint;
			this.state_error = state_error;
		}
	}
	
	/* getters */
	/**
	 * @return create the statement where the mutation is reached
	 */
	public CirStatement get_statement() { return this.state_error.get_statement(); }
	/**
	 * @return the constraint that needs to be satisfied for killing mutation
	 */
	public CirConstraint get_constraint() { return this.constraint; }
	/**
	 * @return the state error that is expected to occur for killing mutation
	 */
	public CirStateError get_state_error() { return this.state_error; }
	@Override
	public String toString() { 
		return this.constraint.toString() + " ==> " + this.state_error.toString();
	}
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		else if(obj instanceof CirMutation)
			return obj.toString().equals(this.toString());
		else
			return false;
	}
	
	/* optimize */
	/**
	 * @param constraint
	 * @param contexts
	 * @return optimize the constraint under the contextual information
	 * @throws Exception
	 */
	private CirConstraint optimize(CirConstraint constraint, 
				CStateContexts contexts) throws Exception {
		SymExpression condition = SymEvaluator.
				evaluate_on(constraint.get_condition(), contexts);
		return new CirConstraint(constraint.get_statement(), condition);
	}
	/**
	 * @param state_error
	 * @param contexts
	 * @return optimize the state error under the contextual information
	 * @throws Exception
	 */
	private CirStateError optimize(CirStateError state_error,
			CStateContexts contexts) throws Exception {
		if(state_error instanceof CirTrapError) {
			return state_error;
		}
		else if(state_error instanceof CirFlowError) {
			return state_error;
		}
		else if(state_error instanceof CirExpressionError) {
			CirExpression expression = ((CirExpressionError) state_error).get_expression();
			SymExpression orig_val = ((CirExpressionError) state_error).get_original_value();
			SymExpression muta_val = ((CirExpressionError) state_error).get_mutation_value();
			orig_val = SymEvaluator.evaluate_on(orig_val, contexts);
			muta_val = SymEvaluator.evaluate_on(muta_val, contexts);
			return new CirExpressionError(expression, orig_val, muta_val);
		}
		else if(state_error instanceof CirReferenceError) {
			CirReferExpression reference = ((CirReferenceError) state_error).get_reference();
			SymExpression orig_val = ((CirReferenceError) state_error).get_original_value();
			SymExpression muta_val = ((CirReferenceError) state_error).get_mutation_value();
			orig_val = SymEvaluator.evaluate_on(orig_val, contexts);
			muta_val = SymEvaluator.evaluate_on(muta_val, contexts);
			return new CirReferenceError(reference, orig_val, muta_val);
		}
		else if(state_error instanceof CirStateValueError) {
			CirReferExpression reference = ((CirStateValueError) state_error).get_reference();
			SymExpression orig_val = ((CirStateValueError) state_error).get_original_value();
			SymExpression muta_val = ((CirStateValueError) state_error).get_mutation_value();
			orig_val = SymEvaluator.evaluate_on(orig_val, contexts);
			muta_val = SymEvaluator.evaluate_on(muta_val, contexts);
			return new CirStateValueError(reference, orig_val, muta_val);
		}
		else {
			throw new IllegalArgumentException("Invalid state_error: " + state_error);
		}
	}
	/**
	 * @param contexts
	 * @return optimized version of the mutation under contextual
	 * 		   information or null if the mutation is invalid.
	 * @throws Exception
	 */
	protected CirMutation optimize(CStateContexts contexts) throws Exception {
		CirConstraint constraint = this.optimize(this.constraint, contexts);
		CirStateError state_error = this.optimize(this.state_error, contexts);
		return new CirMutation(constraint, state_error);
	}
	/**
	 * @param contexts
	 * @return whether the mutation is valid under the contexts
	 * @throws Exception
	 */
	public boolean is_valid(CStateContexts contexts) throws Exception {
		return this.constraint.satisfiable(contexts)
				&& this.state_error.is_valid(contexts);
	}
	
}

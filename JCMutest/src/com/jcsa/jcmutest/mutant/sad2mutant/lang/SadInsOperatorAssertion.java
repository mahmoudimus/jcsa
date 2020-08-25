package com.jcsa.jcmutest.mutant.sad2mutant.lang;

import com.jcsa.jcmutest.mutant.sad2mutant.SadNode;
import com.jcsa.jcparse.lang.irlang.CirNode;

/**
 * seed#statement: ins_oprt(expression, operator)
 * @author yukimula
 *
 */
public class SadInsOperatorAssertion extends SadMutationAssertion {

	protected SadInsOperatorAssertion(CirNode source) {
		super(source);
	}
	
	/**
	 * @return the original expression to be inserted with more operands
	 */
	public SadExpression get_orig_expression() {
		return (SadExpression) this.get_child(1);
	}
	
	/**
	 * @return the operator to be inserted in the head of original expression
	 */
	public SadOperator get_ins_operator() {
		return (SadOperator) this.get_child(2);
	}

	@Override
	protected String generate_content() throws Exception {
		return "ins_oprt(" + this.get_orig_expression().generate_code()
				+ ", " + this.get_ins_operator().generate_code() + ")";
	}

	@Override
	protected SadNode clone_self() {
		return new SadInsOperatorAssertion(this.get_cir_source());
	}
}

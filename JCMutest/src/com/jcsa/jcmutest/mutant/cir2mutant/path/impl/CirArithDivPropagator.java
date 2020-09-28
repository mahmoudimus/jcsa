package com.jcsa.jcmutest.mutant.cir2mutant.path.impl;

import java.util.Map;

import com.jcsa.jcmutest.mutant.cir2mutant.model.CirConstraint;
import com.jcsa.jcmutest.mutant.cir2mutant.model.CirExpressionError;
import com.jcsa.jcmutest.mutant.cir2mutant.model.CirMutations;
import com.jcsa.jcmutest.mutant.cir2mutant.model.CirReferenceError;
import com.jcsa.jcmutest.mutant.cir2mutant.model.CirStateError;
import com.jcsa.jcmutest.mutant.cir2mutant.path.CirErrorPropagator;
import com.jcsa.jcparse.lang.irlang.CirNode;
import com.jcsa.jcparse.lang.irlang.expr.CirComputeExpression;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.sym.SymExpression;
import com.jcsa.jcparse.lang.sym.SymFactory;

public class CirArithDivPropagator implements CirErrorPropagator {

	@Override
	public void propagate(CirMutations cir_mutations, CirStateError error, CirNode source_location,
			CirNode target_location, Map<CirStateError, CirConstraint> propagations) throws Exception {
		CirComputeExpression target = (CirComputeExpression) target_location;
		CirExpression source = (CirExpression) source_location;
		SymExpression muta_operand; SymExpression muta_value;
		CirConstraint constraint;  CirStateError state_error;
		if(error instanceof CirExpressionError) 
			muta_operand = ((CirExpressionError) error).get_mutation_value();
		else if(error instanceof CirReferenceError) 
			muta_operand = ((CirReferenceError) error).get_mutation_value();
		else return;
		
		/* muta_operand / y */
		if(source == target.get_operand(0)) {
			muta_value = SymFactory.arith_div(target.get_data_type(), 
					muta_operand, target.get_operand(1));
			constraint = cir_mutations.expression_constraint(
					target.statement_of(), Boolean.TRUE, true);
			state_error = cir_mutations.expr_error(target, muta_value);
			propagations.put(state_error, constraint);
		}
		/* x / muta_operand */
		else if(source == target.get_operand(1)) {
			constraint = cir_mutations.expression_constraint(target.statement_of(), 
					SymFactory.equal_with(muta_operand, Integer.valueOf(0)), true);
			state_error = cir_mutations.trap_error(target.statement_of());
			propagations.put(state_error, constraint);
			
			constraint = cir_mutations.expression_constraint(target.statement_of(), 
					SymFactory.not_equals(muta_operand, Integer.valueOf(0)), true);
			muta_value = SymFactory.arith_div(
					target.get_data_type(), target.get_operand(0), muta_operand);
			state_error = cir_mutations.expr_error(target, muta_value);
			propagations.put(state_error, constraint);
		}
		else {
			throw new IllegalArgumentException(target.generate_code(true));
		}
	}

}
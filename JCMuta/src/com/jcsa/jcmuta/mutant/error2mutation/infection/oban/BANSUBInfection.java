package com.jcsa.jcmuta.mutant.error2mutation.infection.oban;

import java.util.Map;

import com.jcsa.jcmuta.mutant.error2mutation.StateError;
import com.jcsa.jcmuta.mutant.error2mutation.StateErrorGraph;
import com.jcsa.jcmuta.mutant.error2mutation.StateEvaluation;
import com.jcsa.jcmuta.mutant.error2mutation.infection.OPRTInfection;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;
import com.jcsa.jcparse.lang.lexical.COperator;
import com.jcsa.jcparse.lang.symb.StateConstraints;
import com.jcsa.jcparse.lang.symb.SymExpression;

/**
 * loperand ==-1 					--> rsv_numb(x)
 * roperand ==-1					--> dif_numb(1)
 * loperand == roperand				--> set_numb(0)
 * loperand != 0 or roperand != 0	--> chg_numb(x)
 * @author yukimula
 *
 */
public class BANSUBInfection extends OPRTInfection {

	@Override
	protected SymExpression muta_expression(CirExpression expression, CirExpression loperand, CirExpression roperand)
			throws Exception {
		return StateEvaluation.binary_expression(expression.
				get_data_type(), COperator.arith_sub, loperand, roperand);
	}

	@Override
	protected boolean partial_evaluate(CirExpression expression, CirExpression loperand, CirExpression roperand,
			StateErrorGraph graph, Map<StateError, StateConstraints> output) throws Exception {
		Object lconstant = StateEvaluation.get_constant_value(loperand);
		Object rconstant = StateEvaluation.get_constant_value(roperand);
		
		/** loperand == -1 **/
		if(!(lconstant instanceof SymExpression)) {
			if(lconstant instanceof Long) {
				if(((Long) lconstant).longValue() == -1L) {
					output.put(graph.get_error_set().rsv_numb(loperand), 
							StateEvaluation.get_conjunctions()); return true;
				}
			}
		}
		
		/** roperand ==-1 --> dif_numb(1) **/
		if(!(rconstant instanceof SymExpression)) {
			if(rconstant instanceof Long) {
				if(((Long) rconstant).longValue() == -1L) {
					output.put(graph.get_error_set().dif_numb(expression, 1L), 
							StateEvaluation.get_conjunctions()); return true;
				}
			}
		}
		
		return false;
	}

	@Override
	protected boolean symbolic_evaluate(CirExpression expression, CirExpression loperand, CirExpression roperand,
			StateErrorGraph graph, Map<StateError, StateConstraints> output) throws Exception {
		SymExpression lcondition, rcondition; StateConstraints constraints;
		CirStatement statement = expression.statement_of();
		
		/* loperand == roperand --> set_numb(0) */
		lcondition = StateEvaluation.equal_with(loperand, roperand);
		constraints = StateEvaluation.get_conjunctions();
		this.add_constraint(constraints, statement, lcondition);
		output.put(graph.get_error_set().set_numb(expression, 0L), constraints);
		
		/* loperand != 0 or roperand != 0 */
		lcondition = StateEvaluation.not_equals(loperand, 0L);
		rcondition = StateEvaluation.not_equals(roperand, 0L);
		constraints = StateEvaluation.get_disjunctions();
		this.add_constraint(constraints, statement, lcondition);
		this.add_constraint(constraints, statement, rcondition);
		output.put(graph.get_error_set().chg_numb(expression), constraints);
		
		return false;
	}

}

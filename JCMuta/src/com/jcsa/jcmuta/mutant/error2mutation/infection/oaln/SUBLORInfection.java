package com.jcsa.jcmuta.mutant.error2mutation.infection.oaln;

import java.util.Map;

import com.jcsa.jcmuta.mutant.error2mutation.StateError;
import com.jcsa.jcmuta.mutant.error2mutation.StateErrorGraph;
import com.jcsa.jcmuta.mutant.error2mutation.StateEvaluation;
import com.jcsa.jcmuta.mutant.error2mutation.infection.OPRTInfection;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;
import com.jcsa.jcparse.lang.symb.StateConstraints;
import com.jcsa.jcparse.lang.symb.SymExpression;

/**
 * loperand != roperand	--> set_true
 * loperand == roperand --> dif_numb(1)
 * @author yukimula
 *
 */
public class SUBLORInfection extends OPRTInfection {

	@Override
	protected SymExpression muta_expression(CirExpression expression, CirExpression loperand, CirExpression roperand)
			throws Exception {
		return StateEvaluation.logic_or(loperand, roperand);
	}

	@Override
	protected boolean partial_evaluate(CirExpression expression, CirExpression loperand, CirExpression roperand,
			StateErrorGraph graph, Map<StateError, StateConstraints> output) throws Exception {
		Object lconstant = StateEvaluation.get_constant_value(loperand);
		Object rconstant = StateEvaluation.get_constant_value(roperand);
		CirStatement statement = expression.statement_of();
		SymExpression constraint; StateConstraints constraints;
		
		if(!(lconstant instanceof SymExpression)) {
			if(StateEvaluation.is_zero_number(lconstant)) {
				constraint = StateEvaluation.not_equals(roperand, 0L);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().set_bool(expression, true), constraints);
			}
			else {
				output.put(graph.get_error_set().set_bool(expression, 
						true), StateEvaluation.get_conjunctions());
			}
			return true;
		}
		
		if(!(rconstant instanceof SymExpression)) {
			if(StateEvaluation.is_zero_number(rconstant)) {
				constraint = StateEvaluation.not_equals(loperand, 0L);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().set_bool(expression, true), constraints);
			}
			else {
				output.put(graph.get_error_set().set_bool(expression, 
						true), StateEvaluation.get_conjunctions());
			}
			return true;
		}
		
		return false;
	}

	@Override
	protected boolean symbolic_evaluate(CirExpression expression, CirExpression loperand, CirExpression roperand,
			StateErrorGraph graph, Map<StateError, StateConstraints> output) throws Exception {
		SymExpression constraint; StateConstraints constraints;
		CirStatement statement = expression.statement_of();
		
		constraint = StateEvaluation.equal_with(loperand, roperand);
		constraints = StateEvaluation.get_conjunctions();
		this.add_constraint(constraints, statement, constraint);
		output.put(graph.get_error_set().dif_numb(expression, 1L), constraints);
		
		constraint = StateEvaluation.not_equals(loperand, roperand);
		constraints = StateEvaluation.get_conjunctions();
		this.add_constraint(constraints, statement, constraint);
		output.put(graph.get_error_set().set_bool(expression, true), constraints);
		
		return true;
	}

}

package com.jcsa.jcmuta.mutant.error2mutation.infection.obrn;

import java.util.Map;

import com.jcsa.jcmuta.mutant.error2mutation.StateError;
import com.jcsa.jcmuta.mutant.error2mutation.StateErrorGraph;
import com.jcsa.jcmuta.mutant.error2mutation.StateEvaluation;
import com.jcsa.jcmuta.mutant.error2mutation.StateInfection;
import com.jcsa.jcmuta.mutant.error2mutation.infection.OPRTInfection;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;
import com.jcsa.jcparse.lang.symb.StateConstraints;
import com.jcsa.jcparse.lang.symb.SymExpression;

/**
 * loperand == 0 and loperand == roperand	--> dif_numb(1)
 * roperand >= 32 and loperand == roperand	--> dif_numb(1)
 * loperand == roperand						--> set_true
 * loperand != roperand						--> set_false
 * @author yukimula
 *
 */
public class LSHEQVInfection extends OPRTInfection {

	@Override
	protected SymExpression muta_expression(CirExpression expression, CirExpression loperand, CirExpression roperand)
			throws Exception {
		return StateEvaluation.equal_with(loperand, roperand);
	}

	@Override
	protected boolean partial_evaluate(CirExpression expression, CirExpression loperand, CirExpression roperand,
			StateErrorGraph graph, Map<StateError, StateConstraints> output) throws Exception {
		Object lconstant = StateEvaluation.get_constant_value(loperand);
		Object rconstant = StateEvaluation.get_constant_value(roperand);
		SymExpression constraint; StateConstraints constraints;
		CirStatement statement = expression.statement_of();
		
		if(lconstant instanceof Long) {
			if(((Long) lconstant).longValue() == 0L) {
				constraint = StateEvaluation.equal_with(loperand, roperand);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().dif_numb(expression, 1L), constraints);
				output.put(graph.get_error_set().set_bool(expression, true), constraints);
			}
			else {
				constraint = StateEvaluation.equal_with(loperand, roperand);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().set_bool(expression, true), constraints);
				
				constraint = StateEvaluation.not_equals(loperand, roperand);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().set_bool(expression, false), constraints);
				
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, StateEvaluation.greater_eq(roperand, StateInfection.max_bitwise));
				this.add_constraint(constraints, statement, StateEvaluation.equal_with(loperand, roperand));
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().dif_numb(expression, 1L), constraints);
			}
			return true;
		}
		
		if(rconstant instanceof Long) {
			if(((Long) rconstant).longValue() >= StateInfection.max_bitwise) {
				constraint = StateEvaluation.equal_with(loperand, roperand);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().dif_numb(expression, 1L), constraints);
				output.put(graph.get_error_set().set_bool(expression, true), constraints);
			}
			else {
				constraint = StateEvaluation.equal_with(loperand, roperand);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().set_bool(expression, true), constraints);
				
				constraint = StateEvaluation.not_equals(loperand, roperand);
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().set_bool(expression, false), constraints);
				
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, StateEvaluation.equal_with(loperand, 0L));
				this.add_constraint(constraints, statement, StateEvaluation.equal_with(loperand, roperand));
				constraints = StateEvaluation.get_conjunctions();
				this.add_constraint(constraints, statement, constraint);
				output.put(graph.get_error_set().dif_numb(expression, 1L), constraints);
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
		output.put(graph.get_error_set().set_bool(expression, true), constraints);
		
		constraint = StateEvaluation.not_equals(loperand, roperand);
		constraints = StateEvaluation.get_conjunctions();
		this.add_constraint(constraints, statement, constraint);
		output.put(graph.get_error_set().set_bool(expression, false), constraints);
		
		constraints = StateEvaluation.get_conjunctions();
		this.add_constraint(constraints, statement, StateEvaluation.equal_with(loperand, 0L));
		this.add_constraint(constraints, statement, StateEvaluation.equal_with(loperand, roperand));
		constraints = StateEvaluation.get_conjunctions();
		this.add_constraint(constraints, statement, constraint);
		output.put(graph.get_error_set().dif_numb(expression, 1L), constraints);
		
		return true;
	}

}
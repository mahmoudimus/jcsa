package com.jcsa.jcmutest.backups;

import com.jcsa.jcparse.lang.ctype.CType;
import com.jcsa.jcparse.lang.lexical.COperator;
import com.jcsa.jcparse.lang.sym.SymExpression;
import com.jcsa.jcparse.lang.sym.SymFactory;

public class SecAssignRLPropagator extends SecExpressionPropagator {

	@Override
	protected void propagate_set_expression(SecSetExpressionError error) throws Exception {
		SymExpression muta_expression = error.get_muta_expression().get_expression();
		SecStateError target_error = this.set_reference(muta_expression);
		this.append_propagation_pair(this.condition_constraint(), target_error);
	}

	@Override
	protected void propagate_add_expression(SecAddExpressionError error) throws Exception {
		SymExpression ori_operand = error.get_orig_expression().get_expression();
		SymExpression add_operand = error.get_operand().get_expression();
		COperator operator = error.get_operator().get_operator();
		CType type = this.target_reference().get_data_type();
		
		SymExpression muta_expression; 
		switch(operator) {
		case arith_add:
		{
			muta_expression = SymFactory.arith_add(type, ori_operand, add_operand);
			break;
		}
		case arith_sub:
		{
			muta_expression = SymFactory.arith_sub(type, ori_operand, add_operand);
			break;
		}
		case arith_mul:
		{
			muta_expression = SymFactory.arith_mul(type, ori_operand, add_operand);
			break;
		}
		case arith_div:
		{
			muta_expression = SymFactory.arith_div(type, ori_operand, add_operand);
			break;
		}
		case arith_mod:
		{
			muta_expression = SymFactory.arith_mod(type, ori_operand, add_operand);
			break;
		}
		case bit_and:
		{
			muta_expression = SymFactory.bitws_and(type, ori_operand, add_operand);
			break;
		}
		case bit_or:
		{
			muta_expression = SymFactory.bitws_ior(type, ori_operand, add_operand);
			break;
		}
		case bit_xor:
		{
			muta_expression = SymFactory.bitws_xor(type, ori_operand, add_operand);
			break;
		}
		case left_shift:
		{
			muta_expression = SymFactory.bitws_lsh(type, ori_operand, add_operand);
			break;
		}
		case righ_shift:
		{
			muta_expression = SymFactory.bitws_rsh(type, ori_operand, add_operand);
			break;
		}
		default: throw new IllegalArgumentException(operator.toString());
		}
		
		SecStateError target_error = this.set_reference(muta_expression);
		this.append_propagation_pair(this.condition_constraint(), target_error);
	}

	@Override
	protected void propagate_ins_expression(SecInsExpressionError error) throws Exception {
		SymExpression ori_operand = error.get_orig_expression().get_expression();
		SymExpression add_operand = error.get_operand().get_expression();
		COperator operator = error.get_operator().get_operator();
		CType type = this.target_reference().get_data_type();
		
		SymExpression muta_expression; 
		switch(operator) {
		case arith_sub:
		{
			muta_expression = SymFactory.arith_sub(type, add_operand, ori_operand);
			break;
		}
		case arith_div:
		{
			muta_expression = SymFactory.arith_div(type, add_operand, ori_operand);
			break;
		}
		case arith_mod:
		{
			muta_expression = SymFactory.arith_mod(type, add_operand, ori_operand);
			break;
		}
		case left_shift:
		{
			muta_expression = SymFactory.bitws_lsh(type, add_operand, ori_operand);
			break;
		}
		case righ_shift:
		{
			muta_expression = SymFactory.bitws_rsh(type, add_operand, ori_operand);
			break;
		}
		default: throw new IllegalArgumentException(operator.toString());
		}
		
		SecStateError target_error = this.set_reference(muta_expression);
		this.append_propagation_pair(this.condition_constraint(), target_error);
	}

	@Override
	protected void propagate_uny_expression(SecUnyExpressionError error) throws Exception {
		SymExpression operand = error.get_orig_expression().get_expression();
		COperator operator = error.get_operator().get_operator();
		CType type = this.target_reference().get_data_type();
		
		SymExpression muta_expression;
		switch(operator) {
		case negative:
		{
			muta_expression = SymFactory.arith_neg(type, operand);
			break;
		}
		case bit_not:
		{
			muta_expression = SymFactory.bitws_rsv(type, operand);
			break;
		}
		case logic_not:
		{
			muta_expression = SymFactory.logic_not(operand);
			break;
		}
		default: throw new IllegalArgumentException(operator.toString());
		}
		
		SecStateError target_error = this.set_reference(muta_expression);
		this.append_propagation_pair(this.condition_constraint(), target_error);
	}

}
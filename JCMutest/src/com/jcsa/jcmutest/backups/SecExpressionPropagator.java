package com.jcsa.jcmutest.backups;

import com.jcsa.jcparse.lang.irlang.expr.CirComputeExpression;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.sym.SymExpression;
import com.jcsa.jcparse.lang.sym.SymFactory;

public abstract class SecExpressionPropagator extends SecErrorPropagator {

	@Override
	protected void propagate_add_statement(SecAddStatementError error) throws Exception {}

	@Override
	protected void propagate_del_statement(SecDelStatementError error) throws Exception {}

	@Override
	protected void propagate_set_statement(SecSetStatementError error) throws Exception {}

	@Override
	protected void propagate_trap_error(SecTrapError error) throws Exception {}

	@Override
	protected void propagate_none_error(SecNoneError error) throws Exception {}
	
	@Override
	protected void propagate_set_reference(SecSetReferenceError error) throws Exception {}

	@Override
	protected void propagate_add_reference(SecAddReferenceError error) throws Exception {}

	@Override
	protected void propagate_ins_reference(SecInsReferenceError error) throws Exception {}

	@Override
	protected void propagate_uny_reference(SecUnyReferenceError error) throws Exception {}
	
	protected SymExpression get_loperand() throws Exception {
		CirExpression expression = this.target_expression();
		if(expression instanceof CirComputeExpression)
			return SymFactory.parse(((CirComputeExpression) expression).get_operand(0));
		else
			throw new IllegalArgumentException(expression.generate_code(true));
	}
	
	protected SymExpression get_roperand() throws Exception {
		CirExpression expression = this.target_expression();
		if(expression instanceof CirComputeExpression)
			return SymFactory.parse(((CirComputeExpression) expression).get_operand(1));
		else
			throw new IllegalArgumentException(expression.generate_code(true));
	}
	
}
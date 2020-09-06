package com.jcsa.jcmutest.selang.lang.conc;

import com.jcsa.jcmutest.selang.SedKeywords;
import com.jcsa.jcmutest.selang.lang.SedNode;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;

public class SedChgExpressionError extends SedUnyExpressionError {
	
	public SedChgExpressionError(CirStatement statement, CirExpression orig_expression)
			throws Exception {
		super(statement, SedKeywords.chg_expr, orig_expression);
	}
	
	@Override
	protected boolean verify_expression_type(SedKeywords type) {
		switch(type) {
		case cbool:
		case caddr:
		case cchar:
		case csign:
		case usign:
		case creal:
		case clist:	return true;
		default:	return false;
		}
	}
	
	@Override
	protected SedNode construct() throws Exception {
		return new SedChgExpressionError(
				this.get_statement().get_cir_statement(),
				this.get_orig_expression().get_cir_expression());
	}
	
}
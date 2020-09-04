package com.jcsa.jcmutest.sedlang.lang.conc;

import com.jcsa.jcmutest.sedlang.SedExpressionType;
import com.jcsa.jcmutest.sedlang.SedKeywords;
import com.jcsa.jcmutest.sedlang.lang.SedNode;
import com.jcsa.jcmutest.sedlang.lang.expr.SedExpression;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;

public class SedXorExpressionError extends SedBinExpressionError {

	public SedXorExpressionError(CirStatement statement, 
			CirExpression orig_expression,
			SedExpression muta_expression) throws Exception {
		super(statement, SedKeywords.xor_expr, orig_expression, muta_expression);
	}

	@Override
	protected boolean verify_expression_type(SedExpressionType type) {
		switch(type) {
		case cchar:
		case csign:
		case usign: return true;
		default:	return false;
		}
	}

	@Override
	protected SedNode construct() throws Exception {
		return new SedXorExpressionError(
				this.get_statement().get_cir_statement(),
				this.get_orig_expression().get_cir_expression(),
				this.get_muta_expression());
	}

}

package com.jcsa.jcmutest.selang.lang.serr;

import com.jcsa.jcmutest.selang.SedKeywords;
import com.jcsa.jcmutest.selang.lang.SedNode;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;

/**
 * add_stmt(statement): the statement is executed even though it should NOT
 * be executed in testing with the program of correct version.
 * 
 * @author yukimula
 *
 */
public class SedAddStatementError extends SedStatementError {

	public SedAddStatementError(CirStatement statement, CirStatement orig_statement)
			throws Exception {
		super(statement, SedKeywords.add_stmt, orig_statement);
	}

	@Override
	protected String generate_content() throws Exception {
		return "(" + this.get_orig_statement().generate_code() + ")";
	}

	@Override
	protected SedNode construct() throws Exception {
		return new SedAddStatementError(
				this.get_statement().get_cir_statement(),
				this.get_orig_statement().get_cir_statement());
	}

}
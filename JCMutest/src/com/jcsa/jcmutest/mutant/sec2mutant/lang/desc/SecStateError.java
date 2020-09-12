package com.jcsa.jcmutest.mutant.sec2mutant.lang.desc;

import com.jcsa.jcmutest.mutant.sec2mutant.SecKeywords;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;

public abstract class SecStateError extends SecDescription {

	public SecStateError(CirStatement statement, SecKeywords keyword) throws Exception {
		super(statement, keyword);
	}
	
	@Override
	public boolean is_constraint() {
		return false;
	}

	
	@Override
	public boolean is_state_error() {
		return true;
	}
	
}

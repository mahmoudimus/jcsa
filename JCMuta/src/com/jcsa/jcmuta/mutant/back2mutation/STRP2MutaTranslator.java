package com.jcsa.jcmuta.mutant.back2mutation;

import com.jcsa.jcmuta.mutant.AstMutation;
import com.jcsa.jcparse.lang.astree.AstNode;
import com.jcsa.jcparse.lang.astree.stmt.AstStatement;

import __backup__.TextMutation;

public class STRP2MutaTranslator implements Text2MutaTranslator {

	@Override
	public AstMutation parse(TextMutation mutation) throws Exception {
		AstNode location = mutation.get_origin();
		while(location != null) {
			if(location instanceof AstStatement) {
				return AstMutation.STRP((AstStatement) location);
			}
			else location = location.get_parent();
		}
		return null;
	}

}

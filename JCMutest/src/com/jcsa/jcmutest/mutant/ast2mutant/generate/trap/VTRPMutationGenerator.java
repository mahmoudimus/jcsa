package com.jcsa.jcmutest.mutant.ast2mutant.generate.trap;

import java.util.List;

import com.jcsa.jcmutest.mutant.ast2mutant.AstMutation;
import com.jcsa.jcmutest.mutant.ast2mutant.AstMutations;
import com.jcsa.jcmutest.mutant.ast2mutant.generate.AstMutationGenerator;
import com.jcsa.jcparse.lang.astree.AstNode;
import com.jcsa.jcparse.lang.astree.expr.AstExpression;

public class VTRPMutationGenerator extends AstMutationGenerator {
	
	@Override
	protected boolean is_available(AstNode location) throws Exception {
		return this.is_numeric_expression(location) 
				&& !this.is_left_reference(location);
	}
	
	@Override
	protected void generate_mutations(AstNode location, List<AstMutation> mutations) throws Exception {
		AstExpression expression = (AstExpression) location;
		mutations.add(AstMutations.trap_on_pos(expression));
		mutations.add(AstMutations.trap_on_zro(expression));
		mutations.add(AstMutations.trap_on_neg(expression));
	}
	
}
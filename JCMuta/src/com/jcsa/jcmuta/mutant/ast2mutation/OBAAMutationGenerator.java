package com.jcsa.jcmuta.mutant.ast2mutation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.jcsa.jcmuta.mutant.AstMutation;
import com.jcsa.jcparse.lang.astree.AstNode;
import com.jcsa.jcparse.lang.astree.expr.oprt.AstBinaryExpression;
import com.jcsa.jcparse.lang.astree.expr.oprt.AstBitwiseAssignExpression;
import com.jcsa.jcparse.lang.astree.expr.oprt.AstShiftAssignExpression;
import com.jcsa.jcparse.lang.lexical.COperator;

public class OBAAMutationGenerator extends AstMutationGenerator {
	
	private static final List<COperator> replaces = new LinkedList<COperator>();
	static {
		replaces.add(COperator.arith_add_assign);
		replaces.add(COperator.arith_sub_assign);
		replaces.add(COperator.arith_mul_assign);
		replaces.add(COperator.arith_div_assign);
		replaces.add(COperator.arith_mod_assign);
	}
	
	@Override
	protected void collect_locations(AstNode location, Collection<AstNode> locations) throws Exception {
		if(location instanceof AstBitwiseAssignExpression
			|| location instanceof AstShiftAssignExpression)
			locations.add(location);
	}

	@Override
	protected void generate_mutations(AstNode location, Collection<AstMutation> mutations) throws Exception {
		AstBinaryExpression expression = (AstBinaryExpression) location;
		for(COperator replace : replaces) {
			if(expression.get_operator().get_operator() != replace) {
				mutations.add(AstMutation.OBAA(expression, replace));
			}
		}
	}

}

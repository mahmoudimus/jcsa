package com.jcsa.jcmuta.mutant.ast2mutation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.jcsa.jcmuta.mutant.AstMutation;
import com.jcsa.jcparse.lang.astree.AstNode;
import com.jcsa.jcparse.lang.astree.expr.oprt.AstBinaryExpression;
import com.jcsa.jcparse.lang.astree.expr.oprt.AstBitwiseBinaryExpression;
import com.jcsa.jcparse.lang.astree.expr.oprt.AstShiftBinaryExpression;
import com.jcsa.jcparse.lang.lexical.COperator;

public class OBRNMutationGenerator extends AstMutationGenerator {
	
	private static final List<COperator> replaces = new LinkedList<COperator>();
	static {
		replaces.add(COperator.greater_tn);
		replaces.add(COperator.greater_eq);
		replaces.add(COperator.smaller_tn);
		replaces.add(COperator.smaller_eq);
		replaces.add(COperator.equal_with);
		replaces.add(COperator.not_equals);
	}
	
	@Override
	protected void collect_locations(AstNode location, Collection<AstNode> locations) throws Exception {
		if(location instanceof AstBitwiseBinaryExpression
			|| location instanceof AstShiftBinaryExpression)
			locations.add(location);
	}

	@Override
	protected void generate_mutations(AstNode location, Collection<AstMutation> mutations) throws Exception {
		AstBinaryExpression expression = (AstBinaryExpression) location;
		for(COperator replace : replaces) {
			if(expression.get_operator().get_operator() != replace) {
				mutations.add(AstMutation.OBRN(expression, replace));
			}
		}
	}

}

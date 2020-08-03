package com.jcsa.jcparse.lang.ir.impl;

import com.jcsa.jcparse.lang.ir.CirNode;
import com.jcsa.jcparse.lang.ir.CirTree;
import com.jcsa.jcparse.lang.ir.expr.CirExpression;
import com.jcsa.jcparse.lang.ir.expr.refer.CirReferExpression;
import com.jcsa.jcparse.lang.ir.stmt.assign.CirParameterAssignStatement;

public class CirParameterAssignStatementImpl extends CirStatementImpl implements CirParameterAssignStatement {

	protected CirParameterAssignStatementImpl(CirTree tree) {
		super(tree);
	}

	@Override
	public CirReferExpression get_lvalue() { return (CirReferExpression) this.get_child(0); }

	@Override
	public CirExpression get_rvalue() { return (CirExpression) this.get_child(1); }

	@Override
	protected CirNode copy_self() {
		return new CirParameterAssignStatementImpl(this.get_tree());
	}

}

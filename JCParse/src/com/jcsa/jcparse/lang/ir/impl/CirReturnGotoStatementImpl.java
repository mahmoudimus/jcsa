package com.jcsa.jcparse.lang.ir.impl;

import com.jcsa.jcparse.lang.ir.CirNode;
import com.jcsa.jcparse.lang.ir.CirTree;
import com.jcsa.jcparse.lang.ir.stmt.transit.CirReturnGotoStatement;
import com.jcsa.jcparse.lang.ir.unit.CirLabel;

public class CirReturnGotoStatementImpl extends CirStatementImpl implements CirReturnGotoStatement {

	protected CirReturnGotoStatementImpl(CirTree tree) {
		super(tree);
	}
	
	@Override
	public CirLabel get_next_label() { return (CirLabel) this.get_child(0); }

	@Override
	protected CirNode copy_self() {
		return new CirReturnGotoStatementImpl(this.get_tree());
	}
	
}

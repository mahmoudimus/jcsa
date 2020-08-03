package com.jcsa.jcparse.lang.ir.expr.value;

import com.jcsa.jcparse.lang.ir.expr.CirBinaryExpression;
import com.jcsa.jcparse.lang.ir.expr.CirValueExpression;

/**
 * operand {&, |, ^, <<, >>} operand
 * @author yukimula
 *
 */
public interface CirBitwsBinaryExpression extends CirValueExpression, CirBinaryExpression {
}

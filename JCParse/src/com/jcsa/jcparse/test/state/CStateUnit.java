package com.jcsa.jcparse.test.state;

import com.jcsa.jcparse.lang.ctype.CArrayType;
import com.jcsa.jcparse.lang.ctype.CBasicType;
import com.jcsa.jcparse.lang.ctype.CEnumType;
import com.jcsa.jcparse.lang.ctype.CFunctionType;
import com.jcsa.jcparse.lang.ctype.CPointerType;
import com.jcsa.jcparse.lang.ctype.CStructType;
import com.jcsa.jcparse.lang.ctype.CType;
import com.jcsa.jcparse.lang.ctype.CTypeAnalyzer;
import com.jcsa.jcparse.lang.ctype.CUnionType;
import com.jcsa.jcparse.lang.irlang.expr.CirExpression;
import com.jcsa.jcparse.lang.irlang.expr.CirInitializerBody;
import com.jcsa.jcparse.lang.irlang.expr.CirStringLiteral;
import com.jcsa.jcparse.lang.irlang.stmt.CirCaseStatement;
import com.jcsa.jcparse.lang.irlang.stmt.CirIfStatement;
import com.jcsa.jcparse.lang.irlang.stmt.CirStatement;

/**
 * Each unit in program state space refers to an expression of cir-code,
 * of which value and type are recorded.
 * 
 * @author yukimula
 *
 */
public class CStateUnit {
	
	/* definitions */
	/** the node in which the unit is created **/
	private CStateNode node;
	/** classifier of the data type **/
	private CValueType type;
	/** the expression being instrumented **/
	private CirExpression expression;
	/** the value hold by this expression **/
	private Object value;
	
	/* constructor */
	/**
	 * @param expression
	 * @return determine the classifier of value type of the expression
	 * @throws IllegalArgumentException
	 */
	private CValueType classifier_of(CirExpression expression) throws Exception {
		/* 1. expression-based classifier */
		if(expression instanceof CirInitializerBody) {
			return CValueType.clist;
		}
		else if(expression instanceof CirStringLiteral) {
			return CValueType.clist;
		}
		
		/* 2. context-based classifier */
		CirStatement statement = expression.statement_of();
		if(statement instanceof CirIfStatement) {
			if(((CirIfStatement) statement).get_condition() == expression) {
				return CValueType.cbool;
			}
		}
		else if(statement instanceof CirCaseStatement) {
			if(((CirCaseStatement) statement).get_condition() == expression) {
				return CValueType.cbool;
			}
		}
		
		/* 3. data type based classifier */
		CType data_type = CTypeAnalyzer.get_value_type(expression.get_data_type());
		if(data_type instanceof CBasicType) {
			switch(((CBasicType) data_type).get_tag()) {
			case c_void:	return CValueType.cvoid;
			case c_bool:	return CValueType.cbool;
			case c_char:	
			case c_uchar:	return CValueType.cchar;
			case c_short:
			case c_int:
			case c_long:
			case c_llong:	return CValueType.csign;
			case c_ushort:
			case c_uint:
			case c_ulong:
			case c_ullong:	return CValueType.usign;
			case c_float:
			case c_double:
			case c_ldouble:	return CValueType.creal;
			default:		return CValueType.clist;
			}
		}
		else if(data_type instanceof CArrayType) {
			return CValueType.clist;
		}
		else if(data_type instanceof CPointerType
				|| data_type instanceof CFunctionType) {
			return CValueType.caddr;
		}
		else if(data_type instanceof CStructType
				|| data_type instanceof CUnionType) {
			return CValueType.cbody;
		}
		else if(data_type instanceof CEnumType) {
			return CValueType.csign;
		}
		else {
			throw new IllegalArgumentException(data_type.generate_code());
		}
	}
	/**
	 * create an isolated unit to describe the state of the expression.
	 * @param expression
	 * @param value
	 * @throws Exception
	 */
	protected CStateUnit(CStateNode node, CirExpression expression, Object value) throws Exception {
		if(expression == null || expression.statement_of() == null)
			throw new IllegalArgumentException("Invalid expression: null");
		else if(node == null)
			throw new IllegalArgumentException("Invalid node as null");
		else {
			this.node = node;
			this.type = this.classifier_of(expression);
			this.expression = expression;
			this.value = value;
		}
	}
	
	/* getters */
	/**
	 * @return the node refers to a statement being executed during testing
	 * 		   in which the expression in the unit was evaluated.
	 */
	public CStateNode get_node() { return this.node; }
	/**
	 * @return value type of the expression
	 */
	public CValueType get_value_type() { return this.type; }
	/**
	 * @return data type of the expression
	 */
	public CType get_data_type() { return this.expression.get_data_type(); }
	/**
	 * @return the expression of which state was evaluated
	 */
	public CirExpression get_expression() { return this.expression; }
	/**
	 * @return the statement before which the state is created
	 */
	public CirStatement get_statement() { return this.expression.statement_of(); }
	/**
	 * @return whether the value of the expression is recorded.
	 */
	public boolean has_value() { return this.value != null; }
	/**
	 * @return the value hold by the unit during testing process.
	 */
	public Object get_value() { return this.value; }
	/**
	 * set the value of the expression under testing
	 * @param value
	 */
	protected void set_value(Object value) { this.value = value; }
	
	/* value translators */
	/**
	 * @return value translated as bool
	 * @throws IllegalArgumentException
	 */
	public boolean get_bool() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return ((Boolean) value).booleanValue();
		else if(value instanceof Character) 
			return ((Character) value).charValue() != 0;
		else if(value instanceof Short) 
			return ((Short) value).shortValue() != 0;
		else if(value instanceof Integer) 
			return ((Integer) value).intValue() != 0;
		else if(value instanceof Long) 
			return ((Long) value).longValue() != 0;
		else if(value instanceof Float) 
			return ((Float) value).floatValue() != 0;
		else if(value instanceof Double) 
			return ((Double) value).doubleValue() != 0;
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	/**
	 * @return value translated as char
	 * @throws IllegalArgumentException
	 */
	public char get_char() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return (char) (((Boolean) value).booleanValue() ? 1 : 0);
		else if(value instanceof Character) 
			return (char) (((Character) value).charValue());
		else if(value instanceof Short) 
			return (char) ((Short) value).shortValue();
		else if(value instanceof Integer) 
			return (char) ((Integer) value).intValue();
		else if(value instanceof Long) 
			return (char) ((Long) value).longValue();
		else if(value instanceof Float) 
			return (char) ((Float) value).floatValue();
		else if(value instanceof Double) 
			return (char) ((Double) value).doubleValue();
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	/**
	 * @return value translated as short
	 * @throws IllegalArgumentException
	 */
	public short get_short() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return (short) (((Boolean) value).booleanValue() ? 1 : 0);
		else if(value instanceof Character) 
			return (short) (((Character) value).charValue());
		else if(value instanceof Short) 
			return (short) ((Short) value).shortValue();
		else if(value instanceof Integer) 
			return (short) ((Integer) value).intValue();
		else if(value instanceof Long) 
			return (short) ((Long) value).longValue();
		else if(value instanceof Float) 
			return (short) ((Float) value).floatValue();
		else if(value instanceof Double) 
			return (short) ((Double) value).doubleValue();
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	/**
	 * @return value translated as int
	 * @throws IllegalArgumentException
	 */
	public int get_int() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return (int) (((Boolean) value).booleanValue() ? 1 : 0);
		else if(value instanceof Character) 
			return (int) (((Character) value).charValue());
		else if(value instanceof Short) 
			return (int) ((Short) value).shortValue();
		else if(value instanceof Integer) 
			return (int) ((Integer) value).intValue();
		else if(value instanceof Long) 
			return (int) ((Long) value).longValue();
		else if(value instanceof Float) 
			return (int) ((Float) value).floatValue();
		else if(value instanceof Double) 
			return (int) ((Double) value).doubleValue();
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	/**
	 * @return value translated as long
	 * @throws IllegalArgumentException
	 */
	public long get_long() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return (long) (((Boolean) value).booleanValue() ? 1 : 0);
		else if(value instanceof Character) 
			return (long) (((Character) value).charValue());
		else if(value instanceof Short) 
			return (long) ((Short) value).shortValue();
		else if(value instanceof Integer) 
			return (long) ((Integer) value).intValue();
		else if(value instanceof Long) 
			return (long) ((Long) value).longValue();
		else if(value instanceof Float) 
			return (long) ((Float) value).floatValue();
		else if(value instanceof Double) 
			return (long) ((Double) value).doubleValue();
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	/**
	 * @return value translated as float
	 * @throws IllegalArgumentException
	 */
	public float get_float() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return (float) (((Boolean) value).booleanValue() ? 1 : 0);
		else if(value instanceof Character) 
			return (float) (((Character) value).charValue());
		else if(value instanceof Short) 
			return (float) ((Short) value).shortValue();
		else if(value instanceof Integer) 
			return (float) ((Integer) value).intValue();
		else if(value instanceof Long) 
			return (float) ((Long) value).longValue();
		else if(value instanceof Float) 
			return (float) ((Float) value).floatValue();
		else if(value instanceof Double) 
			return (float) ((Double) value).doubleValue();
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	/**
	 * @return value translated as double
	 * @throws IllegalArgumentException
	 */
	public double get_double() throws IllegalArgumentException {
		if(value == null)
			throw new IllegalArgumentException("No value established");
		if(value instanceof Boolean) 
			return (double) (((Boolean) value).booleanValue() ? 1 : 0);
		else if(value instanceof Character) 
			return (double) (((Character) value).charValue());
		else if(value instanceof Short) 
			return (double) ((Short) value).shortValue();
		else if(value instanceof Integer) 
			return (double) ((Integer) value).intValue();
		else if(value instanceof Long) 
			return (double) ((Long) value).longValue();
		else if(value instanceof Float) 
			return (double) ((Float) value).floatValue();
		else if(value instanceof Double) 
			return (double) ((Double) value).doubleValue();
		else 
			throw new IllegalArgumentException("Unsupport: " + value);
	}
	
	@Override
	public String toString() {
		try {
			return this.type + "::(" + expression.generate_code(true) + ")::" + this.value;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
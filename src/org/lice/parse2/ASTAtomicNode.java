package org.lice.parse2;

import org.lice.model.MetaData;
import org.lice.model.Node;
import org.lice.model.SymbolNode;
import org.lice.model.ValueNode;

public class ASTAtomicNode extends ASTNode {
	ASTAtomicNode(MetaData metaData, Token token) {
		super(metaData);
		this.token = token;
	}

	public Token getToken() { return this.token; }
	private Token token;

	@Override
	public Node accept(Sema sema) {
		switch (token.getType()) {
			case BinNumber: return new ValueNode(org.lice.parse2.Parse.toBinInt(token.getStrValue()), getMetaData());
			case OctNumber: return new ValueNode(org.lice.parse2.Parse.toOctInt(token.getStrValue()), getMetaData());
			case HexNumber: return new ValueNode(org.lice.parse2.Parse.toHexInt(token.getStrValue()), getMetaData());
			case DecNumber: return new ValueNode(Integer.parseInt(token.getStrValue()), getMetaData());
			case LongInteger: return new ValueNode(Long.parseLong(token.getStrValue()), getMetaData());
			case BigInt: return new ValueNode(org.lice.parse2.Parse.toBigInt(token.getStrValue()), getMetaData());
			case BigDec: return new ValueNode(org.lice.parse2.Parse.toBigDec(token.getStrValue()), getMetaData());
			case FloatNumber: return new ValueNode(Float.parseFloat(token.getStrValue()), getMetaData());
			case DoubleNumber: return new ValueNode(Double.parseDouble(token.getStrValue()), getMetaData());
			case StringLiteral: return new ValueNode(token.getStrValue(), getMetaData());
			case Identifier: return new SymbolNode(sema.getSymbolList(), token.getStrValue(), getMetaData());
			case LispKwd:
			case EOI:
				assert false;
				break;
		}
		return null;
	}
}

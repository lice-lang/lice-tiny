package org.lice.parse2;

import org.lice.model.MetaData;

public class ASTAtomicNode extends ASTNode {
	ASTAtomicNode(MetaData metaData, Token token) {
		super(metaData);
		this.token = token;
	}

	public Token getToken() { return this.token; }
	private Token token;
}

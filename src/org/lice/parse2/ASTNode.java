package org.lice.parse2;

import org.lice.model.MetaData;
import org.lice.model.Node;

public class ASTNode {
	ASTNode(MetaData metaData) { this.metadata = metaData; }
	public MetaData getMetaData() { return this.metadata; }
	private MetaData metadata;
	public Node accept(Sema sema) { assert false; return null; }
}

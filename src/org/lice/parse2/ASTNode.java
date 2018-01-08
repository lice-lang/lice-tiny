package org.lice.parse2;

import org.lice.model.MetaData;

public class ASTNode {
	ASTNode(MetaData metaData) { this.metadata = metaData; }
	public MetaData getMetaData() { return this.metadata; }
	private MetaData metadata;
}

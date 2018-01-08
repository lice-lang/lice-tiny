package org.lice.parse2;

import org.lice.model.MetaData;

import java.util.ArrayList;

public class ASTListNode extends ASTNode {
	ASTListNode(MetaData lParthMetaData, ArrayList<ASTNode> subNodes) {
		super(lParthMetaData);
		this.subNodes = subNodes;
	}

	public ArrayList<ASTNode> getSubNodes() { return subNodes; }

	private ArrayList<ASTNode> subNodes;
}

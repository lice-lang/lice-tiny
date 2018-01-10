package org.lice.parse2;

import org.lice.model.*;

import java.util.ArrayList;

public class ASTRootNode extends ASTNode {
	ASTRootNode(ArrayList<ASTNode> subNodes) {
		super(new MetaData());
		this.subNodes = subNodes;
	}

	private ArrayList<ASTNode> subNodes;

	@Override
	public Node accept(Sema sema) {
		ArrayList<Node> mappedSubNodes = new ArrayList<>();
		for (ASTNode subNode : subNodes) {
			mappedSubNodes.add(subNode.accept(sema));
		}
		return new ExpressionNode(
				new SymbolNode(sema.getSymbolList(), "", getMetaData()), getMetaData(), mappedSubNodes);
	}
}

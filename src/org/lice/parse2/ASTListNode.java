package org.lice.parse2;

import org.lice.model.*;

import java.util.ArrayList;
import java.util.List;

public class ASTListNode extends ASTNode {
	ASTListNode(MetaData lParthMetaData, ArrayList<ASTNode> subNodes) {
		super(lParthMetaData);
		this.subNodes = subNodes;
	}

	public ArrayList<ASTNode> getSubNodes() { return subNodes; }
	private ArrayList<ASTNode> subNodes;

	@Override
	public Node accept(Sema sema) {
		if (subNodes.size() > 0) {
			ASTNode first = subNodes.get(0);
			Node mapFirstResult = first.accept(sema);
			if (mapFirstResult instanceof ValueNode
				|| mapFirstResult instanceof ExpressionNode) {
				return mapFirstResult;
			}
			else {
				List<Node> mappedNodes = new ArrayList<>();
				for (int i = 1; i < subNodes.size(); ++i)
					mappedNodes.add(subNodes.get(i).accept(sema));
				return new ExpressionNode(mapFirstResult, getMetaData(), mappedNodes);
			}
		}
		else {
			return new EmptyNode(getMetaData());
		}
	}
}

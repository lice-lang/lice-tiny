package org.lice.parse2

import org.lice.model.*
import java.util.*

class ASTRootNode internal constructor(private val subNodes: ArrayList<ASTNode>) : ASTNode(MetaData()) {
	override fun accept(sema: Sema): Node {
		val mappedSubNodes = subNodes.map { it.accept(sema) }
		return ExpressionNode(
				SymbolNode(sema.symbolList, "", metaData), metaData, mappedSubNodes)
	}
}

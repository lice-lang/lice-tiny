package org.lice.parse2

import org.lice.model.MetaData
import org.lice.model.Node

abstract class ASTNode internal constructor(val metaData: MetaData) {
	abstract fun accept(sema: Sema): Node
}

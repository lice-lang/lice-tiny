package org.lice.core

import org.lice.model.*
import org.lice.util.InterpretException.Factory.notSymbol
import org.lice.util.InterpretException.Factory.tooFewArgument
import org.lice.util.InterpretException.Factory.typeMisMatch
import org.lice.util.cast
import org.lice.util.forceRun

fun Any?.booleanValue() = this as? Boolean ?: (this != null)

fun SymbolList.addControlFlowFunctions() {
	defineFunction("if", { metaData, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, metaData)
		val a = ls.first().eval()
		val condition = a.booleanValue()
		when {
			condition -> ls[1]
			ls.size >= 3 -> ls[2]
			else -> EmptyNode(metaData)
		}
	})
	defineFunction("when", { metaData, ls ->
		for (i in (0..ls.size - 2) step 2) {
			val a = ls[i].eval()
			val condition = a.booleanValue()
			if (condition) return@defineFunction ls[i + 1]
		}
		if (ls.size % 2 == 0) EmptyNode(metaData)
		else ls.last()
	})
	defineFunction("while", { metaData, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, metaData)
		var a = ls.first().eval()
		var ret: Node = EmptyNode(metaData)
		while (a.booleanValue()) {
			// execute loop
			ret.eval()
			ret = ls[1]
			// update a
			a = ls.first().eval()
		}
		ret
	})
}

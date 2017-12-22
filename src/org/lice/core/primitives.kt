/**
 * @author ice1000
 */
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.lang.NumberOperator
import org.lice.lang.NumberOperator.Leveler.compare
import org.lice.model.ValueNode
import org.lice.util.InterpretException.Factory.typeMisMatch
import org.lice.util.cast

fun SymbolList.addLiterals() {
	defineVariable("true", ValueNode(true))
	defineVariable("false", ValueNode(false))
	defineVariable("null", ValueNode(null))
}

fun SymbolList.addNumberFunctions() {
	provideFunctionWithMeta("/") { meta, ls ->
		val init = cast<Number>(ls.first())
		when (ls.size) {
			0 -> 1
			1 -> init
			else -> ls.drop(1)
					.fold(NumberOperator(init)) { sum, value ->
						if (value is Number) sum.div(value, meta)
						else typeMisMatch("Number", value, meta)
					}.result
		}
	}
	provideFunctionWithMeta("<") { meta, ls -> (1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) < 0 } }
	provideFunctionWithMeta(">") { meta, ls -> (1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) > 0 } }
	provideFunctionWithMeta("<=") { meta, ls -> (1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) <= 0 } }
	provideFunctionWithMeta(">=") { meta, ls -> (1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) >= 0 } }
}



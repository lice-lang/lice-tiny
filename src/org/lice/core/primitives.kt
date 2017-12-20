/**
 * @author ice1000
 */
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.model.ValueNode
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.compiler.util.cast
import org.lice.lang.NumberOperator
import org.lice.lang.NumberOperator.Leveler.compare

fun SymbolList.addLiterals() {
	defineVariable("true", ValueNode(true))
	defineVariable("false", ValueNode(false))
	defineVariable("null", ValueNode(null))
}

fun SymbolList.addNumberFunctions() {
	provideFunction("->double") { cast<Number>(it.first()).toDouble() }
	provideFunction("->int") { cast<Number>(it.first()).toInt() }
	provideFunction("->float") { cast<Number>(it.first()).toFloat() }
	provideFunction("->long") { cast<Number>(it.first()).toLong() }
	provideFunctionWithMeta("+") { meta, list ->
		list.fold(NumberOperator(0)) { sum, value ->
			if (value is Number) sum.plus(value, meta)
			else typeMisMatch("Number", value, meta)
		}.result
	}
	provideFunctionWithMeta("-") { meta, ls ->
		when (ls.size) {
			0 -> 0
			1 -> ls.first()
			else -> ls.drop(1)
					.fold(NumberOperator(ls.first() as Number)) { sum, value ->
						if (value is Number) sum.minus(value, meta)
						else typeMisMatch("Number", value, meta)
					}.result
		}
	}
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
	provideFunctionWithMeta("%") { meta, ls ->
		when (ls.size) {
			0 -> 0
			1 -> ls.first()
			else -> ls.drop(1)
					.fold(NumberOperator(cast(ls.first()))) { sum, value ->
						if (value is Number) sum.rem(value, meta)
						else typeMisMatch("Number", value, meta)
					}.result
		}
	}
	provideFunctionWithMeta("*") { ln, ls ->
		ls.fold(NumberOperator(1)) { sum, value ->
			if (value is Number) sum.times(value, ln)
			else typeMisMatch("Number", value, ln)
		}.result
	}
	provideFunction("===") { (1 until it.size).all { i -> it[i] == it[i - 1] } }
	provideFunction("!==") { (1 until it.size).none { i -> it[i] == it[i - 1] } }
	provideFunctionWithMeta("==") { meta, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) == 0 }
	}
	provideFunctionWithMeta("!=") { meta, ls ->
		(1 until ls.size).none { compare(ls[it - 1] as Number, ls[it] as Number, meta) == 0 }
	}
	provideFunctionWithMeta("<") { meta, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) < 0 }
	}
	provideFunctionWithMeta(">") { meta, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) > 0 }
	}
	provideFunctionWithMeta("<=") { meta, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) <= 0 }
	}
	provideFunctionWithMeta(">=") { meta, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, meta) >= 0 }
	}
	provideFunction("&") { paramList ->
		paramList.map { cast<Number>(it).toInt() }.reduce { last, self -> last and self }
	}
	provideFunction("|") { paramList ->
		paramList.map { cast<Number>(it).toInt() }.reduce { last, self -> last or self }
	}
	provideFunction("^") { paramList ->
		paramList.map { cast<Number>(it).toInt() }.reduce { last, self -> last xor self }
	}
	provideFunction("~") { cast<Number>(it.first()).toInt().inv() }
}

fun SymbolList.addBoolFunctions() {
	provideFunction("&&") { ls -> ls.all(Any?::booleanValue) }
	provideFunction("||") { ls -> ls.any(Any?::booleanValue) }
	provideFunction("!") { ls -> ls.first().booleanValue().not() }
}



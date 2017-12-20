/**
 * @author ice1000
 */
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.lang.NumberOperator
import org.lice.lang.NumberOperator.Leveler.compare

fun SymbolList.addLiterals() {
	defineVariable("true", true)
	defineVariable("false", false)
	defineVariable("null", null)
}

fun SymbolList.addNumberFunctions() {
	provideFunction("->double") { (it.first() as Number).toDouble() }
	provideFunction("->int") { (it.first() as Number).toInt() }
	provideFunction("->float") { (it.first() as Number).toFloat() }
	provideFunction("->long") { (it.first() as Number).toLong() }
	provideFunctionWithMeta("+") { meta, list ->
		list.fold(NumberOperator(0)) { sum, value ->
			when (value) {
				is Number -> sum.plus(value, meta)
				else -> typeMisMatch("Number", value, meta)
			}
		}.result
	}
	provideFunctionWithMeta("-") { meta, ls ->
		when (ls.size) {
			0 -> 0
			1 -> ls.first()
			else -> ls.drop(1)
					.fold(NumberOperator(ls.first() as Number)) { sum, value ->
						when (value) {
							is Number -> sum.minus(value, meta)
							else -> typeMisMatch("Number", value, meta)
						}
					}.result
		}
	}
	provideFunctionWithMeta("/") { meta, ls ->
		val init = ls.first() as Number
		when (ls.size) {
			0 -> 1
			1 -> init
			else -> ls.drop(1)
					.fold(NumberOperator(init)) { sum, value ->
						when (value) {
							is Number -> sum.div(value, meta)
							else -> typeMisMatch("Number", value, meta)
						}
					}.result
		}
	}
	provideFunctionWithMeta("%") { meta, ls ->
		when (ls.size) {
			0 -> 0
			1 -> ls.first()
			else -> ls.drop(1)
					.fold(NumberOperator(ls.first() as Number)) { sum, value ->
						when (value) {
							is Number -> sum.rem(value, meta)
							else -> typeMisMatch("Number", value, meta)
						}
					}.result
		}
	}
	provideFunctionWithMeta("*") { ln, ls ->
		ls.fold(NumberOperator(1)) { sum, value ->
			when (value) {
				is Number -> sum.times(value, ln)
				else -> typeMisMatch("Number", value, ln)
			}
		}.result
	}
	provideFunction("===") { (1 until it.size).all { i -> it[i] == it[i - 1] } }
	provideFunction("!==") { (1 until it.size).none { i -> it[i] == it[i - 1] } }
	provideFunctionWithMeta("==") { ln, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, ln) == 0 }
	}
	provideFunctionWithMeta("!=") { ln, ls ->
		(1 until ls.size).none { compare(ls[it - 1] as Number, ls[it] as Number, ln) == 0 }
	}
	provideFunctionWithMeta("<") { ln, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, ln) < 0 }
	}
	provideFunctionWithMeta(">") { ln, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, ln) > 0 }
	}
	provideFunctionWithMeta("<=") { ln, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, ln) <= 0 }
	}
	provideFunctionWithMeta(">=") { ln, ls ->
		(1 until ls.size).all { compare(ls[it - 1] as Number, ls[it] as Number, ln) >= 0 }
	}
	provideFunction("&") { paramList ->
		paramList
				.map { (it as Number).toInt() }
				.reduce { last, self ->
					last and self
				}
	}
	provideFunction("|") { paramList ->
		paramList
				.map { (it as Number).toInt() }
				.reduce { last, self ->
					last or self
				}
	}
	provideFunction("^") { paramList ->
		paramList
				.map { (it as Number).toInt() }
				.reduce { last, self ->
					last xor self
				}
	}
	provideFunction("~") { (it.first() as Number).toInt().inv() }
}

inline fun SymbolList.addBoolFunctions() {
	provideFunctionWithMeta("&&") { ln, ls -> ls.all { it.booleanValue() } }
	provideFunctionWithMeta("||") { ln, ls -> ls.any { it.booleanValue() } }
}



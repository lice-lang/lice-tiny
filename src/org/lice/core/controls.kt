package org.lice.core

import org.lice.compiler.parse.*
import org.lice.compiler.util.InterpretException
import org.lice.compiler.util.InterpretException.Factory.tooFewArgument
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch

fun SymbolList.addMathFunctions() {
	provideFunction("sqrt") { Math.sqrt((it.first() as Number).toDouble()) }
	provideFunction("cbrt") { Math.cbrt((it.first() as Number).toDouble()) }
	provideFunction("sin") { Math.sin((it.first() as Number).toDouble()) }
	provideFunction("rand") { Math.random() }
}

fun SymbolList.addStringFunctions() {
	provideFunction("->str") { it.first().toString() }
	provideFunctionWithMeta("str->int") { ln, ls ->
		val res = ls.first().toString()
		when {
			res.isOctInt() -> res.toOctInt()
			res.isInt() -> res.toInt()
			res.isBinInt() -> res.toBinInt()
			res.isHexInt() -> res.toHexInt()
			else -> throw InterpretException("give string: \"$res\" cannot be parsed as a number!", ln)
		}
	}
	provideFunctionWithMeta("int->hex") { ln, ls ->
		val a = ls.first()
		if (a is Number) "0x${Integer.toHexString(a.toInt())}"
		else typeMisMatch("Int", a, ln)
	}
	provideFunctionWithMeta("int->bin") { ln, ls ->
		val a = ls.first()
		if (a is Number) "0b${Integer.toBinaryString(a.toInt())}"
		else typeMisMatch("Int", a, ln)
	}
	provideFunctionWithMeta("int->oct") { ln, ls ->
		val a = ls.first()
		if (a is Number) "0${Integer.toOctalString(a.toInt())}"
		else typeMisMatch("Int", a, ln)
	}
	provideFunction("str-con") { it.joinToString(transform = Any?::toString, separator = "") }
	provideFunctionWithMeta("format") { ln, ls ->
		if (ls.isEmpty()) tooFewArgument(1, ls.size, ln)
		val format = ls.first().toString()
		String.format(format, *ls.subList(1, ls.size).toTypedArray())
	}
}

package org.lice.core

import org.lice.parse.*
import org.lice.util.InterpretException
import org.lice.util.InterpretException.Factory.tooFewArgument
import org.lice.util.InterpretException.Factory.typeMisMatch

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

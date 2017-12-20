package org.lice.core

import org.lice.compiler.parse.*
import org.lice.compiler.util.InterpretException
import org.lice.compiler.util.InterpretException.Factory.tooFewArgument
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch


fun SymbolList.addMathFunctions() {
	provideFunction("sqrt") { Math.sqrt((it.first() as Number).toDouble()) }
	provideFunction("cbrt") { Math.cbrt((it.first() as Number).toDouble()) }
	provideFunction("sin") { Math.sin((it.first() as Number).toDouble()) }
	provideFunction("sinh") { Math.sinh((it.first() as Number).toDouble()) }
	provideFunction("cosh") { Math.cosh((it.first() as Number).toDouble()) }
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
		when (a) {
			is Int -> "0x${Integer.toHexString(a)}"
			else -> typeMisMatch("Int", a, ln)
		}
	}
	provideFunctionWithMeta("int->bin") { ln, ls ->
		val a = ls.first()
		when (a) {
			is Int -> "0b${Integer.toBinaryString(a)}"
			else -> typeMisMatch("Int", a, ln)
		}
	}
	provideFunctionWithMeta("int->oct") { ln, ls ->
		val a = ls.first()
		when (a) {
			is Int -> "0${Integer.toOctalString(a)}"
			else -> typeMisMatch("Int", a, ln)
		}
	}
	provideFunction("str-con") {
		it.fold(StringBuilder(it.size)) { sb, value -> sb.append(value.toString()) }.toString()
	}
	provideFunctionWithMeta("format") { ln, ls ->
		if (ls.isEmpty()) tooFewArgument(1, ls.size, ln)
		val format = ls.first()
		when (format) {
			is String -> String.format(format, *ls.subList(1, ls.size).toTypedArray())
			else -> typeMisMatch("String", format, ln)
		}
	}
	provideFunction("->chars") {
		it.fold(StringBuilder(it.size)) { sb, value ->
			sb.append(value.toString())
		}.toString().toCharArray().toList()
	}
	provideFunction("split") { ls ->
		val str = ls.first()
		val regex = ls[1]
		str.toString().split(regex.toString()).toList()
	}
}

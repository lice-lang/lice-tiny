package org.lice.core

import org.lice.lang.Echoer
import org.lice.model.*
import org.lice.parse.*
import org.lice.util.InterpretException.Factory.notSymbol
import org.lice.util.InterpretException.Factory.tooFewArgument
import org.lice.util.InterpretException.Factory.typeMisMatch
import org.lice.util.cast
import org.lice.util.forceRun
import java.io.File

fun Any?.booleanValue() = this as? Boolean ?: (this != null)

fun SymbolList.addGetSetFunction() {
	defineFunction("->", { metaData, ls ->
		if (ls.size < 2) tooFewArgument(2, ls.size, metaData)
		var str = cast<SymbolNode>(ls.first()).name
		while (true) {
			if (isVariableDefined(str)) {
				val v = getVariable(str)
				if (v is SymbolNode) str = v.name
				else break
			} else break
		}
		val v = ls[1]
		defineVariable(str, ValueNode(v.eval()))
		ls.first()
	})
}

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

fun SymbolList.addStandard() {
	defineFunction("def?") { metaData, ls ->
		val a = (ls.first() as? SymbolNode ?: notSymbol(metaData)).name
		ValueNode(isVariableDefined(a), metaData)
	}
	defineFunction("undef") { metaData, ls ->
		val a = (ls.first() as? SymbolNode ?: notSymbol(metaData)).name
		ValueNode(null != removeVariable(a), metaData)
	}
	defineFunction("alias") { meta, ls ->
		val a = getVariable(cast<SymbolNode>(ls.first()).name)
		a?.let { function ->
			ls.forEachIndexed { index, _ ->
				val name = cast<SymbolNode>(ls[index]).name
				if (index != 0) {
					if (function is Node) defineVariable(name, function)
					else defineFunction(name, cast(function))
				}
			}
		}
		ValueNode(null != a, meta)
	}

	provideFunctionWithMeta("eval") { _, ls ->
		val value = ls.first().toString()
		mapAst(buildNode(value), symbolList = this).eval()
	}
	provideFunction("type") { ls -> ls.first()?.javaClass ?: Nothing::class.java }
	provideFunction("|>") { it.lastOrNull() }
	defineFunction("force|>") { ln, ls ->
		var ret: Any? = null
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret, ln)
	}

	provideFunctionWithMeta("load-file") { _, ls ->
		createRootNode(File(ls.first().toString()), this)
	}
	provideFunction("print") { ls -> ls.forEach { Echoer.echo(it) } }

	provideFunction("exit") { System.exit(0) }
	defineFunction("str->sym") { ln, ls -> SymbolNode(this, ls.first().eval().toString(), ln) }
	defineFunction("sym->str") { ln, ls ->
		val a = ls.first()
		if (a is SymbolNode) ValueNode(a.name, ln)
		else typeMisMatch("Symbol", a, ln)
	}
}
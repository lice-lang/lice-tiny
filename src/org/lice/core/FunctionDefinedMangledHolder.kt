package org.lice.core

import org.lice.model.*
import org.lice.util.*

@Suppress("unused")
/**
 * `$` in the function names will be replaced with `>`.
 * @author ice1000
 */
class FunctionDefinedMangledHolder(val symbolList: SymbolList) {
	fun `def?`(metaData: MetaData, ls: List<Node>): Node {
		val a = (ls.first() as? SymbolNode ?: InterpretException.notSymbol(metaData)).name
		return ValueNode(symbolList.isVariableDefined(a), metaData)
	}

	fun undef(metaData: MetaData, ls: List<Node>): Node {
		val a = (ls.first() as? SymbolNode ?: InterpretException.notSymbol(metaData)).name
		return ValueNode(null != symbolList.removeVariable(a), metaData)
	}

	fun alias(meta: MetaData, ls: List<Node>): Node {
		val a = symbolList.getVariable(cast<SymbolNode>(ls.first()).name)
		a?.let { function ->
			ls.forEachIndexed { index, _ ->
				val name = cast<SymbolNode>(ls[index]).name
				if (index != 0) {
					if (function is Node) symbolList.defineVariable(name, function)
					else symbolList.defineFunction(name, cast(function))
				}
			}
		}
		return ValueNode(null != a, meta)
	}

	fun `force|$`(meta: MetaData, ls: List<Node>): Node {
		var ret: Any? = null
		forceRun { ls.forEach { node -> ret = node.eval() } }
		return ValueNode(ret, meta)
	}

	fun `str-$sym`(ln: MetaData, ls: List<Node>) = SymbolNode(symbolList, ls.first().eval().toString(), ln)
	fun `sym-$str`(ln: MetaData, ls: List<Node>): Node {
		val a = ls.first()
		return if (a is SymbolNode) ValueNode(a.name, ln)
		else InterpretException.typeMisMatch("Symbol", a, ln)
	}

	fun `-$`(metaData: MetaData, ls: List<Node>): Node {
		if (ls.size < 2) InterpretException.tooFewArgument(2, ls.size, metaData)
		var str = cast<SymbolNode>(ls.first()).name
		while (true) {
			if (symbolList.isVariableDefined(str)) {
				val v = symbolList.getVariable(str)
				if (v is SymbolNode) str = v.name
				else break
			} else break
		}
		val v = ls[1]
		symbolList.defineVariable(str, ValueNode(v.eval()))
		return ls.first()
	}
}
/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.model.*

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

@SinceKotlin("1.1")
typealias ProvidedFuncWithMeta = (MetaData, List<Any?>) -> Any?

@SinceKotlin("1.1")
typealias ProvidedFunc = (List<Any?>) -> Any?

class SymbolList
@JvmOverloads
constructor(init: Boolean = true) {
	val variables: MutableMap<String, Any?> = mutableMapOf()

	init {
		if (init) initialize()
	}

	private fun initialize() {
		addMathFunctions()
		addStandard()
		addDefines()
		addGetSetFunction()
		addControlFlowFunctions()
		addNumberFunctions()
		addLiterals()
		addStringFunctions()
		addBoolFunctions()
	}

	fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta) =
			defineFunction(name) { meta, ls ->
				val value = node(meta, ls.map { it.eval() })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			}

	fun provideFunction(name: String, node: ProvidedFunc) =
			defineFunction(name) { meta, ls ->
				val value = node(ls.map { it.eval() })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			}

	fun defineVariable(name: String, node: Node) = variables.put(name, node)
	fun defineFunction(name: String, node: Func) = variables.put(name, node)
	fun isVariableDefined(name: String): Boolean = variables.containsKey(name)
	fun removeVariable(name: String) = variables.remove(name)
	fun getVariable(name: String) = variables[name]
}

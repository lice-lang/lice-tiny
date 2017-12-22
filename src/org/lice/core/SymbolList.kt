/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.lang.Echoer
import org.lice.model.*
import org.lice.util.className
import java.util.function.Consumer

class SymbolList
@JvmOverloads
constructor(init: Boolean = true) {
	val variables: MutableMap<String, Any?> = hashMapOf()

	init {
		defineFunction("") { _, ls ->
			var ret: Any? = null
			ls.forEach {
				val res = it.eval()
				ret = res
				Echoer.replEcholn("${res.toString()} => ${res.className()}")
			}
			ValueNode(ret)
		}
		if (init) initialize()
	}

	private fun initialize() {
		addStandard()
		addDefines()
		addGetSetFunction()
		addControlFlowFunctions()
		addNumberFunctions()
		addLiterals()
		addStringFunctions()
		val withMetaHolders = FunctionWithMetaHolders(this)
		withMetaHolders.javaClass.declaredMethods.forEach { method ->
			provideFunctionWithMeta(method.name) { meta, list -> method.invoke(withMetaHolders, meta, list) }
		}
		val holders = FunctionHolders(this)
		holders.javaClass.declaredMethods.forEach { method ->
			provideFunction(method.name) { list -> method.invoke(holders, list) }
		}
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

	companion object {
		@JvmStatic
		fun with(init: Consumer<SymbolList>) = SymbolList().also { init.accept(it) }

		@JvmStatic
		fun with(init: SymbolList.() -> Unit) = SymbolList().also { init(it) }
	}
}

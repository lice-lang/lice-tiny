/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.lang.Echoer
import org.lice.model.*
import org.lice.model.MetaData.Factory.EmptyMetaData
import org.lice.util.*

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
		addDefines()
		addLiterals()
		bindMethodsWithMetaOf(FunctionWithMetaHolders(this))
		bindMethodsOf(FunctionHolders(this))
		val definedMangledHolder = FunctionDefinedMangledHolder(this)
		definedMangledHolder.javaClass.declaredMethods.forEach { method ->
			defineFunction(method.name.mangleA()) { meta, list ->
				cast(runReflection { method.invoke(definedMangledHolder, meta, list) })
			}
		}
		val mangledHolder = FunctionMangledHolder(this)
		mangledHolder.javaClass.declaredMethods.forEach { method ->
			provideFunctionWithMeta(method.name.mangleB()) { meta, list ->
				runReflection { method.invoke(mangledHolder, meta, list) }
			}
		}
	}

	fun bindMethodsWithMetaOf(any: Any) = any.javaClass.declaredMethods.forEach { method ->
		provideFunctionWithMeta(method.name) { meta, list -> runReflection { method.invoke(any, meta, list) } }
	}

	fun bindMethodsOf(any: Any) = any.javaClass.declaredMethods.forEach { method ->
		provideFunction(method.name) { list -> runReflection { method.invoke(any, list) } }
	}

	fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta) =
			defineFunction(name) { meta, ls ->
				val value = node(meta, ls.map { it.eval() })
				if (value != null) ValueNode(value, meta)
				else ValueNode(null, meta)
			}

	fun provideFunction(name: String, node: ProvidedFunc) =
			defineFunction(name) { meta, ls ->
				val value = node(ls.map { it.eval() })
				if (value != null) ValueNode(value, meta) else ValueNode(null, meta)
			}

	fun defineVariable(name: String, node: Node) = variables.put(name, node)
	fun defineFunction(name: String, node: Func) = variables.put(name, node)
	fun isVariableDefined(name: String): Boolean = variables.containsKey(name)
	fun removeVariable(name: String) = variables.remove(name)
	fun getVariable(name: String) = variables[name]

	private fun addLiterals() {
		defineVariable("true", ValueNode(true))
		defineVariable("false", ValueNode(false))
		defineVariable("null", ValueNode(null))
	}

	fun extractLiceFunction(name: String): ProvidedFunc
			= { ls -> (getVariable(name) as Func)(EmptyMetaData, ls.map { ValueNode(it) }) }

	fun extractLiceVariable(name: String): Any? = (getVariable(name) as Node).eval()

	companion object {
		val preludeVariables = listOf("null", "true", "false")
		val preludeSymbols by lazy {
			listOf(
					FunctionHolders::class.java.declaredMethods.map { it.name },
					FunctionDefinedMangledHolder::class.java.declaredMethods.map { it.name.mangleA() },
					FunctionMangledHolder::class.java.declaredMethods.map { it.name.mangleB() },
					FunctionWithMetaHolders::class.java.declaredMethods.map { it.name },
					listOf("def", "defexpr", "deflazy", "lambda", "expr", "lazy")
			).flatMap { it }
		}

		private fun String.mangleA() = replace('$', '>')
		private fun String.mangleB() = replace('$', '>').replace('&', '<').replace('_', '/')
	}
}

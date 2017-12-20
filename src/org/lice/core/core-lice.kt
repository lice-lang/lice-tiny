/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.model.*
import org.lice.compiler.util.InterpretException.Factory.notSymbol
import org.lice.compiler.util.InterpretException.Factory.numberOfArgumentNotMatch
import org.lice.compiler.util.InterpretException.Factory.tooFewArgument
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.compiler.util.cast
import org.lice.lang.DefineResult

fun SymbolList.addDefines() {
	fun defFunc(name: String, params: ParamList, block: Mapper<Node>, body: Node) {
		defineFunction(name) { meta, args ->
			val backup = params.map(this::getVariable)
			if (args.size != params.size)
				numberOfArgumentNotMatch(params.size, args.size, meta)
			args.map(block).forEachIndexed { index, obj -> defineVariable(params[index], obj) }
			val ret = ValueNode(body.eval(), meta)
			backup.forEachIndexed { index, node ->
				@Suppress("UNCHECKED_CAST")
				when (node) {
					is Node -> defineVariable(params[index], node)
					null -> removeVariable(params[index])
					else -> defineFunction(params[index], cast(node))
				}
			}
			ret
		}
	}

	fun definer(funName: String, block: Mapper<Node>) {
		defineFunction(funName) { meta, ls ->
			if (ls.size < 2) tooFewArgument(2, ls.size, meta)
			val name = cast<SymbolNode>(ls.first()).name
			val body = ls.last()
			val params = ls.subList(1, ls.size - 1)
					.map { (it as? SymbolNode)?.name ?: notSymbol(meta) }
			val override = isVariableDefined(name)
			defFunc(name, params, block, body)
			return@defineFunction ValueNode(DefineResult("${if (override) "overridden" else "defined"}: $name"))
		}
	}
	definer("def") { node -> ValueNode(node.eval()) }
	definer("deflazy") { node -> LazyValueNode({ node.eval() }) }
	definer("defexpr") { it }
	val lambdaDefiner = { funName: String, mapper: Mapper<Node> ->
		defineFunction(funName) { meta, ls ->
			if (ls.isEmpty()) tooFewArgument(1, ls.size, meta)
			val body = ls.last()
			val params = ls
					.subList(0, ls.size - 1)
					.map { (it as? SymbolNode)?.name ?: typeMisMatch("Symbol", it.eval(), meta) }
			val name = lambdaNameGen()
			defFunc(name, params, mapper, body)
			SymbolNode(this, name, meta)
		}
	}
	lambdaDefiner("lambda") { node -> ValueNode(node.eval()) }
	lambdaDefiner("lazy") { node -> LazyValueNode({ node.eval() }) }
	lambdaDefiner("expr") { it }
}


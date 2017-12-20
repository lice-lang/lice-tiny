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
import org.lice.compiler.model.Node.Objects.getNullNode
import org.lice.compiler.parse.*
import org.lice.compiler.util.InterpretException
import org.lice.compiler.util.InterpretException.Factory.numberOfArgumentNotMatch
import org.lice.compiler.util.InterpretException.Factory.tooFewArgument
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.compiler.util.forceRun
import org.lice.lang.*
import java.io.File
import java.net.URL
import kotlin.concurrent.thread

@SinceKotlin("1.1")
typealias ParamList = List<String>

@SinceKotlin("1.1")
typealias Mapper<T> = (T) -> T

private var lambdaNameCounter = -100

internal fun lambdaNameGen() = "\t${++lambdaNameCounter}"

fun Any?.booleanValue() = this as? Boolean ?: (this != null)

fun SymbolList.addStandard() {
	fun defFunc(name: String, params: ParamList, block: Mapper<Node>, body: Node) {
		defineVariable(name) { ln, args ->
			val backup = params.map { getVariable(it) }
			if (args.size != params.size)
				numberOfArgumentNotMatch(params.size, args.size, ln)
			args.map(block).forEachIndexed { index, obj ->
				if (obj is SymbolNode) defineFunction(params[index], obj.function())
				else defineFunction(params[index], { _, _ -> obj })
			}
			val ret = ValueNode(body.eval().o ?: Nullptr, ln)
			backup.forEachIndexed { index, node ->
				if (node != null) defineFunction(params[index], node)
				else removeVariable(params[index])
			}
			ret
		}
	}
	fun definer(funName: String, block: Mapper<Node>) {
		defineFunction(funName) { meta, ls ->
			if (ls.size < 2) tooFewArgument(2, ls.size, meta)
			val name = (ls.first() as SymbolNode).name
			val body = ls.last()
			val params = ls.subList(1, ls.size - 1)
					.map { (it as? SymbolNode)?.name ?: InterpretException.notSymbol(meta) }
			val override = isVariableDefined(name)
			defFunc(name, params, block, body)
			return@defineFunction ValueNode(DefineResult("${if (override) "overridden" else "defined"}: $name"))
		}
	}
	definer("def") { node -> ValueNode(node.eval().o ?: Nullptr) }
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
	lambdaDefiner("lambda") { node -> ValueNode(node.eval().o ?: Nullptr) }
	lambdaDefiner("lazy") { node -> LazyValueNode({ node.eval() }) }
	lambdaDefiner("expr") { it }
}



/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package org.lice.compiler.model

import org.lice.compiler.model.MetaData.Factory.EmptyMetaData
import org.lice.compiler.util.InterpretException.Factory.notFunction
import org.lice.compiler.util.ParseException.Factory.undefinedVariable
import org.lice.compiler.util.className
import org.lice.core.Func
import org.lice.core.SymbolList

class MetaData(
		val beginLine: Int = -1,
		val beginColumn: Int = -1,
		val endLine: Int = -1,
		val endColumn: Int = -1) {
	val lineNumber: Int get() = beginLine

	companion object Factory {
		val EmptyMetaData = MetaData(-1)
	}
}

interface Node {
	fun eval(): Any?
	val meta: MetaData
	override fun toString(): String

	companion object Objects {
		fun getNullNode(meta: MetaData) = EmptyNode(meta)
	}
}

class ValueNode
@JvmOverloads
constructor(val value: Any?, override val meta: MetaData = EmptyMetaData) : Node {
	override fun eval() = value
	override fun toString() = "value: <$value> => ${value.className()}"
}

class LazyValueNode
//@JvmOverloads
constructor(
		lambda: () -> Any?,
		override val meta: MetaData = EmptyMetaData) : Node {
	val value by lazy(lambda)
	override fun eval() = value
	override fun toString() = "lazy: <$value>"
}

class ExpressionNode(
		val node: Node,
		override val meta: MetaData,
		val params: List<Node>) : Node {

	override fun eval() = (node.eval() as? Func ?: notFunction(meta)).invoke(meta, params).eval()
	override fun toString() = "function with ${params.size} params"
}

class SymbolNode(
		val symbolList: SymbolList,
		val name: String,
		override val meta: MetaData) : Node {
	override fun eval() =
			if (symbolList.isVariableDefined(name)) symbolList.getVariable(name).let {
				if (it is Node) it.eval() else it
			} else undefinedVariable(name, meta)

	override fun toString() = "symbol: <$name>"
}

class EmptyNode(override val meta: MetaData) : Node {
	override fun eval() = null
	override fun toString() = "null"
}


/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.parse

import org.lice.core.SymbolList
import org.lice.model.*
import java.io.File

fun String.isString() = first() == '\"' && last() == '\"'

/**
 * This is the core implementation of mapAst
 *
 * @param str the string to parse
 * @return parsed node
 */
fun parseValue(
		str: String,
		meta: MetaData): Node? = when {
	str.isBlank() -> null
	str.isString() -> ValueNode(str.substring(startIndex = 1, endIndex = str.length - 1), meta)
	str.isOctInt() -> ValueNode(str.toOctInt(), meta)
	str.isInt() -> ValueNode(str.toInt(), meta)
	str.isHexInt() -> ValueNode(str.toHexInt(), meta)
	str.isBinInt() -> ValueNode(str.toBinInt(), meta)
	str.isBigInt() -> ValueNode(str.toBigInt(), meta)
	str.isBigDec() -> ValueNode(str.toBigDec(), meta)
	else -> try {
		if (str.length < 0xF) ValueNode(str.toFloat(), meta)
		else ValueNode(str.toDouble(), meta)
	} catch (e: Throwable) {
		null
	}
}

/**
 * map the string tree, making it a real ast
 *
 * @param symbolList the symbol list
 * @param node the node to parse
 * @return the parsed node
 */
fun mapAst(
		node: StringNode,
		symbolList: SymbolList = SymbolList()): Node = when (node) {
	is StringMiddleNode -> {
		val fst = node.list.first()
		mapAst(fst, symbolList) as? ValueNode ?: ExpressionNode(node = mapAst(fst, symbolList), meta = node.meta,
				params = node.list.drop(1).map { mapAst(it, symbolList) })
	}
	is StringLeafNode -> wrapValue(node, symbolList)
	else -> EmptyNode(node.meta)
}

fun wrapValue(node: StringLeafNode, symbolList: SymbolList): Node =
		parseValue(str = node.str, meta = node.meta) ?:
				SymbolNode(symbolList = symbolList, name = node.str, meta = node.meta)

/**
 * create an executable node
 *
 * @param file source code file
 * @param symbolList symbol list, with a default value
 * @return generated root node of the ast
 */
fun createRootNode(file: File, symbolList: SymbolList = SymbolList(init = true)): Node {
	val code = file.readText()
	val fp = "FILE_PATH"
	symbolList.defineVariable(fp, ValueNode(file.absolutePath))
	val stringTreeRoot = buildNode(code)
	return mapAst(node = stringTreeRoot, symbolList = symbolList)
}
package org.lice.core

import org.lice.model.MetaData
import org.lice.util.cast

/**
 * `$` in the function names will be replaced with `>`,
 * `_` in the function names will be replaced with `/`.
 * @author ice1000
 */
class FunctionMangledHolder(val symbolList: SymbolList) {
	fun `|$`(meta: MetaData, ls: List<Any?>) = ls.lastOrNull()
	fun `-$str`(meta: MetaData, it: List<Any?>) = it.first().toString()
	fun `-$double`(meta: MetaData, it: List<Any?>) = cast<Number>(it.first()).toDouble()
	fun `-$int`(meta: MetaData, it: List<Any?>) = cast<Number>(it.first()).toInt()
}
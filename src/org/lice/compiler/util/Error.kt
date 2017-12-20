/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass
@file:Suppress("NOTHING_TO_INLINE")

package org.lice.compiler.util

import org.lice.compiler.model.MetaData
import org.lice.lang.Echoer

class ParseException(string: String) : RuntimeException(string) {
	constructor(string: String, meta: MetaData) : this("$string\nat line: ${meta.lineNumber}")

	companion object Factory {
		fun undefinedVariable(name: String, meta: MetaData): Nothing
				= throw ParseException("undefined variable: $name", meta)
	}
}

class InterpretException(string: String) : RuntimeException(string) {
	constructor(string: String, meta: MetaData) : this("$string\nat line: ${meta.lineNumber}")

	companion object Factory {
		fun notSymbol(meta: MetaData): Nothing =
				throw InterpretException("type mismatch: symbol expected.", meta)

		fun notFunction(meta: MetaData): Nothing =
				throw InterpretException("type mismatch: function expected.", meta)

		fun typeMisMatch(expected: String, actual: Any?, meta: MetaData): Nothing =
				throw InterpretException("type mismatch: expected: $expected, found: ${actual?.className()}", meta)

		fun tooFewArgument(expected: Int, actual: Int, meta: MetaData): Nothing =
				throw InterpretException("expected $expected or more arguments, found: $actual", meta)

		fun numberOfArgumentNotMatch(expected: Int, actual: Int, meta: MetaData): Nothing =
				throw InterpretException("expected $expected arguments, found: $actual", meta)
	}
}

fun showError(string: String, exit: Boolean = false) {
	if (exit)
		throw RuntimeException(string)
	else
		Echoer.echolnErr(string)
}

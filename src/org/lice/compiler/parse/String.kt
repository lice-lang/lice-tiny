/**
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.compiler.parse

fun String.isString() = first() == '\"' && last() == '\"'

fun String.repeat(times: Int): String {
	val sb = StringBuilder()
	for (i in 1..times) sb.append(this)
	return sb.toString()
}

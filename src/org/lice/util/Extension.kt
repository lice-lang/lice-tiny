/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass

package org.lice.util

import java.lang.reflect.InvocationTargetException

inline fun forceRun(block: () -> Unit) {
	try {
		block()
	} catch (e: Throwable) {
	}
}

fun Any?.className(): String = if (null != this) this.javaClass.name else "NullType"

@Suppress("UNCHECKED_CAST")
fun <R> cast(any: Any?) = any as R

inline fun <T> runReflection(block: () -> T) = try {
	block()
} catch (e: InvocationTargetException) {
	var exception = e.targetException
	while (exception is InvocationTargetException) exception = exception.targetException
	throw exception
}

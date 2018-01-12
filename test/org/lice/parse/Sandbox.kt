package org.lice.parse

import org.junit.Test

class Sandbox {
	@Test
	fun run() {
		val src = """
"""
		val l = Lexer(src)
		Parser.parseTokenStream(l).accept(Sema()).eval()
	}
}

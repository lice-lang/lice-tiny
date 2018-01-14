package org.lice.parse

import org.junit.Test

class Sandbox {
	@Test
	fun run() {
		val src = """
			(print ((if true + -) 111 111))
"""
		val l = Lexer(src)
		Parser.parseTokenStream(l).accept(Sema()).eval()
	}
}

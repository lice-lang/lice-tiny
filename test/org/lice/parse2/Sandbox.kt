package org.lice.parse2

import org.junit.Test

class Sandbox {
	@Test(timeout = 1000)
	fun run() {
		val src = """
			(print ((if true + -) 111 111))
"""
		val l = Lexer(src)
		Parser.parseTokenStream(l).accept(Sema()).eval()
	}
}

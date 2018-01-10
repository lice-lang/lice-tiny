package org.lice.parse2

import org.junit.Test
import org.lice.Lice
import org.lice.parse.createRootNode

class Sandbox {
	@Test(timeout = 1000)
	fun run() {
		val src = """
"""
		val l = Lexer(src)
		Parser.parseTokenStream(l).accept(Sema()).eval()
	}
}

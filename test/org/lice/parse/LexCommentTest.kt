package org.lice.parse

import org.junit.Test

import org.junit.Assert.assertEquals

class LexCommentTest {
	@Test
	fun testLexComment() {
		val src = "; Simply comments"
		val l = Lexer(src)
		assertEquals(Token.TokenType.EOI, l.currentToken().type)
	}

	@Test
	fun testLexComment2() {
		val src = "; Simply comments\n@dentifier"
		val l = Lexer(src)
		assertEquals(Token.TokenType.Identifier, l.currentToken().type)
		assertEquals("@dentifier", l.currentToken().strValue)
		assertEquals(Token.TokenType.EOI, l.peekOneToken().type)
	}
}

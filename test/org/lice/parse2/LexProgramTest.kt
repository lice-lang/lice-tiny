package org.lice.parse2

import org.junit.Test

import org.junit.Assert.assertEquals

class LexProgramTest {
	@Test(timeout = 100)
	fun test1() {
		val src = """
			(def add a b (+ a b))
			(add 12.45M 13.14M)
			""".trimIndent()
		val l = Lexer(src)
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, "(")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "def")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "add")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "a")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "b")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, "(")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "+")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "a")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "b")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, ")")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, ")")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, "(")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "add")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.BigDec)
		assertEquals(l.currentToken().strValue, "12.45")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.BigDec)
		assertEquals(l.currentToken().strValue, "13.14")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, ")")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.EOI)
		l.nextToken()
	}

	@Test(timeout = 100)
	fun test2() {
		val src = """
			(string-add "String" ; comments
			"Liter
			al")
		""".trimIndent()
		val l = Lexer(src)
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, "(")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.Identifier)
		assertEquals(l.currentToken().strValue, "string-add")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.StringLiteral)
		assertEquals(l.currentToken().strValue, "String")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.StringLiteral)
		assertEquals(l.currentToken().strValue, "Liter\nal")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.LispKwd)
		assertEquals(l.currentToken().strValue, ")")
		l.nextToken()
		assertEquals(l.currentToken().kind, Token.TokenType.EOI)
		l.nextToken()
	}
}

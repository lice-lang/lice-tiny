package org.lice.parse2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexIdentifierTest {

	@Test(timeout = 100)
	public void testLexIdentifier() {
		String srcCode = "Fuck@dentifier";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.Identifier, l.currentToken().getType());
		assertEquals("Fuck@dentifier", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexIdentifiers() {
		String srcCode = "Fuck@dentifier _Yet1A2not->her@dentifier";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.Identifier, l.currentToken().getType());
		assertEquals("Fuck@dentifier", l.currentToken().getStrValue());
		l.nextToken();
		assertEquals(Token.TokenType.Identifier, l.currentToken().getType());
		assertEquals("_Yet1A2not->her@dentifier", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexIdentifiers2() {
		String srcCode = "Fuck@dentifier,_Yet1A2not->her@dentifier";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.Identifier, l.currentToken().getType());
		assertEquals("Fuck@dentifier", l.currentToken().getStrValue());
		l.nextToken();
		assertEquals(Token.TokenType.Identifier, l.currentToken().getType());
		assertEquals("_Yet1A2not->her@dentifier", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}
}

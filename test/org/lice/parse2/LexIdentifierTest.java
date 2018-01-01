package org.lice.parse2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexIdentifierTest {
	@Test(timeout = 100)
	public void testLexIdentifier() {
		String srcCode = "Fuck@dentifier";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.Identifier);
		assertEquals(l.currentToken().getStrValue(), "Fuck@dentifier");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexIdentifiers() {
		String srcCode = "Fuck@dentifier _Yet1A2not->her@dentifier";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.Identifier);
		assertEquals(l.currentToken().getStrValue(), "Fuck@dentifier");
		l.nextToken();
		assertEquals(l.currentToken().getType(), Token.TokenType.Identifier);
		assertEquals(l.currentToken().getStrValue(), "_Yet1A2not->her@dentifier");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexIdentifiers2() {
		String srcCode = "Fuck@dentifier,_Yet1A2not->her@dentifier";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.Identifier);
		assertEquals(l.currentToken().getStrValue(), "Fuck@dentifier");
		l.nextToken();
		assertEquals(l.currentToken().getType(), Token.TokenType.Identifier);
		assertEquals(l.currentToken().getStrValue(), "_Yet1A2not->her@dentifier");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}
}

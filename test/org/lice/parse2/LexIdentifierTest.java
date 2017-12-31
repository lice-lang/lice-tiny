package org.lice.parse2;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LexIdentifierTest {
	@Test(timeout = 100)
	public void testLexIdentifier() {
		String srcCode = "Fuck@dentifier";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.currentToken().getStrValue().equals("Fuck@dentifier"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexIdentifiers() {
		String srcCode = "Fuck@dentifier _Yet1A2not->her@dentifier";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.currentToken().getStrValue().equals("Fuck@dentifier"));
		l.nextToken();
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.currentToken().getStrValue().equals("_Yet1A2not->her@dentifier"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexIdentifiers2() {
		String srcCode = "Fuck@dentifier,_Yet1A2not->her@dentifier";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.currentToken().getStrValue().equals("Fuck@dentifier"));
		l.nextToken();
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.currentToken().getStrValue().equals("_Yet1A2not->her@dentifier"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}
}

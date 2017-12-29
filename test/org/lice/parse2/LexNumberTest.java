package org.lice.parse2;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LexNumberTest {
	@Test(timeout = 100)
	public void testLexIdentifier() {
		String srcCode = "0x74138HC";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexHexNumber() {
		String srcCode = "0x74183";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.NumericLiteral);
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}
}

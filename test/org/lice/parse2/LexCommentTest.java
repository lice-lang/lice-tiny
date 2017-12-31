package org.lice.parse2;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LexCommentTest {
	@Test(timeout = 100)
	public void testLexComment() {
		String src = "; Simply comments";
		Lexer l = new Lexer(src);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexComment2() {
		String src = "; Simply comments\n@dentifier";
		Lexer l = new Lexer(src);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.Identifier);
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}
}

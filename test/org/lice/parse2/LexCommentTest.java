package org.lice.parse2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexCommentTest {
	@Test(timeout = 100)
	public void testLexComment() {
		String src = "; Simply comments";
		Lexer l = new Lexer(src);
		assertEquals(Token.TokenType.EOI, l.currentToken().getType());
	}

	@Test(timeout = 100)
	public void testLexComment2() {
		String src = "; Simply comments\n@dentifier";
		Lexer l = new Lexer(src);
		assertEquals(Token.TokenType.Identifier, l.currentToken().getType());
		assertEquals("@dentifier", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}
}

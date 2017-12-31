package org.lice.parse2;

import org.junit.Test;
import org.lice.util.ParseException;

import static org.junit.Assert.assertTrue;

public class LexStringTest {
	@Test(timeout = 100)
	public void testLexString() {
		String srcCode = "\"String\"";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.StringLiteral);
		assertTrue(l.currentToken().getStrValue().equals("String"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexConversionSequence() {
		String srcCode = "\"Str\\ning\"";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.StringLiteral);
		assertTrue(l.currentToken().getStrValue().equals("Str\ning"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexConversionSequenceFailed() {
		String srcCode = "\"\\q\"";
		try {
			Lexer l = new Lexer(srcCode);
			l.currentToken();
		} catch (ParseException e) {
			e.prettyPrint(srcCode.split("\n"));
			return;
		}
		assertTrue(false);
	}

	@Test(timeout = 100)
	public void testMissingQuote() {
		String srcCode = "\"String without closing quote";
		try {
			Lexer l = new Lexer(srcCode);
			l.currentToken();
		} catch (ParseException e) {
			e.prettyPrint(srcCode.split("\n"));
			return;
		}
		assertTrue(false);
	}
}

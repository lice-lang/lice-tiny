package org.lice.parse2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lice.util.ParseException;

import static org.junit.Assert.assertEquals;

public class LexStringTest {
	@Test(timeout = 100)
	public void testLexString() {
		String srcCode = "\"String\"";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getKind(), Token.TokenType.StringLiteral);
		assertEquals(l.currentToken().getStrValue(), "String");
		assertEquals(l.peekOneToken().getKind(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexConversionSequence() {
		String srcCode = "\"Str\\ning\"";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getKind(), Token.TokenType.StringLiteral);
		assertEquals(l.currentToken().getStrValue(), "Str\ning");
		assertEquals(l.peekOneToken().getKind(), Token.TokenType.EOI);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test(timeout = 200)
	public void testLexConversionSequenceFailed() {
		thrown.expect(ParseException.class);
		String srcCode = "\"\\q\"";
		Lexer l = new Lexer(srcCode);
	}

	@Test(timeout = 200)
	public void testMissingQuote() {
		thrown.expect(ParseException.class);
		String srcCode = "\"String without closing quote";
		Lexer l = new Lexer(srcCode);
	}
}

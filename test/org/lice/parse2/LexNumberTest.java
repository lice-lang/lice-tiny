package org.lice.parse2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lice.util.ParseException;

import static org.junit.Assert.assertEquals;

public class LexNumberTest {
	@Test(timeout = 100)
	public void testLexHexNumber() {
		String srcCode = "0x74CD161";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.HexNumber);
		assertEquals(l.currentToken().getStrValue(), "0x74CD161");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBinNumber() {
		String srcCode = "0b101101";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.BinNumber);
		assertEquals(l.currentToken().getStrValue(), "0b101101");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexDecNumber() {
		String srcCode = "074183";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.DecNumber);
		assertEquals(l.currentToken().getStrValue(), "074183");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexOctNumber() {
		String srcCode = "0o1234567";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.OctNumber);
		assertEquals(l.currentToken().getStrValue(), "0o1234567");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexFloatNumber() {
		String srcCode = "3.1415927";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.FloatNumber);
		assertEquals(l.currentToken().getStrValue(), "3.1415927");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber() {
		String srcCode = "3.141592654";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.DoubleNumber);
		assertEquals(l.currentToken().getStrValue(), "3.141592654");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber2() {
		String srcCode = "74183D";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.DoubleNumber);
		assertEquals(l.currentToken().getStrValue(), "74183");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexLongInteger() {
		String srcCode = "123L";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.LongInteger);
		assertEquals(l.currentToken().getStrValue(), "123");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigInt() {
		String srcCode = "123N";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.BigInt);
		assertEquals(l.currentToken().getStrValue(), "123");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigDec1() {
		String srcCode = "123M";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.BigDec);
		assertEquals(l.currentToken().getStrValue(), "123");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigDec2() {
		String srcCode = "123.456012M";
		Lexer l = new Lexer(srcCode);
		assertEquals(l.currentToken().getType(), Token.TokenType.BigDec);
		assertEquals(l.currentToken().getStrValue(), "123.456012");
		assertEquals(l.peekOneToken().getType(), Token.TokenType.EOI);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test(timeout = 200)
	public void testLexFloatingNumberFailed() {
		thrown.expect(ParseException.class);
		String srcCode = "0xAB.CDEF";
		Lexer l = new Lexer(srcCode);
	}

	@Test(timeout = 200)
	public void testLexFloatingNumberFailed2() {
		thrown.expect(ParseException.class);
		String srcCode = "0o32F";
		Lexer l = new Lexer(srcCode);
	}
}

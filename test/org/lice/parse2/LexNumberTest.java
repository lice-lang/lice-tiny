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
		assertEquals(Token.TokenType.HexNumber, l.currentToken().getType());
		assertEquals("0x74CD161", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexBinNumber() {
		String srcCode = "0b101101";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.BinNumber, l.currentToken().getType());
		assertEquals("0b101101", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexDecNumber() {
		String srcCode = "074183";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.DecNumber, l.currentToken().getType());
		assertEquals("074183", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexOctNumber() {
		String srcCode = "0o1234567";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.OctNumber, l.currentToken().getType());
		assertEquals("0o1234567", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexFloatNumber() {
		String srcCode = "3.1415927";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.FloatNumber, l.currentToken().getType());
		assertEquals("3.1415927", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber() {
		String srcCode = "3.141592654";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.DoubleNumber, l.currentToken().getType());
		assertEquals("3.141592654", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber2() {
		String srcCode = "74183D";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.DoubleNumber, l.currentToken().getType());
		assertEquals("74183", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexLongInteger() {
		String srcCode = "123L";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.LongInteger, l.currentToken().getType());
		assertEquals("123", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexBigInt() {
		String srcCode = "123N";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.BigInt, l.currentToken().getType());
		assertEquals("123", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexBigDec1() {
		String srcCode = "123M";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.BigDec, l.currentToken().getType());
		assertEquals("123", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
	}

	@Test(timeout = 100)
	public void testLexBigDec2() {
		String srcCode = "123.456012M";
		Lexer l = new Lexer(srcCode);
		assertEquals(Token.TokenType.BigDec, l.currentToken().getType());
		assertEquals("123.456012", l.currentToken().getStrValue());
		assertEquals(Token.TokenType.EOI, l.peekOneToken().getType());
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

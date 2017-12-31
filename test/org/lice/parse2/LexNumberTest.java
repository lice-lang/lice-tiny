package org.lice.parse2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lice.util.ParseException;
import static org.junit.Assert.assertTrue;

public class LexNumberTest {
	@Test(timeout = 100)
	public void testLexHexNumber() {
		String srcCode = "0x74183";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.HexNumber);
		assertTrue(l.currentToken().getStrValue().equals("0x74183"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBinNumber() {
		String srcCode = "0b101101";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.BinNumber);
		assertTrue(l.currentToken().getStrValue().equals("0b101101"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexDecNumber() {
		String srcCode = "074183";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.DecNumber);
		assertTrue(l.currentToken().getStrValue().equals("074183"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexOctNumber() {
		String srcCode = "0o1234567";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.OctNumber);
		assertTrue(l.currentToken().getStrValue().equals("0o1234567"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexFloatNumber() {
		String srcCode = "3.1415927";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.FloatNumber);
		assertTrue(l.currentToken().getStrValue().equals("3.1415927"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber() {
		String srcCode = "3.141592654";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.DoubleNumber);
		assertTrue(l.currentToken().getStrValue().equals("3.141592654"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber2() {
		String srcCode = "3D";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.DoubleNumber);
		assertTrue(l.currentToken().getStrValue().equals("3"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexLongInteger() {
		String srcCode = "123L";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.LongInteger);
		assertTrue(l.currentToken().getStrValue().equals("123"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigInt() {
		String srcCode = "123N";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.BigInt);
		assertTrue(l.currentToken().getStrValue().equals("123"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigDec1() {
		String srcCode = "123M";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.BigDec);
		assertTrue(l.currentToken().getStrValue().equals("123"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigDec2() {
		String srcCode = "123.456012M";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenType.BigDec);
		assertTrue(l.currentToken().getStrValue().equals("123.456012"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenType.EOI);
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

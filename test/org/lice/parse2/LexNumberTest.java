package org.lice.parse2;

import org.junit.Test;
import org.lice.util.ParseException;

import static org.junit.Assert.assertTrue;

public class LexNumberTest {
	@Test(timeout = 100)
	public void testLexHexNumber() {
		String srcCode = "0x74183";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.HexNumber);
		assertTrue(l.currentToken().getStrValue().equals("0x74183"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexBinNumber() {
		String srcCode = "0b101101";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.BinNumber);
		assertTrue(l.currentToken().getStrValue().equals("0b101101"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexDecNumber() {
		String srcCode = "074183";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.DecNumber);
		assertTrue(l.currentToken().getStrValue().equals("074183"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexOctNumber() {
		String srcCode = "0o1234567";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.OctNumber);
		assertTrue(l.currentToken().getStrValue().equals("0o1234567"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexFloatNumber() {
		String srcCode = "3.1415927";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.FloatNumber);
		assertTrue(l.currentToken().getStrValue().equals("3.1415927"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber() {
		String srcCode = "3.141592654";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.DoubleNumber);
		assertTrue(l.currentToken().getStrValue().equals("3.141592654"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexDoubleNumber2() {
		String srcCode = "3D";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.DoubleNumber);
		assertTrue(l.currentToken().getStrValue().equals("3"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexLongNumber() {
		String srcCode = "123N";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.LongNumber);
		assertTrue(l.currentToken().getStrValue().equals("123"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexBigNumber() {
		String srcCode = "123M";
		Lexer l = new Lexer(srcCode);
		assertTrue(l.currentToken().getKind() == Token.TokenKind.BigNumber);
		assertTrue(l.currentToken().getStrValue().equals("123"));
		assertTrue(l.peekOneToken().getKind() == Token.TokenKind.EOI);
	}

	@Test(timeout = 100)
	public void testLexFloatingNumberFailed() {
		String srcCode = "0xAB.CDEF";
		try {
			Lexer l = new Lexer(srcCode);
		} catch (ParseException e) {
			e.prettyPrint(srcCode.split("\n"));
			return;
		}
		assertTrue(false);
	}

	@Test(timeout = 100)
	public void testLexFloatingNumberFailed2() {
		String srcCode = "0o32F";
		try {
			Lexer l = new Lexer(srcCode);
		} catch (ParseException e) {
			e.prettyPrint(srcCode.split("\n"));
			return;
		}
		assertTrue(false);
	}
}

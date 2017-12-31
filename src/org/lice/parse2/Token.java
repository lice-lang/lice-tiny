package org.lice.parse2;

import org.lice.model.MetaData;

public class Token {
	public enum TokenKind {
		BinNumber,
		OctNumber,
		DecNumber,
		HexNumber,
		LongNumber,
		BigNumber,
		FloatNumber,
		DoubleNumber,
		StringLiteral,
		Identifier,
		LispKwd,
		EOI
	}

	Token(TokenKind kind, String strValue, int beginLineNumber, int endLineNumber, int beginIndex, int endIndex) {
		this.kind = kind;
		this.strValue = strValue;
		this.metaData = new MetaData(beginLineNumber, endLineNumber, beginIndex, endIndex);
	}

	public TokenKind getKind() { return kind; }
	public String getStrValue() { return this.strValue; }
	public MetaData getMetaData() {  return this.metaData; }

	private final TokenKind kind;
	private final String strValue;
	private final MetaData metaData;

	public static boolean isIntegral(TokenKind kind) {
		return kind == TokenKind.BinNumber
				|| kind == TokenKind.OctNumber
				|| kind == TokenKind.DecNumber
				|| kind == TokenKind.HexNumber
				|| kind == TokenKind.LongNumber;
	}

	public static boolean isDecimal(TokenKind kind) {
		return kind == TokenKind.DecNumber
				|| kind == TokenKind.FloatNumber
				|| kind == TokenKind.DoubleNumber;
	}

	public static boolean isFloating(TokenKind kind) {
		return kind == TokenKind.FloatNumber
				|| kind == TokenKind.DoubleNumber;
	}
}

package org.lice.parse2;

import org.lice.model.MetaData;

public class Token {
	public enum TokenType {
		BinNumber,
		OctNumber,
		DecNumber,
		HexNumber,
		LongInteger,
		BigInteger,
		FloatNumber,
		DoubleNumber,
		StringLiteral,
		Identifier,
		LispKwd,
		EOI
	}

	Token(TokenType kind, String strValue, int beginLineNumber, int endLineNumber, int beginIndex, int endIndex) {
		this.kind = kind;
		this.strValue = strValue;
		this.metaData = new MetaData(beginLineNumber, endLineNumber, beginIndex, endIndex);
	}

	public TokenType getKind() { return kind; }
	public String getStrValue() { return this.strValue; }
	public MetaData getMetaData() {  return this.metaData; }

	private final TokenType kind;
	private final String strValue;
	private final MetaData metaData;

	public static boolean isIntegral(TokenType kind) {
		return kind == TokenType.BinNumber
				|| kind == TokenType.OctNumber
				|| kind == TokenType.DecNumber
				|| kind == TokenType.HexNumber
				|| kind == TokenType.LongInteger;
	}

	public static boolean isDecimal(TokenType kind) {
		return kind == TokenType.DecNumber
				|| kind == TokenType.FloatNumber
				|| kind == TokenType.DoubleNumber;
	}

	public static boolean isFloating(TokenType kind) {
		return kind == TokenType.FloatNumber
				|| kind == TokenType.DoubleNumber;
	}
}

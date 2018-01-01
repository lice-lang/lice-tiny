package org.lice.parse2;

import org.lice.model.MetaData;

public class Token {
	public enum TokenType {
		BinNumber,
		OctNumber,
		DecNumber,
		HexNumber,
		LongInteger,
		BigInt,
		BigDec,
		FloatNumber,
		DoubleNumber,
		StringLiteral,
		Identifier,
		LispKwd,
		EOI
	}

	Token(TokenType type, String strValue, int beginLineNumber, int endLineNumber, int beginIndex, int endIndex) {
		this.type = type;
		this.strValue = strValue;
		this.metaData = new MetaData(beginLineNumber, endLineNumber, beginIndex, endIndex);
	}

	public TokenType getType() { return type; }
	public String getStrValue() { return this.strValue; }
	public MetaData getMetaData() {  return this.metaData; }

	private final TokenType type;
	private final String strValue;
	private final MetaData metaData;

	public static boolean isIntegral(TokenType type) {
		return type == TokenType.BinNumber
				|| type == TokenType.OctNumber
				|| type == TokenType.DecNumber
				|| type == TokenType.HexNumber
				|| type == TokenType.LongInteger;
	}

	public static boolean isDecimal(TokenType type) {
		return type == TokenType.DecNumber
				|| type == TokenType.FloatNumber
				|| type == TokenType.DoubleNumber;
	}

	public static boolean isFloating(TokenType type) {
		return type == TokenType.FloatNumber
				|| type == TokenType.DoubleNumber;
	}
}

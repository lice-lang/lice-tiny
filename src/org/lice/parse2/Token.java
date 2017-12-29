package org.lice.parse2;

import org.lice.model.MetaData;

public class Token {
	public enum TokenKind {
		NumericLiteral,
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
}

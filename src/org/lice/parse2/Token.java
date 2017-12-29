package org.lice.parse2;

public class Token {
	public enum TokenKind {
		NumericLiteral,
		StringLiteral,
		Identifier,
		LispKwd,
		EOI
	}

	Token(TokenKind kind, String strValue, int lineNumber, int beginIndex, int endIndex) {
		this.kind = kind;
		this.strValue = strValue;
		this.lineNumber = lineNumber;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	public TokenKind getKind() { return kind; }
	public String getStrValue() { return this.strValue; }
	public int getLineNumber() {
		return this.lineNumber;
	}
	public int getBeginIndex() {
		return this.beginIndex;
	}
	public int getEndIndex() {
		return this.endIndex;
	}

	private final TokenKind kind;
	private final String strValue;
	private final int lineNumber;
	private final int beginIndex;
	private final int endIndex;
}

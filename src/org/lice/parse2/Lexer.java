package org.lice.parse2;

import org.jetbrains.annotations.NotNull;
import org.lice.model.MetaData;

import java.util.ArrayList;

public class Lexer {
	private static final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
	private static final String commonSymbols = "!@#$%^&*_=:<>.?/\\+-*/%[]{}|";
	private static final String binNumbers = "01";
	private static final String octNumbers = "01234567";
	private static final String decNumbers = "0123456789";
	private static final String hexNumbers = "0123456789ABCDEF";
	private static final String blanks = " \f\n\t\r,";
	private static final String lispSymbols = "()";
	private static final String tokenDelimiters = blanks + lispSymbols + "\0";

	private static final String firstIdButNotNumberChars =
			upperCaseLetters + lowerCaseLetters + commonSymbols;
	private static final String idChars = firstIdButNotNumberChars + decNumbers;

	public Lexer(String sourceCode) {
		this.sourceCode = sourceCode.toCharArray();
		doSplitTokens();
	}

	public Token currentToken() {
		assert currentTokenIndex < this.tokenBuffer.size();
		return this.tokenBuffer.get(this.currentTokenIndex);
	}

	public Token peekOneToken() {
		assert currentTokenIndex+1 < this.tokenBuffer.size();
		return this.tokenBuffer.get(this.currentTokenIndex+1);
	}

	public void nextToken() {
		this.currentTokenIndex++;
	}

	private void doSplitTokens() {
		this.line = 1;
		this.col = 1;
		this.charIndex = 0;
		this.tokenBuffer = new ArrayList<Token>();

		while ( currentChar() != '\0' ) {
			if ( firstIdButNotNumberChars.contains(Character.toString(currentChar())) ) {
				lexIdentifier();
			}
			else if ( decNumbers.contains(Character.toString(currentChar())) ) {
				lexNumberOrIdentifier();
			}
			else if ( lispSymbols.contains(Character.toString(currentChar())) ) {
				lexSingleCharToken();
			}
			else if ( currentChar() == '"' ) {
				lexString();
			}
			else if ( blanks.contains(Character.toString(currentChar())) ) {
				nextChar();
			}
			else {
				throw new LexException(new MetaData(this.line, this.line, this.col, this.col+1),
						"Unrecognized character " + Character.toString(currentChar()));
			}
		}
		tokenBuffer.add(new Token(Token.TokenKind.EOI, "",
				this.line, this.line, this.col, this.col+1));
		this.currentTokenIndex = 0;
	}

	private void lexIdentifier() {
		int line = this.line;
		int startAtCol = this.col;
		String str = scanFullString(idChars);
		this.tokenBuffer.add(new Token(Token.TokenKind.Identifier, str,
				this.line, this.line, startAtCol, this.col));
	}

	private void lexNumberOrIdentifier() {
		int line = this.line;
		int startAtCol = this.col;

		String numberStr;
		if (currentChar() != '0') {
			numberStr = scanFullString(decNumbers);
		}
		else {
			switch (peekOneChar()) {
				case 'b': case 'B': {
					nextChar(); nextChar();
					numberStr = "0b" + scanFullString(binNumbers);
					break;
				}
				case 'o': case 'O': {
					nextChar(); nextChar();
					numberStr = "0o" + scanFullString(octNumbers);
					break;
				}
				case 'x': case 'X': {
					nextChar(); nextChar();
					numberStr = "0x" + scanFullString(hexNumbers);
					break;
				}
				default: {
					numberStr = scanFullString(decNumbers);
				}
			}
		}

		if ( !tokenDelimiters.contains(Character.toString(currentChar())) ) {
			String fullIdStr = numberStr + scanFullString(idChars);
			tokenBuffer.add(new Token(Token.TokenKind.Identifier, fullIdStr, line, line, startAtCol, this.col));
		}
		else {
			tokenBuffer.add(new Token(Token.TokenKind.NumericLiteral, numberStr, line, line, startAtCol, this.col));
		}
	}

	@NotNull
	private String scanFullString(String allowedChars) {
		StringBuilder tokenStringBuilder = new StringBuilder();
		while ( allowedChars.contains(Character.toString(currentChar())) ) {
			tokenStringBuilder.append(currentChar());
			nextChar();
		}
		return tokenStringBuilder.toString();
	}

	private void lexSingleCharToken() {
		this.tokenBuffer.add(
				new Token(Token.TokenKind.LispKwd, Character.toString(currentChar()),
						this.line, this.line, this.col, this.col+1));
		nextChar();
	}

	private void lexString() {
		int atLine = this.line;
		int startAtCol = this.col;

		nextChar();

		StringBuilder builder = new StringBuilder();
		while (currentChar() != '"' && currentChar() != '\0') {
			if (currentChar() != '\\') {
				builder.append(currentChar());
				nextChar();
			}
			else {
				switch (peekOneChar()) {
					case 'n':  builder.append('\n'); break;
					case 'f':  builder.append('\f'); break;
					case 't':  builder.append('\t'); break;
					case '\\': builder.append('\\'); break;
					case '"':  builder.append('\"'); break;
					default:
						throw new LexException(new MetaData(this.line, this.line, this.col, this.col+2),
								"Illegal conversion sequence \\" + peekOneChar());
				}
				nextChar();
				nextChar();
			}
		}

		if (currentChar() == '\0') {
			throw new LexException(new MetaData(this.line, this.line, this.col, this.col+1),
					"Unexpected EndOfInput.");
		}
		nextChar();

		this.tokenBuffer.add(
				new Token(Token.TokenKind.StringLiteral, builder.toString(),
							atLine, this.line, startAtCol, this.col));
	}

	private char currentChar() {
		if (charIndex >= sourceCode.length) {
			return '\0';
		}
		else {
			return sourceCode[charIndex];
		}
	}

	private char peekOneChar()  {
		if (charIndex+1 >= sourceCode.length) {
			return '\0';
		}
		else {
			return sourceCode[charIndex+1];
		}
	}

	private void nextChar() {
		if (sourceCode[charIndex] == '\n') {
			line++;
			col = 1;
		}
		else {
			col++;
		}
		charIndex++;
	}

	private char[] sourceCode;
	private int line, col, charIndex;

	private ArrayList<Token> tokenBuffer;
	private int currentTokenIndex;
}

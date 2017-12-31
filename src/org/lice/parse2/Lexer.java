package org.lice.parse2;

import org.jetbrains.annotations.NotNull;
import org.lice.model.MetaData;
import org.lice.util.ParseException;

import java.util.ArrayList;

public class Lexer {
	private static final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
	private static final String commonSymbols = "!@#$%^&*_=:<>.?/\\+-*%[]{}|";
	private static final String binDigits = "01";
	private static final String octDigits = "01234567";
	private static final String decDigits = "0123456789";
	private static final String hexDigits = "0123456789ABCDEF";
	private static final String blanks = " \f\n\t\r,";
	private static final String lispSymbols = "()";
	private static final String tokenDelimiters = blanks + lispSymbols + ";\0";
	private static final String firstIdChars = upperCaseLetters + lowerCaseLetters + commonSymbols;
	private static final String idChars = firstIdChars + decDigits;

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
			if ( firstIdChars.contains(Character.toString(currentChar())) ) {
				lexIdentifier();
			}
			else if ( decDigits.contains(Character.toString(currentChar())) ) {
				lexNumber();
			}
			else if ( lispSymbols.contains(Character.toString(currentChar())) ) {
				lexSingleCharToken();
			}
			else if ( currentChar() == '"' ) {
				lexString();
			}
			else if ( currentChar() == ';' ) {
				skipComment();
			}
			else if ( blanks.contains(Character.toString(currentChar())) ) {
				nextChar();
			}
			else {
				throw new ParseException("Unknown character " + Character.toString(currentChar()),
						new MetaData(this.line, this.line, this.col, this.col+1));
			}
		}
		tokenBuffer.add(new Token(Token.TokenType.EOI, "",
				this.line, this.line, this.col, this.col+1));
		this.currentTokenIndex = 0;
	}

	private void lexIdentifier() {
		int line = this.line;
		int startAtCol = this.col;
		String str = scanFullString(idChars);
		this.tokenBuffer.add(new Token(Token.TokenType.Identifier, str,
				this.line, this.line, startAtCol, this.col));
	}

	private void lexNumber() {
		int line = this.line;
		int startAtCol = this.col;
		Token.TokenType numberKind;
		String numberStr;

		if (currentChar() != '0') {
			numberKind = Token.TokenType.DecNumber;
			numberStr = scanFullString(decDigits);
		}
		else {
			switch (peekOneChar()) {
				case 'b': case 'B': {
					nextChar(); nextChar();
					numberKind = Token.TokenType.BinNumber;
					numberStr = "0b" + scanFullString(binDigits);
					break;
				}
				case 'o': case 'O': {
					nextChar(); nextChar();
					numberKind = Token.TokenType.OctNumber;
					numberStr = "0o" + scanFullString(octDigits);
					break;
				}
				case 'x': case 'X': {
					nextChar(); nextChar();
					numberKind = Token.TokenType.HexNumber;
					numberStr = "0x" + scanFullString(hexDigits);
					break;
				}
				default: {
					numberKind = Token.TokenType.DecNumber;
					numberStr = scanFullString(decDigits);
				}
			}
		}

		if (currentChar() == '.') {
			if (numberKind != Token.TokenType.DecNumber) {
				throw new ParseException("Only decimal floating numbers are allowed",
						new MetaData(line, this.line, startAtCol, this.col));
			}
			nextChar();
			numberStr = numberStr + '.' + scanFullString(decDigits);
			numberKind = numberStr.length() <= 9 ? Token.TokenType.FloatNumber : Token.TokenType.DoubleNumber;
		}

		switch (currentChar()) {
			case 'f': case 'F': {
				if (!Token.isDecimal(numberKind)) {
					throw new ParseException("Only decimal floating numbers are allowed",
							new MetaData(line, this.line, startAtCol, this.col));
				}
				nextChar();
				numberKind = Token.TokenType.FloatNumber;
				break;
			}
			case 'd': case 'D': {
				if (!Token.isDecimal(numberKind)) {
					throw new ParseException("Only decimal floating numbers are allowed",
							new MetaData(line, this.line, startAtCol, this.col));
				}
				nextChar();
				numberKind = Token.TokenType.DoubleNumber;
				break;
			}
			case 'm': case 'M': {
				if (!Token.isIntegral(numberKind)) {
					throw new ParseException("'m' or 'M' is used for big integers",
							new MetaData(line, this.line, startAtCol, this.col));
				}
				nextChar();
				numberKind = Token.TokenType.BigInteger;
				break;
			}
			case 'n': case 'N': {
				if (!Token.isIntegral(numberKind)) {
					throw new ParseException("'m' or 'M' is used for long integers",
							new MetaData(line, this.line, startAtCol, this.col));
				}
				nextChar();
				numberKind = Token.TokenType.LongInteger;
				break;
			}
		}

		if (! tokenDelimiters.contains(Character.toString(currentChar())) ) {
			throw new ParseException("Unexpected character",
					new MetaData(this.line, this.line, this.col, this.col+1));
		}

		tokenBuffer.add(new Token(numberKind, numberStr,line, this.line, startAtCol, this.col));
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
				new Token(Token.TokenType.LispKwd, Character.toString(currentChar()),
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
						throw new ParseException("Illegal conversion sequence \\" + peekOneChar(),
								new MetaData(this.line, this.line, this.col, this.col+2));
				}
				nextChar();
				nextChar();
			}
		}

		if (currentChar() == '\0') {
			throw new ParseException("Unexpected EndOfInput.",
					new MetaData(this.line, this.line, this.col, this.col+1));
		}
		nextChar();

		this.tokenBuffer.add(
				new Token(Token.TokenType.StringLiteral, builder.toString(),
							atLine, this.line, startAtCol, this.col));
	}

	private void skipComment() {
		assert currentChar() == ';';
		while (currentChar() != '\n' && currentChar() != '\0') nextChar();
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
		if (currentChar() == '\n') {
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

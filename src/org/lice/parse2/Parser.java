package org.lice.parse2;

import org.lice.model.MetaData;
import org.lice.util.ParseException;

import java.util.ArrayList;

public class Parser {
	public static ASTNode parseTokenStream(Lexer lexer) {
		ArrayList<ASTNode> nodes = new ArrayList<>();
		while (lexer.currentToken().getType()!= Token.TokenType.EOI) {
			nodes.add(parseNode(lexer));
		}
		return new ASTListNode(new MetaData(), nodes);
	}

	private static ASTNode parseNode(Lexer l) {
		if (l.currentToken().getType() == Token.TokenType.LispKwd
			&& l.currentToken().getStrValue().equals("(")) {
			return parseList(l);
		}
		else {
			ASTAtomicNode ret = new ASTAtomicNode(l.currentToken().getMetaData(), l.currentToken());
			l.nextToken();
			return ret;
		}
	}

	private static ASTListNode parseList(Lexer l) {
		assert (l.currentToken().getType() == Token.TokenType.LispKwd
				&& l.currentToken().getStrValue().equals("("));
		l.nextToken();

		Token leftParthToken = l.currentToken();
		ArrayList<ASTNode> subNodes = new ArrayList<>();

		while (!(l.currentToken().getType() == Token.TokenType.LispKwd
				&& l.currentToken().getStrValue().equals(")"))) {
			subNodes.add(parseNode(l));
			if (l.currentToken().getType() == Token.TokenType.EOI) {
				throw new ParseException("Unexpected EOI, expected ')'", l.currentToken().getMetaData());
			}
		}

		l.nextToken();
		return new ASTListNode(leftParthToken.getMetaData(), subNodes);
	}
}

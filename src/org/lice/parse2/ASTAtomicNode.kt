package org.lice.parse2

import org.lice.model.*
import org.lice.util.ParseException

class ASTAtomicNode internal constructor(metaData: MetaData, val token: Token) : ASTNode(metaData) {
	override fun accept(sema: Sema) = when (token.type) {
		Token.TokenType.BinNumber -> ValueNode(token.strValue.toBinInt(), metaData)
		Token.TokenType.OctNumber -> ValueNode(token.strValue.toOctInt(), metaData)
		Token.TokenType.HexNumber -> ValueNode(token.strValue.toHexInt(), metaData)
		Token.TokenType.DecNumber -> ValueNode(Integer.parseInt(token.strValue), metaData)
		Token.TokenType.LongInteger -> ValueNode(java.lang.Long.parseLong(token.strValue), metaData)
		Token.TokenType.BigInt -> ValueNode(token.strValue.toBigInt(), metaData)
		Token.TokenType.BigDec -> ValueNode(token.strValue.toBigDec(), metaData)
		Token.TokenType.FloatNumber -> ValueNode(java.lang.Float.parseFloat(token.strValue), metaData)
		Token.TokenType.DoubleNumber -> ValueNode(java.lang.Double.parseDouble(token.strValue), metaData)
		Token.TokenType.StringLiteral -> ValueNode(token.strValue, metaData)
		Token.TokenType.Identifier -> SymbolNode(sema.symbolList, token.strValue, metaData)
		Token.TokenType.LispKwd, Token.TokenType.EOI -> throw ParseException("")
	}
}

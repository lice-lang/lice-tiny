package org.lice.parse2

import org.lice.model.MetaData

class Token constructor(
		val type: TokenType,
		val strValue: String,
		val metaData: MetaData) {

	constructor(
			type: TokenType,
			strValue: String,
			beginLineNumber: Int,
			endLineNumber: Int,
			beginIndex: Int,
			endIndex: Int)
			: this(type, strValue, MetaData(beginLineNumber, endLineNumber, beginIndex, endIndex))

	enum class TokenType {
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

	companion object {

		fun isIntegral(type: TokenType) =
				type == TokenType.BinNumber
						|| type == TokenType.OctNumber
						|| type == TokenType.DecNumber
						|| type == TokenType.HexNumber
						|| type == TokenType.LongInteger

		fun isDecimal(type: TokenType) =
				type == TokenType.DecNumber
						|| type == TokenType.FloatNumber
						|| type == TokenType.DoubleNumber

		fun isFloating(type: TokenType)
				= type == TokenType.FloatNumber || type == TokenType.DoubleNumber
	}
}

package org.lice.parse2;

import org.lice.core.SymbolList;

public class Sema {
	public Sema() {
		symbolList = new SymbolList();
	}
	public Sema(SymbolList symbolList) { this.symbolList = symbolList; }

	public SymbolList getSymbolList() { return symbolList; }

	private SymbolList symbolList;
}

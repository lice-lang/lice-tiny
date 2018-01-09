package org.lice.parse2;

import org.lice.core.SymbolList;

public class Sema {
	Sema() {
		symbolList = new SymbolList();
	}

	public SymbolList getSymbolList() { return symbolList; }

	private SymbolList symbolList;
}

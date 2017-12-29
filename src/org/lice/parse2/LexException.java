package org.lice.parse2;

import org.lice.model.MetaData;

public class LexException extends RuntimeException {
	LexException(MetaData metaData, String desc) {
		super(desc + " At " + metaData.getBeginLine() + ":" + metaData.getBeginIndex());
		this.metaData = metaData;
		this.desc = desc;
	}

	public void prettyPrint(String srcCode) {
		String[] lines = srcCode.split("\n");
		System.err.print("At " + metaData.getBeginLine() + ":" + metaData.getBeginIndex() + ": ");
		System.err.println(this.desc);
		System.err.println(lines[metaData.getBeginLine()-1]);
		for (int i = 0; i < metaData.getBeginIndex()-1; i++) {
			System.err.print(' ');
		}
		System.err.print('^');
		for (int i = metaData.getBeginIndex(); i < metaData.getEndIndex()-1; i++) {
			System.err.print('~');
		}
		System.err.println();
	}

	private final MetaData metaData;
	private final String desc;
}

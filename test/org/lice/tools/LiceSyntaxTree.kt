package org.lice.tools

object LiceSyntaxTree {
	/// @todo switch to ASTNodes.
	/*
	private fun mapAst2Display(node: StringNode, viewRoot: DefaultMutableTreeNode): DefaultMutableTreeNode = when (node) {
		is StringLeafNode -> DefaultMutableTreeNode(node)
		is StringMiddleNode -> viewRoot.apply {
			node.list.subList(1, node.list.size).forEach { add(mapAst2Display(it, DefaultMutableTreeNode(it))) }
		}
		else -> DefaultMutableTreeNode("null")
	}

	/**
	 * map the ast
	 */
	private fun mapDisplay2Ast(node: DefaultMutableTreeNode, gen: StringBuilder, numOfIndents: Int = 0) {
		if (numOfIndents == 0) gen.append("\n")
		when {
			node.isLeaf -> gen.append(" ").append(node.userObject.toString()).append("")
			else -> {
				gen.append("\n")
						.append("  ".repeat(numOfIndents))
						.append("(")
						.append(node.userObject.toString())
				node.children().toList().forEach {
					mapDisplay2Ast(it as DefaultMutableTreeNode, gen, numOfIndents + 1)
				}
				gen.append(")")
			}
		}
	}

	private fun createTreeRootFromFile(file: File): JTree {
		val ast = buildNode(file.readText())
		return JTree(mapAst2Display(ast, DefaultMutableTreeNode(ast)))
	}

	private fun File.neighbour() = "$parent/$name-edited-${System.currentTimeMillis()}.lice"

	fun displaySemanticTree(file: File) {
		val frame = JFrame("Lice Semantic Tree")
		frame.layout = BorderLayout()
		frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
		frame.setLocation(80, 80)
		try {
			frame.add(JScrollPane(createTreeRootFromFile(file).apply {
				preferredSize = Dimension(520, 520)
			}), BorderLayout.CENTER)
		} catch (e: RuntimeException) {
			val label = JTextArea(e.message)
			e.stackTrace.forEach { label.append("\t$it\n") }
			label.isEditable = false
			label.preferredSize = Dimension(600, 600)
			frame.add(label, BorderLayout.CENTER)
		}
		frame.pack()
		frame.isVisible = true
	}
	*/

	@JvmStatic
	fun main(args: Array<String>) {
		// displaySemanticTree(File("./sample/test3.lice"))
	}
}
package org.lice.compiler

import org.junit.Test
import org.lice.Lice
import org.lice.core.Func
import org.lice.core.SymbolList
import org.lice.model.MetaData
import org.lice.model.ValueNode
import org.lice.parse.buildNode
import org.lice.parse.mapAst
import org.lice.util.cast

class Benchmark {
	companion object {
		const val cnt = 200000
		//language=TEXT
		const val core = """
(defexpr let x y block (|>
			(-> x y)
			(block)
			(undef x)))
(let reimu 100 (lambda (|>
		reimu)))
"""

		//language=TEXT
		const val func = """
(def codes-to-run (|>
$core))
"""

		//language=TEXT
		const val code = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (|> $core)))

(print "loop count: " i)
"""

		//language=TEXT
		const val code2 = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (code)))

(print "loop count: " i)
"""
	}

	val map = hashMapOf<String, Any>()
	val lice = mapAst(node = buildNode(code))
	val lice2 = mapAst(node = buildNode(code2), symbolList = SymbolList.with {
		provideFunction("code") { java(map) }
	})

	@Test
	fun benchmarkLice() {
		lice.eval()
	}

	@Test
	fun benchmarkLiceCallJava() {
		lice2.eval()
	}

	@Test
	fun benchmarkJavaCallLice() {
		val symbols = SymbolList()
		Lice.run(func, symbols)
		val codes = symbols.getVariable("codes-to-run") as Func
		repeat(cnt) {
			codes.invoke(MetaData.EmptyMetaData, emptyList())
		}
	}

	@Test
	fun benchmarkJava() {
		val loop = { count: Int, block: (Int) -> Unit ->
			var i = 0
			while (i < count) {
				block(i)
				i += 1
			}
		}
		val map = mutableMapOf<String, Any>()
		loop(cnt) { java(map) }
	}



	private fun java(map: MutableMap<String, Any>) {
		val let = { x: String, y: Any, block: (Map<String, Any>) -> Unit ->
			map[x] = y
			block(map)
			map.remove(x)
		}
		let("reimu", 100) {
			val local = 233 + it["reimu"] as Int
		}
	}
}
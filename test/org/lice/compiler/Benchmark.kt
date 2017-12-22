package org.lice.compiler

import org.junit.Test
import org.lice.core.SymbolList
import org.lice.parse.buildNode
import org.lice.parse.mapAst
import org.lice.util.cast

class Benchmark {
	companion object {
		const val cnt = 200000
		//language=TEXT
		const val code = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (|>
	(defexpr let x y block (|>
			(-> x y)
			(block)
			(undef x)))
	(let reimu 100 (lambda (|>
			reimu))))))

(print "loop count: " i)
"""

		//language=TEXT
		const val code2 = """
(loop $cnt (lambda i (|>
	(defexpr let x y block (|>
			(-> x y)
			(block)
			(undef x)))
	(let reimu 100 (lambda (|>
			reimu))))))

(print "loop count: " i)
"""
	}

	val lice = mapAst(node = buildNode(code))
	val lice2 = mapAst(node = buildNode(code), symbolList = SymbolList.with {
		provideFunction("loop") { params ->
			repeat(params[0] as Int) { cast<((Int) -> Any)>(params[1])(it) }
		}
	})

	@Test
	fun benchmarkLice() {
		lice.eval()
	}

	@Test
	fun benchmarkCombine() {
		lice2.eval()
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
		loop(cnt) { i ->
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
}
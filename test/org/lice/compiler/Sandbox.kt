package org.lice.compiler

import org.junit.BeforeClass
import org.lice.Lice

/**
 * Created by ice1000 on 2017/4/23.
 *
 * @author ice1000
 */

class Sandbox {
	companion object {
		@JvmStatic
		@BeforeClass
		fun main(args: Array<String>) {
			//language=TEXT
			Lice.run("""
(defexpr let x y block (|>
	(-> x y)
	(block)
	(undef x)))

(let reimu 100 (lambda (|>
  (print reimu))))
""")
		}
	}
}
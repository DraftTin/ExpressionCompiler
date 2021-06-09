package lexer

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LexerTest {
    private val testSample: Lexer = Lexer()

    @Test
    fun testLexer() {
        var file = File("test.txt")
        var text = file.readText()

        var tokens = testSample.tokenlizer(text)
        for(token in tokens) {
            println("<${token.row}, ${token.col}, ${token.type}, ${token.value}>")
        }
    }
}


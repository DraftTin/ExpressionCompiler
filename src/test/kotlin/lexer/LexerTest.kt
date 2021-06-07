package lexer

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LexerTest {
    private val testSample: Lexer = Lexer()

    @Test
    fun testLexer() {
//        var file: File = File.createTempFile("testnote", "txt")
//        file.writeText("acs = \"xxxxxssdds\nsssssss\"\nasdzxc = 6")
//        var text = file.readText()
//        println(text)
        var data = "acs = \"xxxxxssdds\\nsssssss\"\nasdzxc = 6"
        var tokens = testSample.tokenlizer(data)
        for(token in tokens) {
            println("<${token.row} ${token.col} ${token.type} ${token.value}>")
        }
    }
}


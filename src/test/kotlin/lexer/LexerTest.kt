package lexer

import org.junit.jupiter.api.Test

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

internal class LexerTest {
    @Test
    fun tokenlize() {

        var file = File("test.txt")
        var fileReader = InputStreamReader(FileInputStream(file))
        val testSample = Lexer(fileReader)
        var tokens = ArrayList<Token>()
        while(true) {
            val tok = testSample.scan()
            tokens.add(tok)
            if(tok.kind == Token.EndOfFileToken.kind) break
        }
        for(tok in tokens) print("$tok ")
    }
}
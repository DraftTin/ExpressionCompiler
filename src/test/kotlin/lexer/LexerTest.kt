package lexer

import org.junit.jupiter.api.Test
import CodeAnalysis.syntax.Lexer
import CodeAnalysis.syntax.SyntaxToken

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

internal class LexerTest {
    @Test
    fun tokenlize() {

        var file = File("test.txt")
        var fileReader = InputStreamReader(FileInputStream(file))
        val testSample = Lexer(fileReader)
        var tokens = ArrayList<SyntaxToken>()
        while(true) {
            val tok = testSample.scan()
            tokens.add(tok)
            if(tok.kind == SyntaxToken.EndOfFileToken.kind) break
        }
        for(tok in tokens) print("$tok ")
    }
}
package CodeAnalysis.syntax

import java.io.Reader

class SyntaxTree(var diagnostics: List<String>, var root: ExpressionSyntax, var endOfFileToken: SyntaxToken) {
    companion object {
        fun createSyntaxTree(data: Reader): SyntaxTree {
            var parser = Parser(data)
            return parser.parse()
        }
    }
}

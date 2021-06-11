import lexer.Token
import parser.Parser
import parser.SyntaxNode
import java.io.InputStreamReader

fun prettyPrint(node: SyntaxNode, indent: String = "", isLast: Boolean = true) {
    // └──
    // │
    // ├──
    var marker = if(isLast) "└──" else "├──"
    print(indent)
    print(marker)
    print(node.kind)

    if(node is Token) {
        print(" ")
        print(node)
    }
    println()
    var newIndent = StringBuffer(indent)
    newIndent.append(if(isLast) "   " else "|  ")
    for(child in node.getChildren()) {
        prettyPrint(child, newIndent.toString(), child == node.getChildren().last())
    }
}

fun main() {
    while(true) {
        print("> ")
        var line = readLine()
        var parser = Parser(InputStreamReader(line!!.byteInputStream()))
        var expression = parser.parse()
        prettyPrint(expression)
        if(parser.diagnostics.isNotEmpty()) {
            for(diagnostic in parser.diagnostics) {
                println(diagnostic)
            }
        }
    }
}
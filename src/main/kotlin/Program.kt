import lexer.Token
import parser.Evaluator
import parser.SyntaxNode
import parser.SyntaxTree
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
    var showTree = false
    while(true) {
        print("> ")
        var line = readLine()
        if(line.equals("#showTree")) {
            showTree = !showTree
            println(if(showTree) "showing the tree" else "not showing the tree")
            continue
        }
        var syntaxTree = SyntaxTree.createSyntaxTree(InputStreamReader(line!!.byteInputStream()))
        if(showTree) {
            prettyPrint(syntaxTree.root)
        }
        if(syntaxTree.diagnostics.isNotEmpty()) {
            for(diagnostic in syntaxTree.diagnostics) {
                println("\u001b[38;5;196m$diagnostic")
                print("\u001b[0m")
            }
        }
        else {
            var evaluator = Evaluator(syntaxTree.root)
            var result = evaluator.evaluate()
            println(result)
        }
    }
}
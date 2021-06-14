import CodeAnalysis.Binding.Binder
import CodeAnalysis.syntax.SyntaxToken
import CodeAnalysis.Evaluator
import CodeAnalysis.syntax.SyntaxNode
import CodeAnalysis.syntax.SyntaxTree
import java.io.InputStreamReader

fun prettyPrint(node: SyntaxNode, indent: String = "", isLast: Boolean = true) {
    // └──
    // │
    // ├──
    var marker = if(isLast) "└──" else "├──"
    print(indent)
    print(marker)
    print(node.kind)

    if(node is SyntaxToken) {
        print(" ")
        print(node.value)
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
        if(line != null && line.isBlank()) continue
        if(line.equals("#showTree")) {
            showTree = !showTree
            println(if(showTree) "showing the tree" else "not showing the tree")
            continue
        }
        var syntaxTree = SyntaxTree.createSyntaxTree(InputStreamReader(line!!.byteInputStream()))
        var binder = Binder()
        var boundExpression = binder.bindExpression(syntaxTree.root)
        var diagnostics = syntaxTree.diagnostics + binder.diagnostics
        if(showTree) {
            prettyPrint(syntaxTree.root)
        }
        if(diagnostics.isNotEmpty()) {
            for(diagnostic in diagnostics) {
                println("\u001b[38;5;196m$diagnostic")
                print("\u001b[0m")
            }
        }
        else {
            var evaluator = Evaluator(boundExpression)
            var result = evaluator.evaluate()
            println(result)
        }
    }
}
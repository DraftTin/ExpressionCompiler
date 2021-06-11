package symbols

import lexer.Kind
import lexer.Word

/**
 * 定义类型名的token，如integer, float，也属于保留字，继承Word
 */
open class Type(lexeme: String, kind: Kind, width: Int) : Word(lexeme, kind) {
    val width = width
    companion object {
        val IntType = Type("int", Kind.BASIC, 4)
        val FloatType = Type("float", Kind.BASIC, 8)
        val CharType = Type("char", Kind.CHAR, 1)
        val BoolType = Type("bool", Kind.BOOL, 1)

        val Void = Type("void", Kind.VOID, 0)    // 空类型，用于占位

        fun numeric(p: Type): Boolean {
            if(p == CharType || p == FloatType || p == IntType) return true
            return false
        }

        fun max(p1: Type, p2: Type): Type {
            if(p1 == FloatType || p2 == FloatType) return FloatType
            else if(p1 == IntType || p2 == IntType) return IntType
            else if(p1 == Void && p2 == Void) return Void
            else return CharType
        }
    }
}
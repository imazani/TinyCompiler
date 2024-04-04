package org.example.Token;

public enum TokenType {
    // Terminal Nodes
    EOF,
    NUMBER,
    VARIABLE,
    NEWLINE,
    STRING,

    //Key_Words
    PRINT,
    INPUT,
    LET,
    IF,
    THEN,
    ENDIF,
    WHILE,
    REPEAT,
    ENDWHILE,

    // Operators
    EQUALS_OPERATOR,
    ADDITION_OPERATOR,
    SUBTRACTION_OPERATOR,
    MULTIPLICATION_OPERATOR,
    DIVISION_OPERATOR,
    EQEQ_Operator,
    GTEQ_Operator,
    LTEQ_Operator,
    NOTEQ_Operator,
    GT_Operator,
    LT_Operator,

}

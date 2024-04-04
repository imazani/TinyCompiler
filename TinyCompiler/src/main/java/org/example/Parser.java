package org.example;

import org.example.Lexer;
import org.example.Token.Token;
import org.example.Token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final Lexer lexer;
    private final Emitter emitter;

    public List<Token> variablesDeclared = new ArrayList<>(20);

    private Token currentToken;
    private Token nextToken;


    public Parser(Lexer lexer, Emitter emitter) throws Exception {
        this.lexer = lexer;
        this.emitter = emitter;
        lexer.getToken();
    }

    public  void nextToken() throws Exception {
        this.currentToken = this.nextToken;
        lexer.getToken();
        this.nextToken = this.lexer.currentToken;
    }

    // Return true of the current token matches a specific type
    public boolean checkToken(TokenType type) {
        return type == this.lexer.currentToken.getType();
    }

    // Return true if the next token matches specific type
    public boolean checkPeek(TokenType type) {
        return type == this.lexer.currentToken.getType();
    }

    // Confirm current token type is expected
    public void checkMatch(TokenType type) throws Exception {
        if (checkToken(type)) {
            this.nextToken();
        } else {
            throw new Exception("Expected " + type + " But Found " + this.lexer.currentToken.getType());
        }
    }

    public void parseProgram() throws Exception {
        this.emitter.emitHeader("#include <stdio.h>");
        this.emitter.emitHeader("int main(void) {");

        // Parse the next statement
        while (!checkToken(TokenType.EOF)){
            parseStatement();
        }

        this.emitter.emitLine("return 0;");
        this.emitter.emitLine("}");
    }

    public void parseStatement() throws Exception {

        if (checkToken(TokenType.PRINT)){
            // PRINT (expression | string)

            nextToken();

            if (checkToken(TokenType.STRING)) {
                this.emitter.emitLine("printf(\"" + lexer.currentToken.getValue() + "\\n\");");
                nextToken();
            } else{
                // The input is an expression
                this.emitter.emit("printf(\"%" + ".2f\\n\", (float)(");
                expression();
                this.emitter.emitLine("));");
            }
        } else if (checkToken(TokenType.LET)) {
            // "LET" ident = expression

            nextToken();

            // Check if variable has already been declared
            if (!isDeclared(lexer.currentToken)){
                this.variablesDeclared.add(lexer.currentToken);
                this.emitter.emitHeader("float " + lexer.currentToken.getValue() + ";");
            }

            this.emitter.emit(lexer.currentToken.getValue() + "=");
            checkMatch(TokenType.VARIABLE);
            checkMatch(TokenType.EQUALS_OPERATOR);
            expression();
            this.emitter.emitLine(";");

        } else if (checkToken(TokenType.INPUT)) {
             // "INPUT" ident

            nextToken();

            // Check if variable has already been declared
            if (!isDeclared(lexer.currentToken)){
                this.variablesDeclared.add(lexer.currentToken);
                this.emitter.emitHeader("float " + lexer.currentToken.getValue() + ";");
            }

            this.emitter.emitLine("if(0 == scanf(\"%" + "f\", &" + lexer.currentToken.getValue() + ")) {");
            this.emitter.emitLine(lexer.currentToken.getValue() + " = 0;");
            this.emitter.emit("scanf(\"%");
            this.emitter.emitLine("*s\");");
            this.emitter.emitLine("}");

            checkMatch(TokenType.VARIABLE);

        } else if (checkToken(TokenType.IF)) {
            // "IF" comparison "THEN" block "ENDIF"

            nextToken();
            this.emitter.emit("if(");
            comparison();

            checkMatch(TokenType.THEN);
            newLine();
            this.emitter.emitLine("){");

            while(!checkToken(TokenType.ENDIF)) {
                parseStatement();
            }
            checkMatch(TokenType.ENDIF);
            this.emitter.emitLine("}");

        } else if (checkToken(TokenType.WHILE)) {
            // "WHILE" comparison "REPEAT" block "ENDWHILE"

            nextToken();
            this.emitter.emit("while(");
            comparison();

            checkMatch(TokenType.REPEAT);
            newLine();
            this.emitter.emitLine(") {");

            while(!checkToken(TokenType.ENDWHILE)) {
                parseStatement();
            }

            checkMatch(TokenType.ENDWHILE);
            this.emitter.emitLine("}");
        } else {
            throw new Exception("Invalid statement at TOKEN TYPE: " + lexer.currentToken.getValue() + " TOKEN VALUE: " + lexer.currentToken.getType());
        }

        newLine();
    }

    public void newLine() throws Exception {

        //checkMatch(TokenType.NEWLINE);
        while (checkToken(TokenType.NEWLINE)) {
            nextToken();
        }
    }

    // comparison ::= expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)
    public void comparison() throws Exception {

        expression();

        if(isComparison()){
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();
            expression();
        } else {
            throw new Exception("Expected comparison operator at: " + lexer.currentToken.getValue() + " TOKEN VALUE: " + lexer.currentToken.getType());
        }

        // Handles multiple comparison operator and expressions
        while(isComparison()) {
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();
            expression();
        }
    }

    public boolean isComparison() {

        // Check if operator is comparison operator
        return (checkToken(TokenType.EQEQ_Operator) |
                checkToken(TokenType.NOTEQ_Operator) |
                checkToken(TokenType.GT_Operator) |
                checkToken(TokenType.GTEQ_Operator) |
                checkToken(TokenType.LTEQ_Operator) |
                checkToken(TokenType.LT_Operator));
    }

    // expression ::= term {( "-" | "+" ) term}
    public void expression() throws Exception {

        term();

        // Handles multiple '+'/'-' expressions
        while(checkToken(TokenType.ADDITION_OPERATOR) | checkToken(TokenType.SUBTRACTION_OPERATOR)){
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();
            term();
        }

    }

    // term ::= unary {( "/" | "*") unary}
    public void term() throws Exception {

        unary();

        // Handles situations where there are 0 or more '*' or '/'
        while(checkToken(TokenType.MULTIPLICATION_OPERATOR) | checkToken(TokenType.DIVISION_OPERATOR)){
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();
            unary();
        }
    }

    // unary ::= ["+" | "-"] primary
    public void unary() throws Exception {

        if(checkToken(TokenType.SUBTRACTION_OPERATOR) | checkToken(TokenType.ADDITION_OPERATOR)){
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();
        }
        primary();
    }

    // primary ::= number | ident
    public void primary() throws Exception {

        if (checkToken(TokenType.NUMBER)){
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();
        } else if (checkToken(TokenType.VARIABLE)) {

            // Check if variable has already been declared
            if (!isDeclared(lexer.currentToken)){
                throw new Exception("Cannot reference variable before assignment:  " + this.lexer.currentToken.getValue());
            }
            this.emitter.emit(lexer.currentToken.getValue());
            nextToken();

        }else {
            throw new Exception("Unexpected token: Expected NUMBER or VARIABLE but found: " + this.lexer.currentToken.getType());
        }
    }

    public boolean isDeclared(Token variable) {
        for (Token declaredVariable : this.variablesDeclared) {

            if (variable.equals(declaredVariable)) {
                return true;
            }
        }
        return false;
    }
}

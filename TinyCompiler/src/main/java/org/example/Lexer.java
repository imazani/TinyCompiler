package org.example;

import org.example.Token.Token;
import org.example.Token.TokenType;

import java.util.Arrays;

public class Lexer {

    private final String code;
    private final int codeLength;
    private int currentIndex = 0;

    public Token currentToken;
    private Token previousToken;

    public Lexer(String code, int codeLength) {
        this.code = code;
        this.codeLength = code.length();
        //getToken();
    }

    // Process the next character
    public char nextChar() {
        currentIndex++;
        if (currentIndex + 1 >= codeLength) {
            return '\0';
        }
        return this.code.charAt(currentIndex);
    }

    // Return the look ahead character
    public char peek() {
        if (currentIndex + 1 >= codeLength) {
            return '\0';
        }
        return code.charAt(currentIndex + 1);
    }

    //Return current character
    public char getChar() {
        if (currentIndex >= codeLength) {
            return '\0';
        }
        return code.charAt(currentIndex);
    }


    // Invalid token found, print error message and exit.
    public void abort() {
    }

    // Skip whitespace except newlines, which we will use to indicate the end of a statement
    public void skipWhitespace() {
        while(Arrays.asList(' ', '\r', '\t').contains(getChar())) {
            currentIndex++;
        }
    }

    // Skip comments in the code.
    public void skipComment() {
    }

    // Read a number token as a string
    public String readNumber() {
        String num = "";
        while(Character.isDigit(getChar())) {
            num += getChar();
            nextChar();
        }
        return num;
    }

    // @return String read
    public String readVariable() {
        String var = "";
        while(Character.isLetter(getChar())) {
            var += getChar();
            nextChar();
        }
        return var;
    }

    public String readString(){
        String string = "";
        nextChar();
        while(getChar() != '\"') {
            string += getChar();
            nextChar();
        }
        return string;
    }

    // Return the next token
    public boolean getToken() throws Exception {
        while (getChar() != ' ' || getChar() != '\0') {

            previousToken = currentToken;

            if (Arrays.asList(' ', '\r', '\t').contains(getChar())) {
                skipWhitespace();
                continue;
            } else if (getChar() == '=') {
                // Equals_OPERATOR vs EQEQ_OPERATOR
                if (peek() == '=') {
                    currentToken = new Token(TokenType.EQEQ_Operator, "==");
                    currentIndex++;
                } else {
                    currentToken = new Token(TokenType.EQUALS_OPERATOR, "=");
                }
                currentIndex++;
            } else if (getChar() == '>') {
                // GT_OPERATOR vs GTEQ_OPERATOR
                if (peek() == '=') {
                    currentToken = new Token(TokenType.GTEQ_Operator, ">=");
                    currentIndex++;
                } else {
                    currentToken = new Token(TokenType.GT_Operator, ">");
                }
                currentIndex++;
            } else if (getChar() == '<') {
                // LT_OPERATOR vs LTEQ_OPERATOR
                if (peek() == '=') {
                    currentToken = new Token(TokenType.LTEQ_Operator, "<=");
                    currentIndex++;
                } else {
                    currentToken = new Token(TokenType.LT_Operator, "<");
                }
                currentIndex++;
            } else if (getChar() == '+') {
                currentToken = new Token(TokenType.ADDITION_OPERATOR, "+");
                currentIndex++;
            } else if (getChar() == '-') {
                currentToken = new Token(TokenType.SUBTRACTION_OPERATOR, "-");
                currentIndex++;
            } else if (getChar() == '/') {
                currentToken = new Token(TokenType.DIVISION_OPERATOR, "/");
                currentIndex++;
            } else if (getChar() == '*') {
                currentToken = new Token(TokenType.MULTIPLICATION_OPERATOR, "*");
                currentIndex++;
            } else if (getChar() == '\"') {
                //currentIndex++;
                currentToken = new Token(TokenType.STRING, readString());
                currentIndex++;
                currentIndex++;
            } else if (Character.isDigit(getChar())) {
                currentToken = new Token(TokenType.NUMBER, readNumber());
            } else if (Character.isLetter(getChar())) {
                String inputText = readVariable();
                TokenType keyword = Token.isKeyword(inputText);
                if (keyword == null) {
                    // The string is a variable/identifier
                    currentToken = new Token(TokenType.VARIABLE, inputText);
                } else {
                    // The input text is a keyword, create keyword token
                    currentToken = new Token(keyword);
                }
            } else if (getChar() == '\n') {
                currentToken = new Token(TokenType.NEWLINE);
                currentIndex++;
            } else if (getChar() == '\0') {
                currentToken = new Token(TokenType.EOF);
            } else {
                throw new Exception("Token Not defined");
            }
            return true;
        }
        return false;
    }

}

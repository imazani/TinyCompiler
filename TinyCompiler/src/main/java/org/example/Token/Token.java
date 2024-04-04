package org.example.Token;

import java.util.Objects;

public class Token {

    private final TokenType type;

    private final String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    public Token(TokenType type) {
        this.type = type;
        this.value = null;
    }

    public  TokenType getType(){
        return type;
    }

    public  String getValue() {
        return value;
    }

    public static TokenType isKeyword (String inputText) {
        for (TokenType type : TokenType.values()) {
            if (type.name().equals(inputText)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(!(obj instanceof Token)) {
            return false;
        }

        Token other = (Token) obj;

        return (Objects.equals(this.value, other.getValue()) && this.type == other.getType());
    }

    @Override
    public String toString(){
        return this.type.toString() + " " + this.value;
    }
}

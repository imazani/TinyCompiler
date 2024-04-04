package lexer;

import org.example.Lexer;
import org.junit.jupiter.api.Test;
import org.junit.Assert;

public class LexerTester {

    @Test
    public void lexerIterTest() {
        String code = "Lexer Test";
        String output= "";

        Lexer lexer = new Lexer(code, code.length());

        while(lexer.getChar() != '\0') {
            output += lexer.getChar();
            lexer.nextChar();
        }

        Assert.assertEquals(code, output);
    }

    @Test
    public void readNumberTest() {
        String code = "123 OneTwoThree";
        Lexer lexer = new Lexer(code, code.length());
        String output = lexer.readNumber();
        Assert.assertEquals("123", output);
    }

    @Test
    public void readVariableTest() {
        String code = "Variable 123";
        Lexer lexer = new Lexer(code, code.length());
        String output = lexer.readVariable();
        Assert.assertEquals("Variable", output);
    }
}

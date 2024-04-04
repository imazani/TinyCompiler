package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Tiny Compiler Test");

        String inputFilePath = new File("src/main/resources/program.txt").getAbsolutePath();
        String program = readFileAsString(inputFilePath);

        String outFilePath = new File("src/main/resources/out.c").getAbsolutePath();

        Lexer lexer = new Lexer(program, program.length());
        Emitter emitter = new Emitter(outFilePath);
        Parser parser = new Parser(lexer, emitter);
        parser.parseProgram();
        emitter.writeFile();

        System.out.println("Compilation complete");


   }

    public static String readFileAsString(String fileName) throws Exception {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
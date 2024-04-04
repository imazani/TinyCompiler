package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Emitter {

    public String outputFileName;
    public String code = "";
    public String header = "";

    public Emitter (String fileName) {
        this.outputFileName = fileName;
    }

    public void emit(String inputCode){
        this.code += inputCode;
    }

    public void emitLine(String inputCode){
        this.code += inputCode + '\n';
    }

    public void emitHeader(String inputHeader){
        header += inputHeader + '\n';
    }

    public void writeFile(){
        creatFile();
        try{
            FileWriter writer = new FileWriter(outputFileName);
            writer.write(header + code);
            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        }
    }

    public void creatFile(){
        try {
            File fileCreator = new File(outputFileName);
            if(fileCreator.createNewFile()) {
                System.out.println("File Created: " + fileCreator.getName());
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating this file.");
            e.printStackTrace();
        }
    }


}

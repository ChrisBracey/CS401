/*
 * The driver code for the project
 */

package clite;

public class Main {

    public static void main(String[] args) {
        String file = "programs/hello.cpp";
    	System.out.println("Begin parsing... " + file);
    	Parser parser  = new Parser(new Lexer(file));
        Program prog = parser.program();
        prog.display();      // display abstract syntax tree
    }
}

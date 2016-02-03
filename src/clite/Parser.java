/*
 * Parser.java is a recursive descent parser that inputs a CLite program
 * and generates its abstract syntax.  Each method corresponds to a
 * concrete syntax grammar rule, which appears as a comment at the
 * beginning of the method.
 */

package clite;

public class Parser {

    Token token;          // current token from the input stream
    Lexer lexer;

    public Parser(Lexer ts) { // Open the Clite source program
        lexer = ts;           // as a token stream, and
        token = lexer.next(); // retrieve its first Token
    }

    private String match (TokenType t) {
        String value = token.value();
        if (token.type().equals(t))
            token = lexer.next();
        else
            error(t);
        return value;
    }

    private void error(TokenType tok) {
        System.err.println("Syntax error: expecting: " + tok
                           + "; saw: " + token);
        System.exit(1);
    }

    private void error(String tok) {
        System.err.println("Syntax error: expecting: " + tok
                           + "; saw: " + token);
        System.exit(1);
    }

    public Program program() {
        // Program --> void main ( ) '{' Declarations Statements '}'
        TokenType[ ] header = {TokenType.Int, TokenType.Main,
                          TokenType.LeftParen, TokenType.RightParen};
        for (int i=0; i<header.length; i++)   // bypass "int main ( )"
            match(header[i]);
        match(TokenType.LeftBrace);
        Declarations decpart = declarations();
        Block body = statements();
        match(TokenType.RightBrace);
        return new Program(decpart, body);
    }

    private Declarations declarations () {
        // Declarations --> { Declaration }
        Declarations ds = new Declarations();
        while (isType()) {
            declaration(ds);
        }
        return ds;
    }

    private void declaration (Declarations ds) {
        // Declaration  --> Type Identifier { , Identifier } ;
        Type t = type();
        Variable v = new Variable(match(TokenType.Identifier));
        ds.add(new Declaration(v, t));
        while (token.type().equals(TokenType.Comma)) {
            match(TokenType.Comma);
            v = new Variable(match(TokenType.Identifier));
            ds.add(new Declaration(v, t));
        }
        match(TokenType.Semicolon);
    }

    private Type type () {
        // Type  -->  int | bool | float | char
        Type t = null;
        if (token.type().equals(TokenType.Int))
            t = Type.INT;
        else if (token.type().equals(TokenType.Bool))
            t = Type.BOOL;
        else if (token.type().equals(TokenType.Float))
            t = Type.FLOAT;
        else if (token.type().equals(TokenType.Char))
            t = Type.CHAR;
        else error("int | bool | float | char");
        token = lexer.next(); // pass over the type
        return t;
    }

    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement
        return null;
    }

    private Block statements () {
        // Block --> '{' Statements '}'
        return null;
    }

    private Assignment assignment () {
        // Assignment --> Identifier = Expression ;
        return null;
    }

    private Conditional ifStatement () {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
        return null;
    }

    private Loop whileStatement () {
        // WhileStatement --> while ( Expression ) Statement
        return null;
    }

    private Expression expression () {
        // Expression --> Conjunction { || Conjunction }
        return null;
    }

    private Expression conjunction () {
        // Conjunction --> Equality { && Equality }
        return null;
    }

    private Expression equality () {
        // Equality --> Relation [ EquOp Relation ]
        return null;
    }

    private Expression relation (){
        // Relation --> Addition [RelOp Addition]
        return null;
    }

    private Expression addition () {
        // Addition --> Term { AddOp Term }
        return null;
    }

    private Expression term () {
        // Term --> Factor { MultiplyOp Factor }
        return null;
    }

    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary
        return null;
    }

    private Expression primary () {
        // Primary --> Identifier | Literal | ( Expression )
        //             | Type ( Expression )
        return null;
    }

    private Value literal( ) {
        String s = null;
        switch (token.type()) {
        case IntLiteral:
            s = match(TokenType.IntLiteral);
            return new IntValue(Integer.parseInt(s));
        case CharLiteral:
            s = match(TokenType.CharLiteral);
            return new CharValue(s.charAt(0));
        case True:
            s = match(TokenType.True);
            return new BoolValue(true);
        case False:
            s = match(TokenType.False);
            return new BoolValue(false);
        case FloatLiteral:
            s = match(TokenType.FloatLiteral);
            return new FloatValue(Float.parseFloat(s));
        }
        throw new IllegalArgumentException( "should not reach here");
    }


    private boolean isAddOp( ) {
        return false;
    }

    private boolean isMultiplyOp( ) {
        return false;
    }

    private boolean isUnaryOp( ) {
        return false;
    }

    private boolean isEqualityOp( ) {
        return false;
    }

    private boolean isRelationalOp( ) {
        return false;
    }

    private boolean isType( ) {
        return false;
    }

    private boolean isLiteral( ) {
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }

    private boolean isBooleanLiteral( ) {
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }

    public static void main(String args[]) {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
        prog.display();           // display abstract syntax tree
    } //main

} // Parser

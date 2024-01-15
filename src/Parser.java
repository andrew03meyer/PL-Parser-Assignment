import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parser for DSL programs.
 * TODO: Complete this class to implement a parser for DSL.
 */
public class Parser
{
    // The lexical analyser.
    private final Tokenizer lex;
    // The global symbol table.
    private final SymbolTable st;
    // The current token driving the parse.
    // This is set by a call to getNextToken();
    private Token currentToken;
    // A debugging flag.
    // Set this to false when not required.
    private boolean debug = true;

    /**
     * Create a parser.
     *
     * @param lex The lexical analyser.
     * @param st  Global symbol table for the program.
     */
    public Parser(Tokenizer lex, SymbolTable st)
    {
        this.lex = lex;
        this.st = st;
        currentToken = null;
        // Set currentToken to the first token of the input.
        getNextToken();
        if(debug && currentToken != null) {
            // Show details of the first token.
            //System.out.print("The first token is: ");
            //System.out.println(getTokenDetails());
        }
    }

    /**
     * Parse the full input.
     * @return true if the parse is successful, false otherwise.
     */
    public boolean parseProgram()
    {
        // The first token is already available in the currentToken variable.
        //Pass each token into loop until no more tokens
        do{
            parseStatement();
        }
        while(currentToken != null);

        return debug;
    }

    public boolean parseStatement() {
        if (expectKeyword(Keyword.INT)){
//            System.out.println("declaration: " + getTokenDetails());
            return parseDeclaration();
        }
        else if(expectIdentifier()){
//                System.out.println("assignment: " + getTokenDetails());
                return parseAssignment();
            }
        else if(expectKeyword(Keyword.IF)){
//                System.out.println("conditional: " + getTokenDetails());
                return parseConditional();
            }
        else if(expectKeyword(Keyword.WHILE)){
//                System.out.println("loop: " + getTokenDetails());
                return parseLoop();
            }
        else if(expectKeyword(Keyword.PRINT)){
//                System.out.println("print");
                return parsePrint();
            }
        else{
                throw new SyntaxException("error: " + getTokenDetails());
                //return false;
            }
    }

    /**
     * checks KEYWORD, IDENTIFIER, calls checkComma, checks SYMBOL == ";"
     * @return
     */
    public boolean parseDeclaration(){
        String variable = "";
        if(currentToken == Token.IDENTIFIER) {
            variable = lex.getIdentifier();
        }
        else{
            return false;
        }
        if(currentToken == Token.IDENTIFIER) {
            getNextToken();
            if(checkCommaIdentifier()){
                if (currentToken == Token.SYMBOL && lex.getSymbol().equals(";")){
                    st.declare(variable);
                    getNextToken();
                    return true;
                }
            }
        }
        debug = false;
        return false;
    }

    private boolean checkCommaIdentifier(){

        //Termination conditions
        if(!debug){return false;}

        //Checks for ,
        if(expectSymbol(",")){
            //checks for identifier
            if(currentToken == Token.IDENTIFIER){
                st.declare(lex.getIdentifier());
                getNextToken();
                //if identifier found, return true
                if(checkCommaIdentifier()){
                    return true;
                }
            }
            debug = false;
            return false;
        }
        //Anything ending in identifier is true
        return true;
    }
    public boolean parseAssignment(){
        if(expectSymbol(":=")){
            if(checkExpression() && expectSymbol(";")){
                return true;
            }
        }
        debug = false;
        return debug;
    }

    /**
     * Recursive method to check expressions
     * @return if the expression is valid
     */
    private boolean checkExpression(){
        if(checkTerm()){
            if(checkBinOp()){
                checkExpression();
            }
            return true;
        }
        debug = false;
        return debug;
    }

    /**
     * checks for: variable, integer constant, "(" ")", and "-"
     * @return
     */
    private boolean checkTerm(){
        //check for int value
        if(currentToken == Token.INT_CONST){
            getNextToken();
            return true;
        }
        //check var name
        if(expectIdentifier()){
            return true;
        }
        //check for "("
        if(expectSymbol("(") && checkExpression() && expectSymbol(")")){
            return true;
        }
        //check for "-"
        if(expectSymbol("-") && checkTerm()){
            return true;
        }
        debug = false;
        return debug;
    }

    private boolean checkBinOp(){
        ArrayList<String> accepted = new ArrayList<>(Arrays.asList("+", "-", "=", "!=", "<", ">", "<=", ">="));
        for(String item : accepted){
           if(expectSymbol(item)){
               return true;
           }
        }
        return false;
    }
    public boolean parseConditional() {
        //if (expression THEN statement)
        if(checkExpression() && expectKeyword(Keyword.THEN)/* && parseStatement()*/){
            //while not FI or ELSE, parse statements
            while(currentToken == Token.KEYWORD && lex.getKeyword() != Keyword.ELSE && lex.getKeyword() != Keyword.FI){
                parseStatement();
            }
            //If line contains ELSE
            if(expectKeyword(Keyword.ELSE)){
                while(lex.getKeyword() != Keyword.FI) {
                    parseStatement();
                }
            }
            //Checks if FI is next, returns true if so, false if not
            if(expectKeyword(Keyword.FI)){
                getNextToken();
                return true;
            }
        }

        return false;
    }

    /**
     * checks expression, then DO, then statement(s), then OD
     * @return
     */
    public boolean parseLoop(){
        if(checkExpression() && expectKeyword(Keyword.DO)){

            //go through every statement
            while(currentToken == Token.KEYWORD && lex.getKeyword() != Keyword.OD) {
                parseStatement();
            }
            if(expectKeyword(Keyword.OD)){
                getNextToken();
                return true;
            }
        }
        debug = false;
        return debug;
    }
    /**
     * Parse a print statement:
     *     print ::= PRINT expression ;
     * This method is complete.
     * @return true if a print statement is found, false otherwise.
     */
    private boolean parsePrint()
    {
        if (checkExpression()) {
            getNextToken();
            return true;
        } else {
            throw new SyntaxException("Missing expression");
        }
    }

    /**
     * Check whether the given Keyword is the current token.
     * If it is then <b>get the next token</b> and return true.
     * Otherwise, return false.
     * @param aKeyword The keyword to check for.
     * @return true if the keyword is the current token, false otherwise.
     */
    private boolean expectKeyword(Keyword aKeyword)
    {
        if(currentToken == Token.KEYWORD && lex.getKeyword() == aKeyword) {
            getNextToken();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check whether the given Symbol is the current token.
     * If it is then <b>get the next token</b> and return true.
     * Otherwise, return false.
     * @return true if the keyword is the current token, false otherwise.
     */
    private boolean expectSymbol(String symbol)
    {
        if(currentToken == Token.SYMBOL && symbol.equals(lex.getSymbol())){
            getNextToken();
            return true;
        }
        return false;
    }

    /**
     * Checks the symbol table for Identifier
     * @return - boolean
     */
    public boolean expectIdentifier(){
        if(currentToken == Token.IDENTIFIER && st.isDeclared(lex.getIdentifier())){
            getNextToken();
            return true;
        }
        return false;
    }

    /**
     * A debugging method.
     * Access details of the current token from the tokenizer and
     * format a String with the details.
     * @return a formatted version of the current token.
     */
    private String getTokenDetails()
    {
        StringBuilder s = new StringBuilder();
        s.append(currentToken).append(' ');
        switch(currentToken) {
            case KEYWORD:
                s.append(lex.getKeyword()); break;
            case IDENTIFIER:
                s.append(lex.getIdentifier()); break;
            case SYMBOL:
                s.append(lex.getSymbol()); break;
            case INT_CONST:
                s.append(lex.getIntval()); break;
            default:
                s.append("???"); break;

        }
        s.append(' ');
        return s.toString();
    }
    /**
     * Advance to the next token.
     * Sets currentToken.
     */
    private void getNextToken()
    {
        if (lex.hasMoreTokens()) {
            lex.advance();
            currentToken = lex.getTokenType();
        } else {
            currentToken = null;
        }
    }
}
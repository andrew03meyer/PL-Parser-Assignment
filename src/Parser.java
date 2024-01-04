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
            System.out.print("The first token is: ");
            System.out.println(getTokenDetails());
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
//        boolean valid = false;
        do{
            System.out.println(parseStatement());

            //Testing
            //System.out.println("get token deets: " + getTokenDetails());
            //System.out.println("lex getKeyword: " + lex.getIdentifier());

            //getNextToken();
        }
        while(currentToken != null);

        return debug;
    }

    public boolean parseStatement() {
        //Testing
        //System.out.println(currentToken);
        //System.out.println(lex.getIdentifier());

        if (expectKeyword(Keyword.INT)){
            System.out.println("declaration");
            return parseDeclaration();
        }
        else if(expectIdentifier()){
                System.out.println("assignment");
                return parseAssignment();
            }
        else if(expectKeyword(Keyword.IF)){
                System.out.println("conditional");
                return parseConditional();
            }
        else if(expectKeyword(Keyword.WHILE)){
                System.out.println("loop");
                return parseLoop();
            }
        else if(expectKeyword(Keyword.PRINT)){
                System.out.println("print");
                return parsePrint();
            }
        else{
                System.out.println("error: " + getTokenDetails());                //needs editing
                throw new SyntaxException("error");
                //return false;
            }
    }

    /**
     * checks KEYWORD, IDENTIFIER, calls checkComma, checks SYMBOL == ";"
     * @return
     */
    public boolean parseDeclaration(){
        String variable = lex.getIdentifier();
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

//        System.out.println("current token " + getTokenDetails());
        //Checks for ,
        if(expectSymbol(",")){
            //checks for identifier
            if(currentToken == Token.IDENTIFIER){
//                System.out.println("identifier: " + lex.getIdentifier());
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
        //System.out.println("check expression");
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
        //System.out.println("check term");
        //check fro int value
        if(currentToken == Token.INT_CONST){
            getNextToken();
            return true;
        }
        //check var name
        if(expectIdentifier()){
            //System.out.println("foudn identifier");
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
    public boolean parseConditional(){return false;}

    /**
     * checks expression, then DO, then statement(s), then OD
     * @return
     */
    public boolean parseLoop(){
        if(checkExpression() && expectKeyword(Keyword.DO)){
            System.out.println(getTokenDetails());
            if(parseStatement() && expectKeyword(Keyword.OD)){
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
//        System.out.println(getTokenDetails());
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
        ArrayList<String> symbols = new ArrayList<>(Arrays.asList("{", "}", "[", "]", "(", ")", "|", "&", "<", ">", "=", "+", "-", ";", ":=", "!", "?", "<=",">=", "!=", ",", ";"));
        if(currentToken == Token.SYMBOL && symbols.contains(symbol) && symbol.equals(lex.getSymbol())){
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
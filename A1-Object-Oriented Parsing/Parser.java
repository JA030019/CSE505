
/* OUTLINE OF OBJECT-ORIENTED PARSER FOR TINY PL */

public class Parser {
	static Program p;
	public static void main(String[] args)  {
		System.out.println("Enter program and terminate with 'end'!\n");
		Lexer.lex();
		p = new Program();
		System.exit(0);
	}
}

//program -> { function }+ end
class Program {
	public Program() {
		while (Lexer.nextToken == Token.KEY_INT) {
			//Lexer.lex();
			SymTab.init();
			Code.init();
			Function f = new Function();
			Code.output();
		}
	}
}

// fill in the details of the following classes
// function -> int id pars '{' body '}'
class Function {
	Body b;
	public Function(){
		b = new Body();
	}
}

// pars -> '(' { int id } ')'
class Pars {
	
}

// body -> decls stmts
class Body {
	Decls d;
	Stmts ss;
	public Body(){
		if(Lexer.nextToken == Token.KEY_INT){ 
			d = new Decls();
			}
		ss = new Stmts();
	}
}

// decls -> int idlist ';'
class Decls {
	Idlist list;
	public Decls(){
		Lexer.lex();
		list = new Idlist();
	}
}

// idlist -> id [',' idlist ]
class Idlist {
	Idlist newlist;
	String s;
	public Idlist(){
		s = Lexer.ident;
		SymTab.add(s);
		Lexer.lex();
		if(Lexer.nextToken == Token.COMMA){
			Lexer.lex();
			newlist = new Idlist();
		}
		else Lexer.lex();
	}
}

// stmts -> stmt [ stmts ]
class Stmts {
	Stmts ss;
	Stmt s;
	public Stmts(){
		s = new Stmt();
		if(Lexer.nextToken == Token.ID||
				Lexer.nextToken == Token.LEFT_BRACE||
				Lexer.nextToken == Token.KEY_IF||
				Lexer.nextToken == Token.KEY_WHILE){
			ss = new Stmts();
		}
	}
}

// stmt -> assign ';' | cond | loop | cmpd |
// return expr ';
class Stmt {
	Assign a;
	Cond c;
	Loop l;
	Cmpd cm;
	public Stmt(){
		switch(Lexer.nextToken){
			case Token.ID:{
				a = new Assign();
				break;
			}
			case Token.KEY_IF:{
				c = new Cond();
				break;
			}
			case Token.KEY_WHILE:{
				l = new Loop();
				break;
			}
			case Token.LEFT_BRACE:{
				cm = new Cmpd();
				break;
			}
			default: { break;}
		}
	}
} 

// assign -> id '=' expr
class Assign {
	Expr e;
	int i;
	public Assign(){
		i = SymTab.isPresent(Lexer.ident);
		if(i==0){System.err.println("Identifier not in the table!");}
		Lexer.lex();
		Lexer.lex();
		e = new Expr();
		if(i<4) {Code.gen("istore_" + i);}
		else {Code.gen("istore " + i); Code.skip(1);}
		Lexer.lex();
	}
}

// cond -> if '(' relexp ')' stmt [ else stmt ]
class Cond {
	Rexpr rex;
	Stmt s1, s2;
	private int n2, n3;
	public Cond(){
		Lexer.lex();
		Lexer.lex();
		rex = new Rexpr();
		Code.skip(2);
		n3 = Rexpr.n1;
		Lexer.lex();
		s1 = new Stmt();
		if(Lexer.nextToken == Token.KEY_ELSE){
			Lexer.lex();
			n2 = Code.codeptr;
			Code.gen("goto ");
			Code.skip(2);
			Code.code[n3] = Code.code[n3] + (Code.codeptr);
			s2 = new Stmt();
			Code.code[n2]= "goto " + Code.codeptr;
		}
		else Code.code[n3] = Code.code[n3] + (Code.codeptr);
	}
}

// loop -> while '(' relexp ')' stmt
class Loop {
	private int w1, w2;
	Rexpr rel;
	Stmt s;
	public Loop(){
		w1 = Code.codeptr;
		Lexer.lex();
		Lexer.lex();
		rel = new Rexpr();
		w2 = Rexpr.n1;
		Code.skip(2);
		Lexer.lex();
		s = new Stmt();	
		Code.gen("goto "+ w1);
		Code.skip(2);
		Code.code[w2] = Code.code[w2] + (Code.codeptr);
	}
}

// cmpd -> '{' stmts '}'
class Cmpd {
	Stmts ss;
	public Cmpd(){
		Lexer.lex();
		ss = new Stmts();
		Lexer.lex();  // scan over the right brace
	}
}

class Return {
}

// relexp -> expr ('<' | '>' | '<=' | '>=' | '==' | '!= ') expr
class Rexpr {
	Expr e1, e2;
	public static int n1;
	public Rexpr(){
		e1 = new Expr();
		switch(Lexer.nextToken){
		case Token.LESSER_OP:{
			Lexer.lex();
			e2 = new Expr();
			n1 = Code.codeptr;
			Code.gen("if_icmpge ");
			break;}
		case Token.GREATER_OP:{
			Lexer.lex();
			e2 = new Expr();
			n1 = Code.codeptr;
			Code.gen("if_icmple ");	
			break;}
		case Token.LESSEQ_OP:{
			Lexer.lex();
			e2 = new Expr();
			n1 = Code.codeptr;
			Code.gen("if_icmpgt ");
			break;}
		case Token.GREATEREQ_OP:{
			Lexer.lex();
			e2 = new Expr();
			n1 = Code.codeptr;
			Code.gen("if_icmplt ");
			break;}
		case Token.NOT_EQ:{
			Lexer.lex();
			e2 = new Expr();
			n1 = Code.codeptr;
			Code.gen("if_icmpeq ");
			break;}
		case Token.EQ_OP:{			
			Lexer.lex();
			e2 = new Expr();
			n1 = Code.codeptr;
			Code.gen("if_icmpne ");
			break;}
		}

	}
}

// expr -> term (+ | -) expr | term
class Expr { 				 
	Term t;
	char op;
	Expr e;

	public Expr() {	 // C is an inherited attribute for Expr
		t = new Term();
		if (Lexer.nextToken == Token.ADD_OP || Lexer.nextToken == Token.SUB_OP) {
			op = Lexer.nextChar;
			Lexer.lex();    // scan over op
			e = new Expr();
			Code.gen(op);      // generate the byte-code for op
		}
	}
}

// term -> factor (* | /) term | factor
class Term { 				
	Factor f;
	char op;
	Term t;

	public Term() {
		f = new Factor();
		if (Lexer.nextToken == Token.MULT_OP || Lexer.nextToken == Token.DIV_OP) {
			op = Lexer.nextChar;
			Lexer.lex();     // scan over op
			t = new Term();
			Code.gen(op);
		}
	}
}

// factor -> number | '(' expr ')'
class Factor { 				
	int i;
	Expr e;

	public Factor() {
		switch (Lexer.nextToken) {
		case Token.INT_LIT: // number
			i = Lexer.intValue;
			Lexer.lex();         // scan over int
			Code.gen(i);            // generate byte-code for i
			break;
		case Token.LEFT_PAREN: 
			Lexer.lex();        // scan over '('
			e = new Expr();
			Lexer.lex();        // scan over ')'
			break;
		case Token.ID: 
			i = SymTab.isPresent(Lexer.ident);
			if(i<4) {Code.gen("iload_" + i);}
			else {Code.gen("iload " + i);Code.skip(1);}
			Lexer.lex();
			break;
		default:
			break;
		}
	}
}

// exprlist -> expr [ ',' exprlist ]
class ExprList {
}

class SymTab {
	public static String[] var;
	public static int index;
	public static void init(){
		var = new String[100];
		index = 1;
	}
	public static void add(String s){
		if (isPresent(s)==0){
			var[index] = s;index++;}
	}
	public static int isPresent(String st){
		for(int i = 1; i<100; i++){
			if(st.equals(var[i])){return i;}
			}
		return 0;
	}
}

class Code {   

	public static String[] code;
	
	public static int codeptr;
	
	public static void init() {
		code = new String[100];
		codeptr = 0;
	}

	public static void gen(String s) {
		code[codeptr] = s;
		codeptr++;
	}

	public static void gen(char c) {
		gen(opcode(c));
	}

	public static void gen(int i) {
		if (i < 6 && i > -1)
			gen("iconst_" + i);
		else if (i < 128) {
			gen("bipush " + i);
			skip(1);
		} else {
			gen("sipush " + i);
			skip(2);
		}
	}
	
	public static void skip(int n) {
		codeptr = codeptr + n;
	}

	public static String opcode(char c) {
		switch (c) {
		case '+':
			return "iadd";
		case '-':
			return "isub";
		case '*':
			return "imul";
		case '/':
			return "idiv";
		default:
			return "";
		}
	}

	public static void output() {
		
		System.out.println("Code:");
		
		for (int i = 0; i < codeptr; i++)
			if (code[i] != null && code[i] != "")
				System.out.println("     " + i + ": " + code[i]);
	}
}


    

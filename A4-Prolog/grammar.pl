
% ___________GRAMMAR RULES FOR TINY PL	______________


program --> ['#'].
program --> function, program.


% Write the DCG for 'function' over here.  Each parsed function
% should result in one asserted fact of the form: fun(Name,Body).
% See samplefacts.txt for the facts to be asserted for defs.txt.

function--> type, id(N), ['('], pars, [')'], ['{'], decls, stmts(B), ['}'], {asserta(fun(N, B))}.

type --> [int].
type --> [real].
type --> [boolean].
type --> [double].

pars --> [].
pars --> type, id(X).
pars --> type, id(X), [','], pars.

decls --> [].
decls --> type, idlist, [';'].

idlist --> id(X).
idlist --> id(X), [','], idlist.


stmts(S) --> stmt(S).
stmts((S1; S2)) -->stmt(S1), stmts(S2).

stmt(S) --> assign(S),  [';'].
stmt(S) --> cond(S).
stmt(S) --> loop(S).
stmt(S) --> cmpd(S).
stmt(exit) --> [exit], [';'].
stmt(return(S)) --> [return], expr(S),[';'].

cmpd(S) --> ['{'],  stmts(S), ['}'].

assign((L = R)) --> id(L), ['='], expr(R).

cond(if(B,T))   --> [if], ['('], expr(B), [')'], stmt(T).
cond(if(B,T,E)) --> [if], ['('], expr(B), [')'], stmt(T), 
		        [else], stmt(E).

loop(while(B,W))  --> [while], ['('], expr(B), [')'], stmt(W).

% _____________ EXPRESSION GRAMMAR ___________________
%
%        (add one rule for function calls)

expr(T)	 --> term(T1), term2(T1, T).

term2(T1,T)  --> [BOP], {operator(BOP)}, term(T2),
		 {T3 =.. [BOP,T1,T2]},
		 term2(T3,T).
term2(T,T)  --> [].

term(true) --> [true].
term(false) --> [false].
term(X) --> int(X).
term(T) --> id(T).
term(T) --> ['('], expr(T), [')'].
term(not(X)) --> ['!='], expr(X).
term(F) --> functioncall(F).

functioncall(call(N,L)) --> id(N), ['('], exprlist(L), [')'].


int(X)  --> [num(X)].
id(X)  --> [id(X)].

exprlist([])    --> [].
exprlist([E])   --> expr(E).
exprlist([E|L]) --> expr(E), [','], exprlist(L).

operator(+).
operator(-).
operator(/).
operator(*).
operator('&&').
operator('||').
operator(>=).
operator(=<).
operator('!=').
operator('==').
operator(=).
operator(>).
operator(<).


% ____________ LEXICAL ANALYZER  _______________________
%
%          (no changes needed here)

lex(Stream,Tokens)  :-  get_chars(Stream,L), tokenize(L,Tokens), !.

get_chars(Str,L) :-  get_code(Str,C), get_chars(Str,C,L).

get_chars(_,35, [35]) :- !.	% 35 = #
get_chars(Str,C,  [C|L1]) :- get_chars(Str,L1).

tokenize([], []).
tokenize([C|L], L3)	:- white(C), skip_whites(L,L2), tokenize(L2,L3).
tokenize([C|L], [X|L3]) :- alpha(C), identifier(X,[C|L],L2), tokenize(L2,L3).
tokenize([C|L], [X|L3]) :- d09(C), digits(X,[C|L],L2), tokenize(L2,L3).
tokenize(L, [X|L3])     :- special(X,L,L2), tokenize(L2,L3).

skip_whites([], []).
skip_whites([C|L], L2) :- (white(C) -> skip_whites(L,L2); L2 = [C|L]).

white(9).  % tab
white(10). % newline
white(32). % blank

special('==',[61,61|L],L).
special(=<,[61,60|L],L).
special(>=,[62,61|L],L).
special('!=',[33,61|L],L).
special('&&', [38,38|L],L).
special('||', [124,124|L],L).
special(>,[62|L],L).
special(=,[61|L],L).
special(<,[60|L],L).
special('{',[123|L],L).
special('}',[125|L],L).
special('(',[40|L],L).
special(')',[41|L],L).
special(;,[59|L],L).
special(',',[44|L],L).
special(*,[42|L],L).
special(+,[43|L],L).
special(-,[45|L],L).
special(/,[47|L],L).
special('#', [35|L], L).

identifier(X) --> ident(L), {name(N,L), (keyword(N) -> X=N; X=id(N))}.

ident([X|L]) --> letter(X), legits(L).
ident([X])   --> letter(X).

legits([X|W]) --> legit(X), legits(W).
legits([X])   --> legit(X).

legit(X) --> letter(X) ; digit(X).

letter(X) --> [X],  {alpha(X)}.

alpha(X) :-  X > 64,  X < 91.
alpha(X) :-  X > 96,  X < 123.

keyword(true).
keyword(false).
keyword(if).
keyword(else).
keyword(while).
keyword(int).
keyword(double).
keyword(real).
keyword(boolean).
keyword(exit).
keyword(return).

digits(num(N)) --> digs(L), {name(N,L)}.

digs([X|L]) --> digit(X), digs(L).
digs([X]) --> digit(X).

digit(X) -->  [X],  {d09(X)}.

d09(X) :- X > 47,  X < 58.


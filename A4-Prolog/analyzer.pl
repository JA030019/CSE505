go :- write('Tiny PL Call Graph Analyzer. Commands are:'), nl,
      write('   callers(f, L).'), nl,
      write('   undefined(L).'), nl,
      write('   is_recursive(f).'), nl,
      write('   all_calls(f, L).'), nl,
      nl.

calls(f,[g]).
calls(g,[h,k]).
calls(h,[f]).
calls(k,[t]).
calls(p,[k,s,f,r,s]).
calls(q,[]).
calls(r,[un1]).
calls(s,[s]).
calls(t,[un2]).

% F directly call A
directcall(F, A) :- calls(F, L), member(A, L).

% Write callers(F, L) here
callers(F, L) :- setof(Y, F^directcall(Y, F), L).

% Write undefined(L) here
undefined(L) :- setof(Y, X^directcall(X, Y), L1), 
			    callers(F, L2),
			    subtract(L1, L2, L).


% helper
helper(F, Y, L) :- calls(F, L1), member(Y, L1).
helper(F, Y, L) :- calls(F, L1), member(X, L1), \+member(X, L), helper(X, Y, [X|L]).

% Write is_recursive(F) here
is_recursive(F) :- recursive(F).
recursive(F) :- helper(F, F, []), !.


% Write all_calls(F, L) here
all_calls(F, L) :- setof(Y, F^helper(F, Y, []), L).
































:- dynamic calls/2.
:- dynamic fun/2.

:- include(analyzer).
:- include(grammar).

load(File) :-
	retractall(fun/2),
	open(File,read,Stream),
	lex(Stream,Tokens),
	program(Tokens,[]),
	close(Stream),
	!,
	retractall(calls/2),
	setof(fun(F,Body), fun(F,Body),Funcs),
	printlist(Funcs), nl,
	find_all_calls(Funcs,Calls),
	printlist(Calls),
	!.

% _______ FIND ALL CALLS IN A TINY PL PROGRAM  ______


find_all_calls([],[]).
find_all_calls([fun(F,B)|T],[calls(F,L)|T2]) :-
	find_calls(B,[],L),
	assertz(calls(F,L)),
	find_all_calls(T,T2).

find_calls(X,L,L):-
	atomic(X).
find_calls(call(F,Args),L, L3) :-
	look_inside_args(Args,L,L2),
	union([F],L2,L3).
find_calls(T,L,L2) :-
	T =..[_|Args],
	look_inside_args(Args,L,L2).

look_inside_args([],L,L).
look_inside_args([H|T],L,L3) :-
	find_calls(H,L,L2),
	look_inside_args(T,L2,L3).


show_calls :-
	setof(calls(F,Body), calls(F,Body),Calls),
	printlist(Calls),
        nl.

printlist([]).
printlist([H|T]) :-
        write(H), nl,
        printlist(T).




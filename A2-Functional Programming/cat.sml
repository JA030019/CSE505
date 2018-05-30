open String;

datatype 'a tree = leaf of 'a |
		        node of 'a tree * 'a tree;

fun cat(leaf(s)) = s
  | cat(node(t1,t2)) = cat(t1) ^ " " ^ cat(t2);


val testcase1 = leaf("a");

val testcase2 = node(leaf("a"), leaf("b"));

val testcase3 = node(testcase2, testcase2);


(* Place the code for cat_tail here *)

fun cat_tail([], acc) = acc
	|cat_tail(leaf(a)::b, "") = cat_tail(b, a)
	|cat_tail(leaf(a)::b, acc) = cat_tail(b, acc^" "^a)
	|cat_tail(node(a, b)::c, acc) = cat_tail(a::b::c, acc);

(* Place the code for cat2 here *)

fun cat2(a) = cat_tail([a], "");

(* Then run the three test cases as shown below *)


cat2(testcase1);

cat2(testcase2);

cat2(testcase3);



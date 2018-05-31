Control.Print.printDepth := 10;

datatype 'a ntree = leaf of 'a | node of 'a ntree list;


val t1 = node([node([leaf(1),leaf(2),leaf(3)]), 
	       node([leaf(7),leaf(8)])
              ]);

val t2 = node([node([leaf(10),leaf(20),leaf(30)]), 
	       node([leaf(70),leaf(80)])
             ]);

val t3 = node([node([leaf(100),leaf(200),leaf(300)]), 
	       node([leaf(700),leaf(800)])
             ]);

val tree = node([t1, t2, t3]);


val minint  =  let val SOME(x) = Int.minInt in x end;

val maxint  =  let val SOME(x) = Int.maxInt in x end;
　
fun reduce(f,b,[]) = b
  | reduce(f,b,x::t) = f(x,reduce(f,b,t));


fun min(leaf(x)) = x
  | min(node(l)) = 
		let fun m(a, b) = if max(a) < b then max(a) else b
		 in reduce(m, maxint, l)
		end
and  
     max(leaf(x)) = x
   | max(node(l)) = 
		let fun m(a, b) = if min(a)<b then b else min(a)
		 in reduce(m, minint, l)
		end;



val answer1 = min(tree);

val answer2 = max(tree);

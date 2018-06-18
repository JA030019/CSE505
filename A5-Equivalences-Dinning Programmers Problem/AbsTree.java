
  class AbsTreeDriver {
	  public static void main(String[] args) {
		  
		  Tree tr  = new Tree(100);
			tr.insert(50);
			tr.insert(150);
		      tr.insert(25);
			tr.insert(75);
			tr.insert(50);
			tr.insert(150);
			tr.insert(125);
			tr.insert(175);
			tr.insert(250);
			tr.insert(275);
			tr.insert(225);
			tr.insert(75);
			tr.insert(50);
			tr.insert(75);
			tr.insert(50);
			tr.insert(75);
			
			System.out.println("Tree values are:");
			tr.print();
			System.out.println();
		  
			DupTree dtr = new DupTree(100);
			dtr.insert(50);
			dtr.insert(150);
			dtr.insert(25);
			dtr.insert(75);
			dtr.insert(50);
			dtr.insert(150);
			dtr.insert(505);
			dtr.insert(125);
			dtr.insert(175);
			dtr.insert(250);
			dtr.insert(275);
			dtr.insert(225);
			dtr.insert(75);
			dtr.insert(50);
			dtr.insert(75);
			dtr.insert(50);
			dtr.insert(75);
			
			System.out.println("DupTree values are:");
			tr.print();
			System.out.println();
		  
		}
  }
  
   abstract class AbsTree {
	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public void insert(int n) {
		if (value == n)
			this.count_duplicates();
		else if (value < n)
			if (right == null)
				right = this.add_node(n);
			else
				right.insert(n);
		else if (left == null)
			left = this.add_node(n);
		else
			left.insert(n);
	}
	
	public void print() {
		if (left != null)
			left.print();
		this.print_node();
		if (right != null)
			right.print();
	}

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
	
	protected abstract AbsTree add_node(int n);
	protected abstract void count_duplicates();
	protected abstract void print_node();

}

class Tree extends AbsTree {
	public Tree(int n) {
		super(n);
	}
	protected AbsTree add_node(int n) {
		return new Tree(n);
	}
	protected void count_duplicates() {
		;
	}
	protected void print_node() {
		System.out.print(value + " ");
	}
}

class DupTree extends AbsTree {
	public DupTree(int n) {
		super(n);
		count = 1;
	};
	protected AbsTree add_node(int n) {
		return new DupTree(n);
	}
	protected void count_duplicates() {
		count++;
	}
	protected void print_node() {
		System.out.print(value + ":" + count + "  ");
	}
	protected int count;
}


class AbsTreeDriver2{
	public static void main(String[] args) {
		BTreeInterface tr = new Tree2(100);
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

		BTreeInterface dtr = new DupTree2(100);
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
		dtr.print();
		System.out.println();
	}
}

public class AbsTree2 implements BTreeInterface{
	BTreeInterface this2;
	
	public AbsTree2(BTreeInterface p) {
		this2 = p;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(int n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void count_duplicates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print_node() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tree2 add_node(int n) {
		// TODO Auto-generated method stub
		return new Tree2(n);
	}

	@Override
	public DupTree2 add_node2(int n) {
		// TODO Auto-generated method stub
		return new DupTree2(n);
	}
	
}

class Tree2 implements BTreeInterface{
	
	private int value;
	private Tree2 left;
	private Tree2 right;
	AbsTree2 super2;
	
	public Tree2(int n) {
		value = n;
		left = null;
		right = null;
		super2 = new AbsTree2(this);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		if (left != null)
			left.print();
		this.print_node();
		if (right != null)
			right.print();
	}

	@Override
	public void insert(int n) {
		// TODO Auto-generated method stub
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

	@Override
	public void count_duplicates() {
		// TODO Auto-generated method stub
		;
	}

	@Override
	public void print_node() {
		// TODO Auto-generated method stub
		System.out.print(value + " ");
	}

	@Override
	public Tree2 add_node(int n) {
		// TODO Auto-generated method stub
		return super2.add_node(n);
	}

	@Override
	public DupTree2 add_node2(int n) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class DupTree2 implements BTreeInterface{
	
	private int value;
	private int count;
	private DupTree2 left;
	private DupTree2 right;
	AbsTree2 super2;

	public DupTree2(int n) {
		value = n;
		left = null;
		right = null;
		count = 1;
		super2 = new AbsTree2(this);
	}

	@Override
	public void print() {
		if (left != null)
			left.print();
		this.print_node();
		if (right != null)
			right.print();
	}

	@Override
	public void insert(int n) {
		if (value == n)
			this.count_duplicates();
		else if (value < n)
			if (right == null)
				right = this.add_node2(n);
			else
				right.insert(n);
		else if (left == null)
			left = this.add_node2(n);
		else
			left.insert(n);
	}

	@Override
	public void count_duplicates() {
		// TODO Auto-generated method stub
		count++;
	}

	@Override
	public void print_node() {
		// TODO Auto-generated method stub
		System.out.print(value + ":" + count + "  ");
	}

	@Override
	public Tree2 add_node(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DupTree2 add_node2(int n) {
		// TODO Auto-generated method stub
		return super2.add_node2(n);
	}
	
}

interface BTreeInterface{
	
	void print();
	void insert(int n);
	void count_duplicates();
	void print_node();
	Tree2 add_node(int n);
	DupTree2 add_node2(int n);
	
}

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
 
class DiningProgrammers {

	 public static void main(String[] args)  {
		    
		 Forks fs = new Forks();
		 
	     Programmer p1 = new Programmer("Asif", fs);
	     Programmer p2 = new Programmer("Julia", fs);
	     Programmer p3 = new Programmer("Nick", fs);    
	     Programmer p4 = new Programmer("Stephen", fs);
	     Programmer p5 = new Programmer("Wei", fs);
	        
	     (new Thread(p1)).start();
	     (new Thread(p2)).start();
	     (new Thread(p3)).start();
	     (new Thread(p4)).start();
	     (new Thread(p5)).start();
	    }
	}

class Programmer extends State implements Runnable  {
	    Forks fs;
	    
	    Programmer(String name, Forks fs) {   
	    	this.name = name;
	    	this.count = 0;
	    	this.fs = fs;
	    }
	    
	    @Override
	    public void run() {
	        try {
	         set_state("C");                     // C = coding
	          for(int i=0; i<50; i++) {
	            fs.pickup(this);
	            set_state("H");                  // H = hungry
	            fs.pickup(this);
	            set_state("E");                  // E = eating
	            set_state("C"); 
	            fs.drop(this);
	          }
	        } 
	        catch(Exception e){}
	    }
	}

class Forks {
        int n = 5;
		Semaphore s1 = new Semaphore(5);
  		void pickup(Programmer p){	
  			try{
  				while(p.count < 2){
  					if((p.count < 2 && s1.availablePermits() >= 2) || (p.count == 1 && s1.availablePermits() == 1)){
  						s1.acquire();
  						p.count ++;
  					}
          
  				}
  			}
          	catch (Exception e){}
        
        }

  		void drop(Programmer p){	
  			try{
  				s1.release();
  				s1.release();
  				p.count = 0;
  			}
  			catch (Exception e){}
        }	


}  

	
class State {
	    String state;    // my state: C, H, or E
	    String name;     // my name
	    int count;       // how many forks I presently hold
	    void set_state(String s){
	    	state = s;
	    	try {
	    	if (s.equals("C")) {
	        	System.out.println (name + " is busy coding");
	            Thread.sleep((int)(500*Math.random()));
	        }
	    	if (s.equals("H")) {
	    		System.out.println (name + " got the first fork");
	            Thread.sleep((int)(200*Math.random()));
	    	}
	    	if (s.equals("E")) {
	            System.out.println (name +" got two forks and is eating"); 
	    		Thread.sleep((int)(100*Math.random())); 
	            System.out.println (name +" put down both forks");
	    	}
	    	}
	    	catch (Exception e) {}
	    }
	}


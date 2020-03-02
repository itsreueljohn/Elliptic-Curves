import java.math.BigInteger;
import java.io.File;
import java.util.Scanner;
import java.util.Random;

class EllipticCurves extends Point{


	static BigInteger p;
	static BigInteger a;
	static BigInteger b;
	final static BigInteger two = BigInteger.valueOf(2);

	public static void readCurve() throws Exception{

		File input = new File("Input");
		Scanner sc = new Scanner(input);

		p= new BigInteger(sc.next());
		a= new BigInteger(sc.next());
		b= new BigInteger(sc.next());
		sc.close();
	}

	
	public static void findY(BigInteger x){

		BigInteger n= x.pow(3).add((a.multiply(x)).add(b));

	}

	public static boolean isQuadraticResidue(BigInteger x){

		//rhs = x^(3) + ax + b
		BigInteger rhs= x.pow(3).add((a.multiply(x)).add(b));

		//checking if (rhs^(p-1/2))-1 % p == 0
		//return (squareAndMultiply(rhs,(p.subtract(BigInteger.ONE)).divide(BigInteger.valueOf(2))).subtract(BigInteger.ONE).mod(p).equals(BigInteger.ZERO));
		
		//Computing Legendre symbol and
		//checking if rhs^((p-1)/2) mod p == 1
		return rhs.modPow((p.subtract(BigInteger.ONE)).divide(BigInteger.valueOf(2)),p)==BigInteger.ONE;

	}

	static BigInteger modExp(BigInteger m, BigInteger e, BigInteger n){
		BigInteger r= e.mod(two).equals(BigInteger.ONE) ? m.mod(n):BigInteger.ONE;
		e= e.divide(two);

		while(e.compareTo(BigInteger.ZERO)>0){
			m=(m.multiply(m)).mod(n);

			if(e.mod(two).equals(BigInteger.ONE)) r= (r.multiply(m)).mod(n);
			e=e.divide(two);
		}
		return r;
	}

	//SHANKS algorithm
	public static Point findYGivenX(Point f){

		if(!isQuadraticResidue(f.x)) {
			System.out.println("No such point exists");
			System.exit(0);
		}

		BigInteger s=BigInteger.ZERO,t=p.subtract(BigInteger.ONE);
		BigInteger u =random(p.subtract(BigInteger.ONE));


		while(t.mod(two).equals(BigInteger.ZERO)){
			t=t.divide(two);
			s=s.add(BigInteger.ONE);
		}


		BigInteger k=s;
		BigInteger z = modExp(u,t,p);
		BigInteger x = modExp(a,(t.add(BigInteger.ONE)).divide(BigInteger.valueOf(2)),p);
		BigInteger b = modExp(a,t,p);

		while(!b.equals(BigInteger.ONE.mod(p))){

			//The following few lines of code implement a^b^c mod p in the following steps
			// temp = Compute b^c mod n where n= Euler's totient(p) which is p-1 as we know p is prime
			// Compute a^temp mod p
			BigInteger m=BigInteger.ZERO;
			BigInteger exponentZ=BigInteger.valueOf(2).modPow(k.subtract(m.subtract(BigInteger.ONE)),p.subtract(BigInteger.ONE));// 2^(k-m-1) mod (p-1)
			BigInteger y=modExp(z,exponentZ,p);//z^exponentZ mod p

			z=y.modPow(BigInteger.valueOf(2),p);// z=y^2 mod p

			BigInteger b1=b.mod(p);
			BigInteger b2=z.mod(p);
			b=(b1.multiply(b2)).mod(p);//b = bz mod p

			BigInteger x1= x.mod(p);
			BigInteger x2= y.mod(p);
			x=(x1.multiply(x2)).mod(p); //x=xy mod p

			k=m;
			System.out.println(b);
		}

		f.y=x;
		return f;
	}

	public static BigInteger random(BigInteger n) {
    		BigInteger result = new BigInteger(n.bitLength(), new Random());
    		while( result.compareTo(n) >= 0 ) {
			System.out.println("herh");
        		result = new BigInteger(n.bitLength(), new Random());
    		}
    		return result;
	}

	public static void main(String[]args) throws Exception{

		Point s = new Point();
		Point t = new Point();

		readCurve();
		System.out.println("P = "+p.toString());
		System.out.println("A = "+a.toString());
		System.out.println("B = "+b.toString());


		Scanner choiceScanner = new Scanner(System.in);
		System.out.println("Enter 1) to add the points, 2) to negate a point 3) to subtract the two points 4) to perform point multiplication 5) find a possible Y given X and any other value to exit");
		int choice=choiceScanner.nextInt();

		Point f = new Point();
		switch(choice){

			case 1:
				s.readPoint();
				t.readPoint();
				f=s.add(t,a);
				break;
			case 2:
				s.readPoint();
				f=s.negate();
				break;
			case 3:
				s.readPoint();
				t.readPoint();
				f=s.subtract(t,a);
				break;
			case 4:
				s.readPoint();
				System.out.print("Enter k: ");
				int k=choiceScanner.nextInt();
				f= s.multiply(BigInteger.valueOf(k),a);
				break;
			//PLEASE NOTE THAT THIS FUNCTION DOES NOT WORK
			//I HAVE DEBUGGED FOR TOO LONGG IT'S NOT WORKING :((((
			case 5:
				System.out.println("IMPORTANT: Enter the X value and any garbage Y value.The Y value will be ignored");
				s.readPoint();
				f=findYGivenX(s);
				break;
			default:
				System.out.println("Program terminating.....");
				System.exit(0);
		}

				System.out.println("ANSWER:");
				f.printPoint();

	}
}

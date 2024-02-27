//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
//
// Class          : vendingMachine.Model
//
// Author         : Richard E. Pattis
//                  Computer Science Department
//                  Carnegie Mellon University
//                  5000 Forbes Avenue
//                  Pittsburgh, PA 15213-3891
//                  e-mail: pattis@cs.cmu.edu
//
// Maintainer     : Author
//
//
// Description:
//
//   The Model for the VendingMachine package implements the guts of the
// vending machine: it responds to presses of buttons created by the
// Conroller (deposit, cancel, buy), and tells the View when it needs
// to update its display (calling the update in view, which calls the
// accessor methods in this classes)
// 
//   Note that "no access modifier" means that the method is package
// friendly: this means the member is public to all other classes in
// the calculator package, but private elsewhere.
//
// Future Plans   : More Comments
//                  Increase price as stock goes down
//                  Decrease price if being outsold by competition
//                  Allow option to purchase even if full change cannot 
//                    be returned (purchaser pays a premium to quench thirst)
//                  Allow user to enter 2 x money and gamble: 1/2 time
//                    all money returned with product; 1/2 time no money and
//                    no product returned
//
// Program History:
//   9/20/01: R. Pattis - Operational for 15-100
//   2/10/02: R. Pattis - Fixed Bug in change-making method
//
//
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////


package vendingMachine;


import java.lang.Math;
import java.util.Scanner;


public class Model {
	//Define fields (all instance variables)

	private View view;         // Model must tell View when to update itself

	private int    cokeLeft;
	private int    pepsiLeft;

	private int    quartersLeft, dimesLeft, nickelsLeft;

	//I defined about 10 more fields
	private int cokePrice, pepsiPrice;
	private int amount;
	private String message;
	private int[] cnt = new int[3];
	//Define constructor
	public Model(){
		System.out.println("Vending Machine Initialization");
		Scanner scanner = new Scanner(System.in);
		System.out.print("  Enter quarters   to start(10): ");
		quartersLeft = scanner.nextInt();
		System.out.print("  Enter dimes      to start(10): ");
		dimesLeft = scanner.nextInt();
		System.out.print("  Enter nickels    to start(10): ");
		nickelsLeft = scanner.nextInt();
		System.out.print("  Enter pepsi      to start(5): ");
		pepsiLeft = scanner.nextInt();
		System.out.print("  Enter coke       to start(5): ");
		cokeLeft = scanner.nextInt();
		System.out.print("  Enter pepsi cost in cents(85): ");
		pepsiPrice = scanner.nextInt();
		System.out.print("  Enter coke  cost in cents(95): ");
		cokePrice = scanner.nextInt();
	}

	//Refer to the view (used to call update after each button press)
	public void addView(View v)
	{view = v;}

	//Define required methods: mutators (setters) and accessors (getters)
	public String getDeposited(){
		return "$" + String.format("%.2f", (double)amount/100);
	}

	public String getMessage(){
		return message;
	}

	public int getCokeLeft(){
		return cokeLeft;
	}

	public int getPepsiLeft(){
		return pepsiLeft;
	}

	public String getCokePrice(){
		return "$" + String.format("%.2f", (double)cokePrice/100);
	}

	public String getPepsiPrice(){
		return "$" + String.format("%.2f", (double)pepsiPrice/100);
	}
	//Represent "interesting" state of vending machine
	public String toString()
	{
		return "Vending Machine State: \n" +
				"  Coke     Left      = " + cokeLeft     + "\n" +
				"  Pepsi    Left      = " + pepsiLeft    + "\n" +
				"  Quarters Left      = " + quartersLeft + "\n" +
				"  Dimes    Left      = " + dimesLeft    + "\n" +
				"  Nickels  Left      = " + nickelsLeft  + "\n";
		//Display any other instance variables that you declare too
	}

	//Define helper methods
	public void cancel(){
		message = "Cancelled: ";
		if(cnt[0]>0||cnt[1]>0||cnt[2]>0){
			message += "returning = ";
		}
		if(cnt[0]>0){
			message += cnt[0] + " quarter";
			if(cnt[0]>1){
				message += "s";
			}
			message += " ";
			quartersLeft -= cnt[0];
		}
		if(cnt[1]>0){
			message += cnt[1] + " dime";
			if(cnt[1]>1){
				message += "s";
			}
			message += " ";
			dimesLeft -= cnt[1];
		}
		if(cnt[2]>0){
			message += cnt[2] + " nickel";
			if(cnt[2]>1){
				message += "s";
			}
			message += " ";
			nickelsLeft -= cnt[2];
		}
		amount = 0;
		view.update();
	}

	public void deposit(int amount){
		this.amount += amount;
		if(amount == 25){
			quartersLeft++;
			cnt[0]++;
		}else if(amount == 10){
			dimesLeft++;
			cnt[1]++;
		}else{
			nickelsLeft++;
			cnt[2]++;
		}
		message = amount + " cents desposited";
		view.update();
	}

	public void buy(String product){
		if(product.equals("Pepsi")){
			if(amount < pepsiPrice){
				message = "Deposit more money";
			}else{
				message = change(amount - pepsiPrice,0);
			}

		}else{
			if(amount < cokePrice){
				message = "Deposit more money";
			}else{
				message = change(amount - cokePrice,1);
			}
		}
		view.update();
	}

	public String change(int price, int x){
		int ogQ = quartersLeft, ogD = dimesLeft,ogN = nickelsLeft,ogP = amount - pepsiPrice;
		String ans="";
		if(x == 0) ans += "Pepsi bought: ";
		else ans += "Coke bought: ";
		if(price == 0){
			return "(no change)";
		}
		ans += "change = ";
		int q=0,d=0,n=0;
		while(quartersLeft>0&&price>=25){
			price-=25;
			quartersLeft--;
			q++;
		}
		while(dimesLeft>0&&price>=10){
			price-=10;
			dimesLeft--;
			d++;
		}
		while(nickelsLeft>0&&price>=5){
			price-=5;
			nickelsLeft--;
			n++;
		}
		if(q>0){
			ans += q + " quarter";
			if(q>1){
				ans += "s";
			}
			ans += " ";
		}
		if(d>0){
			ans += d + " dime";
			if(d>1){
				ans += "s";
			}
			ans += " ";
		}
		if(n>0){
			ans += n + " nickel";
			if(n>1){
				ans += "s";
			}
		}
		if(price != 0){
			quartersLeft = ogQ;
			nickelsLeft = ogN;
			dimesLeft = ogD;
			price = ogP;
			ans = "";
			d=0;
			n=0;
			while(dimesLeft>0&&price>=10){
				price-=10;
				dimesLeft--;
				d++;
			}
			while(nickelsLeft>0&&price>=5){
				price-=5;
				nickelsLeft--;
				n++;
			}
			if(price != 0){
				ans = "Cannot make change: ";
				nickelsLeft = ogN;
				dimesLeft = ogD;
				quartersLeft = ogQ;
			}else{
				if(d>0){
					ans += d + " dime";
					if(d>1){
						ans += "s";
					}
					ans += " ";
				}
				if(n>0){
					ans += n + " nickel";
					if(n>1){
						ans += "s";
					}
				}
				if(x==0) pepsiLeft --;
				else cokeLeft --;
				for(int i=0;i<3;i++) cnt[i]=0;
				amount = 0;
			}
		}
		else{
			if(x==0) pepsiLeft --;
			else cokeLeft --;
			for(int i=0;i<3;i++) cnt[i]=0;
			amount = 0;
		}
		return ans;
	}
}
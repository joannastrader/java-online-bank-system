package OnlineBankSystemsPractice;

import java.util.Scanner;

/**
 * Models a simple bank account with an ID, customer name, and balance.
 * Provides deposit, withdraw, display, and transfer behaviors.
 * Also tracks total number of accounts and total balance across all accounts.
 * @author Joanna Strader
 */
public class Account {
	//part 1: data
	private String ID;
	private String name;
	private double balance;
	public static final double RATE = 0.05;
	
	private static int numAccounts = 0;
	private static double totalBalance = 0;
	
	//part 2: constructors
	/**
     * Creates an account with the given ID, name, and starting balance.
     * Updates the bank-wide statistics (number of accounts and total balance).
     * @param ID unique account ID (not changeable after initialized)
     * @param name customer name
     * @param balance starting balance
     */
	public Account(String ID, String name, double balance)
	{
		this.ID = ID;
		this.name = name;
		this.balance = balance;
		numAccounts++;
		totalBalance+= balance;
	}
	/**
	 * Returns this account's unique ID.
	 * @return the account ID
	 */
	public String getID()
	{
		return ID;
	}
	/**
	 * Returns the current balance of this account.
	 * @return current account balance
	 */
	public double getBalance()
	{
		return balance;
	}
	//part 3: methods
	/**
     * Returns the number of accounts created.
     * @return total number of accounts
     */
	public static int getNumAccounts()
	{
		return numAccounts;
	}
	/**
	 * Returns the account holder's name.
	 * @return the customer's name
	 */
	public String getName()
	{
		return name;
	}
	/**
     * Returns the total balance across all accounts.
     * @return total balance across all accounts
     */
	public static double getTotalBalance()
	{
		return totalBalance;
	}
	/**
	 * Searches the accounts array for a given ID.
	 * @param accounts array of Account objects
	 * @param accountCount number of active accounts in the array
	 * @param id the ID to search for
	 * @return index of the account if found; -1 if not found
	 */
	public static int findAccountIndex(Account[] accounts, int accountCount, String id)
	{
		for(int i=0; i < accountCount; i++)
		{
			if (accounts[i].getID().equals(id)) {
				return i;
			}
		}
		return -1; //not found
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	/**
     * Deposits a positive amount into this account and updates total bank balance.
     * @param money amount to deposit (must be > 0)
     */
	public void deposit(double money)
	{
		if (money <= 0) return;
		balance += money;
		totalBalance += money;
		
	}
	/**
     * Attempts to withdraw the given amount.
     * @param money amount to withdraw (must be > 0)
     * @return true if successful; false if insufficient funds or invalid amount
     */
	public boolean withdraw(double money)
	{
		if(balance < money)
		{
			return false;
		}
		if (money <= 0) return false;
		
		balance -= money;
		totalBalance -= money;
		return true;
	}
	 /**
     * Transfers money from this account to another account.
     * Precondition: other must not be null.
     * @param other destination account
     * @param amount amount to transfer (must be > 0)
     * @return true if successful; false otherwise
     */
	public boolean transferTo(Account other, double amount)
	{
		if(amount <= 0) return false;
		if (!withdraw(amount)) return false;
		other.deposit(amount);
		return true;
	}
	/**
     * Prints this account’s information.
     */
	public void display()
	{
		System.out.println("ID: " + this.ID + " Name: " + name + " Balance: " + balance);
	}
	/**
	 * Prints the bank system menu options.
	 */
	public static void printMenu()
	{
		System.out.println("**Welcome to ECU Bank!**");
		System.out.println("1. Create a new account");
		System.out.println("2. Withdraw");
		System.out.println("3. Deposit");
		System.out.println("4. Transfer");
		System.out.println("5. Display Balance");
		System.out.println("0. Quit");
	}
	/**
	 * Runs the Online Bank System program.
	 * Allows staff to create accounts, withdraw, deposit, transfer, 
	 * and display balances through a menu-driven interface.
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		Account[] accounts = new Account[100]; //bank can hold 100 accounts
		int accountCount = 0; //tracks how many accounts are actually created
		
		boolean running = true;
		
		while (running)
		{
			printMenu();
			int choice = input.nextInt();
			switch (choice)
			{
			case 1:
				//create account
				if(accountCount >= accounts.length)
				{
					System.out.println("Bank is full. Cannot create more accounts");
					break;
				}
				System.out.print("Enter ID: ");
				String newId = input.next();
				System.out.print("Enter name: ");
				String newName = input.next();
				System.out.print("Enter starting balance: ");
				double startBal = input.nextDouble();
				
				accounts[accountCount] = new Account(newId, newName, startBal);
				accountCount++;
				
				System.out.println("Account created.");
				break;
			case 2: 
				//withdraw
				System.out.print("Enter ID: ");
				String wId = input.next();
				int wIndex = findAccountIndex(accounts, accountCount, wId);
				if(wIndex == -1)
				{
					System.out.println("Account not found");
					break;
				}
				System.out.println("Enter amount to withdraw");
				double wAmt = input.nextDouble();
				boolean okW = accounts[wIndex].withdraw(wAmt);
				if (!okW)
				{
					System.out.println("Not enough money. Withdrawal failed.");
					
				}
				else
				{
					System.out.println("New balance: " + accounts[wIndex].getBalance());
				}
				break;
			case 3: 
				//deposit
				System.out.print("Enter ID: ");
				String dId = input.next();
				int dIndex = findAccountIndex(accounts, accountCount, dId);
				if(dIndex == -1)
				{
					System.out.println("Account not found.");
					break;
				}
				System.out.println("Enter deposit amount: ");
				double dAmt = input.nextDouble();
				accounts[dIndex].deposit(dAmt);
				System.out.println("New balance: " + accounts[dIndex].getBalance());
				
				break;
			case 4: 
				//transfer
				System.out.print("Enter FROM ID: ");
				String fromId = input.next();
				System.out.print("Enter TO ID: ");
				String toId = input.next();
				int fromIndex = findAccountIndex(accounts, accountCount, fromId);
				int toIndex = findAccountIndex(accounts, accountCount, toId);
				if(fromIndex == -1 || toIndex == -1)
				{
					System.out.println("From or To account not found.");
					break;
				}
				System.out.print("Enter amount to transfer:");
				double tAmt = input.nextDouble();
				boolean okT = accounts[fromIndex].transferTo(accounts[toIndex], tAmt);
				if(!okT)
				{
					System.out.println("Transfer failed (check amount/funds).");
				}
				else
				{
					System.out.println("Transfer complete.");
					System.out.println("FROM new balance: "+ accounts[fromIndex].getBalance());
					System.out.println("TO new balance: " + accounts[toIndex].getBalance());
				}
				break;
			case 5: 
				//display balance
				System.out.print("Enter ID: ");
				String bId = input.next();
				int bIndex = findAccountIndex(accounts, accountCount, bId);
				if (bIndex == -1)
				{
					System.out.println("Account not found.");					
				}
				else
				{
					System.out.println("Balance: " + accounts[bIndex].getBalance());
				}
				
				break;
			case 0: 
				//quit
				running = false;
				break;
				
			default:
				System.out.println("Invalid choice");
			}
		}
		
	}

}

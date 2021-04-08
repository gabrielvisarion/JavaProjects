package bankaccountapp;

import java.util.LinkedList;
import java.util.List;

public class BankAccountApp {

	public static void main(String[] args) {
		List<Account> accounts = new LinkedList<Account>();

		
		/*Checking chkacc1 = new Checking("Tom Wilson", "321456879", 1500);
		
		
		Savings savacc1 = new Savings("Rich Low", "456657897", 2500);
		
		savacc1.compound();
		savacc1.showInfo();
		
		System.out.println("*****************");
		
		chkacc1.showInfo();
		*/
		
		
		

		//Read a CSV file then create new accounts based on the data
		String file = "D:\\iordacheg\\eclipse-workspace\\JavaProject\\src\\bankaccountapp\\NewBankAccounts.csv";		
		List<String[]> newAccountHolders = utilities.CSV.read(file);
		for(String[] accountHolder : newAccountHolders) {
			String name = accountHolder[0];
			String sSN = accountHolder[1];
			String accountType = accountHolder[2];
			double initDeposit = Double.parseDouble(accountHolder[3]);
			
			if(accountType.equals("Savings")) {
				accounts.add(new Savings(name, sSN, initDeposit));
			}else if (accountType.equals("Checking")) {
				accounts.add(new Checking(name, sSN, initDeposit));
			}else {
				System.out.println("ERROR READING ACCOUNT TYPE");
			}
		}
		
		for (Account acc : accounts) {
			System.out.println("*************\n");
			acc.showInfo();
		}
		
	}

}

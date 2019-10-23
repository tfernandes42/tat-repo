package nameVariation;

import java.util.Scanner;

public class Names {
	
	@SuppressWarnings("resource")
	public static void main(String [] args) {
		
		char firstInitial= 0;
		char middleInitial = 0;
		String firstName = null;
		String middleName = null;
		String lastName = null;
		
		Scanner scan = new Scanner(System.in); 
		System.out.print("enter name: ");
		String name = scan.nextLine(); 
		
		System.out.print("male or female: ");
		String gender = scan.nextLine();
			
		String[] fullName = name.split(" ");
		
		//if statement, if the person is male...
		if (gender.equals("Male") || gender.equals("male") || gender.equals("M") || gender.equals("m") ) {
			
		
		// if only first name was given...
		 if(name.indexOf(" ")==-1){
			firstName= fullName[0].substring(0, fullName[0].length());
			System.out.println(firstName);
		    System.exit(0);
		}
		
			// if only second name was given...
				else if((name.indexOf(" ") == name.lastIndexOf(" "))){
			    firstName= fullName[0].substring(0, fullName[0].length());
				middleName= fullName[1].substring(0, fullName[1].length());
				System.out.println(firstName + " " + middleName);
			    System.exit(0);
			}
		 
			//	int nameLength = fullName.length;
			
				else { 
				firstInitial = fullName[0].charAt(0);
				middleInitial= fullName[1].charAt(0);
				firstName= fullName[0].substring(0, fullName[0].length());
				middleName= fullName[1].substring(0, fullName[1].length());
				lastName= fullName[2].substring(0, fullName[2].length());
			
				System.out.println(firstName + " " + middleName + " " + lastName);
				System.out.println(firstName + " " + middleInitial + " " + lastName);
				System.out.println(firstInitial + " " + middleName + " " + lastName);
				//System.out.println(firstInitial + " " + middleInitial + " " + lastName);
				}
			
		}
	
	//if women... 
		else if (gender.equals("Female") || gender.equals("female") || gender.equals("F") || gender.equals("f") ) {

			// if only first name was given...
			 if(name.indexOf(" ")==-1){
				firstName= fullName[0].substring(0, fullName[0].length());
				System.out.println(firstName);
			    System.exit(0);
			}
			
				// if only second name was given...
					else if((name.indexOf(" ") == name.lastIndexOf(" "))){
				    firstName= fullName[0].substring(0, fullName[0].length());
					middleName= fullName[1].substring(0, fullName[1].length());
					System.out.println(firstName + " " + middleName);
				    System.exit(0);
				}
			 
			 		//Married women... add 4th/5th name operators 
			 
			 
			 		//if women and never married.. 
					else { 
					firstInitial = fullName[0].charAt(0);
					middleInitial= fullName[1].charAt(0);
					firstName= fullName[0].substring(0, fullName[0].length());
					middleName= fullName[1].substring(0, fullName[1].length());
					lastName= fullName[2].substring(0, fullName[2].length());
				
					System.out.println(firstName + " " + middleName + " " + lastName);
					System.out.println(firstName + " " + middleInitial + " " + lastName);
					System.out.println(firstInitial + " " + middleName + " " + lastName);
					//System.out.println(firstInitial + " " + middleInitial + " " + lastName);
					}
		
		}
	
	}
}

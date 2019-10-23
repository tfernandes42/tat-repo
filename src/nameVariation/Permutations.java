package nameVariation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Permutations {
	public static void main(String [] args) {
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/Users/tfernandes/Desktop/fsObits/fsobitFemale2.dummy.xml");

		 try {

			 Document document = (Document) builder.build(xmlFile);
			 
			 //root element
				Element rootNode = document.getRootElement();
				
				
				
				@SuppressWarnings("rawtypes")
				List list = rootNode.getChildren("person");
				
				//       																						|
				String [] all = { "Wife", "Husband", "Spouse", "Ex-Wife","Ex-Husband","Mother", "Father", "Parent","Sister", "Brother", "Sister-In-Law", 
						"Brother-In-Law", "Aunt", "Uncle", "Cousin", "Mother-In-Law", "Stepfather", "Sibling", "Stepmother","Father-In-Law", "Stepbrother",
						"Stepsister", "Sibling-In-Law", "Parent-In-Law", "Stepsibling", "Fiancé", "Stepparent", "Domestic Partner"};
				
				String primaryBirthFirst = null;
				String primaryBirthMid = null;
				char primaryBirthMidIn = 0;
				String primaryBirthLast= null;
				String primaryLastName = null;
				boolean gender = false;
				

				for (int i = 0; i < list.size(); i++) {
					
					String birthFirst = null;
					String birthMid = null;
					String lastName = null;
					String birthLast = null;
					String nickName = null;
					String fullname = null;
					char birthFirstInitial = 0;
					char birthLastInitial = 0;
					char birthMidInitial = 0;
					boolean secondary = false;

				   Element node = (Element) list.get(i);				   
				   
				   String first = node.getChildText("person_PR_NAME_GN"); 
				   String last = node.getChildText("person_PR_NAME_SURN");
				   
				    if (first != null && first.contains("Or")) {
					   
					  String[] orBirthFirst = first.split(" Or ");

					  first = orBirthFirst[0].substring(0, orBirthFirst[0].length());
					  nickName = orBirthFirst[1].substring(0, orBirthFirst[1].length());
				   } 
				    
				     if (last != null && last.contains(" Or")) {
				    		int or = last.indexOf(" Or");
						last = last.substring(0, or);
				    }
				     
				     //if the Or is the first thing in the SURName field
				     else if (last != null && last.contains("Or")) {
				    	 	last = null;
				     }
		
				     if (last == null )
				    	 fullname = first;				    				   
				   
				     else 
					   fullname = first + " " + last;				    
				   				   
					   String[] name = fullname.split(" ");
				   
					//if name has one piece
				   if(name.length == 1) 
					   birthFirst = name[0].substring(0, name[0].length());
				   
					//if name has 2 pieces 
				   if(last == null && name.length == 2 && node.getChildText("person_PR_SEX_CODE").equals("Female")) {
					   //if there are two pieces and none are in the last name element
						   birthFirst = name[0].substring(0, name[0].length());
						   birthMid = name[1].substring(0, name[1].length());
						   birthMidInitial = name[1].charAt(0);						   
					   }
					   
					   else if (last != null && name.length == 2 && 
							   node.getChildText("person_PR_SEX_CODE").equals("Female")){
						   birthFirst = name[0].substring(0, name[0].length());
					   	   birthLast = name[1].substring(0, name[1].length());
					   }
				   
				   //if has two pieces and male...
				   if(last == null && name.length == 2 && !node.getChildText("person_PR_SEX_CODE").equals("Female")) {
					   //if there are two pieces and none are in the last name element
						   birthFirst = name[0].substring(0, name[0].length());
						   birthMid = name[1].substring(0, name[1].length());
					   }
				   
				   //if it is primary person print out name
				   else if (last != null && name.length == 2 && !node.getChildText("person_PR_SEX_CODE").equals("Female")){
					   birthFirst = name[0].substring(0, name[0].length());
				   	   lastName = name[1].substring(0, name[1].length());
				   	   
				   	   //get initial of birth first name
				   	   birthFirstInitial = name[0].charAt(0);
				   }
				
				//if PR_NAME_GN has three pieces (two spaces) and female
				   if(name.length == 3 && node.getChildText("person_PR_SEX_CODE").equals("Female")){

					   birthFirst = name[0].substring(0, name[0].length());
					   birthMid = name[1].substring(0, name[1].length());
					   birthLast = name[2].substring(0, name[2].length());
					  
					   //get initial of middle name
					   birthMidInitial = name[1].charAt(0);
					   //get initial of birth last name
					   //birthLastInitial = name[2].charAt(0);
				   }
				   
				   //if three pieces and male
					//if PR_NAME_GN has three pieces (two spaces) and not female
				    if(name.length == 3 && !node.getChildText("person_PR_SEX_CODE").equals("Female") ){

					   birthFirst = name[0].substring(0, name[0].length());
					   birthMid = name[1].substring(0, name[1].length());
					   lastName = name[2].substring(0, name[2].length());
					  
					   //get initial of first name
					   birthFirstInitial = name[0].charAt(0);
					   
					   //get initial of middle name
					   birthMidInitial = name[1].charAt(0);
				   }
				    
				//if PR_NAME_GN has three pieces (two spaces) and female
				    if(name.length == 4 && node.getChildText("person_PR_SEX_CODE").equals("Female")){

					   birthFirst = name[0].substring(0, name[0].length());
					   birthMid = name[1].substring(0, name[1].length());
					   birthLast = name[2].substring(0, name[2].length());
					   lastName = name[3].substring(0, name[3].length());
					   
					   //get initial of middle name
					   birthMidInitial = name[1].charAt(0);

					   //get initial of birth last name
					   birthLastInitial = name[2].charAt(0);							
				   }
				    				    

				    String personRelType = node.getChildText("person_REL_TYPE");
				    
					//save the specific name into a unique name to compare with secondary people to make sure there are no repeats
					if ( personRelType != null && node.getChildText("person_REL_TYPE").equals("Deceased") ) {
						primaryBirthFirst = birthFirst;
						primaryBirthMid = birthMid;
						primaryBirthMidIn = birthMidInitial;
						primaryLastName = lastName;
						primaryBirthLast = birthLast;						
						
						if (node.getChildText("person_PR_SEX_CODE").equals("Female")) {
							gender = true;
						}
					}   
					
					//save the specific name into a unique name if main person does not have personRelType element
					/*else if (  personRelType == null && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) {
						primaryBirthFirst = birthFirst;
						//primaryBirthMid = birthMid;
						//primaryBirthMidIn = birthMidInitial;
						primaryLastName = lastName;
						primaryBirthLast = birthLast;
						
						if (node.getChildText("person_PR_SEX_CODE").equals("Female")) {
							gender = true;
						}
					}   */
 
					   // if it is primary person...
					   if ( personRelType != null && node.getChildText("person_REL_TYPE").equals("Deceased") ) {
						  
						   if ( last == null) {
					    		System.out.println("Inefficient Information");
					    		System.exit(0);
					    		
					    }					   
						   //if primary person and female...
						   if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
					    			
							   System.out.print("&p_field_primary-0=alltext&p_params_primary-0=weight:1&p_text_primary-0=");
							   System.out.print("(");
							   
							   femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
									   lastName,  birthLastInitial,  birthMidInitial, primaryBirthFirst, 
					   					 primaryBirthLast, primaryLastName, personRelType, node, gender, nickName, primaryBirthMid, primaryBirthMidIn);
							   
							   System.out.print(")");
							   
						   }
						   
						   
						 //if primary person and not female...
						   else if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
					    		
							   System.out.print("&p_field_primary-0=alltext&p_params_primary-0=weight:1&p_text_primary-0=");
							   System.out.print("(");
							   
							   malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
					   					birthMidInitial, primaryBirthFirst, primaryLastName, primaryBirthMid, primaryBirthMidIn, personRelType, primaryBirthLast,
					   					node, gender, nickName);
							   
							   System.out.print(")");
						   }	
						   
						   System.out.print("&p_bool_second-1=OR&p_field_second-1=alltext&p_params_second-0=weight:1&p_text_second-1=");
						   System.out.print("(");
						   if (null != node.getChildText("person_PR_BIRTH_CITY_TOWN")) {
							System.out.print("\"" + node.getChildText("person_PR_BIRTH_CITY_TOWN") + "\"");
						   }
						   if (null != node.getChildText("person_PR_DEATH_CITY_TOWN") && 
								   !node.getChildText("person_PR_DEATH_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))) {
							   System.out.print(" OR ");
							   System.out.print("\"" + node.getChildText("person_PR_DEATH_CITY_TOWN") + "\"");
						   }
						   if (null != node.getChildText("person_PR_RES_CITY_TOWN") && 
								   !node.getChildText("person_PR_RES_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
								   && !node.getChildText("person_PR_RES_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN"))) {
							   System.out.print(" OR ");
							   System.out.print("\"" + node.getChildText("person_PR_RES_CITY_TOWN") + "\"");
						   }
						   if (null != node.getChildText("person_PR_BUR_CITY_TOWN") && 
								   !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
								   && !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN")) 
								   && !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_RES_CITY_TOWN"))) {
							   System.out.print(" OR ");							
							   System.out.print("\"" + node.getChildText("person_PR_BUR_CITY_TOWN") + "\"");
						   }
						   System.out.print(")");
						   
						   
					   }
						  	boolean check = false;
						  	if (personRelType != null && node.getChildText("person_REL_TYPE").equals("Deceased"))
						  		check = true;					
						  	
						  	if(check == true) {
						  		System.out.print("&p_field_second-0=alltext&p_params_second-0=weight:3&p_text_second-0=");
						  		System.out.print("(");
						  	}

						  	//Start of SECONDARY people
						   for (int a = 0; a < all.length; a++)	{

							   if ( personRelType != null && node.getChildText("person_REL_TYPE").equals(all[a])) {
								   
								   secondary = true;
							   
							 //if secondary person and female...
							   		if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {

							   			femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
							   					lastName,  birthLastInitial,  birthMidInitial, primaryBirthFirst, 
							   					 primaryBirthLast, primaryLastName, personRelType, node, gender, nickName,
							   					primaryBirthMid, primaryBirthMidIn);
							   			
										   if ( secondary == true && i != 1)
											   System.out.print(" OR ");

								   
							   		}
							   
							   		//if secondary person and not female...
							   		if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
							   			
							   			malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
							   					birthMidInitial,  primaryBirthFirst, primaryLastName, primaryBirthMid, primaryBirthMidIn, primaryBirthLast, 
							   					personRelType, node, gender, nickName);		
							   			
							   			if ( secondary == true && i != 1)
											   System.out.print(" OR ");

							   		}
							   }
						   							   						 				   
							   else if (personRelType == null && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals(all[a])) {

								   //if secondary person and female...
						   		if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
						   			
						   			femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
						   					lastName,  birthLastInitial,  birthMidInitial, 
						   					 primaryBirthFirst, primaryBirthLast, primaryLastName,
						   					 personRelType, node, gender, nickName, primaryBirthMid,
						   					 primaryBirthMidIn);			
						   			
						   			if ( secondary == true && i != 1)
										   System.out.print(" OR ");

						   		}
						   
						   		//if secondary person and not female...
						   		if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
						   			
						   			malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
						   					birthMidInitial, primaryBirthFirst, primaryLastName, primaryBirthMid, primaryBirthMidIn, primaryBirthLast,  
						   					personRelType, node, gender, nickName);	
						   			
						   			if ( secondary == true && i != 1)
										   System.out.print(" OR ");

						   		}
						   }	
								   secondary = true;							   
						 }
						  	if(i == list.size() - 1)
						  		System.out.print(")");
				}
				
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }		
	}
	
	public static void femalePermutation (String [] name, String last, String birthFirst, String birthMid, String birthLast,  
			String lastName, char birthLastInitial, char birthMidInitial,  String primaryBirthFirst, String primaryBirthLast, 
			String primaryLastName, String personRelType, Element node, boolean gender, String nickName, String primaryBirthMid,
			char primaryBirthMidIn) {

		   String s = Character.toString(birthMidInitial);
		  
		//if name has one piece
		   if(name.length == 1 ) {
			   if(gender == true) {
			   System.out.print("\"" +birthFirst + " " + primaryBirthLast+ "\"");
			   }
			   else 
				   System.out.print("\"" +birthFirst + " " + primaryLastName+ "\"");
		   }

		 //if there are two pieces and none are in the last name element
		   if(last == null && name.length == 2) {
			   if(gender == true) {
				   System.out.print("\"" +birthFirst + " "  + birthMid + " " + primaryBirthLast+ "\""+ " OR ");
				   System.out.print("\"" +birthFirst + " " + primaryBirthLast+ "\"");
				   
				   if(!s.equals(birthMid)) {
					   	   System.out.print(" OR ");
						   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + primaryBirthLast+ "\"");	
				   }
			   }
			   else if (gender == false) {
				    System.out.print("\"" +birthFirst + " "  + birthMid + " " + primaryLastName+ "\""+ " OR ");
			   		System.out.print("\"" +birthFirst + " "  + primaryLastName+ "\"");
			   		
					   if(!s.equals(birthMid)) {
					   	   System.out.print(" OR ");
						   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + primaryLastName+ "\"");   
					   }
			   	}
			   }
		   		  
/*		   else if (personRelType == null && node.getChildText("person_REL_TYPE").equals("Wife") || node.getChildText("person_REL_TYPE").equals("Spouse")
				   || node.getChildText("person_REL_TYPE").equals("Ex-Wife") || node.getChildText("person_REL_TYPE").equals("Parent") || 
				   node.getChildText("person_REL_TYPE").equals("Mother")) {
			   System.out.println("no last name: ");
			   if(last == null && name.length == 2) {
				   if(gender == true) {
					   System.out.println(birthFirst + " "  + birthMid + " " + primaryBirthLast);
					   System.out.println(birthFirst + " " + primaryBirthLast);
					   
					   if(!s.equals(birthMid))
							   System.out.println(birthFirst + " " + birthMidInitial + " " + primaryBirthLast);				   
				   }
				   else if (gender == false) {
					    System.out.println(birthFirst + " "  + birthMid + " " + primaryLastName);
				   		System.out.println(birthFirst + " "  + primaryLastName);
				   		
						   if(!s.equals(birthMid))
							   System.out.println(birthFirst + " " + birthMidInitial + " " + primaryLastName);   
				   	}
				   }
		   }*/
		   
			   if (last != null && name.length == 2 /* && birthFirst == null && birthLast == null */){

			   	System.out.print("\"" +birthFirst + " " + birthLast+ "\"");
			   
			   	
			   	if (null != personRelType && node.getChildText("person_REL_TYPE").equals("Deceased")) {
				   	System.out.print(" OR ");
				   	System.out.print("\"" +birthLast + " " + birthFirst+ "\"");
			   	}
			   	
			   	
			   	else if (null == personRelType  && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")) {
				   	System.out.print(" OR ");
			   		System.out.print("\"" +birthLast + " " + birthFirst+ "\"");
			   	}
			   		
			   	if(nickName != null) {
			   		System.out.print(" OR ");
			   		System.out.print("\"" +birthFirst + " " +  nickName + " " + birthLast+ "\"");
			   	}

		   }

			   //if PR_NAME_GN has three pieces (two spaces) and female
		   if(name.length == 3 ){
			   
			   System.out.print("\"" + birthFirst + " " + birthLast+ "\"" + " OR ");
			   
			   //if there are two parts in last name instead of two parts in first name		   
			  if (!birthFirst.equals(primaryBirthFirst) && !birthMid.equals(primaryBirthMid) && !birthLast.equals(primaryBirthLast)) {
			   if (last.contains(" ") )
				   System.out.print("\"" + birthFirst + " " + birthMid+ "\"" + " OR ");
			   else 
				   System.out.print("\"" + birthFirst + " " + birthMid + " " + birthLast+ "\"" + " OR ");
			   
			   if(!s.equals(birthMid) && birthMidInitial != primaryBirthMidIn ) {
			   		System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + birthLast+ "\"");
			   		
			   		if(nickName != null) {
						System.out.print(" OR ");
			   			System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + nickName + " " + birthLast+ "\"" + " OR ");
			   		}
				   }
			  }
			  
			  else if (personRelType != null && node.getChildText("person_REL_TYPE").equals("Deceased")) {
				   if (last.contains(" ") )
					   System.out.print("\"" + birthFirst + " " + birthMid+ "\"" + " OR ");
				   else 
					   System.out.print("\"" + birthFirst + " " + birthMid + " " + birthLast+ "\"" + " OR ");
				   
				   if(!s.equals(birthMid) ) {
				   		System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + birthLast+ "\"" + " OR ");
				   		
				   		if(nickName != null)
				   		System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + nickName + " " + birthLast+ "\"" + " OR ");
					   }
				   System.out.print("\"" + birthLast + " " + birthFirst+ "\"" );
			  }
			  	   				  
			   if (nickName != null) {
				   System.out.print(" OR ");
				   System.out.print("\"" + birthFirst + " " + nickName + " " + birthLast+ "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + nickName + " " + birthMid+ "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " + nickName + " " + birthLast+ "\"");
			   }			   			 			   
		   }

		   //fix this down v do to make sure a name does not get printed if a person has the same first,MIDDLE, and last name as primary
		   //if primary person has same birthFirst and birthLast do not print some out  
		    if(null != personRelType && name.length == 4 && !node.getChildText("person_REL_TYPE").equals("Deceased")){
			   
			   System.out.print("\"" + birthFirst + " " + birthLast + "\"" + " OR ");
			   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) )
				   System.out.print("\"" + birthFirst + " " + lastName + "\"" + " OR ");
			   
			   if(!s.equals(birthMid) && birthFirst.equals(primaryBirthFirst) && birthLast.equals(primaryBirthLast)  ) {
			   //System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
			   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   }

			   System.out.print("\"" + birthFirst + " " + birthLastInitial + " " + lastName + "\"" + " OR ");
			   System.out.print("\"" + birthFirst + " " + birthMid + " " + birthLast + "\"");
			   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) /*&& !s.equals(birthMid)*/) {
				   System.out.print(" OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " + lastName + "\"" + " OR ");
			   }
			   
			   if(!s.equals(birthMid)) {
				   System.out.print(" OR ");
				   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + birthLast  + "\"" );
			   
			   if(nickName != null ) {
				   System.out.print(" OR ");
				   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + nickName+ " " + birthLast + "\"" + " OR ");
			   	   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + nickName + " " + lastName + "\"" + " OR ");
			   }
		    }

			   if (nickName != null) {
				   System.out.print("\"" + birthFirst + " " +nickName+ " " + birthLast + "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthLastInitial + " " + nickName+ " " + lastName + "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " + nickName + " " + birthLast + "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " + nickName+ " " + lastName);	
			   }			   
		   }
		    else if(null == personRelType && name.length == 4 && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")){

		    	System.out.print("\"" + birthFirst + " " + birthLast + "\"" + " OR ");
		    				   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) ) 
				   System.out.print("\"" + birthFirst + " " + lastName + "\"" + " OR ");
			   			   
			   if(!s.equals(birthMid) && birthFirst.equals(primaryBirthFirst) && birthLast.equals(primaryBirthLast)  ) {
			   //System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
			   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   }
			   
			   System.out.print("\"" + birthFirst + " " + birthLastInitial + " " + lastName + "\"" + " OR ");
			   System.out.print("\"" + birthFirst + " " + birthMid + " " + birthLast + "\"" );
			   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast)) {
				   System.out.print(" OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " + lastName + "\"" );
			   }
			   
			   if(!s.equals(birthMid)) {
				   System.out.print(" OR ");
			   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " + birthLast + "\"" + " OR ");
			   
			   if(nickName != null ) {
				   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " +nickName+ " " + birthLast + "\"" + " OR ");
			   	   System.out.print("\"" + birthFirst + " " + birthMidInitial + " " +nickName+ " " + lastName + "\"" + " OR ");
			   }
		    }
			   if (nickName != null) {
				   System.out.print(" OR ");
				   System.out.print("\"" + birthFirst + " " +nickName+ " " + birthLast + "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthLastInitial + " " +nickName+ " " + lastName + "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " +nickName+ " " + birthLast + "\"" + " OR ");
				   System.out.print("\"" + birthFirst + " " + birthMid + " " +nickName+ " " + lastName);	
			   }
		   }

		//if PR_NAME_GN has four pieces, primary person and female
		    else if(name.length == 4){
			   
			   System.out.print("\"" + birthFirst + " " + birthLast + "\"" + " OR ");
			   System.out.print("\"" +birthFirst + " " + lastName + "\"" + " OR ");
			   			     
			   System.out.print("\"" +lastName + " " + birthFirst+ "\"" + " OR ");
			   
			   if(!s.equals(birthMid) ) {
			   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + birthLast + "\"" + " OR ");
			   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   
			   if(nickName != null ) {
				   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + nickName+ " " + birthLast + "\"" + " OR ");
			   	   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + nickName+ " " + lastName + "\"" + " OR ");
			   	}
			   }
			   
			   System.out.print("\"" +birthFirst + " " + birthLastInitial + " " + lastName + "\"" + " OR ");
			   System.out.print("\"" +birthFirst + " " + birthMid + " " + birthLast + "\"" + " OR ");
			   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");	
			   
			   //ask
			   if (nickName != null) {
				   System.out.println(" OR ");
				   System.out.print("\"" +birthFirst + " " + nickName + " " + birthLast + "\"" + " OR ");
				   System.out.print("\"" +birthFirst + " " + birthLastInitial + " " +nickName + " " + lastName + "\"" + " OR ");
				   System.out.print("\"" +birthFirst + " " + birthMid + " " +nickName+ " " + birthLast + "\"" + " OR ");
				   System.out.print("\"" +birthFirst + " " + birthMid + " " + nickName+ " " + lastName + "\"");	

			   }			   
		   }
	}
	
	public static void malePermutation (String [] name,  String last, String lastName,  String birthFirst, 
			String birthMid, char birthFirstInitial, char birthLastInitial, char birthMidInitial,
			String primaryBirthFirst, String primaryLastName, String primaryBirthMid, char primaryBirthMidIn, 
			String primaryBirthLast, String personRelType, Element node, boolean gender, String nickName) {
		
		personRelType = node.getChildText("person_REL_TYPE");
		
		String s = Character.toString(birthMidInitial);

		//if name has one piece
		   if(name.length == 1 ) {
			   if(gender == true) {
			   System.out.print("\"" +birthFirst + primaryBirthLast+ "\"");
			   }
			   else 
				   System.out.print("\"" +birthFirst + primaryLastName+ "\"");
		   }
		   
		   //if there are two pieces and none are in the last name element
		   if(last == null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)) {
			   if(gender == true) {
				   System.out.print("\"" +birthFirst + " "  + birthMid + " " + primaryBirthLast + "\"" + " OR ");
				   System.out.print("\"" +birthFirst + " " + primaryBirthLast + "\"");
			   }
			   else if (gender == false) {
				    System.out.print("\"" +birthFirst + " "  + birthMid + " " + primaryLastName + "\"" + " OR ");
			   		System.out.print("\"" +birthFirst + " "  + primaryLastName + "\"");
			   }
   		   }
		   
		   //if main person ever doesn't not have personRelType tag
		   /* if ( null == personRelType && name.length == 2 && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")){
			   	System.out.println(birthFirst + " " + lastName);
			   	//System.out.println(birthFirstInitial + " " + lastName);
		   }*/

		   //if it is primary person print out name
		   else if (null != personRelType &&  last != null && name.length == 2 && node.getChildText("person_REL_TYPE").equals("Deceased")){
		   	System.out.print("\"" +birthFirst + " " + lastName + "\"" + " OR ");
		   	System.out.print("\"" +lastName + " " + birthFirst + "\"");
		   	
		   	if (nickName != null) {
			    System.out.print(" OR ");
		   		System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
		   	}
		   	//System.out.println(birthFirstInitial + " " + lastName);
	   }
		   
		    /* else if (null == personRelType &&  last  != null && name.length == 2 && 
		     * node.getChildText("person_REL_TYPE").equals("Deceased")){
		   	System.out.println(birthFirst + " " + lastName);
		   	System.out.println(lastName + " " + birthFirst);
		   	//System.out.println(birthFirstInitial + " " + lastName);
	   } */

		   //if secondary person and just one name matches
		   else if(last != null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)
				   && lastName.equals(primaryLastName)) {
			   System.out.print("\"" +birthFirst + " " + lastName + "\"");
			   
			   if (nickName != null) {
				    System.out.print(" OR ");
			   		System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
			   }
		   }
		   
		   //if secondary person and just one name matches
		   else if(last != null && name.length == 2 && birthFirst.equals(primaryBirthFirst)
				   && !lastName.equals(primaryLastName)) {
			   System.out.print("\"" +birthFirst + " " + lastName + "\"");
			   
			   if (nickName != null) {
				    System.out.print(" OR ");
			   		System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
			   }
		   }

		   //if secondary person and same first name and last name
		   else if (last != null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)
				   && !lastName.equals(primaryLastName)){
				System.out.print("\"" +birthFirst + " " + lastName + "\"");
				
				if (nickName != null) {
				    System.out.print(" OR ");
			   		System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
				}
	   }
   
			//if PR_NAME_GN has three pieces (two spaces) and not female
		   if( personRelType != null && name.length == 3 ){
			   
			   if ( !birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryLastName) && 
					   !node.getChildText("person_REL_TYPE").equals("Deceased")  ){
				   System.out.print("\"" +birthFirst + " " + lastName + "\"");				 
				   
				   if ( nickName != null) {
					    System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\""); 
				   }
				   
				   if (!birthMid.equals(primaryBirthMid)) {
					   System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
					  
					   if ( nickName != null) {
						    System.out.print(" OR ");
						   System.out.print("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\""); 
					   }
				   }
			   }

			   else if (node.getChildText("person_REL_TYPE").equals("Deceased")) {
				   System.out.print("\"" +birthFirst + " " + lastName + "\"" + " OR "); 
				   System.out.print("\"" +lastName + " " + birthFirst + "\"" + " OR ");
				   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					    System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"" + " OR "); 
					   System.out.print("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\""); 
				   }				   
			   }
			   
			   else if (!node.getChildText("person_REL_TYPE").equals("Deceased") && birthFirst.equals(primaryBirthFirst) 
					   && lastName.equals(primaryLastName) && !birthMid.equals(primaryBirthMid) ) {
				   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					    System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\"");
				   }
			   }			   
				   
			   
			   	   if(!s.equals(birthMid) ) {
					    System.out.print(" OR ");
			   			System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   			System.out.print("\"" +birthFirstInitial + " " + birthMid + " " + lastName + "\"");
			   		
					   if ( nickName != null) {
						   System.out.print(" OR ");
						   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + nickName + " " + lastName + "\""); 
						   //System.out.println( birthMid + " " + "'"+nickName+ "'" + " " + lastName);
					   }
				   }			   
		   }
		   
		   //if PR_NAME_GN has three pieces (two spaces) and not female
		   else if( name.length == 3 ){
			   
			   if ( !birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryLastName) && 
					   !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")  ){
				   System.out.print("\"" +birthFirst + " " + lastName + "\"");
				   
				   if ( nickName != null) { 
					   System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"" ); 
				   }
				   
				   if (!birthMid.equals(primaryBirthMid)) {
					   System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"" );
					  
					   if ( nickName != null) {
						   System.out.print(" OR ");
						   System.out.print("\"" +birthFirst + " " + birthMid + " " +nickName+ " " + lastName + "\"" ); 
					   }
				   }
			   }

			   else if (null == personRelType && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) {
				   //System.out.print(" OR ");
				   System.out.print("\"" +birthFirst + " " + lastName + "\"" + " OR "); 
				   System.out.print("\"" +lastName + " " + birthFirst + "\"" + " OR ");
				   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					   System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + nickName + " " + lastName + "\"" + " OR "); 
					   System.out.print("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\""); 
				   }				   
			   }
			   
			   else if (!node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") && birthFirst.equals(primaryBirthFirst) 
					   && lastName.equals(primaryLastName) && !birthMid.equals(primaryBirthMid) ) {
				   System.out.print(" OR ");
				   System.out.print("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					   System.out.print(" OR ");
					   System.out.print("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\"");
				   }
			   }			   
			   			   	
			   	   if(!s.equals(birthMid) ) {
					    System.out.print(" OR ");
			   			System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   			System.out.print( "\"" +birthFirstInitial + " " + birthMid + " " + lastName + "\"");
			   		
					   if ( nickName != null) {
						   System.out.print(" OR ");
						   System.out.print("\"" +birthFirst + " " + birthMidInitial + " " + nickName + " " + lastName + "\"" ); 
						   //System.out.println( birthMid + " " + "'"+nickName+ "'" + " " + lastName);
					   }
				   }			   
		   	}
		}
}

package nameVariation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class TestPermutation {
	public static void main(String [] args) {
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/Users/tfernandes/Desktop/fsObits/fsObitMale6.xml");

		 try {

			 Document document = (Document) builder.build(xmlFile);
				
			 //root element
				Element rootNode = document.getRootElement();
				
				Element htmlNode = null;
				Element nbxNode = null;
				Namespace NS = null;
				Element enhancements = null;
				
				if (rootNode.getChild("HTML") != null) {
					htmlNode = rootNode.getChild("HTML");
					NS = Namespace.getNamespace("nbx","http://www.newsbank.com/xml/nbx/1.0");
					enhancements = htmlNode.getChild("enhancements", NS);
				}
				else {
					nbxNode = rootNode.getChild("NBX");
					NS = Namespace.getNamespace("nbx","http://www.newsbank.com/xml/nbx/1.0");
					enhancements = nbxNode.getChild("enhancements", NS);
				}
				
				Element enhancement = enhancements.getChild("enhancement", NS);
				
				Namespace NS2 = Namespace.getNamespace("","http://www.newsbank.com/xml/FamilySearch/1.0");
				
				Element FamilySearch = enhancement.getChild("FamilySearch", NS2);			

				Element record = FamilySearch.getChild("record", NS2); 
				
				@SuppressWarnings("rawtypes")
				List list = record.getChildren("person", NS2);
			

				String [] all = { "Wife", "Husband", "Spouse", "Ex-Wife","Ex-Husband","Mother", "Father", "Parent", "Sister", "Brother", "Sister-In-Law", 
						"Brother-In-Law", "Aunt", "Uncle", "Cousin", "Mother-In-Law", "Stepfather", "Sibling", "Stepmother","Father-In-Law", "Stepbrother",
						"Stepsister", "Sibling-In-Law", "Parent-In-Law", "Stepsibling", "Fiancé", "Stepparent", "Domestic Partner"};
				
				String primaryBirthFirst = null;
				String primaryBirthMid = null;
				char primaryBirthMidIn = 0;
				String primaryBirthLast= null;
				String primaryLastName = null;
				boolean gender = false;
				char space = ' ';
				char quotes = '"';
				char O =  'O';
				char R = 'R';
				StringBuffer triplets = new StringBuffer(); 

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

				   Element node = (Element) list.get(i);				   
				   				   
				   String first = node.getChildText("person_PR_NAME_GN", NS2); 
				   String last = node.getChildText("person_PR_NAME_SURN", NS2);
				   
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
				   if(last == null && name.length == 2 && node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
					   //if there are two pieces and none are in the last name element
						   birthFirst = name[0].substring(0, name[0].length());
						   birthMid = name[1].substring(0, name[1].length());
						   birthMidInitial = name[1].charAt(0);						   
					   }
					   
					   else if (last != null && name.length == 2 && 
							   node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")){
						   birthFirst = name[0].substring(0, name[0].length());
					   	   birthLast = name[1].substring(0, name[1].length());
					   }
				   
				   //if has two pieces and male...
				   if(last == null && name.length == 2 && !node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
					   //if there are two pieces and none are in the last name element
						   birthFirst = name[0].substring(0, name[0].length());
						   birthMid = name[1].substring(0, name[1].length());
					   }
				   
				   //if it is primary person print out name
				   else if (last != null && name.length == 2 && !node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")){
					   birthFirst = name[0].substring(0, name[0].length());
				   	   lastName = name[1].substring(0, name[1].length());
				   	   
				   	   //get initial of birth first name
				   	   birthFirstInitial = name[0].charAt(0);
				   }
				
				//if PR_NAME_GN has three pieces (two spaces) and female
				   if(name.length == 3 && node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")){

					   birthFirst = name[0].substring(0, name[0].length());
					   birthMid = name[1].substring(0, name[1].length());
					   birthLast = name[2].substring(0, name[2].length());
					  
					   //get initial of middle name
					   birthMidInitial = name[1].charAt(0);
				   }
				   
				   //if three pieces and male
					//if PR_NAME_GN has three pieces (two spaces) and not female
				    if(name.length == 3 && !node.getChildText("person_PR_SEX_CODE", NS2).equals("Female") ){

					   birthFirst = name[0].substring(0, name[0].length());
					   birthMid = name[1].substring(0, name[1].length());
					   lastName = name[2].substring(0, name[2].length());
					  
					   //get initial of first name
					   birthFirstInitial = name[0].charAt(0);
					   
					   //get initial of middle name
					   birthMidInitial = name[1].charAt(0);
				   }
				    
				//if PR_NAME_GN has three pieces (two spaces) and female
				    if(name.length == 4 && node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")){

					   birthFirst = name[0].substring(0, name[0].length());
					   birthMid = name[1].substring(0, name[1].length());
					   birthLast = name[2].substring(0, name[2].length());
					   lastName = name[3].substring(0, name[3].length());
					   
					   //get initial of middle name
					   birthMidInitial = name[1].charAt(0);

					   //get initial of birth last name
					   birthLastInitial = name[2].charAt(0);							
				   }
				    				    
				    String personRelType = node.getChildText("person_REL_TYPE", NS2);
				    String personPRrelationship = node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2);
				    
					//save the specific name into a unique name to compare with secondary people to make sure there are no repeats
					if ( personRelType != null && node.getChildText("person_REL_TYPE", NS2).equals("Deceased") ) {
						primaryBirthFirst = birthFirst;
						primaryBirthMid = birthMid;
						primaryBirthMidIn = birthMidInitial;
						primaryLastName = lastName;
						primaryBirthLast = birthLast;						
						
						if (node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
							gender = true;
						}
					}   
										
					   // if it is primary person...
					   if ( personRelType != null && node.getChildText("person_REL_TYPE", NS2).equals("Deceased") ) {
						  
						   if ( last == null) {
					    		triplets.append("Inefficient Information");
					    		System.out.println(triplets);
					    		System.exit(0);					    		
					    }					   
						   //if primary person and female...
						   if(node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
					    			

							   triplets.append("&p_field_primary-0=alltext&p_params_primary-0=weight:1&p_text_primary-0=");
							   triplets.append("(");
							   
							   femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
									   lastName,  birthLastInitial,  birthMidInitial, primaryBirthFirst, 
					   					 primaryBirthLast, primaryLastName, personRelType, node, gender, 
					   					 nickName, primaryBirthMid, primaryBirthMidIn, NS2, personPRrelationship, triplets);							   
						   }
						   
						   
						 //if primary person and not female...
						   else if(!node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
					    		
							   triplets.append("&p_field_primary-0=alltext&p_params_primary-0=weight:1&p_text_primary-0=");
							   triplets.append("(");
							   
							   malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
					   					birthMidInitial, primaryBirthFirst, primaryLastName, primaryBirthMid, primaryBirthMidIn, personRelType, primaryBirthLast,
					   					node, gender, nickName, NS2, personPRrelationship, triplets);							   
							   }		
						   						   
						   triplets.append(")");
						   
						   triplets.append("&p_bool_second-1=OR&p_field_second-1=alltext&p_params_second-1=weight:1&p_text_second-1=");
						   triplets.append("(");
						   
						   boolean termCheck = false;
						   
						   if (null != node.getChildText("person_PR_BIRTH_CITY_TOWN", NS2)) {
							termCheck = true;
							triplets.append("\"" + node.getChildText("person_PR_BIRTH_CITY_TOWN", NS2) + "\"");
						   }
						   
						   if (null != node.getChildText("person_PR_DEATH_CITY_TOWN", NS2) && 
								   !node.getChildText("person_PR_DEATH_CITY_TOWN", NS2).equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN", NS2))) {

							   if(termCheck == true)
							   triplets.append(" OR ");
							   
							   termCheck = true;
							   							   
							   triplets.append("\"" + node.getChildText("person_PR_DEATH_CITY_TOWN", NS2) + "\"");
						   }
						   
						   if (null != node.getChildText("person_PR_RES_CITY_TOWN", NS2) && 
								   !node.getChildText("person_PR_RES_CITY_TOWN", NS2).equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN", NS2))
								   && !node.getChildText("person_PR_RES_CITY_TOWN", NS2).equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN", NS2))) {

							   if(termCheck == true)
							   triplets.append(" OR ");
							   
							   termCheck = true;

							   triplets.append("\"" + node.getChildText("person_PR_RES_CITY_TOWN", NS2) + "\"");
						   }
						   
						   if (null != node.getChildText("person_PR_BUR_CITY_TOWN", NS2) && 
								   !node.getChildText("person_PR_BUR_CITY_TOWN", NS2).equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN", NS2))
								   && !node.getChildText("person_PR_BUR_CITY_TOWN", NS2).equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN", NS2)) 
								   && !node.getChildText("person_PR_BUR_CITY_TOWN", NS2).equalsIgnoreCase(node.getChildText("person_PR_RES_CITY_TOWN", NS2))) {
	
							   if(termCheck == true)
							   triplets.append(" OR ");
							   
							   termCheck = true;

							   triplets.append("\"" + node.getChildText("person_PR_BUR_CITY_TOWN", NS2) + "\"");
						   }

						   triplets.append(")");						   						   
					   }					   
					   
						  	boolean check = false;
						  	if (personRelType != null && node.getChildText("person_REL_TYPE", NS2).equals("Deceased"))
						  		check = true;					
						  	
						  	if(check == true) {
						  		triplets.append("&p_field_second-0=alltext&p_params_second-0=weight:3&p_text_second-0=");
								triplets.append("(");	
						  	}
						  	
							//Start if SECONDARY people
						   for (int a = 0; a < all.length; a++)	{

							   if ( personRelType != null && node.getChildText("person_REL_TYPE", NS2).equals(all[a])) {
								  								 
									   //if secondary person and female...
							   		if(node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {							   			
								   		
							   			femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
							   					lastName,  birthLastInitial,  birthMidInitial, primaryBirthFirst, 
							   					 primaryBirthLast, primaryLastName, personRelType, node, gender, nickName,
							   					primaryBirthMid, primaryBirthMidIn, NS2, personPRrelationship, triplets);

							   			if(triplets.charAt(triplets.length() - 1) == quotes )
						   					triplets.append(" OR ");	
							   		}							   
							   		
							   		//if secondary person and not female...
							   		if(!node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
								   		
							   			malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
							   					birthMidInitial,  primaryBirthFirst, primaryLastName, primaryBirthMid, primaryBirthMidIn, primaryBirthLast, 
							   					personRelType, node, gender, nickName, NS2, personPRrelationship, triplets);							   
							   		
							   			if(triplets.charAt(triplets.length() - 1) == quotes )
						   					triplets.append(" OR ");	

							   		}							   		
							   }
							   							   
							   else if (personPRrelationship != null &&  node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals(all[a])) {

								   //if secondary person and female...
						   		if(node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
						   			
						   			femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
						   					lastName,  birthLastInitial,  birthMidInitial, 
						   					 primaryBirthFirst, primaryBirthLast, primaryLastName,
						   					 personRelType, node, gender, nickName, primaryBirthMid,
						   					 primaryBirthMidIn, NS2, personPRrelationship, triplets);	
						   			
						   			if(triplets.charAt(triplets.length() - 1) == quotes )
					   					triplets.append(" OR ");	
						   								   			
						   		}
						   
						   		//if secondary person and not female...
						   		if(!node.getChildText("person_PR_SEX_CODE", NS2).equals("Female")) {
						   			
						   			malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
						   					birthMidInitial, primaryBirthFirst, primaryLastName, primaryBirthMid, primaryBirthMidIn, primaryBirthLast,  
						   					personRelType, node, gender, nickName, NS2, personPRrelationship, triplets);						   

						   			if(triplets.charAt(triplets.length() - 1) == quotes )
					   					triplets.append(" OR ");	

						   		}						   		
							  }							   							
						 }

						  	if(i == list.size() - 1) 
						  		triplets.append(")");						  	
				}
								
				if( triplets.charAt(triplets.length() - 5) == space && triplets.charAt(triplets.length() - 4) == O &&
						triplets.charAt(triplets.length() - 3) == R && triplets.charAt(triplets.length() - 2) == space) 
					triplets.delete(triplets.length() - 5, triplets.length() - 1); 
				
				System.out.print(triplets);
				
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }		
	}
	
	public static void femalePermutation (String [] name, String last, String birthFirst, String birthMid, String birthLast,  
			String lastName, char birthLastInitial, char birthMidInitial,  String primaryBirthFirst, String primaryBirthLast, 
			String primaryLastName, String personRelType, Element node, boolean gender, String nickName, String primaryBirthMid,
			char primaryBirthMidIn, Namespace NS2, String personPRrelationship, StringBuffer triplets) {

		   String s = Character.toString(birthMidInitial);
		  
		//if name has one piece
		   if(name.length == 1 ) {
			   if(gender == true) 
			   triplets.append("\"" +birthFirst + " " + primaryBirthLast+ "\"");
			   
			   else 
				   triplets.append("\"" +birthFirst + " " + primaryLastName+ "\"");			   
		   }

		 //if there are two pieces and none are in the last name element
		   if(personRelType != null &&last == null && name.length == 2 ) {
			   
			   if(node.getChildText("person_REL_TYPE", NS2).equals("Wife") || node.getChildText("person_REL_TYPE", NS2).equals("Ex-Wife")
					   || node.getChildText("person_REL_TYPE", NS2).equals("Spouse") || node.getChildText("person_REL_TYPE", NS2).equals("Parent")
					   || node.getChildText("person_REL_TYPE", NS2).equals("Mother")) {
			   if(gender == true) {
				   triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryBirthLast+ "\""+ " OR ");
				   triplets.append("\"" +birthFirst + " " + primaryBirthLast+ "\"");
				   
				   if(!s.equals(birthMid)) {
					   	   triplets.append(" OR ");
					   	   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + primaryBirthLast+ "\"");	
				   }
			   }
			   else if (gender == false) {
				    triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryLastName+ "\""+ " OR ");
			   		triplets.append("\"" +birthFirst + " "  + primaryLastName+ "\"");
			   		
					   if(!s.equals(birthMid)) {
					   	   triplets.append(" OR ");
					   	   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + primaryLastName+ "\"");
					   }
			   		}
			   	}
			  }

		   else if(personPRrelationship != null &&last == null && name.length == 2 ) {
			   
			   if(node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Wife") || node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Ex-Wife")
					   || node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Spouse") || node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Parent")
					   || node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Mother")) {
			   if(gender == true) {
				   triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryBirthLast+ "\""+ " OR ");
				   triplets.append("\"" +birthFirst + " " + primaryBirthLast+ "\"");
				   
				   if(!s.equals(birthMid)) {
					   	   triplets.append(" OR ");
					   	   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + primaryBirthLast+ "\"");	
				   }
			   }
			   else if (gender == false) {
				    triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryLastName+ "\""+ " OR ");
			   		triplets.append("\"" +birthFirst + " "  + primaryLastName+ "\"");
			   		
					   if(!s.equals(birthMid)) {
					   	   triplets.append(" OR ");
					   	   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + primaryLastName+ "\"");
					   }
			   		}
			   	}
			   
			   }
		   		  
		   //fix if female have same name
			   if (last != null && name.length == 2){
			    triplets.append("\"" +birthFirst + " " + birthLast+ "\"");
			   	
			   	if (null != personRelType && node.getChildText("person_REL_TYPE", NS2).equals("Deceased")) {
				   	   triplets.append(" OR ");
				   	triplets.append("\"" +birthLast + " " + birthFirst+ "\"");
			   	}
			   	
			   	
			   	else if (personPRrelationship != null  && node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Deceased")) {
				   	triplets.append(" OR ");
			   		triplets.append("\"" +birthLast + " " + birthFirst+ "\"");
			   	}
			   		
			   	if(nickName != null) {
				   	triplets.append(" OR ");
			   		triplets.append("\"" +birthFirst + " " +  nickName + " " + birthLast+ "\"");
			   	}

		   }

			   //if PR_NAME_GN has three pieces (two spaces) and female
		   if(name.length == 3 ){
			   
			   triplets.append("\"" + birthFirst + " " + birthLast+ "\"" + " OR ");
		   
			  if (!birthFirst.equals(primaryBirthFirst) && !birthMid.equals(primaryBirthMid) && !birthLast.equals(primaryBirthLast)) {
			
			//if there are two parts in last name instead of two parts in first name	  
			   if (last.contains(" ") ) 
				   triplets.append("\"" + birthFirst + " " + birthMid+ "\"" + " OR ");
			   
			   else  
				   triplets.append("\"" + birthFirst + " " + birthMid + " " + birthLast+ "\"" + " OR ");

			   
			   if(!s.equals(birthMid) && birthMidInitial != primaryBirthMidIn ) {
				   
			   		triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + birthLast+ "\"");
			   		
			   		if(nickName != null) {

					   	triplets.append(" OR ");
			   			triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + nickName + " " + birthLast+ "\"" + " OR ");
			   			
			   		}
				 }
			  }
			  
			  else if (personRelType != null && node.getChildText("person_REL_TYPE", NS2).equals("Deceased")) {
				   if (last.contains(" ") ) 					   
					   triplets.append("\"" + birthFirst + " " + birthMid+ "\"" + " OR ");
				   
				   else 
					   triplets.append("\"" + birthFirst + " " + birthMid + " " + birthLast+ "\"" + " OR ");				   
				   
				   if(!s.equals(birthMid) ) {
				   		triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + birthLast+ "\"" + " OR ");
				   		
				   		if(nickName != null)
				   		triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + nickName + " " + birthLast+ "\"" + " OR ");
					   }

				   triplets.append("\"" + birthLast + " " + birthFirst+ "\"" );
			  }
			  	   				  
			   if (nickName != null) {

				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " + nickName + " " + birthLast+ "\"" + " OR ");				   
				   triplets.append("\"" + birthFirst + " " + nickName + " " + birthMid+ "\"" + " OR ");				   
				   triplets.append("\"" + birthFirst + " " + birthMid + " " + nickName + " " + birthLast+ "\"");
			   }			   			 			   
		   }

		   //fix this down v do to make sure a name does not get printed if a person has the same first,MIDDLE, and last name as primary
		   //if primary person has same birthFirst and birthLast do not print some out  
		    if(null != personRelType && name.length == 4 && !node.getChildText("person_REL_TYPE", NS2).equals("Deceased")){
			   
			   triplets.append("\"" + birthFirst + " " + birthLast + "\"" + " OR ");
			   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) ) {
				   triplets.append("\"" + birthFirst + " " + lastName + "\"" + " OR ");
			   }
			   
			   if(!s.equals(birthMid) && birthFirst.equals(primaryBirthFirst) && birthLast.equals(primaryBirthLast)  ) 
				   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");			   

			   triplets.append("\"" + birthFirst + " " + birthLastInitial + " " + lastName + "\"" + " OR ");
			   triplets.append("\"" + birthFirst + " " + birthMid + " " + birthLast + "\"");
			   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) ) {
				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " + birthMid + " " + lastName + "\"" + " OR ");
			   }
			   
			   if(!s.equals(birthMid)) {
				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + birthLast  + "\"" );
			   
			   if(nickName != null ) {
				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + nickName+ " " + birthLast + "\"" + " OR ");
			   	   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + nickName + " " + lastName + "\"" + " OR ");
			   }
		    }

			   if (nickName != null) {
				   triplets.append("\"" + birthFirst + " " +nickName+ " " + birthLast + "\"" + " OR ");
				   triplets.append("\"" + birthFirst + " " + birthLastInitial + " " + nickName+ " " + lastName + "\"" + " OR ");
				   triplets.append("\"" + birthFirst + " " + birthMid + " " + nickName + " " + birthLast + "\"" + " OR ");	
				   triplets.append("\"" + birthFirst + " " + birthMid + " " + nickName+ " " + lastName);	
			   }			   
		   }
		    else if(personPRrelationship != null && name.length == 4 && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Deceased")){

		    	triplets.append("\"" + birthFirst + " " + birthLast + "\"" + " OR ");
		    				   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) ) 
				   triplets.append("\"" + birthFirst + " " + lastName + "\"" + " OR ");
			   			   
			   if(!s.equals(birthMid) && birthFirst.equals(primaryBirthFirst) && birthLast.equals(primaryBirthLast)  ) 
				   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   
			   
			   triplets.append("\"" + birthFirst + " " + birthLastInitial + " " + lastName + "\"" + " OR ");
			   triplets.append("\"" + birthFirst + " " + birthMid + " " + birthLast + "\"" );
			   
			   if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast)) {
				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " + birthMid + " " + lastName + "\"" );
			   }
			   
			   if(!s.equals(birthMid)) {
				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " + birthLast + "\"" + " OR ");
			   
			   if(nickName != null ) {
				   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " +nickName+ " " + birthLast + "\"" + " OR ");
			   	   triplets.append("\"" + birthFirst + " " + birthMidInitial + " " +nickName+ " " + lastName + "\"" + " OR ");
			   }
		    }
			   if (nickName != null) {
				   triplets.append(" OR ");
				   triplets.append("\"" + birthFirst + " " +nickName+ " " + birthLast + "\"" + " OR ");
				   triplets.append("\"" + birthFirst + " " + birthLastInitial + " " +nickName+ " " + lastName + "\"" + " OR ");
				   triplets.append("\"" + birthFirst + " " + birthMid + " " +nickName+ " " + birthLast + "\"" + " OR ");	
				   triplets.append("\"" + birthFirst + " " + birthMid + " " +nickName+ " " + lastName);	
			   }
		   }

		//if PR_NAME_GN has four pieces, primary person and female
		    else if(name.length == 4){
			   
			   triplets.append("\"" + birthFirst + " " + birthLast + "\"" + " OR ");
			   triplets.append("\"" +birthFirst + " " + lastName + "\"" + " OR ");
			   triplets.append("\"" +lastName + " " + birthFirst+ "\"" + " OR ");
			   
			   if(!s.equals(birthMid) ) {
			   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + birthLast + "\"" + " OR ");
			   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   
			   if(nickName != null ) {
				   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + nickName+ " " + birthLast + "\"" + " OR ");
			   	   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + nickName+ " " + lastName + "\"" + " OR ");
			   	}
			   }
			   
			   triplets.append("\"" +birthFirst + " " + birthLastInitial + " " + lastName + "\"" + " OR ");
			   triplets.append("\"" +birthFirst + " " + birthMid + " " + birthLast + "\"" + " OR ");
			   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");	
			   
			   //ask
			   if (nickName != null) {

				   triplets.append(" OR ");
				   triplets.append("\"" +birthFirst + " " + nickName + " " + birthLast + "\"" + " OR ");
				   triplets.append("\"" +birthFirst + " " + birthLastInitial + " " +nickName + " " + lastName + "\"" + " OR ");
				   triplets.append("\"" +birthFirst + " " + birthMid + " " +nickName+ " " + birthLast + "\"" + " OR ");	
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName+ " " + lastName + "\"");	

			   }			   
		   }
	}
	
	public static void malePermutation (String [] name,  String last, String lastName,  String birthFirst, 
			String birthMid, char birthFirstInitial, char birthLastInitial, char birthMidInitial,
			String primaryBirthFirst, String primaryLastName, String primaryBirthMid, char primaryBirthMidIn, 
			String primaryBirthLast, String personRelType, Element node, boolean gender, String nickName, Namespace NS2,
			String personPRrelationship, StringBuffer triplets) {
		
		personRelType = node.getChildText("person_REL_TYPE", NS2);
		personPRrelationship =  node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2);
		
		String s = Character.toString(birthMidInitial);

		//if name has one piece
		   if(name.length == 1 ) {
			   if(gender == true) 
			   triplets.append("\"" +birthFirst + primaryBirthLast+ "\"");

			   else 
				   triplets.append("\"" +birthFirst + primaryLastName+ "\"");
			   
		   }
		   
		   //if there are two pieces and none are in the last name element
		   if(personRelType != null && last == null && name.length == 2 ) {
			   if(node.getChildText("person_REL_TYPE", NS2).equals("Husband") || node.getChildText("person_REL_TYPE", NS2).equals("Ex-Husband")
					   || node.getChildText("person_REL_TYPE", NS2).equals("Spouse") || node.getChildText("person_REL_TYPE", NS2).equals("Parent")
					   || node.getChildText("person_REL_TYPE", NS2).equals("Father")) {
			   if(gender == true) {
				   triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryBirthLast + "\"" + " OR ");
				   triplets.append("\"" +birthFirst + " " + primaryBirthLast + "\"");
			   }
			   else if (gender == false) {
				    triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryLastName + "\"" + " OR ");
			   		triplets.append("\"" +birthFirst + " "  + primaryLastName + "\"");
			   }
			  }
   		   }
		   
		   else if(personPRrelationship != null && last == null && name.length == 2 ) {
			   if(node.getChildText("person_REL_TYPE", NS2).equals("Husband") || node.getChildText("person_REL_TYPE", NS2).equals("Ex-Husband")
					   || node.getChildText("person_REL_TYPE", NS2).equals("Spouse") || node.getChildText("person_REL_TYPE", NS2).equals("Parent")
					   || node.getChildText("person_REL_TYPE", NS2).equals("Father")) {
			   if(gender == true) {
				   triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryBirthLast + "\"" + " OR ");
				   triplets.append("\"" +birthFirst + " " + primaryBirthLast + "\"");
			   }
			   else if (gender == false) {
				    triplets.append("\"" +birthFirst + " "  + birthMid + " " + primaryLastName + "\"" + " OR ");
			   		triplets.append("\"" +birthFirst + " "  + primaryLastName + "\"");
			   }
			  }
   		   }
		   
		   //if it is primary person print out name
		   else if (null != personRelType &&  last != null && name.length == 2 && node.getChildText("person_REL_TYPE", NS2).equals("Deceased")){
		   	triplets.append("\"" +birthFirst + " " + lastName + "\"" + " OR ");
		   	triplets.append("\"" +lastName + " " + birthFirst + "\"");
		   	
		   	if (nickName != null) {
			    triplets.append(" OR ");
		   		triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
		   	}
	   }
		   
		   //if secondary person and just one name matches
		   else if(last != null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)
				   && lastName.equals(primaryLastName)) {
			   triplets.append("\"" +birthFirst + " " + lastName + "\"");
			   
			   if (nickName != null) {
				    triplets.append(" OR ");
			   		triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
			   }
		   }
		   
		   //if secondary person and just one name matches
		   else if(last != null && name.length == 2 && birthFirst.equals(primaryBirthFirst)
				   && !lastName.equals(primaryLastName)) {
			   
			   triplets.append("\"" +birthFirst + " " + lastName + "\"");
			   
			   if (nickName != null) {
				    triplets.append(" OR ");
			   		triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
			   }
		   }

		   //if secondary person and same first name and last name
		   else if (last != null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)
				   && !lastName.equals(primaryLastName)){
			   
				triplets.append("\"" +birthFirst + " " + lastName + "\"");
				
				if (nickName != null) {
				    triplets.append(" OR ");
			   		triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"");
				}
	   }
   
			//if PR_NAME_GN has three pieces & uses person_REL_TYPE
		   if( personRelType != null && name.length == 3 ){
			   
			   if ( !birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryLastName) && 
					   !node.getChildText("person_REL_TYPE", NS2).equals("Deceased")  ){

				   triplets.append("\"" +birthFirst + " " + lastName + "\"");	
				   
				   if ( nickName != null) {
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\""); 
				   }
				   
				   if (!birthMid.equals(primaryBirthMid)) {
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
					  
					   if ( nickName != null) {
							triplets.append(" OR ");
						    triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\""); 
					   }
				   }
			   }

			   else if (node.getChildText("person_REL_TYPE", NS2).equals("Deceased")) {
				   triplets.append("\"" +birthFirst + " " + lastName + "\"" + " OR "); 
				   triplets.append("\"" +lastName + " " + birthFirst + "\"" + " OR ");
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					    triplets.append(" OR ");
					    triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"" + " OR ");  
					    triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\""); 
				   }				   
			   }
			   
			   else if (!node.getChildText("person_REL_TYPE", NS2).equals("Deceased") && birthFirst.equals(primaryBirthFirst) 
					   && lastName.equals(primaryLastName) && !birthMid.equals(primaryBirthMid) ) {
				   
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					    triplets.append(" OR ");
					    triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\"");
				   }
			   }	
			   
			   //bias on charles E Reed chase make adjustments when possible
			   else if (!node.getChildText("person_REL_TYPE", NS2).equals("Deceased") && !birthFirst.equals(primaryBirthFirst) 
					   && lastName.equals(primaryLastName) && birthMid.equals(primaryBirthMid) ) {
				   triplets.append("\"" +birthFirst + " " + lastName + "\"");
				   triplets.append(" OR ");
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\"");
				   }
			   }

				   			   
			   	   if(!s.equals(birthMid) ) {

					    triplets.append(" OR ");
			   			triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   			triplets.append("\"" +birthFirstInitial + " " + birthMid + " " + lastName + "\"");
			   		
					   if ( nickName != null) {

						   triplets.append(" OR ");
						   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + nickName + " " + lastName + "\"");
					   }
				   }			   
		   }
		   
		   //if PR_NAME_GN has three pieces & uses person_PR_RELATIONSHIP_TO_DEC
		   else if( name.length == 3 ){
			   
			   if ( !birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryLastName) && 
					   !node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Deceased")  ){

				   triplets.append("\"" +birthFirst + " " + lastName + "\"");
				   
				   if ( nickName != null) { 
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"" ); 
				   }
				   
				   if (!birthMid.equals(primaryBirthMid)) {

					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"" );
					  
					   if ( nickName != null) {

						   triplets.append(" OR ");
						   triplets.append("\"" +birthFirst + " " + birthMid + " " +nickName+ " " + lastName + "\"" ); 
					   }
				   }
			   }

			   else if (personPRrelationship != null && node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Deceased") ) {

				   triplets.append("\"" +birthFirst + " " + lastName + "\"" + " OR "); 
				   triplets.append("\"" +lastName + " " + birthFirst + "\"" + " OR ");
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + nickName + " " + lastName + "\"" + " OR "); 
					   triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\""); 
				   }				   
			   }
			   
			   else if (!node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Deceased") && birthFirst.equals(primaryBirthFirst) 
					   && lastName.equals(primaryLastName) && !birthMid.equals(primaryBirthMid) ) {

				   triplets.append(" OR ");
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\"");
				   }
			   }
			   
			   //bias on charles E Reed chase make adjustments when possible
			   else if (!node.getChildText("person_PR_RELATIONSHIP_TO_DEC", NS2).equals("Deceased") && !birthFirst.equals(primaryBirthFirst) 
					   && lastName.equals(primaryLastName) && birthMid.equals(primaryBirthMid) ) {
				   triplets.append("\"" +birthFirst + " " + lastName + "\"");
				   triplets.append(" OR ");
				   triplets.append("\"" +birthFirst + " " + birthMid + " " + lastName + "\"");
				   
				   if ( nickName != null) {
					   triplets.append(" OR ");
					   triplets.append("\"" +birthFirst + " " + birthMid + " " + nickName + " " + lastName + "\"");
				   }
			   }
			   
			   	   if(!s.equals(birthMid) ) {
					    triplets.append(" OR ");
			   			triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + lastName + "\"" + " OR ");
			   			triplets.append( "\"" +birthFirstInitial + " " + birthMid + " " + lastName + "\"");
			   		
					   if ( nickName != null) {
						   triplets.append(" OR ");
						   triplets.append("\"" +birthFirst + " " + birthMidInitial + " " + nickName + " " + lastName + "\"" ); 
					   }
				   }			   
		   	}
		}
}

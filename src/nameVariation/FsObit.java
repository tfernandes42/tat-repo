package nameVariation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class FsObit {
	public static void main(String [] args) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/Users/tfernandes/Desktop/fsObits/fsObitMale4.xml");
		
		 try {

			 Document document = (Document) builder.build(xmlFile);
			 
			 //root element is NBX
				Element rootNode = document.getRootElement();
				//List s= rootNode.getAdditionalNamespaces();
				
//				Element enhancements = rootNode.getChild("nbx:enhancements");
//				Element enhancement = enhancements.getChild("nbx:enhancement");
//				Element FamilySearch = enhancement.getChild("FamilySearch");
				
				//String [] spouse = {"Wife", "Husband", "Spouse", "Ex-Wife","Ex-Husband"};
				//String [] parent = {"Mother", "Father", "Parent"};
				//String [] otherRel = {"Sister", "Brother", "Sister-In-Law", "Brother-In-Law","Aunt", "Uncle", "Cousin", "Mother-In-Law", "Stepfather", "Sibling", "Stepmother",
				//		"Father-In-Law", "Stepbrother", "Stepsister", "Sibling-In-Law", "Parent-In-Law", "Stepsibling", "Fiancé", "Stepparent", "Domestic Partner"};
				
				String [] all = {"Wife", "Husband", "Spouse", "Ex-Wife","Ex-Husband","Mother", "Father", "Parent","Sister", "Brother", "Sister-In-Law", "Brother-In-Law",
						"Aunt", "Uncle", "Cousin", "Mother-In-Law", "Stepfather", "Sibling", "Stepmother","Father-In-Law", "Stepbrother",
						"Stepsister", "Sibling-In-Law", "Parent-In-Law", "Stepsibling", "Fiancé", "Stepparent", "Domestic Partner"};
				
				String primaryBirthFirst = null;
				//String primaryBirthMid = null;
				//char primaryBirthMidIn = 0;
				String primaryBirthLast= null;
				String primaryMarrLast = null;


				@SuppressWarnings("rawtypes")
				List list = rootNode.getChildren("person");

				for (int i = 0; i < list.size(); i++) {
					
					String birthFirst = null;
					String birthMid = null;
					String birthLast = null;
					String marrLast = null;
					char birthLastInitial = 0;
					char birthMidInitial = 0;

				   Element node = (Element) list.get(i);				   
				   
				   String firstname = node.getChildText("person_PR_NAME_GN"); 
				   String[] GName = firstname.split(" ");
				   String lastname = node.getChildText("person_PR_NAME_SURN");
				
				   String[] SURName = null;
				   
				   if (lastname != null)
					   SURName = lastname.split(" ");	
				  
				//if PR_NAME_GN has three pieces (two spaces)
				   if(GName.length == 3){

					   birthFirst = GName[0].substring(0, GName[0].length());
					   birthMid = GName[1].substring(0, GName[1].length());
					   birthLast = GName[2].substring(0, GName[2].length());
					  
					   //get initial of middle name
					   birthMidInitial = GName[1].charAt(0);

					   //get initial of birth last name
					   birthLastInitial = GName[2].charAt(0);
				   }
				   
				   //String first = firstname.substring(0, firstname.indexOf(" "));
				   if(GName.length == 2){
					   birthFirst = GName[0].substring(0, GName[0].length());
					   birthMid = GName[1].substring(0, GName[1].length());
					   birthMidInitial = GName[1].charAt(0); 
				   }
				   				   
				   //if PR_NAME_GN only has one piece
				   if(GName.length == 1){
					   birthFirst = GName[0].substring(0, GName[0].length());
				   }
				   
				//if pBirthMidInitial = first char of nBirthMid, empty nBirthMid 
				   String s = Character.toString(birthMidInitial);
				   if(s.equals(birthMid) ) {
					   birthMid = null;
				   }
				   
				   //if PR_NAME_SURN has two pieces
				   if(SURName != null && SURName.length == 2 ){

					   birthLast = SURName[0].substring(0, SURName[0].length());
					   marrLast = SURName[1].substring(0, SURName[1].length());
					   birthLastInitial = SURName[0].charAt(0);
				   }		   
				   
				   //if PR_NAME_SURN only has one piece and male... 
				   if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
					  if (SURName != null && SURName.length == 1)
					   birthLast = SURName[0].substring(0, SURName[0].length());
				   }

				   //if PR_NAME_SURN only has one piece and female... //and spouse
				   if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
					  if (SURName != null && SURName.length == 1)
					   marrLast = SURName[0].substring(0, SURName[0].length());
				   }		
				   
				   String personRelType = node.getChildText("person_REL_TYPE");

				 //save the specific name into a unique name to compare with secondary people to make sure there are no repeats
					if (null!= personRelType && node.getChildText("person_REL_TYPE").equals("Deceased") ) {
						primaryBirthFirst = birthFirst;
						//primaryBirthMid = birthMid;
						//primaryBirthMidIn = birthMidInitial;
						primaryBirthLast = birthLast;
						primaryMarrLast= marrLast;
					}
				   
				   // if it is primary person...
				   if ( null!= personRelType && node.getChildText("person_REL_TYPE").equals("Deceased") ) {
					   
					   System.out.println("primary");
					   					   
					   //if primary person and female...
					   if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
						  
						   femalePermutation (birthFirst, birthMid, birthLast, marrLast,
								   birthLastInitial, birthMidInitial, node, primaryBirthFirst, primaryBirthLast, primaryMarrLast,
								   personRelType);
						   
						   System.out.println();
						   System.out.println("Descriptive terms: ");
						   if (null != node.getChildText("person_PR_BIRTH_CITY_TOWN")) {
							System.out.println(node.getChildText("person_PR_BIRTH_CITY_TOWN"));
						   }
						   if (null != node.getChildText("person_PR_CITY_TOWN") && 
								   !node.getChildText("person_PR_DEATH_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))) {
							System.out.println(node.getChildText("person_PR_DEATH_CITY_TOWN"));
						   }
						   if (null != node.getChildText("person_PR_RES_CITY_TOWN") && 
								   !node.getChildText("person_PR_RES_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
								   && !node.getChildText("person_PR_RES_PLACE").equalsIgnoreCase(node.getChildText("person_PR_DEATH_PLACE"))) {
							System.out.println(node.getChildText("person_PR_RES_CITY_TOWN"));
						   }
						   if (null != node.getChildText("person_PR_BUR_CITY_TOWN") && 
								   !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
								   && !node.getChildText("person_PR_BUR_PLACE").equalsIgnoreCase(node.getChildText("person_PR_DEATH_PLACE")) 
								   && !node.getChildText("person_PR_BUR_PLACE").equalsIgnoreCase(node.getChildText("person_PR_RES_PLACE"))) {
							System.out.println(node.getChildText("person_PR_BUR_CITY_TOWN"));
						   }
					   }
					   
					   
					 //if primary person and not female...
					   else if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
						   
						   malePermutation (birthFirst, birthMid, birthLast, marrLast,
								   birthLastInitial, birthMidInitial, node, primaryBirthFirst, primaryBirthLast,
								   personRelType);
						   
						   System.out.println();
						   System.out.println("Descriptive terms: ");
						   if (null != node.getChildText("person_PR_BIRTH_CITY_TOWN")) {
							System.out.println(node.getChildText("person_PR_BIRTH_CITY_TOWN"));
						   }
						   if (null != node.getChildText("person_PR_DEATH_CITY_TOWN") && 
								   !node.getChildText("person_PR_DEATH_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))) {
							System.out.println(node.getChildText("person_PR_DEATH_CITY_TOWN"));
						   }
						   if (null != node.getChildText("person_PR_RES_CITY_TOWN") && 
								   !node.getChildText("person_PR_RES_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
								   && !node.getChildText("person_PR_RES_PLACE").equalsIgnoreCase(node.getChildText("person_PR_DEATH_PLACE"))) {
							System.out.println(node.getChildText("person_PR_RES_CITY_TOWN"));
						   }
						   if (null != node.getChildText("person_PR_BUR_CITY_TOWN") && 
								   !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
								   && !node.getChildText("person_PR_BUR_PLACE").equalsIgnoreCase(node.getChildText("person_PR_DEATH_PLACE")) 
								   && !node.getChildText("person_PR_BUR_PLACE").equalsIgnoreCase(node.getChildText("person_PR_RES_PLACE"))) {
							System.out.println(node.getChildText("person_PR_BUR_CITY_TOWN"));
						   }						   
					   }				   
				   }
					  	
					   for (int a = 0; a < all.length; a++)	{
						   if (null!= personRelType && node.getChildText("person_REL_TYPE").equals(all[a])) {
						   System.out.println();
						   
						 //if secondary person and female...
						   		if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
							  
						   			femalePermutation (birthFirst, birthMid, birthLast, marrLast,
						   					birthLastInitial, birthMidInitial, node, primaryBirthFirst, primaryBirthLast, primaryMarrLast,
						   					personRelType);
							   
						   		}
						   
						   		//if secondary person and not female...
						   		if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
							   
						   			malePermutation (birthFirst, birthMid, birthLast, marrLast,
						   					birthLastInitial, birthMidInitial, node, primaryBirthFirst, primaryBirthLast, personRelType );							   
						   		}
						   }
					   	}
					   
					  
				   for (int a = 0; a < all.length; a++)	{					   
					   if (null == personRelType && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals(all[a])) {
					   System.out.println();
					   
					 //if secondary person and female...
					   		if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
						  
					   			femalePermutation (birthFirst, birthMid, birthLast, marrLast,
					   					birthLastInitial, birthMidInitial, node, primaryBirthFirst, primaryBirthLast, primaryMarrLast,
					   					personRelType);
						   
					   		}
					   
					   		//if secondary person and not female...
					   		if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
						   
					   			malePermutation (birthFirst, birthMid, birthLast, marrLast,
					   					birthLastInitial, birthMidInitial, node, primaryBirthFirst, primaryBirthLast,
					   					personRelType);						   
					   		}
					   }
				   }
				}
			
			  } catch (IOException io) {
				System.out.println(io.getMessage());
			  } catch (JDOMException jdomex) {
				System.out.println(jdomex.getMessage());
			  }
	}
	
	public static void femalePermutation (String birthFirst,String birthMid, String birthLast, String marrLast, 
			char birthLastInitial, char birthMidInitial, Element node, String primaryBirthFirst, String primaryBirthLast,
			String primaryMarrLast, String personRelType) {
		  System.out.println("HERE: " + personRelType);
		//if the person_REL_TYPE attribute exists use the following code...
		if (null != personRelType ) {
		
		   //if nBirthMid not empty and pBirthLast not empty
		   if (birthMid != null && birthLast != null && marrLast != null &&
				   !birthFirst.equals(primaryBirthFirst)  && !birthLast.equals(marrLast) 
				   && !node.getChildText("person_REL_TYPE").equals("Deceased") ) {
			   //System.out.println("RESULT0");
			   System.out.println(birthFirst + " " + birthMid + " " + birthLast);		
			   System.out.println(birthFirst + " " + birthMid + " " + marrLast);
		   }
		   
		   else if (birthMid != null && birthLast != null && node.getChildText("person_REL_TYPE").equals("Deceased")) {
			   System.out.println(birthFirst + " " + birthMid + " " + birthLast);
			   System.out.println(birthFirst + " " + birthMid + " " + marrLast);
		   }

		   else if (birthMid != null && birthLast != null) {
			   //System.out.println("Result 1");
			   System.out.println(birthFirst + " " + birthMid + " " + birthLast);
		   }
		   
		   //if nBirthMid not empty and pBirthLast empty
		   if (birthMid != null &&  birthLast == null && marrLast != null ) {
			   //System.out.println("RESULT2");
			   System.out.println(birthFirst + " " + birthMid + " " + marrLast);
		   }
		   
		   //couple reference!!!
		   //else if (nBirthMid != null &&  pBirthLast == null)
			 //  System.out.println(pBirthFirst + " " + nBirthMid);
		   
		   //if pBirthMidInitial not empty and pBirthLast not empty
		   if (birthMidInitial != 0 && birthLast != null && marrLast != null &&
				   !birthFirst.equals(primaryBirthFirst)  && !birthLast.equals(marrLast)
				   && !node.getChildText("person_REL_TYPE").equals("Deceased")) {
			   //System.out.println("RESULT3");
			   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
			   System.out.println(birthFirst + " " + birthMidInitial + " " + marrLast);
		   } 
		   
		   if (birthMidInitial != 0 && birthLast != null && marrLast != null &&
				   node.getChildText("person_REL_TYPE").equals("Deceased")) {
			   //System.out.println("RESULT4");
			   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
			   System.out.println(birthFirst + " " + birthMidInitial + " " + marrLast);
		   }
		   
		   //check this out.. either delete or modify
		   else if (birthMidInitial != 0 && birthLast != null) {
			   //System.out.println("RESULT5");
			   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
		   }
		   
		   //if pBirthMidInitial not empty and pBirthLast not empty
		   if (birthMidInitial != 0 && birthLast == null && marrLast != null) {
			   //System.out.println("RESULT6");
			   System.out.println(birthFirst + " " + birthMidInitial + " " + marrLast);
		   } 
		   
		   ////.......??
		   //else if (pBirthMidInitial != 0 && pBirthLast == null)
			 //  System.out.println(pBirthFirst + " " + pBirthMidInitial);
		   
		   //if pBirthLastInitial not empty
		   if (birthLastInitial != 0) {
			   //System.out.println("RESULT7");
			   System.out.println(birthFirst + " " + birthLastInitial + " " + marrLast);
		   }
		   
		   //if pBirthLast not empty then
		   if (birthLast != null ) {
			   //System.out.println("RESULT8");
			   System.out.println(birthFirst + " " + birthLast);
		   }
		   
		   if (marrLast != null && !birthFirst.equals(primaryBirthFirst) && birthLast != null && 
				   !birthLast.equals(primaryMarrLast)  && !node.getChildText("person_REL_TYPE").equals("Deceased") )
		   System.out.println(birthFirst + " " + marrLast);
		   
		   else if (marrLast != null  && birthLast == null && !node.getChildText("person_REL_TYPE").equals("Deceased") ) 
			   System.out.println(birthFirst + " " + marrLast);
		   
		   else if (marrLast != null  && node.getChildText("person_REL_TYPE").equals("Deceased") ) 
			   System.out.println(birthFirst + " " + marrLast);
		   

			 //use to grab parent/spouse last name to complete 
		    if (marrLast == null && node.getChildText("person_PR_SEX_CODE").equals("Female") &&
					   node.getChildText("person_REL_TYPE").equals("Wife") || node.getChildText("person_REL_TYPE").equals("Spouse") 
					   ||node.getChildText("person_REL_TYPE").equals("Ex-Wife")) 
				   System.out.println(birthFirst + " " + primaryBirthLast);
			   			   
		    if (marrLast == null && birthMid != null && node.getChildText("person_PR_SEX_CODE").equals("Female") &&
					   node.getChildText("person_REL_TYPE").equals("Wife") || 
					   node.getChildText("person_REL_TYPE").equals("Spouse") || node.getChildText("person_REL_TYPE").equals("Ex-Wife")) {
				   	//System.out.println(birthFirst + " " + primaryBirthLast);
			   		System.out.println(birthFirst + " " + birthMid + " " + primaryBirthLast);
			   		System.out.println(birthFirst + " " + birthMidInitial + " " + primaryBirthLast);
			   }
		}
		
		//if the person_REL_TYPE attribute does NOT exist and it uses "person_PR_RELATIONSHIP_TO_DEC" use the following code...		
		else if (null == personRelType ) {
			   //if nBirthMid not empty and pBirthLast not empty
			   if (birthMid != null && birthLast != null && marrLast != null &&
					   !birthFirst.equals(primaryBirthFirst)  && !birthLast.equals(marrLast) 
					   && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) {
				   //System.out.println("RESULT0");
				   System.out.println(birthFirst + " " + birthMid + " " + birthLast);		
				   System.out.println(birthFirst + " " + birthMid + " " + marrLast);
			   }
			   
			   else if (birthMid != null && birthLast != null && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")) {
				   System.out.println(birthFirst + " " + birthMid + " " + birthLast);
				   System.out.println(birthFirst + " " + birthMid + " " + marrLast);
			   }

			   else if (birthMid != null && birthLast != null) {
				   //System.out.println("Result 1");
				   System.out.println(birthFirst + " " + birthMid + " " + birthLast);
			   }
			   
			   //if nBirthMid not empty and pBirthLast empty
			   if (birthMid != null &&  birthLast == null && marrLast != null ) {
				   //System.out.println("RESULT2");
				   System.out.println(birthFirst + " " + birthMid + " " + marrLast);
			   }
			   
			   //couple reference!!!
			   //else if (nBirthMid != null &&  pBirthLast == null)
				 //  System.out.println(pBirthFirst + " " + nBirthMid);
			   
			   //if pBirthMidInitial not empty and pBirthLast not empty
			   if (birthMidInitial != 0 && birthLast != null && marrLast != null &&
					   !birthFirst.equals(primaryBirthFirst)  && !birthLast.equals(marrLast)
					   && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")) {
				   //System.out.println("RESULT3");
				   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
				   System.out.println(birthFirst + " " + birthMidInitial + " " + marrLast);
			   } 
			   
			   else if (birthMidInitial != 0 && birthLast != null && marrLast != null &&
					   node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")) {
				   //System.out.println("RESULT4");
				   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
				   System.out.println(birthFirst + " " + birthMidInitial + " " + marrLast);
			   }
			   
			   //check this out.. either delete or modify
			   else if (birthMidInitial != 0 && birthLast != null) {
				   //System.out.println("RESULT5");
				   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
			   }
			   
			   //if pBirthMidInitial not empty and pBirthLast not empty
			   if (birthMidInitial != 0 && birthLast == null && marrLast != null) {
				   //System.out.println("RESULT6");
				   System.out.println(birthFirst + " " + birthMidInitial + " " + marrLast);
			   } 
			   
			   ////.......??
			   //else if (pBirthMidInitial != 0 && pBirthLast == null)
				 //  System.out.println(pBirthFirst + " " + pBirthMidInitial);
			   
			   //if pBirthLastInitial not empty
			   if (birthLastInitial != 0) {
				   //System.out.println("RESULT7");
				   System.out.println(birthFirst + " " + birthLastInitial + " " + marrLast);
			   }
			   
			   //if pBirthLast not empty then
			   if (birthLast != null ) {
				   //System.out.println("RESULT8");
				   System.out.println(birthFirst + " " + birthLast);
			   }
			   
			   if (marrLast != null && !birthFirst.equals(primaryBirthFirst) && birthLast != null && 
					   !birthLast.equals(primaryMarrLast)  && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") )
			   System.out.println(birthFirst + " " + marrLast);
			   
			   else if (marrLast != null  && birthLast == null && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) 
				   System.out.println(birthFirst + " " + marrLast);
			   
			   else if (marrLast != null  && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) 
				   System.out.println(birthFirst + " " + marrLast);
			   
				 //use to grab parent/spouse last name to complete 
			    	if (marrLast == null && node.getChildText("person_PR_SEX_CODE").equals("Female") &&
						   node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Wife") || node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Spouse") 
						   ||node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Ex-Wife")) 
					   System.out.println(birthFirst + " " + primaryBirthLast);
				   				   
				    if (marrLast == null && birthMid != null && node.getChildText("person_PR_SEX_CODE").equals("Female") &&
						   node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Wife") || 
						   node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Spouse") || node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Ex-Wife")) {
					   	//System.out.println(birthFirst + " " + primaryBirthLast);
				   		System.out.println(birthFirst + " " + birthMid + " " + primaryBirthLast);
				   		System.out.println(birthFirst + " " + birthMidInitial + " " + primaryBirthLast);
				   }
			}
		}
	
	
	public static void malePermutation (String birthFirst, String birthMid, String birthLast, String marrLast, 
			char birthLastInitial, char birthMidInitial, Element node, String primaryBirthFirst, String primaryBirthLast,
			String personRelType) {	
		  System.out.println("HERE: " + personRelType);
		//if the person_REL_TYPE attribute exists use the following code...
		if (null != personRelType ) {

			if(birthMid != null && birthFirst.equals(primaryBirthFirst)  && birthLast.equals(primaryBirthLast) && 
					   !node.getChildText("person_REL_TYPE").equals("Deceased") ) {
				System.out.println(birthFirst + " " + birthMid + " " + birthLast);
				   //System.out.println(nBirthMid + " " + pBirthLast);
			}
			
			else if(birthMid != null ) {
			   
			   System.out.println(birthFirst + " " + birthMid + " " + birthLast);
			   System.out.println(birthMid + " " + birthLast);
		   }
		   
		   if(birthMidInitial != 0 && birthLast != null) 
			   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
		   
		   //will use this for couple
		   //else if(pBirthMidInitial != 0 )
			 //  System.out.println(pBirthFirst + " " + pBirthMidInitial);
		   		   
		   if (birthLast != null && node.getChildText("person_REL_TYPE").equals("Deceased")) {
			   System.out.println(birthFirst + " " + birthLast);
		   }
		   
		    if (birthLast != null && !birthFirst.equals(primaryBirthFirst) || !birthLast.equals(primaryBirthLast) && 
				   !node.getChildText("person_REL_TYPE").equals("Deceased") )
		    		System.out.println(birthFirst + " " + birthLast);
	
	
			 //use to grab parent/spouse last name to complete 
		    else if (birthLast == null && !node.getChildText("person_PR_SEX_CODE").equals("Female") && node.getChildText("person_REL_TYPE").equals("Spouse") || 
					   node.getChildText("person_REL_TYPE").equals("Husband") || node.getChildText("person_REL_TYPE").equals("Ex-Husband")) {
				   System.out.println("This is where it is coming from 1.5m: ");		   
				   System.out.println(birthFirst + " " + primaryBirthLast);
			   	}
			   
			   else if (birthLast == null && birthMid != null && !node.getChildText("person_PR_SEX_CODE").equals("Female") &&
					   node.getChildText("person_REL_TYPE").equals("Spouse") || node.getChildText("person_REL_TYPE").equals("Husband")
					    || node.getChildText("person_REL_TYPE").equals("Ex-Husband")) {
				   System.out.println("This is where it is coming from 2.5: ");
				   	System.out.println(birthFirst + " " + primaryBirthLast);
			   		System.out.println(birthFirst + " " + birthMid + " " + primaryBirthLast);
			   		System.out.println(birthFirst + " " + birthMidInitial + " " + primaryBirthLast);
			   }

		}
		
		//if the person_REL_TYPE attribute does NOT exist and it uses "person_PR_RELATIONSHIP_TO_DEC" use the following code...				
		else if (null == personRelType ) {
			if(birthMid != null && birthFirst.equals(primaryBirthFirst)  && birthLast.equals(primaryBirthLast) && 
					   !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) {
				System.out.println(birthFirst + " " + birthMid + " " + birthLast);
				   //System.out.println(nBirthMid + " " + pBirthLast);
			}
			
			else if(birthMid != null ) {
			   
			   System.out.println(birthFirst + " " + birthMid + " " + birthLast);
			   System.out.println(birthMid + " " + birthLast);
		   }
		   
		   if(birthMidInitial != 0 && birthLast != null) 
			   System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
		   
		   //will use this for couple
		   //else if(pBirthMidInitial != 0 )
			 //  System.out.println(pBirthFirst + " " + pBirthMidInitial);
		   		   
		   if (birthLast != null && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")) {
			   System.out.println(birthFirst + " " + birthLast);
		   }
		   
		    if (birthLast != null && !birthFirst.equals(primaryBirthFirst) || !birthLast.equals(primaryBirthLast) && 
				   !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") )
		    		System.out.println(birthFirst + " " + birthLast);
	
	
			 //use to grab parent/spouse last name to complete 
		    else if (birthLast == null && !node.getChildText("person_PR_SEX_CODE").equals("Female") && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Spouse") || 
					   node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Husband") || node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Ex-Husband")) {
				   System.out.println("This is where it is coming from 1.5m: ");		   
				   System.out.println(birthFirst + " " + primaryBirthLast);
			   	}
			   
			   else if (birthLast == null && birthMid != null && !node.getChildText("person_PR_SEX_CODE").equals("Female") &&
					   node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Spouse") || node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Husband")
					    || node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Ex-Husband")) {
				   System.out.println("This is where it is coming from 2.5: ");
				   	System.out.println(birthFirst + " " + primaryBirthLast);
			   		System.out.println(birthFirst + " " + birthMid + " " + primaryBirthLast);
			   		System.out.println(birthFirst + " " + birthMidInitial + " " + primaryBirthLast);
			   }
		}
	
	}
}

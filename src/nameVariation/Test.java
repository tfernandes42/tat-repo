package nameVariation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Test {
public static void main(String [] args) {
SAXBuilder builder = new SAXBuilder();
File xmlFile = new File("/Users/tfernandes/Desktop/fsObits/fsObitFemale2.dummy.xml");

try {

Document document = (Document) builder.build(xmlFile);
 
//root element is NBX
Element rootNode = document.getRootElement();
@SuppressWarnings("rawtypes")
List list = rootNode.getChildren("person");
String [] all = { "Wife", "Husband", "Spouse", "Ex-Wife","Ex-Husband","Mother", "Father", "Parent","Sister", "Brother", "Sister-In-Law", 
"Brother-In-Law", "Aunt", "Uncle", "Cousin", "Mother-In-Law", "Stepfather", "Sibling", "Stepmother","Father-In-Law", "Stepbrother",
"Stepsister", "Sibling-In-Law", "Parent-In-Law", "Stepsibling", "Fiancé", "Stepparent", "Domestic Partner"};
String primaryBirthFirst = null;
//String primaryBirthMid = null;
//char primaryBirthMidIn = 0;
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

  Element node = (Element) list.get(i);	  
  
  String first = node.getChildText("person_PR_NAME_GN"); 
  String last = node.getChildText("person_PR_NAME_SURN");
  
   if (first != null && first.contains("Or")) {
  
 String[] orBirthFirst = first.split(" Or ");

 first = orBirthFirst[0].substring(0, orBirthFirst[0].length());
 nickName = orBirthFirst[1].substring(0, orBirthFirst[1].length());
 //System.out.println(nickName); 
  } 
   
    if (last != null && last.contains(" Or")) {
   	int or = last.indexOf(" Or");
last = last.substring(0, or);
   }
    else if (last == null )
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
  
  //put this back if ERROR in future
  //get initial of middle name
  birthMidInitial = name[1].charAt(0);

  //get initial of birth last name
  birthLastInitial = name[2].charAt(0);
  }

   String personRelType = node.getChildText("person_REL_TYPE");
   
//save the specific name into a unique name to compare with secondary people to make sure there are no repeats
if ( null!= personRelType && node.getChildText("person_REL_TYPE").equals("Deceased") ) {
primaryBirthFirst = birthFirst;
//primaryBirthMid = birthMid;
//primaryBirthMidIn = birthMidInitial;
primaryLastName = lastName;
primaryBirthLast = birthLast;
if (node.getChildText("person_PR_SEX_CODE").equals("Female")) {
gender = true;
}
}   
//save the specific name into a unique name if main person does not have personRelType element
/*else if ( null == personRelType && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) {
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
  if ( null!= personRelType && node.getChildText("person_REL_TYPE").equals("Deceased") ) {
  
  System.out.println("primary");
    
  //if primary person and female...
  if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
 
  femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
  lastName,  birthLastInitial,  birthMidInitial, primaryBirthFirst, 
  primaryBirthLast, primaryLastName, personRelType, node, gender, nickName);
  
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
  && !node.getChildText("person_PR_RES_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN"))) {
System.out.println(node.getChildText("person_PR_RES_CITY_TOWN"));
  }
  if (null != node.getChildText("person_PR_BUR_CITY_TOWN") && 
  !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
  && !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN")) 
  && !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_RES_CITY_TOWN"))) {
System.out.println(node.getChildText("person_PR_BUR_CITY_TOWN"));
  }
  }
  
  
//if primary person and not female...
  else if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
  
  malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
  birthMidInitial, primaryBirthFirst, primaryLastName, personRelType, primaryBirthLast,
  node, gender, nickName);
  
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
  && !node.getChildText("person_PR_RES_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN"))) {
System.out.println(node.getChildText("person_PR_RES_CITY_TOWN"));
  }
  if (null != node.getChildText("person_PR_BUR_CITY_TOWN") && 
  !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_BIRTH_CITY_TOWN"))
  && !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_DEATH_CITY_TOWN")) 
  && !node.getChildText("person_PR_BUR_CITY_TOWN").equalsIgnoreCase(node.getChildText("person_PR_RES_CITY_TOWN"))) {
System.out.println(node.getChildText("person_PR_BUR_CITY_TOWN"));
  }	  
  }	  
  }
 
  for (int a = 0; a < all.length; a++)	{
  if (null!= personRelType && node.getChildText("person_REL_TYPE").equals(all[a])) {
  System.out.println();
  
//if secondary person and female...
  if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
 
  femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
  lastName,  birthLastInitial,  birthMidInitial, primaryBirthFirst, 
  primaryBirthLast, primaryLastName, personRelType, node, gender, nickName);
  
  }
  
  //if secondary person and not female...
  if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
  
  malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
  birthMidInitial,  primaryBirthFirst, primaryLastName, primaryBirthLast, 
  personRelType, node, gender, nickName);	  
  }
  }
  }
  
 
  for (int a = 0; a < all.length; a++)	{	  
  if (null == personRelType && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals(all[a])) {
  System.out.println();
  
//if secondary person and female...
  if(node.getChildText("person_PR_SEX_CODE").equals("Female")) {
 
  femalePermutation (name, last,  birthFirst,  birthMid,  birthLast,  
  lastName,  birthLastInitial,  birthMidInitial, 
  primaryBirthFirst, primaryBirthLast, primaryLastName,
  personRelType, node, gender, nickName);	  
  }
  
  //if secondary person and not female...
  if(!node.getChildText("person_PR_SEX_CODE").equals("Female")) {
  
  malePermutation (name, last, lastName, birthFirst, birthMid,  birthFirstInitial,  birthLastInitial,  
  birthMidInitial, primaryBirthFirst, primaryLastName, primaryBirthLast, 
  personRelType, node, gender, nickName);	  
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
//when back from lunch figure out why sister-in-law is not working!!!!!!
public static void femalePermutation (String [] name, String last, String birthFirst, String birthMid, String birthLast,  
String lastName, char birthLastInitial, char birthMidInitial, 
String primaryBirthFirst, String primaryBirthLast, String primaryLastName, 
String personRelType, Element node, boolean gender, String nickName) {

  String s = Character.toString(birthMidInitial);

//if name has one piece
  if(name.length == 1) {
  if(gender == true) {
  System.out.println(birthFirst + " " + primaryBirthLast);
  }
  else 
  System.out.println(birthFirst + " " + primaryLastName);
  }
  
//if there are two pieces and none are in the last name element
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
  
  else if (last != null && name.length == 2 /* && birthFirst == null && birthLast == null */){
  System.out.println(birthFirst + " " + birthLast);
  
 
  if (null != personRelType && node.getChildText("person_REL_TYPE").equals("Deceased")) {
  System.out.println(birthLast + " " + birthFirst);
  if(nickName != null)
  System.out.println(birthFirst + " " +  "'"+nickName+ "'" + " " + birthLast);
  }
 
  else if (null == personRelType  && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")) {
  System.out.println(birthLast + " " + birthFirst);
 
  if( nickName != null)
  System.out.println(birthFirst + " " +  "'"+nickName+ "'" + " " + birthLast);
  }
  }
  
  /*else if (last != null && name.length == 2 && birthFirst != null && birthFirst.equals(primaryBirthFirst)
  && birthLast != null && birthLast.equals(primaryBirthLast)){

  //System.out.println(birthFirst + " " + birthLast);
  }*/

  //if PR_NAME_GN has three pieces (two spaces) and female
  if(name.length == 3 ){
  
  System.out.println(birthFirst + " " + birthLast);
  
  //if there are two parts in last name instead of two parts in first name	  
  if (last.contains(" ") )
  System.out.println(birthFirst + " " + birthMid);
  else 
  System.out.println(birthFirst + " " + birthMid + " " + birthLast);
  
  if (nickName != null)
  System.out.println(birthFirst + " " + "'"+nickName+ "'" + " " + birthLast);
  
if (personRelType != null && node.getChildText("person_REL_TYPE").equals("Deceased")) 
  System.out.println(birthLast + " " + birthFirst);
  
  if(!s.equals(birthMid) ) {
  System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
  }
  
  }

  //if primary person has same birthFirst and birthLast do not print some out  
   if(null != personRelType && name.length == 4 && !node.getChildText("person_REL_TYPE").equals("Deceased")){
  
  System.out.println(birthFirst + " " + birthLast);
  
if (node.getChildText("person_REL_TYPE").equals("Deceased")) 
  System.out.println(birthLast + " " + birthFirst);
 
  if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) )
  System.out.println(birthFirst + " " + lastName);
  
  if(!s.equals(birthMid) && birthFirst.equals(primaryBirthFirst) && birthLast.equals(primaryBirthLast)  ) {
  //System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
  System.out.println(birthFirst + " " + birthMidInitial + " " + lastName);
  }

  System.out.println(birthFirst + " " + birthLastInitial + " " + lastName);
  System.out.println(birthFirst + " " + birthMid + " " + birthLast);
  
  if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) /*&& !s.equals(birthMid)*/)
  System.out.println(birthFirst + " " + birthMid + " " + lastName);
  
  if(!s.equals(birthMid)) 
  System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);

  }
   else if(null == personRelType && name.length == 4 && !node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")){

   	System.out.println(birthFirst + " " + birthLast);
   	  
  if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast) ) 
  System.out.println(birthFirst + " " + lastName);
    
  if(!s.equals(birthMid) && birthFirst.equals(primaryBirthFirst) && birthLast.equals(primaryBirthLast)  ) {
  //System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
  System.out.println(birthFirst + " " + birthMidInitial + " " + lastName);
  }
  
  System.out.println(birthFirst + " " + birthLastInitial + " " + lastName);
  System.out.println(birthFirst + " " + birthMid + " " + birthLast);
  
  if(!birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryBirthLast))
  System.out.println(birthFirst + " " + birthMid + " " + lastName);
  
  if(!s.equals(birthMid)) 
  System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);

  }


//if PR_NAME_GN has four pieces, primary person and female
   else if(name.length == 4){
  
  System.out.println(birthFirst + " " + birthLast);
  System.out.println(birthFirst + " " + lastName);
  
  if(nickName != null )
  System.out.println(birthFirst + " " + "'"+nickName+ "'" + " " + birthLast);
  
  System.out.println(lastName + " " + birthFirst);
  
  if(!s.equals(birthMid) ) {
  System.out.println(birthFirst + " " + birthMidInitial + " " + birthLast);
  System.out.println(birthFirst + " " + birthMidInitial + " " + lastName);
  }
  
  System.out.println(birthFirst + " " + birthLastInitial + " " + lastName);
  System.out.println(birthFirst + " " + birthMid + " " + birthLast);
  System.out.println(birthFirst + " " + birthMid + " " + lastName);	 
  }
}
public static void malePermutation (String [] name,  String last, String lastName,  String birthFirst, 
String birthMid, char birthFirstInitial, char birthLastInitial, char birthMidInitial,
String primaryBirthFirst, String primaryLastName, String primaryBirthLast, String personRelType, 
Element node, boolean gender, String nickName) {
personRelType = node.getChildText("person_REL_TYPE");
String s = Character.toString(birthMidInitial);

//if name has one piece
  if(name.length == 1) {
  if(gender == true) {
  System.out.println(birthFirst + primaryBirthLast);
  }
  else 
  System.out.println(birthFirst + primaryLastName);
  }
  
  //if there are two pieces and none are in the last name element
  if(last == null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)) {
  if(gender == true) {
  System.out.println(birthFirst + " "  + birthMid + " " + primaryBirthLast);
  System.out.println(birthFirst + " " + primaryBirthLast);
  }
  else if (gender == false) {
   System.out.println(birthFirst + " "  + birthMid + " " + primaryLastName);
  System.out.println(birthFirst + " "  + primaryLastName);
  }
   	  }
  
  //if main person ever doesn't not have personRelType tag
  /* if ( null == personRelType && name.length == 2 && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")){
  System.out.println(birthFirst + " " + lastName);
  //System.out.println(birthFirstInitial + " " + lastName);
  }*/

  //if it is primary person print out name
  ///fix this v it shouldn't be null
  else if (null != personRelType &&  last != null && name.length == 2 && node.getChildText("person_REL_TYPE").equals("Deceased")){
  System.out.println(birthFirst + " " + lastName);
  System.out.println(lastName + " " + birthFirst);
 
  if (nickName != null)
  System.out.println(birthFirst + " " + "'"+nickName+ "'" + " " + lastName);
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
  System.out.println(birthFirst + " " + lastName);
  }
  
  //if secondary person and just one name matches
  else if(last != null && name.length == 2 && birthFirst.equals(primaryBirthFirst)
  && !lastName.equals(primaryLastName)) {
  System.out.println(birthFirst + " " + lastName);
  }

  //if secondary person and same first name and last name
  else if (last != null && name.length == 2 && !birthFirst.equals(primaryBirthFirst)
  && !lastName.equals(primaryLastName)/* && birthFirst == null && lastName == null */){
System.out.println(birthFirst + " " + lastName);
  //System.out.println(birthFirstInitial + " " + lastName);
  }
   
  			//if PR_NAME_GN has three pieces (two spaces) and not female
  			if(null != personRelType && name.length == 3 ){
  				if ( !birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryLastName) && 
  						!node.getChildText("person_REL_TYPE").equals("Deceased")  ){
  					System.out.println(birthFirst + " " + lastName);
  				}
  				else if (node.getChildText("person_REL_TYPE").equals("Deceased")) {
  					System.out.println(birthFirst + " " + lastName); 
  					System.out.println(lastName + " " + birthFirst);
  				}
  
  				if (personRelType != null && node.getChildText("person_REL_TYPE").equals("Deceased") && nickName != null) {
  					System.out.println(birthFirst + " " + "'"+nickName+ "'" + " " + lastName); 
  				}
  				
  				System.out.println("HERE: ");
  				System.out.println(birthFirst + " " + birthMid + " " + lastName);
   
  				if(!s.equals(birthMid) ) {
  					System.out.println(birthFirst + " " + birthMidInitial + " " + lastName);
  					System.out.println( birthMid + " " + lastName);
  				}
 
  				//System.out.println(birthMid + " " + lastName);
  			}
  			
  			//if PR_NAME_GN has three pieces (two spaces) and not female
  			else if( name.length == 3 ){
  				if ( !birthFirst.equals(primaryBirthFirst) && !lastName.equals(primaryLastName) && 
  						!node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased")  ){
  					System.out.println(birthFirst + " " + lastName);
  				}
  				//               fix this v part if first person ever doesn't have personRel tag
  				else if (null == personRelType && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") ) {
  					System.out.println(birthFirst + " " + lastName); 
  					System.out.println(lastName + " " + birthFirst);
  				}
  
  				if (personRelType == null && node.getChildText("person_PR_RELATIONSHIP_TO_DEC").equals("Deceased") && nickName != null) {
  					System.out.println(birthFirst + " " + "'"+nickName+ "'" + " " + lastName); 
  				}
  
  				System.out.println(birthFirst + " " + birthMid + " " + lastName);
 
  				if(!s.equals(birthMid) ) {
  					System.out.println(birthFirst + " " + birthMidInitial + " " + lastName);
  					System.out.println( birthMid + " " + lastName);
  				}
 
  				//System.out.println(birthMid + " " + lastName);
  			}
		}	
	}

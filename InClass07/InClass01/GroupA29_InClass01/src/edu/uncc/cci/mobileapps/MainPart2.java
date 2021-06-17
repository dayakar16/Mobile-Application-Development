//    Assignment #: InClass Assignment 1
//    MainPart2.java
//    Dayakar Ravuri and Anoosh Hari,  Group A 29

package edu.uncc.cci.mobileapps;

import java.util.*;
import edu.uncc.cci.mobileapps.Data;

public class MainPart2 {
    /*
    * Question 2:
    * - In this question you will use the Data.users array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - The goal is to count the number of users living each state.
    * - Print out the list of State, Count order in ascending order by count.
    * */

    public static void main(String[] args) {
        //example on how to access the Data.users array.
        Map<String,Integer> m = new HashMap<String,Integer>();
        for (String str : Data.users) {
            //System.out.println(str);
            MainPart1.User user = new MainPart1().new User(str);
            if(m.containsKey(user.state))
            {
                m.put(user.state,m.get(user.state)+1);
            }
            else
            {
                m.put(user.state,1);
            }
        }
        List<Map.Entry<String,Integer>> finalList = new LinkedList(m.entrySet());
        Collections.sort(finalList, new Comparator<Map.Entry<String,Integer>>(){
            public int compare(Map.Entry<String,Integer> temp1, Map.Entry<String,Integer> temp2)
            {
                return (temp1.getValue()).compareTo(temp2.getValue());
            }
        });
        ArrayList<String> finalOrderedList = new ArrayList<>();
        for (Map.Entry itr : finalList){
            finalOrderedList.add(itr.getKey()+"="+itr.getValue());
        }
        System.out.println(finalOrderedList);
    }
}
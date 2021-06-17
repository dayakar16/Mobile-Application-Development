// Assignment # : In Class Assignment 1
// MainPart1.java
// Dayakar Ravuri and Anoosh Hari  Group A 29
package edu.uncc.cci.mobileapps;

import java.util.ArrayList;
import java.util.Collections;
import edu.uncc.cci.mobileapps.Data;


public class MainPart1 {

    /*
     * Question 1:
     * - In this question you will use the Data.users array that includes
     * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
     * - Create a User class that should parse all the parameters for each user.
     * - Insert each of the users in a list.
     * - Print out the TOP 10 oldest users.
     * */
    public class User implements Comparable<User>{
        String firstname, lastname , email , gender , city ,state;
        int age;
        public int getAge() {
            return age;
        }
        public User(String str){
            String[] data = str.split(",");
            this.firstname = data[0];
            this.lastname = data[1];
            this.email = data[3];
            this.gender = data[4];
            this.city = data[5];
            this.state = data[6];
            this.age = Integer.parseInt(data[2]);
        }

        @Override
        public String toString() {
            return "{ " +
                    "firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", email='" + email + '\'' +
                    ", gender='" + gender + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", age=" + age +
                    "}\n";
        }

        @Override
        public int compareTo(User o) {
            return this.age-o.getAge();
        }
    }
    public static void main(String[] args) {
        ArrayList userArrayList = new ArrayList<User>();
        //example on how to access the Data.users array.
        for (String str : Data.users) {
            //System.out.println(str);
            MainPart1 object = new MainPart1();
            MainPart1.User user = object.new User(str);
            userArrayList.add(user);
           // System.out.println(userArrayList);
        }
        Collections.sort(userArrayList);
        ArrayList top10 = new ArrayList<User>(userArrayList.subList(userArrayList.size() -10, userArrayList.size()));
        System.out.println(top10);
    }

}

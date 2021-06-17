//    Assignment #: InClass Assignment 1
//    MainPart3.java
//    Dayakar Ravuri and Anoosh Hari,  Group A 29
package edu.uncc.cci.mobileapps;


import java.util.*;

public class MainPart3 {
    /*
    * Question 3:
    * - This is a simple programming question that focuses on finding the
    * longest increasing subarray. Given the array A = {1,2,3,2,5,2,4,6,7} the
    * longest increasing subarray is {2,4,6,7}, note that the values have to be
    * contiguous.
    * */

    public static void main(String[] args) {
        //example call
        //int[] input = {}; // output {}
        //int[] input = {1}; // output {1}
        //int[] input = {1,2,3,4}; // output {1,2,3,4}
        //int[] input = {1,2,3,4,4,4,4,4,5,6}; // output {1,2,3,4}
        //int[] input = {1,2,3,-1,4,5,8,20,25,1,1,4,6}; // output {-1,4,5,8,20,25}
        //int[] input = {1,2,3,1,1,1,2,3,4,1,1,2,4,6,7}; // output{1,2,4,6,7}
        //int[] input = {1,2,3,2,5,2,4,6,7}; // output {2,4,6,7}
        //int[] result = printLongestSequence(input)
        Set<String> first =  new HashSet<>(Arrays.asList(Data.users));
        ArrayList<User> finalsortedusers = new ArrayList<>();
        for (String str : Data.otherUsers){
            if(!first.add(str)){
                finalsortedusers.add(new User(str));
            }
        }
        Collections.sort(finalsortedusers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(finalsortedusers);
    }

}
class User implements Comparable<User>{
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
        return o.state.compareTo(this.state);
    }
}
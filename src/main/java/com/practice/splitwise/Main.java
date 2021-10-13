package com.practice.splitwise;

import com.practice.splitwise.models.Splitwise;
import com.practice.splitwise.models.User;

import java.util.*;

public class Main {
    public static void main(String args[]){
        Splitwise splitwise = new Splitwise(new HashMap<>(), new HashMap<>());
        splitwise.addUser(new User("Tulika", "abc@gmail.com"));
        splitwise.addUser(new User("Sajal", "sajal@gmail.com"));
        splitwise.addUser(new User("Neha", "neha@gmail.com"));
        splitwise.addUser(new User("Xan", "xan@gmail.com"));

        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Enter 1 for entering the expense and Enter 2 for viewing your balance");
            Integer input = scanner.nextInt();
            switch(input){
                case 1:
                    //new Expense
                    createExpense(splitwise, scanner);
                    break;

                case 2:
                    System.out.println("Enter your user name to show your balance");
                    String name = scanner.next();
                    show(splitwise, name);
                    break;

            }
        }

    }
    public static void createExpense(Splitwise splitwise, Scanner scanner){
        System.out.println("Enter split type");
        System.out.println("Enter 1 for EQUAL");
        System.out.println("Enter 2 for EXACT");
        System.out.println("Enter 3 for PERCENTAGE");

        Integer input = scanner.nextInt();
        switch (input){
            case 1:
                System.out.println("Enter amount");
                Double amount = scanner.nextDouble();

                System.out.println("Enter no of users");
                Integer noOfUsers = scanner.nextInt();

                System.out.println("Enter name of friends who will owe you money with hyphen between them");
                String entries = scanner.next();
                List<String> friends = Arrays.asList(entries.split("-"));

                List<User> userFriends = convertToUserType(splitwise, friends);

                System.out.println("Enter your name");
                User user = convertToUserType(splitwise,scanner.next());

                splitwise.createEqualExpense(user, amount, noOfUsers, userFriends);
                for (Map.Entry<User, Map<User, Double>> entry : splitwise.getSplitTable().entrySet()) {
                    System.out.println(entry.getKey().getName() + " owes");

                    for(Map.Entry<User, Double> entry1 : entry.getValue().entrySet()){
                        System.out.println(entry1.getKey().getName()+" "+entry1.getValue());
                    }
                }

                break;

        }
    }


    public static List<User> convertToUserType(Splitwise splitwise, List<String> friends){
        List<User> userFriends = new ArrayList<>();
        for(String friend : friends){
            if(splitwise.getUserList().containsKey(friend)){
                userFriends.add(splitwise.getUserList().get(friend));
            }
        }
        return userFriends;
    }

    public static User convertToUserType(Splitwise splitwise, String friend){
        User user = new User();
        if(splitwise.getUserList().containsKey(friend)){
                return splitwise.getUserList().get(friend);
            }
        return user;
    }

    public static void show(Splitwise splitwise, String name){
        User user = splitwise.getUserList().get(name);
        System.out.println("Your account details ->");
        if(splitwise.getSplitTable().get(user).size()>0){
            System.out.println(splitwise.getSplitTable().get(user));
        }
        else{
            System.out.println("no remaining balance");
        }

    }
}

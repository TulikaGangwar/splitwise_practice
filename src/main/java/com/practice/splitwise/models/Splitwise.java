package com.practice.splitwise.models;

import com.practice.splitwise.SplitwiseService;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Splitwise {
    Map<String, User> userList;
    Map<User, Map<User, Double>> splitTable;

    SplitwiseService splitwiseService = new SplitwiseService();

    public Splitwise(Map<String, User> userList, Map<User, Map<User, Double>> splitTable) {
        this.userList = userList;
        this.splitTable = splitTable;
    }

    public void addUser(User user){
        userList.put(user.getName(), user);
        splitTable.put(user, new HashMap<>());
    }

    public void createEqualExpense(User user, double amount, int noOfUsers, List<User> friends){
        Map<User, Double> addedAmount = splitwiseService.split(user, amount, noOfUsers, friends, "EQUAL");
        Map<User, Double> selfRecord = splitTable.get(user);
        for(User friend : friends){
            Double remainingOwed = 0D;
            Double result = 0D;
            if(selfRecord.containsKey(friend)) {
                result = splitwiseService.checkOwedMoney(addedAmount.get(friend), selfRecord, friend, remainingOwed);
                splitOwedMoneyToAccounts(selfRecord, result, friend, user);
            }
            else {
                result = addedAmount.get(friend);
                splitMoneyToNewAccounts( result, friend, user);

            }
        }
    }


    public void splitOwedMoneyToAccounts(Map<User, Double> selfRecord, double result, User friend,User user){
        if(result>0){
            selfRecord.put(friend, result);
            splitTable.put(user, selfRecord);
        }
        else if(result<0){
            selfRecord.remove(friend);
            splitTable.put(user, selfRecord);
            Map<User, Double> friendRecord = splitTable.get(friend);
            friendRecord.put(user, result*-1);
            splitTable.put(friend, friendRecord);

        }
        else{
            selfRecord.remove(friend);
            splitTable.put(user, selfRecord);
        }
    }

    public void splitMoneyToNewAccounts(double result , User friend, User user) {
        if(splitTable.get(friend).containsKey(user)){
            splitTable.get(friend).put(user, splitTable.get(friend).get(user) + result);
        }
        else {
            splitTable.get(friend).put(user, result);
        }
    }
}

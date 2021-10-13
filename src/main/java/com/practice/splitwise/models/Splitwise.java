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
            Map<User, Double> record = splitTable.get(friend);
            Double remainingOwed = 0D;
            int result = splitwiseService.checkOwedMoney(addedAmount.get(friend), selfRecord, friend, remainingOwed);
            if(result==1){
                if(remainingOwed == 0){
                    selfRecord.remove(friend);
                    splitTable.put(user, selfRecord);
                }
                else{
                    selfRecord.put(friend, remainingOwed);
                    splitTable.put(user, selfRecord);
                }
            }
            else if(result==-1) {
                if(remainingOwed!=0){
                    splitTable.get(friend).put(user, remainingOwed);
                }
                else {
                    splitTable.get(friend).remove(user);
                }
            }
            else {
                if(splitTable.get(friend).containsKey(user)){
                    splitTable.get(friend).put(user, splitTable.get(friend).get(user) + addedAmount.get(friend));
                }
                else {
                    splitTable.get(friend).put(user, addedAmount.get(friend));
                }
            }
        }
    }
}

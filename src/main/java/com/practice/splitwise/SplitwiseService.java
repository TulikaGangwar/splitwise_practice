package com.practice.splitwise;

import com.practice.splitwise.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitwiseService {
    public Map<User, Double> split(User user, double amount, int noOfUsers, List<User> friends, String splitType){
        Map<User, Double> map = new HashMap<>();
        switch (splitType){
            case "EQUAL":
                double amountForOne = amount/noOfUsers;
                for(User friend : friends){
                    map.put(friend, amountForOne);
                }
                break;
        }
        return map;
    }

    public double checkOwedMoney(double addMoney, Map<User, Double> selfRecord, User friend, double remainingOwedAmount ){
        double alreadyOwedAmount = 0D;
            alreadyOwedAmount = selfRecord.get(friend);
            remainingOwedAmount = alreadyOwedAmount - addMoney;
            return remainingOwedAmount;

    }

}

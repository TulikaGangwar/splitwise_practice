package com.practice.splitwise.models;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String name;
    String emailID;
}

package com.theCoffeeHouse.theCoffeeHouse.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderByDay {

    @Transient
    String day;

    @Transient
    Double totalMoney;

}

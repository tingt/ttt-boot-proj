package com.ttt.core.test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/5/11 下午12:12
 */
public class Java8LambdaTest {
    public static void main(String[] args){
        //old
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello from thread");
            }
        });

        //lambda
        Thread t2=new Thread(() -> System.out.println("hello from thread by lambda"));

        //old
        List<Integer> list= Arrays.asList(1,2,3,4,5,6);
        for(Integer i:list){
            System.out.println(i);
        }

        //lambda
        list.forEach(i -> System.out.println(i));

        list.forEach(System.out::println);

        //use predicate
        System.out.println("print all numbers");
        evaluate(list,n -> true);

        System.out.println("print no numbers");
        evaluate(list,n -> false);

        System.out.println("print even numbers");
        evaluate(list,n -> n%2 ==0);

        System.out.println("print odd numbers");
        evaluate(list,n -> n%2 !=0);

        //old
        List<Integer> list2= Arrays.asList(1,2,3,4,5,6);
        for(Integer i:list2){
            int n = i*i;
            System.out.println(n);
        }
        list.stream().map((x) -> x*x).forEach(System.out::println);

        //Old way:
        List<Integer> list3 = Arrays.asList(1,2,3,4,5,6,7);
        int sum = 0;
        for(Integer n : list) {
            int x = n * n;
            sum = sum + x;
        }
        System.out.println(sum);

        int sum2 = list.stream().map(x -> x*x).reduce((x,y) -> x + y).get();
        System.out.println(sum);
    }

    public static void evaluate(List<Integer> list, Predicate<Integer> predicate){
        for(Integer n : list){
            if(predicate.test(n)){
                System.out.println(n+" ");
            }
        }
    }
}

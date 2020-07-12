package com.ttt.core.test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/4/28 下午4:22
 */
public class StreamTest {
    public static void main(String[] args) {
        /*Stream.of("apple","banana","orange","waltermaleon","grape").map(String::length).forEach(System.out::println);
        Stream.of("apple","banana","orange","waltermaleon","grape").mapToInt(String::length).forEach(System.out::println);
        Stream.of("a-b-c","d-e-f").flatMap(e -> Stream.of(e.split("-"))).forEach(System.out::println);
        List<String> output = Stream.of("a","b","c").map(String::toUpperCase).collect(Collectors.toList());
        IntStream.of(1,2,3,4,5).limit(3).forEach(System.out::println);
        Set<String> stringSet = Stream.of("apple", "banana", "orange", "waltermaleon", "grape")
                .collect(Collectors.toSet()); //收集的结果就是set
        stringSet.forEach(e->System.out.println(e));
        Stream.of("apple", "banana", "orange", "waltermaleon", "grape")
                .collect(Collectors.toSet()).forEach(System.out::println);
        Long count=Stream.of("apple", "banana", "orange", "waltermaleon", "grape").count();
        System.out.println(count);

        Optional<String> stringOptional = Stream.of("apple", "banana", "orange", "waltermaleon", "grape")
                .findFirst();
        stringOptional.ifPresent(System.out::println);
        int sum=Stream.of(1,2,3,4,5).reduce(0,(e1,e2) -> e1+e2);
        int sum1 = Stream.of(1,2,3,4,5).reduce(0, Integer::sum);
        int sum2 = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> squareNums = nums.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());

        Supplier<Integer>  random = (new Random())::nextInt;
        Stream.generate(random).limit(10).forEach(System.out::println);*/

        //     Stream.iterate(0,n -> n+3).limit(10).forEach(System.out::println);

        System.out.println( Long.parseLong("90" + System.currentTimeMillis() + (int) (Math.random() * 1000)));
    }
}

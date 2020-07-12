package com.ttt.core.test;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/4/24 下午7:21
 */
public class FuncationInterTest {
    public static void main(String[] args) {
     /*   System.out.print("test Consumer---");
        testConsumer(1,x -> System.out.println("first:"+x));

        System.out.print("test ConsumerAndThen----");
        testConsumerAndThen(1,x -> System.out.print("first:"+x));

        //测试for each
        Stream<Integer> s = Stream.of(1,2,3,4);
        s.forEach(x -> System.out.print(x+","));
        System.out.println();

        //test with exception
        System.out.print("test ConsumerAndThen with exception----");
        testConsumerAndThen(1,x -> {
            System.out.println("first:"+x);
            throw new NullPointerException();
        });

        System.out.println(testSupplier(() -> new Random(100).nextInt()));
        Supplier<Integer> s = () -> new Random(100).nextInt();
        System.out.println(s.get());*/

      //  testPredicate(8,(i) -> i>5,(i) -> i<10);
     //   testFunction();

        Function<String,String> f=new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s+"_";
            }
        };
        Optional<String> username = Optional.ofNullable("1").filter(s -> s.contains("1")).map(f);

      //  System.out.println(username.get());
        Function<Integer,String> f1= String::valueOf;
        System.out.println(f1.apply(1));
    }

    public static void testConsumer(Integer i,Consumer<Integer> consumer){
        consumer.accept(i);
    }

    public static void testConsumerAndThen(Integer i,Consumer<Integer> consumer){
        Consumer<Integer> c = x -> System.out.println("and then: "+ i);
        consumer.andThen(c).accept(1);
    }

    public static  Integer  testSupplier(Supplier<Integer> s){
        return s.get();
    }

    public static void testPredicate(Integer i,Predicate<Integer> predicate,Predicate<Integer> predicate2){
        System.out.println(predicate.test(i));
        System.out.println(predicate.negate().test(i));
        System.out.println(predicate.and(predicate2).test(i));
        System.out.println(predicate.or(predicate2).test(i));
    }

    public static void testFunction(){
        //String 转成 Integer
        String s ="aaaaaaa";
        Function<String,Integer> f1=(i) -> i.length();
        System.out.println(f1.apply(s));
        Integer t=1;
        Function<Integer,String> f2=(i) -> i+"_";
        System.out.println(f1.compose(f2).apply(t));
        System.out.println(f1.andThen(f2).apply(s));
    }
}

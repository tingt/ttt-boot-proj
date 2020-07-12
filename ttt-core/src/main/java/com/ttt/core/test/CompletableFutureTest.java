package com.ttt.core.test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/5/9 下午7:30
 */
public class CompletableFutureTest {
    public static void main(String[] args) {
        //    testFuture();

        testCompletableFuture();
    }

    public static void testFuture(){
        ExecutorService executor= Executors.newSingleThreadExecutor();
        FutureTask<Integer> task=new FutureTask<>(() -> new Random().nextInt());
        //通过线程池管理
        executor.submit(task);

        //通过thread启动线程
        //     new Thread(task).start();

        try{
            System.out.println(task.get());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void testCompletableFuture(){
        String result= CompletableFuture.supplyAsync(()-> "hello ").thenApplyAsync(v -> v+" world").join();
        System.out.println(result);
    }
}


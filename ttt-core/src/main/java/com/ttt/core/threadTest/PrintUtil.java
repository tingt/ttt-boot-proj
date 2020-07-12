package com.ttt.core.threadTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : tutingting
 * @description: 循环打印0-100
 * @date : 2020/7/12 下午6:00
 */
public class PrintUtil {
    public volatile  static int i =1;
    public final static int  max=10;
    public static Object  lock = new Object();

    public volatile static boolean flag=false;
    public static AtomicInteger num = new AtomicInteger();
    public static CountDownLatch latch = new CountDownLatch(2);

    public void print(){
        ExecutorService executors = Executors.newFixedThreadPool(2);
        executors.submit(new Runnable() {
            @Override
            public void run() {
                    for(;i<=max;){
                        if(i%2==0) {
                            System.out.print(i+",");
                            i++;
                        }
                    }
                }
        });
        executors.submit(() -> {
                for(;i<=max;){
                    if(i%2==1) {
                        System.out.print(i+",");
                        i++;
                    }
                }
        });
    }

    public void print2(){
        ExecutorService executors = Executors.newFixedThreadPool(2);
        executors.submit(()->  {
            while(i<=max){
                synchronized (lock){
                    if(i%2 == 0){
                        System.out.print(i+",");
                        i++;
                        lock.notify();
                    }else{
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
       });

        executors.submit(()->  {
            while(i<=max){
                synchronized (lock){
                    if(i%2 == 1){
                        System.out.print(i+",");
                        i++;
                        lock.notify();
                    }else{
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        PrintUtil p =new PrintUtil();
   //     p.print();

        Thread thread1 = new Thread(()->{
            for(;i<=max;){
                if(i%2==0){
                    System.out.print(i+",");
                    i++;
                }
                latch.countDown();
            }

          /*  while(num.get()<=max){
                if(!flag){
                    System.out.print(num.getAndIncrement()+",");
                }
                latch.countDown();
            }*/
        });
        Thread thread2= new Thread(()->{
            for(;i<=max;){
                if(i%2==1){
                    System.out.print(i+",");
                    i++;
                }
                latch.countDown();
            }
            /*while(num.get()<=max){
                if(flag){
                    System.out.print(num.getAndIncrement()+",");
                }
                latch.countDown();
            }*/
        });
        thread1.start();
        thread2.start();
        latch.await();
    }
}

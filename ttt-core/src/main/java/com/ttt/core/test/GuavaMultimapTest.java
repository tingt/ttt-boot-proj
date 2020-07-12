package com.ttt.core.test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/5/11 下午12:11
 */
public class GuavaMultimapTest {
    public static void main(String[] args){
        ListMultimap<String,String> listMultimap= ArrayListMultimap.create();
        listMultimap.put("fruit","apple");
        listMultimap.put("fruit","pear");
        listMultimap.put("fruit","banana");
        listMultimap.put("vegetable","potato");
        listMultimap.put("vegetable","tomato");

        System.out.println("listMulitMap.size = "+listMultimap.size());
        List<String> fruitList=listMultimap.get("fruit");
        System.out.println("fruit:"+fruitList);

        fruitList.add("orange");
        System.out.println("listMulitMap.size = "+listMultimap.size());

        System.out.println("key不存在时返回空list");
        System.out.println(listMultimap.get("test"));

        System.out.println(listMultimap.keySet());
        System.out.println(listMultimap.values());

        System.out.println("输出所有key-value");
        for(Map.Entry<String,String> map: listMultimap.entries()){
            System.out.println(map.getKey()+":"+map.getValue());
        }

        System.out.println("转成asMap输出");
        Map<String,Collection<String>> asMap=listMultimap.asMap();
        for(Map.Entry<String,Collection<String>> entry:asMap.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}

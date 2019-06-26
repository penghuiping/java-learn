package collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/6/26 11:24
 * @description:
 */
public class CollectionExamples {

    public static void main(String[] args) {
        var list0 = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        for (var i : list0) {
            System.out.println(i);
        }
        var list1 = list0.stream().sorted((o1, o2) -> o2 - o1).collect(Collectors.toUnmodifiableList());
        System.out.println(list1);


        var set0 = Set.of(1,3,2,4,5,7);
        System.out.println(set0);
        var set1 = set0.stream().sorted().collect(Collectors.toSet());
        System.out.println(set1);
        set1.add(1);
        set1.add(2);
        System.out.println(set1);


        var map0 = Map.of("name","jack","age",12);
        System.out.println(map0);
        var map1 = new HashMap<>(map0);
        map1.put("height",170);
        System.out.println(map1);
    }
}

package c7_thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author penghuiping
 * @date 2020/5/3 09:20
 */
public class CompletableFutureTest {

    public static String getWeatherReport() {
        try {
            Thread.sleep(1000);
            System.out.printf("thread:%s%n", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "[{\"city\":\"上海\",\"weather\":\"晴天\"},{\"city\":\"北京\",\"weather\":\"雨天\"}]";
    }

    public static String getTemperatureReport() {
        try {
            Thread.sleep(1000);
            System.out.printf("thread:%s%n", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "[{\"city\":\"上海\",\"temperature\":\"21°\"},{\"city\":\"北京\",\"temperature\":\"17°\"}]";
    }


    /**
     * 方式一
     *
     * @return
     */
    public static Future<String> getWeatherReportAsync() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                String weather = getWeatherReport();
                completableFuture.complete(weather);
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }

        }).start();
        return completableFuture;
    }

    /**
     * 方式二
     * 这种方式与方式一有一样的效果，但个更加简单
     *
     * @return
     */
    public static Future<String> getWeatherReportAsync1() {
        return CompletableFuture.supplyAsync(() -> getWeatherReport());
    }


    @Test
    public void test() throws Exception {
        Future<String> weather = getWeatherReportAsync();
        System.out.println(weather.get());

        Future<String> weather1 = getWeatherReportAsync1();
        System.out.println(weather1.get());
    }

    /**
     * 使用thenCompose,来保证执行顺序
     * 下面第一步、第二步、第三步都是需要保持顺序的。
     * <p>
     * 所以耗时2秒多
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        long start = System.currentTimeMillis();
        ObjectMapper objectMapper = new ObjectMapper();
        CompletableFuture<List<Map<String, String>>> tmp = CompletableFuture
                //第一步获取天气
                .supplyAsync(() -> getWeatherReport())
                //第二步处理天气格式数据
                .thenApply(s -> {
                    List<Map<String, String>> result = null;
                    try {
                        result = objectMapper.readValue(s, new TypeReference<>() {
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return result;
                    //第三步获取温度数据，处理返回
                }).thenCompose(list -> CompletableFuture.supplyAsync(() -> {
                    String rs1 = getTemperatureReport();
                    try {
                        final List<Map<String, String>> list1 = objectMapper.readValue(rs1, new TypeReference<>() {
                        });
                        var rs2 = list.stream().map(map -> {
                            var tmp2 = list1.stream().filter(map1 -> map.get("city").equals(map1.get("city"))).findAny();
                            if (tmp2.isPresent()) {
                                var tmp1 = tmp2.get().get("temperature");
                                map.put("temperature", tmp1);
                                return map;
                            } else {
                                return null;
                            }
                        }).collect(Collectors.toList());
                        return rs2;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }));
        System.out.println(tmp.get());
        System.out.format("耗时:%sms", System.currentTimeMillis() - start);
    }

    /**
     * 使用thenCombine,可以使得两个没有依赖的步骤并行执行，节约时间
     * <p>
     * 所以耗时1秒多
     */
    @Test
    public void test3() throws Exception {
        long start = System.currentTimeMillis();
        ObjectMapper objectMapper = new ObjectMapper();
        CompletableFuture<Void> tmp = CompletableFuture
                .supplyAsync(() -> getWeatherReport())
                //使用thenCombine,可以使得两个没有依赖的步骤并行执行，节约时间
                .thenCombine(CompletableFuture.supplyAsync(() -> getTemperatureReport()), (w, t) -> {
                    try {
                        List<Map<String, String>> wList = objectMapper.readValue(w, new TypeReference<>() {
                        });

                        List<Map<String, String>> tList = objectMapper.readValue(t, new TypeReference<>() {
                        });
                        var rs2 = wList.stream().map(map -> {
                            var tmp2 = tList.stream().filter(map1 -> map.get("city").equals(map1.get("city"))).findAny();
                            if (tmp2.isPresent()) {
                                var tmp1 = tmp2.get().get("temperature");
                                map.put("temperature", tmp1);
                                return map;
                            } else {
                                return null;
                            }
                        }).collect(Collectors.toList());
                        return rs2;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //thenAccept处理完成后的回调方法
                }).thenAccept(maps -> {
                    System.out.println(maps);
                });
        tmp.get();
        System.out.format("耗时:%sms", System.currentTimeMillis() - start);
    }


}

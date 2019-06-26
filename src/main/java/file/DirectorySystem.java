package file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * @author: penghuiping
 * @date: 2019/6/25 18:15
 * @description:
 */
public class DirectorySystem {

    public static void main(String[] args) throws Exception {
        //展示目录结构
        Path path = Paths.get("/Users/penghuiping/Desktop");
        Files.list(path).forEach(System.out::println);

        System.out.println("====================================");

        //只列出文件
        Files.list(path).filter(path1 -> !Files.isDirectory(path1)).forEach(System.out::println);

        var tmp = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));

        //创建文件夹
        Files.createDirectory(Paths.get("/Users/penghuiping/Desktop/新建文件夹"), tmp);

        //判断文件夹是否存在
        if (Files.exists(Paths.get("/Users/penghuiping/Desktop/新建文件夹"))) {
            System.out.println("文件夹成功创建，已存在");
        }
    }
}

package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * @author: penghuiping
 * @date: 2019/6/25 16:48
 * @description:
 */
public class FileSystem {

    public static void main(String[] args) {
        Path file1 = Paths.get("/Users/penghuiping/Desktop/joinsoft-docker", "a.txt");

        //判断文件是否存在
        if (!Files.exists(file1)) {
            try {
                //创建文件
                Files.createFile(file1, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-x---")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //向文件写入数据
        try (BufferedWriter writer = Files.newBufferedWriter(file1, StandardOpenOption.WRITE);) {
            writer.write("hello world\n\r");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //从文件中读取文件
        try (BufferedReader reader = Files.newBufferedReader(file1)) {
            char[] buf = new char[512];
            int index = 0;
            while ((index = reader.read(buf, 0, 512)) > 0) {
                System.out.println(new String(buf, 0, index));
            }
        } catch (IOException e) {

        }


    }


}

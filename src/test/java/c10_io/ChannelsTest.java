package c10_io;

import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author: penghuiping
 * @date: 2019/8/8 18:00
 * @description:
 */
public class ChannelsTest {


    @Test
    public void readChannel() throws Exception {
        Path file1 = Paths.get("/Users/penghuiping/Desktop/joinsoft-docker", "a.txt");

        try (InputStream inputStream = Files.newInputStream(file1, StandardOpenOption.READ)) {
            ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (true) {
                int size = readableByteChannel.read(byteBuffer);
                byteBuffer.flip();
                byte[] buff = new byte[byteBuffer.limit()];
                byteBuffer.get(buff, byteBuffer.position(), byteBuffer.limit());
                System.out.println(new String(buff));
                byteBuffer.clear();
                if (size <= 0) {
                    break;
                }
            }
        }
    }

    @Test
    public void writeChannel() throws Exception {
        Path file = Paths.get("/Users/penghuiping/Desktop/joinsoft-docker", "b.txt");
        try (OutputStream outputStream = Files.newOutputStream(file, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)) {
            WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);
            String content = "写入一些内容";
            ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes());
            writableByteChannel.write(byteBuffer);
        }
    }

    @Test
    public void zeroCopy() throws Exception {
        Path src = Paths.get("/Users/penghuiping/Desktop/joinsoft-docker", "a.txt");
        Path dst = Paths.get("/Users/penghuiping/Desktop/joinsoft-docker", "b.txt");
        try (FileChannel srcChannel = FileChannel.open(src);
             FileChannel dstChannel = FileChannel.open(dst,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
        ) {
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
        }
    }


}

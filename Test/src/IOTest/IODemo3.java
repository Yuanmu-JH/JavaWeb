package IOTest;

import java.io.*;

public class IODemo3{
    public static void main(String[] args) {
        copyFile3();
    }

    private static void copyFile() {
        // 处理文本文件, 需要使用字符流.
        try (FileReader fileReader = new FileReader("d:/test_dir/test.txt");
             FileWriter fileWriter = new FileWriter("d:/test_dir/test2.txt")) {
            char[] buffer = new char[1024];
            int len = -1;
            while ((len = fileReader.read(buffer)) != -1) {
                fileWriter.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile2() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("d:/test_dir/test.txt"));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("d:/test_dir/test2.txt"))) {
            char[] buffer = new char[1024];
            int len = -1;
            while ((len = bufferedReader.read(buffer)) != -1) {
                bufferedWriter.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile3() {
        // 带缓冲区的字符流中有一种特殊的用法, 按行读取.
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("d:/test_dir/test.txt"));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("d:/test_dir/test2.txt"))) {
            String line = "";
            // readLine 表示读一行. 读到换行符为止. 如果读取文件完毕, 就会返回 null
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package IOTest;

import sun.nio.ch.FileKey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IODemo {
    public static void main(String[] args) throws IOException {
        copyFile1("E:/School/Test/山.jpg", "E:/School/Test/山2.jpg");
    }
    private static void copyFile1(String srcPath, String destPath) throws IOException {
        /*
        打开文件创建InputStream/OutputStream才能进行读写
        读取文件内容，并将读取内容写入despath对应文件中
        */
        /*
        版本一存在的问题在于如果出现了IO异常则关闭不会发生，从而导致文件资源泄露
         */
        FileInputStream fileInputStream = new FileInputStream(srcPath);
        FileOutputStream fileOutputStream = new FileOutputStream(destPath); //参数可以是字符串路径也可以是File对象
        // 来进行实例化
        byte[] buffer = new byte[1024]; //单次读取的内容是存在上限（缓冲区长度），要是想把这个文件都读完，需要搭配
        // 循环来使用
        int len = -1;
        //若读到的len为-1则说明读取完毕
        while ((len = fileInputStream.read(buffer)) != -1) {
            //读取成功
            //因为读取到的len值不一定和程序一样长，所以到len结束
            fileOutputStream.write(buffer, 0, len);
            fileInputStream.close();
            fileOutputStream.close();
        }
    }


    /*
        代码2存在问题：不美观 难读
     */
        private static void copyFile2(String srcPath, String destPath){
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                fileInputStream = new FileInputStream(srcPath);
                fileOutputStream = new FileOutputStream(destPath); //参数可以是字符串路径也可以是File对象
                // 来进行实例化
                byte[] buffer1 = new byte[1024]; //单次读取的内容是存在上限（缓冲区长度），要是想把这个文件都读完，需要搭配
                // 循环来使用
                int len1 = -1;
                //若读到的len为-1则说明读取完毕
                while ((len1 = fileInputStream.read(buffer1)) != -1) {
                    //读取成功
                    //因为读取到的len值不一定和程序一样长，所以到len结束
                    fileOutputStream.write(buffer1, 0, len1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void copyFile3(){
            /*
            当代码写成这个样子后就不需要显示调用close,try语句会在代码执行完毕后自动调用close方法
             */
            try(  FileInputStream fileInputStream = new FileInputStream("E:/School/Test/山.jpg");
                  FileOutputStream fileOutputStream = new FileOutputStream("E:/School/Test/山2.jpg")) {
                byte[] buffer = new byte[1024];
                int len = -1;
                while((len = fileInputStream.read(buffer))!= -1){
                    fileOutputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


package IOTest;

import java.io.*;

//利用缓冲区来进行拷贝
public class IODemo2 {
    public static void main(String[] args) throws IOException {
        copyFile();
    }
    /*
    若出现异常则close无法调用
     */
    private static void copyFile() throws IOException {
        //要创建BufferedInputStream和BufferedOutputStream实例要先创建FileInputStream和FileOutputStream
        FileInputStream fileInputStream = new FileInputStream("E:/School/Test/山.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("E:/School/Test/山2.jpg");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        byte[] buffer = new byte[1024];
        int length = -1;
        while((length = bufferedInputStream.read(buffer))!= -1){
            bufferedOutputStream.write(buffer,0,length);
        }
        //此时涉及了4个流对象，调用下面的一组close，就会自动关闭内部包含的FileInputStream和FileOutputStream
        bufferedInputStream.close();
        bufferedOutputStream.close();
    }

    private static void copyFile1(){
        try(  BufferedInputStream bufferedInputStream =
                      new BufferedInputStream(new FileInputStream("E:/School/Test/山.jpg"));
              BufferedOutputStream bufferedOutputStream =
                      new BufferedOutputStream(new FileOutputStream ("E:/School/Test/山2.jpg"))){
            byte[] buffer = new byte[1024];
            int length = -1;
            while((length = bufferedInputStream.read(buffer))!= -1){
                bufferedOutputStream.write(buffer,0,length);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

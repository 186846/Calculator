package 第二周作业;

public class BracketsException extends Exception{
    //默认构造器
    public BracketsException() {
    }
    //带有详细信息的构造器，信息存储在message中
    public BracketsException(String message) {
        super(message);
    }
}

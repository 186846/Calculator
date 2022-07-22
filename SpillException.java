package 第二周作业;

public class SpillException extends Exception{
    //默认构造器
    public SpillException() {
    }
    //带有详细信息的构造器，信息存储在message中
    public SpillException(String message) {
        super(message);
    }
}

package Second Work;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

//判断你输入的字符串的括号是否对称，不对称的话，将终止程序。
class Symmetry{
    private String str;
    public String getStr(){
       return str;
    }
    public void setStr(String str) throws BracketsException {
        //实例化 Stack
        Stack<String> stack = new Stack<>();
        //假设修正法
        boolean flag = true;//假设是匹配的
        //拆分字符串获取字符
        for (int i = 0; i <str.length(); i++) {
            char c = str.charAt(i);
            if (c == '{') {
                stack.push("}");
            }
            if (c == '[') {
                stack.push("]");
            }
            if (c == '(') {
                stack.push(")");
            }
            //判断符号是否匹配
            if (c == '}' || c == ']' || c == ')') {
                if (stack.empty()) {
                    //修正处理
                    flag = false;
                    break;
                }
                String x = stack.pop();
                if (x.charAt(0) != c) {
                    //修正处理
                    flag = false;
                    break;
                }
            }
        }
        if (!stack.empty()) {
            //修正处理
            flag = false;
        }
        if (!flag){
            throw new BracketsException("你输入的算式的括号不对称！");
        }
        this.str = str;
    }
}
//判断结果是否溢出的类
class OrSpill{
    private double number;
    private final double num1 = 1000000000.0;
    private final double num2 = -1000000000.0;

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) throws SpillException {
        if (number>num1 || number<num2){
            throw new SpillException("你所计算的结果超过了计算范围，重新输一次吧！");
        }
        this.number = number;
    }
}

public class Calculator {
    public static void main(String[] args) {
        System.out.print("Please input: ");
        Scanner in = new Scanner(System.in);
        String Str = in.nextLine();
        //去除字符串前后空格
        String str = Str.trim();
        Symmetry c = new Symmetry();
        //第一步。判断括号是否对称
        //判断这个字符串的括号是否对称
        //如果不对称的话，程序直接报错，且终止程序。
        try {
            c.setStr(str);
        }catch (BracketsException b){
            b.printStackTrace();
        }


        //第二步。将字符串转化为list结构
        //将字符串转化为list
        List<String> exList = StrToList(str);


        //第三步。将list按照一定的顺序入栈，方便下一步计算
        List<String> orderedList = changeToOrder(exList);


        //第四步。出队依次进行运算。
        double result = calculator(orderedList);

        //第五步，判断result值是否溢出
        //规定result的结果不能超过10位，不要问我为什么，因为计算器是这样搞的。
        OrSpill num = new OrSpill();
        try {
            num.setNumber(result);
        }catch (SpillException spillException){
            spillException.printStackTrace();
        }


        //第六步。输出结果
        System.out.printf(str+"=%.2f\n",result);
    }



    //第二步。将字符串转化为list结构
    private static List<String> StrToList(String str) {
        int index = 0;
        List<String> list = new ArrayList<>();
        while (index<str.length()){
            char ch = str.charAt(index);//获取str元素的ascll码值
            //这些字符包括了括号和操作符(+,-,*,/)
            if(ch!=46 && (ch <= 47 || ch >= 58)){
                //是操作符，直接添加至list中
                index ++ ;
                list.add(ch+"");
            }else{
                //是数字,判断多位数的情况
                //其中有能表示小数的字符串
                //ascll码值的48到57为0到9的字符，而“.”小数点的ascll码值为46
                String str1 = "";
                while (index < str.length() && (str.charAt(index) >47 && str.charAt(index) < 58 || str.charAt(index)==46)){
                    str1+= str.charAt(index);
                    index ++;
                }
                list.add(str1);
            }
        }
        return list;
    }


    //第三步。将list按照一定的顺序入栈，方便下一步计算
    private static List<String> changeToOrder(List<String> exList) {
        //创建一个栈用于保存操作符
        Stack<String> operatorStack = new Stack<>();
        //创建一个list保存有序的字符顺序(后缀表达式子：单行逆波兰表达式）
        //这个我是在csdn上看到的。
        List<String> orderedList = new ArrayList<>();
        //接下来是具体操作
        for (String value:exList) {
            //按顺序将操作符填入list（两数一操作符号）(总思想)
            if (isOperator(value)) {
                //判断operatorStack是否为空,还有就是优先级(主要是对加减乘除进行判断，对小括号不进行判断)，决定了入队顺序
                if (operatorStack.empty() || "(".equals(operatorStack.peek()) || priority(value) > priority(operatorStack.peek())) {
                    //操作符暂时入栈
                    operatorStack.push(value);
                } else {
                    //否则将栈中元素出栈如队，直到遇到大于当前操作符或者遇到左括号时
                    while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                        if (priority(value) <= priority(operatorStack.peek())) {
                            orderedList.add(operatorStack.pop());
                        }
                    }
                    //当前操作符压栈
                    operatorStack.push(value);
                }
            } //数字直接入队
            else if (isNum(value)) {
                orderedList.add(value);
            }
            //匹配“（”
            else if ("(".equals(value)){
                //"("压栈
                operatorStack.add(value);
            }
            //匹配")"，进行出栈操作
            else if (")".equals(value)){
                //"("是在栈的最低层，上面可能有加减乘除四种符号
                while (!operatorStack.isEmpty()){
                    if("(".equals(operatorStack.peek())){
                        //此时小括号的部分结束判断
                        operatorStack.pop();
                        break;
                    }
                    //这是还有除"("外还有其他操作符
                    else {
                        orderedList.add(operatorStack.pop());
                    }
                }
            }else if(".".equals(value)){
                //System.out.print('a');
                orderedList.add(value);
            }
            else {
                throw new RuntimeException("有非法字符！");
            }
        }
        //循环完毕，如果操作符栈中元素不为空，将栈中元素出栈入队
        while (!operatorStack.isEmpty()){
            orderedList.add(operatorStack.pop());
        }
        return orderedList;
    }

    //
    private static boolean isNum(String value) {
        //这里正则匹配数
        return value.matches("^([0-9]{1,}[.][0-9]*)$") || value.matches("^([0-9]{1,})$");
    }

    //判断是否为从操作符号
    private static boolean isOperator(String value) {
        //这里用到了正则
       return value.equals("+")||value.equals("-")||value.equals("*")||value.equals("/");
    }

    //操作符优先级判断
    public static int priority(String operator){
        if (operator.equals("*")||operator.equals("/")){
            return 1;
        }else if (operator.equals("+")||operator.equals("-")){
            return 0;
        }
        return -1;
    }




    //第四步。出队依次进行运算
    public static double calculator(List<String> list){
        //临时定义一个栈，用来储存数字和计算出来的数字
        Stack<Double> NumStack = new Stack<>();
        for (String item:list){
            if (item.matches("^([0-9]{1,}[.][0-9]*)$") || item.matches("^([0-9]{1,})$")){
                //是数字
                NumStack.push(Double.parseDouble(item));
            }//不是数字的话就是操作符
            else{
                //取出栈区的栈顶的两个数
                double n1 = NumStack.pop();
                double n2 = NumStack.pop();
                //定义一个double型的变量，来记录两个数的运算结果
                double res = switch (item) {
                    case "+" -> n1 + n2;
                    case "-" -> n1 - n2;
                    case "*" -> n1 * n2;
                    case "/" -> n1 / n2;
                    default -> throw new RuntimeException("运算符错误！");
                };
                NumStack.push(res);
            }
        }
        //返回NumStack里面最后剩下了的数值，这个数值就我们所求字符串的结果
        return NumStack.pop();
    }
}

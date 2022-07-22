package �ڶ�����ҵ;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

//�ж���������ַ����������Ƿ�Գƣ����ԳƵĻ�������ֹ����
class Symmetry{
    private String str;
    public String getStr(){
       return str;
    }
    public void setStr(String str) throws BracketsException {
        //ʵ���� Stack
        Stack<String> stack = new Stack<>();
        //����������
        boolean flag = true;//������ƥ���
        //����ַ�����ȡ�ַ�
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
            //�жϷ����Ƿ�ƥ��
            if (c == '}' || c == ']' || c == ')') {
                if (stack.empty()) {
                    //��������
                    flag = false;
                    break;
                }
                String x = stack.pop();
                if (x.charAt(0) != c) {
                    //��������
                    flag = false;
                    break;
                }
            }
        }
        if (!stack.empty()) {
            //��������
            flag = false;
        }
        if (!flag){
            throw new BracketsException("���������ʽ�����Ų��Գƣ�");
        }
        this.str = str;
    }
}
//�жϽ���Ƿ��������
class OrSpill{
    private double number;
    private final double num1 = 1000000000.0;
    private final double num2 = -1000000000.0;

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) throws SpillException {
        if (number>num1 || number<num2){
            throw new SpillException("��������Ľ�������˼��㷶Χ��������һ�ΰɣ�");
        }
        this.number = number;
    }
}

public class Calculator {
    public static void main(String[] args) {
        System.out.print("Please input: ");
        Scanner in = new Scanner(System.in);
        String Str = in.nextLine();
        //ȥ���ַ���ǰ��ո�
        String str = Str.trim();
        Symmetry c = new Symmetry();
        //��һ�����ж������Ƿ�Գ�
        //�ж�����ַ����������Ƿ�Գ�
        //������ԳƵĻ�������ֱ�ӱ�������ֹ����
        try {
            c.setStr(str);
        }catch (BracketsException b){
            b.printStackTrace();
        }


        //�ڶ��������ַ���ת��Ϊlist�ṹ
        //���ַ���ת��Ϊlist
        List<String> exList = StrToList(str);


        //����������list����һ����˳����ջ��������һ������
        List<String> orderedList = changeToOrder(exList);


        //���Ĳ����������ν������㡣
        double result = calculator(orderedList);

        //���岽���ж�resultֵ�Ƿ����
        //�涨result�Ľ�����ܳ���10λ����Ҫ����Ϊʲô����Ϊ��������������ġ�
        OrSpill num = new OrSpill();
        try {
            num.setNumber(result);
        }catch (SpillException spillException){
            spillException.printStackTrace();
        }


        //��������������
        System.out.printf(str+"=%.2f\n",result);
    }



    //�ڶ��������ַ���ת��Ϊlist�ṹ
    private static List<String> StrToList(String str) {
        int index = 0;
        List<String> list = new ArrayList<>();
        while (index<str.length()){
            char ch = str.charAt(index);//��ȡstrԪ�ص�ascll��ֵ
            //��Щ�ַ����������źͲ�����(+,-,*,/)
            if(ch!=46 && (ch <= 47 || ch >= 58)){
                //�ǲ�������ֱ�������list��
                index ++ ;
                list.add(ch+"");
            }else{
                //������,�ж϶�λ�������
                //�������ܱ�ʾС�����ַ���
                //ascll��ֵ��48��57Ϊ0��9���ַ�������.��С�����ascll��ֵΪ46
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


    //����������list����һ����˳����ջ��������һ������
    private static List<String> changeToOrder(List<String> exList) {
        //����һ��ջ���ڱ��������
        Stack<String> operatorStack = new Stack<>();
        //����һ��list����������ַ�˳��(��׺���ʽ�ӣ������沨�����ʽ��
        //���������csdn�Ͽ����ġ�
        List<String> orderedList = new ArrayList<>();
        //�������Ǿ������
        for (String value:exList) {
            //��˳�򽫲���������list������һ�������ţ�(��˼��)
            if (isOperator(value)) {
                //�ж�operatorStack�Ƿ�Ϊ��,���о������ȼ�(��Ҫ�ǶԼӼ��˳������жϣ���С���Ų������ж�)�����������˳��
                if (operatorStack.empty() || "(".equals(operatorStack.peek()) || priority(value) > priority(operatorStack.peek())) {
                    //��������ʱ��ջ
                    operatorStack.push(value);
                } else {
                    //����ջ��Ԫ�س�ջ��ӣ�ֱ���������ڵ�ǰ��������������������ʱ
                    while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                        if (priority(value) <= priority(operatorStack.peek())) {
                            orderedList.add(operatorStack.pop());
                        }
                    }
                    //��ǰ������ѹջ
                    operatorStack.push(value);
                }
            } //����ֱ�����
            else if (isNum(value)) {
                orderedList.add(value);
            }
            //ƥ�䡰����
            else if ("(".equals(value)){
                //"("ѹջ
                operatorStack.add(value);
            }
            //ƥ��")"�����г�ջ����
            else if (")".equals(value)){
                //"("����ջ����Ͳ㣬��������мӼ��˳����ַ���
                while (!operatorStack.isEmpty()){
                    if("(".equals(operatorStack.peek())){
                        //��ʱС���ŵĲ��ֽ����ж�
                        operatorStack.pop();
                        break;
                    }
                    //���ǻ��г�"("�⻹������������
                    else {
                        orderedList.add(operatorStack.pop());
                    }
                }
            }else if(".".equals(value)){
                //System.out.print('a');
                orderedList.add(value);
            }
            else {
                throw new RuntimeException("�зǷ��ַ���");
            }
        }
        //ѭ����ϣ����������ջ��Ԫ�ز�Ϊ�գ���ջ��Ԫ�س�ջ���
        while (!operatorStack.isEmpty()){
            orderedList.add(operatorStack.pop());
        }
        return orderedList;
    }

    //
    private static boolean isNum(String value) {
        //��������ƥ����
        return value.matches("^([0-9]{1,}[.][0-9]*)$") || value.matches("^([0-9]{1,})$");
    }

    //�ж��Ƿ�Ϊ�Ӳ�������
    private static boolean isOperator(String value) {
        //�����õ�������
       return value.equals("+")||value.equals("-")||value.equals("*")||value.equals("/");
    }

    //���������ȼ��ж�
    public static int priority(String operator){
        if (operator.equals("*")||operator.equals("/")){
            return 1;
        }else if (operator.equals("+")||operator.equals("-")){
            return 0;
        }
        return -1;
    }




    //���Ĳ����������ν�������
    public static double calculator(List<String> list){
        //��ʱ����һ��ջ�������������ֺͼ������������
        Stack<Double> NumStack = new Stack<>();
        for (String item:list){
            if (item.matches("^([0-9]{1,}[.][0-9]*)$") || item.matches("^([0-9]{1,})$")){
                //������
                NumStack.push(Double.parseDouble(item));
            }//�������ֵĻ����ǲ�����
            else{
                //ȡ��ջ����ջ����������
                double n1 = NumStack.pop();
                double n2 = NumStack.pop();
                //����һ��double�͵ı���������¼��������������
                double res = switch (item) {
                    case "+" -> n1 + n2;
                    case "-" -> n1 - n2;
                    case "*" -> n1 * n2;
                    case "/" -> n1 / n2;
                    default -> throw new RuntimeException("���������");
                };
                NumStack.push(res);
            }
        }
        //����NumStack�������ʣ���˵���ֵ�������ֵ�����������ַ����Ľ��
        return NumStack.pop();
    }
}
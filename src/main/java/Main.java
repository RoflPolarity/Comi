import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int n,a, max,min = Integer.MAX_VALUE, stop;
    static double mut;
    static ArrayList<Integer> minArr = new ArrayList<>();
    static class rebro{
        node a,b;
        int len;
        public rebro(node a,node b, int len){
            this.a = a;
            this.b = b;
            this.len = len;
        }

        @Override
        public String toString() {
            return "rebro{" +
                    "a=" + a +
                    ", b=" + b +
                    ", len=" + len +
                    '}';
        }
    }

    static class node{
        int id;
        ArrayList<rebro> rebra = new ArrayList<>();
        public node(int id){
            this.id = id;
        }
        public void addRebro(node b, int len){
            rebra.add(new rebro(this,b,len));
        }

        public int getLen(node b){
            for (Main.rebro rebro : rebra) {
                if (rebro.b == b) return rebro.len;
            }
            return -1;
        }
        public int getLenSumm(){
            AtomicInteger summ = new AtomicInteger();
            rebra.forEach(x-> summ.addAndGet(x.len));
            return summ.get();
        }
        @Override
        public String toString() {
            return ""+id;
        }
    }

    public static void main(String[] args) {
        System.out.println("Введите количество городов");
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        System.out.println("Введите размер начальной популяции");
        a = scanner.nextInt();
        System.out.println("Введите верхнюю границу генерации случайной длины дороги");
        max = scanner.nextInt();
        System.out.println("Укажите вероятность мутации");
        mut = scanner.nextDouble();
        System.out.println("Введите параметр остановки");
        stop = scanner.nextInt();

        ArrayList<node> temp = new ArrayList<>();
        for (int j = 1; j < n+1; j++){temp.add(new node(j));}
        for (int i = 0; i < temp.size(); i++){
            for (int j = 1; j < temp.size()-1; j++) {
                temp.get(i).addRebro(temp.get(j), ThreadLocalRandom.current().nextInt(1,max));
            }
        }
        Set<ArrayList<node>> set = new HashSet<>();
        printAllRecursive(temp,set,a);
        ArrayList<ArrayList<node>> first = new ArrayList<>(set);


        Random random = new Random();
        int counter = 1, count = 0;
        System.out.println(first);
        System.out.println();
        while (true){
            System.out.println("Round " + counter);
            ArrayList<node> father = first.get(random.nextInt(first.size())), mother = first.get(random.nextInt(first.size()));
            while (father==mother) father = first.get(random.nextInt(first.size()));
            System.out.println("Father = " + father);
            System.out.println("Mother = " + mother);
            ArrayList<node> child = cycledCrossover(father,mother);
            System.out.println("Child = " + child);
            first.add(child);
            System.out.println(first);
            getRedux(first);
            System.out.println(first);
            counter++;
            if (minArr.size()==first.size()){
                if (check()){
                    System.out.println("Кротчайший путь при заданных параметрах равен " + minArr.get(0));
                    break;
                }
            }
        }
    }
    public static boolean check(){
        int count = 0;
        int min = minArr.get(0);
        for(Integer num : minArr){
            if (num==min) count++;
        }
        return count == minArr.size();
    }
    public static void printAllRecursive(ArrayList<node> elements, Set<ArrayList<node>> set, int size) {
        while (set.size()!=size){
            ArrayList<node> temp = new ArrayList<>(elements);
            Collections.shuffle(temp);
            set.add(temp);
        }
    }
    public static boolean getMutation(double mut){return Math.random() <= mut;}
    public static ArrayList<node> mutation(ArrayList<node>child){
        int pos1 = ThreadLocalRandom.current().nextInt(child.size()/4);
        int pos2 = ThreadLocalRandom.current().nextInt(pos1+2,child.size()-1);
        ArrayList<node> reservChild = new ArrayList<>(child);
        List<node> temp = child.subList(pos1+1,pos2+1);
        ArrayList<node> res = new ArrayList<>();
        for(int i = 0; i<=pos1; i++)res.add(child.get(i));
        while (temp.size() != 0) {
                node min = (temp.stream().min((x, y) -> (x.getLen(res.get(res.size() - 1)) < y.getLen(res.get(res.size() - 1))) ? 1 : -1).get());
                res.add(min);
                temp.remove(min);
            }
        res.addAll(reservChild.subList(pos2+1,reservChild.size()));
        return res;
    }
    public static ArrayList<node> cycledCrossover(ArrayList<node> father, ArrayList<node> mother){
        ArrayList<node> child = new ArrayList<>();
        int peek = 0;
        while (child.size()<n){
            child.add(father.get(peek));
            child.add(mother.get(mother.size()-1-peek));
            peek++;
        }
        child.remove(child.size()-1);
        if (getMutation(mut)) {
           child = mutation(child);
        }

        return child;
    }
    public static ArrayList<ArrayList<node>> getRedux(ArrayList<ArrayList<node>> nodes){
        ArrayList<Integer> nums = new ArrayList<>();
        for(ArrayList<node> lst: nodes){
            int summ = 0;
            for(node node : lst){
                summ+=node.getLenSumm();
            }
            nums.add(summ);
        }
        System.out.println(nums);
        System.out.println(nodes.get(nums.indexOf(Collections.max(nums)))+" node for remove");
        minArr.add(Collections.min(nums));
        nodes.remove(nums.indexOf(Collections.max(nums)));
        return nodes;
    }
}

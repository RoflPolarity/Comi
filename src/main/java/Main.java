import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int n,a, max;
    static double mut;
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
            rebra.stream().forEach(x-> summ.addAndGet(x.len));
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

        ArrayList<node> temp = new ArrayList<>();
        for (int j = 1; j < n+1; j++){temp.add(new node(j));}
        for (int i = 0; i < temp.size(); i++){
            for (int j = 1; j < temp.size()-1; j++) {
                temp.get(i).addRebro(temp.get(j),5 + new Random().nextInt(max));
            }
        }
        Set<ArrayList<node>> set = new HashSet<>();
        printAllRecursive(temp,set,a);
        ArrayList<ArrayList<node>> first = new ArrayList<>(set);


        Random random = new Random();
        int counter = 1;
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
            break;
        }





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
        System.out.println(child + " child");
        List<node> temp = child.subList(pos1+1,pos2+1);
        ArrayList<node> res = new ArrayList<>();
        for(int i = 0; i<=pos1; i++)res.add(child.get(i));
        System.out.println(temp);
        while (temp.size() != 0) {
                node min = (temp.stream().min((x, y) -> (x.getLen(res.get(res.size() - 1)) < y.getLen(res.get(res.size() - 1))) ? 1 : -1).get());
                res.add(min);
                temp.remove(min);
            }
        res.addAll(child.subList(pos2,child.size()));
        System.out.println(res + " res");
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
        ArrayList<Integer> res = new ArrayList<>();
        HashMap<Integer,ArrayList<node>> map = new HashMap<>();
        for(ArrayList<node> lst: nodes){
            int summ = 0;
            for(node node : lst){
                summ+=node.getLenSumm();
            }
            System.out.println(lst);
            res.add(summ);
            map.put(summ,lst);
        }
        System.out.println(map);
        System.out.println(map.get(Collections.max(res))+" node for remove");
        nodes.remove(map.get(Collections.max(res)));
        return nodes;
    }
}

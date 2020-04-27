import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int nPerm = Integer.parseInt(args[0]);
        RandomizedQueue<String> rqueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String readedVal = StdIn.readString();
            for (String elem : readedVal.split(" ")) {
                rqueue.enqueue(elem);
            }
        }
        int n = 0;
        for (String permString : rqueue) {
            System.out.print(permString + "\n");
            if (++n == nPerm) {
                break;
            }
        }
    }
}
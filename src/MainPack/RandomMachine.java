package MainPack;

import java.util.ArrayList;
import java.util.Random;

/**
 * RandomMachine stores an array of objects and array of chances that the object
 * will be drawn. Real chance is the chance given while putting object divided by
 * all chances. E.g. after putting into RandomMachine integer 1 with chance 10,
 * integer 2 with chance 5, and integer 3 with chance 25, there is 25% that 1
 * will be drawn, 12.5% that 2, and 62.5% that 3.
 * @author Jaroslaw Pawlak
 */
public class RandomMachine {
    private ArrayList<Object> object;
    private ArrayList<Integer> chance;
    public RandomMachine() {
        this(10);
    }
    /**
     * @param initialCapacity initial capacity of <code>ArrayLists</code>
     * used by the machine
     */
    public RandomMachine(int initialCapacity) {
        object = new ArrayList<Object>(initialCapacity);
        chance = new ArrayList<Integer>();
    }
    public void put(Object object, int chance) {
        if(chance < 1) {
            throw new IllegalArgumentException("illegal number of possibilities: "
                    + chance + ", should be greater or equal then 1");
        }
        this.object.add(object);
        this.chance.add(chance);
    }
    /**
     * Returns random object according to their chances
     * @return random object put into machine
     */
    public Object get() {
        int x = new Random().nextInt(getAllChances());
        int r = 0;
        for(int i = 0; true; i++) {
            if(x < (r += chance.get(i))) {
                return object.get(i);
            }
        }
    }
    /**
     * Returns a sum of all chances used while putting objects into machine
     * @return a sum of all chances
     */
    public int getAllChances() {
        int r = 0;
        for(Integer e : chance) {
            r += e;
        }
        return r;
    }
}
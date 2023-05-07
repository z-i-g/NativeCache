import java.lang.reflect.Array;

public class NativeCache <T>{
    public int size;
    public int step;
    public String [] slots;
    public T [] values;
    public int [] hits;

    public NativeCache(int sz, Class clazz)
    {
        size = sz;
        step = 3;
        slots = new String[size];
        hits = new int[size];
        values = (T[]) Array.newInstance(clazz, this.size);
    }

    public int hashFun(String key)
    {
        int intVal = 0;
        for (byte b: key.getBytes()) {
            intVal = intVal + b;
        }
        return Math.abs(intVal % size);
        // всегда возвращает корректный индекс слота
    }

    public int seekSlot(String value)
    {
        int slotIndex = hashFun(value);
        if (slots[slotIndex] == null)
            return slotIndex;

        for (int i = 0; i < size; i++) {
            slotIndex = slotIndex + step;
            if (slotIndex >= size) {
                slotIndex = size - (slotIndex + step);
                slotIndex = Math.abs(slotIndex);
            }

            if (slots[slotIndex] == null)
                return slotIndex;
        }
        return releaseSlot();
    }

    public int releaseSlot() {
        int slotIndex  = 0;
        int minRequestCount = hits[0];
        for (int i = 0; i < size; i++) {
            if (minRequestCount > hits[i]) {
                slotIndex = i;
                minRequestCount = hits[i];
            }
        }
        slots[slotIndex] = null;
        values[slotIndex] = null;
        hits[slotIndex] = 0;
        return slotIndex;
    }

    public int put(String key, T value)
    {
        int slotIndex = seekSlot(key);
        if (seekSlot(key) != -1) {
            slots[slotIndex] = key;
            values[slotIndex] = value;
            return slotIndex;
        }

        return -1;
    }

    public T get(String key)
    {
        int slot = getSlotsByKey(key);
        if (slot != -1) {
            hits[slot] = hits[slot] + 1;
            return values[slot];
        }
        return null;
    }

    public int getSlotsByKey(String key) {
        for (int i = 0; i < size; i++) {
            if (key.equals(slots[i]))
                return i;
        }
        return -1;
    }
}


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NativeCacheTest {
    NativeCache<String> nativeCache = new NativeCache<>(17, String.class);

    private void init() {
        nativeCache.put("1", "1");
        nativeCache.put("2", "2");
        nativeCache.put("3", "3");
        nativeCache.put("4", "4");
        nativeCache.put("5", "5");
        nativeCache.put("6", "6");
        nativeCache.put("7", "7");
        nativeCache.put("8", "8");
        nativeCache.put("9", "9");
        nativeCache.put("10", "10");
        nativeCache.put("11", "11");
        nativeCache.put("12", "12");
        nativeCache.put("13", "13");
        nativeCache.put("14", "14");
        nativeCache.put("15", "15");
        nativeCache.put("16", "16");
        nativeCache.put("17", "17");
    }

    @Test
    public void releaseSlotTest() {
        init();
        nativeCache.get("1");
        nativeCache.get("2");
        nativeCache.get("3");
        nativeCache.get("4");
        nativeCache.get("5");
        nativeCache.get("6");
        nativeCache.get("7");
        nativeCache.get("8");
        nativeCache.get("9");
        nativeCache.get("10");
        nativeCache.get("11");
        nativeCache.get("12");
        nativeCache.get("13");
        nativeCache.get("14");
        nativeCache.get("15");
        nativeCache.get("16");
        int releasedSlot = nativeCache.getSlotsByKey("17");

        nativeCache.put("20", "20");

        assertEquals(-1, nativeCache.getSlotsByKey("17"));
        assertEquals("20", nativeCache.get("20"));
        assertEquals(releasedSlot, nativeCache.getSlotsByKey("20"));
    }

    @Test
    public void hits_whenNotGetOperations() {
        init();
        for (int i: nativeCache.hits) {
            assertEquals(0, nativeCache.hits[i]);
        }
    }

    @Test
    public void hits_whenGetOperations() {
        init();
        nativeCache.get("1");
        assertEquals(1, nativeCache.hits[nativeCache.getSlotsByKey("1")]);

        nativeCache.get("1");
        assertEquals(2, nativeCache.hits[nativeCache.getSlotsByKey("1")]);
    }
}
package bukkaa.mediahouse.ShakerGameWS.controller;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CacheSprite
{
    private static ArrayList<CacheRecord> caches = new ArrayList<>();
    
    public static CacheRecord getCache(String spriteName) {
        CacheRecord rec = null;
        for (CacheRecord r : CacheSprite.caches) {
            if (r.spriteName.equals(spriteName)) {
                rec = r;
                break;
            }
        }
        return rec;
    }
    
    public static void addCache(CacheRecord rec) {
        CacheSprite.caches.add(rec);
    }
    
    public static void removeAll() {
        CacheSprite.caches.clear();
    }

    public static class CacheRecord {
        public String spriteName;
        public BufferedImage[] stand;
        public BufferedImage[] run;
        public BufferedImage[] finish;
    }
}

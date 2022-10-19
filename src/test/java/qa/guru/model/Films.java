package qa.guru.model;

import java.util.List;

public class Films {
    public String title;
    public String actors;
    public String language;
    public boolean respons;
    public int year;
    public List<String> rental;
    public Awards awards;

    public static class Awards{
        public int world;
        public String country;

    }
}

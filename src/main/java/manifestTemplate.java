public class manifestTemplate {
    private final String id;
    private final String name;
    private final String region;
    private final String[] credits;

    public manifestTemplate(String id, String name, String region, String credits) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.credits = credits.split(",");
    }
}

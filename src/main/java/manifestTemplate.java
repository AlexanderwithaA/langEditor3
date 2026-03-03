public class manifestTemplate {
    private String id;
    private String name;
    private String region;
    private String[] credits;

    public manifestTemplate(String id, String name, String region, String credits) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.credits = credits.split(",");
    }
}

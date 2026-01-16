public class LineItem {

    protected final String type = "LINE_ITEM";
    protected String reference;
    protected String contents;

    public LineItem(String reference1) {
        reference = reference1;
    }

    public String getType() {
        return type;
    }

    public String getReference() {
        return reference;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String newContents) {
        contents = newContents;
    }
}

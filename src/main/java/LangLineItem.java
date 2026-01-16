public class LangLineItem extends LineItem {

    protected final String type = "LANG_LINE_ITEM";
    protected String keyReference;
    protected String valueReference;
    protected String valueContents;

    public LangLineItem(String reference1) {
        super(reference1);
    }
}

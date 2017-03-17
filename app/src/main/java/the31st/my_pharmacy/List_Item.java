package the31st.my_pharmacy;

/**
 * Created by TIH on 3/13/2017.
 */
public class List_Item {

    String head, desc, imageUrl;

    public List_Item() {
    }

    public List_Item(String head, String desc, String imageUrl) {
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public List_Item(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List_Item(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

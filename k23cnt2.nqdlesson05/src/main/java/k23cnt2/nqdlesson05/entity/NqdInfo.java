package k23cnt2.nqdlesson05.entity;

public class NqdInfo {
    private String name;
    private String nickName;
    private String email;
    private String website;

    public NqdInfo() {
    }

    public NqdInfo(String name, String nickName, String email, String website) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

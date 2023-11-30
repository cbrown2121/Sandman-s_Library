import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public String userName;

    public UserInfo(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}

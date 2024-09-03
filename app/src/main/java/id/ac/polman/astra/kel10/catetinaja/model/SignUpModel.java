package id.ac.polman.astra.kel10.catetinaja.model;

public class SignUpModel {
    private String email;
    private String password;
    private String rePassword;
    private String username;

    public SignUpModel(String username, String email, String password, String rePassword) {
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
        this.username = username;
    }

    public SignUpModel(String username, String email) {
        this.email = email;
        this.username = username;
    }

    public SignUpModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package models;

public class RegisterModel {
    private String Login;
    private String Username;
    private String Password;
    private String ConfirmPassword;
    private String Email;
    private String Phonenumber;
    private String Address;

    public RegisterModel(String login, String username, String password, String confirmPassword, String email, String phonenumber, String address) {
        Login = login;
        Username = username;
        Password = password;
        ConfirmPassword = confirmPassword;
        Email = email;
        Phonenumber = phonenumber;
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }
}

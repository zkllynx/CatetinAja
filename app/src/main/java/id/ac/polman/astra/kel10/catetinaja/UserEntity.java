package id.ac.polman.astra.kel10.catetinaja;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class UserEntity {
    public String email;
    public String password;
    public String username;
    private List<String> pemasukan;
    private List<String> pengeluaran;
    public int saldo;

    public UserEntity(String email, String password, String username, List<String> pemasukan, List<String> pengeluaran, int saldo) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
        this.saldo = saldo;
    }

    public UserEntity(String username, String email) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPemasukan() {
        return pemasukan;
    }

    public void setPemasukan(List<String> pemasukan) {
        this.pemasukan = pemasukan;
    }

    public List<String> getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(List<String> pengeluaran) {
        this.pengeluaran = pengeluaran;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}

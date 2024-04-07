package Data;

public class SignUp {
  private String namaLengkap;
  private String username;
  private String password;

  public SignUp(String namaLengkap, String username, String password) {
    this.namaLengkap = namaLengkap;
    this.username = username;
    this.password = password;
  }

  // Getter dan setter

  public String getNamaLengkap() {
    return namaLengkap;
  }

  public void setNamaLengkap(String namaLengkap) {
    this.namaLengkap = namaLengkap;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

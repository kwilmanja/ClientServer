import java.io.Serializable;
import java.util.Objects;

public class Transaction implements Serializable {

  private boolean delete;
  private String key;
  private String value;

  public Transaction(String key){
    this.key = key;
    this.delete = true;
  }

  public Transaction(String key, String value){
    this.key = key;
    this.value = value;
    this.delete = false;
  }

  public String getKey(){
    return this.key;
  }

  public String getValue(){
    return this.value;
  }

  public boolean isDelete() {
    return delete;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Transaction otherTransaction = (Transaction) obj;
    return this.getKey().equals(otherTransaction.getKey());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.key);  // Use only the id field for hashCode
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "delete=" + delete +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
  }
}

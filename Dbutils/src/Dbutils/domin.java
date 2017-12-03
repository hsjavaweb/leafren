package Dbutils;

public class student {
    private int id;
    private String name;
    private String age;
    private String password;


    public Student(){
    }
    public Student(String name, String age, String password) {
        super();
        this.name = name;
        this.age = age;
        this.password = password;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", age=" + age + ", password=" + password + "]";
    }
}


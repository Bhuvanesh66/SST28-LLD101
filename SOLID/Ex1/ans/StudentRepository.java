public interface StudentRepository {
    void save(StudentRecord r);
    int count();
    java.util.List<StudentRecord> all();
}

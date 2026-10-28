package fu.edu.sba301.ui;

import fu.edu.sba301.ch18.dao.StudentDAO;
import fu.edu.sba301.ch18.repository.StudentRepository;
import fu.edu.sba301.ch18.service.IStudentService;
import fu.edu.sba301.ch18.service.StudentService;
import fu.edu.sba301.ch18.util.HibernateUtil;
import fu.edu.sba301.ui.controller.StudentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StudentApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                StudentApplication.class.getResource(
                        "/fu/edu/sba301/ui/student-view.fxml"
                )
        );
        Parent root = loader.load();

        IStudentService studentService = createStudentService();
        StudentController controller = loader.getController();
        controller.setStudentService(studentService);

        Scene scene = new Scene(root);
        stage.setTitle("Student Management");
        stage.setScene(scene);
        stage.show();
    }

    private IStudentService createStudentService() {
        String dbUrl = System.getenv("DB_URL");
        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB;encrypt=true;trustServerCertificate=true";
        }
        String dbUser = System.getenv("DB_USER");
        if (dbUser == null || dbUser.isBlank()) {
            dbUser = "sa";
        }
        String dbPass = System.getenv("DB_PASSWORD");
        if (dbPass == null) {
            dbPass = "123456";
        }

        Map<String, String> overrides = new HashMap<>();
        overrides.put("hibernate.connection.url", dbUrl);
        overrides.put("hibernate.connection.username", dbUser);
        overrides.put("hibernate.connection.password", dbPass);

        SessionFactory sessionFactory = HibernateUtil.initialize(overrides);
        StudentDAO dao = new StudentDAO(sessionFactory);
        StudentRepository repository = new StudentRepository(dao);
        return new StudentService(repository);
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

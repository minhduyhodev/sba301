package fu.edu.sba301.ui.controller;

import fu.edu.sba301.ch18.entity.Student;
import fu.edu.sba301.ch18.service.IStudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentController {
    @FXML private TextField txtEmail;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtMarks;

    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, Long> colId;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, Double> colMarks;

    private final ObservableList<Student> data =
            FXCollections.observableArrayList();

    private IStudentService studentService;
    private Long selectedStudentId;

    public void setStudentService(IStudentService service) {
        this.studentService = service;
        refreshTable();
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(
                new PropertyValueFactory<>("email"));
        colFirstName.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        colMarks.setCellValueFactory(
                new PropertyValueFactory<>("marks"));

        tblStudents.setItems(data);

        tblStudents.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) ->
                        populateForm(selected));
    }

    private void populateForm(Student s) {
        if (s == null) {
            return;
        }
        selectedStudentId = s.getId();
        txtEmail.setText(s.getEmail());
        txtFirstName.setText(s.getFirstName());
        txtLastName.setText(s.getLastName());
        txtMarks.setText(Double.toString(s.getMarks()));
    }

    private void refreshTable() {
        if (studentService == null) {
            return;
        }
        data.setAll(studentService.getAll());
    }

    private Student readForm() {
        String email = txtEmail.getText().trim();
        String first = txtFirstName.getText().trim();
        String last = txtLastName.getText().trim();

        if (email.isBlank() || first.isBlank() || last.isBlank()) {
            throw new IllegalArgumentException(
                    "Email, first name and last name are required");
        }

        double marks;
        try {
            marks = Double.parseDouble(txtMarks.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Marks must be a number");
        }

        if (marks < 0 || marks > 10) {
            throw new IllegalArgumentException(
                    "Marks must be between 0 and 10");
        }

        return new Student(email, first, last, marks);
    }

    @FXML
    private void handleCreate() {
        try {
            Student input = readForm();
            studentService.create(input);
            refreshTable();
            clearForm();
            showInfo("Student created");
        } catch (IllegalArgumentException |
                 IllegalStateException ex) {
            showError(ex.getMessage());
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            showError("Unable to create student");
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedStudentId == null) {
            showError("Select a student first");
            return;
        }

        try {
            Student input = readForm();
            input.setId(selectedStudentId);
            studentService.update(input);
            refreshTable();
            clearForm();
            showInfo("Student updated");
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedStudentId == null) {
            showError("Select a student first");
            return;
        }

        if (!confirm("Delete selected student?")) {
            return;
        }

        try {
            studentService.delete(selectedStudentId);
            refreshTable();
            clearForm();
            showInfo("Student deleted");
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    @FXML
    private void handleRefresh() {
        refreshTable();
    }

    private void clearForm() {
        selectedStudentId = null;
        txtEmail.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtMarks.clear();
        tblStudents.getSelectionModel().clearSelection();
        txtEmail.requestFocus();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Operation failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean confirm(String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                message,
                ButtonType.YES,
                ButtonType.NO
        );
        return alert.showAndWait().orElse(ButtonType.NO)
                == ButtonType.YES;
    }
}

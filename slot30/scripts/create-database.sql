-- =====================================================================
-- SBA301 - Slot 30 - Chapter 18 Part A: Hibernate CRUD Operations
-- Database Setup and Seed Data Script
-- Database Name: LibraryDB
-- =====================================================================

IF DB_ID(N'LibraryDB') IS NULL
BEGIN
    CREATE DATABASE LibraryDB;
    PRINT 'Created database LibraryDB successfully.';
END
ELSE
BEGIN
    PRINT 'Database LibraryDB already exists.';
END;
GO

USE LibraryDB;
GO

-- Clean up existing tables to ensure clean structure
IF OBJECT_ID('dbo.STUDENT_BOOKS', 'U') IS NOT NULL DROP TABLE dbo.STUDENT_BOOKS;
IF OBJECT_ID('dbo.STUDENTS_BOOKS', 'U') IS NOT NULL DROP TABLE dbo.STUDENTS_BOOKS;
IF OBJECT_ID('dbo.BORROWINGS', 'U') IS NOT NULL DROP TABLE dbo.BORROWINGS;
IF OBJECT_ID('dbo.STUDENTS', 'U') IS NOT NULL DROP TABLE dbo.STUDENTS;
IF OBJECT_ID('dbo.BOOKS', 'U') IS NOT NULL DROP TABLE dbo.BOOKS;
GO

-- Table: STUDENTS
CREATE TABLE dbo.STUDENTS (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(120) NOT NULL CONSTRAINT UK_STUDENTS_EMAIL UNIQUE,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    marks FLOAT NOT NULL
);
GO

-- Table: BOOKS
CREATE TABLE dbo.BOOKS (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL CONSTRAINT UK_BOOKS_ISBN UNIQUE,
    title VARCHAR(200) NOT NULL
);
GO

-- Table: STUDENT_BOOKS (Many-to-Many join table)
CREATE TABLE dbo.STUDENT_BOOKS (
    student_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    CONSTRAINT PK_STUDENT_BOOKS PRIMARY KEY (student_id, book_id),
    CONSTRAINT FK_SB_STUDENT FOREIGN KEY (student_id) REFERENCES dbo.STUDENTS(id) ON DELETE CASCADE,
    CONSTRAINT FK_SB_BOOK FOREIGN KEY (book_id) REFERENCES dbo.BOOKS(id)
);
GO

-- =====================================================================
-- Seed Data (Dữ liệu mẫu ban đầu)
-- =====================================================================
INSERT INTO dbo.STUDENTS (email, first_name, last_name, marks) VALUES
('alice.nguyen@fu.edu.vn', 'Alice', 'Nguyen', 8.5),
('bob.tran@fu.edu.vn', 'Bob', 'Tran', 7.0),
('charlie.le@fu.edu.vn', 'Charlie', 'Le', 9.2),
('david.pham@fu.edu.vn', 'David', 'Pham', 6.8);
GO

INSERT INTO dbo.BOOKS (isbn, title) VALUES
('978-0134685991', 'Effective Java 3rd Edition'),
('978-1617294945', 'Spring in Action 6th Edition'),
('978-1491950357', 'Designing Data-Intensive Applications');
GO

INSERT INTO dbo.STUDENT_BOOKS (student_id, book_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3);
GO

-- Verification
PRINT '--- Current Database State ---';
SELECT 'STUDENTS' AS TableName, COUNT(*) AS TotalRows FROM dbo.STUDENTS
UNION ALL
SELECT 'BOOKS' AS TableName, COUNT(*) AS TotalRows FROM dbo.BOOKS
UNION ALL
SELECT 'STUDENT_BOOKS' AS TableName, COUNT(*) AS TotalRows FROM dbo.STUDENT_BOOKS;
GO

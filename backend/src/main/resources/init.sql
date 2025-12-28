-- Database initialization script for Course Learning Platform
-- This script runs when the MySQL container starts for the first time

-- Create database if it doesn't exist (handled by docker-compose environment variables)
-- The database 'course_learning' is created by docker-compose

-- Optional: Create additional tables or initial data here if needed

-- Example: Insert some sample data (uncomment if needed)
-- INSERT INTO course (title, description, instructor_id, status, created_at, updated_at)
-- VALUES ('Introduction to Java', 'Learn the basics of Java programming', 1, 'PUBLISHED', NOW(), NOW());

-- Note: JPA will automatically create tables based on entities when ddl-auto=update

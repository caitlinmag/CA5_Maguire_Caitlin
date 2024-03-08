DROP TABLE IF EXISTS employees;

CREATE TABLE employees(empID int NOT NULL AUTO_INCREMENT,
 firstName VARCHAR(20),
             lastName VARCHAR(20),
			 age int, 
			 department VARCHAR(20),
			 role VARCHAR(20),
			 hourlyRate FLOAT,
			 PRIMARY KEY (empID));
			 
INSERT INTO employees VALUES(01, 'Tom', 'Reilly', 45, 'Human Resources', 'Assistant Manager', 12.60);
INSERT INTO employees VALUES(02, 'Bonnie', 'Ryan', 33, 'Inventory', 'Inventory Manager', 13.00 );
INSERT INTO employees VALUES(03, 'Ruby', 'Mullen', 25, 'Customer Service', 'Customer Service Assistant', 11.00); 
INSERT INTO employees VALUES(04, 'Dylan', 'Mckevitt', 19, 'Sales', 'Sales Assistant', 10.50);
INSERT INTO employees VALUES(05, 'Annie', 'Murphy', 20, 'Sales', 'Sales Assistant', 10.70);
INSERT INTO employees VALUES(06, 'Carl','Connolly', 30, 'Customer Service', 'Manager', 15.50);
INSERT INTO employees VALUES(07, 'Molly', 'Smith', 47, 'Human Resources', 'Human Resources Manager', 16.16);
INSERT INTO employees VALUES(08, 'Kai', 'Hughes', 23, 'Inventory', 'Stock Assistant', 10.70);
INSERT INTO employees VALUES(09, 'Sophia', 'Cooper', 38, 'Sales', 'Supervisor', 14.30);
INSERT INTO employees VALUES(10, 'Jess', 'O Brien', 50, 'Customer Service', 'Assistant Manager', 13.90);
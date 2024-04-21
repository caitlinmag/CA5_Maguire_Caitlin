ALTER TABLE products
DROP FOREIGN KEY IF EXISTS products_ibfk_1;

DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS products;
CREATE TABLE employees(empID int NOT NULL AUTO_INCREMENT,
                       firstName VARCHAR(20),
                       lastName VARCHAR(20),
                       age int,
                       department VARCHAR(20),
                       role VARCHAR(60),
                       hourlyRate FLOAT,
                       PRIMARY KEY (empID));

CREATE TABLE products(product_ID int NOT NULL AUTO_INCREMENT,
                      productName VARCHAR(50),
                      productType VARCHAR(50),
                      Quantity int,
                      price FLOAT,
                      staff_ID int,
                      PRIMARY KEY (product_ID),
                      FOREIGN KEY (staff_ID) REFERENCES employees(empID)
);

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

INSERT INTO products VALUES (01,'Randoms','Treats',10,2.30,02);
INSERT INTO products VALUES (02,'Tayto Value Pack','Treats',40,1.79,06);
INSERT INTO products VALUES (03,'Oral B Genius X Electric Toothbrush','Toiletries',25,22.50,02);
INSERT INTO products VALUES (04,'Scott 1000 Toilet Paper','Toiletries',90,3.99,04);
INSERT INTO products VALUES (05,'Playstation 5','Electronics',15,499.99,04);
INSERT INTO products VALUES (06,'Stanley Adventure Quencher','Drinkware',17,39.95,05);
INSERT INTO products VALUES (07,'Toxic Waste','Sweets',30,1.99,05);
INSERT INTO products VALUES (08,'Double Mesh Stainless Steel Sieve','Culinary',8,12.30,02);
INSERT INTO products VALUES (09,'J2 Steel Professional Chef Knife Set','Culinary',4,52.76,06);
INSERT INTO products VALUES (10,'Amazon Echo Dot (3rd Generation)','Electronics',34,92.00,04);
INSERT INTO products VALUES (11,'Wireless Bluetooth FM Transmitter','Electronics',24,14.99,05);
INSERT INTO products VALUES (12,'12V Portable Analog Tyre Pump','Vehicular',19,11.50,06);

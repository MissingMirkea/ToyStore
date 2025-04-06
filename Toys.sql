Create table Toys (
ID Serial Primary Key,
Name varchar(100), 
Country varchar(3),
ToyType varchar(100),
Age int,
Cantity int , 
Price float 
);

Select *FROM Toys;

Insert into Toys (name , country , toytype ,age ,cantity , price)
values
('Teddy Bear' , 'USA' , 'Plushie' , 3 , 19 , 199.99);

Create table ToysOutOfStock(
ID Serial Primary Key,
Name varchar(100), 
Country varchar(3),
ToyType varchar(100),
Age int,
Price float 
);

Insert into ToysOutOfStock(Name , Country , ToyType,Age,Price) select name , country , toytype ,age , price from Toys;

Select *FROM ToysOutOfStock;

Create table Sales(
ID Serial Primary Key,
ToysID int , 
Cantity int , 
Transaction_date timestamp,
Total float  
);

Select *FROM Sales;
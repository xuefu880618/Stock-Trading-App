
import re
from urllib import request
from flask import Flask, session
from flask import request
from flask_mysqldb import MySQL
import re 
from random import random
from flask_mysqldb import MySQL
import numpy as np
import random
import json
from json import JSONEncoder


import os
import sys
import mysql.connector
import datetime


start = datetime.time(0, 0, 0)
end = datetime.time(23, 59, 59)


start = start.strftime('%H:%M:%S')
end = end.strftime('%H:%M:%S')


app = Flask(__name__)


try:
    db = mysql.connector.connect(host="localhost", user="root", passwd="Justin0618!")
except:
    sys.exit("Error")

db_cursor = db.cursor()

try:
    db_cursor.execute(operation="CREATE DATABASE loginregister")
except mysql.connector.DatabaseError:
    sys.exit("Error.")

db_cursor.execute("SHOW DATABASES")
databases = db_cursor.fetchall()
print(databases)


try:
    conn = mysql.connector.connect(host="localhost", user="root", passwd="Justin0618!", database="loginregister")
except:
    sys.exit("Error.")

cursor = conn.cursor()




try:
    cursor.execute("CREATE TABLE `loginregister`.`userdata` ( `id` INT NOT NULL AUTO_INCREMENT , `username` VARCHAR(255) NOT NULL , `fullname` VARCHAR(255) NOT NULL , `email` VARCHAR(255) NOT NULL , `password` VARCHAR(255) NOT NULL , `admin` TINYINT(1) NOT NULL , `cash_amount` DECIMAL(10,2) DEFAULT 0 , PRIMARY KEY (`id`), UNIQUE (`username`), UNIQUE (`email`)) ENGINE = InnoDB;")
    print("users table created successfully.")
except:
    sys.exit("The table already exists.")



try:
    cursor.execute("CREATE TABLE `loginregister`.`stock` ( `id` INT NOT NULL AUTO_INCREMENT , `company` VARCHAR(255) NOT NULL , `stock_symbol` VARCHAR(255) NOT NULL , `cur_price` DECIMAL(10,2) NOT NULL , `open_price` DECIMAL(10,2) NOT NULL , `high_price` DECIMAL(10,2) NOT NULL , `low_price` DECIMAL(10,2) NOT NULL , `volume` BIGINT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")
    print("stock table created successfully.")
except:
    sys.exit("The table already exists.")



try:
    cursor.execute("CREATE TABLE `loginregister`.`userstock` ( `id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(255) NOT NULL , `stock_symbol` VARCHAR(255) NOT NULL , `shares` BIGINT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")
    print("userstock table created successfully.")
except:
    sys.exit("The table already exists.")



try:
    cursor.execute("CREATE TABLE `loginregister`.`history_transaction` ( `id` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(255) NOT NULL , `stock_symbol` VARCHAR(255) NOT NULL , `buy_price` DECIMAL(10,2) NOT NULL , `buy` TINYINT(1) NOT NULL , `shares` BIGINT NOT NULL , `buy_time` DATETIME NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")
    print("history_transaction table created successfully.")
except:
    sys.exit("The table already exists.")



try:
    cursor.execute("CREATE TABLE `loginregister`.`openhour` ( `id` INT NOT NULL AUTO_INCREMENT ,  `start` TIME NOT NULL , `end` TIME NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;")
    print("openhour table created successfully.")
except:
    sys.exit("The table already exists.")


insert_query = "INSERT INTO openhour ( start, end) VALUES( %s, %s)"

insert_values = (start, end)
try:
    cursor.execute(insert_query, [start,end])
    conn.commit()

except Exception as e:
    print("inserting fail")



try:
    cursor.execute("CREATE TABLE `loginregister`.`holiday` ( `id` INT NOT NULL AUTO_INCREMENT , `date` DATE NOT NULL , `holiday` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`),  UNIQUE (`holiday`)) ENGINE = InnoDB;")
    print("holiday table created successfully.")
except:
    sys.exit("The table already exists.")


try:
    cursor.execute("CREATE TABLE `loginregister`.`templimitorder` (`id` INT NOT NULL AUTO_INCREMENT,`username` VARCHAR(255) NOT NULL,`stock_symbol` VARCHAR(255) NOT NULL,`buy` TINYINT NOT NULL,`shares` BIGINT NOT NULL,`prices` DECIMAL NOT NULL,`expire_date` DATE , PRIMARY KEY (`id`));")
    print("templimitorder table created successfully.")
except:
    sys.exit("The table already exists.")



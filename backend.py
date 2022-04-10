from asyncio.windows_events import NULL
from http.client import CONTINUE
import re
from urllib import request
from flask import Flask, session
from flask import request
from flask_mysqldb import MySQL
import re 
from random import random
import numpy as np
import time
import random
from apscheduler.schedulers.background import BackgroundScheduler
import json
from json import JSONEncoder
import datetime
from datetime import date
from datetime import datetime as dd
import os
import hashlib
#from passlib.hash import sha256_crypt



app = Flask(__name__)

app.config['MYSQL_HOST'] = "localhost"
app.config['MYSQL_USER'] = "root"
app.config['MYSQL_PASSWORD'] = "Justin0618!"
app.config['MYSQL_DB'] = "loginregister"

mysql = MySQL(app)




@app.route('/login',methods = ['GET', 'POST'])
def login():
    conn = mysql.connection
    cursor = conn.cursor()
    # Output message if something goes wrong...
    msg = ''
    # Check if "username" and "password" POST requests exist (user submitted form)
    if request.method == 'POST' and 'username' in request.form and 'password' in request.form:
        # Create variables for easy access
        username = request.form['username']
        password = request.form['password']
        password = password.encode('utf-8')
        apppassword = hashlib.sha256(password).hexdigest()
        #commapos = password.find(",")
        #val = password[0:commapos]
        #hashval = password[commapos+1:-1]
        #rehashval = hash_str(val)
        #if rehashval == hashval:
            #return val
        # Check if account exists using MySQL
        cursor.execute('SELECT * FROM userdata WHERE username = %s AND password = %s', (username, apppassword))
        # Fetch one record and return result
        account = cursor.fetchone()
        #print(account)
    # If account exists in accounts table in out database
        if account:

            if(account[5] == 1):
                msg = 'Logged in as admin'
            else:
                msg = 'Logged in as user'
            

            return msg
            #return redirect(url_for('home'))
        else:
            # Account doesnt exist or username/password incorrect
            msg = 'Incorrect username/password!'
            print(msg)
            return msg


@app.route('/register',methods = ['GET', 'POST'])
def register():
    conn = mysql.connection
    cursor = conn.cursor()
  
    # Output message if something goes wrong...
    msg = ''
    # Check if "username", "password" and "email" POST requests exist (user submitted form)
    if request.method == 'POST' :
        
        # Create variables for easy access
        fullname = request.form['fullName']
        username = request.form['username']
        password = request.form['password']
        email = request.form['email']
        #print(email)
        admin = request.form['admin1']
        # hash password
        password = password.encode('utf-8')
        password = hashlib.sha256(password).hexdigest()

        #Check if account exists using MySQL
        cursor.execute('SELECT * FROM userdata WHERE username = %s', [username])
        account = cursor.fetchone()
        #print(account)
        # If account exists show error and validation checks
        if account:
            msg = 'Account already exists!'
            print(msg)
            res = 4
        elif not re.match(r'[^@]+@[^@]+\.[^@]+', email):
            msg = 'Invalid email address!'
            print(msg)
            res =3
        elif not re.match(r'[A-Za-z0-9]+', username):
            msg = 'Username must contain only characters and numbers!'
            print(msg)
            res = 2
        elif not username or not password or not email or not fullname:
            msg = 'Please fill out the form!'
            print(msg)
            res = 1
        else:
            print('insert successful')
            # Account doesnt exists and the form data is valid, now insert new account into accounts table
            cursor.execute('INSERT INTO userdata (fullname,username,email,password,admin) VALUES (%s, %s, %s, %s, %s)', (fullname, username, email, password, admin)) 
            
            
            msg = 'You have successfully registered!'
            print(msg)
    else :
        # Form is empty... (no POST data)
        msg = 'Field cannot be empty'
        res = 1
    # Show registration form with message (if any)
    #cursor.close()
    conn.commit()
    print(msg)
    return msg
#@app.route('/logout')
#def logout():
    # Remove session data, this will log the user out
   #session.pop('loggedin', None)
   #session.pop('id', None)
   #session.pop('username', None)
   # Redirect to login page
   #return redirect(url_for('login'))            


def update():
    conn = mysql.connection
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM stock')
    # Fetch one record and return result
    account = cursor.fetchone()
    # random stock generater
    curprice = account[3]
    mu = 0.001
    sigma = 0.01
    #print(account)     
        
def updatestock():
    with app.app_context():
        conn = mysql.connection
        cursor = conn.cursor()
        cursor.execute('SELECT start FROM openhour')
        start = cursor.fetchone()
        start = start[0]
        start = str(start)
        start = datetime.datetime.strptime(start,'%H:%M:%S')
        start = start.time()
        #print(type(start))
        #start = start.strftime('%H:%M:%S')
        cursor.execute('SELECT end FROM openhour')
        end = cursor.fetchone()
        end = end[0]
        end = str(end)
        end = datetime.datetime.strptime(end,'%H:%M:%S')
        end = end.time()
        
        #print(type(end))
        #end = end.strftime('%H:%M:%S')
        cursor.execute('SELECT date FROM holiday')
        holiday = cursor.fetchall()
        #print(type(holiday[0]))
        today = date.today()
        if today.weekday() > 4:
            return 'today is weekend'
        #print(today)
        now = dd.now()
        d1 = today.strftime('%Y-%m-%d')
        #print("d1 =", d1)
        d1 = datetime.datetime.strptime(d1,'%Y-%m-%d')
        #current_time = now.strftime("%H:%M:%S")
        d2 = now.strftime('%H:%M:%S')
        d2 = datetime.datetime.strptime(d2,'%H:%M:%S')
        d2 = d2.time()
        
        #print("d2 =", d2)    
        
        
        if d2 > start and d2 < end:
            CONTINUE
        else:
            print('bye')
            return 'none'
        
        if len(holiday) != 0:
            for row in holiday:
                #print(type(row[0]))
                #print(row[0])
                #print(today)
                #print(type(d1))
                #row = dd.datetime.strftime(row[0],'%Y-%m-%d')
                #row = datetime.datetime.strfpime(row[0],'%Y-%m-%d')
                
                #print(d1)
                if row[0]== today:
                    print('today is not open')
                    return 'none'
                    
        cursor.execute('SELECT * FROM stock')
        # Fetch one record and return result
        account = cursor.fetchall()
        for item in account:
            curprice = item[3]
            company = item[1]
            curpricefloat = float ( curprice )
            x = random.uniform(-0.1, 0.1)
            price = curpricefloat + x
            print("stock price:" + str(price))
            #mu = 1
            #sigma = 0.001
            #np.random.seed(0)
            #returns = np.random.normal(loc=mu, scale=sigma, size=1)
            #price = float_number*(1+returns)
            #print(price)
            cursor.execute("UPDATE stock SET cur_price = %s WHERE company = %s",[price,company])
            #conn.commit()
            cursor.execute("SELECT high_price FROM `stock` WHERE company = %s",[company])
            #conn.commit()
            largest = cursor.fetchone()
            cursor.execute("SELECT low_price FROM `stock` WHERE company = %s",[company])
            #conn.commit()
            smallest = cursor.fetchone()
            #print(largest[0])
            if price > float(largest[0]):
                cursor.execute("UPDATE stock SET high_price = %s WHERE company = %s",[price,company])
                #conn.commit()
            if price < float(smallest[0]):
                cursor.execute("UPDATE stock SET low_price = %s WHERE company = %s",[price,company])
                #conn.commit()
            



        conn.commit()
        cursor.close()
        #conn.close()
@app.route('/stock',methods = ['GET','POST'])
def getstock():
     with app.app_context():
        

        conn = mysql.connection
        cursor = conn.cursor()
        username = request.form['username']

        cursor.execute("SHOW columns in stock")

        #print('connection successul')
        row_headers=[x[0] for x in cursor.fetchall()]
        #print(row_headers)
        cursor.execute('SELECT * FROM stock')
            # Fetch one record and return result
        account = cursor.fetchall()

        
        #records = db_cursor.fetchall()
        if len(account) == 0:
            return "none"
        
            
        json_data = []
        all = {}
        i = 0;
        #sched = BackgroundScheduler(daemon=True)
        #sched.add_job(updatestock,'interval',seconds=3)
        #sched.start()
        
        

        for row in account:
            as_list = list(row)
            as_list[3] = float(as_list[3])
            as_list[4] = float(as_list[4])
            as_list[5] = float(as_list[5])
            as_list[6] = float(as_list[6])
            
            row = tuple(as_list)
            json_data.append(dict(zip(row_headers, row)))
            #print(row)
            i+=1
        #print(json_data)
        all.update({'company': json_data})



        
        conn.commit()
        cursor.close()
        #conn.close()
        #stock_db.free_result()
        return json.dumps(all, cls=JSONEncoder)

@app.route('/daw',methods = ['GET','POST'])
def depositandwithdraw():
    #print('connection success')
    if request.method == 'POST' :
        
        msg = ''
        conn = mysql.connection
        cursor = conn.cursor()
        operation = request.form['operation']
        money = request.form['money']
        username = request.form['username']
        if int(money):
            money = int(money)
        else:
            msg = 'please type number'
            return msg
        if(operation == 'deposit'):
            cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
            curmoney = cursor.fetchone()
            #print(curmoney)
            curmoney = curmoney[0] + money
            cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[curmoney,username])
            msg = 'deposit success!'
        elif(operation == 'withdraw'):
            cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
            #print(curmoney)
            curmoney = cursor.fetchone()
            if(curmoney[0] - money < 0):
                msg = 'you do not have enough money'
            else:
                curmoney = curmoney[0] - money
                cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[curmoney,username])
                msg = 'withdraw success!'
    print(msg)
    conn.commit()
    return msg

@app.route('/buyandsell',methods = ['GET','POST'])
def buyandsell():

    if request.method == 'POST' :
        #print("buyandsell")
        msg = 'hi'
        conn = mysql.connection
        cursor = conn.cursor()
        stocksymbol = request.form['stocksymbol']
        share = request.form['share']
        limitorder = request.form['limitorder']
        operation = request.form['operation']
        #buytime = request.form['buytime']
        username = request.form['username']
        expiredate = request.form['expire_date']
        #volume = request.form['volume']

        cursor.execute('SELECT start FROM openhour')
        start = cursor.fetchone()
        start = start[0]
        start = str(start)
        start = datetime.datetime.strptime(start,'%H:%M:%S')
        start = start.time()
        #print(type(start))
        #start = start.strftime('%H:%M:%S')
        cursor.execute('SELECT end FROM openhour')
        end = cursor.fetchone()
        end = end[0]
        end = str(end)
        end = datetime.datetime.strptime(end,'%H:%M:%S')
        end = end.time()
        
        #print(type(end))
        #end = end.strftime('%H:%M:%S')
        cursor.execute('SELECT date FROM holiday')
        holiday = cursor.fetchall()
        #print(type(holiday[0]))
        today = date.today()
        

        now = dd.now()
        d1 = today.strftime('%Y-%m-%d')
        d11 = today.strftime('%Y-%m-%d')
    
        #print("d1 =", d1)
        
        
        d1 = datetime.datetime.strptime(d1,'%Y-%m-%d')
        #current_time = now.strftime("%H:%M:%S")
        d2 = now.strftime('%H:%M:%S')
        datetime1 = d11 + ' ' + d2
        #print(datetime1)
        d2 = datetime.datetime.strptime(d2,'%H:%M:%S')
        d2 = d2.time()
        
        #print("d2 =", d2)  
        #print(stocksymbol)
        #print(share)
        #print(limitorder)
        #print(operation)
        #print(buytime)
        f = '%d-%m-%Y %H:%M:%S'
        share = int(share)
        cursor.execute("SELECT cur_price FROM stock WHERE stock_symbol = %s",[stocksymbol])
        price = cursor.fetchone()
        price = price[0]
        if int(price):
            price = int(price)
        else:
            msg = 'there is not that stock in the market'
            return msg


        ######if there is not limitorder
        if len(limitorder) == 0:
            if price != 0:
                print(d2)
                print(start)
                print(end)
                if not(d2 > start and d2 < end):
                    print('bye')
                    return 'now is not open'
                if today.weekday() > 4:
                    print('today is weekend')
                    return 'today is weekend'
                if len(holiday) != 0:
                    for row in holiday:
                        if row[0]== today:
                            print('today is not open')
                            return 'today is not open'
                amount = price*share
                cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
                user_cash = cursor.fetchone();
                user_cash = user_cash[0]
                #print('before sell')
                if operation == 'buy':
                    if amount > user_cash:
                        msg = 'you do not have enough money'
                    else:
                        user_cash = user_cash - amount
                        cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                        
                        #date_time_obj1 = datetime.datetime.strptime(d2, f)
                        cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 1 , price, stocksymbol, share, datetime1))
                        cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                        stock = cursor.fetchall()

                        if len(stock) == 0:
                            cursor.execute('INSERT INTO userstock (username,stock_symbol,shares) VALUES (%s, %s, %s)', (username,  stocksymbol, share))
                        else:
                            
                            print(stock)
                            oldshare = stock[0][3]
                            cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare + share, username, stocksymbol])

                        
                        msg = 'buy stock complete'
                elif operation == 'sell':
                    user_cash = user_cash + amount
                    cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                    #f = '%Y-%m-%d %H:%M:%S'
                    #date_time_obj1 = datetime.datetime.strptime(d2, f)
                    cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 0 , price, stocksymbol, share, datetime1))
                    print('selling')
                    cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                    
                    stock = cursor.fetchall()

                    if len(stock) == 0:
                        msg = 'You do not have this stock in your account'
                    else:
                            
                            
                        oldshare = stock[0][3]
                        if oldshare < share:
                            msg = 'You do not have enough stock to sell'
                        if oldshare == share:
                            cursor.execute("DELETE FROM userstock WHERE username = %s AND stock_symbol = %s ",[username, stocksymbol])
                        else:
                            cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare - share, username, stocksymbol])
                    msg = 'sell stock complete'
            else:
                msg = 'The stock is not exist'


        ###############if there is limitorder
        else:
            if int(limitorder):
                limitorder = int(limitorder)
            else:
                msg = 'please type number'
                print(msg)
                return msg
            if operation == 'buy':
                #
                if limitorder <= price:
                    print(type(expiredate))
                    if len(expiredate) != 0 :
                        cursor.execute('INSERT INTO templimitorder (username, stock_symbol ,buy, shares, prices, expire_date) VALUES (%s, %s, %s, %s, %s,%s)', (username, stocksymbol , 1, share, limitorder, expiredate))
                    else:
                        cursor.execute('INSERT INTO templimitorder (username, stock_symbol ,buy, shares, prices) VALUES (%s, %s, %s, %s, %s)', (username, stocksymbol , 1, share, limitorder))
                    msg = 'buy limit all set!'
                
                else:
                    
                    if price != 0:
                        if d2 < start or d2 > end:
                            print('bye')
                            return 'now is not open'
                        if today.weekday() > 4:
                            print('today is weekend')
                            return 'today is weekend'
                        if len(holiday) != 0:
                            for row in holiday:
                                if row[0]== today:
                                    print('today is not open')
                                    return 'today is not open'
                        amount = price*share
                        cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
                        user_cash = cursor.fetchone();
                        user_cash = user_cash[0]
                        if operation == 'buy':
                            
                            if amount > user_cash:
                                msg = 'you do not have enough money'
                            else:
                                user_cash = user_cash - amount
                                cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                                #f = '%Y-%m-%d %H:%M:%S'
                                #date_time_obj1 = datetime.datetime.strptime(d2, f)
                                cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 1 , price, stocksymbol, share, datetime1))
                                cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                                stock = cursor.fetchall()

                                if len(stock) == 0:
                                    cursor.execute('INSERT INTO userstock (username,stock_symbol,shares) VALUES (%s, %s, %s)', (username,  stocksymbol, share))
                                else:
                                    
                                    
                                    oldshare = stock[0][3]
                                    cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare + share, username, stocksymbol])
                                
                                msg = 'buy stock complete'



                        elif operation == 'sell':
                            user_cash = user_cash + amount
                            cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                            #f = '%Y-%m-%d %H:%M:%S'
                            #date_time_obj1 = datetime.datetime.strptime(d2, f)
                            cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 0 , price, stocksymbol, share, datetime1))
                            #print('selling')
                            cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                            stock = cursor.fetchall()
                            
                            if len(stock) == 0:
                                msg = 'You do not have this stock in your account'
                            else:
                                    
                                    
                                oldshare = stock[0][3]
                                if oldshare < share:
                                    msg = 'You do not have enough stock to sell'
                                if oldshare == share:
                                    cursor.execute("DELETE FROM userstock WHERE username = %s AND stock_symbol = %s ",[username, stocksymbol])
                                else:
                                    cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare - share, username, stocksymbol])
                            
                            
                            msg = 'sell stock complete'



            elif operation == 'sell':
                print("selling")
                
                if limitorder >= price:
                #put into limitorder database
                    if len(expiredate) != 0:
                        cursor.execute('INSERT INTO templimitorder (username, stock_symbol ,buy, shares, prices, expire_date) VALUES (%s, %s, %s, %s, %s,%s)', (username, stocksymbol , 0, share, limitorder, expiredate))
                    else:
                        cursor.execute('INSERT INTO templimitorder (username, stock_symbol ,buy, shares, prices) VALUES (%s, %s, %s, %s, %s)', (username, stocksymbol , 0, share, limitorder))
                   
                    msg = 'sell limit all set!'
                else:
                    if d2 < start or d2 > end:
                            print('bye')
                            return 'now is not open'
                    if today.weekday() > 4:
                        print('today is weekend')
                        return 'today is weekend'
                    if len(holiday) != 0:
                        for row in holiday:
                            if row[0]== today:
                                print('today is not open')
                                return 'today is not open'
                    amount = price*share
                    cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
                    user_cash = cursor.fetchone();
                    user_cash = user_cash[0]
                    cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                    #f = '%Y-%m-%d %H:%M:%S'
                    #date_time_obj1 = datetime.datetime.strptime(d2, f)
                    cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 0 , price, stocksymbol, share, datetime1))
                    #print('selling')
                    cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                    stock = cursor.fetchall()
                    
                    if len(stock) == 0:
                        msg = 'You do not have this stock in your account'
                    else:
                            
                            
                        oldshare = stock[0][3]
                        if oldshare < share:
                            msg = 'You do not have enough stock to sell'
                        if oldshare == share:
                            cursor.execute("DELETE FROM userstock WHERE username = %s AND stock_symbol = %s ",[username, stocksymbol])
                        else:
                            cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare - share, username, stocksymbol])
                    
                    
                    msg = 'sell stock complete'

        
    conn.commit()   
    cursor.close()
    #conn.close()
    print(msg)
    return msg  


@app.route('/history',methods = ['GET','POST'])
def gethistory():
    conn = mysql.connection
    cursor = conn.cursor()
    cursor.execute("SHOW columns in history_transaction")
    username = request.form['username']
    #username = 'xuefu0618'
    print('connection successul')
    row_headers=[x[0] for x in cursor.fetchall()]
        #print(row_headers)
    cursor.execute("SELECT * FROM history_transaction WHERE username = %s",[username])
        
            # Fetch one record and return result
    account = cursor.fetchall()
        #records = db_cursor.fetchall()
    if len(account) == 0:
        print('none')
        return "none"
    json_data = []
    all = {}
    i = 0;
        
    for row in account:
        as_list = list(row)

        as_list[3] = float(as_list[3])
        as_list[6] = str(as_list[6])
            
        row = tuple(as_list)
            #print(row)
        json_data.append(dict(zip(row_headers, row)))
            #print(row)
        i+=1
        
    all.update({'history': json_data})
        #cursor.close()
    #print(all)
        #stock_db.free_result()
    conn.commit() 
    return json.dumps(all, cls=JSONEncoder )

@app.route('/property',methods = ['GET','POST'])
def getproperty():
     #updatestock()
     with app.app_context():
        conn = mysql.connection
        cursor = conn.cursor()
        
        cursor.execute("SHOW columns in userstock")
        #print('hi')
        username = request.form['username']
        #username = 'xuefu0618'
        print('connection successul')
        row_headers=[x[0] for x in cursor.fetchall()]
        #row_headers.append('cashamount')
        print(row_headers)
        cursor.execute("SELECT * FROM userstock WHERE username = %s",[username])
        userstock = cursor.fetchall();
        #cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
        #cashamount = cursor.fetchone();
        #cashamount = float(cashamount[0])
        
        

        json_data = []
        all = {}
        i = 0;
        if len(userstock) == 0:
            return 'none'
        else:
            for row in userstock:
                as_list = list(row)

                as_list[3] = float(as_list[3])
                #as_list[6] = str(as_list[6])
                #as_list.append(cashamount)
                row = tuple(as_list)
                row
                print(row)
                json_data.append(dict(zip(row_headers, row)))
                #json_data.append(dict(zip('cashamount',cashamount)))
                #print(row)
                i+=1
        
        
        all.update({'userproperty': json_data})
        #cursor.close()
        
        #print(all)
        #stock_db.free_result()
        conn.commit() 
        cursor.close()
        #conn.close()
        return json.dumps(all, cls=JSONEncoder)

@app.route('/cashamount',methods = ['GET','POST'])
def getcashamount():
    username = request.form['username']
    conn = mysql.connection
    cursor = conn.cursor()
    cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
    cashamount = cursor.fetchone();
    cashamount = float(cashamount[0])

    return str(cashamount)


@app.route('/addstock',methods = ['GET', 'POST'])
def addstock():
    conn = mysql.connection
    cursor = conn.cursor()
  
    # Output message if something goes wrong...
    msg = ''
    # Check if "username", "password" and "email" POST requests exist (user submitted form)
    if request.method == 'POST' :
        res = 0
        # Create variables for easy access
        company = request.form['company']
        stocksymbol = request.form['stocksymbol']
        openprice = request.form['openprice']
        volume = request.form['volume']
 
  #Check if account exists using MySQL
        cursor.execute('SELECT * FROM stock WHERE company = %s', [company])
        account = cursor.fetchone()
        #print(account)
        # If account exists show error and validation checks
        if account:
            msg = 'Stock already exists!'
            print(msg)
        elif not company or not stocksymbol or not openprice or not volume:
            msg = 'Please fill out the form!'
            print(msg)
        else:
            print('insert successful')
            # Account doesnt exists and the form data is valid, now insert new account into accounts table
            cursor.execute('INSERT INTO stock (company,stock_symbol,cur_price,open_price,high_price,low_price,volume) VALUES (%s, %s, %s, %s, %s, %s, %s)', (company, stocksymbol,openprice, openprice, openprice, openprice,volume)) 
            conn.commit()
            
            msg = 'You have add new stock successfully!'
            print(msg)
    else :
        # Form is empty... (no POST data)
        msg = 'Field cannot be empty'
    # Show registration form with message (if any)
    #cursor.close()
    print(msg)
    return msg          

@app.route('/addholiday',methods = ['GET', 'POST'])
def addholiday():
    conn = mysql.connection
    cursor = conn.cursor()
  
    # Output message if something goes wrong...
    msg = ''
    # Check if "username", "password" and "email" POST requests exist (user submitted form)
    if request.method == 'POST' :
        res = 0
        # Create variables for easy access
        
        holidayname = request.form['holidayname']
        operation = request.form['operation']
        #print(date)
        #print(type(date))

 
  #Check if account exists using MySQL
        cursor.execute('SELECT * FROM holiday WHERE holiday = %s', [holidayname])
        account = cursor.fetchone()
        print(account)
        # If account exists show error and validation checks
        if operation == '1':
            date = request.form['date']
            if account:
                msg = 'Close day already exists!'
                print(msg)
            elif not date or not holidayname:
                msg = 'Please fill out the form!'
                print(msg)
            else:
                print('insert successful')
                # Account doesnt exists and the form data is valid, now insert new account into accounts table
                cursor.execute('INSERT INTO holiday (holiday,date) VALUES (%s, %s)', (holidayname, date)) 
                conn.commit()
                
                msg = 'You have add new close day successfully!'
                print(msg)
        else:
            if account:
                cursor.execute('DELETE FROM holiday WHERE holiday = %s',[holidayname]);
                conn.commit()
                
                msg = 'You have delete close day successfully!'
                print(msg)
            else:
                #cursor.execute('DELETE FROM holiday WHERE (holiday = %s)',(holidayname))
                msg = 'That holiday is not exsit!'

    else :
        # Form is empty... (no POST data)
        msg = 'Field cannot be empty'
    # Show registration form with message (if any)
    #cursor.close()
    print(msg)
    return msg   

@app.route('/changetime',methods = ['GET', 'POST'])
def changetime():
    conn = mysql.connection
    cursor = conn.cursor()
  
    # Output message if something goes wrong...
    msg = ''
    # Check if "username", "password" and "email" POST requests exist (user submitted form)
    if request.method == 'POST' :
        res = 0
        # Create variables for easy access
        start = request.form['start']
        start = start+':00'
        end = request.form['end']
        end = end+':00'
        f = '%H:%M:%S'
        
        date_time_obj1 = datetime.datetime.strptime(start, f)
        date_time_obj2 = datetime.datetime.strptime(end, f)
        cursor.execute("UPDATE openhour SET start = %s , end = %s  WHERE id = 1 ", [date_time_obj1.time(),date_time_obj2.time()])
        msg = 'change complete'
    conn.commit()
    print(msg)
    return msg        
            
def limitorder():
    with app.app_context():
        conn = mysql.connection
        cursor = conn.cursor()
        today = date.today()
            

        now = dd.now()
        d11 = today.strftime('%Y-%m-%d')    
        #current_time = now.strftime("%H:%M:%S")
        d2 = now.strftime('%H:%M:%S')
        datetime1 = d11 + ' ' + d2
        cursor.execute('SELECT * FROM templimitorder ')
        order = cursor.fetchall()
        if len(order) != 0:

            for item in order:
                
                #print(item)
                id = [item[0]]
                username = item[1]
                prices = float(item[5])
                stocksymbol = item[2]
                buy = item[3]
                share = item[4]
                expiredate = item[6]
                cursor.execute("SELECT cur_price FROM `stock` WHERE stock_symbol = %s",[stocksymbol])
                tempprice = cursor.fetchone()
                tempprice = tempprice[0]
                cursor.execute("SELECT cash_amount FROM userdata WHERE username = %s",[username])
                user_cash = cursor.fetchone();
                user_cash = user_cash[0]
                amount = prices*share
  

                ##########expiredate operation
                if expiredate is not None:
                    print('not null')
                    print(today)
                    print(expiredate)
                    if str(today) == str(expiredate):
                        print('true')
                        if buy == 1:
                            if amount > user_cash:
                                msg = 'you do not have enough money'
                            else:
                                user_cash = float(user_cash)
                                user_cash = user_cash - amount
                                cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                                #f = '%Y-%m-%d %H:%M:%S'
                                #date_time_obj1 = datetime.datetime.strptime(d2, f)
                                cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 1 , prices, stocksymbol, share, datetime1))
                                cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                                stock = cursor.fetchall()

                                if len(stock) == 0:
                                    cursor.execute('INSERT INTO userstock (username,stock_symbol,shares) VALUES (%s, %s, %s)', (username,  stocksymbol, share))
                                else:
                                    
                                    
                                    oldshare = stock[0][3]
                                    cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare + share, username, stocksymbol])
                                cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                                
                                msg = 'buy stock complete'
                        else:
                            user_cash = float(user_cash)
                            user_cash = user_cash + amount
                            cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash,username])
                            #f = '%Y-%m-%d %H:%M:%S'
                            #date_time_obj1 = datetime.datetime.strptime(d2, f)
                            cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 0 , prices, stocksymbol, share, datetime1))
                            print('selling')
                            cursor.execute("SELECT * FROM userstock WHERE username = %s AND stock_symbol = %s",[username,stocksymbol])
                            stock = cursor.fetchall()
                            
                            if len(stock) == 0:
                                msg = 'You do not have this stock in your account'
                            else:
                                    
                                    
                                oldshare = stock[0][3]
                                if oldshare < share:
                                    msg = 'You do not have enough stock to sell'
                                if oldshare == share:
                                    cursor.execute("DELETE FROM userstock WHERE username = %s AND stock_symbol = %s ",[username, stocksymbol])
                                    cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                                else:
                                    cursor.execute("UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s",[oldshare - share, username, stocksymbol])
                                    cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                            
                            
                            msg = 'sell stock complete'
                else:
                    msg = 'field cannot be empty'


                ########### expire price operation                      

                if buy == 1:
                    print('buy process')
                    if(prices >= tempprice):
                        amount = tempprice*share
                        print('buying process')
                        cursor.execute("SELECT shares FROM `userstock` WHERE stock_symbol = %s AND username = %s",[stocksymbol, username])
                        
                        old_share = cursor.fetchone();
                        if old_share is not None:

                            print(old_share)
                            old_share = old_share[0]
                            # update
                            if amount <= user_cash:
                                cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash - amount,username])
                        
                        #date_time_obj1 = datetime.datetime.strptime(d2, f)
                                cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 1 , tempprice, stocksymbol, share, datetime1))
                                cursor.execute('UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s', (old_share + share,username, stocksymbol))
                                cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                            else:
                                cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                                return 'you do not have enough money'
                            
                        else:

                            cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                            return 'you do not have enough that stock'
                else:
                    print('sell process')
                    if(prices <= tempprice):
                        print('selling process')
                        cursor.execute("SELECT shares FROM `userstock` WHERE stock_symbol = %s AND username = %s",[stocksymbol, username])
                        amount = tempprice*share
                        old_share = cursor.fetchone();
                        if old_share is not None:

                            print(old_share)
                            old_share = old_share[0]
                            # update
                            if share <= old_share:
                                cursor.execute('UPDATE userstock SET shares = %s WHERE username = %s AND stock_symbol = %s', (old_share - share, username, stocksymbol ))
                                cursor.execute("UPDATE userdata SET cash_amount = %s WHERE username = %s",[user_cash + amount,username])
                                cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))
                        
                        #date_time_obj1 = datetime.datetime.strptime(d2, f)
                                cursor.execute('INSERT INTO history_transaction (username,buy,buy_price,stock_symbol,shares,buy_time) VALUES (%s, %s, %s, %s, %s, %s)', (username, 0 , tempprice, stocksymbol, share, datetime1))
                            else:
                                cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id)) 
                                return 'you do not have enough share'
                        else: 
                            cursor.execute('DELETE FROM `templimitorder` WHERE `templimitorder`.`id` = %s', (id))   
                            return 'you do not have that stock'
        conn.commit()

@app.route('/limitorderview',methods = ['GET','POST'])
def getlimitorderview():
     #updatestock()
     with app.app_context():
        conn = mysql.connection
        cursor = conn.cursor()
        
        cursor.execute("SHOW columns in templimitorder")
        #print('hi')
        username = request.form['username']
        print('connection successul')
        row_headers=[x[0] for x in cursor.fetchall()]
        #row_headers.append('cashamount')
        print(row_headers)
        cursor.execute("SELECT * FROM templimitorder WHERE username = %s",[username])
        userlimitorder = cursor.fetchall();

        json_data = []
        all = {}
        i = 0;
        if len(userlimitorder) == 0:
            return 'none'
        else:
            for row in userlimitorder:
                as_list = list(row)

                as_list[5] = float(as_list[5])
                as_list[6] = str(as_list[6])
                #as_list.append(cashamount)
                row = tuple(as_list)
                row
                print(row)
                json_data.append(dict(zip(row_headers, row)))
                #json_data.append(dict(zip('cashamount',cashamount)))
                #print(row)
                i+=1
        
        
        all.update({'userlimitorder': json_data})
        #cursor.close()
        
        #stock_db.free_result()
        conn.commit() 
        cursor.close()
        #conn.close()
        #print(all)
        return json.dumps(all, cls=JSONEncoder)

@app.route('/deleteorder',methods = ['GET','POST'])
def deleteorder():
    conn = mysql.connection
    cursor = conn.cursor()
  
    # Output message if something goes wrong...
    msg = ''
    # Check if "username", "password" and "email" POST requests exist (user submitted form)
    if request.method == 'POST' :
        res = 0
        # Create variables for easy access
        id = request.form['id']
        username = request.form['username']
 
  #Check if account exists using MySQL
        cursor.execute('SELECT * FROM templimitorder WHERE username = %s', [username])
        account = cursor.fetchone()
        #print(account)
        if account:
                cursor.execute('DELETE FROM templimitorder WHERE id = %s',[id]);
                conn.commit()
                    
                msg = 'You have delete order successfully!'
                print(msg)
        else:
                    #cursor.execute('DELETE FROM holiday WHERE (holiday = %s)',(holidayname))
                msg = 'That order is not exsit!'
        return msg

sched = BackgroundScheduler(daemon=True)
sched.add_job(updatestock,'interval',seconds=3)
sched.start()

sched1 = BackgroundScheduler(daemon=True)
sched1.add_job(limitorder,'interval',seconds=3)
sched1.start()

if __name__ == "__main__":
    app.run(host ='0.0.0.0')
    #call update function with timer/scheduler
    
    

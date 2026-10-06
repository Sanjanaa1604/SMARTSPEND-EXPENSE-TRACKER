USE smartspend;
-- Demo password: SmartSpend123
-- This PBKDF2 hash is intentionally generated for this project and should be replaced if you change the demo password.
INSERT INTO users(name,email,password_hash) VALUES('Demo User','demo@smartspend.com','jMLvZDI26mCzFbh2KhRODg==:DQWPnzs5UovLFgZSGekkv5HSH0StHjUMzlL7ZLuPEyk=');
-- The application can create the demo account automatically with the Register screen.
-- To avoid shipping a misleading hard-coded password hash, register demo@smartspend.com / SmartSpend123 once.
SET @uid=(SELECT id FROM users WHERE email='demo@smartspend.com');
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method,notes) SELECT @uid,id,35000,'INCOME','Salary',CURDATE(),'Bank Transfer','Monthly salary' FROM categories WHERE name='Salary';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,3800,'EXPENSE','Food & dining',DATE_SUB(CURDATE(),INTERVAL 2 DAY),'UPI' FROM categories WHERE name='Food';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,1850,'EXPENSE','Daily transport',DATE_SUB(CURDATE(),INTERVAL 4 DAY),'UPI' FROM categories WHERE name='Transport';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,2400,'EXPENSE','Shopping',DATE_SUB(CURDATE(),INTERVAL 7 DAY),'Debit Card' FROM categories WHERE name='Shopping';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,1500,'EXPENSE','Course material',DATE_SUB(CURDATE(),INTERVAL 9 DAY),'UPI' FROM categories WHERE name='Education';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,1200,'EXPENSE','Movies and outings',DATE_SUB(CURDATE(),INTERVAL 12 DAY),'UPI' FROM categories WHERE name='Entertainment';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,2000,'EXPENSE','Electricity and bills',DATE_SUB(CURDATE(),INTERVAL 15 DAY),'UPI' FROM categories WHERE name='Bills';
INSERT INTO transactions(user_id,category_id,amount,type,description,transaction_date,payment_method) SELECT @uid,id,799,'EXPENSE','Subscriptions',DATE_SUB(CURDATE(),INTERVAL 18 DAY),'Credit Card' FROM categories WHERE name='Subscriptions';
INSERT INTO budgets(user_id,category_id,amount,month,year) SELECT @uid,id,5000,MONTH(CURDATE()),YEAR(CURDATE()) FROM categories WHERE name='Food';
INSERT INTO budgets(user_id,category_id,amount,month,year) SELECT @uid,id,3000,MONTH(CURDATE()),YEAR(CURDATE()) FROM categories WHERE name='Transport';
INSERT INTO budgets(user_id,category_id,amount,month,year) SELECT @uid,id,2000,MONTH(CURDATE()),YEAR(CURDATE()) FROM categories WHERE name='Entertainment';
INSERT INTO savings_goals(user_id,name,target_amount,current_amount,deadline,description) VALUES(@uid,'MacBook',80000,32000,DATE_ADD(CURDATE(),INTERVAL 10 MONTH),'Laptop savings goal');
INSERT INTO recurring_expenses(user_id,name,amount,category_id,frequency,next_payment_date) SELECT @uid,'Netflix',649,id,'Monthly',DATE_ADD(CURDATE(),INTERVAL 4 DAY) FROM categories WHERE name='Subscriptions';
INSERT INTO notifications(user_id,title,message,type) VALUES(@uid,'Welcome to SmartSpend','Your finance workspace is ready.','INFO'),(@uid,'Food budget reminder','Track your food spending to stay inside your monthly limit.','BUDGET');

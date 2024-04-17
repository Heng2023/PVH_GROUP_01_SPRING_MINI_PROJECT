
create table users(
                      user_id uuid  default uuid_generate_v4() primary key,
                      email varchar(200) not null UNIQUE,
                      password varchar(200) not null,
                      profile_image varchar(200) not null
);

create table otps(
                     otp_id uuid  default uuid_generate_v4()  primary key,
                     otp_code varchar(255) not null ,
                     issued_at timestamp not null,
                     expiration timestamp not null ,
                     verify boolean default false not null ,
                     user_id uuid not null ,
                     FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table categories(
                           category_id uuid  default uuid_generate_v4() primary key,
                           name varchar(200) not null,
                           description text ,
                           user_id uuid not null ,
                           FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE expenses(
                         expense_id  uuid  default uuid_generate_v4()  PRIMARY KEY,
                         amount decimal not null ,
                         description text ,
                         date date not null ,
                         user_id uuid not null,
                         category_id uuid not null,
                         FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (category_id) REFERENCES categories (category_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

SELECT * FROM categories WHERE user_id = 'ed04dd47-53a1-4d76-894d-97357dce0e6b'
LIMIT 1
OFFSET 2
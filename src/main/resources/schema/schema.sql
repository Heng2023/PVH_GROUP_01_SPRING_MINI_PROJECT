create table users(
    user_id uuid primary key,
    email varchar(200) not null UNIQUE,
    password varchar(200) not null,
    profile_image varchar(200) not null
);

create table otps(
    otp_id uuid primary key,
    otp_code int not null ,
    issued_at timestamp not null,
    expiration timestamp not null ,
    verify boolean default false not null ,
    user_id uuid not null ,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table categories(
    category_id uuid primary key,
    name varchar(200) not null,
    description text not null ,
    user_id uuid not null ,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE expenses(
    id  uuid PRIMARY KEY,
    amount int not null ,
    description text not null ,
    date date not null ,
    user_id uuid not null,
    category_id uuid not null,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (category_id) ON UPDATE CASCADE ON DELETE CASCADE
);
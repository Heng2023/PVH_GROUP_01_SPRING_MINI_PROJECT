create table users(
    user_id uuid primary key,
    email varchar(200) not null ,
    password varchar(200) not null,
    profile_image varchar(200) not null
);

create table Otps(
    otp_id uuid primary key,
    otp_code int not null ,
    issued_at timestamp not null,
    expiration interval default '2 minutes',
    verify boolean default false,
    user_id uuid not null ,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table categories(
    category_id uuid primary key,
    name varchar(200) not null,
    description varchar(200) not null ,
    user_id uuid,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Expenses
(
    id          uuid PRIMARY KEY,
    amount      INT          not null,
    description varchar(200) not null,
    date        date         not null,
    user_id     uuid,
    category_id uuid,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (category_id) ON UPDATE CASCADE ON DELETE CASCADE
);
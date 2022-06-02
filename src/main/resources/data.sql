-- 予め用意しているユーザーレコードを使う場合はハッシュ化されたパスワードを利用することが必須です(これはパスワードmikotoです)
-- またauthoritiesテーブルにもusernameに紐づくレコードが登録されていないとログインできません
INSERT INTO "users" ("username", "password","enabled") VALUES ('misaka', '{bcrypt}$2a$10$t.uzqGjte5WsosZO4wFV5OULSCqYJbn0qC5Lh0Uilj5hF.biixBoG', true);
insert into "authorities" ("username","authority") values ('misaka','level5');

-- 独自に定義したテーブルでJDBC認証を行う場合
INSERT INTO "myusers" ("username", "password","enabled") VALUES ('misaka', '{bcrypt}$2a$10$t.uzqGjte5WsosZO4wFV5OULSCqYJbn0qC5Lh0Uilj5hF.biixBoG',true);
insert into "myauthorities" ("username","role") values ('misaka','level5')
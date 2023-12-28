# java-filmorate
___
Template repository for Filmorate project.
<br/>
### Диаграмма базы данных
![Diagram](https://github.com/maximboltinov/java-filmorate/blob/readme-diagram/DB_filmorate.png)
<br/>
<br/>
### Краткое описание базы данных
* **users**: содержит данные пользователей  
* **user_friend**: содержит id пользователя подавшего запрос на добавление в друзья и пользователя, кому запрос адресован.
Поле **status_id** типа boolean, false - для нового запроса (по умолчанию), true - для подтвержденного.
При отклонении запроса - удаление записи.  
* **films**: содержит данные о фильме. В т.ч. внешний ключ для рейтинга (ограничения по просмотру).  
* **user_film_like**: таблица соответстия фильмов и пользователей, отметившие эти фильмы, как понравившиеся.  
* **film_genre**: таблица соответсвия фильмов и жанров (по условию один фильм может относиться к нескольким жанрам).  
* **rating_MPA** и genre: рейтинги ограничений на просмотр и жанры, соответственно.
  <br/>
  <br/>
### Примеры запросов
* Получить всех пользователей
```postgresql
SELECT *
FROM users;
```
* Получить пользователя по id
```postgresql
SELECT *
FROM users
WHERE user_id = 1;
```
* Получить список друзей для пользователя
```postgresql
SELECT *
FROM films
WHERE film_id IN (SELECT film_id
                  FROM user_film_like
                  WHERE user_id = 1);
```
* Получить список общих друзей
```posgresql
SELECT *
FROM users
WHERE user_id IN (SELECT user_accept_id
                  FROM user_friend
                  WHERE user_suggest_id = 2 AND status = true
                  UNION
                  SELECT user_suggest_id
                  FROM user_friend
                  WHERE user_accept_id = 2 AND status = true)
	  AND
	  user_id IN (SELECT user_accept_id
                  FROM user_friend
                  WHERE user_suggest_id = 1 AND status = true
                  UNION
                  SELECT user_suggest_id
                  FROM user_friend
                  WHERE user_accept_id = 1 AND status = true);
```
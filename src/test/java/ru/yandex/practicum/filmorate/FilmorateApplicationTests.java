package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.LocalDateAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FilmorateApplicationTests {
	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
			.create();
	static HttpClient httpClient;
	final String url = "http://localhost:8080";
	final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
	static URI uri;
	HttpRequest request;
	HttpResponse<String> response;

	@BeforeAll
	public static void setHttpClient() {
		httpClient = HttpClient.newHttpClient();
	}

	@Test
	void postingFilmCorrectFields() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("About")
				.releaseDate(LocalDate.of(2020, Month.MAY, 15))
				.duration(125)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(200, response.statusCode());
	}

	@Test
	void postingFilmEmptyName() throws IOException, InterruptedException {
		Film film = Film.builder()
				.description("About")
				.releaseDate(LocalDate.of(2020, Month.MAY, 15))
				.duration(125)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(400, response.statusCode());
	}

	@Test
	void postingFilmTooLongDescription() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" +
						"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" +
						"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" +
						"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789")
				.releaseDate(LocalDate.of(2020, Month.MAY, 15))
				.duration(125)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(500, response.statusCode());
	}

	@Test
	void postingFilm200Description() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" +
						"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" +
						"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" +
						"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789")
				.releaseDate(LocalDate.of(2020, Month.MAY, 15))
				.duration(125)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(200, response.statusCode());
	}

	@Test
	void postingFilmReleaseDateIsTooEarly() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("About")
				.releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
				.duration(125)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(500, response.statusCode());
	}

	@Test
	void postingFilmReleaseDateIsNormal() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("About")
				.releaseDate(LocalDate.of(1895, Month.DECEMBER, 28))
				.duration(125)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(200, response.statusCode());
	}

	@Test
	void postingFilmDurationIsNegative() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("About")
				.releaseDate(LocalDate.of(1995, Month.DECEMBER, 28))
				.duration(-1)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(400, response.statusCode());
	}

	@Test
	void postingFilmDurationIsZero() throws IOException, InterruptedException {
		Film film = Film.builder()
				.name("Avalon")
				.description("About")
				.releaseDate(LocalDate.of(1995, Month.DECEMBER, 28))
				.duration(0)
				.build();
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(500, response.statusCode());
	}

	@Test
	void postingFilmEmptyObject() throws IOException, InterruptedException {
		uri = URI.create(url + "/films");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson((Object) null))).build();
		response = httpClient.send(request, handler);
		assertEquals(400, response.statusCode());
	}

	@Test
	void postingUserCorrectFields() throws IOException, InterruptedException {
		User film = User.builder()
				.email("mail@mail.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2010, Month.MAY, 15))
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(200, response.statusCode());
	}

	@Test
	void postingUserIncorrectEmail() throws IOException, InterruptedException {
		User film = User.builder()
				.email("mailmail.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2010, Month.MAY, 15))
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(400, response.statusCode());
	}

	@Test
	void postingUserEmptyEmail() throws IOException, InterruptedException {
		User film = User.builder()
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2010, Month.MAY, 15))
				.email("")
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(500, response.statusCode());
	}

	@Test
	void postingUserIncorrectLogin() throws IOException, InterruptedException {
		User film = User.builder()
				.email("mail@mail.ru")
				.login("Login login")
				.name("Name")
				.birthday(LocalDate.of(2010, Month.MAY, 15))
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(500, response.statusCode());
	}

	@Test
	void postingUserIncorrectBirthday() throws IOException, InterruptedException {
		User film = User.builder()
				.email("mail@mail.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.now().plusDays(1))
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(500, response.statusCode());
	}

	@Test
	void postingUserEmptyBirthday() throws IOException, InterruptedException {
		User film = User.builder()
				.email("mail@mail.ru")
				.login("Login")
				.name("Name")
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(200, response.statusCode());
	}

	@Test
	void postingUserEmptyName() throws IOException, InterruptedException {
		User film = User.builder()
				.email("mail@mail.ru")
				.login("Login")
				.birthday(LocalDate.now())
				.build();
		uri = URI.create(url + "/users");
		request = HttpRequest.newBuilder().uri(uri).headers("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film))).build();
		response = httpClient.send(request, handler);
		assertEquals(200, response.statusCode());
	}
}
package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class, MpaDbStorage.class})
class FilmorateDBTests {

    @Autowired
    private final UserStorage userDbStorage;
    @Autowired
    private final FilmStorage filmDbStorage;
    @Autowired
    private final MpaStorage mpaDbStorage;

    @Test
    public void createUserTest() {
        User user = User.builder()
                .email("userfbgfdg@email.com")
                .name("Userdfeg")
                .login("loginurtyhb")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        user.setId(userDbStorage.findByName("Userdfeg").getId());
        User createdUser = userDbStorage.findByName("Userdfeg");
        Assertions.assertEquals(user, createdUser);
    }

    @Test
    public void getUserTest() {
        User user = User.builder()
                .email("user1fvr@email.com")
                .name("Userrgfersdv")
                .login("logingxfgjhg")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        long createdUserId = userDbStorage.findByName("Userrgfersdv").getId();
        User getUser = userDbStorage.findById(createdUserId);
        Assertions.assertEquals(getUser.getName(), "Userrgfersdv",
                "Actual user is not expected");
    }

    @Test
    public void getUsersTest() {
        User user1 = User.builder()
                .email("user2fbgtry@email.com")
                .name("User1")
                .login("login1kjhgxg")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user3trhytt@email.com")
                .name("User2")
                .login("login2ghfxfhgjh")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        Assertions.assertFalse(userDbStorage.findAll().isEmpty(),
                "Users list is empty");
    }

    @Test
    public void updateUserTest() {
        User user = User.builder()
                .email("userthrgv@email.com")
                .name("Userrstikjh")
                .login("loginukjhfdfx")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        User createdUser = userDbStorage.findByName("Userrstikjh");
        user.setId(createdUser.getId());
        user.setName("Updated User");
        userDbStorage.update(user);
        User updatedUser = userDbStorage.findById(user.getId());
        Assertions.assertEquals("Updated User", updatedUser.getName(),
                "Actual user is not updated");
    }

    @Test
    public void addFriendTest() {
        User user1 = User.builder()
                .email("user1rtgvtrd@email.com")
                .name("Usergnhgf")
                .login("login1dtjh")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2dgrtv@email.com")
                .name("Userfhftgjht")
                .login("login2kjhsdd")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long userId = userDbStorage.findByName("Usergnhgf").getId();
        long friendId = userDbStorage.findByName("Userfhftgjht").getId();
        userDbStorage.addFriend(userId, friendId);
        Assertions.assertTrue(userDbStorage.getFriends(userId).contains(userDbStorage.findById(friendId)),
                "Friend is not added");
    }

    @Test
    public void getFriendsTest() {
        User user = User.builder()
                .email("userrgvre@email.com")
                .name("Userfdsfghgbv")
                .login("loginthdfgx")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user1 = User.builder()
                .email("user1fgvfredf@email.com")
                .name("User1dsfgfnbvf")
                .login("login1dfjh")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2ver@email.com")
                .name("User2jhdfgvgf")
                .login("login2gjhfdf")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long userId = userDbStorage.findByName("Userfdsfghgbv").getId();
        long friend1Id = userDbStorage.findByName("User1dsfgfnbvf").getId();
        long friend2Id = userDbStorage.findByName("User2jhdfgvgf").getId();
        userDbStorage.addFriend(userId, friend1Id);
        userDbStorage.addFriend(userId, friend2Id);
        Set<User> expectedFriends = new HashSet<>();
        expectedFriends.add(userDbStorage.findById(friend1Id));
        expectedFriends.add(userDbStorage.findById(friend2Id));
        Set<User> actualFriends = userDbStorage.getFriends(userId);
        Assertions.assertEquals(expectedFriends, actualFriends,
                "Friends set is not expected");
    }

    @Test
    public void removeFriendTest() {
        User user = User.builder()
                .email("userergverd@email.com")
                .name("Userrerlkjgfj")
                .login("loginhtdhgd")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user1 = User.builder()
                .email("user1fvrtd@email.com")
                .name("User1eghtrhgrt")
                .login("login1yjhytg")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2ergdfv@email.com")
                .name("User2erukjhdftsee")
                .login("login2tjuyt")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long hasFriendsId = userDbStorage.findByName("Userrerlkjgfj").getId();
        long deletedFriendId = userDbStorage.findByName("User1eghtrhgrt").getId();
        long savedFriendId = userDbStorage.findByName("User2erukjhdftsee").getId();
        userDbStorage.addFriend(hasFriendsId, deletedFriendId);
        userDbStorage.addFriend(hasFriendsId, savedFriendId);
        Set<User> expectedFriends = new HashSet<>();
        expectedFriends.add(userDbStorage.findById(savedFriendId));
        userDbStorage.removeFriend(hasFriendsId, deletedFriendId);
        Set<User> actualFriends = userDbStorage.getFriends(hasFriendsId);
        Assertions.assertEquals(expectedFriends, actualFriends,
                "Friends set is not expected");
    }

    @Test
    public void removeAllFriendsTest() {
        User user = User.builder()
                .email("usergfrvedg@email.com")
                .name("Useruyguyhbnmj")
                .login("logingfjhgdf")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user1 = User.builder()
                .email("user1ergverdx@email.com")
                .name("User1xcgxfhgkhxc")
                .login("login1dfhb")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2dfvcrgv@email.com")
                .name("User2xfjghkgjhgxfj")
                .login("login2jhytfgd")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long hasFriendsId = userDbStorage.findByName("Useruyguyhbnmj").getId();
        long deletedFriend1Id = userDbStorage.findByName("User1xcgxfhgkhxc").getId();
        long deletedFriend2Id = userDbStorage.findByName("User2xfjghkgjhgxfj").getId();
        userDbStorage.addFriend(hasFriendsId, deletedFriend1Id);
        userDbStorage.addFriend(hasFriendsId, deletedFriend2Id);
        userDbStorage.removeAllFriends(hasFriendsId);
        Assertions.assertTrue(userDbStorage.getFriends(hasFriendsId).isEmpty(),
                "Friends set is not empty");
    }

    @Test
    public void getCommonFriendsTest() {
        User user = User.builder()
                .email("useruyjnhb@email.com")
                .name("Userjhgsgszaa")
                .login("loginfhgtrh")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user1 = User.builder()
                .email("user1thgf@email.com")
                .name("User1ghjlkjbv")
                .login("login1jhrdfg")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2fghnu@email.com")
                .name("User2kjhgfsgg")
                .login("login2kjhdfg")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long hasFriend1Id = userDbStorage.findByName("Userjhgsgszaa").getId();
        long friendId = userDbStorage.findByName("User1ghjlkjbv").getId();
        long hasFriend2Id = userDbStorage.findByName("User2kjhgfsgg").getId();
        userDbStorage.addFriend(hasFriend1Id, friendId);
        userDbStorage.addFriend(hasFriend2Id, friendId);
        Assertions.assertTrue(userDbStorage.getCommonFriends(hasFriend1Id, hasFriend2Id)
                        .contains(userDbStorage.findById(friendId)),
                "Set of common friends is not expected");
    }

    @Test
    public void createFilmTest() {
        Film film = Film.builder()
                .name("filmkjughjhfh")
                .description("filmDescription")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film);
        long createdFilmId = filmDbStorage.findByName("filmkjughjhfh").getId();
        Assertions.assertNotNull(filmDbStorage.findById(createdFilmId),
                "Film is not created");
    }

    @Test
    public void getFilmByIdTest() {
        Film film = Film.builder()
                .name("filmyijhdghgjh")
                .description("filmDescription")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(2))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film);
        long getFilmId = filmDbStorage.findByName("filmyijhdghgjh").getId();
        film.setId(getFilmId);
        Assertions.assertEquals(filmDbStorage.findById(getFilmId), film,
                "Actual film is not expected");
    }

    @Test
    public void getFilmsTest() {
        Film film1 = Film.builder()
                .name("film1")
                .description("film1description")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        Film film2 = Film.builder()
                .name("film2")
                .description("film2description")
                .duration(120)
                .mpa(mpaDbStorage.getMpaById(2))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        Assertions.assertFalse(filmDbStorage.findAll().isEmpty(),
                "Films are not found");
    }

    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .name("filmaykhjhgxgh")
                .description("filmdescription")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film);
        Film createdFilm = filmDbStorage.findByName("filmaykhjhgxgh");
        film.setId(createdFilm.getId());
        film.setName("UpdatedFilm");
        filmDbStorage.update(film);
        Film updatedFilm = filmDbStorage.findById(createdFilm.getId());
        Assertions.assertEquals("UpdatedFilm", updatedFilm.getName(),
                "Actual film is not expected");
    }

    @Test
    public void addLikeTest() {
        Film film = Film.builder()
                .name("filmjhgfffghj")
                .description("description")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film);
        long filmId = filmDbStorage.findByName("filmjhgfffghj").getId();
        User user = User.builder()
                .email("useryfgjhd@email.com")
                .name("Useroiuytrddvbn")
                .login("logindfhbgv")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user);
        long userId = userDbStorage.findByName("Useroiuytrddvbn").getId();
        filmDbStorage.addLike(filmId, userId);
        Assertions.assertFalse(filmDbStorage.getLikes(filmId).isEmpty(),
                "Like is not added");
    }

    @Test
    public void getLikesTest() {
        Film film = Film.builder()
                .name("filmdfghjlkjh")
                .description("filmdescription")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film);
        User user1 = User.builder()
                .email("user1dfgrtgjh@email.com")
                .name("User1sdkjhgscgj")
                .login("login1dxfgj")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2dfhgyh@email.com")
                .name("User2fdfgkmnbxc")
                .login("login2fxyhdjk")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long filmId = filmDbStorage.findByName("filmdfghjlkjh").getId();
        long user1Id = userDbStorage.findByName("User1sdkjhgscgj").getId();
        long user2Id = userDbStorage.findByName("User2fdfgkmnbxc").getId();
        filmDbStorage.addLike(filmId, user1Id);
        filmDbStorage.addLike(filmId, user2Id);
        Set<Long> expectedLikesSet = new HashSet<>(Set.of(user1Id, user2Id));
        Assertions.assertEquals(expectedLikesSet, filmDbStorage.getLikes(filmId),
                "Actual likes set is not expected");
    }

    @Test
    public void removeLikeTest() {
        Film film = Film.builder()
                .name("filmhkgjm")
                .description("filmdescription")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(film);
        User user1 = User.builder()
                .email("user1dfhntyjg@email.com")
                .name("User1khjhvnm")
                .login("login1hkjf")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user2dhbtfgjhb@email.com")
                .name("User2ccvcmmhb")
                .login("login2hkgmv")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        long filmId = filmDbStorage.findByName("filmhkgjm").getId();
        long user1Id = userDbStorage.findByName("User1khjhvnm").getId();
        long user2Id = userDbStorage.findByName("User2ccvcmmhb").getId();
        filmDbStorage.addLike(filmId, user1Id);
        filmDbStorage.addLike(filmId, user2Id);
        Set<Long> expectedLikesSet = new HashSet<>(Set.of(user1Id));
        filmDbStorage.removeLike(filmId, user2Id);
        Assertions.assertEquals(expectedLikesSet, filmDbStorage.getLikes(filmId),
                "Actual likes set is not expected");
    }

    @Test
    public void getPopularFilmsTest() {
        Film badFilm = Film.builder()
                .name("filmgggjhjhfb")
                .description("filmdescription")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        Film goodFilm = Film.builder()
                .name("film1fhjdfdbth")
                .description("film1description")
                .duration(100)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        Film bestFilm = Film.builder()
                .name("film2jhkgvfh")
                .description("film2description")
                .duration(120)
                .mpa(mpaDbStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2025, 4, 5))
                .build();
        filmDbStorage.create(badFilm);
        filmDbStorage.create(goodFilm);
        filmDbStorage.create(bestFilm);
        User user1 = User.builder()
                .email("userfghfcgyg@email.com")
                .name("Userdhjkhjhdxd")
                .login("loginvkhm")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user2 = User.builder()
                .email("user1jkiukhj@email.com")
                .name("User1dfhgjjmngx")
                .login("login1jhkjn")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        User user3 = User.builder()
                .email("user2ukiugfgc@email.com")
                .name("User2xfhjnbvxdgh")
                .login("login2hjij")
                .birthday(LocalDate.of(2024, 5, 5))
                .build();
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
        long film1Id = filmDbStorage.findByName("filmgggjhjhfb").getId();
        long film2Id = filmDbStorage.findByName("film1fhjdfdbth").getId();
        long film3Id = filmDbStorage.findByName("film2jhkgvfh").getId();
        long user1Id = userDbStorage.findByName("Userdhjkhjhdxd").getId();
        long user2Id = userDbStorage.findByName("User1dfhgjjmngx").getId();
        long user3Id = userDbStorage.findByName("User2xfhjnbvxdgh").getId();
        filmDbStorage.addLike(film2Id, user1Id);
        filmDbStorage.addLike(film2Id, user2Id);
        filmDbStorage.addLike(film3Id, user1Id);
        filmDbStorage.addLike(film3Id, user2Id);
        filmDbStorage.addLike(film3Id, user3Id);
        List<Film> expectedTopList = new ArrayList<>(List.of(bestFilm, goodFilm));
        Assertions.assertEquals(expectedTopList, filmDbStorage.getPopularFilms(2),
                "Actual toplist is not expected");
    }

}
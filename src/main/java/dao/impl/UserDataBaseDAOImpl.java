package dao.impl;

import dao.UserDAO;
import dto.UserDTO;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class UserDataBaseDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public @NotNull @Unmodifiable List<UserDTO> getAllUsers() {
        final List<User> users = entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();

        return users.stream().map(this::mapToUserDTO).toList();
    }

    @Override
    public boolean isPassportNumberAvailable(final String passportNumber) {
        final Long count = entityManager
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.passportNumber = :passportNumber", Long.class)
                .setParameter("passportNumber", passportNumber)
                .getSingleResult();

        return count == 0;
    }

    @Override
    public @NotNull Optional<UserDTO> getUserById(final int id) {
        final User user = entityManager.find(User.class, id);

        return ofNullable(user == null ? null : mapToUserDTO(user));
    }

    @Override
    public User getUserByPassportNumber(final String passportNumber) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.passportNumber = :passportNumber", User.class)
                .setParameter("passportNumber", passportNumber)
                .getSingleResult();
    }

    @Override
    public @NotNull UserDTO saveUser(final User user) {
        entityManager.persist(user);

        return mapToUserDTO(user);
    }

    @Override
    public boolean updateUser(final User user) {
        final User existingUser = entityManager.find(User.class, user.getId());
        existingUser.setUserName(user.getUserName());
        existingUser.setGender(user.getGender());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        existingUser.setPassportNumber(user.getPassportNumber());

        return true;
    }

    @Override
    public void deleteUser(final long id) {
        final User user = entityManager.find(User.class, id);

        entityManager.remove(user);
    }

    @Contract("_ -> new")
    private @NotNull UserDTO mapToUserDTO(final @NotNull User user) {
        return new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getGender(),
                user.getDateOfBirth(),
                user.getPassportNumber()
        );
    }
}

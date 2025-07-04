package dao.impl;

import dao.UserDAO;
import dto.UserDTO;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


@Repository
public final class UserDataBaseDAOImpl implements UserDAO {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public UserDataBaseDAOImpl(final EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public @NotNull @Unmodifiable List<UserDTO> getAllUsers() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final List<User> users = entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();

        entityManager.close();

        return users.stream().map(this::mapToUserDTO).toList();
    }

    @Override
    public boolean isPassportNumberAvailable(final String passportNumber) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final Long count = entityManager
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.passportNumber = :passportNumber", Long.class)
                .setParameter("passportNumber", passportNumber)
                .getSingleResult();

        entityManager.close();

        return count == 0;
    }

    @Override
    public @NotNull Optional<UserDTO> getUserById(final int id) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final User user = entityManager.find(User.class, id);

        entityManager.close();

        return ofNullable( user == null ? null : mapToUserDTO(user));
    }

    @Override
    public User getUserByPassportNumber(final String passportNumber) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final User user = entityManager
                .createQuery("SELECT u FROM User u WHERE u.passportNumber = :passportNumber", User.class)
                .setParameter("passportNumber", passportNumber)
                .getSingleResult();

        entityManager.close();

        return user;
    }

    @Override
    public @NotNull UserDTO saveUser(final User user) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        entityManager.close();
        return mapToUserDTO(user);
    }

    @Override
    public boolean updateUser(final User user) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        final User existingUser = entityManager.find(User.class, user.getId());

        existingUser.setUserName(user.getUserName());
        existingUser.setGender(user.getGender());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        existingUser.setPassportNumber(user.getPassportNumber());

        transaction.commit();
        entityManager.close();
        return true;
    }

    @Override
    public void deleteUser(final long id) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        final User user = entityManager.find(User.class, id);
        entityManager.remove(user);
        transaction.commit();
        entityManager.close();
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

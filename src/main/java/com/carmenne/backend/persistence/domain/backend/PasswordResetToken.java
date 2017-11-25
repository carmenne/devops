package com.carmenne.backend.persistence.domain.backend;

import com.carmenne.backend.persistence.converters.LocalDateTimeAttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class PasswordResetToken implements Serializable {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetToken.class);

    /** The Serial Version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    private final static int DEFAULT_TOKEN_LENGTH_IN_MINUTES = 120;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    /**
     * The default constructor
     */
    public PasswordResetToken() {}


    /**
     * Full constructor
     * @param user
     * @param token
     * @param creationDate
     * @param expirationInMinutes, defaults to the default value if 0
     */
    public PasswordResetToken(User user,
                              String token,
                              LocalDateTime creationDate,
                              int expirationInMinutes) {

        if (user == null || token == null || creationDate == null) {
            throw new IllegalArgumentException("User, toker or creation date" +
                    "cannot be null");
        }

        if (expirationInMinutes == 0) {
            LOG.warn("Expiration in minutes is 0. Assigning the default value {}",
                    DEFAULT_TOKEN_LENGTH_IN_MINUTES);
            expirationInMinutes = DEFAULT_TOKEN_LENGTH_IN_MINUTES;
        }

        this.token = token;
        this.user = user;
        this.expiryDate = creationDate.plusMinutes(expirationInMinutes);

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetToken that = (PasswordResetToken) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

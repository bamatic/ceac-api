package services.impl1;

import contracts.ILogger;
import contracts.IUserAuthenticator;
import contracts.IUserRepository;
import entities.User;
import services.impl1.jwt.PasswordHashService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AuthenticationService implements IUserAuthenticator {

    private IUserRepository userRepo;

    public AuthenticationService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public long verify(String email, String password) {
        User user = this.userRepo.getUserAndPassword(email, password);
        if (user == null) {
            return 0;
        }
        return user.getId();
    }
}

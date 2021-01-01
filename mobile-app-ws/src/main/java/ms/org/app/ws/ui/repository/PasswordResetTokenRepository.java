package ms.org.app.ws.ui.repository;

import org.springframework.data.repository.CrudRepository;

import ms.org.app.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
	PasswordResetTokenEntity findByToken(String token);

}

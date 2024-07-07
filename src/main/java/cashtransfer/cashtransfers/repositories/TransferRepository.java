package cashtransfer.cashtransfers.repositories;

import cashtransfer.cashtransfers.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Abubakir Dev
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer,Long> {
    Optional<Transfer> findByConfirmationCode(String transactionId);
}

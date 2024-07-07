package cashtransfer.cashtransfers.repositories;

import cashtransfer.cashtransfers.entities.CashRegister;
import cashtransfer.cashtransfers.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister,Long> {
    @Query("select new cashtransfer.cashtransfers.entities.CashRegister(c.id, c.name, c.balance,c.urlImage ,c.currency) " +
            "from CashRegister c " +
            "where (:name is null or c.name like %:name%) " +
            "and (:balance is null or c.balance = :balance)")
    List<CashRegister> findByCriteria(@Param("name") String name, @Param("balance") Double balance);

    boolean existsByUser(User user);

    @Query("select new cashtransfer.cashtransfers.entities.CashRegister(c.id,c.name,c.balance,c.currency,c.urlImage) from CashRegister c")
    Page<CashRegister> findAllCashes(Pageable pageable);

    List<CashRegister> findByUser(User currentUser);
}
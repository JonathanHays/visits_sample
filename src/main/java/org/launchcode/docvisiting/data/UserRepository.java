package org.launchcode.docvisiting.data;
// <!-- 
// created by: Jonathan Hays
//  -->

import org.launchcode.docvisiting.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    User findByUsername(String username);

    User findByEmailContainingIgnoreCaseOrderByFirstNameAsc(String email);

    List<User> findByLoginTypeOrderByFirstNameAsc(String type);

    Page<User> findByLoginTypeNot(String type, Pageable pageable);
    User findByEmailIgnoreCase(String email);

    Page<User> findByLoginTypeOrderByFirstNameAsc(String visitorType, PageRequest pageRequest);

    @Query("SELECT u FROM User u WHERE (u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm% OR u.email LIKE %:searchTerm%) AND u.loginType != :visitorType")
    Page<User> findByLoginTypeNotAndLastNameContainingOrFirstNameContainingOrEmailContainingIgnoreCaseOrderByFirstNameAsc(@Param("visitorType") String visitorType, @Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm% OR u.email LIKE %:searchTerm%) AND u.loginType = :visitorType")
    Page<User> findByLoginTypeAndLastNameContainingOrFirstNameContainingOrEmailContainingIgnoreCaseOrderByFirstNameAsc(@Param("visitorType") String visitorType, @Param("searchTerm") String searchTerm, Pageable pageable);

}

package com.finalproject.airport.approval.repository;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Integer> {


}

/*import org.springframework.data.jpa.repository.JpaRepository;
import com.finalproject.airport.approval.entity.ApprovalEntity;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {
    // 다른 커스텀 메서드를 추가할 수 있습니다.
}
*/

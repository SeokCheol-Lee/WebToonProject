package com.example.webtoonproject.domain;

import com.example.webtoonproject.exception.CashException;
import com.example.webtoonproject.type.ErrorCode;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @OneToOne
    @JoinTable(name = "ACCOUNT_USER",
        joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
        inverseJoinColumns= @JoinColumn(name = "USERTABLE_ID"))
    private User accountUser;
    private String accountNumber;
    private Long balance;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addBalance(Long amount){
        balance += amount;
    }

    public void useBalance(Long amount){
        if(amount > balance){
            throw new CashException(ErrorCode.AMOUNT_EXCEED_CASH);
        }
        balance -= amount;
    }

}

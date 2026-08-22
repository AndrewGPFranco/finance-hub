package com.agpf.finance.hub.models.configurations;

import com.agpf.finance.hub.models.configurations.converter.YearMonthDateConverter;
import com.agpf.finance.hub.models.subdomain.Subdomain;
import com.agpf.finance.hub.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.YearMonth;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
public abstract class UserScopedConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subdomain_id", nullable = false)
    protected Subdomain subdomain;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Column(name = "date_to_use", nullable = false)
    @Convert(converter = YearMonthDateConverter.class)
    protected YearMonth dateToUse;

    @CreationTimestamp
    protected Instant createdAt;

    @UpdateTimestamp
    protected Instant updatedAt;
}

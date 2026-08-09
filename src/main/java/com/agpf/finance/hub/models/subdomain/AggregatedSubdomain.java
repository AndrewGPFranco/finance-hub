package com.agpf.finance.hub.models.subdomain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "subdomain_aggregates",
        indexes = {
                @Index(name = "idx_subdomain_target", columnList = "subdomain_id"),
                @Index(name = "idx_subdomain_aggregate", columnList = "subdomain_aggregate_id"),
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subdomain_target_and_aggregate",
                        columnNames = {"subdomain_id", "subdomain_aggregate_id"}
                )
        }
)
public class AggregatedSubdomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subdomain_id", nullable = false)
    private Subdomain subdomainTarget;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subdomain_aggregate_id", nullable = false)
    private Subdomain subdomainAggregate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

}

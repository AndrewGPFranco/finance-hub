package com.agpf.finance.hub.models.subdomain;

import com.agpf.finance.hub.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "subdomains",
        uniqueConstraints = {
                @UniqueConstraint(name = "name_user_unique", columnNames = {"name", "user_id"})
        },
        indexes = {
                @Index(name = "idx_name_user", columnList = "user_id, name")
        }
)
public class Subdomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "É necessário informar um nome ao subdomínio.")
    private String name;

    @Column(name = "url_photo")
    private String urlPhoto;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subdomain")
    private Set<SubdomainMember> subdomainMembers;

}

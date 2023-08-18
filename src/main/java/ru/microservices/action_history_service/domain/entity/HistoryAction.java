package ru.microservices.action_history_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("history_action")
public class HistoryAction {
    @Id
    private Long id;
    @Column("service_instance_id")
    private Long serviceInstanceId; // название сервиса (role_service, user_service, etc)

    @Column("entity")
    private String entity; // название класса (role, permission, user, token, etc)
    @Column("field")
    private String field; // название поля класса (role.name, user.age etc)
    @Column("value")
    private String value;
    @Column("updated_at")
    private String updatedAt;
}

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
    private Long serviceInstanceId;
    @Column("entity")
    private String entity;
    @Column("field")
    private String field;
    @Column("value")
    private String value;
    @Column("updated_at")
    private String updatedAt;
}

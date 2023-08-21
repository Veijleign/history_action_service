package ru.microservices.action_history_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    @Column("entity_id")
    private String entityId;
    @Column("entity_name")
    private String entityName;
    @Column("field_name")
    private String fieldName;
    @Column("past_value")
    private String pastValue;
    @Column("new_value")
    private String newValue;
    @Column("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

package ru.microservices.action_history_service.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.repository.HistoryActionDao;

import java.util.List;

@Service
@AllArgsConstructor
public class HistoryActionService {

    private final HistoryActionDao historyActionDao;

    public List<HistoryAction> getHistoryActionByService(Long id) {



    }

    public HistoryAction getLastHistoryActionByService(Long id) {



    }

}

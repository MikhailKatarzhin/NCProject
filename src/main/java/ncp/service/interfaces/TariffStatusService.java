package ncp.service.interfaces;

import ncp.model.TariffStatus;

import java.util.List;

public interface TariffStatusService {

    TariffStatus getByName(String name);
    TariffStatus getById(Long id);
    List<TariffStatus> getAll();
}

package ncp.service.interfaces;

import ncp.model.TransmitterStatus;

import java.util.List;

public interface TransmitterStatusService {

    TransmitterStatus getByName(String name);

    TransmitterStatus getById(Long id);

    List<TransmitterStatus> getAll();
}

package ncp.service.implementations;

import ncp.model.TransmitterStatus;
import ncp.repository.TransmitterStatusRepository;
import ncp.service.interfaces.TransmitterStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransmitterStatusServiceImp implements TransmitterStatusService {
    private final TransmitterStatusRepository transmitterStatusRepository;

    @Autowired
    public TransmitterStatusServiceImp(TransmitterStatusRepository transmitterStatusRepository){
        this.transmitterStatusRepository = transmitterStatusRepository;
    }

    public TransmitterStatus getByName(String name){
        return transmitterStatusRepository.getByName(name);
    }
    public TransmitterStatus getById(Long id){
        return transmitterStatusRepository.getById(id);
    }
    public List<TransmitterStatus> getAll(){
        return transmitterStatusRepository.findAll();
    }
}
